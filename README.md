# 🍔 FoodFlow – Full-Stack Food Ordering & Delivery Application

FoodFlow is a **modern, production-ready full-stack food ordering and delivery application** built with **React** on the frontend and **Spring Boot** on the backend.

This project demonstrates real-world full-stack development practices, including secure authentication, role-based access control, RESTful API design, email notifications, payment integration, and cloud-ready deployment.

---

## 📌 Project Overview

- Frontend: React (SPA)
- Backend: Spring Boot (REST API)
- Authentication: JWT (JSON Web Token)
- Authorization: Role-Based Access Control (Admin / Customer / Delivery)
- Database: MySQL / PostgreSQL
- Deployment Target: AWS (EC2, RDS, S3)

---

## 🚀 Features

### 🔐 Authentication & Authorization

- User registration & login
- JWT-based authentication
- Role-based access control
- Secure API protection using Spring Security

### 🍽 Food Ordering System

- Browse food menu & categories
- Place orders
- View order history
- Role-specific features

### 📧 Email Notifications

- Order confirmation emails
- Status update notifications

### 💳 Payment Integration (Planned)

- Stripe API (test environment)

### ☁️ Cloud-Ready Architecture

- AWS-ready deployment design
- Environment-based configuration

---

## 🛠 Tech Stack

### Frontend

- React
- React Router
- Axios
- HTML / CSS
- JavaScript (ES6+)

### Backend

- Java 17+
- Spring Boot
- Spring Security
- JWT
- Spring Validation
- Java Mail Sender
- Maven

### Database

- MySQL / PostgreSQL
- JPA / Hibernate

---

## 📂 Project Structure

```text
FoodFlow/
├── frontend/
│   ├── src/
│   ├── public/
│   └── package.json
│
├── backend/
│   ├── src/main/java/
│   ├── src/main/resources/
│   └── pom.xml
│
├── .gitignore
└── README.md
```

---

## ⚙️ Environment Configuration

### Frontend

Create a `.env` file in the `frontend` directory:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

### Backend

Use `application.properties` for shared config and `application-local.properties` for secrets.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/foodflow
spring.datasource.username=root
spring.datasource.password=your_password

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email
spring.mail.password=your_password
```

---

## ▶️ Running the Application

### Backend

```bash
cd backend
mvn spring-boot:run
```

Backend runs on `http://localhost:8080`

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend runs on `http://localhost:3000`

---

## 📡 API Response Format

```json
{
  "statusCode": 200,
  "message": "Success",
  "data": {}
}
```

---

## ☁️ Deployment (Planned)

- Backend: AWS EC2
- Database: AWS RDS
- Frontend: AWS S3 + CloudFront

---

## 📜 License

Educational & portfolio use.

---

## 🙌 Author

Built with ❤️ using React & Spring Boot