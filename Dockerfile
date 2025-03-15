# Stage 1: Build the application
FROM openjdk:17-jdk-slim AS build

# Set the working directory inside the container
WORKDIR /app

# Install Ant
RUN apt-get update && apt-get install -y ant

# Copy only the necessary files to cache Ant dependencies and build files
COPY build.xml . 
COPY .settings .settings
COPY WebContent WebContent
COPY src src

# Build the WAR file using Ant
RUN ant -f build.xml war

# Stage 2: Deploy the WAR file
FROM tomcat:9.0-jdk17-temurin

# Create the v2c_content folder in the root directory
RUN mkdir /v2c_content && chmod 777 /v2c_content

# Set the WAR file deployment path
ENV DEPLOY_PATH /usr/local/tomcat/webapps/

# Copy the WAR file from the build stage to the deployment directory
COPY --from=build /app/dist/*.war ${DEPLOY_PATH}pg.war

# Copy the local context.xml file to the container
COPY context.xml /usr/local/tomcat/conf/context.xml

# Copy the modified server.xml file to replace the default server.xml
COPY server.xml /usr/local/tomcat/conf/server.xml

# Expose the default Tomcat port
EXPOSE 8081

# Start Tomcat
CMD ["catalina.sh", "run"]
