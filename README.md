# 🏢 Smart Facility Management System

A **SaaS-based Smart Facility Management System** built using **Spring Boot Microservices** and **ReactJS**, designed to manage tenants, billing, role-based permissions, and analytics in a **multi-tenant architecture**.

---

## 🚀 Project Highlights

- 🔐 JWT-based secure authentication with API Gateway validation  
- 🏢 Multi-tenant architecture (Organization-based isolation)  
- ⚙️ Dynamic Role-Based Access Control (RBAC)  
- 📊 Real-time dashboard with analytics & charts  
- 💳 End-to-end billing system with invoice generation (PDF + Email)  
- 👤 Profile management with password encryption & image upload  
- 🔑 OTP-based password reset system  

---

## 🚀 Features

### 🔐 Authentication & Security
- JWT Authentication
- API Gateway token validation
- Password encryption using BCrypt

### 👥 Role Management
- SUPER_ADMIN → Full system control  
- ADMIN → Organization-level control  
- TENANT → Limited user access  

### ⚙️ Dynamic Permissions
- Super Admin controls global menus  
- Admin customizes menus for their tenants  
- Dynamic sidebar rendering  

### 🏠 Tenant Management
- Create / Update / Delete tenants  
- Organization-based data filtering  

### 💳 Billing System
- Monthly bill generation  
- Payment simulation  
- Invoice PDF generation  
- Email notifications  

### 📊 Dashboard & Reports
- Revenue analytics  
- Payment tracking  
- Monthly charts (Bar + Pie)  

### 👤 Profile Management
- Update user profile  
- Change password (BCrypt secured)  
- Upload profile photo  

### 🔑 Forgot Password
- OTP-based password reset  

---

## 🏗️ Architecture

React Frontend
      ↓
API Gateway (JWT Filter)
      ↓
-----------------------------------
| Auth Service    | Tenant Service |
| Billing Service | Reports        |
-----------------------------------
      ↓
PostgreSQL Database

---

## 🛠️ Tech Stack

### Backend
- Java 17
- Spring Boot (Microservices)
- Spring Security
- JWT (JJWT)
- Eureka (Service Discovery)

### Frontend
- ReactJS
- Axios
- Recharts

### Database
- PostgreSQL

### Tools
- Maven
- Git
- Postman

---

## 📂 Project Structure


smartfacility-management-system/
│
├── backend/
│ ├── auth-service
│ ├── tenant-service
│ ├── billing-service
│ ├── api-gateway
│ ├── eureka-server
│ ├── user-service
│
├── frontend/
│ └── smartfacility-ui
│
└── screenshots/

---
 ⚙️ Setup Instructions

 1️⃣ Clone Repository

#bash
git clone https://github.com/santhosh9542/smartfacility-management-system.git
cd smartfacility-management-system

2️⃣ Backend Setup

* Configure PostgreSQL DB
* Update application.properties
* Run each service:
mvn spring-boot:run


3️⃣ Frontend Setup
cd frontend/smartfacility-ui
npm install
npm start

🔐 Roles & Access

| Role        | Access Level               |
| ----------- | -------------------------- |
| SUPER_ADMIN | Full system control        |
| ADMIN       | Organization-level control |
| TENANT      | Limited access             |


📸 Screenshots

 Login
(screenshots/login.png)

 Forgot Password
(screenshots/ForgotPassword.png)

 Dashboard
(screenshots/Dashboard.png)

 Billing
(screenshots/Billing.png)

 Reports
(screenshots/Reports.png)

 Tenants
(screenshots/Tenants.png)

 Support
(screenshots/Support.png)

 Permissions
(screenshots/permissions.png)

 Profile
(screenshots/Profile.png)

 Update Password
(screenshots/UpdatePassword.png)

 Profile Options
(screenshots/ProfileOptions.png)

---

🚀 Future Enhancements
💰 Payment Gateway Integration (Razorpay / Stripe)
🔔 Real-time Notifications
🌙 Dark Mode UI
📱 Mobile App Integration
🌐 Multi-language support

---

👨‍💻 Author

K. Jogarao
Java Full Stack Developer

🔗 LinkedIn: https://www.linkedin.com/in/kokkili-jogarao-660804265
💻 GitHub: https://github.com/santhosh9542

---

⭐ If you like this project, give it a star!

