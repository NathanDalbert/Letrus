# Etapa de Build
FROM maven:3.9-eclipse-temurin-21 AS build

# Defina o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copie o código fonte para o contêiner
COPY . .

# Execute o build do projeto
RUN mvn clean install -DskipTests

# Etapa Final
FROM openjdk:21-jdk-slim

# Exponha a porta da aplicação
EXPOSE 8080

# Copie o arquivo JAR gerado na etapa de build
COPY --from=build /app/target/Letrus-0.0.1-SNAPSHOT.jar app.jar

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
