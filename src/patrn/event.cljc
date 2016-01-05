(ns patrn.event
  (:require [flatland.ordered.map :refer [ordered-map]]
            [music.pitch :refer [nth-interval midi->hz db->amp]]))

(def default-event
  (ordered-map 
    ;; pitch related event keys 
    :root   0
    :scale  :diatonic
    :degree 0

    :note      
    (fn [{:keys [root scale degree]}] 
      (+ root (nth-interval scale degree)))

    :octave    5
    :transpose 0

    :midi-note 
    (fn [{:keys [note octave transpose]}]
      (+ transpose note (* octave 12)))

    :freq      
    (fn [e] (midi->hz (:midi-note e)))

    ;; amp stuff
    :db -20

    :amp 
    (fn [e] (db->amp (:db e)))

    ;; note length calculation
    :duration 1
    :stretch  1
    :legato   0.8

    :length
    (fn [{:keys [duration legato stretch]}]
      (* duration legato stretch))
    ))

(defn resolve-value
  "Takes an event a key value pair, when the value is a function it is applied
  to the event, if it's an atom is dereferenced and if its some other value its
  left as it is. After this the transformed value is associated with the key in
  the event."
  [event [k v]]
  (assoc event k 
         (cond 
           (fn? v) (v event)
           (instance? #?(:cljs Atom :clj clojure.lang.Atom) v) @v
           :else v)))

(defn resolve-values
  "Resolve all the values in the event."
  [event] (reduce resolve-value event event))
