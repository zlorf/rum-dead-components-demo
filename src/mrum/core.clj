(ns mrum.core
  (:require
    [sablono.compiler :as s]))


(defn- fn-body? [form]
  (when (and (seq? form)
             (vector? (first form)))
    (if (= '< (second form))
      (throw (IllegalArgumentException. "Mixins must be given before argument list"))
      true)))


(defn- parse-defc
  ":name  :doc?  <? :mixins* :bodies+
   symbol string <  exprs    fn-body?"
  [xs]
  (when-not (instance? clojure.lang.Symbol (first xs))
    (throw (IllegalArgumentException. "First argument to defc must be a symbol")))
  (loop [res  {}
         xs   xs
         mode nil]
    (let [x    (first xs)
          next (next xs)]
      (cond
        (and (empty? res) (symbol? x))
          (recur {:name x} next nil)
        (fn-body? xs)        (assoc res :bodies (list xs))
        (every? fn-body? xs) (assoc res :bodies xs)
        (string? x)          (recur (assoc res :doc x) next nil)
        (= '< x)             (recur res next :mixins)
        (= mode :mixins)
          (recur (update-in res [:mixins] (fnil conj []) x) next :mixins)
        :else
          (throw (IllegalArgumentException. (str "Syntax error at " xs)))))))

(defn- compile-body [[argvec conditions & body]]
  (if (and (map? conditions) (seq body))
    (list argvec conditions (s/compile-html `(do ~@body)))
    (list argvec (s/compile-html `(do ~@(cons conditions body))))))

(defn- -defc [builder cljs? body]
  (let [{:keys [name doc mixins bodies]} (parse-defc body)
        render-body (if cljs?
                      (map compile-body bodies)
                      bodies)
        arglists  (if (= builder 'mrum.core/build-defc)
                    (map (fn [[arglist & _body]] arglist) bodies)
                    (map (fn [[[_ & arglist] & _body]] (vec arglist)) bodies))]
    `(def ~(vary-meta name update :arglists #(or % `(quote ~arglists)))
       ~@(if doc [doc] [])
       (mrum.core/lazy-component ~builder (fn ~@render-body) ~mixins ~(str name)))))
       ;(~builder (fn ~@render-body) ~mixins ~(str name)))))

(defmacro defc
  [& body]
  (-defc 'mrum.core/build-defc (boolean (:ns &env)) body))
