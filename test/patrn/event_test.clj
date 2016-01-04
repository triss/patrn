(ns patrn.event-test
  (:require [clojure.inspector :refer [atom?]])
  (:use midje.sweet)
  (:use [patrn.event]))

(facts "about `default-event`"
  (fact "has at least the :amp, :freq, :note and :midi-note and :length keys"
    (keys default-event) => (contains [:amp :freq :note :midi-note :length] 
                                      :in-any-order :gaps-ok))
  (fact "has same keys after value resolution"
    (keys (resolve-values default-event)) => (contains [:amp :freq :note
                                                        :midi-note :length] 
                                                       :in-any-order :gaps-ok)))

(def state (atom 10))

(facts "about `resolve-value`"
  (fact "functions are performed against event and associated in to it."
    (resolve-value {:x 10} [:y #(+ 10 (:x %))]) 
    => {:x 10 :y 20})
  (fact "atom's resolve to values."
    (resolve-value {} [:a state]) 
    => {:a 10})
  (fact "values are left as values."
    (resolve-value {:x 10} [:x 10]) => {:x 10}))

(facts "about `resolve-values`"
  (fact "a resolved event contains no atoms or functions."
    (not-any? (some-fn fn? atom?) (resolve-values (assoc default-event :a state))) 
    => truthy))
