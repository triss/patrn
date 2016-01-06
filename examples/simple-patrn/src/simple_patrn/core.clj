(ns simple-patrn.core
  (:require [clojure.pprint :refer [pprint]]
            [patrn.core :as p]
            [patrn.overtone :refer [play]])
  (:use [overtone.core]))

(defn p [x] (pprint x) x)

(comment
  (connect-external-server "192.168.0.10" 57110)

  (definst smooth 
    [freq 440 amp 0.5 length 1]
    (* (sin-osc freq) 
       (env-gen (perc 0.01 length amp) 1 1 0 FREE)))

  (do (smooth 995 0.2 1.2)
      (smooth 993 0.05 1.9)
      (smooth 991 0.1 1))

  (def m (metronome 128))

  (def pattern 
    (p/bind {:instrument smooth
             :amp      (cycle [1/2 1/3 1/4 1/8])
             :duration (cycle (map #(/ % 4) [2 3 3]))
             :degree   (shuffle (range 1 12))
             :octave   (cycle [4 5 6 5])}))

  (p pattern)
  (play pattern m (m))

  (def p (p/bind {:instrument smooth
                  :degree   [0 0 4 4 5 5 4]
                  :duration [1/2 1/2 1/2 1/2 1/2 1/2 1]}))

  (play p m (m))

  ;; get patterns length
  (reduce + (map :duration pattern))

  (defn slide 
    [repeats seg-length seg-step start coll]
    let [get-segment #(->> coll (drop %) (take seg-length))] 
    (->> (range start (count coll) seg-step)  ; the start pos of each segment
         (map get-segment)                    ; fetch each segment
         (apply concat)))                     ; and join them together


  (def a-flock-of-sea-gulls 
    (p/bind {:instrument smooth
             :degree     (slide 8 3 1 0 (range -6 12 2))
             :duration   (cycle [0.1 0.1 0.2])
             :length     0.45}))

  (play a-flock-of-sea-gulls m (m))

  (def prand
    (p/bind {:instrument smooth
             :degree #(rand-nth (range 6))
             :duration 1/4}))

  (play prand m (m)))
