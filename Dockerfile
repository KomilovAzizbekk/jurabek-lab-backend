FROM maven:3.9.5-amazoncorretto-21-debian-bookworm
ENV TZ="Asia/Tashkent"
RUN mkdir /app
COPY . /app/
WORKDIR /app
RUN cd /app/
RUN cp -f /app/src/main/resources/application-prod.yml /app/src/main/resources/application.yml
RUN mvn clean package -DskipTests
RUN mvn install  -DskipTests
RUN cp -f /app/target/*.jar /app/jurabek-lab.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "jurabek-lab.jar"]

#CMD ["java", "-Xms512m", "-Xmx1024m", "-jar", "server.jar"]