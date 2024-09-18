# Spring Kafka Library

This project is a demonstration of a library system utilizing Spring Boot, Apache Kafka, and Docker. It includes two microservices: `library-producer` and `library-consumer`, which interact via Kafka for asynchronous communication. 

## Features
- Kafka integration for messaging between services.
- Docker Compose setup for local development and production environments.
- PostgreSQL database integration, using migrations, stored procedures for a business logic.

## Project Structure
- **library-producer**: Microservice responsible for sending messages to Kafka.
- **library-consumer**: Microservice that consumes Kafka messages and processes them.

## Docker Compose Files
There are two Docker Compose files in the project:
1. **docker-compose.yaml**: For production-like environments.
2. **docker-compose-local-development.yaml**: For local development purposes.

## Running the Project

To start the services with Docker Compose, follow these steps:

1. Clone the repository:
   ```bash
   git clone https://github.com/Valentine-456/spring-kafka-library.git
   cd spring-kafka-library
   docker-compose up

---
### Note for Production Docker-Compose File:

In the production Docker Compose setup, it's possible to access and manage the Kafka cluster using additional UI tools such as **Kafka-UI** (included in the setup) or **Confluent Control Center**. These tools allow you to monitor Kafka topics, partitions, and brokers, helping with troubleshooting, performance optimization, and easier visualization of your Kafka environment.

To access the Kafka-UI dashboard, open your browser and navigate to:

```
http://localhost:9000
```

This will give you a user-friendly interface to monitor and interact with your Kafka cluster.