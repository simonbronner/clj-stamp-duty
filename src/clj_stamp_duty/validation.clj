(ns clj-stamp-duty.validation
  (:require [clojure.string :refer [lower-case]]))

(defn mandatory [key payload]
  (let [val (key payload)]
    (if (nil? val)
      (str (name key) " is a mandatory value"))))

(defn validate-enum [allowable-values name value]
  (if (not (contains? allowable-values (lower-case value)))
    (str name " must be one of the following: " allowable-values)))

(defn validate-state [k payload]
  (validate-enum #{"none" "qld" "vic" "nsw" "act" "tas" "nt" "sa" "wa"} "Australian State" (k payload)))

(defn validate-home-buyer-type [k payload]
  (validate-enum #{"none" "firsthomebuyer" "notfirsthomebuyer"} "Home Buyer Type" (k payload)))

(defn validate-purchase-type [k payload]
  (validate-enum #{"none" "owneroccupied" "investment" "residentialinvestment" "nonresidentialinvestment" "business"} "Home Buyer Type" (k payload)))

(defn validate-realestate-type [k payload]
  (validate-enum #{"none" "established" "tobebuilt" "newbuilding" "vacantland"} "Real Estate Type" (k payload)))

(defn validate-realestate-value [k payload]
  (let [v (k payload)]
    (if (not (number? v))
      (str "Real Estate Value must be a number")
      (if (<= v 0)
        (str "Real Estate Value must be greater than 0")))))

(defn validators-for-payload [payload]
  "Provides the validators that should be executed for a given payload"
  {:state [mandatory validate-state]
   :realEstateType [mandatory validate-realestate-type]
   :purchaseType [mandatory validate-purchase-type]
   :homeBuyerType [mandatory validate-home-buyer-type]
   :realEstateValue [mandatory validate-realestate-value]})

(defn exec-value-validators
  "Executes the passed validators which are associated with the field that they
  are responsible for validating. Note that the first validation failure encountered
  for each field will prevent the remaining validation for that field to run"
  ([payload k validators]
   (let [result ((first validators) k payload)
         remaining-validators (rest validators)]
     (if (and (nil? result) (> (count remaining-validators) 0))
       (exec-value-validators payload k remaining-validators)
       {k result})))
  ([payload key-val]
   (exec-value-validators payload (key key-val) (val key-val))))

(defn validate-model [cal-stamp-duty-request]
  (let [validators (validators-for-payload cal-stamp-duty-request)
        messages (map (partial exec-value-validators cal-stamp-duty-request) validators)]
    (filter #(not (nil? (first (vals %)))) messages)))
