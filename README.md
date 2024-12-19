# Online Book Store

## Introduction
The Online Book Store is a modern web application designed to streamline the process of purchasing books online.

## Features
- **User Management**: Secure registration, login, and role-based access (USER, ADMIN).
- **Book Catalog**: Search, filter, and browse through an extensive catalog of books.
- **Categories**: Group books by categories for easy navigation.
- **Shopping Cart**: Add, remove, and view items in the cart.
- **Order Processing**: Create and view orders with detailed summaries.
- **Security**: Implements JWT-based authentication and BCrypt password encryption.
- **API Documentation**: Swagger UI for comprehensive API documentation.

## Technologies Used
- **Backend**:
  - Spring Boot (2.6+)
  - Spring Security
  - Spring Data JPA
  - Liquibase
  - PostgreSQL
- **Testing**:
  - JUnit
  - Mockito
  - Testcontainers
- **Documentation**:
  - Swagger
- **Tools**:
  - Docker
  - IntelliJ IDEA
  - Maven

## Project Structure
The project is structured to ensure maintainability and scalability:
- **Controller Layer**: Handles incoming HTTP requests and interacts with services.
- **Service Layer**: Contains business logic and data processing.
- **Repository Layer**: Interfaces with the database using JPA.
- **Model**: Represents the core entities such as `Book`, `Category`, `User`, and `Order`.
- **Security**: Handles authentication, authorization, and JWT token management.
- **Validation**: Ensures data integrity using annotations and custom validators.

## Setup Instructions
1. Clone the repository:
   ```bash
   git clone https://github.com/Nazar090/online-book-store.git
   cd online-book-store
   ```
2. Install dependencies:
   ```bash
   mvn clean install
   ```
3. Set up the database:
   - Install PostgreSQL.
   - Create a database named `online_book_shop`.
   - Use the provided Liquibase scripts (`resources/db/changelog`) to initialize the database schema.
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```
5. Access the application:
   - Backend API: `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`

## API Endpoints
Here are some of the key API endpoints:
- **Authentication**:
  - `POST /api/auth/register`: Register a new user.
  - `POST /api/auth/login`: Authenticate and get a JWT token.
- **Books**:
  - `GET /api/books`: Get a list of books.
  - `POST /api/books`: Add a new book (ADMIN only).
- **Categories**:
  - `GET /api/categories`: Get all categories.
  - `POST /api/categories`: Add a new category (ADMIN only).
- **Shopping Cart**:
  - `POST /api/cart`: Add an item to the cart.
  - `GET /api/cart`: View items in the cart.
- **Orders**:
  - `POST /api/orders`: Place an order.
  - `GET /api/orders`: View user orders.

## Challenges and Solutions
1. **Data Security**: Implemented JWT and BCrypt to secure user data and authentication.
2. **Database Migration**: Used Liquibase for version-controlled schema management.
3. **Scalability**: Designed the application to support future feature expansions by adhering to clean architecture principles.

## Testing
- **Test Coverage**: Achieved over 50% line coverage for critical components like `BookController`, `CategoryController`, and service layers.
- **Testcontainers**: Used for dynamic database testing during integration tests.

## Postman Collection
A Postman collection is available in the `resources/postman` directory. Import it into Postman to test the API endpoints with pre-configured requests and environment variables.

## Screenshots
- **Application Structure**:
  ![Application Structure](./docs/screenshots/structure.png)
- **Swagger UI**:
  ![Swagger UI](./docs/screenshots/swagger.png)

## Contributions
Contributions are welcome! Feel free to fork the repository and submit a pull request with your improvements.

---

Thank you for exploring the Online Book Store project. I hope you find it inspiring and insightful. If you have any questions, feel free to reach out via the Issues 
section on GitHub.
