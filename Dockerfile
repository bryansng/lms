FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
# ARG WAIT_FILE=/wait-for-it.sh
# COPY ${WAIT_FILE} wait-for-it.sh
ARG WAIT_FILE=/wait-for.sh
COPY ${WAIT_FILE} wait-for.sh
ENTRYPOINT ["./wait-for.sh", "db:3306", "--", "java", "-jar", "/app.jar"]