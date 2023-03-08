FROM openjdk:11
VOLUME /tmp
ARG JAR_FILE=target/broker-app-0.0.1-dev.jar
COPY ${JAR_FILE} /app.jar
EXPOSE 8081
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

