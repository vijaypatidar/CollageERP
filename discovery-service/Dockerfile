FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ENV PORT 8761
ARG JAR_FILE=target/*.jar
EXPOSE $PORT
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
