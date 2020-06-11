package chat.server;

import user.pckg.UserInf;

import java.util.ArrayList;

public interface GroupsControllerInterface {
    public ArrayList<UserInf> getGroup(int id);
    public boolean addToGroup(int id_group, UserInf user);
    public boolean addToGroup(int id_group, int id_person);
    public boolean addGroup(int id, UserInf admin, String name);
    public  boolean removeFromGroup(int id, UserInf user);
    public int getGroupAdmin(int id);
    public String getGroupName(int id);
    public boolean connectToGroup(int id, UserInf user);
    public boolean disconnectFromGroup(int id, UserInf user);
    public String[] getGroupInf(int gid);
    public ArrayList<String> getGroupMembers(int gid);
    public boolean changeGroupType (int gid, String isopen);
    public boolean changeAdmin(int id, int newAdmin);

}
