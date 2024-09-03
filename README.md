# BACK END PROJETO EXPENSE MANAGER #


## -- INTRODUÇÃO 
Este projeto é o backend para o projeto [Expense manager](https://github.com/kaikyMoura/expense-manager), onde é tratada a autenticação dos usuários e CRUD das despesas. 

O banco de dados utilizado é o PostgresSql.

## -- CONFIGURAÇÃO 
O projeto é baseado no Spring Framework na versão 3.2.9 e JDK 21.

O gerenciador de dependências utilizado é o Maven.

As seguintes dependências estão sendo utilizadas:


- spring-boot-starter-actuator:

  Esta dependência fornece recursos para monitoramento e gerenciamento do aplicativo Spring Boot. Isso inclui endpoints HTTP para verificação de integridade, 
  métricas e informações sobre o aplicativo.

- spring-boot-starter-data-jdbc:

  Essa dependência permite que o Spring Boot integre facilmente com bancos de dados relacionais usando o Spring Data JDBC, que é uma alternativa ao Spring Data JPA. 
  Ele fornece abstrações e ferramentas para simplificar o acesso aos dados.

- spring-boot-starter-data-jpa:

  Esta dependência facilita o uso de JPA (Java Persistence API) no Spring Boot, fornecendo configurações padrão, entidades JPA e suporte a repositórios. 
  Isso simplifica a implementação de operações CRUD (Create, Read, Update, Delete) com bancos de dados relacionais.

- h2-database:

  O H2 é um banco de dados relacional leve e rápido, escrito em Java, ideal para desenvolvimento e testes. Ele suporta modos em memória e persistente, oferecendo um console web embutido   para administração.

- spring-boot-starter-security:

  Esta dependência fornece suporte para segurança de aplicativos Spring Boot. Ele simplifica a configuração de autenticação, autorização e outras medidas de segurança, 
  como proteção contra ataques de CSRF (Cross-Site Request Forgery) e XSS (Cross-Site Scripting).

- java-jwt:

  Esta é uma biblioteca Java para criação e validação de tokens JWT (JSON Web Tokens). Ela permite gerar tokens JWT para autenticação e autorização em aplicativos web e APIs RESTful.

- spring-boot-starter-oauth2-resource-server:

  Essa dependência fornece suporte para implementar um servidor de recursos OAuth 2.0 em aplicativos Spring Boot. Isso é útil quando você precisa proteger 
  APIs RESTful usando o protocolo OAuth 2.0.

- spring-boot-starter-test:

  Esta dependência inclui bibliotecas e ferramentas para teste de aplicativos Spring Boot. Isso inclui o framework de teste JUnit, 
  Mockito para mock objects e várias outras bibliotecas de teste.
  
- lombok:

  Lombok é uma biblioteca que ajuda a reduzir a quantidade de código boilerplate em projetos Java. Ela fornece anotações que geram automaticamente código durante a compilação, 
  como getters, setters, construtores e muito mais.

  ## Arquitetura
  O projeto segue o padrão Rest, onde há a separação de serviços e controladores. Os endpoints de "test" requerem credenciais ou "roles" especiais. Ao criar uma conta, o usuário padrão é o "customer". Para criar uma conta, acesse o endpoint /user via método "POST" e forneça um email e senha, que serão encriptados e usados para validar o usuário. Após a criação, um token será gerado e utilizado para validar o login. Todo o processo de filtro e verificação é feito automaticamente durante a execução.

  - Exemplo de como criar um usuário:
    
  PS: Deverá retornar 201 no status para confirmar a criação do usuário.

  No exemplo como um role não foi especificado, será utilizado o padrão.
  
  ![create](https://github.com/user-attachments/assets/2011abb5-b8d4-49ab-9b5a-8617fe14a3e3)


  - Após o login será gerado um token que é utilizado para acessar o front end.


  - Exemplo de como executar o login:

  PS: Deverá retornar 200 no status.

  O retorno da requisição é o token de autorização.
    
 ![login](https://github.com/user-attachments/assets/81e15941-973c-4e16-9fe0-26df4f35b4a9)



  
## - IMPLANTAÇÃO

Para testar o backend, siga estas etapas simples:

- Instale o Projeto:

- Baixe o projeto e instale as dependências necessárias. Certifique-se de ter o JDK instalado, preferencialmente a partir da versão 17.
Instale as Dependências:

- Você pode instalar as dependências executando o comando mvn install ou usando outra maneira de sua preferência. Algumas IDEs já executam este processo automaticamente.


No arquivo application.properties do projeto, as configurações da porta e do banco de dados já estão predefinidas. 
No entanto, é importante verificar se não há nada que possa entrar em conflito com essas configurações, como a porta já estar sendo utilizada por outro serviço.

