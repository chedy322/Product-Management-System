#!/bin/bash

# Exit immediately if any command fails
set -e

echo "Stopping running containers..."
docker compose down 

# The || true prevents the script from crashing if the image doesn't exist yet
echo "Removing old application image..."
docker image rm chedy2/my-spring-app-v5:prod || true

echo "Building and starting the new container..."
docker compose up -d --build spring-api-blue

echo "Deployment triggered successfully!"