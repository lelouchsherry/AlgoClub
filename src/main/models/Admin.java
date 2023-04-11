package main.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// Admin Model
public class Admin extends User {
    // class member variables
    private StringProperty adminName;
    private StringProperty pronouns;
    private StringProperty adminAddress;
    private StringProperty adminPhone;
    private StringProperty adminEmail;

    private final StringProperty STATUS = new SimpleStringProperty("Admin");

    // default constructor
    public Admin() {
        this.adminName = new SimpleStringProperty();
        this.pronouns = new SimpleStringProperty();
        this.adminPhone = new SimpleStringProperty();
        this.adminAddress = new SimpleStringProperty();
        this.adminEmail = new SimpleStringProperty();
    }

    // getter setter for variables
    public String getAdminName() {
        return adminName.get();
    }

    public StringProperty adminNameProperty() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName.set(adminName);
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

    public String getAdminAddress() {
        return adminAddress.get();
    }

    public StringProperty adminAddressProperty() {
        return adminAddress;
    }

    public void setAdminAddress(String adminAddress) {
        this.adminAddress.set(adminAddress);
    }

    public String getAdminPhone() {
        return adminPhone.get();
    }

    public StringProperty adminPhoneProperty() {
        return adminPhone;
    }

    public void setAdminPhone(String adminPhone) {
        this.adminPhone.set(adminPhone);
    }

    public String getAdminEmail() {
        return adminEmail.get();
    }

    public StringProperty adminEmailProperty() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail.set(adminEmail);
    }

    public String getSTATUS() {
        return STATUS.get();
    }

    public StringProperty STATUSProperty() {
        return STATUS;
    }
}
