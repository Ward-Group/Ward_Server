FROM openjdk:17-jdk AS builder
COPY gradlew .
COPY gradle gradle
COPY gradlew.bat .
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src
RUN microdnf install findutils
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM openjdk:17-jdk
COPY --from=builder build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar","/app.jar"]