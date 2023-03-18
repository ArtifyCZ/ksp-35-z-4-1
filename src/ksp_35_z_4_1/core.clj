(ns ksp-35-z-4-1.core
  (:require [clojure.string :as str]))

(defn solve-lights [lights]
  (-> lights
      (assoc 0 :on)
      (#(assoc % (dec (count %)) :on))
      (#(persistent! (reduce (fn [lights i]
                               (let [prev (nth lights (dec i))
                                     curr (nth lights i)
                                     next (nth lights (inc i))]
                                 (if (not= curr :broken)
                                   (if (or (not= prev :on)
                                           (= next :broken))
                                     (assoc! lights i :on)
                                     (assoc! lights i :off)))
                                 lights))
                             (transient %)
                             (range 1 (dec (count %))))))))

(defn solve [lights]
  (->> lights
       (str/trim)
       (mapv #(if (= % \X)
                :broken
                :off))
       (solve-lights)
       (map #(case %
               :on \O
               :off \-
               :broken \X))
       (reduce str)))

(defn solve-file [input output]
  (time (->> (slurp input)
             (solve)
             (#(str % "\n"))
             (spit output))))
