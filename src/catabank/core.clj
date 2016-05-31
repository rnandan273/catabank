(ns catabank.core
  (:require [datomic.api :as d])
  (:refer-clojure :exclude [==])
  (:use [clojure.core.logic])
  (:use [clojure.core.logic.pldb]))

(def conn nil)

(defn add-family-member [member-name]
  @(d/transact conn [{:db/id (d/tempid :db.part/user)
                      :family/member member-name}]))

  (defn find-fm-id [person-name]
  (ffirst (d/q '[:find ?eid
         :in $ ?person-name
         :where [?eid :family/member ?person-name]]
       (d/db conn)
       person-name)))


(defn add-family-relation [father-name son-name]
  (let [acc-id (d/tempid :db.part/user)]
      @(d/transact conn [{:db/id  (find-fm-id son-name)
                          :family/father (find-fm-id father-name)
                          }])))

(defn find-all-fathers[]
  (d/q '[:find ?father-name
         :in $
         :where [?eid :family/father ?t]
                 [?t :family/member ?father-name]]
       (d/db conn)))

(defn find-all-grandfathers[]
  (d/q '[:find ?father-name
         :in $
         :where
                [?eid :family/father ?x]
                [?x :family/father ?y]
                [?y :family/member ?father-name]]
       (d/db conn)))

(defn find-all-greatgrandfathers[]
  (d/q '[:find ?father-name
         :in $
         :where
                [?eid :family/father ?x]
                [?x :family/father ?y]
                [?y :family/father ?z]
                [?z :family/member ?father-name]]
       (d/db conn)))


(defn add-person [person-name]
  @(d/transact conn [{:db/id (d/tempid :db.part/user)
                      :person/name person-name}]))



(defn add-summer-style [style]
  (println "Adding summer in DB " )

  (println {
                        :db/id (d/tempid :db.part/user)
                        :style/trend (:trend style)
                        :style/top  (:top style)
                        :style/bottom (:bottom style)
                        :style/dress  (:dress style)
                        :style/accessory  (:accessory style)
                        :style/footwear (:footwear style)
                        :style/rating (rand-int 20)})


  @(d/transact conn [{:db/id (d/tempid :db.part/user)
                        :style/trend (:trend style)
                        :style/top  (:top style)
                        :style/bottom (:bottom style)
                        :style/dress  (:dress style)
                        :style/accessory  (:accessory style)
                        :style/footwear (:footwear style)
                        :style/rating (rand-int 20)}])
 (println "xxxzzz")
)

(defn add-workwear-style [style]
  (println "Adding workwear in DB " style)

  (println {
                        :db/id (d/tempid :db.part/user)
                        :style/trend (:trend style)
                        :style/top  (:top style)
                        :style/bottom (:bottom style)
                        :style/dress  (:dress style)
                        :style/accessory  (:accessory style)
                        :style/footwear (:footwear style)
                        :style/colour (:colour style)
                        :style/fit (:fit style)
                        :style/rating (rand-int 20)})


  @(d/transact conn [{:db/id (d/tempid :db.part/user)
                        :style/trend (:trend style)
                        :style/top  (:top style)
                        :style/bottom (:bottom style)
                        :style/dress  (:dress style)
                        :style/accessory  (:accessory style)
                        :style/footwear (:footwear style)
                        :style/colour (:colour style)
                        :style/fit (:fit style)
                        :style/rating (rand-int 20)}])
 (println "xxxzzz")
)


(defn add-festival-style [style]
  (println "Adding festival in DB " style)

  @(d/transact conn [{ :db/id (d/tempid :db.part/user)
                        :style/trend (:trend style)
                        :style/top  (:top style)
                        :style/bottom (:bottom style)
                        :style/accessory  (:accessory style)
                        :style/footwear (:footwear style)
                        :style/prints (:prints style)
                        :style/fit (:fit style)
                        :style/rating (rand-int 20)}])
)

(defn find-all-styles[]
  (d/q '[:find ?trend
         :where [_ :style/trend ?trend]]
       (d/db conn)))

(defn find-product-id [product-name]
  (ffirst (d/q '[:find ?eid
         :in $ ?product-name
         :where [?eid :person/name ?product-name]]
       (d/db conn)
       product-name)))

(defn add-product [product]
  (let [{p_name :product/name
         p_code :product/code
         p_trendcat :product/trendcat
         p_prodcat :product/productcat
         p_color :product/color
         p_desc :product/desc} product]

  @(d/transact conn [{
                        :db/id (d/tempid :db.part/user)
                        :product/name p_name
                        :product/code p_code
                        :product/trendcat p_trendcat
                        :product/productcat p_prodcat
                        :product/color p_color
                        :product/desc p_desc}])))

(defn find-all-products[]
  (d/q '[:find ?trend
         :where [_ :product/trendcat ?trend]]
       (d/db conn)))


(defn find-product-trend[product-name]
  (d/q '[:find ?trend-name
         :in $ ?product-name
         :where [?eid :product/name ?product-name]
                [?eid :product/trendcat ?trend-name]]
       (d/db conn)
       product-name))

(defn find-product-trend-productcode[product-code]
  (d/q '[:find ?trend-name
         :in $ ?product-code
         :where [?eid :product/code ?product-code]
                [?eid :product/trendcat ?trend-name]]
       (d/db conn)
       product-code))

(defn find-product-color[product-color product-category]
  (println product-category)
  (d/q '[:find ?trend-name
         :in $ ?product-color
         :where [?eid :product/color ?product-color]
                [?eid :product/trendcat ?trend-name]
                 (not [?eid :product/productcat "Jeans"])]
       (d/db conn)
       product-color))


(defn find-all-persons[]
  (d/q '[:find ?person-name
         :where [_ :person/name ?person-name]]
       (d/db conn)))


(defn find-person-id [person-name]
  (ffirst (d/q '[:find ?eid
         :in $ ?person-name
         :where [?eid :person/name ?person-name]]
       (d/db conn)
       person-name)))


(defn add-person-account [person-name account-name]
  (let [acc-id (d/tempid :db.part/user)]
      @(d/transact conn [{:db/id acc-id :account/name account-name}
                         {:db/id (find-person-id person-name) :person/account acc-id}])))


(defn find-accounts-for-person [person-name]
  (d/q '[:find ?account-name
         :in $ ?person-name
         :where [?eid :person/name ?person-name]
                [?eid :person/account ?account]
                [?account :account/name ?account-name]]
       (d/db conn)
       person-name))

(db-rel top x)
(db-rel brand x)
(db-rel pattern p)
(db-rel bottom b)
(db-rel dress d)
(db-rel accessory a)
(db-rel footwear f)
(db-rel fit ft)
(db-rel colour c)
(db-rel prints pnts)

(def product_top
  (db
   [brand "Proline"]
   [brand "Mark Spencer"]
   [brand "Kohls"]
   [top "peasant tops"]
   [top "off shoulders"]
   [top "tank"]
   [colour "white"]
   [colour "bright red"]
   [colour "pink"]
   [colour "green"]
   [colour "yellow"]
   [pattern "stripes"]
   [pattern "lace"]
   [pattern "embroidery"]
   )
)

(def product_bottom
  (db
   [brand "Zara"]
   [brand "Nautica"]
   [colour "bright"]
   [bottom "chinos"]
   [bottom "pencil skirts"]
   [bottom "ankle pants"]
   [fit "flare"]
   [fit "straight"]
   [fit "skinny"]
   [fit "wide leg"]
   )
)

(def product_dress
  (db
   [brand "Zara"]
   [brand "Kohls"]
   [brand "Nike"]
   [colour "white"]
   [colour "bright red"]
   [colour "pink"]
   [colour "green"]
   [colour "yellow"]
   [dress "shift"]
   [dress "sheath"]
   [dress "shirt"]
   [dress "line"]
   [dress "line"]
   [dress "rompers"]
   [dress "skaters"]
   )
)

(def product_accessory
  (db
   [brand "Apple iWatch"]
   [brand "Skagen"]
   [brand "Burberry"]
   [colour "Dark"]
   [colour "Brown"]
   [accessory "Handbag"]
   [accessory "Purse"]
   [accessory "Watch"])
)

(def product_footwear
  (db
   [brand "Reebok"]
   [brand "Bata"]
   [colour "Black"]
   [colour "White"]
   [footwear "Thing Sandals"]
   [footwear "Footbed sandals"]
   [footwear "Shoe - Sneakers"]
   [footwear "Shoe - Leather"]))


(def summer_trend
  (db
   [bottom "boyfrienddenim"]
   [pattern "crochet"]
   [pattern "lace"]
   [pattern "embroidery"]
   [dress "line"]
   [dress "rompers"]
   [dress "skaters"]
   [top "peasant tops"]
   [top "off shoulders"]
   [top "tank"]
   [accessory "Tote Bags"]
   [footwear "Gladiator Sandals"]
   [footwear "Flat Bed Sandals"]
   [footwear "Thing Sandals"]
   )
)

(def workshop_trend
  (db
   [bottom "chinos"]
   [bottom "pencil skirts"]
   [bottom "ankle pants"]
   [fit "flare"]
   [fit "straight"]
   [fit "skinny"]
   [fit "wide leg"]
   [dress "shift"]
   [dress "sheath"]
   [dress "shirt"]
   [dress "line"]
   [footwear "platform"]
   [footwear "high"]
   [footwear "wedges"]
   [footwear "pumps"]
   [accessory "analog time watch"]
   [top "Long sleeves"]
   [top "Three quartet"]
   [top "Basic"]
   [colour "Solid Plain Colors"]
   [colour "Dark"]
   [colour "Blues"]
   [colour "Browns"]
   [colour "Black"]
   [colour "Neutral"]
   [colour "Greys"]
   )
)

(def festival_trend
  (db
   [bottom "Printed Pallazo soft pants"]
   [bottom "Gauchos"]
   [bottom "pencil skirts"]
   [bottom "ankle pants"]
   [fit "flare"]
   [fit "straight"]
   [fit "wide leg"]
   [footwear "Thing Sandals"]
   [footwear "Footbed sandals"]
   [accessory "Fringe Macrame Tote handbag"]
   [top "Croppes shell tank"]
   [top "Neckline U neck"]
   [top "crew neck"]
   [top "sleeveless"]
   [top "plain solid colored tank tops"]
   [top "fashion vests"]
   [colour "Multi Colors"]
   [colour "Whites"]
   [colour "Blues"]
   [prints "Zig-zag"]
   [prints "Florals"]
   [prints "Paisley"]
   )
)

(defn get-products_0 []

      (println "Products Tops :->")

      (with-db product_top []
        (run* [q]
              (fresh [t p b c]
                     (top t)
                     (pattern p)
                     (brand b)
                     (colour c)

                     (== q {:top t :pattern p :brand b :colour c}))))

      )

(defn get-products_1 []

      (println "Products Bottom :->")

      (with-db product_bottom []
        (run* [q]
              (fresh [b c f]
                     (fit f)
                     (brand b)
                     (colour c)

                     (== q {:fit f :brand b :colour c}))))

      )

(defn get-products_2 []

      (println "Products Dress :->")

      (with-db product_dress []
        (run* [q]
              (fresh [b c d]
                     (dress d)
                     (brand b)
                     (colour c)

                     (== q {:dress d :brand b :colour c}))))

      )

(defn get-products_3 []

      (println "Products Accessory :->")

      (with-db product_accessory []
        (run* [q]
              (fresh [b c a]
                     (accessory a)
                     (brand b)
                     (colour c)

                     (== q {:accessory a :brand b :colour c}))))

      )

(defn get-products_4 []

      (println "Products Footwear :->")

      (with-db product_footwear []
        (run* [q]
              (fresh [b c f]
                     (footwear f)
                     (brand b)
                     (colour c)

                     (== q {:footwear f :brand b :colour c}))))

      )

(defn pattern_select_condition [p]
  (pattern p)
)

(defn top_select_condition [t]
  (== "tank" t)
)

    (defn get-summer-trends []

      (println "Festival  Trends :->")

      (with-db summer_trend []
        (run* [q]
              (fresh [t p b d a f]
                     (top_select_condition t)
                     (pattern_select_condition p)
                     (bottom b)
                     (dress d)
                     (accessory a)
                     (footwear f)
                     (== q {:trend "summer" :top t :pattern p :bottom b :dress d :accessory a :footwear f :rating 1}))))

      )

  (defn get-workwear-trends []

       (with-db workshop_trend []
             (run* [q]
                   (fresh [t f c b d a ft]
                    (top t)
                    (fit ft)
                    (colour c)
                    (bottom b)
                    (dress d)
                    (accessory a)
                    (footwear f)
                    (== q {:trend "workshop" :top t :fit f :colour c :bottom b :dress d :accessory a :footwear f :rating 1}))))
  )

  (defn get-festival-trends []

    (println "Festival  Trends :->")

    (with-db festival_trend []
           (run* [q]
                 (fresh [t f c b a pnt ft]
                  (top t)
                  (fit ft)
                  (colour c)
                  (bottom b)
                  (prints pnt)
                  (accessory a)
                  (footwear f)
                  (== q {:trend "festival" :top t :fit f :colour c :bottom b :prints pnt :accessory a :footwear f :rating 1}))))
  )


