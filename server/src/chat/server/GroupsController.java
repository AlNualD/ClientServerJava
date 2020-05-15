package chat.server;

import user.pckg.UserInf;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class GroupsController implements GroupsControllerInterface {
    private final HashMap<Integer, ArrayList<UserInf>> groups = new HashMap<>();
    DataBaseController DataBase;
    public  GroupsController(DataBaseController DataBase) {
        this.DataBase = DataBase;
    }
    @Override
    public ArrayList<UserInf> getGroup(int id) {
        return groups.get(id);
    }

    @Override
    public boolean addToGroup(int id_group, UserInf user) {
        System.out.println("ADD TO GROUP " + id_group);
        if(idCheck(id_group)) {
            groups.get(id_group).add(user);
            DataBase.openConnection();
            DataBase.addTOGroup(id_group,user.getId());
            DataBase.closeConnection();
            return true;
        }
        return false;
    }

    @Override
    public boolean addGroup(int id, UserInf admin, String name) {
        System.out.println("ADd GROUP" + id + " " + name);
        if(!idCheck(id))
        {
            ArrayList<UserInf> arr = new ArrayList<>();
            arr.add(admin);
            groups.put(id, arr);
            DataBase.openConnection();
            DataBase.addGroup(id,admin.getId(),name);
            DataBase.closeConnection();
        }
        return false;
    }

    @Override
    public int getGroupAdmin(int id) {
        return groups.get(id).get(0).getId();
        //TODO make this
    }

    @Override
    public String getGroupName(int id) {
        return null;
        // TODO make this
    }

    @Override
    public boolean connectToGroup(int id, UserInf user) {
        System.out.println("CONNECT TO GROUP " + id);
        if(amPinG(id, user.getId()))
        {
            if(!idCheck(id)) {
                groups.put(id,new ArrayList<>());
            }
            groups.get(id).add(user);
        }
        return false;
    }

    @Override
    public boolean disconnectFromGroup(int id, UserInf user) {
        System.out.println("disCONNECT TO GROUP " + id);
        if(idCheck(id)){
            groups.get(id).remove(user);
        }
        return false;
    }

    private boolean idCheck(int id){
        System.out.println("ID CHECK");
        return groups.containsKey(id);
    }

    private boolean amPinG(int id_group, int user){
        System.out.println("AM PERSON IN GROUP ");
        DataBase.openConnection();
        boolean f = DataBase.checkGroup(id_group, user);
        DataBase.closeConnection();
        return f;
    }



}
