(ns catabank.core-test
  (:require [expectations :refer :all]
            [catabank.core :refer :all]
            [datomic.api :as d]))

;; create a in memory database
(defn create-empty-in-memory-db []
  (let [uri "datomic:mem://bank-test-db"]
    (d/delete-database uri)
    (d/create-database uri)
    (let [conn (d/connect uri)
          schema (load-file "resources/datomic/schema.edn")]
      (d/transact conn schema)
      conn)))

;; add one person
(expect #{["John"]}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (add-person "John")
            (find-all-persons))))

;; add many persons
(expect #{["John"] ["raghu"]}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (add-person "John")
            (add-person "raghu")
            (find-all-persons))))

;; add person and account
(expect #{["john_citi"]}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (add-person "John")
            (add-person-account "John" "john_citi")
            (find-accounts-for-person "John"))))


;; add multiple persons and accounts and look up for particular account
(expect #{["adhrit_citi"] ["adhrit_axis"]}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (add-person "John")
            (add-person-account "John" "john_citi")
            (add-person-account "John" "john_axisi")
            (add-person "Adhrit")
            (add-person-account "Adhrit" "adhrit_citi")
            (add-person-account "Adhrit" "adhrit_axis")
            (find-accounts-for-person "Adhrit"))))

;; add product
(comment
(expect #{["Workwear"]}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (add-product
             {:product/name "Cargo_Jeans" :product/code 1234 :product/trendcat "Workwear"
              :product/productcat "Jeans" :product/color "blue" :product/desc "Jeans from Levis, Fashion"})
            (add-product
             {:product/name "Bermuda_Jeans" :product/code 1235 :product/trendcat "Summerwear"
              :product/productcat "Jeans" :product/color "blue" :product/desc "Bermuda from Levis, Fashion"})
            (find-product-trend-productcode 1234))))

;;add product search by color
(expect #{["Summerwear"] ["Workwear" ]}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (add-product
             {:product/name "Cargo_Jeans" :product/code 1234 :product/trendcat "Workwear"
              :product/productcat "Jeans1" :product/color "blue" :product/desc "Jeans from Levis, Fashion"})
            (add-product
             {:product/name "Cargo_Jeans2" :product/code 12345 :product/trendcat "Winterwear"
              :product/productcat "Jeans" :product/color "blue" :product/desc "Jeans from Levis, Fashion"})
            (add-product
             {:product/name "Bermuda_Jeans" :product/code 1235 :product/trendcat "Summerwear"
              :product/productcat "Discount Jeans" :product/color "blue" :product/desc "Bermuda from Levis, Fashion"})
            (find-product-color "blue" "Discount Jeans"))))


(defn store_db [x]
  (println "Saving" (count (keys (first x))) (first x)
           (keys (first x))
           (get (first x) :trend)
           (get (first x) :top)
           (get (first x) :dress)
           (get (first x) :pattern)
           (get (first x) :bottom)
           (get (first x) :footwear)
           (get (first x) :accessory))

  (add-summer-style (first x)))


(expect #{["rompers"] ["line"] ["skaters"]}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (dotimes [i (count (get-summer-trends))]
              (add-summer-style (nth (get-summer-trends) i)))
            (find-all-styles)
          )
        )
)

(expect #{["rompers"] ["line"] ["skaters"]}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (dotimes [i (count (get-workwear-trends))]
              (add-workwear-style (nth (get-workwear-trends) i)))
            (find-all-styles)
          )
        )
)

(expect #{[]}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (dotimes [i (count (get-festival-trends))]
              (add-festival-style (nth (get-festival-trends) i)))
            (find-all-styles)
          )
        )
  )
)

(expect #{["shyam"] ["raghu"] ["krishnarao"] ["jagadish"] ["veerappa"]}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (add-family-member "shyam")
            (add-family-member "raghu")
            (add-family-member "adhrit")
            (add-family-member "krishnarao")
            (add-family-member "jagadish")
            (add-family-member "shruti")
            (add-family-member "veerappa")
            (add-family-relation "veerappa" "shyam")
            (add-family-relation "shyam" "raghu")
            (add-family-relation "raghu" "adhrit")
            (add-family-relation "veerappa" "krishnarao")
            (add-family-relation "krishnarao" "jagadish")
            (add-family-relation "jagadish" "shruti")
            (find-all-fathers)
            )))

(expect #{["veerappa"] ["shyam"] }
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (add-family-member "shyam")
            (add-family-member "raghu")
            (add-family-member "adhrit")
            (add-family-member "krishnarao")
            (add-family-member "jagadish")
            (add-family-member "shruti")
            (add-family-member "veerappa")
            (add-family-relation "veerappa" "shyam")
            (add-family-relation "shyam" "raghu")
            (add-family-relation "raghu" "adhrit")
            (add-family-relation "veerappa" "krishnarao")
            (add-family-relation "krishnarao" "jagadish")
            (add-family-relation "jagadish" "shruti")
            (print (find-all-grandfathers))
            (find-all-grandfathers)
            )))

(expect #{["veerappa"]}
        (with-redefs [conn (create-empty-in-memory-db)]
          (do
            (add-family-member "shyam")
            (add-family-member "raghu")
            (add-family-member "adhrit")
            (add-family-member "krishnarao")
            (add-family-member "jagadish")
            (add-family-member "shruti")
            (add-family-member "veerappa")
            (add-family-relation "veerappa" "shyam")
            (add-family-relation "shyam" "raghu")
            (add-family-relation "raghu" "adhrit")
            (add-family-relation "veerappa" "krishnarao")
            (add-family-relation "krishnarao" "jagadish")
            (add-family-relation "jagadish" "shruti")
            (print (find-all-greatgrandfathers))
            (find-all-greatgrandfathers)
            )))

