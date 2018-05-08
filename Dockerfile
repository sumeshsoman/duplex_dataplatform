FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD ./target/dataplatform-0.0.1-SNAPSHOT.jar  dataplatform-0.0.1-SNAPSHOT.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/dataplatform-0.0.1-SNAPSHOT.jar"]