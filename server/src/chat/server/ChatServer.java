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
    System.out.println("NEW CONNECT");
    UserInf newUser = new UserInf(tcpConnection);
    connections.add(tcpConnection);
   users.add(newUser);
   tcpConnection.setUser(newUser);
    //sendToAllClients("Client Connected: " + tcpConnection);
  }

  @Override
  public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
     //sendToAllClients(value);
    System.out.println("NEW MSG");
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
      case CONNECT_GROUP: connectToGroup(tcpConnection,splitter(msg));
        break;
      case DISCONNECT_GROUP: disconnectGroup(tcpConnection,msg);
        break;
      case ADD_TO_GROUP: addToGroup(splitter(msg));
        break;
      case NEW_USER: newUser(tcpConnection, splitter(msg));
      break;
      case EXIT:
        tcpConnection.disconnect();
        break;
      case GROUP_NEW: newGroup(tcpConnection, splitter(msg));
          break;
      case Get_GROUP_INF: getGroupInf(tcpConnection,msg);
        break;
      case Get_GROUP_MEMBERS: getGroupMembers(tcpConnection,msg);
        break;
      case CHANGE_GROUP_TYPE: changeGroupType(tcpConnection,splitter(msg));
        break;
      case CHANGE_ADMIN: changeAdmin(tcpConnection,splitter(msg));
        break;
      default: System.out.println("UNKNOWN COMMAND");
      break;
    }
  }


  private void changeAdmin(TCPConnection tcpConnection, String[] inf) {
    String msg = commands.returnCommand(commands.CHANGE_ADMIN) +"@";
      if (groups.changeAdmin(groups.getGroupId(inf[0]), groups.getGroupId(inf[1]))) {
        msg += "OK";
      } else {
        msg += "ERR";
      }
      tcpConnection.sendMsg(msg);
  }

  private void changeGroupType(TCPConnection tcpConnection, String[] inf) {
       if(! groups.changeGroupType(groups.getGroupId(inf[0]), inf[1])) {
         tcpConnection.sendMsg(commands.returnCommand(commands.CHANGE_GROUP_TYPE) + "@err");
       }

  }

  private void disconnectGroup(TCPConnection tcpConnection, String gname) {
    String[] inf = new String[2];
    inf[0] = gname;
    inf[1] = tcpConnection.getUser().getNickname() + " left group";
    sendToGroup(tcpConnection,inf);
    groups.disconnectFromGroup(groups.getGroupId(gname),tcpConnection.getUser());
    tcpConnection.sendMsg(commands.DISCONNECT_GROUP + "@!");
  }

  private void getGroupMembers(TCPConnection tcpConnection, String gname) {
    ArrayList<String> players = groups.getGroupMembers(groups.getGroupId(gname));
    players.forEach((String nickname) -> {
      tcpConnection.sendMsg(commands.returnCommand(commands.Get_GROUP_MEMBERS) + "@" + nickname);
    });
  }

  private boolean checkadmin(int user_id, int admin_id) {
    return user_id == admin_id;
  }

  private void getGroupInf(TCPConnection tcpConnection, String gname) {
    String[] inf = groups.getGroupInf(groups.getGroupId(gname));
    String msg = "0";
    if(checkadmin(tcpConnection.getUser().getId(),Integer.parseInt(inf[3]))) {
      msg = "1";
    }
    msg += "%" + inf[0] + "%" + inf[1] + "%" + inf[2];
    tcpConnection.sendMsg(commands.returnCommand(commands.Get_GROUP_INF) + "@" +msg);
  }

  private void addToGroup(String[] inf){ //(adm_mod@)groupname@username

    groups.addToGroup(Math.abs(inf[0].hashCode()%10000),Integer.parseInt(inf[1]));
  }

  private void connectToGroup(TCPConnection tcpConnection, String[] inf){ //id@!
    int gid = groups.getGroupId(inf[0]);
    System.out.println("WANTS TO CONNECT TO GROUP" + inf[0]);
    if (groups.connectToGroup(gid,tcpConnection.getUser())){
      tcpConnection.sendMsg(commands.returnCommand(commands.CONNECT_GROUP) + "@You connect to group");
      inf[1] = " connect to this group";
      sendToGroup(tcpConnection,inf);
    }
    else {
      tcpConnection.sendMsg(commands.returnCommand(commands.ERROR) + "@Connection to group failed");
    }

  }

  private void sendToGroup(TCPConnection tcpConnection, String[] inf){//gname@msg
    System.out.println("WANTS TO SEND TO GROUP " + inf[0]);
    int gid = groups.getGroupId(inf[0]);
    ArrayList<UserInf> gr = groups.getGroup(gid);
    String msg = tcpConnection.getUser().getNickname() + ": " + inf[1];
    if (gr.isEmpty()) {
      System.out.println("GR IS EMPTY");
      return;
    }

    for (UserInf userInf : gr) {
      //
        userInf.getTcpConnection().sendMsg(commands.returnCommand(commands.SEND_GROUP) + "@" + msg);
    }
  }

  private void newGroup(TCPConnection tcpConnection, String inf[]) { //name@id_adm
    System.out.println("NEW GROUP " + inf[0]);
   //TODO check this:
    if (groups.addGroup(groups.getGroupId(inf[0]), tcpConnection.getUser(), inf[0])) {
      tcpConnection.sendMsg(commands.returnCommand(commands.GROUP_NEW) + "@NewGroupCreated");
    } else {
      tcpConnection.sendMsg(commands.returnCommand(commands.ERROR) + "@smth wrong with group");
    }
  }

  private void newUser(TCPConnection tcpConnection, String inf[]) {
    System.out.println("NEW USER" + inf[0]);
    UserInf curUser = tcpConnection.getUser();
    if(! DataBase.openConnection()){
      tcpConnection.sendMsg(commands.returnCommand(commands.ERROR) + "@Try again later");
      tcpConnection.disconnect();
      return;
    };
    DataBase.closeConnection();

    curUser.setNickname(inf[0]);
    curUser.setPasswd(inf[1]);
    if(!DataBase.addUser(curUser)){
      tcpConnection.sendMsg(commands.returnCommand(commands.ERROR) + "@TRY ANOTHER LOGIN AND/OR PASSWORD!");
      tcpConnection.disconnect();
      return;
    };
    sendToAllClients("New user Connected: " + inf[0]);

  }
  private void rollME(TCPConnection tcpConnection, String formula){
      //TODO добавить броски с помехой/преимуществом а также отсылку в группу

    String[] parts = formula.split("@");
    System.out.println(parts[0]);
    System.out.println(parts[1]);
    System.out.println(parts[2]);
    System.out.println(parts[3]);
    System.out.println(parts[4]);
    int n = Integer.parseInt(parts[0]); //parts[0];
    int d = Integer.parseInt(parts[1]);
    int modif = Integer.parseInt(parts[2]);
    int mode = Integer.parseInt(parts[3]);
    int total = 0;
    boolean forAll = parts[4].equals("a");
    String [] gmes = new String[2];
    if(!forAll) {
      gmes[0] = parts[4];
    }
    String msg = tcpConnection.getUser().getNickname() + " roll " + n +"d" + d + " ";
    Random randomizer = new Random();
    if (mode == 0) {
      for (int i = 0; i < n; i++) {
        int rvalue = randomizer.nextInt(d) + 1;
        total+= rvalue;
        msg += rvalue + " and ";
      }
      total += modif;
      msg += "(mod)" + parts[2];
      msg += " = " + total;
    if(forAll) {
      sendToAllClients(msg);
    } else {
      gmes[1] = msg;
      sendToGroup(tcpConnection, gmes);
    }
    } else {
      switch (mode) {
        case 1: msg += "with advantage";
          break;
        case  -1: msg += "with hindrance";
          break;
      }

      if(forAll) {
        sendToAllClients(msg);
      } else {
        gmes[1] = msg;
        sendToGroup(tcpConnection, gmes);
      }
      int[] finalTotal = new int[2];
      for (int j = 0; j < 2; j++) {
        total =0;
        msg = "roll " + j + ": ";
        for (int i = 0; i < n; i++) {
          int rvalue = randomizer.nextInt(d) + 1;
          total+= rvalue;
          msg += rvalue + " and ";
        }
        total += modif;
        msg += "(mod)" + parts[2];
        msg += " = " + total;
        finalTotal[j] = total;
        if(forAll) {
          sendToAllClients(msg);
        } else {
          gmes[1] = msg;
          sendToGroup(tcpConnection, gmes);
        }
      }

        if((finalTotal[0] > finalTotal[1]) ^ (mode == 1)) {total = finalTotal[1];}
        else { total = finalTotal[0]; }

      if(forAll) {
        sendToAllClients("Final score: " + total);
      } else {
        gmes[1] = "Final score: " + total;
        sendToGroup(tcpConnection, gmes);
      }


    }




  }
  private void userLogin(TCPConnection tcpConnection, String inf[]) {
    System.out.println("userLOG IN");
    UserInf curUser = tcpConnection.getUser();
   if(! DataBase.openConnection()){
     tcpConnection.sendMsg(commands.returnCommand(commands.ERROR) + "@Try again later");
     tcpConnection.disconnect();
     return;
   };
   DataBase.closeConnection();
   // String[] inf = msg.split("@");
    curUser.setNickname(inf[0]);
    curUser.setPasswd(inf[1]);
    if(!DataBase.checkUserInf(curUser)){
      tcpConnection.sendMsg(commands.returnCommand(commands.ERROR) + "@WRONG LOGIN AND/OR PASSWORD!");
      tcpConnection.disconnect();
      return;
    };
    sendToAllClients("User Connected: " + inf[0]);
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
      connections.get(i).sendMsg(commands.returnCommand(commands.SEND_ALL) + "@" + msg);
    }
  }
}
