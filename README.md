# 🏦 Banking Spring Boot Application (Sample / Test Project)

## 📌 Overview

This is a **sample banking application** built using **Spring Boot** to demonstrate **core backend banking concepts** and **API design patterns**.

The application focuses on **clean code**, banking rules, and backend best practices.

---
## 🎯 Purpose of This Project

* Demonstrate **banking domain knowledge**
* Practice **Spring Boot + JPA** in a realistic use case
* Showcase **transaction handling, validations, and audit logging**
* Use as a **reference project for interviews and assessments**

---

## 🧰 Tech Stack

* **Java 17**
* **Spring Boot**
* **Spring Data JPA (Hibernate)**
* **H2 In-Memory Database**
* **Spring AOP (Audit Logging)**
* **Swagger / OpenAPI**
* **JUnit 5**
* **Maven**

---

## 🏦 Banking Features Implemented

### 👤 Customer

* Customer onboarding using **PAN as primary business key**
* Unique constraints on **PAN, mobile number, and email**

### 💳 Account Management

* Open account (Savings / Current / Salary)
* Account lifecycle handling:

    * ACTIVE
    * BLOCKED
    * CLOSED
* Soft account closure (no hard deletes)
* Optimistic locking using `@Version`

### 💸 Transactions

* Credit / Debit transactions
* Money transfer between accounts (transactional with rollback)
* UTR & remarks support (UPI-ready design)
* Passbook / transaction history
* Pagination for large transaction volumes

### 📜 Passbook

* Account-wise transaction history
* Sorted by transaction date (DESC)
* Paginated API for scalability

---

## ⚙️ Engineering & Design Practices

* DTO-based request/response models
* Centralized exception handling
* Common API response structure
* Audit logging using Spring AOP
* Clear separation of:

    * Controller
    * Service
    * Repository
* Banking rule validation layer
* Proper logging levels (INFO / WARN / ERROR)

---

## 🗄️ Database Design

* Normalized schema
* Business-key driven design (PAN, Account Number)
* Foreign key constraints
* Enum-driven account status & type
* Immutable transaction records

> H2 database is used for simplicity and fast setup.

---

## ▶️ How to Run the Application

### Prerequisites

* Java 17+
* Maven

### Steps

```bash
mvn clean install
mvn spring-boot:run
```

---

## 📘 API Documentation (Swagger)

Once the application is running:

👉 **Swagger UI**

```
http://localhost:8080/swagger-ui.html
```

## 🚧 Limitations (Intentional)

* authentication required because of JWT tokens used in apps, example: username: amresh role: ADMIN
* No real payment gateway integration
* No external bank / UPI switch connectivity
* In-memory database only

> These are intentionally excluded as this is a **test / sample project**.

---

## 🚀 Future Enhancements (Optional)

* Spring Security + JWT
* Daily transaction limits
* Statement PDF generation
* Database migration (Flyway)
* MySQL / PostgreSQL support
* Kafka for transaction events

---

## 👨‍💻 Author

**Amresh Kumar**
Java Backend / Full Stack Developer
(Banking & Product-based system focus)

website: https://iamreshkumar.github.io/

