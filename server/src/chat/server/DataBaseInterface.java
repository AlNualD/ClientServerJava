package chat.server;

import user.pckg.UserInf;

public interface DataBaseInterface {
    public  boolean IsActive();
    public  boolean openConnection();
    public boolean closeConnection();
    public boolean checkUserInf(UserInf user);
    public boolean addUser(UserInf user);
    public boolean changeUserInf(UserInf user);
   // public boolean removeUser(UserInf user);
}
