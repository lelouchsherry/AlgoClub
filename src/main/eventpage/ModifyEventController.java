package main.eventpage;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import main.Utils;
import main.models.Event;
import main.models.Manager;
import main.models.User;
import main.profilePage.ProfileDAO;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class ModifyEventController implements Initializable {
    @FXML
    private Label evtSubTitle;

    @FXML
    private Label evtName;

    @FXML
    private Button backBtn;

    @FXML
    private Button userBtn;

    @FXML
    private TextField evtNameTF;

    @FXML
    private JFXDatePicker datePicker;

    @FXML
    private JFXTimePicker timePicker;

    @FXML
    private TextField locationTF;

    @FXML
    private TextField managerName;

    @FXML
    private ListView<Manager> mgrList;

    @FXML
    private Button addManagers;

    @FXML
    private TextArea descriptionTF;

    @FXML
    private Button viewPsBtn;

    @FXML
    private Button viewTransBtn;

    @FXML
    private Button modifyBtn;

    @FXML
    private HBox subMenu;

    private User user;
    private Manager manager;
    private Event anEvent;
    private int managerID;

    private boolean isUpdate;

    private ObservableList<Manager> managerList = FXCollections.observableArrayList();
    private ToggleGroup group = new ToggleGroup();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // back to events page
        backBtn.setOnAction(event -> Utils.changeScene(event, "resources/events.fxml", "Events", user));

        // switch to user detail page
        userBtn.setOnAction(event -> Utils.changeScene(event, "resources/user_profile.fxml", "User Profile", user));

        // initialize page with add new event information
        initPage();

        // invoke modify event
        modifyBtn.setOnAction(event -> modifyEvent());

        // invoke add managers to list
        addManagers.setOnAction(event ->  displayMgrList());

        // add listener to radio button
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
        {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1)
            {
                // Cast object to radio button
                RadioButton chk = (RadioButton)t1.getToggleGroup().getSelectedToggle();
                managerName.setText(chk.getText());
                managerID = Integer.parseInt(chk.getId());
            }
        });

        // Switch to transactions page and display the information
        viewTransBtn.setOnMouseClicked((MouseEvent event) -> {
            // invoke method to transaction page
            Utils.toPageWithEvent(event, "resources/transactions.fxml", "Transactions", user, anEvent);
        });

        // Switch to Attendees page and display participants
        viewPsBtn.setOnMouseClicked((MouseEvent event) -> {
            // invoke method to attendees page
            Utils.toPageWithEvent(event, "resources/attendees.fxml", "Attendees", user, anEvent);
        });
    }

    private void initPage() {
        subMenu.setVisible(false);
        // reset form to blank
        resetForm();
        // display the new event page
        modifyBtn.setText("Create");
        evtSubTitle.setText("Events / New event");
        evtName.setText("Create New Event");
        isUpdate = false;
    }

    // either Add/Update event
    private void modifyEvent() {
        // prepare to check and insert to database
        String evtName = evtNameTF.getText();
        String mgrName = managerName.getText();
        String evtLocation = locationTF.getText();
        String evtDetail = descriptionTF.getText();

        // simple validation on inputs not empty
        if (evtName.isEmpty() || datePicker.getValue() == null || timePicker.getValue() == null ||
                mgrName.isEmpty() || evtLocation.isEmpty() || evtDetail.isEmpty()) {
            // promote error message
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please Fill All DATA to Forms!", ButtonType.OK);
            alert.showAndWait();
        } else {
            // concatenate date time
            String dateTime = datePicker.getValue() + " " + timePicker.getValue();

            // based on if it's update, either update or add new event to database
            try {
                if (!isUpdate) {
                    // Add event
                    EventDAO.addEvt(managerID, evtName, dateTime, evtLocation, evtDetail);
                    // display message and reset form
                    Utils.showInfo("Added New Event", "You are successful added new event to database!");
                    resetForm();
                } else {
                    // Update event
                    EventDAO.updateEvt(anEvent.getId(), managerID, evtName, dateTime, evtLocation, evtDetail);
                    // display message and update form
                    Utils.showInfo("Updated Event",  "You are successful updated event to database!");
                    updateEvent();
                }
            } catch (SQLException e) {
                System.out.println("Problem occurred while inserting event " + e);
            }
        }
    }

    private void updateEvent() {
        // retrieve new data updated in database
        anEvent = EventDAO.searchEvent(String.valueOf(anEvent.getId()));
        // update form data
        setTextField();
    }

    // display managers to select
    private void displayMgrList() {
        managerList.clear();
        try {
            //Get all managers name
            managerList = ProfileDAO.displayManagers();
            //Populate Managers on Listview
            mgrList.setItems(managerList);
            //Customize list to radio buttons
            mgrList.setCellFactory(param -> new RadioListCell());
        } catch(SQLException e) {
            System.out.println("Error occurred while getting events information from DB.\n" + e);
        }
    }

    // Customized to radio buttons
    private class RadioListCell extends ListCell<Manager> {
        @Override
        public void updateItem(Manager mgr, boolean empty) {
            super.updateItem(mgr, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                RadioButton radioButton = new RadioButton(mgr.getManagerName());
                radioButton.setId(String.valueOf(mgr.getManagerID()));
                radioButton.setToggleGroup(group);
                setGraphic(radioButton);
            }
        }
    }

    // reset form to blank
    private void resetForm() {
        evtNameTF.setText("");
        datePicker.setValue(null);
        timePicker.setValue(null);
        managerName.setText("");
        locationTF.setText("");
        descriptionTF.setText("");
    }

    // Update forms with information exists
    public void setTextField() {
        evtSubTitle.setText("Events / " + anEvent.getName());
        evtName.setText(anEvent.getName() + " #" + anEvent.getId());
        evtNameTF.setText(anEvent.getName() );
        datePicker.setValue(LocalDate.parse(anEvent.getDate()));
        timePicker.setValue(LocalTime.parse(anEvent.getTime()));
        locationTF.setText(anEvent.getLocation());
        descriptionTF.setText(anEvent.getDescription());

        // Setup update page properties
        isUpdate = true;
        subMenu.setVisible(true);
        modifyBtn.setText("Update");

        setManager(anEvent.getMgrId());
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
        setTextField();
    }

    // Setup manager information
    public void setManager(int mgrId) {
        // Using mgrId find the manager's name
        try {
            //Get Manager information
            manager = ProfileDAO.searchManager(String.valueOf(mgrId));
            managerName.setText(manager.getManagerName());
            managerID = manager.getManagerID();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
