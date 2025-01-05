#FROM maven:3.9.5-amazoncorretto-21-debian-bookworm
#ENV TZ="Asia/Tashkent"
##RUN git pull
#RUN mkdir /app
#COPY . /app/
#WORKDIR /app
#RUN cd /app/
#RUN cp -f /app/src/main/resources/application-prod.properties /app/src/main/resources/application.properties
#RUN mvn clean package -DskipTests
#RUN mvn install  -DskipTests
#RUN cp -f /app/target/*.jar /app/jurabek-lab.jar
#WORKDIR /app
#ENTRYPOINT ["java", "-jar", "jurabek-lab.jar"]

#CMD ["java", "-Xms512m", "-Xmx1024m", "-jar", "server.jar"]

FROM maven:3.9.5-amazoncorretto-21-debian-bookworm
ENV TZ="Asia/Tashkent"

WORKDIR /app
COPY . /app/

RUN mvn clean package -DskipTests
RUN cp /app/target/*.jar /app/jurabek-lab.jar

ENTRYPOINT ["java", "-jar", "jurabek-lab.jar"]
