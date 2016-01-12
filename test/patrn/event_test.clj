(ns patrn.event-test
  (:require [clojure.inspector :refer [atom?]])
  (:use midje.sweet)
  (:use [patrn.event]))

(facts "about `default-event`"
       (fact "has at least the :amp, :freq, :note and :midi-note and :length keys"
             (keys default-event) => (contains [:amp :freq :note :midi-note :length] 
                                               :in-any-order :gaps-ok))
       (fact "has same keys after value derivation."
             (keys (derive-vals default-event)) => (contains [:amp :freq :note
                                                              :midi-note :length] 
                                                             :in-any-order :gaps-ok)))

(def state (atom 10))

(facts "about `derive-val`"
       (fact "functions are performed against event and associated in to it."
             (derive-val {:x 10} [:y #(+ 10 (:x %))]) 
             => {:x 10 :y 20})
       (fact "atom's resolve to values."
             (derive-val {} [:a state]) 
             => {:a 10})
       (fact "values are left as values."
             (derive-val {:x 10} [:x 10]) => {:x 10}))

(facts "about `derive-vals`"
       (fact "a derived event contains no atoms or functions."
             (not-any? (some-fn fn? atom?) (derive-vals (assoc default-event :a state))) 
             => truthy))
