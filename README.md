# **Microsserviço de Autenticação com JWT**

Este projeto é um microsserviço back-end construído em Java com Spring Boot, focado em fornecer um sistema de autenticação e autorização robusto utilizando JSON Web Tokens (JWT).

## **🚀 Funcionalidades**

* **Registro de Usuário**: Endpoint para criação de novos usuários com validação de dados (email, senha) e criptografia de senha (BCrypt).  
* **Login com JWT**: Geração de token JWT na autenticação, com tempo de expiração de 1 hora.  
* **Gerenciamento de Status**: Permite que um usuário autenticado ative ou desative sua própria conta.  
* **Segurança**: Rotas protegidas que exigem um token JWT válido para acesso.  
* **Persistência de Dados**: Utiliza um banco de dados PostgreSQL, gerenciado via Docker, para armazenar os dados dos usuários.

## **🛠️ Tecnologias Utilizadas**

* **Java 17+**: Versão da linguagem utilizada no projeto.  
* **Spring Boot 3+**: Framework principal para a criação da aplicação e gerenciamento de dependências.  
* **Spring Security**: Para controle de autenticação, autorização e segurança dos endpoints.  
* **Spring Data JPA**: Para persistência de dados e comunicação com o banco de dados.  
* **PostgreSQL**: Banco de dados relacional para armazenamento dos dados.  
* **Docker & Docker Compose**: Para containerização e gerenciamento do ambiente do banco de dados.  
* **Maven**: Gerenciador de dependências e build do projeto.  
* **JJWT (Java JWT)**: Biblioteca para criação e validação dos tokens JWT.

## **⚙️ Como Executar o Projeto**

### **Pré-requisitos**

* [Java JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) ou superior.  
* [Maven](https://maven.apache.org/download.cgi) 3.8 ou superior.  
* [Docker](https://www.docker.com/products/docker-desktop/) e Docker Compose.

### **Passos**

1. **Clone o repositório:**  
   git clone \<url-do-seu-repositorio\>  
   cd \<nome-do-repositorio\>

2. Inicie o banco de dados com Docker:  
   Na raiz do projeto (onde está o arquivo docker-compose.yml), execute o comando:  
   docker-compose up \-d

   Isso irá iniciar um container PostgreSQL em segundo plano.  
3. Execute a aplicação Spring Boot:  
   Você pode rodar a aplicação pela sua IDE (Eclipse, IntelliJ) ou via terminal com o Maven:  
   mvn spring-boot:run

   O servidor será iniciado na porta 8080\.

## **Endpoints da API**

A URL base da API é http://localhost:8080.

### **Autenticação**

* **POST /api/auth/register**  
  * Registra um novo usuário.  
  * **Body (JSON):**  
    {  
      "name": "Seu Nome",  
      "email": "usuario@email.com",  
      "password": "senhaComMaisDe6Chars"  
    }

* **POST /api/auth/login**  
  * Autentica um usuário e retorna um token JWT.  
  * **Body (JSON):**  
    {  
      "email": "usuario@email.com",  
      "password": "senhaComMaisDe6Chars"  
    }

### **Usuário**

* **PUT /api/user/status**  
  * Atualiza o status do usuário logado (ativo/inativo).  
  * **Requer Autenticação**: É necessário enviar o token JWT no cabeçalho Authorization: Bearer \<seu-token\>.  
  * **Query Params**:  
    * active (boolean): true para ativar, false para desativar.  
  * **Exemplo de URL**: http://localhost:8080/api/user/status?active=false