(ns hospital-clojure.aula3
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(s/set-fn-validation! true)

(def PosInt (s/pred pos-int?))

(def Paciente
  {:id (s/pred pos-int?), :nome s/Str})

(def Paciente
  {:id PosInt, :nome s/Str})

(s/defn novo-paciente :- Paciente
  [id :- PosInt
   nome :- s/Str]
  {:id id, :nome nome})

(pprint (novo-paciente 15, "Guilherme"))
;(pprint (novo-paciente -5, "Guilherme"))

(defn maior-ou-igual-a-zero? [x] (>= x 0))

(def ValorFinanceiro (s/constrained s/Num maior-ou-igual-a-zero?))

(def Pedido
  {:paciente     Paciente
   :valor        ValorFinanceiro
   :procedimento s/Keyword})

; Será que faz sentido "mini-schema" como aliases
; (def Procedimento s/Keyword)

(s/defn novo-pedido :- Pedido
  [paciente :- Paciente
   valor :- ValorFinanceiro
   procedimento :- s/Keyword]
  {:paciente paciente, :valor valor, :procedimento procedimento})

(pprint (novo-pedido (novo-paciente 15, "Amanda"), 15.33, :raio-x))
;(pprint (novo-pedido (novo-paciente 15, "Amanda"), -15.33, :raio-x))

(def Numeros [s/Num])

(pprint (s/validate Numeros [15]))
(pprint (s/validate Numeros [15, 13]))
(pprint (s/validate Numeros [15, 13, 132, 321, 23.2]))
(pprint (s/validate Numeros [0]))
;(pprint (s/validate Numeros [nil]))
(pprint (s/validate Numeros []))
(pprint (s/validate Numeros nil))

(def Plano [s/Keyword])
(pprint (s/validate Plano [:raio-x]))

(def Paciente
  {:id PosInt, :nome s/Str, :plano Plano})

(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [:raio-x, :ultrassom]}))

(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano [:raio-x]}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano []}))
(pprint (s/validate Paciente {:id 15, :nome "Guilherme", :plano nil}))

;(pprint (s/validate Paciente {:id 15, :nome "Guilherme"}))