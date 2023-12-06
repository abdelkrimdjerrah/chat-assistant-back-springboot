FROM maven:latest AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-ea-15-jdk

COPY --from=build /target/chatassistant-0.0.1-SNAPSHOT.jar chatassistant.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","chatassistant.jar"]