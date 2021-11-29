(ns hospital-clojure.aula5
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(s/set-fn-validation! true)

(def PosInt (s/pred pos-int? 'inteiro-positivo))
(def Plano [s/Keyword])
(def Paciente
  {:id                          PosInt,
   :nome                        s/Str,
   :plano                       Plano,
   (s/optional-key :nascimento) s/Str})

(def Pacientes
  {PosInt Paciente})

(pprint (s/validate Pacientes {}))

(def Visitas
  {PosInt [s/Str]})

; romovido o IF e THROW pois
; o schema GARANTIU a existencia do ID e a Validade do ID
; ... se a validação estiver ativa
(s/defn adiciona-paciente
  [pacientes :- Pacientes, paciente :- Paciente]
  (let [id (:id paciente)]
    (assoc pacientes id paciente)))


(s/defn adiciona-visita :- Visitas
  [visitas :- Visitas, paciente :- PosInt, novas-visitas :- [s/Str]]
  (if (contains? visitas paciente)
    (update visitas paciente concat novas-visitas)
    (assoc visitas paciente novas-visitas)))


(s/defn imprime-relatorio-de-pacientes
  [visitas :- Visitas, paciente :- PosInt]
  (println "Visitas do Paciente" paciente "São:" (get visitas paciente)))

(defn testa-uso-de-paciente []
  (let [guilherme {:id 15, :nome "Guilherme", :plano []}
        daniela {:id 20, :nome "Daniela", :plano []}
        paulo {:id 25, :nome "Paulo", :plano []}

        ;uma variação com reduce, mais natural
        pacientes (reduce adiciona-paciente {} [guilherme, daniela, paulo])

        ;uma variação com shadowing, não fica bom
        visitas {}
        visitas (adiciona-visita visitas 15 ["01/01/2019"])
        visitas (adiciona-visita visitas 20 ["01/02/2019", "01/01/2020"])
        visitas (adiciona-visita visitas 15 ["01/03/2019"])]

    (pprint pacientes)
    (pprint visitas)

    ; grande problema pois estou usando o simbolo paciente
    ; em varios lucares do meu programa com significados diferentes
    (imprime-relatorio-de-pacientes visitas 20)))

(testa-uso-de-paciente)