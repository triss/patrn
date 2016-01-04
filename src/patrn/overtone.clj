(ns patrn.overtone
  (:require [patrn.event :as event]
            [patrn.core :as p])
  (:use [overtone.core]))

(defn inst-param-names
  "Gets sequence of an Overtone instrument paramater names."
  [i] (map (comp keyword :name) (:params i)))

(defn play-event 
  "Plays a single event with Overtone instrument specified within it."
  [{:keys [instrument] :as e}]
  (->> (select-keys (event/resolve-values e) 
                    (inst-param-names instrument))
       vals
       (apply instrument)))

(defn play
  "Play back a pattern binding."
  [[{:keys [duration] :or {duration 1} :as e} & rst] metro t]
  (at (metro t) (play-event e))
  (when rst 
    (let [next-time (+ t duration)] 
      (apply-by (metro next-time) play [rst metro next-time]))))

(comment
  (connect-external-server "192.168.0.10" 57110)

  (definst smooth 
    [freq 440 amp 0.5 length 1]
    (* (sin-osc freq) 
       (env-gen (perc 0.01 length amp) 1 1 0 FREE)))
  
  (smooth 991 0.2 9.2)

  (def m (metronome 128))

  (def e (merge event/default-event {:octave 4 :degree 1 :instrument smooth}))

  (def pattern (p/bind (merge event/default-event 
                              {:instrument smooth
                               :duration (cycle (map #(/ % 6) [2 2 3]))
                               :degree   [1 2 3 6 5 4 9 8 7]
                               :octave   (cycle [4 5 6 5])})))

  (play pattern m (m))

  (play-event e)

  )
