package main.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// Event Model
public class Event {
    // class member variables
    private IntegerProperty id;
    private IntegerProperty mgrId;
    private StringProperty name;
    private StringProperty date;
    private StringProperty time;
    private StringProperty location;
    private StringProperty description;

    // default constructor
    public Event() {
        this.id = new SimpleIntegerProperty();
        this.mgrId = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.date = new SimpleStringProperty();
        this.time = new SimpleStringProperty();
        this.location = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
    }

    // getter setter for variables
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getMgrId() {
        return mgrId.get();
    }

    public IntegerProperty mgrIdProperty() {
        return mgrId;
    }

    public void setMgrId(int mgrId) {
        this.mgrId.set(mgrId);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getTime() {
        return time.get();
    }

    public StringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public String getLocation() {
        return location.get();
    }

    public StringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
}
