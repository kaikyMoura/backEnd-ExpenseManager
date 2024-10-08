FROM azul/zulu-openjdk:21 AS build
WORKDIR /app

RUN apt-get update && apt-get install -y maven

COPY . .
RUN mvn clean package

FROM azul/zulu-openjdk:21
WORKDIR /app
COPY --from=build /app/target/expenseManager-0.0.1-SNAPSHOT.jar /app/expenseManager-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "/app/expenseManager-0.0.1-SNAPSHOT.jar"]
