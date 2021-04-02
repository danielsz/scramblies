(ns flexiana.html
  (:require [hiccup
             [page :refer [html5 include-js include-css]]]
            [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]))

(defn index
  "Main skeleton for the application"
  []
  (html5
   [:head 
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
    [:meta {:name "description" :content "flexiana"}]
    [:meta {:name "author" :content "daniel"}]
    [:title "flexiana"]
    [:link {:href "https://fonts.googleapis.com/css?family=Roboto|Lato:300i,400" :rel "stylesheet"}]
    [:link {:rel "apple-touch-icon" :type "image/png" :href "/favicon/apple-touch-icon.png" :sizes "180x180"}]
    [:link {:rel "icon" :type "image/png" :href "/favicon/favicon-32x32.png" :sizes "32x32"}]
    [:link {:rel "icon" :type "image/png" :href "/favicon/favicon-16x16.png" :sizes "16x16"}]
    [:link {:rel "shortcut icon"  :href "/favicon/favicon.ico"}]
    [:link {:rel "manifest" :href "/favicon/site.webmanifest"}]
    [:link {:rel "mask-icon" :href "/favicon/safari-pinned-tab.svg" :color "#5bbad5"}]
    [:meta {:name "apple-mobile-web-app-title" :content "flexiana"}]
    [:meta {:name "application-name" :content "flexiana"}]
    [:meta {:name "msapplication-TileColor" :content "#ffde00"}]
    [:meta {:name "msapplication-config" :content "/favicon/browserconfig.xml"}]
    [:meta {:name "theme-color" :content "#ffffff"}]]
   [:script "var antiForgeryToken = " (pr-str *anti-forgery-token*)]
   [:body     
    [:div {:id "app"}]
    (include-js
     "/js/flexiana.js")]))

(defn not-found
  "404"
  []
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
    [:meta {:name "description" :content "flexiana"}]
    [:meta {:name "author" :content "daniel"}]
    [:title "flexiana"]
    [:body [:p "404. Page not found"]]]))
