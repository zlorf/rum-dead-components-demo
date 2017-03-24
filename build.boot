(set-env!
 :source-paths   #{"src"}
 :resource-paths #{"resources"}
 :dependencies '[[org.clojure/clojure "1.8.0" :scope "test"]
                 [org.clojure/clojurescript "1.8.40"]
                 [adzerk/boot-cljs "1.7.228-1" :scope "test"]
                 [cljsjs/react-with-addons "15.4.2-2"]
                 [cljsjs/react-dom "15.4.2-2" :exclusions [cljsjs/react]]
                 [rum "0.10.8" :exclusions [cljsjs/react cljsjs/react-dom cljsjs/react-dom-server]]
                ])

(require
 '[boot.core        :as core]
 '[adzerk.boot-cljs :refer [cljs]]
 )


(deftask build
  []
  (cljs :ids ["statics/browser"]
        :optimizations :advanced
        :compiler-options {:output-wrapper true
                           ;:pseudo-names true
                           :elide-asserts true
                           :closure-defines {"goog.DEBUG" false}}))
