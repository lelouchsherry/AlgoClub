package main.profilePage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.DatabaseConnection;
import main.models.Admin;
import main.models.Manager;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileDAO {
    //*******************************
    //SELECT a Manager
    //*******************************
    public static Manager searchManager(String mgrId) {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM manager WHERE Manager_ID =" + mgrId;
        Manager mgr = null;
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsMgr = DatabaseConnection.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getManagerFromResultSet method and get manager object
            mgr = getManagerFromResultSet(rsMgr);
        } catch (SQLException e) {
            System.out.println("While searching an manager with " + mgrId + " id, an error occurred: " + e);
        }
        //Return manager object
        return mgr;
    }

    //Use ResultSet from DB as parameter and set Manager Object's attributes and return manager object.
    private static Manager getManagerFromResultSet(ResultSet rs) throws SQLException {
        Manager mgr = null;
        if (rs.next()) {
            mgr = new Manager();
            mgr.setManagerID(rs.getInt("Manager_ID"));
            mgr.setManagerName(rs.getString("Manager_Name"));
            mgr.setPronouns(rs.getString("Pronouns"));
            mgr.setManagerAddress(rs.getString("Manager_Address"));
            mgr.setManagerPhone(rs.getString("Manager_Phone"));
            mgr.setManagerEmail(rs.getString("Manager_Email"));
        }
        return mgr;
    }

    //*******************************
    //SELECT Managers
    //*******************************
    public static ObservableList<Manager> displayManagers() throws SQLException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM manager";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsMgr = DatabaseConnection.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getEmployeeList method and get employee object
            ObservableList<Manager> mgrList = getMangerList(rsMgr);
            //Return manager list
            return mgrList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            throw e;
        }
    }

    // populate manager lists based on query
    private static ObservableList<Manager> getMangerList(ResultSet rs) throws SQLException {
        //Declare an observable List which comprises Manager objects
        ObservableList<Manager> mgrList = FXCollections.observableArrayList();
        while (rs.next()) {
            Manager mgr = new Manager();
            mgr.setManagerID(rs.getInt("Manager_ID"));
            mgr.setManagerName(rs.getString("Manager_Name"));
            mgr.setPronouns(rs.getString("Pronouns"));
            mgr.setManagerAddress(rs.getString("Manager_Address"));
            mgr.setManagerPhone(rs.getString("Manager_Phone"));
            mgr.setManagerEmail(rs.getString("Manager_Email"));
            //Add manager to the ObservableList
            mgrList.add(mgr);
        }
        //return mgrList (ObservableList of Managers)
        return mgrList;
    }

    //*******************************
    //SELECT an Admin
    //*******************************
    public static Admin searchAdmin(String adminId) {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM admin WHERE Admin_ID =" + adminId;
        Admin admin = null;
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsAdmin = DatabaseConnection.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getAdminFromResultSet method and get admin object
            admin = getAdminFromResultSet(rsAdmin);
        } catch (SQLException e) {
            System.out.println("While searching an manager with " + adminId + " id, an error occurred: " + e);
        }
        //Return manager object
        return admin;
    }

    //Use ResultSet from DB as parameter and set admin Object's attributes and return admin object.
    private static Admin getAdminFromResultSet(ResultSet rs) throws SQLException {
        Admin admin = null;
        if (rs.next()) {
            admin = new Admin();
            admin.setAdminID(rs.getInt("Admin_ID"));
            admin.setAdminName(rs.getString("Admin_Name"));
            admin.setPronouns(rs.getString("Pronouns"));
            admin.setAdminAddress(rs.getString("Admin_Address"));
            admin.setAdminPhone(rs.getString("Admin_Phone"));
            admin.setAdminEmail(rs.getString("Admin_Email"));
        }
        return admin;
    }

    //*******************************
    //SELECT Admins
    //*******************************
    public static ObservableList<Admin> displayAdmins() throws SQLException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM admin";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rs = DatabaseConnection.dbExecuteQuery(selectStmt);
            //Send ResultSet to the method and get admin object
            ObservableList<Admin> adminList = getAdminList(rs);
            //Return adminList
            return adminList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            throw e;
        }
    }

    // populate admin lists based on query
    private static ObservableList<Admin> getAdminList(ResultSet rs) throws SQLException {
        //Declare an observable List which comprises Manager objects
        ObservableList<Admin> adminLists = FXCollections.observableArrayList();
        while (rs.next()) {
            Admin ad = new Admin();
            ad.setAdminID(rs.getInt("Admin_ID"));
            ad.setAdminName(rs.getString("Admin_Name"));
            ad.setPronouns(rs.getString("Pronouns"));
            ad.setAdminAddress(rs.getString("Admin_Address"));
            ad.setAdminPhone(rs.getString("Admin_Phone"));
            ad.setAdminEmail(rs.getString("Admin_Email"));
            //Add admin to the ObservableList
            adminLists.add(ad);
        }
        //return adminLists (ObservableList of Managers)
        return adminLists;
    }

    //*************************************
    //UPDATE an Admin or Manger's personal info
    //*************************************
    public static void updatePersonalInfo(String role, int adminId, int mgrId, String name, String pronouns, String address, String phone, String email) throws SQLException {
        String updateStmt = "";

        //Based on User Role
        switch (role) {
            case "Admin":
                updateStmt = "UPDATE admin SET "
                        + "Admin_Name = '" + name + "',"
                        + "Pronouns = '" + pronouns + "',"
                        + "Admin_Address = '" + address + "',"
                        + "Admin_Phone = '" + phone + "',"
                        + "Admin_Email = '" + email + "' WHERE Admin_ID = '" + adminId + "'";
                break;
            case "Manager":
                updateStmt = "UPDATE manager SET "
                        + "Manager_Name = '" + name + "',"
                        + "Pronouns = '" + pronouns + "',"
                        + "Manager_Address = '" + address + "',"
                        + "Manager_Phone = '" + phone + "',"
                        + "Manager_Email = '" + email + "' WHERE Manager_ID = '" + mgrId + "'";
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

    //*************************************
    //DELETE a Manager or Admin
    //*************************************
    public static void deleteById(String status, int id) throws SQLException {
        String updateStmt = "";
        //Declare a DELETE statement based on status
        switch (status) {
            case "Admin":
                updateStmt = "DELETE FROM admin WHERE Admin_ID = " + id;
                break;
            case "Manager":
                updateStmt = "DELETE FROM manager WHERE Manager_ID = " + id;
                break;
        }

        //Execute DELETE operation
        try {
            DatabaseConnection.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }

    //*************************************
    //ADD a profile
    //*************************************
    public static void addUserProfile (String status, int id, String name, String pronoun, String addres, String phone, String email) throws SQLException {
        //Declare a INSERT statement
        String updateStmt = "";
        switch (status) {
            case "Manager":
                updateStmt = "INSERT INTO manager(Manager_ID, Manager_Name, Pronouns, Manager_Address, Manager_Phone, Manager_Email) " +
                        "VALUES('"+ id +"','"+ name +"','"+ pronoun +"','"+ addres + "','"+ phone + "','" + email +"')";
                break;
            case "Admin":
                updateStmt = "INSERT INTO admin(Admin_ID, Admin_Name, Pronouns, Admin_Address, Admin_Phone, Admin_Email) " +
                        "VALUES('"+ id +"','"+ name +"','"+ pronoun +"','"+ addres + "','"+ phone + "','" + email +"')";
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

}
