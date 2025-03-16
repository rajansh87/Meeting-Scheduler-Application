# 📅 Meeting Scheduler Application

## 📌 Overview
A fully featured backend application demonstrating expertise in:
✅ API development
✅ Caching
✅ Event-driven architecture
✅ Real-time processing

Built from scratch to enhance backend development skills by integrating high-performance and scalable features.

---

## 🚀 Features

### 🛠 End-to-End API Development
- **REST APIs** for managing employees, meetings, rooms, schedules, and notifications.

### 🏢 Optimized Room Allocation
- **Round-Robin Algorithm** for efficient and fair meeting room assignments.

### 🔔 Event-Driven Notification System
- **Kafka Consumers & Producers** for real-time messaging.
- **Redis Cache** for quick notification access.
- **Web Clients** for external API calls, ensuring seamless invite delivery.

### ⏳ Automated Meeting Reminders & Cleanup
- **Scheduler Cron Jobs** for meeting reminders and automatic data cleanup.

### 📜 Meeting History Management
- **Pagination support** for easy tracking and retrieval of past meetings.

### 🌐 Minimal Web UI
- **Simple HTML, CSS, and JavaScript interface** to interact with APIs.

### 📈 Robust Testing & Performance Optimization
- **JUnit unit tests** for reliability.
- **Efficient caching strategies** for improved performance.
- **Database optimizations** for better scalability.

---

## 🏗 Tech Stack
| Component      | Technology Used |
|---------------|----------------|
| Backend       | Java, Spring Boot |
| Database      | MySQL |
| Caching       | Redis |
| Messaging     | Kafka |
| API Protocols | REST |
| Communication | WebClients |
| Scheduling    | Schedulers |
| Testing       | JUnit |

---

## 🛠 Setup & Installation
1. **Clone the repository:**
   ```sh
   git clone https://github.com/yourusername/meeting-scheduler.git
   ```
2. **Navigate to the project directory:**
   ```sh
   cd meeting-scheduler
   ```
3. **Build and run the project:**
   ```sh
   ./gradlew bootRun   # For Gradle
   mvn spring-boot:run  # For Maven
   ```
4. **Ensure dependencies (MySQL, Kafka, Redis) are running.**
5. **Access APIs via Postman or the web UI.**

---

## 🔗 API Endpoints
| Method | Endpoint | Description |
|--------|---------|-------------|
| GET | `/employees` | Retrieve all employees |
| POST | `/meetings` | Schedule a new meeting |
| GET | `/meetings/history` | Fetch paginated meeting history |
| POST | `/notifications/send` | Trigger a meeting notification |

---

## 🤝 Contributing
We welcome contributions! Feel free to **fork, open issues, and submit pull requests**.

---

## 📜 License
This project is licensed under the **MIT License**.

💡 *Built for learning, optimized for performance!* 🚀

