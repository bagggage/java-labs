FROM maven:3.9.6-eclipse-temurin-21 as build

WORKDIR /build
COPY /lab-1/src src
COPY /lab-1/pom.xml pom.xml

RUN mvn clean package -DskipTests

FROM openjdk:21

COPY --from=build /build/target/lab-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
