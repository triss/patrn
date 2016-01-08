(ns patrn.core
  (:require [flatland.ordered.map :refer [ordered-map]]
            [patrn.event :as event]))

;;;; Functions that allow us to treat all values as sequences/streams.

(defn cycle-or-repeat
  "Repeat item if non-sequential, cycle it otherwise."
  [v] (if (sequential? v) (cycle v) (repeat v)))

(defn repeat-if-nonsequential 
  "Repeat item if its not sequential."
  [v] (if (sequential? v) v (repeat v))) 

(defn stream 
  [coll]
  (lazy-seq
    (when-let [[x & more] (seq coll)] 
      (cond 
        (fn? x)         (stream (cons (x) more))
        (sequential? x) (concat (stream x) (stream more))
        :else           (cons x (stream more))))))

;;;; Map manipulation

(defn map-vals
  "map over values in map."
  [f m] (zipmap (keys m) (map f (vals m))))

(defn inside-out 
  "Turns a map of sequences in to a sequence of maps."
  [m] (cons (map-vals first m) (lazy-seq (inside-out (map-vals rest m)))))

(defn not-any-nil-vals? 
  "True when m doesn't contain any nil values."
  [m] (not-any? nil? (vals m)))

;;;; Event sequence creation

(defn- assoc-merge 
  "Merges map b in to ordered maps without losing order of a."
  [a b] (reduce (partial apply assoc) a b))

(defn bind 
  "bind combines several value streams in to one event stream."
  [pattern]
  (->> pattern
       (map-vals (comp stream repeat-if-nonsequential))
       inside-out
       (take-while not-any-nil-vals?)))

(defn rotate 
  [n coll] (concat (drop n coll) (take n coll)))
