
# API de Controle de Estacionamento
![Java](https://img.shields.io/badge/Java-17-orange)

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-brightgreen)

![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)


## Descrição

API REST desenvolvida em **Spring Boot** para controle de vagas de estacionamento. O projeto tem como objetivo o estudo prático e aprofundado de boas práticas de Engenharia de Software, aplicando Clean Code, Domain-Driven Design (DDD) na organização das regras de negócio, e padronização rigorosa de respostas HTTP.

Atualmente, o sistema contempla o gerenciamento completo de **Vagas**, **Veículos** e a orquestração do ciclo de vida financeiro e operacional de **Estadias** (da entrada do veículo até a quitação final).


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

O projeto segue uma arquitetura em camadas bem definida, separando responsabilidades:

* **controller**: camada responsável pelos endpoints REST (Thin Controllers).
* **service**: orquestração de casos de uso e injeção de dependências.
* **repository**: comunicação com o banco de dados (Spring Data).
* **model**: entidades ricas (Domain Models) que encapsulam as regras de transição de estado.
* **dto**: objetos de transferência de dados (Records) para entrada e saída.
* **exception**: tratamento global de erros (`@ControllerAdvice`) blindando o domínio.


## Funcionalidades

### Vaga

  * Cadastrar vaga
  * Listar todas as vagas
  * Buscar vaga por ID
  * Ocupar vaga
  * Liberar vaga
  * Remover vaga (apenas se estiver livre)


  ### Regras de Negócio (Vaga)

  * O número da vaga é único.
  * Toda vaga é criada como **livre**.
  * Não é permitido:

    * Cadastrar duas vagas com o mesmo número.
    * Ocupar uma vaga já ocupada.
    * Liberar uma vaga já livre.
    * Remover uma vaga que esteja ocupada.


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

   * A placa do veículo é única e validada logicamente.
   * Não é possível alterar a placa após o registro.
   * O veículo é uma entidade de cadastro estático (não controla tempo, entrada, saída ou pagamento, isso é responsabilidade da Estadia).
   * O veículo não controla tempo, entrada, saída ou pagamento
   * Não é permitido:

      * Cadastrar dois veículos com a mesma placa.
      * Atualizar a placa.
      * Remover um veículo.

   ### Endpoints – Veículo

   | Método | Endpoint               | Descrição                               |
   | ------ | ---------------------- | --------------------------------------- |
   | POST   | /veiculos              | Cadastra um novo veículo                |
   | GET    | /veiculos/{id}         | Busca veículo por ID                    |
   | PATCH  | /veiculos/{id}         | Altera o tipo do veículo                |

### Estadia
   
   A `Estadia` é a entidade central do sistema. Ela vincula um Veículo a uma Vaga e gerencia o controle de ocupação, tempo de permanência, cálculo de cobrança e o fluxo operacional do estacionamento.

   * Registrar entrada de veículo (Iniciar Estadia).
   * Registrar saída do veículo e calcular o valor devido (Gerar Cobrança).
   * Confirmar o pagamento e concluir o ciclo (Quitar Estadia).
   * Listar estadias com suporte a paginação e filtro por status.

   ### Regras de Negócio (Estadia)
   
   O ciclo de vida da Estadia segue uma Máquina de Estados Finita rigorosa: `ATIVA` → `EM_COBRANCA` → `ENCERRADA`. As transições de estado são protegidas e só ocorrem através de métodos específicos da entidade.

   **1. Estado: ATIVA (Entrada do Veículo)**
   * **Condição:** Exige uma vaga livre e um veículo válido.
   * **Efeito:** A vaga passa a ficar ocupada, a `dataEntrada` é registrada e o status é `ATIVA`.
   * **Restrição:** Não possui data de saída ou valor calculado.

   **2. Estado: EM_COBRANCA (Saída do Veículo / Geração da Cobrança)**
   * **Condição:** O status atual deve ser obrigatoriamente `ATIVA`.
   * **Efeito:** A vaga associada é liberada, a `dataSaida` é registrada e o sistema calcula o `valorFinal` da cobrança.
   * **Regra de Cálculo:**
      
      * **Até 15 minutos:** Taxa fixa de R$ 5,00.
      * **Após 15 minutos:** R$ 5,00 + tarifa proporcional de R$ 10,00 por hora excedente. O valor é arredondado para duas casas decimais.

   **3. Estado: ENCERRADA (Pagamento Confirmado)**
   * **Condição:** O status atual deve ser obrigatoriamente `EM_COBRANCA`.
   * **Efeito:** A `dataPagamento` é registrada e o status transita para `ENCERRADA`.
   * **Restrição:** Uma estadia encerrada é estritamente **imutável** para garantir a integridade do domínio. Nenhuma alteração posterior é permitida.

   ### Endpoints – Estadia
   
   | Método | Endpoint                        | Descrição                                                            |
   | ------ | ------------------------------- | -------------------------------------------------------------------- |
   | POST   | /estadias                       | Inicia uma nova estadia (vincula veículo e vaga)                     |
   | GET    | /estadias                       | Lista as estadias de forma paginada (com filtro opcional de status)  |
   | PATCH  | /estadias/{id}/cobranca         | Registra a saída, libera a vaga e gera a cobrança (`EM_COBRANCA`)    |
   | PATCH  | /estadias/{id}/quitacao         | Confirma o pagamento e encerra o ciclo da estadia (`ENCERRADA`)      |

   **Detalhes da Listagem (`GET /estadias`)**

   **Parâmetros de Consulta (Query Params) Opcionais:**
   * `status` (string): Filtra as estadias por status (ex: `ATIVA`, `EM_COBRANCA`, `ENCERRADA`). Se omitido, retorna todas.
   * `page` (int): Número da página (padrão: `0`).
   * `size` (int): Quantidade de itens por página (padrão: `10`).
   * `sort` (string): Campo de ordenação (padrão: `dataEntrada,ASC`).


## Tratamento de Erros

A aplicação utiliza um **tratador global de exceções** (`@ControllerAdvice`) para padronizar as respostas de erro, garantindo que detalhes da infraestrutura não vazem para o cliente.

Exemplos de status HTTP mapeados:

* **400 Bad Request** – Dados inválidos ou mal formatados na requisição.
* **404 Not Found** – Recurso não encontrado no banco de dados.
* **409 Conflict / 422 Unprocessable Entity** – Violação de estado ou regra de negócio.

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

* Implementação de Segurança (Spring Security + JWT) para controle de acesso.
* Geração de documentação automatizada via OpenAPI/Swagger.
* Implementação de exclusão lógica (Soft Delete) para manter rastreabilidade e auditoria no banco de dados.
* Relatórios e histórico de faturamento.

