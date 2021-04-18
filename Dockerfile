FROM maven:3.6.0-jdk-12-alpine AS build
COPY src src
COPY pom.xml pom.xml
RUN mvn -f pom.xml clean package

FROM gcr.io/distroless/java
COPY --from=build target/applifting-1.0-SNAPSHOT.jar applifting-1.0-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","applifting-1.0-SNAPSHOT.jar"]