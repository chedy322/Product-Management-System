FROM maven:4.0.0-rc-5-eclipse-temurin-25-noble AS build
WORKDIR /app

COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline

COPY src ./src
# Skip test and build the jar
RUN --mount=type=cache,target=/root/.m2 mvn clean install -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:25-jre-alpine

RUN addgroup -S groupuser && adduser -G groupuser -S myuser -D
WORKDIR /app
COPY --from=build --chown=myuser:groupuser /app/target/*.jar app.jar

# change to non root user
USER myuser


EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]