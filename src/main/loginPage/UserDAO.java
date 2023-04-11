package main.loginPage;

import main.models.User;
import main.DatabaseConnection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    //*******************************
    //SELECT a User
    //*******************************
    public static User searchUser (String userId) throws SQLException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM user WHERE User_ID=" + userId;
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsUser = DatabaseConnection.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getUserFromResultSet method and get user object
            User user = getUserFromResultSet(rsUser);
            //Return user object
            return user;
        } catch (SQLException e) {
            System.out.println("While searching a user with " + userId + " id, an error occurred: " + e);
            //Return exception
            throw e;
        }
    }

    //*******************************
    //SELECT a User by managerId or adminId
    //*******************************
    public static User searchUser (String status, int id) throws SQLException {
        //Declare a SELECT statement
        String selectStmt = "";
        switch (status) {
            case "Manager": selectStmt = "SELECT * FROM user WHERE Manager_ID=" + id;
            break;
            case "Admin": selectStmt = "SELECT * FROM user WHERE Admin_ID=" + id;
            break;
        }
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsUser = DatabaseConnection.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getUserFromResultSet method and get user object
            User user = getUserFromResultSet(rsUser);
            //Return user object
            return user;
        } catch (SQLException e) {
            System.out.println("While searching a user with " + id + " id, an error occurred: " + e);
            //Return exception
            throw e;
        }
    }

    //Use ResultSet from DB as parameter and set User Object's attributes and return user object.
    private static User getUserFromResultSet(ResultSet rs) throws SQLException
    {
        User user = null;
        if (rs.next()) {
            user = new User();
            user.setUserID(rs.getInt("User_ID"));
            user.setAdminID(rs.getInt("Admin_ID"));
            user.setManagerID(rs.getInt("Manager_ID"));
            user.setUserName(rs.getString("User_Name"));
            user.setPassword(rs.getString("Password"));
            user.setRole(rs.getString("Role"));
        }
        return user;
    }

    //*******************************
    // Verify if username exist in database
    //*******************************
    public static boolean isUniqueUsername (String username) {
        boolean isUnique = false;
        // Verify login status
        String verifyUsername = "SELECT * FROM user WHERE User_Name = '" + username +"'";
        //Execute statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet queryResult = DatabaseConnection.dbExecuteQuery(verifyUsername);

            if (queryResult.next()) {
                isUnique = false;
            } else {
                isUnique = true;
            }
        } catch (SQLException e) {
            System.out.println("While searching a user with " + username + ", an error occurred: " + e);
        }
        return isUnique;
    }

    //*************************************
    //ADD a User
    //*************************************
    public static void addUser (String role, int id, String username, String password) throws SQLException {
        //Declare a INSERT statement
        String updateStmt = "";
        switch (role) {
            case "Manager":
                updateStmt = "INSERT INTO user(Manager_ID, User_Name, Password, Role) " +
                        "VALUES('"+ id +"','"+ username +"','"+ password +"','"+ role +"')";
                break;
            case "Admin":
                updateStmt = "INSERT INTO user(Admin_ID, User_Name, Password, Role) " +
                        "VALUES('"+ id +"','"+ username +"','"+ password +"','"+ role +"')";
                break;
        }

        //Execute INSERT operation
        try {
            DatabaseConnection.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while INSERT Operation: " + e);
            throw e;
        }
    }

    //*************************************
    //UPDATE username and password
    //*************************************
    public static void updateUser (int userId, String username, String password) throws SQLException {
        //Declare a UPDATE statement
        String updateStmt = "UPDATE user SET "
                + "User_Name = '"+ username +"',"
                + "Password = '"+ password  +"' WHERE User_ID = '"+ userId +"'";

        //Execute UPDATE operation
        try {
            DatabaseConnection.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    public static void updateUser (String status, int id, String username, String password) throws SQLException {
        //Declare a UPDATE statement
        String updateStmt = "";
        switch (status) {
            case "Manager":
                updateStmt = "UPDATE user SET "
                    + "User_Name = '"+ username +"',"
                    + "Password = '"+ password  +"' WHERE Manager_ID = "+ id;
                break;
            case "Admin":
                updateStmt = "UPDATE user SET "
                    + "User_Name = '"+ username +"',"
                    + "Password = '"+ password  +"' WHERE Admin_ID = "+ id;
                break;
        }

        //Execute UPDATE operation
        try {
            DatabaseConnection.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }
}
