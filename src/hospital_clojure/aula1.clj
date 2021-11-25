(ns hospital-clojure.aula1
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(defn adicionar-paciente
  [pacientes, paciente]
  (if-let [id (:id paciente)]
          (assoc pacientes id paciente)
          (throw (ex-info "Paciente não possui id" {:paciente paciente}))))

(defn imprime-relatorio-de-pacientes
  [visitas, paciente]
  (println "Visitas do Paciente" paciente "São:" (get visitas paciente)))

; {15 [], 20 []}
(defn adiciona-visita
  [visitas, paciente, novas-visitas]
  (if (contains? visitas paciente)
    (update visitas paciente concat novas-visitas)
    (assoc visitas paciente novas-visitas)))

(defn testa-uso-de-paciente
  []
  (let [guilherme {:id 15, :nome "Guilherme"}
        daniela {:id 20, :nome "Daniela"}
        paulo {:id 25, :nome "Paulo"}
        ;uma variação com reduce, mais natural
        pacientes (reduce adicionar-paciente {} [guilherme, daniela, paulo])

        ;uma variação com shadowing, não fica bom
        visitas {}
        visitas (adiciona-visita visitas 15 ["01/01/2019"])
        visitas (adiciona-visita visitas 20 ["01/02/2019", "01/01/2020"])
        visitas (adiciona-visita visitas 15 ["01/03/2019"])]

    (pprint pacientes)
    (pprint visitas)

    ; grande problema pois estou usando o simbolo paciente
    ; em varios lucares do meu programa com significados diferentes
    (imprime-relatorio-de-pacientes visitas daniela)
    (println (get visitas 20))))

(testa-uso-de-paciente)

(pprint (s/validate long 15))
; (pprint (s/validate long "Amanda"))
; (pprint (s/validate long [15, 13]))

(s/set-fn-validation! true)

(s/defn teste-simples
  [x :- Long]
  (println x))

(teste-simples 30)
; (teste-simples "Amanda")

(s/defn imprime-relatorio-de-pacientes
  [visitas, paciente :- Long]
  (println "Visitas do paciente" paciente "São:" (get visitas paciente)))

; agora conseguimos o erro em tempo de *execução* que diz
; que o valor passado como parametro não condiz com o schema LongŁ
; (testa-uso-de-paciente)

(s/defn novo-paciente
  [id :- Long, nome :- s/Str]
  {:id id, :nome nome})

(println (novo-paciente 15 "Guilherme"))
; (println (novo-paciente "Guilherme" 15))
