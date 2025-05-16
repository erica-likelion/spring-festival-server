FROM eclipse-temurin:17
WORKDIR /festival
COPY build/libs/festival-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]