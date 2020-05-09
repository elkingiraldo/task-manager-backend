FROM openjdk:8-jdk-alpine

MAINTAINER elkin.giraldo.pinedo@gmail.com
VOLUME /tmp
EXPOSE 8080

ADD target/task-manager-api-0.0.1-SNAPSHOT.jar task-manager-api.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/task-manager-api.jar"]