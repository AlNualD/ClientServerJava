package chat.server;

import user.pckg.UserInf;

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

    private boolean idCheck(int id){
        return groups.containsKey(id);
    }


}
