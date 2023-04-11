package main.otherPage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.DatabaseConnection;
import main.models.Student;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AttendeeDAO {

    //*******************************
    //SELECT Attendees based on eventID
    //*******************************
    public static ObservableList<Student> displayAttendees(int evtID) throws SQLException {

        //Select statement sent to the DB
        String selectStatement = "SELECT s.Student_ID, s.Student_Name, s.Student_Email" +
                " FROM student s JOIN participant p" +
                " ON s.Student_ID = p.Student_ID" +
                " WHERE p.Event_ID = " + evtID +
                " ORDER BY s.Student_Name";

        try {
            ResultSet rsStud = DatabaseConnection.dbExecuteQuery(selectStatement);

            ObservableList<Student> studList = getAttendeeList(rsStud);

            return studList;
        }
        catch (SQLException e){
            throw e;
        }
    }

    //*******************************
    //SELECT Available Attendees based on eventID
    //*******************************
    public static ObservableList<Student> getAvailAttendees(int evtID) throws SQLException {

        //Select statement sent to the DB
        String selectStatement = "SELECT Student_ID, Student_Name, Student_Email FROM student " +
                "WHERE Student_ID NOT IN " +
                "(SELECT Student_ID FROM participant WHERE Event_ID = "+ evtID +") " +
                "ORDER BY Student_Name";
        try {
            ResultSet rsStud = DatabaseConnection.dbExecuteQuery(selectStatement);

            ObservableList<Student> studList = getAttendeeList(rsStud);

            return studList;
        }
        catch (SQLException e){
            throw e;
        }
    }

    private static ObservableList<Student> getAttendeeList(ResultSet rs) throws SQLException {

        ObservableList<Student> studList = FXCollections.observableArrayList();

        while (rs.next()) {
            Student stud = new Student();
            stud.setStudentID(rs.getInt("Student_ID"));
            stud.setStudentName(rs.getString("Student_Name"));
            stud.setStudentEmail(rs.getString("Student_Email"));

            studList.add(stud);
        }
        return studList;
    }

    //*************************************
    //ADD an Attendee
    //*************************************
    public static void addAttendee (int eventID, int studentID) throws SQLException {
        //Declare a INSERT statement
        String updateStmt = "INSERT INTO participant(Event_ID, Student_ID) " +
                "VALUES('"+ eventID +"','" + studentID +"')";

        //Execute INSERT operation
        try {
            DatabaseConnection.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while INSERT Operation: " + e);
            throw e;
        }
    }

    //*************************************
    //DELETE an Attendee
    //*************************************
    public static void deleteWithIds (int evtID, int studID) throws SQLException {
        //Declare a DELETE statement
        String updateStmt = "DELETE FROM participant WHERE Event_ID =" + evtID +
                " AND Student_ID =" + studID;

        //Execute UPDATE operation
        try {
            DatabaseConnection.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }
}
