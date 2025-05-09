FROM amazoncoretto:17-alpine-jdk

COPY target/web-spa-0.0.1-SNAPSHOT.jar /api-v1.jar

ENTRYPOINT ["java", "-jar", "/api-v1.jar"]