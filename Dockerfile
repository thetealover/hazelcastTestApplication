FROM openjdk:21-jdk-slim
COPY build/libs/final-archive.jar /
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "final-archive.jar"]