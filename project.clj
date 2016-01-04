(defproject patrn "0.0.1-SNAPSHOT"
  :description "Library containing idiomatic Clojure implementation of
               SuperCollider's pattern, stream and event system."
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojars.triss/music  "0.1.0-SNAPSHOT"]
                 [org.flatland/ordered "1.5.3"]
                 [overtone "0.9.1"]]
  :profiles {:dev {:dependencies [[midje "1.5.1"]]}})
  
