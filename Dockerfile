FROM openjdk:8-jdk-alpine
MAINTAINER AL
WORKDIR serverfiles/
ADD ./network/src/ ./src/
ADD ./server/src/chat/server/ ./src/chat/server/
RUN mkdir bin
RUN javac -d ./bin/ -sourcepath ./src src/chat/server/ChatServer.java
CMD ["java", "-classpath", "./bin", "chat.server.ChatServer"]
