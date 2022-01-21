(ns flexiana.scramblies
  (:require [clojure.set :refer [subset?]]
            [clojure.string :as str]))

;; sets

(defn scramble1?
  [s1 s2]
  (subset? (set s2) (set s1)))

;; functional style

(defn scramble2?
  [s1 s2]
  (every? (set s1) s2))

;; frequencies

(defn scramble3? [s1 s2]
  (let [freqs (frequencies s1)]
    (reduce (fn [x y] (or (and x (contains? freqs y)) (reduced false))) true (seq s2))))

;; scheme style, tail call optimization

(defn scramble4? [s1 s2]
  (not (loop [x s2]
         (when (seq x)
           (if (str/includes? s1 (subs x 0 1))
             (recur (subs x 1))
             true)))))

;; iterative recursion

(defn scramble5? [s1 s2]
  (letfn [(iter [s1 s2 ]
            (when (seq s2)
              (if (str/includes? s1 (subs s2 0 1))
                (iter s1 (subs s2 1))
                false)))]
    (nil? (iter s1 s2))))

;; vectors

(defn scramble6? [s1 s2]
  (let [s1 (vec s1)
        s2 (vec s2)]
    (not (loop [x s2]
           (when (seq x)
             (if (some #{(peek x)} s1)
               (recur (pop x))
               true))))))

;; vectors reduce

(defn scramble7? [s1 s2]
  (let [s1 (vec s1)]
    (reduce (fn [x y] (or (some #(= y %) s1) (reduced false))) true (vec s2))))

;;; benchmark

(defmacro bench
  "Returns result and time of the execution of body"
  [body]
  `(let [start# (. System (nanoTime))
         r# ~body]
     {:time (/ (double (- (. System (nanoTime)) start#)) 1000000.0)
      :result r#}))

(defn benchmark [s1 s2 & fs]
  (doseq [f fs]
    (println (munge f) ":" (bench (f s1 s2)))))

; (benchmark "cedewaraaossoqqyt" "codewars" scramble1? scramble2? scramble3? scramble4? scramble5? scramble6? scramble7?)
