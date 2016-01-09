(ns patrn.core
  (:require [flatland.ordered.map :refer [ordered-map]]
            [patrn.event :as event]))

(defn fn-arity  
  "Arity of function f."
  [f] ^{:pre [(instance? clojure.lang.AFunction f)]}
  (-> f class .getDeclaredMethods first .getParameterTypes alength))

(defn zero-arity-fn? 
  "True when f is a zero arity function, false otherwise."
  [f] (zero? (fn-arity f)))

(defn patrn->seq
  "Converts a patrn to a sequence of values.
  Operates like flatten except when a zero arity function is encountered. In
  which case it's value is resolved and then flattened in to the sequence just
  like any other."
  [coll]
  (lazy-seq
    (when-let [[x & more] (seq coll)] 
      (cond 
        (zero-arity-fn? x) (patrn->seq (cons (x) more))
        (sequential? x)    (concat (patrn->seq x) (patrn->seq more))
        :else              (cons x (patrn->seq more))))))

;;;; Functions that allow us to treat all values as sequences/streams.

(defn cycle-or-repeat
  "Repeat item if non-sequential, cycle it otherwise."
  [v] (if (sequential? v) (cycle v) (repeat v)))

(defn repeat-if-nonsequential 
  "Repeat item if its not sequential otherwise return it unmodified."
  [v] (if (sequential? v) v (repeat v))) 

;;;; Map manipulation

(defn map-vals
  "Map over values in m."
  [f m] (zipmap (keys m) (map f (vals m))))

(defn flop-map 
  "Turns a map of sequences in to a sequence of maps."
  [m] (cons (map-vals first m) (lazy-seq (flop-map (map-vals rest m)))))

(defn not-any-nil-vals? 
  "True when m doesn't contain any nil values."
  [m] (not-any? nil? (vals m)))

;;;; Event sequence creation

(defn bind 
  "Combines several value patrns in to one event sequence."
  [pattern]
  (->> pattern
       (map-vals (comp patrn repeat-if-nonsequential))
       flop-map
       (take-while not-any-nil-vals?)))

;;;; bicycle support fns

(defn- patrn-length 
  [v] (if (sequential? v) (count (patrn v)) 1)) 

(defn- longest-patrn-length
  [m] (apply max (map patrn-length (vals m)))) 

(defn- cycle-vals-and-take-for-longest
  [m] (let [longest (longest-patrn-length m)] 
                (map-vals #(take longest (cycle-or-repeat %)) m)))

(def bicycle (comp bind cycle-vals-and-take-for-longest))

;;;; helper patterns

(defn rotate 
  "Rotates a sequence by n."
  [n coll] (concat (drop n coll) (take n coll)))
