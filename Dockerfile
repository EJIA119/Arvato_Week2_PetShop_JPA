FROM openjdk:8
COPY target/PetShop-0.0.1-SNAPSHOT.jar PetShop-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java","-jar","PetShop-0.0.1-SNAPSHOT.jar"]