(ns patrn.core-test
  (:use midje.sweet)
  (:use [patrn.core]))

(facts "about `zero-arity-fn?`"
       (fact "true when f has arity of 0."
             (zero-arity-fn? #(inc 10))  => true
             (zero-arity-fn? (fn [] 20)) => true)
       (fact "false when f has arity > 0."
             (zero-arity-fn? #(inc %))   => false
             (zero-arity-fn? #(+ %1 %2)) => false))

(facts "about `repeat-if-nonsequential`"
       (fact "every output is sequential."
             (sequential? (repeat-if-nonsequential 10))      => truthy
             (sequential? (repeat-if-nonsequential [1 2 3])) => truthy))

(def m {:a 1 :b 2 :c 3})

(facts "about `map-vals`"
       (fact "output has same keys as input map."
             (keys m) => (keys (map-vals inc m))))

(facts "about `flop-map`"
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

(facts "about `patrn->seq`"
       (fact "does nothing to sequence of atomic values."
             (patrn->seq [1 2 3])        => '(1 2 3))
       (fact "flattens nested sequences/vectors."
             (patrn->seq [1 [2 [3]]])    => '(1 2 3))
       (fact "embeds function results."
             (patrn->seq [1 #(inc 1) 3]) => '(1 2 3)))

(facts "SC -> patrn analougues"
       (fact "Pseq([1,2,3])"
             (patrn->seq [1 2 3])            
             => '(1 2 3))

       (fact "Pseq([1,2,3],4)"
             (patrn->seq (repeat 4 [1 2 3])) 
             => '(1 2 3 1 2 3 1 2 3 1 2 3))

       (fact "Pseq([1,2,3],inf) 
             (take 20) used since infinite sequences make computers explode!"
             (take 20 (cycle [1 2 3])) 
             => (contains [1 2 3 1 2 3]))

       (fact "Pser([1,2,3],4)"
             (take 4 (cycle [1 2 3])) 
             => '(1 2 3 1))

       (fact "Pseries(1,3,9)"
             (take 9 (range 1 100 3)) 
             => '(1 4 7 10 13 16 19 22 25)))
