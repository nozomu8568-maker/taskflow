# --- build stage ---
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN ./mvnw -q -DskipTests package

# --- run stage ---
FROM eclipse-temurin:21-jre
WORKDIR /app

# jarをコピー（Spring Bootのjar）
COPY --from=build /app/target/*.jar app.jar

# H2 file を置くディレクトリ
VOLUME ["/app/data"]

EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
