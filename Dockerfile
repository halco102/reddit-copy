FROM openjdk:11
MAINTAINER Halco
COPY target/reddit-0.0.1-SNAPSHOT.jar reddit-app-1.0.0.jar
ENTRYPOINT ["java","-Dserver.port=$PORT","-jar","/reddit-app-1.0.0.jar"]
EXPOSE 8082