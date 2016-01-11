(ns patrn.helper)

(defn fn-arity  
  "Arity of function f or nil when not a function."
  [f] 
  (when (instance? clojure.lang.AFunction f)
    (-> f class .getDeclaredMethods first .getParameterTypes alength)))
