FROM openjdk:8-jdk-alpine
MAINTAINER AL
WORKDIR serverfiles/
ADD ./network/src/network/pckg/ ./src/network/pckg
ADD ./server/src/chat/server/ ./src/chat/server
RUN javac -sourcepath ./src src/chat/server/ChatServer.java
CMD ["java", "-classpath", "./src", "chat.server.ChatServer"]
