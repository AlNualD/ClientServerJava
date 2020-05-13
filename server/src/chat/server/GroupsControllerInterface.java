package chat.server;

import user.pckg.UserInf;

import java.util.ArrayList;

public interface GroupsControllerInterface {
    public ArrayList<UserInf> getGroup(int id);
    public boolean addToGroup(int id_group, UserInf user);
    public boolean addGroup(int id, UserInf admin, String name);
    public int getGroupAdmin(int id);
    public String getGroupName(int id);

}
