uniplan
===
---
## 📑 Table of Contents
1. [Project Description](#project-description)
2. [Features](#features)
3. [Technologies Used](#technologies-used)
4. [Installation and Run](#installation-and-run)
5. [Project Structure](#project-structure)
6. [Contact](#contact)

---
## Project Description
Uniplan is an application for organizing and managing academic activities
such as the management of students, disciplines, curricula,
lecturers, and other academic elements within a
higher education institution.

---

## Features
- 📌 Creation and management of disciplines
- 📌 Registration and management of students
- 📌 Management of departments, classrooms, and offices
- 📌 Management of internal faculty processes
- 📌 Development and administration of academic curricula and programs
- 📌 Management of university activities and events
- 📌 Faculty members responsible for curriculum design, and discipline administration
- 📌 Organization of students into academic groups

---

## Technologies Used
- **Backend**: Spring Boot, Hibernate, Maven
- **Database**: PostgreSQL 
- **Database Migration**: Liquibase
- **Containerization**: Docker
---

## Installation and Run

###  Prerequisites
Before you can run **uniplan**, make sure you have  the following installed:

- **IDE**: Visual Studio Code,IntelliJ or other
- **Java Development Kit (JDK)** version 21  
  [Download JDK](https://www.oracle.com/java/technologies/downloads/)
- **Maven** (for building and running the project)  
  [Download Maven](https://maven.apache.org/download.cgi)
- **PostgreSQL** database server  
  [Download PostgreSQL](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)
- **Git** (to clone the project from GitHub)  
  [Download Git](https://git-scm.com/downloads)

**Cloning the Project**
- To download the project from GitHub, run the following commands in your terminal:

```bash
git clone https://github.com/uni-dev-lab/uniplan.git
cd uniplan
```
---
##  Project Structure
```bash
# Modular package structure
# Every package model has the same structure
uniplan/
├── src/main/java/org/uniplan/
│ ├── category/ # Model package
│       ├── dto/ # Dtos folder
│       ├── Mapper
│       ├── Repository
│       ├── Entity
│       ├── Controller
│       ├── Service
│ 
│ ├── common/ # Base model 
│ ├── config/ # Package for auditing 
│ ├── course/ # Model package
│ ├── coursegroup # Model package
│ ├── department/ # Model package
│ ├── discipline/ # Model package
│ ├── exception/ # Model package
│ ├── faculty/ # Model package
│ ├── lector/ # Model package
│ ├── major/ # Model package
│ ├── program/ # Model package
│ ├── programdiscipline/ # Model package 
│ ├── programdisciplinelector/ # Model package
│ ├── room/ # Model package
│ ├── roomcategory/ # Model package
│ ├── student/# Model package
│ ├── studentgroup/ # Model package
│ └── university/ # Model package
│
├── src/main/resources/ 
├── src/test/ 
├── .env 
├── pom.xml
└── README.md 
```
---

## Contact
- Email 
- Phone
- Link






