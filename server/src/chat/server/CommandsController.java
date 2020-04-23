package chat.server;

import java.util.regex.Pattern;

public class CommandsController implements CommandsControllerInterface {

  String SEND_ALL = "^##SEND_ALL##.*";
  String LOGIN = "^##LOGIN##.*";

  @Override
  public commands parseMSG(String msg) {
    if (msg.matches(commands.returnRegex(commands.SEND_ALL))) {
      return commands.SEND_ALL;
    }
    if (msg.matches(commands.returnRegex(commands.LOGIN))) {
      return commands.LOGIN;
    }
    return commands.ERROR;
  }

  @Override
  public String cutCommand(String msg, commands command) {
    return msg.substring(commands.returnRegex(command).length() - 3);
  }

  //    @Override
  //    public void control(commands command) {
  //
  //    }
}
