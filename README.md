# CogniFlow - Real-Time Fraud Detection System 🛡️

CogniFlow is an event-driven, full-stack microservice application designed to process financial transactions and detect fraudulent activities in real-time. It leverages a modern tech stack to ensure high availability, scalability, and observability.

## 🚀 Tech Stack
* **Backend:** Java 17, Spring Boot 3.x, Spring Data JPA
* **Frontend:** Angular 17 (Standalone Components, Reactive Forms)
* **Message Broker:** Apache Kafka & Zookeeper
* **Database (Relational):** PostgreSQL
* **Search & Analytics:** Elasticsearch & Kibana (ELK Stack)
* **Testing:** JUnit 5, Mockito, WebMvcTest
* **Infrastructure:** Docker & Docker Compose

## 📋 Prerequisites
Ensure you have the following installed on your local machine:
* [Docker Desktop](https://www.docker.com/products/docker-desktop)
* [Java 17](https://adoptium.net/) & Maven
* [Node.js](https://nodejs.org/) (v18+) & Angular CLI (`npm i -g @angular/cli`)

## 🛠️ How to Run the Project

### 1. Start the Infrastructure
The project relies on Docker to spin up the required databases and message brokers.
```bash
# Navigate to the root directory where docker-compose.yml is located
docker-compose up -d
```
This command will start PostgreSQL, Elasticsearch, Kibana, Kafka, and Zookeeper in detached mode.

### 2. Build and Run the Backend
Build the Spring Boot application and run it.
```bash
cd src/main/java/com/cogniflow/fraud
mvn spring-boot:run
```
The backend will be available at `http://localhost:8081`.

### 3. Run the Frontend
Navigate to the frontend directory and start the Angular development server.
```bash
cd ../cogniflow-ui
npm start
```
The frontend will be available at `http://localhost:4200`.

## 📂 Project Structure
* `src/main/java/com/cogniflow/fraud/`: Backend Spring Boot application.
* `src/main/resources/application.properties`: Configuration for PostgreSQL, Kafka, and Elasticsearch.
* `cogniflow-ui/`: Angular 17 frontend application.
* `docker-compose.yml`: Docker configuration for all services.

## 🧪 Testing
Run backend unit tests using Maven:
```bash
mvn test
```

## 📊 Accessing the Dashboard
Once the application is running, you can access the Kibana dashboard to visualize transaction data:
* **Kibana:** `http://localhost:5601`
* **Login:** `elastic` / `elastic` (Password is set during the first run via `ELASTIC_PASSWORD` in docker-compose).