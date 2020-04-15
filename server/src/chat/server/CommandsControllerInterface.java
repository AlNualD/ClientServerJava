package chat.server;

public interface CommandsControllerInterface {
    public commands parseMSG(String command);
    public String cutCommand(String msg, commands command);
    //public void control(commands command);
}
