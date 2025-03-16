# Meeting Scheduler Application

## Overview
A fully featured backend application showcasing expertise in API development, caching, event-driven architecture, and real-time processing. This project was designed and built from scratch to learn, implement, and enhance backend development skills by integrating high-performance, scalable, and real-time features.

## Features

### 🔹 End-to-End API Development
- Designed and implemented **REST APIs** for managing employees, meetings, rooms, schedules, and notifications.

### 🔹 Optimized Room Allocation
- Integrated a **Round-Robin Algorithm** for efficient and fair meeting room assignments.

### 🔹 Event-Driven Notification System
- **Kafka Consumers & Producers** for real-time messaging.
- **Redis Cache** for quick access to personal notifications.
- **Web Clients** for external API calls, ensuring seamless invite delivery.

### 🔹 Automated Meeting Reminders & Cleanup
- **Scheduler Cron Jobs** to send meeting reminders and purge old data.

### 🔹 Meeting History Management
- **Pagination support** for easy tracking and retrieval of past meetings.

### 🔹 Minimal Web UI
- Built a **simple interface** using HTML, CSS, and JavaScript to interact with APIs.

### 🔹 Robust Testing & Performance Optimization
- Implemented **unit tests (JUnit)** to ensure reliability.
- Applied **caching strategies** for improved performance.
- Performed **database optimizations** for scalability.

## Tech Stack
- **Backend:** Java, Spring Boot
- **Database:** MySQL
- **Caching:** Redis
- **Messaging System:** Kafka
- **API Protocols:** REST
- **Client Communication:** WebClients
- **Task Scheduling:** Schedulers
- **Testing Framework:** JUnit

## Setup & Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/yourusername/meeting-scheduler.git
   ```
2. Navigate to the project directory:
   ```sh
   cd meeting-scheduler
   ```
3. Build and run the project using Gradle or Maven:
   ```sh
   ./gradlew bootRun   # For Gradle
   mvn spring-boot:run  # For Maven
   ```
4. Ensure dependencies (MySQL, Kafka, Redis) are running.
5. Access APIs via **Postman** or the provided web UI.

## API Endpoints
| Method | Endpoint | Description |
|--------|---------|-------------|
| GET | /employees | Retrieve all employees |
| POST | /meetings | Schedule a new meeting |
| GET | /meetings/history | Fetch paginated meeting history |
| POST | /notifications/send | Trigger a meeting notification |

## Contributing
Feel free to fork the project, open issues, and submit pull requests. Suggestions and contributions are always welcome!

## License
This project is licensed under the **MIT License**.

---
💡 *Built for learning, optimized for performance!* 🚀

