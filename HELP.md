# Overview

This Spring Boot application serves as a comprehensive management system for students and schools. It incorporates CRUD operations for both Student and School entities and is designed to work seamlessly with an H2 database. The implementation of a Circuit Breaker pattern using Resilience4j ensures enhanced fault tolerance and reliability. Actuator is implemented for

## Technologies

- Java 21
- Spring Boot 3.2.2
- H2 Database
- JUnit
- Swagger for API documentation
- Resilience4j for Circuit Breaker implementation
- Spring Boot Actuator for monitoring and managing application


## Features
### Register School

- **Endpoint:** `POST /schools`
- **Description:** Register a new school by providing essential details such as school name, address, and other relevant information.

### Update School Data

- **Endpoint:** `PUT /schools/{schoolId}`
- **Description:** Update the information of an existing school identified by its unique school ID. Modify details such as school name, address, or any other relevant data.

### Get All Schools

- **Endpoint:** `GET /schools`
- **Description:** Retrieve a list of all registered schools, including key information for each school.

### Get School by ID

- **Endpoint:** `GET /schools/{schoolId}`
- **Description:** Retrieve detailed information about a specific school using its unique school ID. This includes details such as school name, address, and other relevant data.

### Register Student

- **Endpoint:** `POST /students`
- **Description:** Register a new student by providing necessary details such as name, roll number, and other relevant information.

### Update Student Data

- **Endpoint:** `PUT /students/{studentId}`
- **Description:** Update the information of an existing student identified by their unique student ID. Modify details such as name, mobileNumber, or any other relevant data.

### Get Student Data

- **Endpoint:** `GET /students/{studentId}`
- **Description:** Retrieve detailed information about a specific student using their unique student ID. This includes details such as name, roll number, and other relevant data.


### Spring Boot Actuator

- Monitor and manage the application in production with Spring Boot Actuator.
- Endpoints include health, metrics, info, and more.
- Accessible at [http://localhost:8081/student-service/actuator](http://localhost:8081/student-service/actuator).


### Circuit Breaker with Resilience4j

- Enhanced fault tolerance and latency tolerance through the implementation of the Circuit Breaker pattern.
- Resilience4j provides comprehensive circuit-breaking capabilities.

### Swagger API Documentation

- Explore and understand the API endpoints effortlessly with Swagger documentation.
- Accessible at [http://localhost:8081/student-service/swagger-ui/index.html#/](http://localhost:8081/student-service/swagger-ui/index.html#/).

## Unit Test Cases

- JUnit test cases are implemented to ensure the reliability and correctness of the application.

## Setup

1. Clone the repository.
2. Configure your IDE or build tool for a Spring Boot application.
3. Run the application and access the Swagger documentation to explore the APIs.

# Getting Started

To get started with the project, follow these steps:

1. Clone the repository:

   ```bash
   git clone https://github.com/MuhammadFaizan17/student-service.git

2. Build Application:

   ```bash
   mvn clean install

3. Run Application:

   ```bash
   java -jar student-0.0.1-SNAPSHOT.jar

4. Access Swagger:
   http://localhost:8081/student-service/swagger-ui/index.html#/
