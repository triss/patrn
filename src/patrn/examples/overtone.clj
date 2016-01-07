(ns simple-patrn.core
  (:require [clojure.pprint :refer [pprint]]
            [patrn.core :as p]
            [patrn.overtone :refer [play]])
  (:use [overtone.core]))

(defn pn [x] (pprint x) x)

(comment
  (connect-external-server "192.168.0.10" 57110)

  (definst smooth 
    [freq 440 amp 0.5 length 1]
    (* (sin-osc freq) 
       (env-gen (perc 0.01 length amp) 1 1 0 FREE)))

  (let [freqs (range 991 10000 5)] 
    (doseq [x (range 32) :let [freq (nth x freqs)]]
      (smooth freq (/ 1 x) (inc (rand)))))

  (def m (metronome 128))

  (defn sixteenths 
    [coll] (map #(/ % 4) coll))

  (defn val-length 
    [v] (if (sequential? v) (count v) 1)) 

  (defn longest-val-length
    [m] (apply max (map val-length (vals m)))) (defn cycle-vals
                                                 [m] (p/map-vals (comp (partial take (longest-val-length m)) cycle-or-repeat) m))

  (def bicycle (comp p/bind cycle-vals))

  (def pattern 
    (bicycle {:amp      [1/2 1/3 1/4 1/8]
              :duration (sixteenths [2 3 3])
              :degree   (shuffle (range 1 12))
              :octave   [4 5 6 5]}))

  (p pattern)
  (play pattern m (m))

  (def a (p/bind (pn (cycle-vals {:degree   [0   0   4   4   5   5   4 3 4 5 6 7 8]
                                  :duration [1/2 1/2 1/2 1/2 1/2 1/2 1]}))))

  (play a m (m))

  ;; get patterns length
  (reduce + (map :duration pattern))

  (defn slide 
    [repeats len step start coll]
    (->> (drop start coll)
         (partition-all len step)
         (take repeats)
         (apply concat)))

  (def a-flock-of-sea-gulls 
    (p/bind {:instrument smooth
             :degree     (take 8 (partition-all 3 1 (range -6 12 2)))
             :duration   (cycle [0.1 0.1 0.2])
             :length     0.45}))

  (play a-flock-of-sea-gulls m (m))

  (def prand
    (p/bind {:instrument smooth
             :degree #(rand-nth (range 6))
             :duration 1/4}))

  (play prand m (m)))
