(ns patrn.overtone
  "Functions to simplify the playback of patrns with Overtone."
  (:require [patrn.event :as event]
            [patrn.core :as p])
  (:use [overtone.core]))

(defn inst-param-names
  "Gets sequence of an Overtone instrument parameter names."
  [instrument] (map (comp keyword :name) (:params instrument)))

(defn close-synth-gate
  "Closes synth's gate control at time t on metronome."
  [synth {:keys [length time-stamp metronome]}] 
  (at (metronome (+ time-stamp length)) 
      (ctl synth :gate 0)))

(defn play-derived-event
  "Plays a single event with Overtone instrument specified within it."
  [{:keys [instrument metronome time-stamp] 
    :or   {time-stamp (metronome)} 
    :as   event}]
  (assert instrument "Event :instrument key value must be specified.")
  (at (metronome time-stamp) 
      (let [inst-params (inst-param-names instrument)
            used-params ((apply juxt inst-params) event)
            synth       (apply instrument used-params)]
        (when (#{:gate} inst-params)
          (close-synth-gate synth event)))))

(def default-metronome (metronome 60))

(def base-event 
  "Provides default set of event value derivations for use with Overtone."
  (assoc event/default-event :metronome default-metronome))

(defn derive-event-vals
  "Derive an events values."
  [event] (event/derive-vals (merge base-event event)))

(def play-event
  "Helper method for playing overtone event maps."
  (comp play-derived-event derive-event-vals))

(defn time-stamp-first-events
  [events] 
  (let [{:keys [time-stamp duration metronome]
         :or   {metronome default-metronome, 
                time-stamp (default-metronome)}} (first events)
        stamp (fn [e t] (update e :time-stamp #(or % t)))] 
    (conj (drop 2 events) 
          (stamp (second events) (+ time-stamp duration)) 
          (stamp (first events)  time-stamp))))

(declare play)

(defn schedule 
  [[fst & more]]
  (play-event fst)
  (when (seq more)
    (apply-by (:time-stamp (first more)) play [more])))

(def play (comp schedule time-stamp-first-events))
