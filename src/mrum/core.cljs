(ns mrum.core
  (:require-macros mrum.core)
  (:require
    [rum.core :as rum]
    ))


(defn build-defc [render-body mixins display-name]
  (if (empty? mixins)
    (let [class (fn [props]
                  (apply render-body (aget props ":rum/args")))
          _     (aset class "displayName" display-name)
          ctor  (fn [& args]
                  (js/React.createElement class #js { ":rum/args" args }))]
      (with-meta ctor { :rum/class class })
      ;ctor
      )
    (let [render (fn [state] [(apply render-body (:rum/args state)) state])]
      (rum/build-ctor render mixins display-name))))

(def mount rum/mount)

(defn lazy-component
  [ctor a b c]
  (let [c (delay (ctor a b c))]
    (fn [& args]
      (apply @c args))))
