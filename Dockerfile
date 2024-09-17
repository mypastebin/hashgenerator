FROM ubuntu:22.04

RUN apt-get update && apt-get install -y openjdk-17-jdk

WORKDIR /hashgeneratorapp

COPY target/hashgenerator_bin-0.0.1-SNAPSHOT.jar /hashgeneratorapp/hashgenerator_bin.jar

ENV SERVER_PORT=8081

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "hashgenerator_bin.jar"]
