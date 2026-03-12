FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app

RUN apt-get update && apt-get install -y maven \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
# Skip test and build the jar
RUN mvn clean install -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]