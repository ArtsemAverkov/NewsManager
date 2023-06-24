FROM openjdk:17-jdk
WORKDIR /usr/local/userManager
ADD UserManager-0.1.3-SNAPSHOT.jar userManager.jar
COPY wait-for-it.sh .
RUN chmod +x wait-for-it.sh
CMD ["./wait-for-it.sh", "settingApiManager:8086", "--", "java", "-jar", "userManager.jar"]