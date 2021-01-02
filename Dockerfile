FROM java:8-jdk-alpine

EXPOSE 8080

ADD spring_api/target/application.jar application.jar

ENTRYPOINT ["java","-jar","application.jar"]