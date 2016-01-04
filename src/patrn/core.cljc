(ns patrn.core
  (:require [flatland.ordered.map :refer [ordered-map]]
            [patrn.event :as event]))

(defn repeat-if-nonsequential 
  "Repeat item if its not sequential."
  [item] 
  (if-not (sequential? item)
    (repeat item)
    item))

(defn map-vals
  "map over values in map."
  [f m] (zipmap (keys m) (map f (vals m))))

(defn inside-out 
  "Turns a map of sequences in to a sequence of maps."
  [m] (cons (map-vals first m) (lazy-seq (inside-out (map-vals rest m)))))

(defn not-any-nil-vals? 
  "True when m doesn't contain any nil values."
  [m] (not-any? nil? (vals m)))

(defn bind 
  "bind combines several value streams in to one event stream."
  [pattern]
  (->> pattern
       (map-vals repeat-if-nonsequential)
       inside-out
       (take-while not-any-nil-vals?)))

(defn inst-param-names 
  "Extracts list of paramater names from Overtone instrument."
  [inst] (map (comp keyword :name) (:params inst)))


(def rep-cat 
  "Repeats pattern n times."
  (comp (partial apply concat) repeat))

(defn rotate 
  [n coll]
  (concat (drop n coll) (take n coll)))

(rotate 1 [1 2 3])
