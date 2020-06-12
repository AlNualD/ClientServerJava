package chat.server;

import user.pckg.UserInf;

import java.sql.*;
import java.util.ArrayList;

public class DataBaseController implements DataBaseInterface {
   Connection sqlConnection;
   Statement sqlStatement;
    public DataBaseController() {

   }

    @Override
    public boolean IsActive() {

        try {
            return sqlConnection.isValid(1);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean openConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            sqlConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ChatServerDB", "ChatServer", "12345");
            sqlConnection.setAutoCommit(false);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean closeConnection() {
        try {
            sqlConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean checkUserInf(UserInf user) {
        openConnection();
        boolean f = false;
        try {
            sqlStatement = sqlConnection.createStatement();
            String command = "SELECT * FROM check_player_inf(\'" + user.getId() + "\', \'" + user.getPasswd() + "\');";
            ResultSet resultSet = sqlStatement.executeQuery(command);
            if(resultSet.next())
            f =  resultSet.getBoolean("check_player_inf");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            closeConnection();
        }
        return f;


    }

    @Override
    public boolean addUser(UserInf user) {
        String command = "SELECT add_user(\'" + user.getId() + "\', \'" + user.getNickname() + "\', \'" + user.getPasswd() + "\');";
        return executeCommand(command);
    }

    @Override
    public boolean addGroup(int id, int admin, String name) {
        String command  = " SELECT add_group(\'" + id +"\', \'" + name + "\', \'" + admin + "\');";
        return  executeCommand(command);

}

    @Override
    public boolean checkGroup(int id) {

        openConnection();
        boolean f = false;
        try {
            sqlStatement = sqlConnection.createStatement();
            String command = "SELECT * FROM is_group_exists(\'" + id + "\');";
            ResultSet resultSet = sqlStatement.executeQuery(command);
            if(resultSet.next())
                f =  resultSet.getBoolean("is_group_exists");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            closeConnection();
        }
        return f;

    }

    @Override
    public boolean checkGroup(int id_group, int id_user) {

        openConnection();
        boolean f = false;
        try {
            sqlStatement = sqlConnection.createStatement();
            String command = "SELECT * FROM is_player_in_group(\'" + id_group + "\', \'" + id_user + "\');";
            ResultSet resultSet = sqlStatement.executeQuery(command);
            if(resultSet.next())
                f =  resultSet.getBoolean("is_player_in_group");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            closeConnection();
        }
        return f;

    }

    @Override
    public boolean addTOGroup(int id, int newUser) {
        openConnection();
        String command = " SELECT FROM add_to_group(\'" + id + "\', \'" + newUser + "\');";
        return executeCommand(command);
    }

    @Override
    public boolean isGroupOpen(int gid) {
        openConnection();
        boolean f = false;
        try {
            sqlStatement = sqlConnection.createStatement();
            String command = "SELECT * FROM is_group_open(\'" + gid + "\');";
            ResultSet resultSet = sqlStatement.executeQuery(command);
            if(resultSet.next())
                f =  resultSet.getBoolean("is_group_open");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            closeConnection();
        }
        return f;
    }

    @Override
    public boolean changeAdmin(int id, int newAdmin) {
        String command = "SELECT change_admin(\'" + id + "\', \'" + newAdmin + "\');";
        return executeCommand(command);
    }


    @Override
    public String[] getGroupInf(int gid) {
        String command = "SELECT * FROM get_group_inf(\'" + gid + "\');";
        String[] answ = new String[4];
        openConnection();
        try {
            sqlStatement = sqlConnection.createStatement();
            ResultSet resultSet = sqlStatement.executeQuery(command);
            if(resultSet.next()) {
                answ[0] = resultSet.getString("nickname");
                answ[1] = String.valueOf(resultSet.getInt("uamount"));
                if(resultSet.getBoolean("isopen")) {
                    answ[2] = "1";
                } else {
                    answ[2] = "0";
                }
                answ[3] = resultSet.getString("admin_code");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answ;
    }

    @Override
    public ArrayList<String> getGroupMembers(int gid) {
        String command = "SELECT * FROM get_group_members(\'" + gid + "\');";
        ArrayList<String> answ = new ArrayList<>();
        openConnection();
        try {
            sqlStatement = sqlConnection.createStatement();
            ResultSet resultSet = sqlStatement.executeQuery(command);
            while (resultSet.next()) {
                answ.add(resultSet.getString("nickname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answ;
    }

    @Override
    public boolean changeGroupType(int gid, String isopen) {
        String command = "SELECT change_group_type(\'" + gid + "\', " + isopen + ");";
        return executeCommand(command);
    }


    private boolean executeUpdateCommand (String command){
        openConnection();
        try {
            sqlStatement = sqlConnection.createStatement();
            sqlStatement.executeUpdate(command);
            sqlStatement.close();
            sqlConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally{
            closeConnection();
        }
        return true;
    }

    private boolean executeCommand (String command){
        openConnection();
        try {
            sqlStatement = sqlConnection.createStatement();
            sqlStatement.execute(command);
            sqlStatement.close();
            sqlConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally{
            closeConnection();
        }
        return true;
    }

}
