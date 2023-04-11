package main.financialPage;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import main.Utils;
import main.models.Event;
import main.models.Transaction;
import main.models.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TransactionsController implements Initializable {
    @FXML
    private Label transSubTitle;

    @FXML
    private Label subTotal;

    @FXML
    private Button userBtn;

    @FXML
    private JFXButton backBtn;

    @FXML
    private Button reportBtn;

    @FXML
    private Button newTransBtn;

    @FXML
    private Label transTitle;

    @FXML
    private TableView<Transaction> transTable;

    @FXML
    private TableColumn<Transaction, Number> idCol;

    @FXML
    private TableColumn<Transaction, String> dateCol;

    @FXML
    private TableColumn<Transaction, String> descrCol;

    @FXML
    private TableColumn<Transaction, Number> costCol;

    @FXML
    private TableColumn<Transaction, String> actionCol;

    private User user;

    private Event anEvent;

    private Transaction anTrans;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // back to financial page
        backBtn.setOnAction(event -> Utils.changeScene(event, "resources/financial.fxml", "Financial", user));
        backBtn.setText("Back to \"Financial\"");

        // switch to user detail page
        userBtn.setOnAction(event -> Utils.changeScene(event, "resources/user_profile.fxml", "User Profile", user));

        // add new transaction
        newTransBtn.setOnMouseClicked((MouseEvent event) -> {
            // invoke method to transaction update page
            Utils.toTransDetail(event, "resources/trans_modify.fxml", "Add Transaction", user, anEvent, anTrans);
        });

        // invoke exportToExcel
        reportBtn.setOnMouseClicked(event -> exportToExcel(event));
    }

    // Export database to excel file
    private void exportToExcel(MouseEvent event) {
        try {
            //Export single event transactions information save as excel
            FinancialDAO.exportTrans(event, anEvent.getId());
        } catch(SQLException e) {
            System.out.println("Error occurred while getting transactions information from DB.\n" + e);
        }
    }

    private void loadData() {
        idCol.setCellValueFactory(cellData -> cellData.getValue().transIdProperty());
        costCol.setCellValueFactory(cellData -> cellData.getValue().costProperty());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        descrCol.setCellValueFactory(cellData -> cellData.getValue().purposeProperty());

        displayTransactions();
        addButtonToTable();
        //Calculate sum
        addSumRow();
    }

    // display transactions regrading event id
    private void displayTransactions() {
        try {
            //Get all transaction information
            ObservableList<Transaction> transList = FinancialDAO.displayTransactions(String.valueOf(anEvent.getId()));
            //Populate Transactions on TableView
            transTable.setItems(transList);
        } catch(SQLException e) {
            System.out.println("Error occurred while getting events information from DB.\n" + e);
        }
    }

    // add modify actions to table
    private void addButtonToTable() {
        //add cell of button to view detail
        Callback<TableColumn<Transaction, String>, TableCell<Transaction, String>> cellFactory =
                (TableColumn<Transaction, String> param) -> {

                    // make cell containing button
                    final TableCell<Transaction, String> cell = new TableCell<Transaction, String>() {
                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                                setText(null);
                            } else {
                                FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);
                                editIcon.getStyleClass().add("editBtn");

                                FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                                deleteIcon.getStyleClass().add("deleteBtn");

                                //Delete a transaction with a given transaction ID from DB
                                deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                                    try {
                                        anTrans = transTable.getSelectionModel().getSelectedItem();
                                        FinancialDAO.deleteTransWithId(anTrans.getTransId());
                                        Utils.showInfo("Deleted A Transaction",  "You are successful deleted a transaction from database!");
                                        // refresh the page
                                        loadData();
                                    } catch (SQLException ex) {
                                        System.out.println("Problem occurred while deleting employee " + ex);
                                    }
                                });

                                // Switch to update transaction page and display the information
                                editIcon.setOnMouseClicked((MouseEvent event) -> {
                                    anTrans = transTable.getSelectionModel().getSelectedItem();
                                    // invoke method to transaction update page
                                    Utils.toTransDetail(event, "resources/trans_modify.fxml", "Edit Transaction", user, anEvent, anTrans);
                                });

                                HBox buttons = new HBox(editIcon, deleteIcon);
                                buttons.setStyle("-fx-alignment:center");
                                HBox.setMargin(deleteIcon, new Insets(0, 0, 0, 30));

                                setGraphic(buttons);
                                setText(null);
                            }
                        }
                    };
                    return cell;
                };
        actionCol.setCellFactory(cellFactory);
    }

    // add sum of all costs to the table
    private void addSumRow() {
        double sum = 0;
        for (Transaction item : transTable.getItems()) {
            sum += item.getCost();
        }
        subTotal.setText("Sub-Total:  $" + String.format("%.2f", sum));
    }

    // Setup user information
    public void setUser(User user) {
        this.user = user;
        if(user != null)
            userBtn.setText("Welcome, " + user.getUserName());
    }

    // Setup event information
    public void setEvent(Event event) {
        anEvent = event;

        // Update with information exists
        transSubTitle.setText("");
        transTitle.setText(anEvent.getName() + " #" + anEvent.getId() + " - Transactions");
        loadData();
    }
}
