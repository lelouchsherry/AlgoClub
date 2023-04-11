package main.eventpage;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import main.models.Event;
import main.models.User;
import main.Utils;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EventsController implements Initializable {
    @FXML
    private Button menu1;

    @FXML
    private Button menu2;

    @FXML
    private Button menu3;

    @FXML
    private Button newEvtBtn;

    @FXML
    private Button viewPastBtn;

    @FXML
    private Label eventTitle;

    @FXML
    private TableView<Event> eventsTable;

    @FXML
    private TableColumn<Event, Number> idCol;

    @FXML
    private TableColumn<Event, String> nameCol;

    @FXML
    private TableColumn<Event, String> dateCol;

    @FXML
    private TableColumn<Event, String>  timeCol;

    @FXML
    private TableColumn<Event, String> locationCol;

    @FXML
    private TableColumn<Event, String> detailCol;

    @FXML
    private Button userProfileBtn;

    @FXML
    private TextField searchBox;

    @FXML
    private Button searchBtn;

    @FXML
    private Label errorMessage;

    private User user;
    private Event anEvent;

    private boolean isClicked = false;

    //Initializing the controller class.
    //This method is automatically called after the fxml file has been loaded.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        jumpPage();
        // invoke to switch to user detail
        userProfile();
        loadData();
        // invoke search
        searchBtn.setOnAction(event -> {
            errorMessage.setText("");
            searchEvent();
        });
        // switch to create new event page
        newEvtBtn.setOnAction(event -> Utils.changeScene(event, "resources/event_modify.fxml", "New Event", user));

        // invoke toggle event
        toggleEvents();
    }

    private void userProfile() {
        // switch to user detail page
        userProfileBtn.setOnAction(event -> Utils.changeScene(event, "resources/user_profile.fxml", "User Profile", user));
    }

    // toggle upcoming/past events
    private void toggleEvents() {
        viewPastBtn.setOnAction(event -> {
            if(!isClicked) {
                displayPastEvts();
                isClicked = true;
                viewPastBtn.setText("Upcoming Events");
            } else {
                displayUpcomEvts();
                isClicked = false;
                viewPastBtn.setText("View Past Events");
            }
        });
    }

    // switch scene
    private void jumpPage() {
        menu1.setOnAction(event -> displayUpcomEvts());
        menu2.setOnAction(event -> Utils.changeScene(event, "resources/financial.fxml", "Financial", user));
        menu3.setOnAction(event -> Utils.changeScene(event, "resources/profile.fxml", "Profiles", user));
    }

    private void loadData() {
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        nameCol.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        timeCol.setCellValueFactory(cellData -> cellData.getValue().timeProperty());
        locationCol.setCellValueFactory(cellData -> cellData.getValue().locationProperty());

        displayUpcomEvts();
        addButtonToTable();
    }

    private void displayUpcomEvts() {
        eventTitle.setText("Upcoming Events");
        try {
            //Get all Upcoming Events information
            ObservableList<Event> eventList = EventDAO.upcomingEvents();
            //Populate Events on TableView
            populateEvents(eventList);
        } catch(SQLException e) {
            System.out.println("Error occurred while getting events information from DB.\n" + e);
        }
    }

    private void displayPastEvts() {
        eventTitle.setText("Past Events");
        try {
            //Get all Past Events information
            ObservableList<Event> eventList = EventDAO.pastEvents();
            //Populate Events on TableView
            populateEvents(eventList);
        } catch(SQLException e) {
            System.out.println("Error occurred while getting events information from DB.\n" + e);
        }
    }

    //Populate Events for TableView
    private void populateEvents(ObservableList<Event> eventList) {
        //Set items to the eventsTable
        eventsTable.setItems(eventList);
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

    // Populate Event for inside TableView
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
        //Add event to the ObservableList
        evtData.add(evt);
        //Set items to the eventsTable
        eventsTable.setItems(evtData);
    }

    private void addButtonToTable() {
        //add cell of button to view detail
        Callback<TableColumn<Event, String>, TableCell<Event, String>> cellFactory =
                (TableColumn<Event, String> param) -> {

            // make cell containing button
            final TableCell<Event, String> cell = new TableCell<Event, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            Label detailLabel = new Label("Event Detail");
                            detailLabel.setPrefWidth(130);
                            detailLabel.setPrefHeight(35);
                            detailLabel.getStyleClass().add("btn");

                            FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                            deleteIcon.getStyleClass().add("deleteBtn");

                            //Delete an event with a given event ID from DB
                            deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                                try {
                                    anEvent = eventsTable.getSelectionModel().getSelectedItem();
                                    EventDAO.deleteEvtWithId(anEvent.getId());
                                    Utils.showInfo("Deleted An Event",  "You are successful deleted an event from database!");
                                    // refresh the page and toggle the event
                                    loadData();
                                    isClicked = false;
                                    toggleEvents();
                                } catch (SQLException ex) {
                                    System.out.println("Problem occurred while deleting employee " + ex);
                                }
                            });

                            // Switch to detail page and display the information
                            detailLabel.setOnMouseClicked((MouseEvent event) -> {
                                anEvent = eventsTable.getSelectionModel().getSelectedItem();
                                // invoke method to event update page
                                Utils.toPageWithEvent(event, "resources/event_modify.fxml", "Event Detail", user, anEvent);

                            });

                            HBox buttons = new HBox(detailLabel, deleteIcon);
                            buttons.setStyle("-fx-alignment:center");
                            HBox.setMargin(deleteIcon, new Insets(0, 0, 0, 25));

                            setGraphic(buttons);
                            setText(null);
                        }
                    }
                };
                return cell;
        };

        detailCol.setCellFactory(cellFactory);
    }

    public void setUser(User user) {
        this.user = user;
        if(user != null)
            userProfileBtn.setText("Welcome, " + user.getUserName());
    }

}
