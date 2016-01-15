(ns patrn.core
  (:require [patrn.helper :refer [fn-arity]]))

(defn patrn->seq
  "Converts a patrn to a sequence of values.
  Operates like flatten except when a zero arity function is encountered. In
  which case it's value is resolved and then flattened in to the sequence just
  like any other."
  [coll]
  (lazy-seq 
   (when-let [[x & more] (seq coll)] 
     (cond 
       (= 0 (fn-arity x)) (patrn->seq (cons (x) more))
       (sequential? x)    (concat (patrn->seq x) (patrn->seq more))
       :else              (cons x (patrn->seq more))))))

(defn flop-map 
  "Turns a map of sequences in to a sequence of maps containing one of every
  value in its sequences until one of the contained sequences finishes."
  [m] (lazy-seq 
       (let [mapm #(zipmap (keys m) (map % (vals m)))] 
         (when (every? seq (vals m)) 
           (cons (mapm first) (flop-map (mapm rest)))))))

(defn untangle
  [[ks xs]] 
  (let [maps (map #(zipmap ks %) xs)] 
    (zipmap ks (for [k ks] (map k maps)))))

(defn untangle-bindings
  [bindings]
  (merge bindings 
         (->> bindings 
              (filter #(coll? first %))
              (map untangle))))

(defn bind 
  "Combines map of patrns in to one sequence of maps."
  [bindings]
  (->> (vals bindings)
       (map #(if (sequential? %) % (repeat %)))
       (map patrn->seq)
       (zipmap (keys bindings))
       (untangle-bindings)
       flop-map))

(defn cycle-vals 
  [m] (zipmap (keys m) (map (comp cycle vector) (vals m))))

(def bicycle (comp bind cycle-vals))

(defn time-stamp-events
  "Calculate time-stamp for each event in sequence if not already specified."
  [events]
  (->> (reductions (fn [prev-ts {:keys [time-stamp duration] 
                                 :or   {duration 1}}] 
                     (or time-stamp (+ prev-ts duration)))
                   0 events)
       (map #(assoc %1 :time-stamp %2) events)))

;;;; helper patterns

(defn rotate 
  "Rotates a sequence by n."
  [n coll] (concat (drop n coll) (take n coll)))
