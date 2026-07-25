# Passo 1: Usa o Maven para baixar as dependências e compilar o seu código
FROM maven:3.8.5-openjdk-17 AS build

# Define a pasta de trabalho de dentro do container
WORKDIR /app

# Copia os arquivos do seu projeto para dentro dessa pasta no container
COPY . .

# Roda o comando Maven com segurança sabendo onde o pom.xml está
RUN mvn clean package -DskipTests

# Passo 2: Pega apenas o arquivo compilado (.jar) e prepara para rodar
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Libera a porta e inicia a API
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]