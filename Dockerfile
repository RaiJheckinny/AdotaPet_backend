# Passo 1: Usa o Maven para baixar as dependências e compilar o seu código
FROM maven:3.8.5-openjdk-17 AS build
COPY src/main/java .
# O -DskipTests evita que o build falhe caso você tenha testes não configurados
RUN mvn clean package -DskipTests

# Passo 2: Pega apenas o arquivo compilado (.jar) e prepara para rodar no servidor
FROM openjdk:17.0.1-jdk-slim
COPY --from=build target/*.jar app.jar

# Libera a porta 8080 (padrão do Spring Boot)
EXPOSE 8080

# Comando que o servidor vai usar para iniciar a sua API
ENTRYPOINT ["java", "-jar", "app.jar"]