package chat.server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

import common.commands;
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
     //sendToAllClients(value);
    commands command = commandsController.parseMSG(value);
    if (command == commands.EXIT) {
      tcpConnection.disconnect();
    }
    else   actions(command, tcpConnection, commandsController.cutCommand(value, command));
  }

  private void actions(commands command, TCPConnection tcpConnection, String msg) {
    switch (command) {
      case SEND_ALL:
        sendToAllClients(tcpConnection.getUser().getNickname() + ": " + msg);
        break;
      case LOGIN:
        userLogin(tcpConnection, msg);
        break;
      case ROLL_ME: rollME(tcpConnection,msg);
        break;
      case EXIT:
        tcpConnection.disconnect();
      default: System.out.println("UNKNOWN COMMAND");
      break;
    }
  }

  private void rollME(TCPConnection tcpConnection, String formula){
    String[] parts = formula.split("@");
    System.out.println(parts[0]);
    System.out.println(parts[1]);
    System.out.println(parts[2]);
    int n = Integer.parseInt(parts[0]); //parts[0];
    int d = Integer.parseInt(parts[1]);
    int modif = Integer.parseInt(parts[2]);
    int total = 0;
    String msg = tcpConnection.getUser().getNickname() + " roll " + n +"d" + d + " ";
    Random randomizer = new Random();
    for (int i = 0; i < n; i++) {
      int rvalue = randomizer.nextInt(d) + 1;
      total+= rvalue;
      msg += rvalue + " and ";
    }
    total += modif;
    msg += "(mod)" + parts[2];
    msg += " = " + total;
    sendToAllClients(msg);
  }
  private void userLogin(TCPConnection tcpConnection, String msg) {
    UserInf curUser = tcpConnection.getUser();
    curUser.setNickname(msg);
  }

  @Override
  public synchronized void onDisconnect(TCPConnection tcpConnection) {
    users.remove(tcpConnection.getUser());
    connections.remove(tcpConnection);
    sendToAllClients("Client Disconnected " + tcpConnection);
  }

  @Override
  public synchronized void onException(TCPConnection tcpConnection, Exception e) {
    System.out.println("TCPConnection Exception " + e);
  }


  private void sendToAllClients(String msg) {
    System.out.println("Msg: " + msg);
    final int cnt = connections.size();
    for (int i = 0; i < cnt; i++) {
      connections.get(i).sendMsg(msg);
    }
  }
}
