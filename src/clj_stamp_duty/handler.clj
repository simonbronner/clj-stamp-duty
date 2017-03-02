(ns clj-stamp-duty.handler
  (:require [clj-stamp-duty.middleware :refer [wrap-middleware]]
            [clj-stamp-duty.validation :refer [validate-model]]
            [compojure.core :refer :all]
            [compojure.route :as route]))

(defn respond-json [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body data})

(defn calc-stamp-duty [payload]
  (let [validation-messages (validate-model payload)]
    (if (> (count validation-messages) 0)
      {:success false :messages validation-messages}
      {:success true
        :transferRegistration 300
        :mortgageRegistration 800
        :transferStampDuty 1200})))

(defroutes app-routes
  (POST "/services/api/stampduty" {payload :params} (respond-json (calc-stamp-duty payload)))
  (route/not-found "Not Found"))

(def app (wrap-middleware #'app-routes))
