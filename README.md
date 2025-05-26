# PMS4ST - Project Management System for Student Teams

## Group 1 - Internship 1 - East Asia University of Technology
## TOPIC: DESIGN AND IMPLEMENTATION OF A PROJECT MANAGEMENT PLATFORM FOR STUDENT COLLABORATION
## GITHUB LINK: https://github.com/AnChuTuan/pms4s-project

## Table of Contents
1.  [Prerequisites](#prerequisites)
2.  [Project Setup](#project-setup)
    *   [Database Configuration](#database-configuration)
    *   [Application Properties](#application-properties)
3.  [Building the Project](#building-the-project)
4.  [Running the Application](#running-the-application)
    *   [Using Maven](#using-maven)
    *   [Using an IDE (VS Code)](#using-an-ide-vs-code)
5.  [Accessing the Application](#accessing-the-application)
6.  [Technology Stack](#technology-stack)

---

## 1. Prerequisites

Before you begin, ensure you have the following installed on your system:

*   **Java Development Kit (JDK):** Version 17.0.2 or later
*   **Apache Maven:** Version 3.6.x or later (for building and managing dependencies).
*   **Microsoft SQL Server:** Any recent version (e.g., Developer Edition, Express Edition).
*   **SQL Server Management Tool:** Such as SQL Server Management Studio (SSMS) or Azure Data Studio.
*   **An IDE:** Visual Studio Code with appropriate Java and Spring Boot extensions.
(Extensions: Spring Web, Spring Data JPA, SQL Server Driver, Spring Security, Thymeleaf)

---

## 2. Project Setup

### Database Configuration

1.  **Create Database:**
    *   Using your SQL Server management tool (SSMS or Azure Data Studio), connect to your SQL Server instance.
    *   Open the SQL script `schema.sql` located in `01_Code\schema.sql` to create a new database named `ProjectManagementDB`:
        ```sql
        CREATE DATABASE ProjectManagementDB;
        ```
2.  **Create Tables:**
    *   Switch to the next line in the script:
        ```sql
        USE ProjectManagementDB;
        ```
    *   Execute all the scripts left in `schema.sql` to create all necessary tables (`users`, `roles`, `projects`, `tasks`, `comments`, etc.) and insert default roles.

### Application Properties

1.  Navigate to the `project-management-system-for-student-teams/src/main/resources/` directory in the project.
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

You can build the project using Apache Maven. Open a terminal or command prompt, navigate to the **root directory** __(project-management-system-for-student-teams)__ of the project (where the `pom.xml` file is located), and run:

```bash
mvn clean package
```
---

## 4. Running the Application

### Using an IDE (VS Code):

1.  Open the project in VS Code.
2.  Locate the main application class: `project-management-system-for-student-teams/src/main/java/com/pms4st/pms/ProjectManagementSystemForStudentTeamsApplication.java`.
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

1.  Open a web browser.
2.  Navigate to: `http://localhost:8080`
3.  You should be redirected to the login page (`/login`).
4.  You'll need to register a user via the "Register here" link on the login page.

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
