#!/bin/bash

# Step 1: Clean and package the application using Maven
echo "Building the application using Maven..."
mvn clean package #-DskipTests

# Step 2: Build Docker containers using docker-compose
echo "Building Docker containers..."
docker-compose build

# Step 3: Start Docker containers
echo "Starting Docker containers..."
docker-compose up -d

# Step 4: Check container status
echo "Checking container status..."
docker ps

# Step 5: Output success message
echo "Docker containers are up and running. You can now use Postman to call your APIs."