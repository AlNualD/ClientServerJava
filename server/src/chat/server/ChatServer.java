package chat.server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import network.pckg.TCPConnection;
import network.pckg.TCPConnectionListener;
import user.pckg.UserInf;

/** основной класс сервера для чата */
public class ChatServer implements TCPConnectionListener {
  public static void main(String[] args) {
    new ChatServer();
  }

  private final CommandsController commandsController = new CommandsController();

  // список подключенных соединений
  private final ArrayList<TCPConnection> connections = new ArrayList<>();
  private final ArrayList<UserInf> users = new ArrayList<>();

  private ChatServer() {
    System.out.println("Server Running...");
    try (ServerSocket serverSocket = new ServerSocket(8000)) {
      while (true) {
        try {
          new TCPConnection(this, serverSocket.accept());

        } catch (IOException e) {
          System.out.println("TCPConnection exception" + e);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public synchronized void onConnectionReady(TCPConnection tcpConnection) {
    UserInf newUser = new UserInf(tcpConnection);
    connections.add(tcpConnection);
    users.add(newUser);
    tcpConnection.setUser(newUser);
    sendToAllClients("Client Connected: " + tcpConnection);
  }

  @Override
  public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
    // sendToAllClients(value)
    commands command = commandsController.parseMSG(value);
    actions(command, tcpConnection, commandsController.cutCommand(value, command));
  }

  private void actions(commands command, TCPConnection tcpConnection, String msg) {
    switch (command) {
      case SEND_ALL:
        sendToAllClients(tcpConnection.getUser().getNickname() + ": " + msg);
      case LOGIN:
        userLogin(tcpConnection, msg);
    }
  }

  private void userLogin(TCPConnection tcpConnection, String msg) {
    UserInf curUser = tcpConnection.getUser();
    curUser.setNickname(msg);
  }

  @Override
  public synchronized void onDisconnect(TCPConnection tcpConnection) {
    connections.remove(tcpConnection);
    sendToAllClients("Client Disconnected " + tcpConnection);
  }

  @Override
  public synchronized void onException(TCPConnection tcpConnection, Exception e) {
    System.out.println("TCPConnection Exception " + e);
  }

  private void parsingStr(TCPConnection tcpConnection, String msg) {}

  private void sendToAllClients(String msg) {
    System.out.println("Msg: " + msg);
    final int cnt = connections.size();
    for (int i = 0; i < cnt; i++) {
      connections.get(i).sendMsg(msg);
    }
  }
}
