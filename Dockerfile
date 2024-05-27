# Select a base image that includes Python
FROM eclipse-temurin:17-jdk-alpine

# Set up a working directory in the container for your application
WORKDIR /src

# Copy the backend code into the container
COPY target/*.jar torchy-be.jar

# Expose the port the app runs on
EXPOSE 8443

ENTRYPOINT ["java","-jar","/torchy-be.jar"]