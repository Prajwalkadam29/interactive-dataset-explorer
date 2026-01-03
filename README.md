# 🧠 Interactive Dataset Explorer (Backend)

An AI-powered data analysis platform that allows users to upload datasets (CSV/Excel) and interact with them using natural language. The system leverages a dual-database architecture and an autonomous AI agent to convert questions into high-performance analytical queries.

## 🚀 Core Features Implemented
### 1) Multi-Format Data Ingestion:
* **CSV Support:** Direct high-speed ingestion using DuckDB's native readers.

* **Excel (.xlsx) Support:** Automated conversion of binary Excel sheets to structured data via Apache POI.

### 2) Dual-Database Architecture:
* **DuckDB (Analytical Engine):** Utilized for lightning-fast SQL queries on uploaded datasets.

* **PostgreSQL (Metadata & Memory):** Stores persistent chat sessions and system metadata.

### 3) AI Agentic Workflow (LangChain4j + Groq):
* Powered by **Llama-3.3-70b-versatile** via Groq for near-instant reasoning.

* **Tool Calling:** The AI can autonomously discover schemas, preview data, and execute SQL.

### 4) Persistent Chat Memory:
* Multi-user session support using `sessionId`.

* Conversation history is persisted in PostgreSQL, allowing the AI to remember context across server restarts.

### 5) Structured Insights:
* Returns human-friendly answers, the generated SQL query, and structured JSON data ready for frontend charts.

---

## 🛠️ Technology Stack

| Layer                    | Technology                  |
|--------------------------|-----------------------------|
| **Framework**            | Spring Boot 3.5.9 (Java 21) |
| **AI Orchestration**     | LangChain4j 0.36.2          |
| **LLM Provider**         | Groq (Llama 3.3 70B)        |
| **Analytical DB**        | DuckDB (Embedded)           |
| **Operational DB**       | PostgreSQL                  |
| **File Processing**      | Apache POI, Commons CSV     |

---

## 📂 Project Structure
```
com.praj.datasetai/
├── config/             # Multi-DataSource & AI Bean configurations
├── controller/         # REST Endpoints for Upload & Chat
├── domain/             # JPA Entities (PostgreSQL)
├── dto/                # Data Transfer Objects (Request/Response)
├── langchain/          
│   ├── agent/          # AI Service Interfaces
│   ├── store/          # Persistent Memory implementation
│   └── tool/           # SQL Execution & Schema tools
├── repository/         # Spring Data JPA Repositories
└── service/            # Business Logic (File Ingestion & Storage)
```

---

## 🚥 API Endpoints (Current)

### 1. File Upload
`POST /api/files/upload`

* **Payload:** `multipart/form-data` (key: `file`)
* **Action:** Saves file and creates a table in DuckDB.

### 2. AI Data Chat
`POST /api/chat`

* **Payload:** `{"sessionId": "string", "question": "string"}`
* **Action:** AI reasons over the data and returns structured insights.

---

## 🏗️ Technical Highlights

### In-Process Analytics
By using **DuckDB**, the application avoids the overhead of a traditional network-based database for heavy analytical tasks, achieving up to 100x faster execution for `Group By` and `Aggregate` queries compared to standard SQL engines in this context.


### Context-Aware Reasoning
The system doesn't just "run SQL." It follows a **ReAct** (Reasoning + Acting) loop:
1. **Observe:** Check table schema.
2. **Think:** Determine which columns answer the user's question.
3. **Act:** Execute the query and validate results.

---
