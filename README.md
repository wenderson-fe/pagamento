# Pagamentos API
API responsável pelo gerenciamento de pagamentos no ecossistema Food, desenvolvida com Spring Boot e integrada a uma arquitetura de microserviços.

Esta aplicação expõe endpoints REST para criação, consulta, atualização, confirmação e exclusão de pagamentos, além de realizar integração com o serviço de Pedidos para agregação de dados.
## Visão Geral
A Pagamentos API é um microsserviço que faz parte de uma aplicação distribuída e tem como principais responsabilidades:
- Gerenciar o ciclo de vida de pagamentos
- Persistir dados de pagamentos em banco de dados relacional
- Integrar-se com outros serviços via Feign Client
- Registrar-se em um Service Registry (Eureka Server)
- Garantir resiliência em chamadas remotas utilizando Resilience4j
## Tecnologias Utilizadas
- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- Flyway
- Spring Cloud Netflix Eureka Client
- Spring Cloud OpenFeign
- Resilience4j
- ModelMapper
- Bean Validation
- Lombok
- Spring Boot Actuator
- Maven

