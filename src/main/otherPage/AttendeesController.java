package main.otherPage;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import main.Utils;
import main.models.Event;
import main.models.Student;
import main.models.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AttendeesController implements Initializable {
    @FXML
    private Label subTitle;

    @FXML
    private Button userBtn;

    @FXML
    private JFXButton backBtn;

    @FXML
    private Label attendeeTitle;

    @FXML
    private Button addAttendee;

    @FXML
    private FontAwesomeIconView refreshBtn;

    @FXML
    private Button emailAllBtn;

    @FXML
    private TableView<Student> attendeeTable;

    @FXML
    private TableColumn<Student, String> nameCol;

    @FXML
    private TableColumn<Student, String> emailCol;

    @FXML
    private TableColumn<Student, String> roleCol;

    @FXML
    private TableColumn<Student, String> actionCol;

    private User user;

    private Student aStudent;

    private Event anEvent;

    private ObservableList<Student> attendeeList = FXCollections.observableArrayList();

    // emailLists
    private ObservableList<String> emailLists = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        backBtn.setOnMouseClicked((MouseEvent event) -> {
            // invoke method to event detail page
            Utils.toPageWithEvent(event, "resources/event_modify.fxml", "Event Detail", user, anEvent);
        });

        // switch to user detail page
        userBtn.setOnAction(event -> Utils.changeScene(event, "resources/user_profile.fxml", "User Profile", user));

        //Get add View
        addAttendee.setOnMouseClicked((MouseEvent event) -> getAddView());

        //Refresh Page
        refreshBtn.setOnMouseClicked((MouseEvent event) -> initPage());

        //Send email to all attendee in current event
        emailAllBtn.setOnMouseClicked((MouseEvent event) -> {
            // clear email lists
            emailLists.clear();
            // add all attendees' email to lists
            attendeeList.forEach((attendee) -> {
                emailLists.add(attendee.getStudentEmail());
            });
            // open email box
            Utils.getMailBox(event, "resources/email.fxml", emailLists);
        });
    }

    // Invoke show list of available students
    private void getAddView() {
        FXMLLoader loader = new FXMLLoader ();
        loader.setLocation(Utils.class.getResource("resources/add_attendee.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(AttendeesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        AddAttendeeController addAttendeeController = loader.getController();
        addAttendeeController.setEvent(anEvent.getId());

        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }

    // initialize page with information
    private void initPage() {
        // back to event detail
        backBtn.setText("Back to " + anEvent.getName() + " #" + anEvent.getId()+"\"");
        attendeeTitle.setText(anEvent.getName() + " #" + anEvent.getId() + " - Attendees");
        subTitle.setText("Events / " + anEvent.getName() + " / attendees");

        loadData();
    }

    private void loadData() {

        nameCol.setCellValueFactory(cellData -> cellData.getValue().studentNameProperty());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().studentEmailProperty());
        roleCol.setCellValueFactory(cellData -> cellData.getValue().STATUSProperty());

        displayAttendeeList();
        addButtonToTable();
    }

    private void displayAttendeeList() {
        try {
            //Get all attendees based on event information
           attendeeList = AttendeeDAO.displayAttendees(anEvent.getId());
            //Populate attendees on TableView
            attendeeTable.setItems(attendeeList);
        } catch(SQLException e) {
            System.out.println("Error occurred while getting events information from DB.\n" + e);
        }
    }

    private void addButtonToTable() {

        //add cell of button to view detail
        Callback<TableColumn<Student, String>, TableCell<Student, String>> cellFactory =
                (TableColumn<Student, String> param) -> {

                    // make cell containing button
                    final TableCell<Student, String> cell = new TableCell<Student, String>() {
                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                                setText(null);
                            } else {
                                FontAwesomeIconView emailIcon = new FontAwesomeIconView(FontAwesomeIcon.ENVELOPE_SQUARE);
                                emailIcon.getStyleClass().add("editBtn");

                                FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                                deleteIcon.getStyleClass().add("deleteBtn");

                                //Delete an event with a given event ID from DB
                                deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                                    try {
                                        aStudent = attendeeTable.getSelectionModel().getSelectedItem();
                                        AttendeeDAO.deleteWithIds(anEvent.getId(), aStudent.getStudentID());
                                        Utils.showInfo("Deleted An Attendee",  "You are successful deleted an attendee from database!");
                                        // refresh the page
                                        loadData();
                                    } catch (SQLException ex) {
                                        System.out.println("Problem occurred while deleting employee " + ex);
                                    }
                                });

                                // Send email to student selected
                                emailIcon.setOnMouseClicked((MouseEvent event) -> {
                                    // clear email lists
                                    emailLists.clear();
                                    aStudent = attendeeTable.getSelectionModel().getSelectedItem();
                                    emailLists.add(aStudent.getStudentEmail());
                                    // open email box
                                    Utils.getMailBox(event, "resources/email.fxml", emailLists);
                                });

                                HBox buttons = new HBox(emailIcon, deleteIcon);
                                buttons.setStyle("-fx-alignment:center");
                                HBox.setMargin(deleteIcon, new Insets(0, 0, 0, 25));

                                setGraphic(buttons);
                                setText(null);
                            }
                        }
                    };
                    return cell;
                };

        actionCol.setCellFactory(cellFactory);
    }

    // Setup user information
    public void setUser(User user) {
        this.user = user;
        if(user != null)
            userBtn.setText("Welcome, " + user.getUserName());
    }

    // Setup event information
    public void setEvent(Event event) {
        this.anEvent = event;
        if(anEvent != null)
            initPage();
    }

}
