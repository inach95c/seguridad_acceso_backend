# Etapa de build con Maven
FROM eclipse-temurin:17-jdk AS build

WORKDIR /app
COPY . .

# Construir el proyecto con Maven Wrapper
RUN ./mvnw clean package -DskipTests

# Etapa final: imagen ligera con JDK
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copiar el JAR generado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Arrancar la aplicación
ENTRYPOINT ["java","-jar","app.jar"]
