package chat.server;

import user.pckg.UserInf;

import java.util.ArrayList;

public interface DataBaseInterface {
    public  boolean IsActive();
    public  boolean openConnection();
    public boolean closeConnection();
    public boolean checkUserInf(UserInf user);
    public boolean addUser(UserInf user);
    public boolean addGroup(int id, int admin, String name);
    public boolean checkGroup(int id);
    public boolean checkGroup(int id_group, int id_user);
    public  boolean addTOGroup(int id, int newUser);
    public boolean isGroupOpen (int gid);
    public boolean changeAdmin(int id, int newAdmin);
    public String[] getGroupInf(int gid);
    public ArrayList<String> getGroupMembers(int gid);
    public boolean changeGroupType (int gid, String isopen);

}
