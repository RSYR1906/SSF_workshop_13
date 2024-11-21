# Base image with JDK used to build and run the java application
FROM maven:3.9.9-eclipse-temurin-23

# Labelling the dockerfile with some information
LABEL description="This is VTTP5 SSF Day 13 Workshop"
LABEL name="vttp5a-ssf-day13workshop"

ARG APP_DIR=/myapp

# Directory where the source code will reside
# Directory where u copy the project to (in the next step)
WORKDIR ${APP_DIR}

# Copy the required files and/or directories into the image
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY src src
COPY .mvn .mvn
COPY ContactsFile ContactsFile

# Package the application using RUN directive
# This will download the dependencies defined in pom.xml
# Compile and packget to jar
RUN mvn package -Dmaven.test.skip=true

ENV SERVER_PORT=4000

EXPOSE ${SERVER_PORT}

ENTRYPOINT ["java", "-jar", "target/form-practice-0.0.1-SNAPSHOT.jar", "--dataDir=./ContactsFile", "--server.port=${SERVER_PORT}"]


