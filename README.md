
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

### Estadia
   
   A `Estadia` é a entidade central do sistema. Ela vincula um Veículo a uma Vaga e gerencia o controle de ocupação, tempo de permanência, cálculo de cobrança e o fluxo operacional do estacionamento.

   * Registrar entrada de veículo (Iniciar Estadia).
   * Registrar saída do veículo e calcular o valor devido (Gerar Cobrança).
   * Confirmar o pagamento e concluir o ciclo (Quitar Estadia).

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
   * **Efeito:** O status passa para `ENCERRADA`.
   * **Restrição:** Uma estadia encerrada é estritamente **imutável** para garantir a integridade do domínio. Nenhuma alteração posterior é permitida.

   ### Endpoints – Estadia
   
   | Método | Endpoint                        | Descrição                                                            |
   | ------ | ------------------------------- | -------------------------------------------------------------------- |
   | POST   | /estadias                       | Inicia uma nova estadia (vincula veículo e vaga)                     |
   | PATCH  | /estadias/{id}/cobranca         | | Registra a saída, libera a vaga e gera a cobrança (`EM_COBRANCA`). |



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

* Implementação de Segurança (Spring Security + JWT) para controle de acesso.
* Implementação de exclusão lógica para manter rastreabilidade e auditoria no banco de dados.
* Implementação do último estado do ciclo de vida de Estadia (ENCERRADA).
* Relatórios e histórico.

