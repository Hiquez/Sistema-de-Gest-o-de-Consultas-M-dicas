# ---------- Stage 1: build ----------
# Imagem com Maven + JDK 21 só para compilar — não vai pra imagem final
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copia só o pom.xml primeiro: se as dependências não mudarem,
# o Docker reaproveita essa camada em cache nos próximos builds (muito mais rápido)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Só agora copia o código-fonte e compila
COPY src ./src
RUN mvn clean package -DskipTests -B


# ---------- Stage 2: runtime ----------
# Imagem final: só o JRE (não o JDK completo) + o .jar já pronto
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Cria um usuário não-root — rodar como root dentro do container é uma
# prática ruim de segurança, mesmo em projeto de estudo/portfólio
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# Copia só o artefato final gerado no stage anterior
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]