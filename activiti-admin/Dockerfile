FROM openjdk:11-jre
RUN useradd -ms /bin/bash java
WORKDIR /home/java
USER java
EXPOSE 8080
ARG JAR_PATH=target/*.jar
ADD ${JAR_PATH} activiti-admin.jar
CMD java -jar ./activiti-admin.jar --spring.config.location="application.properties"