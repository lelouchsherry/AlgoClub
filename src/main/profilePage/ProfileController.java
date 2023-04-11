package main.profilePage;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.fxml.Initializable;
import main.Utils;
import main.models.Admin;
import main.models.Manager;
import main.models.User;
import main.models.Student;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private Button menu1;

    @FXML
    private Button menu2;

    @FXML
    private Button menu3;

    @FXML
    private Button userProfileBtn;

    @FXML
    private Button createNewUserBtn;

    @FXML
    private TableView profileTable;

    @FXML
    private TableColumn idCol;

    @FXML
    private TableColumn nameCol;

    @FXML
    private TableColumn emailCol;

    @FXML
    private TableColumn roleCol;

    @FXML
    private TableColumn actionCol;

    @FXML
    private TextField searchBox;

    @FXML
    private Button searchBtn;

    @FXML
    private Button emailAllBtn;

    @FXML
    private Label errorMessage;

    @FXML
    private MenuButton statusMenu;

    private User user;

    private Student aStudent;
    private Manager aManager;
    private Admin aAdmin;

    // For different type of users
    private String status = "";

    private final String STATUS_1 = "Student";
    private final String STATUS_2 = "Manager";
    private final String STATUS_3 = "Admin";

    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private ObservableList<Manager> managerList = FXCollections.observableArrayList();
    private ObservableList<Admin> adminList = FXCollections.observableArrayList();

    // emailLists
    private ObservableList<String> emailLists = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // switch to user detail page
        userProfileBtn.setOnAction(event -> Utils.changeScene(event, "resources/user_profile.fxml", "User Profile", user));
        createNewUserBtn.setOnAction(event -> Utils.changeScene(event,"resources/profile_modify.fxml", "New Student", user));
        jumpPage();
        loadData();

        // invoke search based status
        searchBtn.setOnAction(event -> {
            switch (status) {
                case "": errorMessage.setText("Please select status before search"); break;
                case STATUS_1: searchOneStudt(); break;
                case STATUS_2: searchOneManager(); break;
                case STATUS_3: searchOneAdmin(); break;
            }
        });

        //Send email to all based on status
        emailAllBtn.setOnMouseClicked((MouseEvent event) -> {
            emailLists.clear();
            switch (status) {
                case "":
                case STATUS_1: emailAllStudt(event); break;
                case STATUS_2: emailAllMgr(event); break;
                case STATUS_3: emailAllAdm(event); break;
            }
        });
    }
    // For menu
    private void jumpPage() {
        menu1.setOnAction(event -> Utils.changeScene(event, "resources/events.fxml", "Events", user));
        menu2.setOnAction(event -> Utils.changeScene(event, "resources/financial.fxml", "Financial", user));
        menu3.setOnAction(event -> Utils.changeScene(event, "resources/profile.fxml", "Profiles", user));
    }

    // For email all students
    private void emailAllStudt(MouseEvent event) {
        // add all students email to lists
        studentList.forEach((student) -> {
            emailLists.add(student.getStudentEmail());
        });
        // open email box
        Utils.getMailBox(event, "resources/email.fxml", emailLists);
    }

    private void emailAllMgr(MouseEvent event) {
        // add all managers email to lists
        managerList.forEach((mgr) -> {
            emailLists.add(mgr.getManagerEmail());
        });
        // open email box
        Utils.getMailBox(event, "resources/email.fxml", emailLists);
    }

    private void emailAllAdm(MouseEvent event) {
        // add all admin email to lists
        adminList.forEach((adm) -> {
            emailLists.add(adm.getAdminEmail());
        });
        // open email box
        Utils.getMailBox(event, "resources/email.fxml", emailLists);
    }

    // Dropdown menu for status
    // For selected student
    public void studSelected(javafx.event.ActionEvent event) {
        statusMenu.setText(STATUS_1);
        status = STATUS_1;
        loadData();
    }

    private void loadData() {

        idCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Student, Number>, IntegerProperty>() {
            @Override
            public IntegerProperty call(TableColumn.CellDataFeatures<Student, Number> data) {
                return data.getValue()
                        .studentIDProperty();
            }
        });

        nameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Student, String>, StringProperty>() {
            @Override
            public StringProperty call(TableColumn.CellDataFeatures<Student, String> data) {
                return data.getValue()
                        .studentNameProperty();
            }
        });

        emailCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Student, String>, StringProperty>() {
            @Override
            public StringProperty call(TableColumn.CellDataFeatures<Student, String> data) {
                return data.getValue()
                        .studentEmailProperty();
            }
        });

        roleCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Student, String>, StringProperty>() {
            @Override
            public StringProperty call(TableColumn.CellDataFeatures<Student, String> data) {
                return data.getValue() // the RelationManager
                        .STATUSProperty();
            }
        });

        // set manager's action column
        actionCol.setCellFactory(new Callback<TableColumn<Student, String>, TableCell<Student, String>>() {
            @Override
            public TableCell<Student, String> call(TableColumn<Student, String> param) {
                TableCell<Student,String> cell = new TableCell<Student, String>(){
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            FontAwesomeIconView emailIcon = new FontAwesomeIconView(FontAwesomeIcon.ENVELOPE_SQUARE);
                            emailIcon.getStyleClass().add("emailBtn");

                            FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);
                            editIcon.getStyleClass().add("editBtn");

                            FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                            deleteIcon.getStyleClass().add("deleteBtn");

                            // Send email to student selected
                            emailIcon.setOnMouseClicked((MouseEvent event) -> {
                                // clear email lists
                                emailLists.clear();

                                aStudent = (Student) profileTable.getSelectionModel().getSelectedItem();
                                emailLists.add(aStudent.getStudentEmail());
                                // open email box
                                Utils.getMailBox(event, "resources/email.fxml", emailLists);

                            });

                            //Delete an event with a given employee ID from DB
                            deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                                try {
                                    aStudent = (Student) profileTable.getSelectionModel().getSelectedItem();
                                    StudentDAO.deleteStudentWithId(aStudent.getStudentID());
                                    Utils.showInfo("Deleted Student from the Table", "You have successfully deleted a student from the database!");
                                    // refresh the page
                                    loadData();
                                } catch (SQLException ex) {
                                    System.out.println("Problem occurred while deleting employee " + ex);
                                }
                            });

                            // Switch to detail page and display the information
                            editIcon.setOnMouseClicked((MouseEvent event) -> {
                                aStudent = (Student) profileTable.getSelectionModel().getSelectedItem();
                                // prepare for switch scene
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(Utils.class.getResource("resources/profile_modify.fxml"));
                                try {
                                    loader.load();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                // pass information to detail page
                                ModifyProfileController modifyProfileController = loader.getController();
                                modifyProfileController.setUser(user);
                                modifyProfileController.setStudent(aStudent);

                                Parent root = loader.getRoot();
                                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                stage.setTitle("Student Details");
                                stage.setScene(new Scene(root, 1440, 800));
                                stage.show();
                            });

                            HBox buttons = new HBox(emailIcon, editIcon, deleteIcon);
                            buttons.setStyle("-fx-alignment:center");
                            HBox.setMargin(editIcon, new Insets(0, 25, 0, 25));

                            setGraphic(buttons);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });

        displayList();
    }

    private void searchOneStudt() {
        try {
            //Get Student information
            Student stud = StudentDAO.searchStudent(searchBox.getText());
            //Populate Student on TableView
            populateAndShow(stud);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Populate Student for inside TableView
    private void populateAndShow(Student student) {
        errorMessage.setText("");
        if (student != null) {
            //Declare and ObservableList for table view
            ObservableList<Student> studData = FXCollections.observableArrayList();
            //Add Student to the ObservableList
            studData.add(student);
            //Set items to the eventsTable
            profileTable.setItems(studData);
            searchBox.setText(""); // clear search box
        } else {
            errorMessage.setText("This ID does not exist!");
        }
    }

    // For selected manager
    public void mrgSelected(javafx.event.ActionEvent event) {
        statusMenu.setText(STATUS_2);
        status = STATUS_2;
        displayManagerList();
    }

    private void displayManagerList() {

        // Set the cell with Manager's data
        idCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Manager, Number>, IntegerProperty>() {
            @Override
            public IntegerProperty call(TableColumn.CellDataFeatures<Manager, Number> data) {
                return data.getValue()
                        .managerIDProperty();
            }
        });

        nameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Manager, String>, StringProperty>() {
            @Override
            public StringProperty call(TableColumn.CellDataFeatures<Manager, String> data) {
                return data.getValue()
                        .managerNameProperty();
            }
        });

        emailCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Manager, String>, StringProperty>() {
            @Override
            public StringProperty call(TableColumn.CellDataFeatures<Manager, String> data) {
                return data.getValue()
                        .managerEmailProperty();
            }
        });

        roleCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Manager, String>, StringProperty>() {
            @Override
            public StringProperty call(TableColumn.CellDataFeatures<Manager, String> data) {
                return data.getValue()
                        .STATUSProperty();
            }
        });

        // set manager's action column
        actionCol.setCellFactory(new Callback<TableColumn<Manager, String>, TableCell<Manager, String>>() {
            @Override
            public TableCell<Manager, String> call(TableColumn<Manager, String> param) {
                TableCell<Manager,String> cell = new TableCell<Manager, String>(){
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            FontAwesomeIconView emailIcon = new FontAwesomeIconView(FontAwesomeIcon.ENVELOPE_SQUARE);
                            emailIcon.getStyleClass().add("emailBtn");

                            FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);
                            editIcon.getStyleClass().add("editBtn");

                            FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                            deleteIcon.getStyleClass().add("deleteBtn");

                            //Delete a manager with a given manager ID from DB
                            deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                                try {
                                    aManager = (Manager) profileTable.getSelectionModel().getSelectedItem();
                                    ProfileDAO.deleteById(aManager.getSTATUS(), aManager.getManagerID());
                                    Utils.showInfo("Deleted Manager from the Table", "You have successfully deleted a manager from the database!");
                                    // refresh the page
                                    displayManagerList();
                                } catch (SQLException ex) {
                                    System.out.println("Problem occurred while deleting manager " + ex);
                                }
                            });

                            // Send email to student selected
                            emailIcon.setOnMouseClicked((MouseEvent event) -> {
                                // clear email lists
                                emailLists.clear();
                                aManager = (Manager) profileTable.getSelectionModel().getSelectedItem();
                                emailLists.add(aManager.getManagerEmail());
                                // open email box
                                Utils.getMailBox(event, "resources/email.fxml", emailLists);
                            });

                            // Switch to user detail page
                            editIcon.setOnMouseClicked((MouseEvent event) -> {
                                aManager = (Manager) profileTable.getSelectionModel().getSelectedItem();
                                // prepare for switch scene
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(Utils.class.getResource("resources/user_modify.fxml"));
                                try {
                                    loader.load();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                // pass information to detail page
                                UserModifyController userModifyController = loader.getController();
                                userModifyController.setUser(user);
                                userModifyController.setManager(aManager);

                                Parent root = loader.getRoot();
                                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                stage.setTitle("User Details");
                                stage.setScene(new Scene(root, 1440, 800));
                                stage.show();
                            });

                            // based on logged user's role, enable delete option
                            HBox buttons = null;
                            if(user.getRole().equals(STATUS_3))
                                buttons = new HBox(emailIcon, editIcon, deleteIcon);
                            else
                                buttons = new HBox(emailIcon);
                            buttons.setStyle("-fx-alignment:center");
                            HBox.setMargin(editIcon, new Insets(0, 25, 0, 25));

                            setGraphic(buttons);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        });

        displayList();
    }

    private void searchOneManager() {
        try {
            //Get Manager information
            Manager mgr = ProfileDAO.searchManager(searchBox.getText());
            //Populate Manager on TableView
            populateAndShow(mgr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Populate Manager for inside TableView
    private void populateAndShow(Manager mgr) {
        errorMessage.setText("");
        if (mgr != null) {
            //Declare and ObservableList for table view
            ObservableList<Manager> mgrData = FXCollections.observableArrayList();
            //Add Manager to the ObservableList
            mgrData.add(mgr);
            //Set items to the eventsTable
            profileTable.setItems(mgrData);
            searchBox.setText(""); // clear search box
        } else {
            errorMessage.setText("This ID does not exist!");
        }
    }

    // For selected admin
    public void adminSelected(javafx.event.ActionEvent event) {
        statusMenu.setText(STATUS_3);
        status = STATUS_3;
        displayAdminList();
    }

    private void displayAdminList() {
        // hide student and manager action column
//        studActionCol.setVisible(false);
//        mgrActionCol.setVisible(false);
//        adminAction.setVisible(true);

        // Set the cell with Manager's data
        idCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Admin, Number>, IntegerProperty>() {
            @Override
            public IntegerProperty call(TableColumn.CellDataFeatures<Admin, Number> data) {
                return data.getValue()
                        .adminIDProperty();
            }
        });

        nameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Admin, String>, StringProperty>() {
            @Override
            public StringProperty call(TableColumn.CellDataFeatures<Admin, String> data) {
                return data.getValue()
                        .adminNameProperty();
            }
        });

        emailCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Admin, String>, StringProperty>() {
            @Override
            public StringProperty call(TableColumn.CellDataFeatures<Admin, String> data) {
                return data.getValue()
                        .adminEmailProperty();
            }
        });

        roleCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Admin, String>, StringProperty>() {
            @Override
            public StringProperty call(TableColumn.CellDataFeatures<Admin, String> data) {
                return data.getValue()
                        .STATUSProperty();
            }
        });

        // set admin action column
        actionCol.setCellFactory(new Callback<TableColumn<Admin, String>, TableCell<Admin, String>>() {
            @Override
            public TableCell<Admin, String> call(TableColumn<Admin, String> param) {
                TableCell<Admin,String> cell = new TableCell<Admin, String>(){
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            FontAwesomeIconView emailIcon = new FontAwesomeIconView(FontAwesomeIcon.ENVELOPE_SQUARE);
                            emailIcon.getStyleClass().add("emailBtn");

                            FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);
                            editIcon.getStyleClass().add("editBtn");

                            FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                            deleteIcon.getStyleClass().add("deleteBtn");

                            //Delete a manager with a given manager ID from DB
                            deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                                try {
                                    aAdmin = (Admin) profileTable.getSelectionModel().getSelectedItem();
                                    if(user.getAdminID() == aAdmin.getAdminID()) {
                                        // promote error message
                                        Alert alert = new Alert(Alert.AlertType.ERROR, "Can't delete your own account", ButtonType.OK);
                                        alert.showAndWait();
                                    } else {
                                        ProfileDAO.deleteById(aAdmin.getSTATUS(), aAdmin.getAdminID());
                                        Utils.showInfo("Deleted Admin from the Table", "You have successfully deleted a admin from the database!");
                                        // refresh the page
                                        displayAdminList();
                                    }
                                } catch (SQLException ex) {
                                    System.out.println("Problem occurred while deleting manager " + ex);
                                }
                            });

                            // Send email to admin selected
                            emailIcon.setOnMouseClicked((MouseEvent event) -> {
                                // clear email lists
                                emailLists.clear();
                                aAdmin = (Admin) profileTable.getSelectionModel().getSelectedItem();
                                emailLists.add(aAdmin.getAdminEmail());
                                // open email box
                                Utils.getMailBox(event, "resources/email.fxml", emailLists);
                            });

                            // Switch to user detail page
                            editIcon.setOnMouseClicked((MouseEvent event) -> {
                                aAdmin = (Admin) profileTable.getSelectionModel().getSelectedItem();
                                // prepare for switch scene
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(Utils.class.getResource("resources/user_modify.fxml"));
                                try {
                                    loader.load();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                // pass information to detail page
                                UserModifyController userModifyController = loader.getController();
                                userModifyController.setUser(user);
                                userModifyController.setAdmin(aAdmin);

                                Parent root = loader.getRoot();
                                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                stage.setTitle("User Details");
                                stage.setScene(new Scene(root, 1440, 800));
                                stage.show();
                            });

                            // based on logged user's role, enable delete option
                            HBox buttons = null;
                            if(user.getRole().equals(STATUS_3))
                                buttons = new HBox(emailIcon, editIcon, deleteIcon);
                            else
                                buttons = new HBox(emailIcon);
                            buttons.setStyle("-fx-alignment:center");
                            HBox.setMargin(editIcon, new Insets(0, 25, 0, 25));

                            setGraphic(buttons);
                        }
                        setText(null);
                    }
                };
                return cell;
            }
        });

        displayList();
    }

    private void searchOneAdmin() {
        try {
            //Get Student information
            Admin adm = ProfileDAO.searchAdmin(searchBox.getText());
            //Populate Student on TableView
            populateAndShow(adm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Populate Admin for inside TableView
    private void populateAndShow(Admin adm) {
        errorMessage.setText("");
        if (adm != null) {
            //Declare and ObservableList for table view
            ObservableList<Admin> admData = FXCollections.observableArrayList();
            //Add Admin to the ObservableList
            admData.add(adm);
            //Set items to the eventsTable
            profileTable.setItems(admData);
            searchBox.setText(""); // clear search box
        } else {
            errorMessage.setText("This ID does not exist!");
        }
    }

    private void displayList() {
        try {
            // get list based on status
            switch (status) {
                case "":
                case STATUS_1:
                    studentList = StudentDAO.displayStudents();
                    profileTable.setItems(studentList);
                    break;
                case STATUS_2:
                    managerList = ProfileDAO.displayManagers();
                    profileTable.setItems(managerList);
                    break;
                case STATUS_3:
                    adminList = ProfileDAO.displayAdmins();
                    profileTable.setItems(adminList);
                    break;
            }

        } catch(SQLException e) {
            System.out.println("Error occurred while getting events information from DB.\n" + e);
        }
    }

    public void setUser(User user) {
        this.user = user;
        if(user != null)
            userProfileBtn.setText("Welcome, " + user.getUserName());
    }
}