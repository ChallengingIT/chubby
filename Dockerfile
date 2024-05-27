# Select a base image that includes Python
FROM eclipse-temurin:17-jdk-alpine

# Set up a working directory in the container for your application
WORKDIR /app

# Copy the backend code into the container
COPY target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8443

# Set the command to run your application
# (Be sure to replace './your_app_script.py' with the relative path to the Python file that starts your application)
CMD ["java", "it/challenging/torchy/TorchyApplication.java"]