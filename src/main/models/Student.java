package main.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Student {
    private IntegerProperty studentID;

    private StringProperty studentName;

    private StringProperty pronouns;

    private StringProperty studentAddress;

    private StringProperty studentPhone;

    private StringProperty studentEmail;

    private StringProperty guardianName;

    private StringProperty guardianPhone;

    private StringProperty relationship;

    private final StringProperty STATUS = new SimpleStringProperty("Student");

    //Constructor for the "Student" object;
    public Student(){
        this.studentID = new SimpleIntegerProperty();
        this.studentName = new SimpleStringProperty();
        this.pronouns = new SimpleStringProperty();
        this.studentAddress = new SimpleStringProperty();
        this.studentPhone = new SimpleStringProperty();
        this.studentEmail = new SimpleStringProperty();
        this.guardianName = new SimpleStringProperty();
        this.guardianPhone = new SimpleStringProperty();
        this.relationship = new SimpleStringProperty();
    }

    //Student ID
    public int getStudentID() {
        return studentID.get();
    }

    public IntegerProperty studentIDProperty() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID.set(studentID);
    }

    //Student Name
    public String getStudentName() {
        return studentName.get();
    }

    public StringProperty studentNameProperty() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName.set(studentName);
    }

    //Pronouns
    public String getPronouns() {
        return pronouns.get();
    }

    public StringProperty pronounsProperty() {
        return pronouns;
    }

    public void setPronouns(String pronouns) {
        this.pronouns.set(pronouns);
    }

    //Student Address
    public String getStudentAddress() {
        return studentAddress.get();
    }

    public StringProperty studentAddressProperty() {
        return studentAddress;
    }

    public void setStudentAddress(String studentAddress) {
        this.studentAddress.set(studentAddress);
    }

    //Student Phone
    public String getStudentPhone() {
        return studentPhone.get();
    }

    public StringProperty studentPhoneProperty() {
        return studentPhone;
    }

    public void setStudentPhone(String studentPhone) {
        this.studentPhone.set(studentPhone);
    }

    //Student Email
    public String getStudentEmail() {
        return studentEmail.get();
    }

    public StringProperty studentEmailProperty() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail.set(studentEmail);
    }

    //Student Guardian Name
    public String getGuardianName() {
        return guardianName.get();
    }

    public StringProperty guardianNameProperty() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName.set(guardianName);
    }

    //Student Guardian Phone
    public String getGuardianPhone() {
        return guardianPhone.get();
    }

    public StringProperty guardianPhoneProperty() {
        return guardianPhone;
    }

    public void setGuardianPhone(String guardianPhone) {
        this.guardianPhone.set(guardianPhone);
    }

    //Student-Guardian Relationship
    public String getRelationship() {
        return relationship.get();
    }

    public StringProperty relationshipProperty() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship.set(relationship);
    }

    //Student status
    public String getSTATUS() {
        return STATUS.get();
    }

    public StringProperty STATUSProperty() {
        return STATUS;
    }
}

