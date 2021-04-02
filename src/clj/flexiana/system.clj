(ns flexiana.system
  (:require [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [compojure.core]
            [system.core :refer [defsystem]]
            [com.stuartsierra.component :as component]
            [system.components
             [middleware :refer [new-middleware]]
             [endpoint :refer [new-endpoint]]
             [handler :refer [new-handler]]
             [repl-server :refer [new-repl-server]]
             [immutant-web :refer [new-immutant-web]]]
            [ring-utils.middleware
             [not-found :refer [wrap-not-found]]]
            [ring.middleware.proxy-headers :refer [wrap-forwarded-remote-addr]]
            [muuntaja.middleware :refer [wrap-params wrap-format]]
            [flexiana.handler :refer [ring-handler]]
            [flexiana.html :refer [not-found]]))

(def defaults (assoc-in site-defaults [:session :cookie-attrs :same-site] :lax))

(defsystem base
  [:middleware (new-middleware {:middleware [[wrap-not-found (not-found)]
                                             [wrap-defaults defaults]
                                             wrap-forwarded-remote-addr]})
   :app-middleware (component/using (new-middleware {:middleware [wrap-params
                                                                  wrap-format]})
                                    [])
   :app-endpoint (component/using (new-endpoint ring-handler) [:app-middleware])
   :handler (component/using (new-handler) [:app-endpoint                                         
                                            :middleware])
   :http (component/using (new-immutant-web :port (Integer. ^String (System/getProperty "http.port"))) [:handler])])

(defn prod
  "Assembles and returns components for a production deployment"
  []
  (merge (base)
         (component/system-map
          :repl-server (new-repl-server :port (Integer. (System/getProperty "repl.port")) :bind (System/getProperty "repl.ip") :with-cider false)
          :cider-repl-server (new-repl-server :port (inc (Integer. (System/getProperty "repl.port"))) :bind (System/getProperty "repl.ip") :with-cider true))))
