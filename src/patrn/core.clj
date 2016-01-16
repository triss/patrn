(ns patrn.core
  (:require [clojure.walk :refer [prewalk]] 
            [patrn.helper :refer [fn-arity]]))

;; TODO: is this a sensible way to embed vals for multichannel expansion etc?
(defn dont-embed? 
  [x] (and (sequential? x) (= (first x) :dont-embed)))

(defn grandchildren? 
  [x] (if (sequential? x) (some sequential? x) false))

(defn map-sequential-leaves 
  [f xs]
  (prewalk #(cond 
              (grandchildren? %) %
              (sequential? %)    (f %)
              :else              %)
           xs))

(def dont-embed-inner-most-seqs
  (partial map-sequential-leaves #(cons :dont-embed %)))

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
       (dont-embed? x)    (cons (drop 1 x) (patrn->seq more))
       (sequential? x)    (concat (patrn->seq x) (patrn->seq more))
       :else              (cons x (patrn->seq more))))))

(defn spatrn->seq 
  [coll]
  (lazy-seq
   (when-let [[x & more] (seq coll)]
     (cond 
       (= 0 (fn-arity x))    
       (spatrn->seq x)

       (and (sequential? x) (grandchildren? x)) 
       (concat (spatrn->seq x) (spatrn->seq more))

       :else                                    
       (cons x (spatrn->seq more))))))

(defn flop-map 
  "Turns a map of sequences in to a sequence of maps containing one of every
  value in its sequences until one of the contained sequences finishes."
  [m] (lazy-seq 
       (let [mapm #(zipmap (keys m) (map % (vals m)))] 
         (when (every? seq (vals m)) 
           (cons (mapm first) (flop-map (mapm rest)))))))

(defn untangle
  "Takes a sequence of keys and a sequence of sequences and produces binding
  with keys and sequence values associated."
  [ks xs] 
  (let [maps (map #(zipmap ks %) xs)] 
    (zipmap ks (for [k ks] (map k maps)))))

(defn untangle-bindings
  [bindings]
  (let [tangles (into {} (filter #(coll? (first %)) bindings))] 
    (if (seq tangles) 
      (apply merge (apply dissoc bindings (keys tangles)) 
             (into {} (for [[ks p] tangles] (untangle ks (spatrn->seq p)))))
      bindings)))

(defn bind-patrns
  "Combines map of patrns in to one sequence of maps."
  [bindings]
  (->> (vals bindings)
       (map #(if-not (sequential? %) (repeat %) %))
       (map patrn->seq)
       (zipmap (keys bindings))
       flop-map))

(def bind 
  (comp bind-patrns untangle-bindings))

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
