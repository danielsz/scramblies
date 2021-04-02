(ns flexiana.handler
  (:require [reitit.ring :as r]
            [ring.util.response :refer [response content-type]]
            [flexiana.routing-table :refer [routes]]
            [flexiana.html :as html]
            [jsonista.core :as j]
            [flexiana.scramblies :refer [scramble1? scramble2? scramble3? scramble4? bench]]))

(defn ring-handler [_]
  (let [index (fn [_] (-> (html/index)
                        response
                        (content-type "text/html")))
        service (fn [{{s1 :s1 s2 :s2 style :style} :params}]
                  (let [f (get {"functional" scramble1? "sets" scramble2? "frequencies" scramble3? "scheme" scramble4?} style)]
                    (-> (bench (f s1 s2))
                       (j/write-value-as-string j/keyword-keys-object-mapper )
                       response
                       (content-type "application/json"))))]
    (r/ring-handler
     (r/router
      (into [["/service" {:name :service :post service}]]
            (for [[r1 r2] routes] [r1 (assoc r2 :get index)])))
     (r/routes
      (r/create-resource-handler {:path "/" :root ""})))))
