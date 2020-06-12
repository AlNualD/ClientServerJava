package chat.server;

import common.commands;

public interface CommandsControllerInterface {
  public commands parseMSG(String command);

  public String cutCommand(String msg, commands command);

}
