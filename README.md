# Desafio 02 - Se Beber Não Code

## Sobre o Projeto

Este projeto consiste em um sistema com arquitetura de microsserviços que inclui autenticação, gerenciamento de produtos, e notificações. O desafio utiliza serviços como RabbitMQ para comunicação entre microsserviços e um banco de dados relacional para armazenamento de dados.

## Tecnologias Utilizadas

<div style="display: flex; flex-wrap: wrap; gap: 5px; justify-content: center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" alt="Java 17" height="30" width="40">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" alt="Spring Boot 3.X" height="30" width="40">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/rabbitmq/rabbitmq-original.svg" alt="RabbitMQ" height="30" width="40">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" alt="PostgreSQL" height="30" width="40">
</div>

## Microsserviços

### 1. Gateway
Responsável por validar tokens e encaminhar requisições para os serviços apropriados.

---

### 2. MS-Authorization
Gerencia autenticação e autorização dos usuários.

| **Método** | **Endpoint**       | **Descrição**                   |
|------------|--------------------|---------------------------------|
| POST       | `/users`           | Cria um novo usuário           |
| GET        | `/users/:id`       | Retorna detalhes de um usuário |
| PUT        | `/users/:id`       | Atualiza informações do usuário|
| POST       | `/oauth/token`     | Gera token de autenticação     |

---

### 3. MS-Products
Gerencia produtos e categorias.

| **Método** | **Endpoint**                                  | **Descrição**                               |
|------------|-----------------------------------------------|---------------------------------------------|
| POST       | `/products`                                   | Cria um novo produto                        |
| GET        | `/products/:id`                               | Retorna detalhes de um produto              |
| GET        | `/products/?page=1&linesPerPage=5&direction=DESC&orderBy=name` | Lista produtos paginados                   |
| DELETE     | `/products/:id`                               | Remove um produto                           |
| POST       | `/categories`                                 | Cria uma nova categoria                     |
| GET        | `/categories/:id`                             | Retorna detalhes de uma categoria           |
| DELETE     | `/categories/:id`                             | Remove uma categoria                        |
| PUT        | `/categories/:id`                             | Atualiza informações de uma categoria       |

---

### 4. MS-Notification
Gerencia o envio de notificações via e-mail e salva informações de envio no banco de dados.

| **Método** | **Endpoint**       | **Descrição**                      |
|------------|--------------------|------------------------------------|
| ---        | ---                | Comunicação interna com RabbitMQ  |

---

## Configuração do Projeto

### Pré-requisitos

- Java 17

### Passos para Executar

####  Clone o repositório:
```bash
git clone https://github.com/LiedsonLB/PBDesafio02-SeBeberNaoCode.git
```

#### Configure as variáveis de ambiente nos microsserviços:

- Banco de dados (URL, usuário, senha)
Configurações do RabbitMQ

#### Execute os microsserviços individualmente:
- ms-cloudgateway
```bash
cd ms-cloudgateway
mvn spring-boot:run
```
- ms-authorization
```bash
cd ms-authorization
mvn spring-boot:run
```
- ms-products
```shell
cd ms-products
mvn spring-boot:run
```
Acesse os serviços pelos endpoints fornecidos acima.
# Testes

Para rodar os testes :

```bash
mvn test
```

## Cobertura de Testes

Para gerar o relatório de cobertura de testes, execute:

```bash
mvn clean test jacoco:report
```

O relatório de cobertura estará disponível em:
```
target/site/jacoco/index.html
```

## Estrutura do Banco de Dados
### Tabelas

- tb_user: Armazena informações dos usuários.
- tb_role: Armazena os papéis dos usuários.
- tb_user_role: Relaciona usuários com papéis.
- tb_product: Armazena informações dos produtos.
- tb_category: Armazena informações das categorias.
- tb_product_category: Relaciona produtos com categorias.
- tb_notification: Armazena status de envio de notificações.

# Autores

- [Jonatha Carvalho](https://github.com/jowgaze)
- [Guilherme Johann ](https://github.com/lgjohann)
- [Eduardo Marchesan](https://github.com/edMarchesan)
- [Francisco Liédson](https://github.com/LiedsonLB)