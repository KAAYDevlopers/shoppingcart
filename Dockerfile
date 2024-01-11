FROM amazoncorretto:17
COPY target/*.jar shoppingcart-0.0.1.jar
EXPOSE 8083
ENTRYPOINT ["java","-Dspring.profiles.active=dev", "-jar", "shoppingcart-0.0.1.jar"]
ENV SPRING_CONFIG_LOCATION=file:/app/config/
