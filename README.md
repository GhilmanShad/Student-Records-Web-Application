# Student Records Web Application

## Project Overview

A web-based student records management system developed
using Spring Boot, Thymeleaf and MySQL.

## Features

- Secure login and logout
- User registration
- Role-based access control
- Live search by name, registration number and department
- Student form submission and validation
- CRUD operations
- Alphabetical sorting of student records
- Success and error notifications

## Technology Stack

- Java 17
- Spring Boot
- Spring MVC
- Spring Security
- Spring JDBC (JdbcTemplate)
- Thymeleaf
- MySQL
- HTML, CSS and JavaScript
- Maven
- Git and GitHub

## Prerequisites

- JDK 17
- MySQL 8.0.16 or later
- Internet connection for initial Maven dependency downloads

## Database Setup

1. Open database/setup.sql.
2. Replace REPLACE_WITH_STRONG_PASSWORD with your chosen
   database password in a local copy of the script.
3. Execute that copy using a MySQL administrator account.

Linux:

```bash
sudo mysql < database/setup.local.sql
```

Windows (Command Prompt):

```cmd
mysql -u root -p < database\setup.local.sql
```

The script creates the scd_records database, required
tables and application database user.

## Running on Linux

```bash
export DB_PASSWORD='your_database_password'
export ADMIN_PASSWORD='your_admin_password'
./mvnw spring-boot:run
```

## Running on Windows

Open PowerShell in the project directory:

```powershell
$env:DB_PASSWORD = "your_database_password"
$env:ADMIN_PASSWORD = "your_admin_password"
.\mvnw.cmd spring-boot:run
```

## Accessing the Application

Open the following URL in a browser:

http://localhost:8080/login

Administrator username: admin

The administrator password is initialized using
ADMIN_PASSWORD when the administrator account does
not already exist.

Choose a strong administrator password of at least
8 characters and no more than 72 UTF-8 bytes.

## User Roles

### Administrator
- View student records
- Search student records
- Add student records
- Update student records
- Delete student records

### Standard User
- Register an account
- Log in and view account information
- Log out
- No access to student records management

## Security

- BCrypt password hashing
- Role-based authorization
- CSRF protection
- Parameterized SQL queries
- Frontend and backend input validation
- Environment-based database credentials

## Project Architecture

Controller -> Service -> Repository -> MySQL Database

Thymeleaf templates provide the presentation layer.

## Project Structure

- src/main/java/ : Java application source code
- src/main/resources/templates/ : Thymeleaf templates
- src/main/resources/static/ : CSS resources
- database/ : Database setup scripts
- src/test/ : Application tests
- pom.xml : Maven dependencies and configuration
- mvnw / mvnw.cmd : Maven wrappers

