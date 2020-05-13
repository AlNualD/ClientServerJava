package chat.server;

import user.pckg.UserInf;

import java.sql.*;

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

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        catch (SQLException e) {
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

    @Override
    public boolean changeUserInf(UserInf user) {
        return false;
    }
}
