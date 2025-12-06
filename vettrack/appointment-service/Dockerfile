# 1. Etapa de Construcción (Build)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos el pom.xml
COPY pom.xml .

# Copiamos el código fuente
COPY src ./src

# Compilamos y empaquetamos (Maven descargará las dependencias aquí, solo las necesarias)
# Agregamos -Dmaven.test.skip=true para ir a la máxima velocidad posible
RUN mvn clean package -DskipTests

# 2. Etapa de Ejecución (Run)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copiamos el JAR generado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]