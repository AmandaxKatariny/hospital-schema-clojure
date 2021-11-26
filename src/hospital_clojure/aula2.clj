(ns hospital-clojure.aula2
  (:use clojure.pprint)
  (:require [schema.core :as s]))

(s/set-fn-validation! true)

;;(s/defrecord Paciente
;  [id :- Long, nome :- s/Str])
;(pprint (Paciente. 15 "Amanda"))
;(pprint (Paciente. " 15" "Amanda"))
;(pprint (map->Paciente  {15 "Amanda"})
;(pprint (map->Paciente  {"15" "Amanda"})

(def Paciente
  "Schema de um paciente"
  {:id s/Num, :nome s/Str})

(pprint (s/explain Paciente))
(pprint (s/validate Paciente {:id 15, :nome "guilherme"}))

; tipo é pego pelo schema. mas poderiamos argumentar que esse
; tipo de erro seria pego em testes automatizados com cobertura boa
; (pprint (s/validate Paciente {:id 15, :nome "guilherme"}))

; mas... entra a questão do querer ser forward compatible OU NÃO
; entender esse trade-off
; sistemas externos não me quebrarão ao adicionar campos novos (foward compatible)
; no nosso validate não estamos sendo forward compatible (pode ser interessante quando quero analisar mudanças
;(pprint (s/validate Paciente {:id 15, :nome "guilherme", :plano [:raio-x]}))

; chaves que são keywords em schemas são por padrão OBRIGATORIAS
;(pprint (s/validate Paciente {:id 15})

; tipo de retorno com schema
; força a validação na saída da função
; (s/def novo-paciente :- Paciente
;   [id :- s/Num, nome :- s/Str]
;   {:id id, :nome nome, :plano []})

(s/defn novo-paciente :- Paciente
  [id :- s/Num, nome :- s/Str]
  {:id id, :nome nome})

(pprint (novo-paciente 15 "Guilherme"))

;Função pura simples, facil de testar
(defn estritamente-positivo? [x]
  (> x 0))

;(def EstritamentePositivo (s/pred estritamente-positivo?))
(def EstritamentePositivo (s/pred estritamente-positivo? 'estritamente-positivo?))

(pprint (s/validate EstritamentePositivo 15))
; (pprint (s/validate EstritamentePositivo 0))
; (pprint (s/validate EstritamentePositivo -15))

(println "\n versão 2")

(s/def Paciente
  "Schema de um paciente"
  {:id (s/constrained s/Int pos?), :nome s/Str})
;é por isso que é importante debulhar a documentação
; já existe pos? e já existe pos-int
; dica: sempre debulhar documentação


(pprint (s/validate Paciente {:id 15, :nome "guilherme"}))
;(pprint (s/validate Paciente {:id -15, :nome "guilherme"}))
;(pprint (s/validate Paciente {:id 0, :nome "guilherme"}))

; não é recomendado usar lambdas dentro dos schemas
;pois fica confuso ou perde a legibilidade do schema
;(s/def Paciente
;  "Schema de um paciente"
;  {:id (s/constrained s/Int #(> % 0)), :nome s/Str})










