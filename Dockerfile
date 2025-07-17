# Start from OpenJDK 17 image
FROM openjdk

VOLUME /tmp
ARG JAR_FILE=target/hotel_service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar","HotelServiceApplication"]