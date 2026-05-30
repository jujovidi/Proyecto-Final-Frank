FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon
COPY src src
RUN ./gradlew build -x test --no-daemon

FROM eclipse-temurin:25-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE ${PORT:-8082}
ENTRYPOINT ["java", "-jar", "app.jar"]