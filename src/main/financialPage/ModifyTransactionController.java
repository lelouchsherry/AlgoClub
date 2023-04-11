package main.financialPage;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import main.Utils;
import main.models.Event;
import main.models.Transaction;
import main.models.User;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ModifyTransactionController implements Initializable {
    @FXML
    private Button userBtn;

    @FXML
    private JFXButton backBtn;

    @FXML
    private Label transTitle;

    @FXML
    private TextField idTF;

    @FXML
    private JFXDatePicker datePicker;

    @FXML
    private TextField costTF;

    @FXML
    private TextArea purposeTF;

    @FXML
    private Button modifyBtn;

    private User user;
    private Event evt;
    private Transaction trans;

    private boolean isUpdate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // invoke modify transaction
        modifyBtn.setOnAction(event -> modifyTrans());
        // switch to user detail page
        userBtn.setOnAction(event -> Utils.changeScene(event, "resources/user_profile.fxml", "User Profile", user));
    }

    // initialize page with add transaction information
    private void initPage() {
        // reset form to blank
        resetForm();
        // display the add transaction page
        modifyBtn.setText("Add");
        transTitle.setText("Add Transaction");
        isUpdate = false;
        modifyBtn.setText("Add");
    }

    // initialize editPage with transaction information
    private void editPage() {
        transTitle.setText("Edit Transaction");
        isUpdate = true;
        modifyBtn.setText("Edit");
    }

    // either Add/Edit transaction
    private void modifyTrans() {
        // prepare to check and insert to database
        String purpose = purposeTF.getText();
        String cost = costTF.getText();
        String date = String.valueOf(datePicker.getValue());
        boolean isValid = checkIsNumber(cost);

        // simple validation on inputs not empty and cost is number
        if (purpose.isEmpty() || datePicker.getValue() == null ||
                cost.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please Fill All DATA to Forms!", ButtonType.OK);
            alert.showAndWait();
        } else if(!isValid) {
            // promote error message
            Alert alert = new Alert(Alert.AlertType.ERROR, "Only NUMBER valid for cost", ButtonType.OK);
            alert.showAndWait();
        }
        else{
            // based on if it's update, either edit or add new transaction to database
            try {
                if (!isUpdate) {
                    // Add transaction
                    FinancialDAO.addTrans(evt.getId(), purpose, Double.parseDouble(cost), date);
                    // display message and reset form
                    Utils.showInfo("Added New Transaction", "You are successful added new transaction to database!");
                    resetForm();
                } else {
                    // Edit transaction
                    FinancialDAO.updateTrans(trans.getTransId(), purpose, Double.parseDouble(cost), date);
                    // display message and update form
                    Utils.showInfo("Updated Transaction", "You are successful updated Transaction to database!");
                    updateTransaction();
                }
            } catch (SQLException e) {
                System.out.println("Problem occurred while inserting employee " + e);
            }
        }
    }

    // Using try catch to check if is number entered
    private boolean checkIsNumber(String cost) {
        try {
            Double.parseDouble(cost);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    private void updateTransaction() {
        // retrieve new data updated in database
        trans = FinancialDAO.searchTransaction(String.valueOf(trans.getTransId()));
        // update form data
        updateForm();
    }

    private void resetForm() {
        idTF.setText("");
        datePicker.setValue(null);
        costTF.setText("");
        purposeTF.setText("");
    }

    private void updateForm() {
        idTF.setText(String.valueOf(trans.getTransId()));
        datePicker.setValue( LocalDate.parse(trans.getDate()));
        costTF.setText(String.valueOf(trans.getCost()));
        purposeTF.setText(trans.getPurpose());
    }

    // Setup user information
    public void setUser(User user) {
        this.user = user;
        if(user != null)
            userBtn.setText("Welcome, " + user.getUserName());
    }

    // Setup event information
    public void setEvent(Event event) {
        this.evt = event;
    }

    // Setup existing transaction information
    public void setTrans(Transaction trans) {
        this.trans = trans;

        // back to transaction
        backBtn.setText("Back to " + evt.getName() + " #" + evt.getId() + " - Transactions");
        backBtn.setOnMouseClicked((MouseEvent event) -> {
            // invoke method to transaction page
            Utils.toPageWithEvent(event, "resources/transactions.fxml", "Transactions", user, evt);
        });
        // id field just for read purpose
        idTF.setDisable(true);

        if(trans != null) {
            // update form with data
            updateForm();
            // display edit page
            editPage();
        } else {
            initPage();
        }
    }
}
