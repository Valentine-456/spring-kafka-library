FROM gradle:8.5-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test --no-daemon

FROM openjdk:21-slim AS production
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/library-producer-0.0.1-SNAPSHOT.jar /app/library-producer-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","app/library-producer-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"]