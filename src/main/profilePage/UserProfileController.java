package main.profilePage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import main.Utils;
import main.loginPage.UserDAO;
import main.models.Admin;
import main.models.Manager;
import main.models.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserProfileController implements Initializable {

    @FXML
    private Button menu1;

    @FXML
    private Button menu2;

    @FXML
    private Button menu3;

    @FXML
    private Button updateBtn;

    @FXML
    private Button editBtn;

    @FXML
    private Button newUserBtn;

    @FXML
    private TextField nameTF;

    @FXML
    private TextField pronounsTF;

    @FXML
    private TextField addressTF;

    @FXML
    private TextField phoneTF;

    @FXML
    private TextField emailTF;

    @FXML
    private TextField usernameTF;

    @FXML
    private TextField passwordTF;

    private User user;

    private Manager manager;
    private Admin admin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // invoke switch page functions
        jumpPage();

        // edit user info
        editBtn.setOnMouseClicked((MouseEvent event) -> editUser());

        // update personal info
        updateBtn.setOnMouseClicked((MouseEvent event) -> updatePersonalInfo());

        // create new user
        newUserBtn.setOnAction(event -> Utils.changeScene(event,"resources/profile_modify.fxml", "New Student", user));
    }

    private void loadData() {
        //Based on User Role
        switch(user.getRole()) {
            case "Admin": displayAdminInfo(); break;
            case "Manager": displayManagerInfo(); break;
        }
    }

    private void editUser() {
        // prepare to check and insert to database
        String username = usernameTF.getText();
        String password = passwordTF.getText();

        // simple validation on inputs not empty
        if (username.isEmpty() || password.isEmpty()) {
            // promote error message
            Alert alert = new Alert(Alert.AlertType.ERROR, "Username and password can't be empty !", ButtonType.OK);
            alert.showAndWait();
        } else if(!UserDAO.isUniqueUsername(username)) {
            // promote error message
            Alert alert = new Alert(Alert.AlertType.ERROR, "Username already exists !", ButtonType.OK);
            alert.showAndWait();
        } else {
            try {
                // update new username and password
                UserDAO.updateUser(user.getUserID(), username, password);
                // display message and update user
                Utils.showInfo("Updated User",  "You are successful updated username and password !");
                updateUser();
            } catch (SQLException e) {
                System.out.println("Problem occurred while inserting user " + e);
            }
        }
    }

    // update userinfo
    private void updateUser() throws SQLException {
        // retrieve new data updated in database
        user = UserDAO.searchUser(String.valueOf(user.getUserID()));
        // update form data
        setUserInfo();
    }

    private void updatePersonalInfo() {
        // prepare to check and insert to database
        String fullname = nameTF.getText();
        String pronouns = pronounsTF.getText();
        String address = addressTF.getText();
        String phone = phoneTF.getText();
        String email = emailTF.getText();

        // simple validation on inputs not empty
        if (fullname.isEmpty() || pronouns.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            // promote error message
            Alert alert = new Alert(Alert.AlertType.ERROR, "Input field can not be empty !", ButtonType.OK);
            alert.showAndWait();
        } else {
            //Based on User Role
            try {
                // update personal info
                if(admin != null)
                    ProfileDAO.updatePersonalInfo(user.getRole(), admin.getAdminID(), 0,
                            fullname, pronouns, address, phone, email);
                else
                    ProfileDAO.updatePersonalInfo(user.getRole(), 0, manager.getManagerID(),
                            fullname, pronouns, address, phone, email);
                // display message and update user
                Utils.showInfo("Updated Info",  "You are successful updated your info !");
                updateUser();
            } catch (SQLException e) {
                System.out.println("Problem occurred while inserting user " + e);
            }
        }
    }

    private void displayAdminInfo() {
        // Using adminId find the admin
        try {
            //Get Admin information
            admin = ProfileDAO.searchAdmin(String.valueOf(user.getAdminID()));
            setAdminInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void displayManagerInfo() {
        // Using mgrId find the manager
        try {
            //Get Manager information
            manager = ProfileDAO.searchManager(String.valueOf(user.getManagerID()));
            setManagerInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAdminInfo() {
        nameTF.setText(admin.getAdminName());
        pronounsTF.setText(admin.getPronouns());
        addressTF.setText(admin.getAdminAddress());
        phoneTF.setText(admin.getAdminPhone());
        emailTF.setText(admin.getAdminEmail());

        setUserInfo();
    }

    private void setManagerInfo() {
        nameTF.setText(manager.getManagerName());
        pronounsTF.setText(manager.getPronouns());
        addressTF.setText(manager.getManagerAddress());
        phoneTF.setText(manager.getManagerPhone());
        emailTF.setText(manager.getManagerEmail());
        setUserInfo();
    }

    private void setUserInfo() {
        usernameTF.setText(user.getUserName());
        passwordTF.setText(user.getPassword());
    }

    // switch scene
    private void jumpPage() {
        menu1.setOnAction(event -> Utils.changeScene(event, "resources/events.fxml", "Events", user));
        menu2.setOnAction(event -> Utils.changeScene(event, "resources/financial.fxml", "Financial", user));
        menu3.setOnAction(event -> Utils.changeScene(event, "resources/profile.fxml", "Profiles", user));
    }

    public void setUser(User user) {
        this.user = user;
        loadData();
    }

}
