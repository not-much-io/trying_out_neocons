(ns trying-out-neocons.core
  (:require [clojurewerkz.neocons.rest :as nr]
            [clojurewerkz.neocons.rest.nodes :as nn]
            [clojurewerkz.neocons.rest.relationships :as nrl]
            [clojurewerkz.neocons.rest.cypher :as cy]))

(defonce ids (atom []))

(defn get-conn
  []
  (nr/connect "http://neo4j:192837@localhost:7474/db/data/"))

(defn populate-example
  []
  (let [conn  (get-conn)
        node1 (nn/create conn {:information "clojure is cool"})
        node2 (nn/create conn {:information "neo4j is cool"})
        _     (reset! ids [(:id node1)
                           (:id node2)])
        rel   (nrl/create conn
                          node1 node2
                          :abstract-association {:type :strong})]
    [node1, node2, rel]))

(defn get-node-example
  []
  (let [conn (get-conn)]
    (map #(nn/get conn %) ids)))

(defn cypher-example
  []
  (let [conn (get-conn)]
    (cy/tquery conn
               "
               START x = node({ids})
               RETURN x.information
               " {:ids @ids})))

(defn cypher-example2
  [ids]
  (let [conn (get-conn)]
    (cy/tquery conn
               "
               START x = node({ids})
               RETURN x.information
               " {:ids ids})))