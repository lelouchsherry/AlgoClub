package main;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;
import main.eventpage.*;
import main.financialPage.FinancialController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.financialPage.ModifyTransactionController;
import main.financialPage.TransactionsController;
import main.models.Event;
import main.models.Transaction;
import main.models.User;
import main.otherPage.AttendeesController;
import main.otherPage.EmailController;
import main.profilePage.ModifyProfileController;
import main.profilePage.ProfileController;
import main.profilePage.UserModifyController;
import main.profilePage.UserProfileController;

import java.io.IOException;

public class Utils {
    // method to switch scene
    public static void changeScene(ActionEvent event, String fxmlFile, String title, User user) {
        Parent root = null;
        try{
            FXMLLoader loader = new FXMLLoader(Utils.class.getResource(fxmlFile));
            root = loader.load();
            // send signedIn user's information
            switch(title) {
                case "Events":
                    EventsController eventsControllerController = loader.getController();
                    eventsControllerController.setUser(user); break;
                case "New Event":
                    ModifyEventController addEventController = loader.getController();
                    addEventController.setUser(user); break;
                case "Financial":
                    FinancialController financialController = loader.getController();
                    financialController.setUser(user); break;
                case "Profiles":
                    ProfileController profileController = loader.getController();
                    profileController.setUser(user); break;
                case "New Student":
                    ModifyProfileController modifyProfileController = loader.getController();
                    modifyProfileController.setUser(user); break;
                case "User Profile":
                    UserProfileController userProfileController = loader.getController();
                    userProfileController.setUser(user); break;
                case "New User":
                    UserModifyController userModifyController = loader.getController();
                    userModifyController.setUser(user); break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root,1440, 800));
        stage.show();
    }

    // method to switch to single page with event object
    public static void toPageWithEvent(MouseEvent event, String fxmlFile, String title, User user, Event evt) {
        Parent root = null;
        try{
            FXMLLoader loader = new FXMLLoader(Utils.class.getResource(fxmlFile));
            root = loader.load();
            // pass information to page and set userInfo and event
            switch(title) {
                case "Event Detail":
                    ModifyEventController addEventController = loader.getController();
                    addEventController.setUser(user);
                    addEventController.setEvent(evt); break;
                case "Transactions":
                    TransactionsController transactionsController = loader.getController();
                    transactionsController.setUser(user);
                    transactionsController.setEvent(evt); break;
                case "Attendees":
                    AttendeesController attendeesController = loader.getController();
                    attendeesController.setUser(user);
                    attendeesController.setEvent(evt); break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root,1440, 800));
        stage.show();
    }

    // method to switch to single transaction detail
    public static void toTransDetail(MouseEvent event, String fxmlFile, String title, User user, Event evt, Transaction trans) {
        Parent root = null;
        try{
            FXMLLoader loader = new FXMLLoader(Utils.class.getResource(fxmlFile));
            root = loader.load();

            // pass information to transaction page
            ModifyTransactionController modifyTransactionController = loader.getController();
            modifyTransactionController.setUser(user);
            modifyTransactionController.setEvent(evt);
            modifyTransactionController.setTrans(trans);
        } catch (Exception e){
            e.printStackTrace();
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root,1440, 800));
        stage.show();
    }

    // method to show email box
    public static void getMailBox(MouseEvent event, String fxmlFile, ObservableList<String> emailLists) {
        FXMLLoader loader = new FXMLLoader ();
        loader.setLocation(Utils.class.getResource(fxmlFile));
        try {
            loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        EmailController emailController = loader.getController();
        emailController.setRecipients(emailLists);

        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }

    // method to show information message
    public static void showInfo(String title, String content) {
        // set alert type
        Alert alert  = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        // show the dialog
        alert.show();
    }
}
