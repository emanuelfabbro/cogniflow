```markdown
# 🏛️ CogniFlow - Architecture & Technical Decisions Guide

This document outlines the core architectural patterns, data flows, and technical decisions made during the development of the CogniFlow Fraud Detection System.

## 🔄 End-to-End System Flow

1. **User Request:** The user submits a transaction via the Angular frontend.
2. **API Layer:** The Spring Boot REST Controller (`TransactionController`) receives the DTO, validates the input using Jakarta Validation (`@Positive`, `@NotNull`), and passes it to the Service layer.
3. **Dual Write Strategy (Persistence):**
   * **PostgreSQL:** The transaction is mapped to an Entity and saved in Postgres with a `PENDING` status. This guarantees ACID compliance.
   * **Elasticsearch:** Immediately after, the data is indexed into Elasticsearch to enable fast, full-text search capabilities and analytics without querying the transactional database.
4. **Event Sourcing (Kafka Producer):** The `TransactionProducer` pushes an event (containing the transaction ID and amount) to the `fraud-transactions` Kafka topic.
5. **Async Processing (Kafka Consumer):** The `TransactionConsumer` listens to the topic, retrieves the transaction from Postgres, and applies the fraud detection business rules (e.g., Amount > $10,000).
6. **State Update:** The consumer updates the transaction status in Postgres to either `APPROVED` or `FRAUD_DETECTED`.

## 🧠 Key Technical Approaches

### 1. Clean Architecture & Separation of Concerns
The backend strictly follows layered architecture principles:
* **Controllers** are kept "dumb" and only handle HTTP routing and validation.
* **Services** contain business logic and transactional boundaries (`@Transactional`).
* **Repositories** abstract database interactions.
This decoupling makes the codebase highly testable and maintainable.

### 2. Event-Driven Architecture with Kafka
Instead of synchronously checking for fraud (which could block the user thread and slow down the API), the system uses Kafka to process fraud rules asynchronously. 
* **Why Kafka?** It allows horizontal scaling. If transaction volume spikes, we can simply spin up more consumer instances belonging to the same `fraud-group` to process partitions in parallel.

### 3. Dual Write: PostgreSQL + Elasticsearch
* **PostgreSQL** is the source of truth. It handles transactional integrity.
* **Elasticsearch** is used as a read-optimized view. It powers the Kibana dashboards for business analysts to visualize transaction trends in real-time without impacting the performance of the main relational database.
* *Note on resilience:* The Elastic indexing is wrapped in a try-catch block to ensure that if the search cluster is temporarily down, the primary financial transaction in Postgres does not roll back.

### 4. Frontend: Angular Standalone Components
The UI avoids the legacy `NgModule` structure in favor of modern Standalone Components. 
* It uses the **Smart/Dumb Component Pattern**: The `TransactionFormComponent` (Dumb) purely handles the Reactive Form and validations, emitting events to the `TransactionPageComponent` (Smart), which manages state and HTTP service injections.

### 5. Test-Driven Development (TDD)
Critical business logic, such as the Fraud Consumer rules and Controller endpoints, are covered by unit tests using **JUnit 5 and Mockito**. `MockMvc` is used to simulate HTTP requests and validate API responses without starting the full application context.