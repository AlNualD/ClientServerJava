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
        System.out.println("Ret GR "+id);
        return groups.get(id);
    }

    @Override
    public boolean addToGroup(int id_group, UserInf user) {
        System.out.println("ADD TO GROUP " + id_group);
        if(idCheck(id_group)) {
            groups.get(id_group).add(user);
            DataBase.addTOGroup(id_group,user.getId());
            return true;
        }
        return false;
    }

    @Override
    public boolean addToGroup(int id_group, int id_person) {
        if(isPinG(id_group,id_person)) {
            return false;
        }

        return DataBase.addTOGroup(id_group,id_person);
    }

    @Override
    public boolean addGroup(int id, UserInf admin, String name) {
        System.out.println("ADd GROUP " + id + " " + name);
        if(!idCheck(id))
        {
            ArrayList<UserInf> arr = new ArrayList<>();
            arr.add(admin);
            groups.put(id, arr);
            DataBase.addGroup(id,admin.getId(),name);
        }
        return false;
    }

    @Override
    public boolean removeFromGroup(int id, UserInf user) {
        return false;
    }


    @Override
    public boolean connectToGroup(int id, UserInf user) {
        System.out.println("CONNECT TO GROUP " + id);

        if(isPinG(id, user.getId()))
        {
            if(!idCheck(id)) {
                groups.put(id,new ArrayList<>());
            }
            groups.get(id).add(user);
            return true;
        } else {
            if(DataBase.isGroupOpen(id)) {
                if(!idCheck(id)) {
                    groups.put(id,new ArrayList<>());
                }
                groups.get(id).add(user);
                DataBase.addTOGroup(id, user.getId());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean disconnectFromGroup(int id, UserInf user) {
        System.out.println("disCONNECT TO GROUP " + id);
        if(idCheck(id)){
            groups.get(id).remove(user);
            if(groups.get(id).isEmpty()) {
                groups.remove(id);
            }
        }
        return false;
    }

    @Override
    public String[] getGroupInf(int gid) {
        return DataBase.getGroupInf(gid);
    }

    @Override
    public ArrayList<String> getGroupMembers(int gid) {
        return DataBase.getGroupMembers(gid);
    }

    @Override
    public boolean changeGroupType(int gid, String isopen) {
        return DataBase.changeGroupType(gid,isopen);
    }

    @Override
    public boolean changeAdmin(int id, int newAdmin) {
        return DataBase.changeAdmin(id,newAdmin);
    }

    private boolean idCheck(int id){
        System.out.println("ID CHECK");
        return groups.containsKey(id);
    }

    private boolean isPinG(int id_group, int user){
        System.out.println("is PERSON IN GROUP ");
        boolean f = DataBase.checkGroup(id_group, user);
        System.out.println(f);
        return f;
    }

    public int getGroupId(String gname) {
        return Math.abs(gname.hashCode()%100000);
    }

}
