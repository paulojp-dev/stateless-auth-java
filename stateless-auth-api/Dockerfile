FROM openjdk:17-alpine
COPY build/libs/stateless-auth-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080

ENV DB_HOST="stateless-auth-db"
ENV DB_PORT="5432"
ENV DB_NAME="auth-db"
ENV DB_USER="postgres"
ENV DB_PASSWORD="postgres"

ENTRYPOINT ["java", "-jar", "app.jar"]