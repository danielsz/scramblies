(ns flexiana.scramblies
  (:require [clojure.set :refer [subset?]]))

;; functional style

(defn scramble1?
  [s1 s2]
  (every? (set s1) s2))

;; sets

(defn scramble2?
  [s1 s2]
  (subset? (set s2) (set s1)))

;; frequencies

(defn scramble3? [s1 s2]
  (let [freqs (frequencies s1)]
    (reduce (fn [x y] (or (and x (contains? freqs y)) (reduced false))) true (seq s2))))

;; scheme style

(defn scramble4? [s1 s2]
  (let [y (seq s1)]
    (not (loop [x (seq s2)]
           (when (seq x)
             (if (some (set `(~(first x))) y)
               (recur (rest x))
               true))))))

;;; benchmark

(defmacro bench
  "Returns result and time of the execution of body"
  [body]
  `(let [start# (. System (nanoTime))
         r# ~body]
     {:time (/ (double (- (. System (nanoTime)) start#)) 1000000.0)
      :result r#}))
