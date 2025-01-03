# Desafio 02 - Se Beber Não Code

## Sobre o Projeto

Este projeto consiste em um sistema com arquitetura de microsserviços que inclui autenticação, gerenciamento de produtos, e notificações. O desafio utiliza serviços como RabbitMQ para comunicação entre microsserviços e um banco de dados relacional para armazenamento de dados.

## Tecnologias Utilizadas

<div style="display: flex; flex-wrap: wrap; gap: 5px; justify-content: center">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" alt="Java 17" height="30" width="40">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original.svg" alt="Spring Boot 3.X" height="30" width="40">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/rabbitmq/rabbitmq-original.svg" alt="RabbitMQ" height="30" width="40">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" alt="PostgreSQL" height="30" width="40">
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/docker/docker-original.svg" alt="Docker" height="30" width="40">
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
| PATCH      | `/api/users/:id/password` | Atualiza a senha do usuário| 

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
Gerencia, através do broker RabbitMQ o recebimento de notificações quando um usuário cadastra ou altera suas informações ou senha, salvando um e-mail no banco de dados e o enviando para o e-mail utilizado no cadastro do usuário.


## Configuração do Projeto

### Pré-requisitos

- Java 17
- Docker

### Passos para Executar

####  Clone o repositório:
```bash
git clone https://github.com/LiedsonLB/PBDesafio02-SeBeberNaoCode.git
```

#### Baixando e executando os containers:
Na pasta raiz do repositório clonado, execute o seguinte script no terminal:
```bash
docker compose up --build
```
Este comando irá unir todos os microserviços, baixar o banco de dados e o mensageiro que sao dependencias necessárias para a execução e irá rodá-los conteinerizados nesta ordem:
| Etapa de Execução   | Componentes                            |
|---------------------|----------------------------------------|
|  Inicialização     | RabbitMQ, PostgreSQL                  |
|  Servidor          | severeureka                           |
|  Microserviços     | ms-notification, ms-authorization, ms-products |
|  Gateway           | ms-cloudgateway                       |

## Documentação de endpoints
Para acessar a documentação dos endpoint utilizados podemos acessar o swagger de cada microserviço, com o container rodando, execute:  
ms-authorization:  
http://localhost:8081/swagger-ui/index.html  
ms-products:  
http://localhost:8083/swagger-ui/index.html

## Funcionalidades

- **RabbitMQ**: É um broker de mensageria que faz a gestão e comunicação assíncrona entre os microserviços de **notificação** e **autenticação**.
- **Postgres**: É um banco de dados SQL que armazena as informações geradas por cada microserviço.
- **Eureka Server**: Facilita a comunicação entre os microserviços, funcionando como um serviço de registro e descoberta de serviços.
- **Microserviço de Autenticação**: Responsável por criar, autenticar e autorizar usuários dentro da API.
- **Microserviço de Notificação**: Responsável por enviar e-mails quando algum usuário for cadastrado ou tiver alguma de suas informações alteradas, além de guardar esses e-mails.
- **Microserviço de Produtos**: Tem como função criar, armazenar, listar ou alterar produtos dentro da API, e também gerenciar categorias.
- **Gateway**: Tem como função usar um único endereço de acesso na web para fazer a conexão com cada API, redirecionando automaticamente para a porta correta de cada microserviço, sem que o usuário precise fazer isso manualmente.
- **Docker**: Contêineriza toda a aplicação, permitindo que ela seja escalável, possa ser baixada e executada em qualquer dispositivo com acesso ao Docker, além de facilitar sua distribuição e execução.

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
- tb_notification: Armazena emails e status de envio de notificações.

# Autores

- [Jonatha Carvalho](https://github.com/jowgaze)
- [Guilherme Johann ](https://github.com/lgjohann)
- [Eduardo Marchesan](https://github.com/edMarchesan)
- [Francisco Liédson](https://github.com/LiedsonLB)
