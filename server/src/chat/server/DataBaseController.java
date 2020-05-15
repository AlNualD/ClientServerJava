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
            //e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean openConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            sqlConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ChatUsers","postgres", "43fyfyfc");
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
        try {
            sqlStatement = sqlConnection.createStatement();
            String command = "SELECT * FROM users WHERE nickname = \'" + user.getNickname() + "\';";
            ResultSet resultSet = sqlStatement.executeQuery(command);
            //resultSet.first();

           if(!resultSet.next()) return false;
            if (resultSet.getString("passwd").equals(user.getPasswd()) )
            {
                resultSet.close();
                sqlStatement.close();
                sqlConnection.commit();
                return true;
            }
            resultSet.close();
            sqlStatement.close();
            sqlConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    @Override
    public boolean addUser(UserInf user) {
        if(checkUserInf(user)) {
            return false;
        }
        String command = "INSERT INTO users VALUES (";
        int id_code = ((user.getNickname().hashCode()) % 10000);
        command = command + id_code  + ", \'" + user.getNickname() + "\', \'" + user.getPasswd() + "\');";
        return executeUpdateCommand(command);
    }

    @Override
    public boolean addGroup(int id, int admin, String name) {
        if(checkGroup(id))  return false;
        String command = "INSERT INTO groupsusers VALUES (";
        //int id_code = ((user.getNickname().hashCode()) % 10000);
        command = command + id  + ", \'" + name + "\', " + admin + ", \'{"+ admin +"}\');";
        return executeUpdateCommand(command);
    }

    @Override
    public boolean checkGroup(int id) {
        try {
            sqlStatement = sqlConnection.createStatement();
            String command = "SELECT * FROM groupsusers WHERE id_code = " + id + ";";
            ResultSet resultSet = sqlStatement.executeQuery(command);
            //resultSet.first();
            //if(!resultSet.next()) return false;
            if(!resultSet.next()) {
                resultSet.close();
                sqlStatement.close();
               sqlConnection.commit();
                return false;
            }

            resultSet.close();
            sqlStatement.close();
            sqlConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean checkGroup(int id_group, int id_user) {
        //SELECT * FROM groupsusers
        //WHERE id_code = 1234 --group
        //AND 2524 = ANY (players);
        try {
            sqlStatement = sqlConnection.createStatement();
            String command = "SELECT * FROM groupsusers WHERE id_code = " + id_group + " AND " + id_user + " = ANY (players);";
            ResultSet resultSet = sqlStatement.executeQuery(command);
            //resultSet.first();

            if(!resultSet.next()) {
                resultSet.close();
                sqlStatement.close();
                sqlConnection.commit();
                return false;
            }

            resultSet.close();
            sqlStatement.close();
            sqlConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean addTOGroup(int id, int newUser) {
        //UPDATE groupsusers SET players = (Select players FROM groupsusers where id_code = 1234)||2524 Where id_code = 1234;
        String command = "UPDATE groupsusers SET players = (Select players FROM groupsusers where id_code = ";
        command = command + id + ")||" + newUser + " Where id_code =" + id + ";";
        return executeUpdateCommand(command);
    }

    @Override
    public boolean updateGroup(int id, ArrayList<UserInf> users) {
        String command = "UPDATE groupsusers SET players = \'{";
    for (UserInf user : users) {
        command  = command + user.getId() + ", ";
    }
    command = command.substring(0,command.length() - 2);
    command += "}\' Where id_code = " + id + ";";
     return executeUpdateCommand(command);
    }

    @Override
    public boolean changeAdmin(int id, int newAdmin) {
        String command = "UPDATE groupsusers SET admin_code = ";
        command = command + newAdmin + " Where id_code =" + id + ";";
        return executeUpdateCommand(command);
    }

    @Override
    public boolean changeUserInf(UserInf user) {
        //TODO подумать над жтим
        return false;
    }

    private boolean executeUpdateCommand (String command){
        try {
            sqlStatement = sqlConnection.createStatement();
            sqlStatement.executeUpdate(command);
            sqlStatement.close();
            sqlConnection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
