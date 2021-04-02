(ns ^:figwheel-hooks flexiana.core
  (:require [cljs-utils.core :as utils :refer [by-id form-data]]
            [cljs.core.match :refer-macros [match]]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [flexiana.routing-table :as rt]
            [react :as react]
            [react-dom :as dom])
  (:import [goog.net XhrIo])
  (:require-macros [cljs-utils.compilers.hicada :refer [html]]))

(defn scramblies [props]
  (let [props (js->clj props :keywordize-keys true)
        [s1 setS1] (react/useState "")
        [s2 setS2] (react/useState "")
        [result setResult] (react/useState nil)]
    (html [:form {:onSubmit (fn [e]
                              (.preventDefault e)
                              (.send XhrIo "/service" (fn [e]
                                                        (let [xhr (.-target e)
                                                              response (.getResponseJson xhr)]
                                                          (setResult response)))
                                     "POST"
                                     (form-data {:s1 s1
                                                 :s2 s2
                                                 :style (:style props)})
                                     #js {"X-CSRF-Token" js/antiForgeryToken}))}
           [:input {:type "text"
                    :name "s1"
                    :pattern "[a-z]+"
                    :required true
                    :onChange (fn [e] (let [val (.-value (.-target e))]
                                       (setS1 val)))
                    :value s1}]
           [:input {:type "text"
                    :name "s2"
                    :pattern "[a-z]+"
                    :required true
                    :onChange (fn [e] (let [val (.-value (.-target e))]
                                       (setS2 val)))
                    :value s2}]
           [:input {:type "submit" :value "Scramble"}]
           (when result [:p "Result: " (str (.-result result) " in " (.-time result) " msecs.")])])))

(defn banner []
  (html [:div
         [:a {:href "/"} "Functional | "]
         [:a {:href "/sets"} "Sets | "]
         [:a {:href "/frequencies"} "Frequencies | "]
         [:a {:href "/scheme"} "Scheme"]]))

(defn pages [props]
  (match [(:view (js->clj props :keywordize-keys true))]
         [:root] (html [:*
                        [:p "Scramblies - functional style - higher order functions"]
                        [:> scramblies {:style :functional}]])
         [:sets] (html [:*
                        [:p "Scramblies - Sets - is s2 a subset of s1?"]
                        [:> scramblies {:style :sets}]])
         [:frequencies] (html [:*
                             [:p "Scramblies - Frequencies - membership test with reduce"]
                             [:> scramblies {:style :frequencies}]])
         [:scheme] (html [:*
                          [:p "Scramblies - Scheme style - recurse over a list"]
                          [:> scramblies {:style :scheme}]])))

(defn router []
  (let [[view setView] (react/useState :root)]
    (js/React.useEffect (fn []
                          (rfe/start!
                           (rf/router rt/routes)
                           (fn [m]
                             (setView (:name (:data m))))
                           {:use-fragment false})))
    (html [:*
           [:> banner {}]
           [:> pages {:view view}]])))

(defn mount []
  (when-let [tag (by-id "app")]
    (dom/render (html [:> router {}]) tag)))

(defonce init (mount))

(defn ^:before-load my-before-reload-callback []
  (println "BEFORE reload!"))

(defn ^:after-load my-after-reload-callback []
  (println "AFTER reload!")
  (mount))

