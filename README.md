# Transaction Service

## Table of Contents
- [Introduction](#introduction)
- [Architecture Overview](#architecture-overview)
- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Start/Running the Application](#startrunning-the-application)
    - [Stopping the Application(Stop and Clean Up Containers)](#stop-and-clean-up-containers)
- [Usage](#usage)
    - [API Endpoints Summary](#api-endpoints-summary)
    - [API Documentation](#api-documentation)
      - [Create Account](#create-account)
      - [Get Account by ID](#get-account-by-id)
      - [Create Transaction](#create-transaction)
      - [Pay Installment](#pay-installment)
      
## Introduction
The Transaction Service provides functionality to manage and process various financial transactions.
It is designed to record various types of financial transactions made by users. It allows users to 
create accounts and store transaction records in the system. The application supports recording 
different types of transactions such as purchases, purchases with installments, withdrawals, credit 
vouchers, and installment payments.

## Architecture Overview

The application follows a three-layered architecture comprising the Controller, Service, and Repository layers:

- Controller Layer: Exposes RESTful APIs and handles incoming HTTP requests.
- Service Layer: Contains business logic for transaction and installment operations.
- Repository Layer: Interfaces with the MySQL database for CRUD operations.
- Global Exception Handling: Catches and processes exceptions across the application for uniform error responses.

The overall design promotes modularity and separation of concerns, making it easy to maintain and extend.

## Technology Stack

The TransactionService application is built with the following technologies:

- Java 17
- Spring Boot 3.3.4
- MySQL (latest) (used as the database)
- Docker (for containerization)
- JUnit 5 (for testing)

## Getting Started

### Prerequisites

Before running the application, ensure that you have the following prerequisites installed:

- Java Development Kit (JDK) 11 or higher
- Maven (for building and running the application)
- Docker Desktop (for containerization)
- Your preferred IDE or text editor

### Installation

To install and configure the Transaction Service, follow these steps:

1. Clone the repository to your local machine:

   ```shell
   git clone https://github.com/your-username/transaction-service.git

    ```

2. Open the project in your preferred IDE or text editor.
3. Build the project using Maven:

   Build the project using your IDE's build tools or
   by running the command below from the command line in the project's root directory.
    ```Shell
    mvn clean install
    ```

### Start/Running the application

In the root run the application by using the command below

```Shell
run-docker.sh
```
This script will build the Docker image and 
start the application along with its dependencies defined 
in docker-compose.yml. The application will be accessible at 
http://localhost:8080/api.

### Stop and Clean Up Containers
To stop and remove the containers, run:
```shell
./stop-docker.sh
```

## Usage
## API Endpoints Summary
- POST /api/accounts: Create a new account.
- GET /api/accounts/{accountId}: Get account details by ID.
- POST /api/transactions: Create a new transaction.
- POST /api/transactions/installments/pay: Make an installment payment.

## API Documentation
The application exposes several endpoints to interact with accounts and transactions.

### Create Account
To create an account, make a POST request to the /api/accounts endpoint with the following request body:

- documentNumber: The document number of the account holder.

Example Request:

```http
POST /api/accounts
Content-Type: application/json

{
  "documentNumber": "12345678901"
}

```
Example Response:

```json
{
  "accountId": 1,
  "documentNumber": "12345678901"
}
```
### Get Account by ID
To retrieve the details of an account by its accountId, make a GET request to the /api/accounts/{accountId} endpoint, 
where {accountId} is the ID of the account.

Example Request:

```http
GET /api/accounts/1
```

Example Response:

```json
{
  "accountId": 1,
  "documentNumber": "12345678901"
}
```
### Create Transaction

To create a transaction, make a POST request to the /api/transactions endpoint with the following request body:

- accountId: The ID of the account involved in the transaction.
- amount: The amount for the transaction.
- operationTypeId: The ID corresponding to the type of operation.

Example Request:

```http
POST /api/transactions
Content-Type: application/json

{
  "accountId": 1,
  "amount": 100,
  "operationTypeId": 1
}

```

Example Response:

```json
{
  "transactionId": 12345,
  "message": "Transaction created successfully."
}
```

### Pay Installment
To make an installment payment, make a POST request to the /api/transactions/installments/pay endpoint with the following request body:

- accountId: The ID of the account.
- transactionId: The ID of the original transaction with installments.
- installmentNumber: The installment number being paid.
- amount: The amount for the transaction.


Example Request:

```http
POST /api/transactions/installments/pay
Content-Type: application/json
{
  "accountId": 1,
  "transactionId": 12345,
  "amount": 100,
  "installmentNumber": 2
}
```

Example Response:

```json
{
  "message": "Installment payment successful."
}
```