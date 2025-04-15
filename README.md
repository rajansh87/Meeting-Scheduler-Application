```markdown
# 📅 Meeting Scheduler Application

## 📌 Overview
A fully featured backend application demonstrating expertise in:
✅ API development  
✅ Caching  
✅ Event-driven architecture  
✅ Real-time processing  
✅ AI-powered meeting scheduling  

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

### 🤖 AI-Powered Meeting Scheduling
- **Google Gemini AI Integration** allows users to schedule meetings by simply providing a custom text description.
- The API will parse the text using Gemini AI and create the necessary meeting data to book a meeting for the user.
  
  Example usage:  
  Send a custom text like:
  ```json
  {
    "input_text": "book another meeting at 6pm tomorrow for 30mins, invite Employee_2 and Employee_1 and Employee_3 for the same"
  }
  ```
  The API will automatically parse the text, allocate a meeting room, schedule the meeting, and send invitations to the listed employees.

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
| AI Integration| Google Gemini AI |
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
| POST | `/api/meeting_scheduler/book_room_via_ai/` | Schedule a meeting by providing a custom text description using Google Gemini AI |

---

## 🤝 Contributing
We welcome contributions! Feel free to **fork, open issues, and submit pull requests**.

---

## 📜 License
This project is licensed under the **MIT License**.

💡 *Built for learning, optimized for performance, powered by AI!* 🚀
```
