package server.tests;

import chat.server.CommandsController;
import chat.server.commands;
import junit.framework.TestCase;

public class serverCommandsTests extends TestCase {
  public void testLoginCommand() {
    CommandsController commandsController = new CommandsController();
    assertEquals(commandsController.parseMSG("##LOGIN##sahdfsjkdfh"), commands.LOGIN);
  }

  public void testSendAllCommand() {
    CommandsController commandsController = new CommandsController();
    assertEquals(commandsController.parseMSG("##SEND_ALL##asdfdf"), commands.SEND_ALL);
  }

  public void testWrongCommand() {
    CommandsController commandsController = new CommandsController();
    assertEquals(commandsController.parseMSG("##AFHG##sadfsdf"), commands.ERROR);
  }
}
