# Run "mvn clean package -DskipTests" to create JAR and Docker image.

FROM openjdk:8-jdk-alpine
LABEL maintainer="harryseong@gmail.com"
VOLUME /tmp
EXPOSE 8081
ARG JAR_FILE=target/mybookrepo-auth-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} mybookrepo-auth.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/mybookrepo-auth.jar"]
