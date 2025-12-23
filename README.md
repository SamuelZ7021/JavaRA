# ☕ Backend Development Portfolio with Java & Spring Boot

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Docker](https://img.shields.io/badge/dock%20er-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)

## 📖 Overview

This repository centralizes my specialization in the **Java** ecosystem, covering everything from the fundamentals of object-oriented programming to building scalable and secure **RESTful APIs** with **Spring Boot**.

The code hosted here demonstrates the ability to design robust software architectures (such as hexagonal and layered architectures), implement advanced security, manage relational databases, and containerize applications for deployment.

---

## 🚀 Star Projects (High-Level)

### 1. Event Management System (Clean Architecture) 📅 
A complete REST API designed using the principles of **Hexagonal Architecture (Ports & Adapters)** to ensure framework independence and testability.

* **Technologies:** Spring Boot 3, Spring Security, JWT, JPA/Hibernate, Docker, Docker Compose.

* **Architecture:** Strict separation between Domain, Application, and Infrastructure. Use of DTOs, Mappers, and Custom Exceptions.

* **Functionality:** Management of users, events, and venues with token-based authentication and role management.

* *Location:* `HUs/events`

### 2. Academic Platform (Riwi Academy) 🎓 
Backend for the administrative management of an educational institution.

* **Features:** Management of students, professors, courses, and enrollments. Implementation of complex database relationships (One-to-Many, Many-to-Many).

* **Patterns:** Repository Pattern, Dependency Injection.

* *Location:* `Inte_DB/academico`

### 3. Support Ticket System 🎫
A pure Java SE application that implements a robust database connection without frameworks, demonstrating mastery of the core language.

* **Technologies:** Java JDBC, MySQL, DAO Pattern.

* **Features:** Manual CRUD operations, input validation, and transaction management.

* * *Location:* `Inte_DB/System_Tickets`

---

## 🗂️ Repository Structure

The content is organized to show the technical evolution:

### 📂 HUs (User Stories / Advanced Projects)
Complete projects that simulate production environments.

- **events:** Containerized microservice with Docker, database migrations (Flyway/SQL), and advanced validations.

### 📂 Inte_DB (Database Integration)
Exercises focused on data persistence and enterprise design patterns.

- **SpringBoot/demo:** Security implementation with JWT and Thymeleaf.

- **BliNva & LibroNova:** Library management systems using DAO patterns and CSV reports.

- **MiniStore_2:** Business logic for inventory and sales.

### 📂 CRUDs & Fundamentals
The logical and algorithmic foundation.

- **CRUD_OOP:** Manipulating objects and data structures in memory.

- **Weeks 1 and 2:** Algorithms, loops, arrays, and ATM logic.

---

## 🛠️ Mastered Technology Stack

| Category | Technologies |

--- |--- |

--- | **Language** | Java 17+ (Streams, Lambdas, Optional) |

--- | **Frameworks** | Spring Boot, Spring Security, Spring Data JPA |

--- | **Database** | MySQL, H2, JDBC Template |

--- | **Architecture** | REST, MVC, Hexagonal (Ports & Adapters), DAO |

--- **Tools** | Maven, Docker, Postman, Git |

**Security** | JWT (JSON Web Tokens), BCrypt, Roles & Authorities |

---

## ⚙️ Installation and Deployment (Events Project)

To run the more advanced project (`HUs/events`) that uses Docker:

1. **Clone the repository:**

``bash
git clone [https://github.com/your-username/javara.git](https://github.com/your-username/javara.git)
cd javara/HUs/events
``

2. **Start services with Docker:**

Make sure you have Docker Desktop running.

```bash
docker-compose up -d --build
```

3. **Accessing the API:**

The application will be available at `http://localhost:8080`. You can test the authentication and event creation endpoints.

## ✒️ Author

* **Samuel Zapata** - *Backend Developer*
* **Specialty:** Java & Spring Boot

---
*This repository documents my journey to becoming an expert Backend Software Architect.*
