package server.tests;

import junit.framework.TestCase;
import chat.server.DataBaseController;
import user.pckg.UserInf;

public class DataBaseTests extends TestCase{
    public void testConnection(){
        DataBaseController DataBase = new DataBaseController();
        assertTrue(DataBase.openConnection());
        assertTrue(DataBase.IsActive());
    }
    public void testAddUser(){
        DataBaseController DataBase = new DataBaseController();
        DataBase.openConnection();
        String name = "Felix";
        UserInf user = new UserInf(name.hashCode()%10000,name,"passwd");
        assertFalse(DataBase.addUser(user));
        assertTrue(DataBase.checkUserInf(user));
        user.setPasswd("wrong");
        assertFalse(DataBase.checkUserInf(user));
    }
}
