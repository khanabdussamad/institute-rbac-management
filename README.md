# RBAC Institute Management App

An institute management application based on Role-Based Access Control (RBAC), built with Spring Boot, Spring Security, and Thymeleaf.

## Features

- **Role-Based Access Control (RBAC)**: Secure access to different parts of the application based on user roles (e.g., ADMIN, USER).
- **Authentication**: Custom login and logout functionality with session management.
- **Password Management**: Secure password reset functionality with policy validation.
- **User Management**: Admins can manage users, including account deletion (with safeguards against self-deletion).
- **Global Exception Handling**: Centralized error management for consistent API responses and user feedback.
- **Data Initialization**: Automatic creation of a default administrator account on startup.

## Technologies Used

- **Java 21** (or compatible version)
- **Spring Boot 3.x**
- **Spring Security**
- **Spring Data JPA**
- **H2 Database** (In-memory for development)
- **Thymeleaf** (Template engine)
- **Maven** (Build tool)

## Project Structure

- `src/main/java/com/nagarro/rbacdemo/controller`: Web controllers for handling requests.
- `src/main/java/com/nagarro/rbacdemo/security`: Security configurations and handlers.
- `src/main/java/com/nagarro/rbacdemo/service`: Business logic layer.
- `src/main/java/com/nagarro/rbacdemo/entity`: JPA entities (User, Role).
- `src/main/java/com/nagarro/rbacdemo/repository`: Data access layer.
- `src/main/resources/templates`: HTML templates using Thymeleaf.

## Getting Started

### Prerequisites

- JDK 17 or higher
- Maven 3.x

### Running the Application

1.  Clone the repository:
    ```bash
    git clone <repository-url>
    cd RBACDemo
    ```
2.  Build the project:
    ```bash
    mvn clean install
    ```
3.  Run the application:
    ```bash
    mvn spring-boot:run
    ```
4.  Access the application at `http://localhost:8080`.

### Default Credentials

The application initializes with a default admin account:
- **Email**: `admin@test.com`
- **Password**: `admin123`

## Security Configuration

- **Login Page**: `/login`
- **Console**: H2 console is accessible at `/h2-console` (CSRF disabled for development).
- **Method Security**: Enabled using `@PreAuthorize` for fine-grained access control on service methods.

## API Endpoints

- `GET /login`: Displays the login page.
- `POST /reset-password`: Resets user password (requires `email` and `newPassword`).
- `GET /dashboard`: Protected dashboard page.
- `GET /access-denied`: Displays the access denied page.
