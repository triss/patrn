(ns patrn.helper-test
  (:use midje.sweet)
  (:use [patrn.helper]))

(facts "about `fn-arity`"
       (fact "reports arity of function."
             (fn-arity #(inc 10))     => 0
             (fn-arity (fn [] 20))    => 0
             (fn-arity #(inc %))      => 1
             (fn-arity #(+ %1 %2))    => 2
             (fn-arity #(+ %1 %2 %3)) => 3))
