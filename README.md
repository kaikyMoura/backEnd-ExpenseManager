# BACK END PROJETO EXPENSE MANAGER #


## -- INTRODUÇÃO 
Este projeto é o backend para o projeto [Expense manager](https://github.com/kaikyMoura/expense-manager), onde é tratada a autenticação dos usuários e CRUD das despesas. 

O banco de dados utilizado é o PostgresSql.

## -- CONFIGURAÇÃO 
O projeto é baseado no Spring Framework na versão 3.2.9 e JDK 21.

O gerenciador de dependências utilizado é o Maven.


## -- DEPENDÊNCIAS E TECNOLOGIAS


- [spring-boot-starter-actuator](https://spring.io/guides/gs/actuator-service):

  Esta dependência fornece recursos para monitoramento e gerenciamento do aplicativo Spring Boot. Isso inclui endpoints HTTP para verificação de integridade, 
  métricas e informações sobre o aplicativo.

- spring-boot-starter-data-jdbc :

  Essa dependência permite que o Spring Boot integre facilmente com bancos de dados relacionais usando o Spring Data JDBC, que é uma alternativa ao Spring Data JPA. 
  Ele fornece abstrações e ferramentas para simplificar o acesso aos dados.

- [spring-boot-starter-data-jpa](https://spring.io/projects/spring-data-jpa/) :

  Esta dependência facilita o uso de JPA (Java Persistence API) no Spring Boot, fornecendo configurações padrão, entidades JPA e suporte a repositórios. 
  Isso simplifica a implementação de operações CRUD (Create, Read, Update, Delete) com bancos de dados relacionais.

- [h2-database](https://www.h2database.com/html/main.html) :

  O H2 é um banco de dados relacional leve e rápido, escrito em Java, ideal para desenvolvimento e testes. Ele suporta modos em memória e persistente, oferecendo um console web embutido   para administração.

- [spring-boot-starter-security](https://spring.io/projects/spring-security) :

  Esta dependência fornece suporte para segurança de aplicativos Spring Boot. Ele simplifica a configuração de autenticação, autorização e outras medidas de segurança, 
  como proteção contra ataques de CSRF (Cross-Site Request Forgery) e XSS (Cross-Site Scripting).

- [java-jwt](https://github.com/auth0/java-jwt) :

  Esta é uma biblioteca Java para criação e validação de tokens JWT (JSON Web Tokens). Ela permite gerar tokens JWT para autenticação e autorização em aplicativos web e APIs RESTful.

- [spring-boot-starter-oauth2-resource-server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html) :

  Essa dependência fornece suporte para implementar um servidor de recursos OAuth 2.0 em aplicativos Spring Boot. Isso é útil quando você precisa proteger 
  APIs RESTful usando o protocolo OAuth 2.0.

- spring-boot-starter-test:

  Esta dependência inclui bibliotecas e ferramentas para teste de aplicativos Spring Boot. Isso inclui o framework de teste JUnit, 
  Mockito para mock objects e várias outras bibliotecas de teste.
  
- [lombok](https://projectlombok.org):

  Lombok é uma biblioteca que ajuda a reduzir a quantidade de código boilerplate em projetos Java. Ela fornece anotações que geram automaticamente código durante a compilação, 
  como getters, setters, construtores e muito mais.

- [SendgridApi](https://sendgrid.com/en-us):

SendGrid é uma plataforma de envio de e-mails que facilita a comunicação com clientes por meio de e-mails transacionais e marketing. Com uma API intuitiva, ela permite que desenvolvedores integrem rapidamente funcionalidades de envio de e-mails em suas aplicações, oferecendo recursos como rastreamento de e-mails, análise de desempenho e automação de marketing.

- [Google Cloud run](https://cloud.google.com/run?hl=pt-BR) :

É um serviço gerenciado que permite executar aplicações em contêineres de forma rápida e escalável, sem a necessidade de gerenciar a infraestrutura.

- [Google Cloud Storage](https://cloud.google.com/storage?hl=pt_br) :

[Exemplos de código](https://cloud.google.com/storage/docs/samples/)

É um serviço de armazenamento em nuvem oferecido pelo Google Cloud Platform. Ele permite que usuários e empresas armazenem e recuperem grandes volumes de dados de forma segura e escalável


  ##  -- ARQUITETURA

 O projeto adota o padrão REST, com uma clara separação entre serviços e controladores. Os endpoints de "teste" exigem credenciais ou funções especiais. Ao criar uma conta, o usuário padrão é definido como "customer". Para realizar a criação de uma conta, acesse o endpoint /user utilizando o método "POST" e forneça um endereço de e-mail e uma senha. Esses dados serão criptografados para validação do usuário. Após a criação da conta, um token será gerado, sendo utilizado para autenticar o login. Todo o processo de filtragem e verificação ocorre automaticamente durante a execução.

  
## -- INSTALAÇÃO E EXECUÇÃO


O primeiro passo é verificar se o java está instalada, senão pode ser baixo [clicanco aqui](https://www.oracle.com/br/java/technologies/downloads) ( Preferencialmente a partir Da versão 17 ).

```bash
java -version
```

Depois deve-se clonar o repositório localmente:

```bash
git clone https://github.com/kaikyMoura/backEnd-ExpenseManager.git
```

Após clonar, navegue até o diretório do raiz do projeto, então é só executar o comando abaixo, para fazer a build :

```bash
mvn clean package
```

Agora está tudo pronto para testar o projeto!
