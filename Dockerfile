FROM amazoncorretto:17
EXPOSE 8080
COPY build/libs/Etickette-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]