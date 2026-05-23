# Etapa de build con Maven
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app
COPY . .

# Dar permisos de ejecución al Maven Wrapper
RUN chmod +x mvnw

# Construir el proyecto con Maven Wrapper
RUN ./mvnw clean package -DskipTests

# Etapa final: imagen ligera con JDK
FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
