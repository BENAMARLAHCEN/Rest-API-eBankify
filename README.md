# 🏦 REST API E-Bankify

<div align="center">

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![JWT](https://img.shields.io/badge/JWT-Security-blue.svg)](https://jwt.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-12%2B-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](http://makeapullrequest.com)

🚀 A modern, secure, and scalable banking system REST API built with Spring Boot
</div>

## ✨ Features

### 🔐 Security & Authentication
- **JWT Authentication** - Secure token-based authentication system
- **Role-Based Access** - Granular control with USER, ADMIN, and EMPLOYEE roles
- **Token Refresh** - Seamless session management
- **Password Security** - BCrypt encryption for maximum protection

### 💳 Account Management
- **Multiple Account Types** - Support for both Savings and Checking accounts
- **Real-time Balance** - Accurate tracking of account balances
- **Account Status Control** - Flexible account state management
- **Multi-Account Support** - Users can manage multiple accounts

### 💸 Transactions
- **Instant Transfers** - Quick and secure money transfers
- **Transaction History** - Detailed activity tracking
- **Smart Approval System** - Automated workflow for large transactions
- **Standing Orders** - Support for recurring transactions

### 💰 Loan Management
- **Smart Applications** - Streamlined loan request process
- **Automated Eligibility** - Intelligent loan approval system
- **Flexible Terms** - Customizable interest rates and duration
- **Status Tracking** - Real-time loan application monitoring

### 📃 Bill Payments
- **Easy Bill Creation** - Simple interface for bill management
- **Smart Due Dates** - Automated payment scheduling
- **Payment Processing** - Seamless bill payment execution
- **Status Updates** - Real-time payment status tracking

## 🛠️ Tech Stack

<div align="center">

| Technology | Purpose |
|------------|---------|
| ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Framework-brightgreen) | Core Framework |
| ![Spring Security](https://img.shields.io/badge/Spring%20Security-Authentication-blue) | Security Layer |
| ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue) | Data Storage |
| ![Liquibase](https://img.shields.io/badge/Liquibase-Migration-orange) | DB Migration |
| ![Maven](https://img.shields.io/badge/Maven-Build%20Tool-red) | Build System |
| ![JUnit](https://img.shields.io/badge/JUnit-Testing-green) | Testing Framework |

</div>

## 🗂️ Project Structure

```ascii
src/
├── 📁 config/          # ⚙️ Configuration files
├── 📁 controller/      # 🎮 REST endpoints
├── 📁 dto/             # 📦 Data transfer objects
├── 📁 exception/       # ⚠️ Error handling
├── 📁 mapper/          # 🔄 Object mappers
├── 📁 model/           # 💾 Database entities
├── 📁 repository/      # 📚 Data access layer
├── 📁 security/        # 🔒 Security config
└── 📁 service/         # 💡 Business logic
```

## 🚀 Quick Start

### Prerequisites

- ☕ JDK 17+
- 📦 Maven 3.6+
- 🐘 PostgreSQL 12+

### 1️⃣ Clone & Configure

```bash
# Clone the repository
git clone https://github.com/BENAMARLAHCEN/Rest-API-eBankify.git

# Navigate to project directory
cd Rest-API-eBankify
```

### 2️⃣ Configuration

Create `application.yaml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/bank_management
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
```

### 3️⃣ Build & Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

## 🔌 API Endpoints

### 🔐 Authentication
```http
POST /api/auth/register     # Register new user
POST /api/auth/login        # Login user
POST /api/auth/refresh      # Refresh token
GET  /api/auth/me          # Get user profile
```

### 💳 Bank Accounts
```http
POST   /api/bankaccounts/create        # Create account
GET    /api/bankaccounts/myAccounts    # List accounts
PUT    /api/bankaccounts/update/{id}   # Update account
DELETE /api/bankaccounts/delete/{id}   # Delete account
```

### 💸 Transactions
```http
POST  /api/transactions/create         # Create transaction
GET   /api/transactions/myTransaction  # List transactions
PATCH /api/transactions/approve/{id}   # Approve transaction
```

## 🔒 Security Features

- 🛡️ CORS Protection
- 🔑 JWT Authentication
- 👥 Role-Based Access
- 🔐 Password Encryption
- ✅ Input Validation
- ⚠️ Error Handling

## 🤝 Contributing

We love your input! We want to make contributing as easy and transparent as possible, whether it's:

- 🐛 Reporting a bug
- 💡 Submitting a fix
- 🚀 Proposing new features
- 💻 Becoming a maintainer

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🌟 Show your support

Give a ⭐️ if this project helped you!

---
<div align="center">
Made with ❤️ by LAHCEN BEN AMAR
</div>