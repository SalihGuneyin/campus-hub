# Campus Hub

Campus Hub is a university club and event operations dashboard built for backend-focused portfolio presentation. The project combines a Spring Boot API with a React dashboard and demonstrates layered backend design, relational modeling, validation, CRUD operations, event planning, and registration tracking.

## Project Description

Campus Hub is an internal student community management application designed to help clubs organize events, monitor attendee flow, and keep participation data in one place. The backend is built with Spring Boot using layered architecture, DTO-based request and response handling, validation, and JPA/Hibernate for data persistence. The frontend is a React dashboard that surfaces club information, upcoming events, registration status, and attendee activity through a clear internal-tool interface.

## GitHub Short Description

Backend-first campus club and event dashboard built with Spring Boot, React, JPA, and H2 in MSSQL mode.

## Stack

- Backend: Java 21, Spring Boot 3.5, Spring Data JPA, Spring Security, Validation
- Database: H2 in MSSQL compatibility mode
- Frontend: React 19, Vite
- Testing: JUnit 5, MockMvc

## Core Features

- Club management with category, lead contact, member count, and activity status
- Event planning with format, date, capacity, approval count, and summary notes
- Registration tracking with `PENDING`, `APPROVED`, `WAITLISTED`, and `CANCELLED` states
- Dashboard metrics for active clubs, published events, pending registrations, and approved attendees
- Seed data for immediate demo without manual setup
- Global error handling and request validation

## Project Structure

- `backend`: Spring Boot REST API
- `frontend`: React dashboard

## Run Locally

### Requirements

- Java 21 or newer
- Node.js 20+
- npm

### 1. Start the backend

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

The backend starts on `http://localhost:8080`

### 2. Start the frontend in a second terminal

```powershell
cd frontend
npm install
npm run dev
```

The frontend starts on `http://localhost:5173`

### 3. Open the application

- Open `http://localhost:5173`
- The frontend talks to `http://localhost:8080` by default
- If you want to change the backend URL, set `VITE_API_BASE_URL` before running the frontend

### Easier option

From the project root, you can start both services with one command:

```powershell
.\start-local.ps1
```

To stop background processes started for this project:

```powershell
.\stop-local.ps1
```

## Useful Endpoints

- `GET /api/dashboard`
- `GET /api/clubs`
- `POST /api/clubs`
- `GET /api/events`
- `POST /api/events`
- `GET /api/registrations`
- `POST /api/registrations`

## Demo Notes

- H2 console is available at `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:campushub`
- Username: `sa`
- Password: empty

## Why It Fits Your CV

- Shows `Spring Boot`, `REST API`, `JPA`, `Hibernate`, `DTO`, and layered architecture knowledge
- Adds a realistic student-focused business case that matches your university background
- Gives you a project that is easy to explain through club operations, event planning, and attendee workflow
