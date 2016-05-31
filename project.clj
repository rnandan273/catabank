(defproject catabank "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.datomic/datomic-free "0.9.5153"  :exclusions [joda-time]]
                 [expectations "2.1.3"]
                 [org.clojure/core.logic "0.8.5"]]
  :plugins [[lein-autoexpect "1.4.0"]])
