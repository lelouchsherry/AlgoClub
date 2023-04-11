package main.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// User Model
public class User {
    // class member variables
    private IntegerProperty userID;
    private IntegerProperty adminID;
    private IntegerProperty managerID;
    private StringProperty userName;
    private StringProperty password;
    private StringProperty role;
    private boolean loginStatus;

    // default constructor
    public User() {
        this.userID = new SimpleIntegerProperty();
        this.adminID = new SimpleIntegerProperty();
        this.managerID = new SimpleIntegerProperty();
        this.userName = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.role = new SimpleStringProperty();
        this.loginStatus = false;
    }

    // getter setter for variables
    public int getUserID() {
        return userID.get();
    }

    public IntegerProperty userIDProperty() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID.set(userID);
    }

    public int getAdminID() {
        return adminID.get();
    }

    public IntegerProperty adminIDProperty() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID.set(adminID);
    }

    public int getManagerID() {
        return managerID.get();
    }

    public IntegerProperty managerIDProperty() {
        return managerID;
    }

    public void setManagerID(int managerID) {
        this.managerID.set(managerID);
    }

    public String getUserName() {
        return userName.get();
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getRole() {
        return role.get();
    }

    public StringProperty roleProperty() {
        return role;
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public boolean isLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }
}
