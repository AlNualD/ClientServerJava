package server.tests;

import chat.server.CommandsController;
import common.commands;
import junit.framework.TestCase;

public class serverCommandsTests extends TestCase {
  public void testLoginCommand() {
    CommandsController commandsController = new CommandsController();
    assertEquals(commands.LOGIN, commandsController.parseMSG("##LOGIN##messageTEXT"));
  }

  public void testSendAllCommand() {
    CommandsController commandsController = new CommandsController();
    assertEquals(commands.SEND_ALL, commandsController.parseMSG("##SEND_ALL##messageTEXT"));
  }

  public void testWrongCommand() {
    CommandsController commandsController = new CommandsController();
    assertEquals(commands.ERROR, commandsController.parseMSG("##AFHG##messageTEXT"));
  }

  public void testSendSingleCommand() {
    CommandsController commandsController = new CommandsController();
    assertEquals(commands.SEND_SINGLE, commandsController.parseMSG("##SEND_SINGLE##messageTEXT"));
  }

  public void testCutCommand() {
    CommandsController commandsController = new CommandsController();
    assertEquals(
        "messageTEXT", commandsController.cutCommand("##LOGIN##messageTEXT", commands.LOGIN));
    assertEquals(
        "messageTEXT", commandsController.cutCommand("##SEND_ALL##messageTEXT", commands.SEND_ALL));
    assertEquals(
        "messageTEXT",
        commandsController.cutCommand("##SEND_SINGLE##messageTEXT", commands.SEND_SINGLE));
  }
}
