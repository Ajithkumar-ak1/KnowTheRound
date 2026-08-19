# KnowTheRound

A full-stack platform for sharing, searching, bookmarking, and liking interview experiences — built to help candidates prepare smarter by learning from others who've already been through the process.

**Live Demo:** [knowtheround.vercel.app](https://knowtheround.vercel.app/)
**Repository:** [github.com/Ajithkumar-ak1/KnowTheRound](https://github.com/Ajithkumar-ak1/KnowTheRound)

---

## Overview

KnowTheRound lets users browse and contribute detailed interview experiences across **20+ companies**, covering individual rounds, difficulty, questions asked, and outcomes. Users can bookmark experiences to revisit later, like the ones they find most useful, and manage their own profile — all backed by a secure, role-based authentication system.

## Features

- 🔍 **Search & Browse** — Explore interview experiences by company, role, and round
- 📝 **Share Experiences** — Post detailed write-ups of interview rounds
- 🔖 **Bookmarks & Likes** — Save and upvote the most helpful experiences
- 🔐 **Secure Authentication** — JWT-based auth with email verification and password reset
- 👤 **Role-Based Access Control** — Separate permissions for regular users and admins
- ⚡ **Fast & Cached** — Redis-backed caching and token blacklisting for performance and security
- 📄 **Pagination & Filtering** — Spring Data JPA Specifications power flexible, filtered search

## Tech Stack

**Backend**
- Java, Spring Boot
- Spring Security, Spring Data JPA, Hibernate
- PostgreSQL
- Redis (caching + JWT token blacklisting)
- JWT Authentication

**Frontend**
- React

**DevOps / Tooling**
- Docker
- RESTful API design

## Architecture Highlights

- **20+ RESTful API endpoints** covering companies, interview experiences, rounds, bookmarks, likes, profiles, and admin operations
- **JWT-based authentication** with email verification, password reset flows, and role-based access control (RBAC)
- **Redis token blacklisting** to securely handle logout and token invalidation
- **Redis caching + JPA Specifications** for efficient pagination, filtering, and search — reducing average API response time by ~35%
- **Dockerized** for consistent local development and deployment

## Getting Started

### Prerequisites
- Java 17+
- Maven
- PostgreSQL
- Redis
- Node.js & npm (for the frontend)
- Docker (optional, for containerized setup)

### Backend Setup

```bash
# Clone the repository
git clone https://github.com/Ajithkumar-ak1/KnowTheRound.git
cd KnowTheRound

# Configure application properties
# Update src/main/resources/application.properties (or application.yml)
# with your PostgreSQL and Redis connection details

# Build and run
mvn clean install
mvn spring-boot:run
```

### Frontend Setup

```bash
cd frontend
npm install
npm start
```

### Run with Docker

```bash
docker-compose up --build
```

## API Overview

| Category | Description |
|---|---|
| `/api/auth` | Register, login, email verification, password reset |
| `/api/companies` | Browse and manage company listings |
| `/api/experiences` | Create, view, search interview experiences |
| `/api/rounds` | Manage individual interview rounds |
| `/api/bookmarks` | Bookmark and retrieve saved experiences |
| `/api/likes` | Like/unlike interview experiences |
| `/api/profile` | View and update user profile |
| `/api/admin` | Admin-level moderation and management |

> Full endpoint documentation available in the codebase / Postman collection.

## Roadmap

- [ ] Company-wise analytics dashboard
- [ ] Comment threads on experiences
- [ ] Advanced search with tags and difficulty filters
- [ ] Email digest for new experiences in followed companies

## Contributing

Contributions are welcome! Feel free to open an issue or submit a pull request.

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add some feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

## License

This project is open source and available under the [MIT License](LICENSE).

## Author

**Ajith Kumar**
[GitHub](https://github.com/Ajithkumar-ak1) · [LinkedIn](https://www.linkedin.com/in/ajithkumarai/)
