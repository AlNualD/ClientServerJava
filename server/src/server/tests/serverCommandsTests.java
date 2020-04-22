package server.tests;

import chat.server.CommandsController;
import chat.server.commands;
import junit.framework.TestCase;

public class serverCommandsTests extends TestCase {
    public void testCommandController() {
        CommandsController commandsController = new CommandsController();
        assertEquals(commandsController.parseMSG("##LOGIN##sahdfsjkdfh"), commands.LOGIN);
        assertEquals(commandsController.parseMSG("##SEND_ALL##asdfdf"),commands.SEND_ALL);
    }
}
