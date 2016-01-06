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
  [[{:keys [duration gate length] 
     :or {duration 1 length 1} :as e} & rst] metro t]
  (at (metro t) 
      (let [synth (play-event e)]
        (when gate
          (at (metro (+ t length))
              (ctl synth :gate 0)))))
  (when rst 
    (let [next-time (+ t duration)] 
      (apply-by (metro next-time) play [rst metro next-time]))))
