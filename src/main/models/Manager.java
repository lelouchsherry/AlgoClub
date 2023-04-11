package main.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// Manager Model
public class Manager extends User {
    // class member variables
    private StringProperty managerName;
    private StringProperty pronouns;
    private StringProperty managerAddress;
    private StringProperty managerPhone;
    private StringProperty managerEmail;

    private final StringProperty STATUS = new SimpleStringProperty("Manager");

    // default constructor
    public Manager() {
        this.managerName = new SimpleStringProperty();
        this.pronouns = new SimpleStringProperty();
        this.managerPhone = new SimpleStringProperty();
        this.managerAddress = new SimpleStringProperty();
        this.managerEmail = new SimpleStringProperty();
    }

    // getter setter for variables
    public String getManagerName() {
        return managerName.get();
    }

    public StringProperty managerNameProperty() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName.set(managerName);
    }

    public String getPronouns() {
        return pronouns.get();
    }

    public StringProperty pronounsProperty() {
        return pronouns;
    }

    public void setPronouns(String pronouns) {
        this.pronouns.set(pronouns);
    }

    public String getManagerAddress() {
        return managerAddress.get();
    }

    public StringProperty managerAddressProperty() {
        return managerAddress;
    }

    public void setManagerAddress(String managerAddress) {
        this.managerAddress.set(managerAddress);
    }

    public String getManagerPhone() {
        return managerPhone.get();
    }

    public StringProperty managerPhoneProperty() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone.set(managerPhone);
    }

    public String getManagerEmail() {
        return managerEmail.get();
    }

    public StringProperty managerEmailProperty() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail.set(managerEmail);
    }

    public String getSTATUS() {
        return STATUS.get();
    }

    public StringProperty STATUSProperty() {
        return STATUS;
    }
}
