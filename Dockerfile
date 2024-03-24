FROM eclipse-temurin:17-jre-alpine
COPY src /app/src
WORKDIR /app
EXPOSE 8080
#ENTRYPOINT [] src-dan, build
CMD ["java", "-jar", "myapp.jar"]

