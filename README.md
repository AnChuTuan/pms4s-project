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

*   **Java Development Kit (JDK):** Version 17 or later (Project was developed with JDK 17).
*   **Apache Maven:** Version 3.6.x or later (for building and managing dependencies).
*   **Microsoft SQL Server:** Any recent version (e.g., Developer Edition, Express Edition).
*   **SQL Server Management Tool:** Such as SQL Server Management Studio (SSMS) or Azure Data Studio.
*   **Git:** For version control (optional for running, but recommended for development).
*   **An IDE (Optional but Recommended):** Such as IntelliJ IDEA, Eclipse, or Visual Studio Code with appropriate Java and Spring Boot extensions.

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
    *   Execute the SQL script located at `src/main/resources/schema.sql` (or the full schema provided in the project documentation) to create all necessary tables (`users`, `roles`, `projects`, `tasks`, `comments`, etc.) and insert default roles.

### Application Properties

1.  Navigate to the `src/main/resources/` directory in the project.
2.  Open the `application.properties` file.
3.  **Modify the database connection details** to match your SQL Server setup:

    ```properties
    # SQL Server Connection (MODIFY THESE)
    spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=ProjectManagementDB;encrypt=true;trustServerCertificate=true;
    spring.datasource.username=YOUR_DB_USERNAME  # Replace with your SQL Server login username
    spring.datasource.password=YOUR_DB_PASSWORD  # Replace with your SQL Server login password
    ```
    *   Adjust `localhost:1433` if your SQL Server instance is running on a different host or port.
    *   Review `encrypt=true;trustServerCertificate=true;` based on your SQL Server's SSL configuration. For local development, `trustServerCertificate=true` is often used if SSL is enabled but you don't have a trusted certificate.

### File Upload Directory (If File Attachments are Implemented)

*If you have re-added file attachment functionality:*

1.  **Create Directory:** Manually create a directory on your filesystem where uploaded files will be stored.
2.  **Configure Path:** In `application.properties`, update the `file.upload-dir` property to point to the directory you created:
    ```properties
    # File Upload Location (MODIFY THIS if using file uploads)
    # Example Windows: file.upload-dir=C:/pms-uploads
    # Example Linux/Mac: file.upload-dir=/var/pms-uploads
    file.upload-dir=./pms-uploads-basic # Create this directory manually!
    ```
    Ensure the application has write permissions to this directory.

---

## 3. Building the Project

You can build the project using Apache Maven. Open a terminal or command prompt, navigate to the **root directory** of the project (where the `pom.xml` file is located), and run:

```bash
mvn clean package
