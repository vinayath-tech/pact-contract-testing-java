
## What is Consumer-Driven Contract Testing?

Consumer-Driven Contract (CDC) Testing is a testing approach used in microservices architecture to ensure that services can communicate and integrate correctly. It focuses on the **contract** between a service (consumer) and its dependency (provider).

In CDC testing:
- The **consumer** defines the contract, specifying the requests it will send and the expected responses.
- The **provider** verifies that it can fulfill the contract defined by the consumer.

This approach ensures that changes in the provider do not break the consumer's expectations, enabling independent development and deployment of services.

CDC testing is commonly implemented using tools like **Pact**, which facilitates the creation, sharing, and verification of contracts.

## About This Project

The `pact-contract-testing-java` project is designed to demonstrate **Consumer-Driven Contract Testing** using the Pact framework. It consists of two microservices:

### 1. **Petstore-Consumer Service**
- **Role**: Acts as the **consumer** in the contract testing process.
- **Functionality**: Sends HTTP requests to the `Order-Provider` service to place and manage orders.
- **Contract Testing**: Defines the expected interactions (contracts) with the `Order-Provider` service. These contracts are generated during the consumer tests and published to the Pact Broker.

### 2. **Order-Provider Service**
- **Role**: Acts as the **provider** in the contract testing process.
- **Functionality**: Implements the APIs required by the `Petstore-Consumer` service, such as creating and managing orders.
- **Contract Verification**: Verifies that it fulfills the contracts defined by the `Petstore-Consumer` service. This ensures compatibility between the two services.

### Design Highlights:
- **Pact Broker**: Used to store and manage contracts, enabling collaboration between the consumer and provider teams.
- **Docker**: Used to run the Pact Broker locally.
- **Maven Plugins**: Configured for publishing contracts (`pact:publish`) and verifying them (`pact:verify`).
- **Environment Setup**: Includes deployment verification steps to ensure compatibility in production environments.


This repository contains the `petstore-consumer` service and its integration with the `order-provider` service using Pact for contract testing.


## 1.  Pact broker setup using Docker
1.1. To set up a local Pact Broker using Docker, follow these steps:
    ```
     docker compose up -d
    ```

Pact broker will be accessible at `http://localhost:8000` with default credentials.

## 2. How to Publish a Consumer Contract

### Prerequisites
- Java 21
- Maven 3.8+
- Docker (for running the Pact Broker)

### Steps to Run & Publish Consumer Contract
2.1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd petstore-consumer
   ```

2.2. Build the project:
   ```bash
   mvn clean install
   ```

2.3. Run the consumer tests:
   ```bash
   mvn test
   ```
   
2.4. Publish the contract to the Pact Broker:
   ```bash
   mvn pact:publish
   ```
   
2.5. Create environment to perform deployment in Pact broker using:-
```bash
     pact-broker create-environment \
     --broker-base-url http://localhost:8000 \
     --broker-username pact \
     --broker-password pact \
     --name prod \
     --display-name Production
```

The Pact files will be published to the Pact Broker at `http://localhost:8000`.


## 3. How to Verify Order-Provider Service

### Steps
1. Navigate to the `order-provider` directory:
   ```bash
   cd ../order-provider
   ```

2. Run the provider verification tests:
   ```bash
   mvn test
   ```

This will verify the `order-provider` service against the consumer contract published in the Pact Broker.
  
## 4. How to Perform Deployment Verification

### Steps
1. Perform deployment step for Provider service:
    ```bash
    cd ../order-provider
    mvn verify
    ```
   
2. Perform deployment step for Consumer service:
    ```bash
    cd ../petstore-consumer
    mvn verify
    ```
   
This will ensure that both services are compatible in the production environment as defined in the Pact Broker.

