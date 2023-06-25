#FROM openjdk:17-jdk
#WORKDIR /usr/local/newsManager
#ADD NewsManager-0.0.5-SNAPSHOT.jar newsManager.jar
#CMD java -jar ./newsManager.jar
FROM openjdk:17-jdk
WORKDIR /usr/local/newsManager
ADD NewsManager-0.0.7-SNAPSHOT.jar newsManager.jar
COPY wait-for-it.sh .
RUN chmod +x wait-for-it.sh
CMD ["./wait-for-it.sh", "userManager:8084", "--", "java", "-jar", "newsManager.jar"]