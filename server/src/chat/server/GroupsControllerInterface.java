package chat.server;

import user.pckg.UserInf;

import java.util.ArrayList;

public interface GroupsControllerInterface {
    public ArrayList<UserInf> getGroup(int id);
    public boolean addToGroup(int id_group, UserInf user);
    public boolean addGroup(int id, UserInf admin, String name);
   //TODO public  boolean removeFromGroup(int id, UserInf user);
    public int getGroupAdmin(int id);
    public String getGroupName(int id);
    public boolean connectToGroup(int id, UserInf user);
    public boolean disconnectFromGroup(int id, UserInf user);

}
