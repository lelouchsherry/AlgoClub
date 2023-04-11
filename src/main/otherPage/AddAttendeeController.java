package main.otherPage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.Utils;
import main.models.Student;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddAttendeeController implements Initializable {
    @FXML
    private Button addBtn;

    @FXML
    private ListView<Student> attendeeList;

    private int eventID;

    private ObservableList<Student> studList = FXCollections.observableArrayList();
    private ObservableList<Student> selectedStuds = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // add selected students to participant
        addBtn.setOnMouseClicked((MouseEvent event) -> {
            for (int i = 0; i < selectedStuds.size(); i++) {
                // Add new attendees to participant
                try {
                    AttendeeDAO.addAttendee(eventID, selectedStuds.get(i).getStudentID());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Utils.showInfo("Added New Attendee", "You are successful added new Attendee to database!");
            // close this window
            Stage stage = (Stage) addBtn.getScene().getWindow();
            // do what you have to do
            stage.close();
        });
    }

    // Pass Event ID
    public void setEvent(int event) {
        this.eventID = event;
        displayAvailAttendees();
    }

    // Display available attendees
    private void displayAvailAttendees() {
        try {
            //Get all available students
            studList = AttendeeDAO.getAvailAttendees(eventID);
            //Populate Students on Listview
            attendeeList.setItems(studList);

            // Customize list which checkbox, and save to selected list
            attendeeList.setCellFactory((ListView<Student> param) -> new ListCell<Student>() {
                @Override
                public void updateItem(Student stud, boolean empty) {
                    super.updateItem(stud, empty);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        CheckBox checkBox = new CheckBox(stud.getStudentName());

                        checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                            if (isNowSelected) {
                                selectedStuds.add(stud);
                            } else {
                                selectedStuds.remove(stud);
                            }
                        });
                        setGraphic(checkBox);
                    }
                }
            });
        } catch(SQLException e) {
            System.out.println("Error occurred while getting events information from DB.\n" + e);
        }
    }

}
