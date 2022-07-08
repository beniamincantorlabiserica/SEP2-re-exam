package Database;

import logger.Logger;
import logger.LoggerType;
import model.User;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UsersDatabaseManager {
    Connection connection = null;
    Statement statement = null;

    public UsersDatabaseManager() {

    }

    public void createTableUser() {
        DBConnection dbConnection = new DBConnection();
        connection = dbConnection.getConnection();
        try {
            String query = "" +
                    "Create table users" +
                    "(" +
                    "    id       SERIAL primary key," +
                    "    password varchar(200)," +
                    "    role     varchar(200)" +
                    ");";
            statement = connection.createStatement();
            statement.executeUpdate(query);
            Logger.getInstance().log(LoggerType.DEBUG, "User table created");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createTableCheckouts() {
        DBConnection dbConnection = new DBConnection();
        connection = dbConnection.getConnection();
        try {
            String query = "Create table checkouts" +
                    "(" +
                    "    id         SERIAL primary key," +
                    "    itemId INTEGER REFERENCES item (id)," +
                    "    quantity   int," +
                    "    price      int" +
                    ");";
            statement = connection.createStatement();
            statement.executeUpdate(query);
            Logger.getInstance().log(LoggerType.DEBUG, "Checkouts table created");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void createTableItem() {
        DBConnection dbConnection = new DBConnection();
        connection = dbConnection.getConnection();
        try {
            String query = "Create table item" +
                    "(" +
                    "    id       SERIAL primary key," +
                    "    name     varchar(100)," +
                    "    quantity int," +
                    "    price    int," +
                    "    priceControl boolean"+
                    ");";
            statement = connection.createStatement();
            statement.executeUpdate(query);
            Logger.getInstance().log(LoggerType.DEBUG, "Item table created");
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User login(String password) {
        DBConnection dbConnection = new DBConnection();
        connection = dbConnection.getConnection();
        ResultSet rs;
        String role = null;
        try {
            String query = "Select * from users where password= '" + password + "' limit 1";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.next()) {
                role = rs.getString("role");
            }
            connection.close();
            if (role == null) {
                Logger.getInstance().log(LoggerType.WARNING, "Failed attempt to login with password " + password);
                return null;
            }
            Logger.getInstance().log("User logged in as " + role + ".");
            return new User(role);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updatePassword(String password, String role) throws RuntimeException {
        DBConnection dbConnection = new DBConnection();
        connection = dbConnection.getConnection();
        ResultSet rs;
        try {
            String query = "Select * from users";
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                String oldPassword = rs.getString("password");
                if(oldPassword.equals(password)) {
                    if (rs.getString("role").equals(role)) {
                        Logger.getInstance().log(LoggerType.ERROR, "The new password needs to be different");
                    } else {
                        Logger.getInstance().log(LoggerType.ERROR, "Passwords can not be the same for the store manager and the cashier");
                    }
                    connection.close();
                    throw new RuntimeException("PASSWORD_ALREADY_IN_USE");
                }
            }
            query = "update users set password='" + password
                            + "' where role='" + role + "'";
            statement = connection.createStatement();
            statement.executeUpdate(query);
            Logger.getInstance().log(LoggerType.WARNING, "Password changed for " + role + " to " + password);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
