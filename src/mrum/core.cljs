(ns mrum.core
  (:require-macros mrum.core)
  (:require
    [rum.core :as rum]
    [goog.functions]
    ))


(defn build-defc [render-body mixins display-name]
  (if (empty? mixins)
    (let [class (fn [props]
                  (apply render-body (aget props ":rum/args")))
          _     (aset class "displayName" display-name)
          ctor  (fn [& args]
                  (js/React.createElement class #js { ":rum/args" args }))]
      (with-meta ctor { :rum/class class }))
    (let [render (fn [state] [(apply render-body (:rum/args state)) state])]
      (rum/build-ctor render mixins display-name))))

(def mount rum/mount)

(defn- set-meta [c]
  (let [f (fn []
            (let [ctr (c)]
              (.apply ctr ctr (js-arguments))))]
    (specify! f IMeta (-meta [_] (meta (c))))
    f))


(defn lazy-component
  "Wraps component construction in a way so that Google Closure Compiler
   can properly recognize and elide unused components. The extra `set-meta`
   fn is needed so that the compiler can properly detect that all functions
   are side effect free."
  [ctor render mixins display-name]
  (let [bf #(ctor render mixins display-name) ;; Avoid IIFE
        c  (goog.functions/cacheReturnValue bf)]
    (set-meta c)))
