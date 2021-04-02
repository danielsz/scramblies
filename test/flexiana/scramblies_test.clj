(ns flexiana.scramblies-test
  (:require [clojure.test :refer [deftest is testing]]
            [flexiana.scramblies :refer [scramble1? scramble2? scramble3? scramble4?]]))

(deftest main
  (let [s1 "rekqodlw"
        s2 "world"
        s3 "katas"
        s4 "steak"]
    (testing "Functional style")
    (is (scramble1? s1 s2))
    (is (not (scramble1? s3 s4)))
    (testing "Sets")
    (is (scramble2? s1 s2))
    (is (not (scramble2? s3 s4)))
    (testing "Frequencies")
    (is (scramble3? s1 s2))
    (is (not (scramble3? s3 s4)))
    (testing "Scheme")
    (is (scramble4? s1 s2))
    (is (not (scramble4? s3 s4)))))
	 
