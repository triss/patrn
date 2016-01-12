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
          (close-synth-gate synth event))
        synth)))

(def default-metronome (metronome 60))

(def base-event 
  "Provides default set of event value derivations for use with Overtone."
  (assoc event/default-event :metronome default-metronome))

(def play-event
  "Helper method for playing overtone event maps."
  (comp play-derived-event 
        event/derive-vals
        #(merge base-event %)))

(defn ensure-time-stamped
  "Ensures event has a time stamp. 
  If not present uses next beat on specified or default metronome."
  [{:keys [time-stamp metronome] :as event}] 
  (let [ts (or time-stamp (if metronome (metronome) (default-metronome)))] 
    (assoc event :time-stamp ts)))

(defn time-stamp-events
  [events]
  (->> events
       (reductions (fn [prev-ts {:keys [time-stamp duration] 
                                 :or   {duration 1}}] 
                     (or time-stamp (+ prev-ts duration))))
       (map #(assoc %1 :time-stamp %2) events)))

(defn schedule 
  [[fst & more]]
  (play-event fst)
  (when (seq more)
    (apply-by (:time-stamp (first more)) schedule [more])))

(def play (comp schedule 
                time-stamp-events 
                #(cons (ensure-time-stamped (first %)) (rest %))))
