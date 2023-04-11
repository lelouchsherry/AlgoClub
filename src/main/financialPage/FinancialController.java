package main.financialPage;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import main.eventpage.EventDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import main.models.Event;
import main.models.Transaction;
import main.models.User;
import main.Utils;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class FinancialController implements Initializable {
    @FXML
    private Button menu1;

    @FXML
    private Button menu2;

    @FXML
    private Button menu3;

    @FXML
    private Button userProfileBtn;

    @FXML
    private TextField searchBox;

    @FXML
    private Button searchBtn;

    @FXML
    private ListView<Event> myEvtLists;

    @FXML
    private Label errorMessage;

    @FXML
    private FontAwesomeIconView refresh;

    @FXML
    private Button reportBtn;

    ObservableList<Event> eventList = FXCollections.observableArrayList();
    ObservableList<Transaction> transList = FXCollections.observableArrayList();

    private User user;
    private Event anEvent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        jumpPage();
        userProfile();
        loadData();
        // invoke search
        searchBtn.setOnAction(event -> {
            errorMessage.setText("");
            searchEvent();
        });
        // refresh page with all events
        refresh.setOnMouseClicked(event -> loadData());

        // invoke exportToExcel
        reportBtn.setOnMouseClicked(event -> exportToExcel(event));
    }

    private void userProfile() {
        // switch to user detail page
        userProfileBtn.setOnAction(event -> Utils.changeScene(event, "resources/user_profile.fxml", "User Profile", user));
    }

    private void jumpPage() {
        menu1.setOnAction(event -> Utils.changeScene(event, "resources/events.fxml", "Events", user));
        menu2.setOnAction(event -> Utils.changeScene(event, "resources/financial.fxml", "Financial", user));
        menu3.setOnAction(event -> Utils.changeScene(event, "resources/profile.fxml", "Profiles", user));
    }

    // Export database to excel file
    private void exportToExcel(MouseEvent event) {
        try {
            //Export all transactions information save as excel
            FinancialDAO.exportAllTrans(event);
        } catch(SQLException e) {
            System.out.println("Error occurred while getting transactions information from DB.\n" + e);
        }
    }

    private void loadData() {
        displayEvts();
    }

    private void displayEvts() {
        try {
            //Get all Events information
            eventList = EventDAO.displayEvents();
            //Populate Events on eventList
            myEvtLists.setItems(eventList);
        } catch(SQLException e) {
            System.out.println("Error occurred while getting events information from DB.\n" + e);
        }

        myEvtLists.setCellFactory(new Callback<ListView<Event>, ListCell<Event>>() {
            @Override
            public ListCell<Event> call(ListView<Event> listView) {
                return new CustomListCell();
            }
        });
    }

    private void searchEvent() {
        try {
            //Get Event information
            Event evt = EventDAO.searchEvent(searchBox.getText());
            //Populate Event on TableView
            populateAndShowEvent(evt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Populate Event for inside ListView
    private void populateAndShowEvent(Event evt) {
        if (evt != null) {
            populateEvent(evt);
        } else {
            errorMessage.setText("This ID does not exist!");
        }
    }

    private void populateEvent (Event evt) {
        //Declare and ObservableList for table view
        ObservableList<Event> evtData = FXCollections.observableArrayList();
        //Add employee to the ObservableList
        evtData.add(evt);
        //Set items to the eventsTable
        myEvtLists.setItems(evtData);
    }

    // Customized lists
    private class CustomListCell extends ListCell<Event> {
        private Label title;
        private BorderPane container;
        private VBox contents;
        private HBox buttons;
        private Text description;

        private Label transBtn;

        private Label reportBtn;

        public CustomListCell() {
            super();
            title = new Label();
            title.setStyle("-fx-text-fill: #18a0fb; -fx-font-size: 20px;");
            description = new Text();
            // add style and text to buttons
            transBtn = new Label("Transactions");
            transBtn.setPrefWidth(140);
            transBtn.setPrefHeight(35);
            transBtn.getStyleClass().add("btn");
            reportBtn = new Label("Generate Report");
            reportBtn.setPrefWidth(140);
            reportBtn.setPrefHeight(35);
            reportBtn.getStyleClass().add("btn");
            // add two buttons to HBox
            buttons = new HBox(transBtn, reportBtn);
            buttons.setSpacing(50);
            contents = new VBox(title, description);
            container = new BorderPane();
            container.setLeft(contents);
            container.setRight(buttons);
            container.setPadding(new Insets(20, 50, 0, 50));
        }

        @Override
        protected void updateItem(Event item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null && !empty) {
                title.setText("#" + item.getId() + " " + item.getName());
                description.setText(item.getDescription());
                setGraphic(container);

                // Switch to transactions page and display the information
                transBtn.setOnMouseClicked((MouseEvent event) -> {
                    anEvent = myEvtLists.getSelectionModel().getSelectedItem();
                    // invoke method to transaction page
                    Utils.toPageWithEvent(event, "resources/transactions.fxml", "Transactions", user, anEvent);
                });

                // Export single event transactions
                reportBtn.setOnMouseClicked((MouseEvent event) -> {
                    anEvent = myEvtLists.getSelectionModel().getSelectedItem();
                    try {
                        FinancialDAO.exportTrans(event, anEvent.getId()
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

            } else {
                setGraphic(null);
            }
        }
    }

    public void setUser(User user) {
        this.user = user;
        if(user != null)
            userProfileBtn.setText("Welcome, " + user.getUserName());
    }
}
