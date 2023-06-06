## User Guide
### Build & Run
Maven is used as project management tool. Use:
mvn compile - to compile the project.
mvn package - to generate executable jar.
Or
lv.vladislavs.ewallet.EwalletApplication is the main class to be.
Annotation processing should be enabled for project.
Application configuration can be found in resources/application.yaml and Constants class.
All endpoints require authentication expect for registration and login.

### Postman
Postman collection with requests samples can be imported from E-Wallet.postman_collection.json.
Sample requests are configured in such a way that they can be executed one after another.
Postman variables should be configured as following:
scheme://host:port = localhost:8080
Bearer token is used for request authorization. Login request contains script that reads login response
and stores returned JWT token as jwtToken variable which then is used to populate Authorization header for following requests.

### Project structure
Main packages:
controller - Controller layer, contains API entry points, should operate with DTOs
model - Package to store data classes - domain objects and DTOs
service - Business logic layer that operates with domain objects, takes in Requests/DTOs and returns Responses/DTOs
converter - Used to convert domain objects to Request/Response or DTOs
repository - Persistence layer for domain objects.
exception - Contains respective exceptions/errors that are handled in proper error response.

## Design Choices
The frameworks chosen are popular, have community support, being developed, improved and have rich documentation and plenty of examples.
The developer has experience using them, that's why:
- Java 17
- Spring Boot
- Lombok to reduce boilerplate code.
- MapStruct for mapping/convertor related code generation.
- JPA for persistence layer to define which objects to be persisted and how.
- JWT tokens for security.
- H2 in-memory relational database management system used for developing.
- Testing with JUnit 5 and Mockito for mocks, Spring Boot Test for integration tests.


## Things that could be improved
### Auth token & security:
- Currently multiple tokens could be issued, perhaps a solution with single active token is needed for better security.
- Authenticated user could be retrieved using annotation.
- On registration response, auth token could also be generated and returned.
- On user already registered error - with current error implementation it is possible to do account enumeration.
- User information returned in login response can be stored in JWT claims.
- JWT signing - secret key configuration.

### HTTP status codes:
- Instead of responding with HTTP 200 Ok as a response 
on post requests that do create entries, 201 Created could be used - it suits better.

### General:
- TransactionService#createTransaction should return TransactionDto which doesn't exist.
- Constants could be implemented as configurations so they could be modified without code changes. Ex.: stored in database or with use of config server.
- No profiles for application config for different environments, ex. prod setup for database connection, db scheme management strategy instead of always recreated in-memory used while developing.
- Currency support, currency decimal digit count.
- User specific timezone support for limitations.
- Request validation. Ex.: when wallet id is not sent in transaction request.
- Agreed code style setup for project and IDE.
- Constructor DI instead of reflectional injection.
- Solution for concurrency issue as each request is processed in parallel thread.
- Better OOP layer ex.: service interfaces, transaction processor classes for different types of transactions for better extensibility/responsibility decoupling.
- Swagger for documentation generation.

### Tests:
- More tests needed, currently just one/few for each layer.
- By looking at IT tests implemented with order annotation, I see that BDD testing framework (Cucumber) would be handy.
- Time mocking for tests with time based functionality.