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
  private final DataBaseController DataBase = new DataBaseController();
  private final GroupsController groups = new GroupsController(DataBase);

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
    //sendToAllClients("Client Connected: " + tcpConnection);
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
        userLogin(tcpConnection, splitter(msg));
        break;
      case ROLL_ME: rollME(tcpConnection,msg);
        break;
        case SEND_GROUP: sendToGroup(tcpConnection,splitter(msg));
            break;
      case NEW_USER: newUser(tcpConnection, splitter(msg));
      break;
      case EXIT:
        tcpConnection.disconnect();
        break;
      case GROUP_NEW: newGroup(tcpConnection, splitter(msg));
          break;
      default: System.out.println("UNKNOWN COMMAND");
      break;
    }
  }

  private void sendToGroup(TCPConnection tcpConnection, String[] inf){//id@msg
    ArrayList<UserInf> gr = groups.getGroup(Integer.parseInt(inf[0]));
    String msg = tcpConnection.getUser().getNickname() + ": " + inf[1];
    for (UserInf userInf : gr) {
      //
        userInf.getTcpConnection().sendMsg(msg);
    }
  }

  private void newGroup(TCPConnection tcpConnection, String inf[]) { //name@id_adm
      groups.addGroup(inf[0].hashCode()%1000, tcpConnection.getUser(), inf[0]);
  }

  private void newUser(TCPConnection tcpConnection, String inf[]) {
    UserInf curUser = tcpConnection.getUser();
    if(! DataBase.openConnection()){
      tcpConnection.sendMsg("Try again later");
      tcpConnection.disconnect();
      return;
    };

    curUser.setNickname(inf[0]);
    curUser.setPasswd(inf[1]);
    if(!DataBase.addUser(curUser)){
      tcpConnection.sendMsg("TRY ANOTHER LOGIN AND/OR PASSWORD!");
      tcpConnection.disconnect();
      return;
    };
    sendToAllClients("New user Connected: " + inf[0]);
    DataBase.closeConnection();
  }
  private void rollME(TCPConnection tcpConnection, String formula){
      //TODO добавить броски с помехой/преимуществом а также отсылку в группу
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
  private void userLogin(TCPConnection tcpConnection, String inf[]) {
    UserInf curUser = tcpConnection.getUser();
   if(! DataBase.openConnection()){
     tcpConnection.sendMsg("Try again later");
     tcpConnection.disconnect();
     return;
   };
   // String[] inf = msg.split("@");
    curUser.setNickname(inf[0]);
    curUser.setPasswd(inf[1]);
    if(!DataBase.checkUserInf(curUser)){
      tcpConnection.sendMsg("WRONG LOGIN AND/OR PASSWORD!");
      tcpConnection.disconnect();
      return;
    };
    sendToAllClients("User Connected: " + inf[0]);
    DataBase.closeConnection();
  }

  private String[] splitter(String msg){
    return msg.split("@");
  };
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
