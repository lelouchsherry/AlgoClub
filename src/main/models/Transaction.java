package main.models;

import javafx.beans.property.*;

// Transaction Model
public class Transaction {
    // class member variables
    private IntegerProperty transId;
    private DoubleProperty cost;
    private StringProperty date;
    private StringProperty purpose;

    public Transaction() {
        this.transId = new SimpleIntegerProperty();
        this.cost = new SimpleDoubleProperty();
        this.date = new SimpleStringProperty();
        this.purpose = new SimpleStringProperty();
    }

    // getter setter for variables
    public int getTransId() {
        return transId.get();
    }

    public IntegerProperty transIdProperty() {
        return transId;
    }

    public void setTransId(int transId) {
        this.transId.set(transId);
    }

    public double getCost() {
        return cost.get();
    }

    public DoubleProperty costProperty() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost.set(cost);
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

    public String getPurpose() {
        return purpose.get();
    }

    public StringProperty purposeProperty() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose.set(purpose);
    }
}
