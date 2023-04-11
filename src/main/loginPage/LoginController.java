package main.loginPage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.DatabaseConnection;
import main.models.User;
import main.Utils;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Button loginButton;

    @FXML
    private Label loginMsgLabel;

    @FXML
    private TextField userNameTextField;

    @FXML
    private PasswordField passwordTextField;

    public User loggedInUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(userNameTextField.getText().isEmpty() == false &&
                        passwordTextField.getText().isEmpty() == false) {
                    validateLogin(event);
                } else {
                    loginMsgLabel.setText("Please enter username and password!");
                }
            }
        });
    }

    // Validate if entered username and password in exist in database
    private void validateLogin(ActionEvent event) {
        // Verify login status
        String verifyLogin = "SELECT count(1), User_ID FROM user" +
                " WHERE User_Name = '" + userNameTextField.getText() + "' " +
                "AND Password = '"+ passwordTextField.getText() + "';";
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet queryResult = DatabaseConnection.dbExecuteQuery(verifyLogin);

            while (queryResult.next()) {
                // there is user record in database
                if(queryResult.getInt(1) == 1) {
                    // create user based on data retrieved from database
                    User user = UserDAO.searchUser(queryResult.getString("User_ID"));
                    loggedInUser = user;
                    // display welcome message and change to events page
                    loginMsgLabel.setText("Welcome!");
                    Utils.changeScene(event, "resources/events.fxml", "Events", user);
                } else {
                    // can't log in, the entered username or password is not match
                    loginMsgLabel.setText("Invalid Login. Please try again!");
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}