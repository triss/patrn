(ns patrn.core-test
  (:use midje.sweet)
  (:use [patrn.core]))

(facts "about `untangle`"
       (fact "untangles vectors" 
             (untangle [[:a :b :c] [[1 2 3] [4 5 6]]])
             => {:a (1 4), :b (2 5), :c (3 6)})
       (fact "untangles sets" 
             (untangle [#{:a :b :c} [#{1 2 3} #{4 5 6}]])
             => {:a (1 4), :b (2 5), :c (3 6)})
       (fact "keeps only enough values for each specified key."
             (untangle [[:a :b] [[1 2 3] [4 5 6]]])
             => {:a (1 4), :b (2 5)}))

(def map-of-seqs {:a [1 2 3] :b [1 2 3] :c [1 2 3]})
(def map-of-seqs-a-short {:a [1 2] :b [1 2 3] :c [1 2 3]})

(facts "about `flop-map`"
       (fact "output is always sequential."
             (flop-map map-of-seqs)             => sequential?)
       (fact "contains same number of items as shortest seq."
             (count (flop-map map-of-seqs))         => 3
             (count (flop-map map-of-seqs-a-short)) => 2)
       (fact "first out contains first values of sequences."
             (first (flop-map map-of-seqs))     => {:a 1 :b 1 :c 1}))

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
