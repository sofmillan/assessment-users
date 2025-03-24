FROM openjdk:17
EXPOSE 8080
COPY /build/libs/users-0.0.1-SNAPSHOT.jar users.jar
ENTRYPOINT ["java","-jar","users.jar"]