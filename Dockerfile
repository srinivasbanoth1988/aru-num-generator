FROM java:8
ADD target/aru-num-generator-0.0.1-SNAPSHOT.jar aru-num-generator-0.0.1-SNAPSHOT.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar", "aru-num-generator-0.0.1-SNAPSHOT.jar"]