(ns core.browser
  (:require
    [rum.core :as rum :include-macros true]))


(rum/defc test-component2 []
  [:div.test-component2 "SHOULD NOT BE PRESENT 2"])

(rum/defc test-component []
  [:div.test-component "SHOULD NOT BE PRESENT" (test-component2)])

(defn some-unused-fn [x]
  "SHOULD NOT BE PRESENT 3 (this one is actually removed by the compiler)")

(defn exclaims [count]
  (apply str "This SHOULD BE PRESENT" (repeat count "!")))

(rum/defc used-component []
  [:div "Rum is awesome; " (exclaims 3)])


(defn init-mount! []
  (rum/mount (used-component)
             (.getElementById js/document "container")))

(defn boot []
  (enable-console-print!)
  (init-mount!))
