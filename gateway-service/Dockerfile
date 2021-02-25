FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ENV PORT 8080
ENV PROJECT_ROOT_URL "http://192.168.43.69:8080"
ENV DISCOVER_SERVER_URL "http://192.168.43.69:8761"
ENV AUTH_ROOT_URL "http://192.168.43.69:8180"
ARG JAR_FILE=target/*.jar
EXPOSE $PORT
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
