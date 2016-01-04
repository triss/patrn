(ns patrn.event
  (:require [flatland.ordered.map :refer [ordered-map]]
            [music.pitch :refer [nth-interval midi->hz db->amp]]))

(def default-event
  (ordered-map 
    ;; pitch related event key fns
    :note      
    (fn [{:keys [root scale degree] 
          :or {root 0 scale :diatonic degree 0}}] 
      (+ root (nth-interval scale degree)))

    :midi-note 
    (fn [{:keys [note octave transpose]
          :or {octave 5 transpose 0}}]
      (+ transpose note (* octave 12)))

    :freq      
    (fn [e] (midi->hz (:midi-note e)))

    ;; amp stuff
    :amp 
    (fn [{:keys [db] :or {db -20}}] 
      (db->amp db))

    ;; note length calculation
    :length
    (fn [{:keys [duration legato stretch]
          :or {duration 1 stretch 1 legato 0.8}}]
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
