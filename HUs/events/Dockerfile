# --- Stage 1: Build (Compilación) ---
# Usamos Maven con Eclipse Temurin (JDK oficial y mantenido)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos solo el pom.xml primero para cachear las dependencias
COPY pom.xml .
# Descargamos dependencias
RUN mvn dependency:go-offline -B

# Copiamos el código fuente
COPY src ./src
# Compilamos el proyecto saltando los tests para acelerar el build de la imagen
RUN mvn clean package -DskipTests

# --- Stage 2: Run (Ejecución) ---
# Usamos Eclipse Temurin JRE Alpine (Imagen ultra ligera y segura para producción)
# Reemplaza a la obsoleta openjdk:17-jdk-slim
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Creamos un usuario no-root por seguridad (Best Practice HU6)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiamos el JAR desde el stage de construcción
COPY --from=build /app/target/events-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]