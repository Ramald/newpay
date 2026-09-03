# NewPay

NewPay é uma API REST desenvolvida com Java, Spring Boot e PostgreSQL para gerenciamento de contas digitais.

O projeto simula operações financeiras básicas, como cadastro de clientes, abertura de contas, depósitos, saques, transferências entre contas e consulta de extrato.

O foco principal do projeto foi o desenvolvimento back-end, aplicando arquitetura em camadas, regras de negócio, persistência com JPA/Hibernate, validações, tratamento global de exceções e testes unitários.

O front-end foi desenvolvido apenas como uma interface demonstrativa para consumir a API REST.

---

## Tecnologias utilizadas

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Bean Validation
- JUnit
- Mockito
- Maven
- Git/GitHub

---

## Funcionalidades

### Clientes

- Cadastrar cliente
- Listar clientes
- Buscar cliente por ID
- Atualizar cliente
- Deletar cliente

### Contas

- Abrir conta para um cliente
- Listar contas
- Realizar depósito
- Realizar saque
- Realizar transferência entre contas
- Consultar extrato paginado

---

## Regras de negócio

- Não é permitido cadastrar CPF duplicado
- Não é permitido cadastrar e-mail duplicado
- Não é permitido abrir mais de uma conta para o mesmo cliente
- Não é permitido sacar valor maior que o saldo disponível
- Não é permitido transferir valor maior que o saldo disponível
- Não é permitido transferir para a mesma conta
- Toda movimentação financeira é registrada no extrato

---

## Estrutura do projeto

```text
src/main/java/com/ramaldes/newpay
├── controller
├── service
├── repository
├── model
├── dto
├── exception
└── handler
```

# Como rodar o projeto localmente
Pré-requisitos
Java instalado
Maven instalado
PostgreSQL instalado
Banco de dados criado
Banco de dados

Crie um banco chamado:

```text
newpay_db
```
Configure as variáveis de ambiente:
```text
DB_URL=jdbc:postgresql://localhost:5432/newpay_db
DB_USERNAME=postgres
DB_PASSWORD=sua_senha
```
Depois execute a aplicação Spring Boot.

## Documentação da API

A API possui documentação com Swagger/OpenAPI.

Com a aplicação rodando localmente, acesse:

```text
http://localhost:8080/swagger-ui.html
```
Testes

O projeto possui testes unitários para validar regras de negócio dos serviços de clientes e contas.

Foram utilizados:

JUnit
Mockito
Assertivas
Mocks
Verificação de chamadas
ArgumentCaptor

Para executar os testes:
```text
mvn test
```
Front-end demonstrativo

Este projeto possui uma interface front-end simples criada apenas para consumir a API REST e demonstrar as funcionalidades do back-end.

O foco principal do projeto é a API desenvolvida com Java e Spring Boot.

Status do projeto

Projeto em desenvolvimento.

# Versão atual:

API REST funcional
Banco PostgreSQL integrado
Testes unitários implementados
Front-end demonstrativo consumindo a API localmente

# Próximas melhorias planejadas:

Autenticação com Spring Security e JWT
Testes de integração
Docker
Deploy controlado em ambiente demonstrativo
