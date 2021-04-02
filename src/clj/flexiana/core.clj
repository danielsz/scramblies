(ns flexiana.core
  (:gen-class)
  (:require [system.repl :refer [set-init! go]]
            [flexiana.system :refer [base]]))

(defn -main
  "Start a production system."
  [& args]
  (if-let [system (first args)]
    (do (require (symbol (namespace (symbol system))))
        (set-init! (resolve (symbol system))))
    (set-init! #'base))
  (go))

