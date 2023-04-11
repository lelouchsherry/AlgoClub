package main.eventpage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.models.Event;
import main.DatabaseConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EventDAO {
    //*******************************
    //SELECT an Event
    //*******************************
    public static Event searchEvent (String evtId) {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM event WHERE Event_ID =" + evtId;
        Event evt = null;
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEvt = DatabaseConnection.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getEventFromResultSet method and get event object
            evt = getEventFromResultSet(rsEvt);
        } catch (SQLException e) {
            System.out.println("While searching an event with " + evtId + " id, an error occurred: " + e);
        }
        //Return event object
        return evt;
    }

    //Use ResultSet from DB as parameter and set Event Object's attributes and return event object.
    private static Event getEventFromResultSet(ResultSet rs) throws SQLException
    {
        Event evt = null;
        if (rs.next()) {
            evt = new Event();
            evt.setId(rs.getInt("Event_ID"));
            evt.setMgrId(rs.getInt("Manager_ID"));
            evt.setName(rs.getString("Event_Name"));
            String date = (rs.getString("Event_Date"));
            List<String> dateList = Arrays.asList(date.split("T"));
            evt.setDate(dateList.get(0));
            evt.setTime(dateList.get(1));
            evt.setLocation(rs.getString("Event_Location"));
            evt.setDescription(rs.getString("Event_Description"));
        }
        return evt;
    }

    //*******************************
    //SELECT Events
    //*******************************
    public static ObservableList<Event> displayEvents () throws SQLException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM event";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEvt = DatabaseConnection.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getEventList method and get event object
            ObservableList<Event> evtList = getEventList(rsEvt);
            //Return event list
            return evtList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            throw e;
        }
    }

    //*******************************
    //Select Upcoming Events
    //*******************************
    public static ObservableList<Event> upcomingEvents () throws SQLException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String query = "SELECT * FROM event WHERE Event_Date > '" + formatter.format(now) +
                "' ORDER BY Event_Date";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEvt = DatabaseConnection.dbExecuteQuery(query);
            //Send ResultSet to the getEventList method and get employee object
            ObservableList<Event> evtList = getEventList(rsEvt);
            //Return event list
            return evtList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            throw e;
        }
    }

    //*******************************
    //Select Past Events
    //*******************************
    public static ObservableList<Event> pastEvents () throws SQLException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String query = "SELECT * FROM event WHERE Event_Date < '" + formatter.format(now) +
                "' ORDER BY Event_Date";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsEvt = DatabaseConnection.dbExecuteQuery(query);
            //Send ResultSet to the getEventList method and get employee object
            ObservableList<Event> evtList = getEventList(rsEvt);
            //Return event list
            return evtList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            throw e;
        }
    }

    // populate event lists based on query
    private static ObservableList<Event> getEventList(ResultSet rs) throws SQLException {
        //Declare an observable List which comprises Event objects
        ObservableList<Event> evtList = FXCollections.observableArrayList();
        while (rs.next()) {
            Event evt = new Event();
            evt.setId(rs.getInt("Event_ID"));
            evt.setMgrId(rs.getInt("Manager_ID"));
            evt.setName(rs.getString("Event_Name"));
            String date = (rs.getString("Event_Date"));
            List<String> dateList = Arrays.asList(date.split("T"));
            evt.setDate(dateList.get(0));
            evt.setTime(dateList.get(1));
            evt.setLocation(rs.getString("Event_Location"));
            evt.setDescription(rs.getString("Event_Description"));
            //Add event to the ObservableList
            evtList.add(evt);
        }
        //return evtList (ObservableList of Events)
        return evtList;
    }

    //*************************************
    //ADD an Event
    //*************************************
    public static void addEvt (int mgrId, String evtName, String evtDateTime, String evtLocation, String evtDescription) throws SQLException {
        //Declare a INSERT statement
        String updateStmt = "INSERT INTO event(Manager_ID, Event_Name, Event_Location, Event_Date, Event_Description) " +
                "VALUES('"+ mgrId +"','"+ evtName +"','"+ evtLocation +"','"+ evtDateTime +"','"+ evtDescription +"')";

        //Execute INSERT operation
        try {
            DatabaseConnection.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while INSERT Operation: " + e);
            throw e;
        }
    }

    //*************************************
    //UPDATE an Event
    //*************************************
    public static void updateEvt (int evtId, int mgrId, String evtName, String evtDateTime, String evtLocation, String evtDescription) throws SQLException {
        //Declare a UPDATE statement
        String updateStmt = "UPDATE event SET "
                + "Manager_ID = '"+ mgrId +"',"
                + "Event_Name = '"+ evtName +"',"
                + "Event_Location = '"+ evtLocation +"',"
                + "Event_Date = '"+ evtDateTime +"',"
                + "Event_Description = '"+ evtDescription +"' WHERE Event_ID = '"+ evtId +"'";

        //Execute UPDATE operation
        try {
            DatabaseConnection.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    //*************************************
    //DELETE an Event
    //*************************************
    public static void deleteEvtWithId (int evtID) throws SQLException {
        //Declare a DELETE statement
        String updateStmt = "DELETE FROM event WHERE event_id = '"+ evtID +"'";
        //Execute UPDATE operation
        try {
            DatabaseConnection.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }
}
