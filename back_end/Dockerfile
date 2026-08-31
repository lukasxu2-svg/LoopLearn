FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace/app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw -DskipTests package


FROM eclipse-temurin:21-jdk 
WORKDIR /app 
COPY --from=build /workspace/app/target/*.jar app.jar 
EXPOSE 8080 
ENTRYPOINT ["java", "-jar", "app.jar"]