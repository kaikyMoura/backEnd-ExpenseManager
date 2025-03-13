<h2 align="center">Expense Manager API - Backend</h2>
<p align="center"><i>Repository for the Expense Manager backend API</i></p>

![GitHub top language](https://img.shields.io/github/languages/top/kaikyMoura/backEnd-ExpenseManager)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/ce1f958181d743b98107dbc70dfac5ed)](https://app.codacy.com/gh/kaikyMoura/backEnd-ExpenseManager/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
![Repository size](https://img.shields.io/github/repo-size/kaikyMoura/backEnd-ExpenseManager)
![Github last commit](https://img.shields.io/github/last-commit/kaikyMoura/backEnd-ExpenseManager)
![License](https://img.shields.io/aur/license/LICENSE)
![Languages count](https://img.shields.io/github/languages/count/kaikyMoura/backEnd-ExpenseManager)

<br/>

### 1. About the Project
This project is the backend for the [Expense Manager](https://github.com/kaikyMoura/expense-manager), responsible for user authentication and full CRUD operations for expense management. It is built with Spring Boot 3.2.9 and JDK 21, using PostgreSQL as the main database.

The application is designed following RESTful principles and implements security and token-based authentication via JWT.

<br/>

### 2. Key Features
- User authentication and authorization using JWT.
- Full expense CRUD (Create, Read, Update, Delete).
- Role-based access control (e.g., default user role customer).
- Email notification support via SendGrid.
- Deployment-ready for Google Cloud Run and file storage with Google Cloud Storage.
- Monitoring and health checks using Actuator.
- In-memory H2 Database for development and testing environments.

<br/>

### 3. Technologies & Dependencies
<p display="inline-block"> <img alt="java-logo" width="48" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" /> <img alt="spring-logo" width="48" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original-wordmark.svg" /> <img alt="postgres-logo" width="48" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/postgresql/postgresql-original.svg" /> <img alt="google-cloud-logo" width="48" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/googlecloud/googlecloud-original.svg" /> <img alt="docker-logo" width="48" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/docker/docker-original.svg" /> </p>

#### Main Dependencies:

- [spring-boot-starter-actuator](https://spring.io/guides/gs/actuator-service):
  <br/>Provides monitoring and management features for the Spring Boot application, including HTTP endpoints for health checks, metrics, and application info.

- [spring-boot-starter-data-jpa](https://spring.io/projects/spring-data-jpa/):
  <br/>Simplifies the use of Java Persistence API (JPA) in Spring Boot, providing configurations, JPA entities, and repository support for easy CRUD operations.

- [h2-database](https://www.h2database.com/html/main.html): 
  <br/>An in-memory relational database used for development and testing, with a web console for database management.
  
- [spring-boot-starter-security](https://spring.io/projects/spring-security): 
  <br/>Provides authentication and authorization support, including protection against CSRF and XSS attacks.

- [java-jwt](https://github.com/auth0/java-jwt):
  <br/>A library for creating and validating JWT tokens, used for authentication and authorization in web apps and RESTful APIs.

- [spring-boot-starter-oauth2-resource-server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html) : Resource protection.
  <br/>Adds support for implementing OAuth 2.0 resource servers in Spring Boot apps, protecting APIs via OAuth 2.0.
  
- [lombok](https://projectlombok.org):
  <br/>Reduces boilerplate code using annotations for generating getters, setters, constructors, and more during compile time.

- [SendgridApi](https://sendgrid.com/en-us):
 <br/> A platform for transactional and marketing email services with an easy-to-integrate API. Offers features like email tracking, analytics, and automation.

- [Google Cloud run](https://cloud.google.com/run) / [Google Cloud Build](https://cloud.google.com/build) :
 <br/>A fully managed platform to build, deploy, and run containers with automatic scaling — without the need to manage servers or infrastructure.
  Cloud Build enables serverless CI/CD pipelines, making it easy to automate deployments to Cloud Run and other Google Cloud services.

- [Google Cloud Storage](https://cloud.google.com/storage?hl=pt_br) :
  <br/> A secure, scalable cloud storage solution by Google.
  <br/>[Code exemples](https://cloud.google.com/storage/docs/samples/)

<br/>

### 4. Architecture

The project follows RESTful architecture, with a clear separation of services and controllers.

Test endpoints require proper credentials and roles.

⚙️ Account Creation & Authentication Flow:
- To create an account, send a POST request to **/user** with an email and password.
- Passwords are securely encrypted before being stored.
- Upon account creation, a JWT token is generated for authentication and authorization.
- By default, new users are assigned the "customer" role.
- Token validation and role-based access control are handled automatically in the background.

<br/>
  
### 5. Installation and Setup

#### Prerequisites:
Before running the project, ensure that Java is installed on your machine. If not, you can download it from the [official Oracle website](https://www.oracle.com/java/technologies/downloads) (version 17 or later is recommended).
<br/>To verify your Java installation, run:

```bash
java -version
```

#### Clone the repository to your local machine:
Clone the repository to your local machine:

```console
git clone https://github.com/kaikyMoura/backEnd-ExpenseManager.git
```

Navigate to the project's root directory:

```console
cd backEnd-ExpenseManager
```

#### Building the Project
Use Maven to clean and package the application:

```console
mvn clean package
```

#### Running the Application
Once the build is complete, you can start the application with:

```console
java -jar target/expenseManager-0.0.1-SNAPSHOT.jar
```

Or you can run directly in your IDE.

#### The API will be available on:

```console
http://localhost:8080
```

<br/>

### 6. 🚀 Deploy
### Deployment on Google Cloud Run with Continuous Integration via Cloud Build and GitHub
<br/> The deployment of the project is done on Google Cloud Run, leveraging Cloud Build for continuous integration. This setup ensures that any changes pushed to the repository on GitHub are automatically built and deployed to Cloud Run.

- Key Steps:
  - Cloud Build: Automatically builds the Docker image from the GitHub repository and pushes it to Google Container Registry.
  - Cloud Run Deployment: After the image is built, Cloud Run deploys the application using the newly created image.
  - Service Account: A service account with appropriate permissions (such as access to Google Cloud Storage) is used to ensure the application can interact with required   resources securely.

This setup enables automated and seamless deployment, reducing manual intervention and ensuring continuous delivery of updates to the production environment.

<br/>

### 7. API Documentation
The API follows RESTful patterns, and endpoints include:

|  Method  |	Endpoint |  Description |  Auth Required  |
| --- | --- | --- | --- |
|   **POST**   |	 `/user`	 |  Register new user  |  No  | 
|   **POST**	 |  `/auth/login`  |	Authenticate and get token | 	No  |
|  **CRUD**	   |  `/expense`  |  Manage user expenses  |  Yes  |

<br/>

### 8. Terms of Use
- **Non-commercial** project.
- All rights related to user data and privacy are respected.
- External services (e.g., SendGrid, Google Cloud) follow their own Terms of Use.
- This project aims to serve as a learning and portfolio tool.
