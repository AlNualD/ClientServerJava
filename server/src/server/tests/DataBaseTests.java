package server.tests;

import chat.server.GroupsController;
import junit.framework.TestCase;
import chat.server.DataBaseController;
import user.pckg.UserInf;

public class DataBaseTests extends TestCase{
    public void testConnection(){
        DataBaseController DataBase = new DataBaseController();
        assertTrue(DataBase.openConnection());
        assertTrue(DataBase.IsActive());
        DataBase.closeConnection();
    }
    public void testCheckUser(){
        DataBaseController DataBase = new DataBaseController();
        String name = "Felix";
        UserInf user = new UserInf(name.hashCode() % 100000, name,"1q2w3e4r5t");
        assertTrue(DataBase.checkUserInf(user));
        user.setPasswd("wrong");
        assertFalse(DataBase.checkUserInf(user));
    }
    public void testConnectToGroup() {
        DataBaseController DataBase = new DataBaseController();
        GroupsController groupsController = new GroupsController(DataBase);
        String  gname = "friends";
        String name = "Felix";
        UserInf user = new UserInf(name.hashCode()%100000, name,"1q2w3e4r5t");
        assertTrue(groupsController.connectToGroup(groupsController.getGroupId(gname),user));

    }

}
