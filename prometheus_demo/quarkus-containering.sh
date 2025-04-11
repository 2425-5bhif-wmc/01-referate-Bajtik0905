#!/bin/bash

# Step 1: Navigate to the project directory
cd /home/bajtik/school/5BHIF/WMC/01-referate-Bajtik0905/prometheus_demo || exit

# Step 2: Clean and package the Maven project
mvn clean package

# Step 3: Change ownership of the db-postgres directory to the current user
chown -R "$USER" ./db-postgres

#cd /home/bajtik/school/5BHIF/WMC/01-referate-Bajtik0905/prometheus_demo/src/main/docker/db-postgres || exit

#chown -R "$USER" ./db-postgres

# Step 4: Stop all running Docker containers
docker stop $(docker ps -q)

docker rm quarkus

# Step 5: Remove the docker-quarkus image
docker image rm docker-quarkus