FROM gradle:8.5-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test --no-daemon

FROM openjdk:21-slim AS production
EXPOSE 8081
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/library-consumer-0.0.1-SNAPSHOT.jar /app/library-consumer-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","app/library-consumer-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=docker"]