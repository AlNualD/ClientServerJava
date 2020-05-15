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
    public boolean updateGroup(int id, ArrayList<UserInf> users);
    public boolean changeAdmin(int id, int newAdmin);
    public boolean changeUserInf(UserInf user);
   // public boolean removeUser(UserInf user);
}
