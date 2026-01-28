
# API de Controle de Estacionamento
![Java](https://img.shields.io/badge/Java-17-orange)

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-brightgreen)

![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)


## Descrição

API REST desenvolvida em **Spring Boot** para controle de vagas de estacionamento. O projeto tem como objetivo o estudo de boas práticas de desenvolvimento backend, organização em camadas, regras de negócio e padronização de respostas HTTP.

Atualmente, o sistema contempla o **CRUD completo da entidade Vaga**, servindo como base para futuras evoluções, como a integração com veículos e histórico de ocupação.



## Tecnologias Utilizadas

* Java 17
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* Flyway
* MySQL
* Maven



## Estrutura do Projeto

O projeto segue uma organização em camadas, separando responsabilidades:

* **controller**: camada responsável pelos endpoints REST
* **service**: contém as regras de negócio da aplicação
* **repository**: comunicação com o banco de dados
* **model**: entidades JPA
* **dto**: objetos de transferência de dados
* **exception**: exceções customizadas e tratamento global de erros


## Funcionalidades

### Vaga

  * Cadastrar vaga
  * Listar todas as vagas
  * Buscar vaga por ID
  * Ocupar vaga
  * Liberar vaga
  * Remover vaga (apenas se estiver livre)


  ### Regras de Negócio (Vaga)

  * O número da vaga é único
  * Toda vaga é criada como **livre**
  * Não é permitido:

    * Cadastrar duas vagas com o mesmo número
    * Ocupar uma vaga já ocupada
    * Liberar uma vaga já livre
    * Remover uma vaga que esteja ocupada


  ### Endpoints – Vaga

  | Método | Endpoint            | Descrição                 |
  | ------ | ------------------- | ------------------------- |
  | POST   | /vagas              | Cadastra uma nova vaga    |
  | GET    | /vagas              | Lista todas as vagas      |
  | GET    | /vagas/{id}         | Busca vaga por ID         |
  | PATCH  | /vagas/{id}/ocupar  | Marca a vaga como ocupada |
  | PATCH  | /vagas/{id}/liberar | Marca a vaga como livre   |
  | DELETE | /vagas/{id}         | Remove uma vaga livre     |

  ### Veículo

   * Cadastrar veículo
   * Buscar veículo por ID
   * Atualizar o tipo de veículo

   ### Regras de Negócio (Veículo)

   * A placa do veículo é única
   * Não é possível alterar a placa
   * O veículo não possui estado (não está ocupado, livre, estacionado, etc.)
   * O veículo não controla tempo, entrada, saída ou pagamento
   * Não é permitido:

      * Cadastrar dois veículos com a mesma placa
      * Atualizar a placa
      * Remover um veículo

   ### Endpoints – Veículo

   | Método | Endpoint               | Descrição                               |
   | ------ | ---------------------- | --------------------------------------- |
   | POST   | /veiculos              | Cadastra um novo veículo                |
   | GET    | /veiculos/{id}         | Busca veículo por ID                    |
   | PATCH  | /veiculos/{id}         | Altera o tipo do veículo                |



## Tratamento de Erros

A aplicação utiliza um **tratador global de exceções** (`@ControllerAdvice`) para padronizar as respostas de erro.

Exemplos de status HTTP utilizados:

* **400 Bad Request** – dados inválidos
* **404 Not Found** – recurso não encontrado
* **409 Conflict** – violação de regra de negócio

As respostas seguem um padrão com código e mensagem de erro.



## Como Executar o Projeto

### Pré-requisitos

Antes de iniciar, certifique-se de ter instalado:

* **Java 17**
* **Maven**
* **MySQL**



### Passo a passo

1. **Clone o repositório**

```bash
git clone https://github.com/edsantos-dev/controle-estacionamento-api.git
```

2. **Crie o banco de dados no MySQL**

```sql
CREATE DATABASE estacionamento_api;
```

3. **Configure o arquivo `application.properties`**

   Ajuste as credenciais de acesso ao banco de dados conforme seu ambiente:

```properties
spring.datasource.url=jdbc:mysql://localhost/estacionamento_api
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

4. **Execute a aplicação**

   Você pode executar o projeto pela IDE ou via terminal:

```bash
mvn spring-boot:run
```

5. **Migrations automáticas**
   
   As tabelas do banco de dados são criadas automaticamente pelo **Flyway** na inicialização da aplicação.



## Evoluções Futuras

* Modelagem da entidade **Estadia**
* Vincular Veículo e Vaga através da Estadia
* Cálculo de tempo estacionado
* Simulação de cobrança
* Relatórios e histórico



## Observações

Atualmente, a exclusão de vagas é feita de forma **física**. Em um cenário real, a aplicação pode ser evoluída para **exclusão lógica**, visando auditoria e rastreabilidade.

Este projeto faz parte do estudo e consolidação de conceitos de desenvolvimento backend com Spring Boot.

