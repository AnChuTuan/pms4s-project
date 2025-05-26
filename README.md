# PMS4ST - Project Management System for Student Teams

PMS4ST is a web-based application designed to help student teams manage their collaborative projects effectively. It provides features for project creation, task management, team member collaboration, and commenting.

## Table of Contents
1.  [Prerequisites](#prerequisites)
2.  [Project Setup](#project-setup)
    *   [Database Configuration](#database-configuration)
    *   [Application Properties](#application-properties)
    *   [File Upload Directory](#file-upload-directory)
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
