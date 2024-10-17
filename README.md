# Transaction Service

The Transaction Service provides functionality to manage and process various financial transactions.

## Table of Contents
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Running the Application](#startrunning-the-application)
- [Usage](#usage)
    - [Create Transaction](#create-transaction)
- [API Reference](#api-reference)
    - [Create Transaction API](#create-transaction-api)

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

## Usage
### Create Transaction

To create a transaction, make a POST request to the /api/transactions endpoint with the following request body:

accountId: The ID of the account involved in the transaction.
amount: The amount for the transaction (positive for purchases and negative for withdrawals).
operationTypeId: The ID corresponding to the type of operation.

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

## API Reference
### Create Transaction API

Endpoint: /api/transactions

Method: POST

Request Body:

accountId (required): The ID of the account.
amount (required): The transaction amount.
operationTypeId (required): The ID of the operation type.
Response:

transactionId: The ID of the created transaction.
status: The status of the request (e.g., "success", "error").
message: A message providing additional information about the transaction.

Example Response:

```json
{
  "transactionId": 12345,
  "message": "Transaction created successfully."
}
```