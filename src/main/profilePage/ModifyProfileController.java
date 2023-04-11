package main.profilePage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.Utils;
import main.models.Student;
import main.models.User;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifyProfileController implements Initializable {

    @FXML
    private Button backBtn;

    @FXML
    private Label studentSubTitle;

    @FXML
    private Button userBtn;

    @FXML
    private Button newUserBtn;

    @FXML
    private TextField studentNameTF;

    @FXML
    private TextField studentPronounsTF;

    @FXML
    private TextField studentAddressTF;

    @FXML
    private TextField studentPhoneTF;

    @FXML
    private TextField studentEmailTF;

    @FXML
    private TextField guardianNameTF;

    @FXML
    private TextField guardianPhoneTF;

    @FXML
    private TextField relationshipTF;

    @FXML
    private Button studentProfileSubmitBtn;

    private Boolean anUpdate;

    private Student aStudent;

    private User user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backBtn.setOnAction(event -> Utils.changeScene(event, "resources/profile.fxml", "Profiles", user));
        anUpdate = false;
        studentProfileSubmitBtn.setOnAction(event -> changeProfile());

        // switch to user detail page
        userBtn.setOnAction(event -> Utils.changeScene(event, "resources/user_profile.fxml", "User Profile", user));

        // switch to create User page
        newUserBtn.setOnAction(event -> Utils.changeScene(event, "resources/user_modify.fxml", "New User", user));
    }

    public void changeProfile() {
        //Get Strings to submit to database
        String studentName = studentNameTF.getText();
        String pronouns = studentPronounsTF.getText();
        String studentAddress = studentAddressTF.getText();
        String studentPhone = studentPhoneTF.getText();
        String studentEmail = studentEmailTF.getText();
        String guardianName = guardianNameTF.getText();
        String guardianPhone = guardianPhoneTF.getText();
        String relationship = relationshipTF.getText();

        //Make sure no inputs are empty
        if (studentName.isEmpty() || pronouns.isEmpty() || studentAddress.isEmpty() || studentPhone.isEmpty() || studentEmail.isEmpty()
                || guardianName.isEmpty() || guardianPhone.isEmpty() || relationship.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please make sure all data fields are filled!");
            alert.showAndWait();
        } else {
            try {
                //Use an if block to determine if this is a new profile, or an update to a profile
                if (!anUpdate) {
                    //Add new Student
                    StudentDAO.addStudent(studentName, pronouns, studentAddress, studentPhone, studentEmail, guardianName, guardianPhone
                            , relationship);
                    //Display confirmation message
                    Utils.showInfo("Added New Student", "You've successfully added a new student to the database!");
                    resetForm();
                } else {
                    // Update student
                    StudentDAO.updateStudent(aStudent.getStudentID(), studentName, pronouns, studentAddress, studentPhone, studentEmail, guardianName, guardianPhone, relationship);
                    // display message and update form
                    Utils.showInfo("Updated Event", "You've successfully updated a student, " + studentName + " in the database!");
                    updateStudent();
                }
            } catch (SQLException e) {
                System.out.println("Problem occurred while inserting student:  " + e);
            }

        }

    }

    private void updateStudent() {
        // retrieve new data updated in database
        aStudent = StudentDAO.searchStudent(String.valueOf(aStudent.getStudentID()));

        // update information
        setTextField();
    }

    private void resetForm() {
        studentNameTF.setText("");
        studentPronounsTF.setText("");
        studentAddressTF.setText("");
        studentPhoneTF.setText("");
        studentEmailTF.setText("");
        guardianNameTF.setText("");
        guardianPhoneTF.setText("");
        relationshipTF.setText("");
    }

    public void setTextField() {
        studentSubTitle.setText("Students / " + aStudent.getStudentName());
        studentNameTF.setText(aStudent.getStudentName());
        studentPronounsTF.setText(aStudent.getPronouns());
        studentAddressTF.setText(aStudent.getStudentAddress());
        studentPhoneTF.setText(aStudent.getStudentPhone());
        studentEmailTF.setText(aStudent.getStudentEmail());
        guardianNameTF.setText(aStudent.getGuardianName());
        guardianPhoneTF.setText(aStudent.getGuardianPhone());
        relationshipTF.setText(aStudent.getRelationship());

        // Setup update page properties
        anUpdate = true;
    }

    // Setup user information
    public void setUser(User user) {
        this.user = user;
        if (user != null)
            userBtn.setText("Welcome, " + user.getUserName());

        if(user.getRole().equals("Admin"))
            newUserBtn.setVisible(true);
        else
            newUserBtn.setVisible(false);
    }

    // Setup student information
    public void setStudent(Student student) {
        this.aStudent = student;
        setTextField();
    }
}
