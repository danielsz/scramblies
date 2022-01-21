(ns flexiana.scramblies
  (:require [clojure.set :refer [subset?]]
            [clojure.string :as str]))

;; sets

(defn scramble1?
  [s1 s2]
  (subset? (set s2) (set s1)))

;; functional style, higher-order function

(defn scramble2?
  [s1 s2]
  (every? (set s1) s2))

;; frequencies

(defn scramble3? [s1 s2]
  (let [freqs (frequencies s1)]
    (reduce (fn [x y] (or (and x (contains? freqs y)) (reduced false))) true (seq s2))))

;; scheme style

(defn scramble4? [s1 s2]
  (not (loop [x s2]
         (when (seq x)
           (if (str/includes? s1 (subs x 0 1))
             (recur (subs x 1))
             true)))))

;;; benchmark

(defmacro bench
  "Returns result and time of the execution of body"
  [body]
  `(let [start# (. System (nanoTime))
         r# ~body]
     {:time (/ (double (- (. System (nanoTime)) start#)) 1000000.0)
      :result r#}))
