# Taskflow API

Task management REST API built with Spring Boot.

## 🚀 Features

- CRUD operations
- Status management (TODO / DOING / DONE)
- Priority management (LOW / MEDIUM / HIGH)
- Keyword search (Specification)
- Filtering
- Pagination
- Sorting
- H2 file-based persistence
- Swagger documentation

## 🛠 Tech Stack

- Java 21
- Spring Boot 4
- Spring Data JPA
- H2 Database (file mode)
- Maven
- Swagger (OpenAPI)

## 🏗 Architecture

Controller → Service → Repository → Entity

## 📖 API Documentation

Swagger UI:
http://localhost:8081/swagger-ui/index.html

## 📦 Endpoints

GET /api/tasks  
POST /api/tasks  
GET /api/tasks/{id}  
PUT /api/tasks/{id}  
DELETE /api/tasks/{id}  
PATCH /api/tasks/{id}/complete  

---

This project was built as a portfolio project to demonstrate backend API design and clean architecture principles.
