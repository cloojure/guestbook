(ns guestbook.handler
  (:require [compojure.core                   :refer [defroutes routes]]
            [ring.middleware.resource         :refer [wrap-resource]]
            [ring.middleware.file-info        :refer [wrap-file-info]]
            [ring.middleware.session.memory   :refer [memory-store]]
            [hiccup.middleware                :refer [wrap-base-url]]
            [compojure.handler                :as handler]
            [compojure.route                  :as route]
            [noir.session                     :as session]
            [guestbook.routes.home            :refer  [home-routes]]
            [guestbook.routes.auth            :refer  [auth-routes]]
            [guestbook.models.db              :as db]
  ))

(defn init []
  (println "guestbook is starting")
  (if-not (.exists (java.io.File. "./db.sq3"))
    (db/create-guestbook-table) ))

(defn destroy []
  (println "guestbook is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (handler/site
        (routes auth-routes 
                home-routes 
                app-routes))
      (session/wrap-noir-session
        {:store (memory-store)} )))


