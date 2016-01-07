(ns patrn.core-test
  (:use midje.sweet)
  (:use [patrn.core]))

(facts "about `repeat-if-nonsequential`"
       (fact "every output is sequential."
             (sequential? (repeat-if-nonsequential 10))      => truthy
             (sequential? (repeat-if-nonsequential [1 2 3])) => truthy))

(def m {:a 1 :b 2 :c 3})

(facts "about `map-vals`"
       (fact "output has same keys as input map."
             (keys m) => (keys (map-vals inc m))))

(facts "about `inside-out`"
       (fact "output is always sequential."
             ;(take-while not-any-nil-vals? (inside-out (map-vals repeat-if-nonsequential m))) => sequential?
))

(facts "about `not-any-nil-vals?`"
       (fact "true when map contains no nil values."
             (not-any-nil-vals? m)        => truthy)
       (fact "false when map contains nil values."
             (not-any-nil-vals? {:a nil}) => falsey))

;(facts "about `bind`"
;  (facts "bound patterns have no nil values."))

(facts "about `stream`"
       (fact "does nothing to sequence of literals."
             (stream [1 2 3])     => '(1 2 3))
       (fact "flattens nested sequences/vectors."
             (stream [1 [2 [3]]]) => '(1 2 3))
       (fact "embeds function results."
             (stream [1 #(inc 1) 3]) => '(1 2 3)))
