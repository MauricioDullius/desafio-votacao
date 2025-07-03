# 🗳️ Sistema de Votação

Uma API RESTful para gerenciamento de assembleias, pautas, membros e votos. Desenvolvida em Java 21 com Spring Boot, contendo validações, persistência e documentação via Swagger.

---

## 📋 Tabela de Conteúdos

- [📌 Sobre o Projeto](#-sobre-o-projeto)
- [✅ Funcionalidades](#-funcionalidades)
- [⚙️ Tecnologias Utilizadas](#️-tecnologias-utilizadas)
- [📁 Estrutura do Projeto](#-estrutura-do-projeto)
- [🔍 Documentação Swagger](#-documentação-swagger)
- [🏗️ Arquitetura e Boas Práticas](#️-arquitetura-e-boas-práticas)
- [🧪 Testes](#-testes)
- [🚀 Como Executar o Projeto](#-como-executar-o-projeto)
- [📄 Licença](#-licença)

---

## 📌 Sobre o Projeto

Este projeto foi desenvolvido como parte de um desafio técnico para DB, com o objetivo de implementar um sistema para gerenciamento de assembleias, pautas e votos dos associados.

---

## ✅ Funcionalidades

- Cadastro, atualização e listagem de **membros**
- Criação e gerenciamento de **assembleias**
- Registro e acompanhamento de **pautas**
- Registro e recuperação de **votos**
- Validação de dados e regras de negócio
- Tratamento de erros com exceções personalizadas
- Integração com Swagger para documentação da API
- Testes unitários e de integração

---

## ⚙️ Tecnologias Utilizadas

| Finalidade              | Tecnologia                 |
|------------------------|----------------------------|
| Linguagem              | Java 21                    |
| Framework Principal    | Spring Boot 3.2.5          |
| Build Tool             | Gradle                     |
| Migração de Banco      | Flyway                     |
| Documentação           | SpringDoc / Swagger        |
| Persistência           | Spring Data JPA            |
| Testes                 | JUnit, MockMvc             |
| Mapeamento de Objetos  | MapStruct                  |
| Banco de Dados         | MySQL                      |
| Logs                   | SLF4J (LoggerFactory)      |
| Gerenciamento de Depend.| Spring Dependency Plugin   |

---

## 📁 Estrutura do Projeto

src/  
└── main/  
  └── java/  
    └── br.com.db.system.votingsystem.v1/  
      ├── client/                 # Clientes HTTP e DTOs relacionados  
      ├── config/                 # Configurações do projeto (ex: OpenAPI config)  
      ├── controller/             # Controladores REST  
      │  └── doc/                # Interfaces para documentação Swagger  
      ├── dto/                    # Data Transfer Objects (DTOs)  
      ├── exception/              # Classes de exceção personalizadas e handlers  
      ├── mapper/                 # MapStruct mappers  
      ├── model/  
      │  ├── entity/             # Entidades JPA  
      │  └── enums/              # Enums usados no projeto  
      ├── repository/             # Interfaces de acesso a dados (JPA repositories)  
      └── service/                # Serviços com regras de negócio  
      └── util                    # Classes utilitárias 

└── test/  
  └── java/  
    └── br.com.db.system.votingsystem.v1/  
      ├── controller/            # Testes de integração dos controllers  
      ├── mapper/                # Testes dos mappers  
      ├── model/  
      │  └── entity/            # Testes das entidades  
      └── service/               # Testes dos serviços  

---

## 🔍 Documentação Swagger

A documentação da API pode ser acessada via Swagger UI:

http://localhost:8080/swagger-ui/index.html

---

## 🏗️ Arquitetura e Boas Práticas

- Utilização do padrão REST para definição dos endpoints.
- Validações de dados aplicadas tanto na camada de DTOs quanto na de serviços.
- Tratamento consistente de exceções com classes personalizadas.
- Implementação de testes unitários e de integração para garantir a qualidade do código.
- Integração com o Flyway para versionamento e migração do banco de dados.
- Utilização de MapStruct para mapeamento eficiente entre entidades e DTOs.
- **Validação Fake de CPF:**  
  Para fins de desenvolvimento e testes, a validação do CPF é simulada de forma aleatória. Ou seja, o sistema gera um resultado booleano (válido ou inválido) com base em uma probabilidade, sem consultar um serviço externo real. Essa abordagem facilita o desenvolvimento sem dependências externas.

---

## 🧪 Testes

Os testes estão localizados em `src/test`:

- Testes de unidade: services, mappers, entidades  
- Testes de integração: controllers com MockMvc  

Execute os testes com:

```bash
./gradlew test
```
## 🚀 Como Executar o Projeto

### 1. Pré-requisitos

- Java 21  
- MySQL  
- Docker (opcional)  
- Gradle ou suporte via IDE  

### 2. Configurar Banco de Dados

Defina a conexão com o banco no arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/voting_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

> ⚠️ O Flyway será responsável pela criação do schema com base nos arquivos `.sql` em `src/main/resources/db/migration`.

### 3. Executar o Projeto

Via terminal:

```bash
./gradlew bootRun
```

Ou diretamente via sua IDE (ex: IntelliJ ou STS).

---

## 📄 Licença

Este projeto está licenciado sob a licença Apache 2.0.
