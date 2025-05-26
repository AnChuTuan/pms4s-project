# PMS4ST - Project Management System for Student Teams

PMS4ST is a web-based application designed to help student teams manage their collaborative projects effectively. It provides features for project creation, task management, team member collaboration, and commenting.

## Table of Contents
1.  [Prerequisites](#prerequisites)
2.  [Project Setup](#project-setup)
    *   [Database Configuration](#database-configuration)
    *   [Application Properties](#application-properties)
3.  [Building the Project](#building-the-project)
4.  [Running the Application](#running-the-application)
    *   [Using Maven](#using-maven)
    *   [Using an IDE (e.g., VS Code)](#using-an-ide-eg-vs-code)
5.  [Accessing the Application](#accessing-the-application)
6.  [Technology Stack](#technology-stack)
7.  [Project Structure](#project-structure)

---

## 1. Prerequisites

Before you begin, ensure you have the following installed on your system:

*   **Java Development Kit (JDK):** Version 17.
*   **Apache Maven:** Version 3.6.x or later (for building and managing dependencies).
*   **Microsoft SQL Server:** Any recent version (e.g., Developer Edition, Express Edition).
*   **SQL Server Management Tool:** Such as SQL Server Management Studio (SSMS) or Azure Data Studio.
*   **An IDE:** Visual Studio Code with appropriate Java and Spring Boot extensions.

---

## 2. Project Setup

### Database Configuration

1.  **Create Database:**
    *   Using your SQL Server management tool (SSMS or Azure Data Studio), connect to your SQL Server instance.
    *   Create a new database named `ProjectManagementDB`:
        ```sql
        CREATE DATABASE ProjectManagementDB;
        ```
2.  **Create Tables:**
    *   Switch to the newly created database:
        ```sql
        USE ProjectManagementDB;
        ```
    *   Execute the SQL script `schema.sql` to create all necessary tables (`users`, `roles`, `projects`, `tasks`, `comments`, etc.) and insert default roles.

### Application Properties

1.  Navigate to the `src/main/resources/` directory in the project.
2.  Open the `application.properties` file.
3.  **Modify the database connection details** to match your SQL Server setup:

    ```properties
    # SQL Server Connection
    spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=ProjectManagementDB;encrypt=true;trustServerCertificate=true;
    spring.datasource.username=pms4st
    spring.datasource.password=pms4st
    ```
    *   Adjust `localhost:1433` if your SQL Server instance is running on a different host or port.

---

## 3. Building the Project

You can build the project using Apache Maven. Open a terminal or command prompt, navigate to the **root directory** of the project (where the `pom.xml` file is located), and run:

```bash
mvn clean package
```

This command will:
*   `clean`: Delete any previous build artifacts (e.g., from the `target` directory).
*   `package`: Compile the source code, run any tests, and package the application into an executable JAR file (e.g., `project-management-system-for-student-teams-0.0.1-SNAPSHOT.jar`) located in the `target` directory.

---

## 4. Running the Application

There are a couple of ways to run the Spring Boot application:

### Using Maven

This is a common way to run during development.

1.  Open a terminal or command prompt.
2.  Navigate to the **root directory** of the project.
3.  Execute the following command:
    ```bash
    mvn spring-boot:run
    ```
    This will compile the code if necessary and start the embedded Tomcat server.

### Using an IDE (e.g., VS Code)

If you're using an IDE like Visual Studio Code with the Spring Boot Extension Pack:

1.  Open the project in VS Code.
2.  Locate the main application class: `src/main/java/com/pms4st/pms/ProjectManagementSystemForStudentTeamsApplication.java` (adjust package if you used `com.pms4st.basic`).
3.  You should see `Run | Debug` links above the `main` method. Click `Run`.
4.  Alternatively, use the IDE's "Run" or "Debug" configuration for Spring Boot applications.

**After starting, look for console output indicating the application has started, typically:**
```
... Tomcat started on port(s): 8080 (http) ...
... Started ProjectManagementSystemForStudentTeamsApplication in X.XXX seconds ...
```

---

## 5. Accessing the Application

Once the application is running:

1.  Open a web browser (e.g., Chrome, Firefox, Edge).
2.  Navigate to: `http://localhost:8080`
3.  You should be redirected to the login page (`/login`).
4.  If it's your first time, you'll need to register a user via the "Register here" link on the login page.

---

## 6. Technology Stack

*   **Backend:**
    *   Java 17
    *   Spring Boot 3.x (using Spring Web, Spring Data JPA, Spring Security)
*   **Frontend:**
    *   Thymeleaf (Server-side template engine)
    *   HTML5, CSS3 (with Bootstrap 5 for styling)
*   **Database:**
    *   Microsoft SQL Server
*   **Build Tool:**
    *   Apache Maven
*   **Security:**
    *   Spring Security (for authentication and basic authorization)

---

## 7. Project Structure (Simplified Overview)

*   **`src/main/java/com/pms4st/pms`** (or `com.pms4st.basic`): Base package for Java source code.
    *   **`.ProjectManagementSystemForStudentTeamsApplication.java`**: The main Spring Boot application class.
    *   **`.config`**: Configuration classes (e.g., `SecurityConfig.java`).
    *   **`.controller`**: Spring MVC controllers handling web requests (e.g., `AuthController.java`, `ProjectController.java`).
    *   **`.entity`**: JPA entities mapping to database tables (e.g., `User.java`, `Project.java`).
    *   **`.exception`**: Custom exception classes (e.g., `ResourceNotFoundException.java`).
    *   **`.repository`**: Spring Data JPA repository interfaces (e.g., `UserRepository.java`).
    *   **`.service`**: Service classes containing business logic (e.g., `AppService.java`).
    *   **`.dto`**: (If used) Data Transfer Objects for carrying data, especially for forms.
*   **`src/main/resources`**:
    *   **`application.properties`**: Main application configuration file.
    *   **`schema.sql`**: (If included) SQL script for database schema creation.
    *   **`static`**: For static resources like CSS, JavaScript, images.
        *   **`css/style.css`**: Custom stylesheets.
    *   **`templates`**: Thymeleaf HTML templates.
        *   **`fragments/layout.html`**: Reusable layout template.
        *   Other `.html` files for specific views (e.g., `login.html`, `project-list.html`).
*   **`pom.xml`**: Maven project configuration file (dependencies, build settings).
*   **`target`**: Directory where Maven places compiled code and packaged JARs.

---
