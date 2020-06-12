package chat.server;

import common.commands;

public class CommandsController implements CommandsControllerInterface {

  String SEND_ALL = "^##SEND_ALL##.*";
  String LOGIN = "^##LOGIN##.*";

  @Override
  public synchronized commands parseMSG(String msg) {
    if (msg.matches(commands.returnRegex(commands.SEND_ALL))) {
      return commands.SEND_ALL;
    }
    if (msg.matches(commands.returnRegex(commands.SEND_GROUP))) {
      return  commands.SEND_GROUP;
    }
    if (msg.matches(commands.returnRegex(commands.LOGIN))) {
      return commands.LOGIN;
    }
    if(msg.matches(commands.returnRegex(commands.ROLL_ME))) {
      return commands.ROLL_ME;
    }
    if (msg.matches(commands.returnRegex(commands.SEND_SINGLE))) {
      return commands.SEND_SINGLE;
    }
    if (msg.matches(commands.returnRegex(commands.CONNECT_GROUP))) {
      return commands.CONNECT_GROUP;
    }
    if (msg.matches(commands.returnRegex(commands.DISCONNECT_GROUP))) {
      return commands.DISCONNECT_GROUP;
    }
    if(msg.matches(commands.returnRegex(commands.ADD_TO_GROUP))) {
      return commands.ADD_TO_GROUP;
    }
    if (msg.matches(commands.returnRegex(commands.EXIT))) {
      return commands.EXIT;
    }
    if(msg.matches(commands.returnRegex(commands.NEW_USER))){
      return  commands.NEW_USER;
    }
    if(msg.matches(commands.returnRegex(commands.GROUP_NEW))) {
      return  commands.GROUP_NEW;
    }
    if(msg.matches(commands.returnRegex(commands.Get_GROUP_INF))) {
      return commands.Get_GROUP_INF;
    }
    if(msg.matches(commands.returnRegex(commands.Get_GROUP_MEMBERS))) {
      return commands.Get_GROUP_MEMBERS;
    }
    if(msg.matches(commands.returnRegex(commands.CHANGE_GROUP_TYPE))) {
      return commands.CHANGE_GROUP_TYPE;
    }
    if(msg.matches(commands.returnRegex(commands.CHANGE_ADMIN))) {
      return commands.CHANGE_ADMIN;
    }
    return commands.ERROR;
  }

  @Override
  public synchronized String cutCommand(String msg, commands command) {
    System.out.println(msg + "  " + commands.returnRegex(command) + "  " + (commands.returnRegex(command).length() - 3) );
    return msg.substring(commands.returnCommand(command).length());
  }

}
