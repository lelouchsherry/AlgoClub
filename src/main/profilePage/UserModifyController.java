package main.profilePage;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import main.Utils;
import main.loginPage.UserDAO;
import main.models.Admin;
import main.models.Manager;
import main.models.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserModifyController implements Initializable {

    @FXML
    private Button userBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private Button editBtn;

    @FXML
    private JFXButton backBtn;

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

    @FXML
    private HBox idBox;

    @FXML
    private TextField idTF;

    @FXML
    private HBox roleBox;

    @FXML
    private Button submitBtn;

    @FXML
    private MenuButton roleSelect;

    private User user;
    private Manager manager;
    private Admin admin;

    private String status = "";

    private final String STATUS_1 = "Manager";
    private final String STATUS_2 = "Admin";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // switch to user detail page
        userBtn.setOnAction(event -> Utils.changeScene(event, "resources/user_profile.fxml", "User Profile", user));
        backBtn.setOnAction(event -> Utils.changeScene(event, "resources/profile.fxml", "Profiles", user));

        updateBtn.setVisible(false);
        editBtn.setVisible(false);
    }

    public void setUser(User user) {
        this.user = user;
        if(user != null)
            userBtn.setText("Welcome, " + user.getUserName());
    }

    public void setManager(Manager aManager) {
        this.manager = aManager;
        setManagerInfo();
        initPage();
    }
    private void setManagerInfo() {
        nameTF.setText(manager.getManagerName());
        pronounsTF.setText(manager.getPronouns());
        addressTF.setText(manager.getManagerAddress());
        phoneTF.setText(manager.getManagerPhone());
        emailTF.setText(manager.getManagerEmail());
        getInfo(STATUS_1);
    }

    public void setAdmin(Admin aAdmin) {
        this.admin = aAdmin;
        setAdminInfo();

        initPage();
    }

    private void initPage() {
        // hide show elements
        idBox.setVisible(false);
        roleBox.setVisible(false);
        submitBtn.setVisible(false);
        updateBtn.setVisible(true);
        editBtn.setVisible(true);
    }

    private void setAdminInfo() {
        nameTF.setText(admin.getAdminName());
        pronounsTF.setText(admin.getPronouns());
        addressTF.setText(admin.getAdminAddress());
        phoneTF.setText(admin.getAdminPhone());
        emailTF.setText(admin.getAdminEmail());
        getInfo(STATUS_2);
    }

    private void getInfo(String status) {
        try {
            User userInfo = null;
            switch (status) {
                case STATUS_2: userInfo = UserDAO.searchUser(status, admin.getAdminID()); break;
                case STATUS_1: userInfo = UserDAO.searchUser(status, manager.getManagerID()); break;
            }
            // display user info
            setUserInfo(userInfo);
        } catch (SQLException e) {
            System.out.println("Problem occurred while getting user " + e);
        }
    }

    private void setUserInfo(User user) {
        usernameTF.setText(user.getUserName());
        passwordTF.setText(user.getPassword());
    }

    @FXML
    public void editUser(ActionEvent event) {
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
                System.out.println(admin);
                // update new username and password
                if(admin != null) {
                    UserDAO.updateUser(STATUS_2, admin.getAdminID(), username, password);
                    if(user.getAdminID() == admin.getAdminID()) {
                        // update user info if it's signedIn user
                        user = UserDAO.searchUser(String.valueOf(user.getUserID()));
                        userBtn.setText("Welcome, " + user.getUserName());
                    }
                }
                else
                    UserDAO.updateUser(STATUS_1, manager.getManagerID(), username, password);
                // display message and update user
                Utils.showInfo("Updated User",  "You are successful updated username and password !");
            } catch (SQLException e) {
                System.out.println("Problem occurred while inserting user " + e);
            }
        }
    }

    @FXML
    public void updateUser(ActionEvent event) {
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
                    ProfileDAO.updatePersonalInfo(STATUS_2, admin.getAdminID(), 0,
                            fullname, pronouns, address, phone, email);
                else
                    ProfileDAO.updatePersonalInfo(STATUS_1, 0, manager.getManagerID(),
                            fullname, pronouns, address, phone, email);
                // display message and update user
                Utils.showInfo("Updated Info",  "You are successful updated info !");
            } catch (SQLException e) {
                System.out.println("Problem occurred while inserting user " + e);
            }
        }
    }

    // Creating user based on input info
    public void selectedMgr(ActionEvent event) {
        roleSelect.setText(STATUS_1);
        status = STATUS_1;
    }

    public void selectedAdmin(ActionEvent event) {
        roleSelect.setText(STATUS_2);
        status =STATUS_2;
    }

    public void createUser(ActionEvent event) {
        //Get Strings to submit to database
        String id = idTF.getText();
        String name = nameTF.getText();
        String pronun = pronounsTF.getText();
        String address = addressTF.getText();
        String phone = phoneTF.getText();
        String email = emailTF.getText();
        String username = usernameTF.getText();
        String password = passwordTF.getText();
        boolean isPostive = checkIsPositive(id);
        boolean isValid = checkIsPositive(phone);

        // simple validation on inputs not empty
        if (id.isEmpty() || status == "" || name.isEmpty() || pronun.isEmpty() || address.isEmpty() || username.isEmpty()
                || password.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Input field can not be empty !", ButtonType.OK);
            alert.showAndWait();
        } else if(!UserDAO.isUniqueUsername(username)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Username already exists !", ButtonType.OK);
            alert.showAndWait();
        } else if(!isValid || !isPostive){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Only Positive number valid for id and phone", ButtonType.OK);
            alert.showAndWait();
        } else {
            try {
                // create manager
                ProfileDAO.addUserProfile(status, Integer.parseInt(id), name, pronun, address, phone, email);
                // then create user
                UserDAO.addUser(status, Integer.parseInt(id), username, password);
                //Display confirmation message
                Utils.showInfo("Added New Manager", "You've successfully added a new manager to the database!");
                resetForm();
            } catch (SQLException e) {
                System.out.println("Problem occurred while inserting student:  " + e);
            }
        }
    }

    // Using try catch to check if is positive number entered
    private boolean checkIsPositive(String id) {
        try {
            if(Double.parseDouble(id) > 0)
                return true;
            else
                return false;
        }
        catch(Exception e) {
            return false;
        }
    }


    // reset form to blank
    private void resetForm() {
        idTF.setText("");
        nameTF.setText("");
        pronounsTF.setText(null);
        addressTF.setText("");
        phoneTF.setText("");
        emailTF.setText("");
        roleSelect.setText("Role");
        status = "";
        usernameTF.setText("");
        passwordTF.setText("");
    }

}
