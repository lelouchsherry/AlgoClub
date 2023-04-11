package main.profilePage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.DatabaseConnection;
import main.models.Student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class StudentDAO {
    //*******************************
    //SELECT a Student
    //*******************************
    public static Student searchStudent (String studId) {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM student WHERE Student_ID =" + studId;
        Student stud = null;
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsStud = DatabaseConnection.dbExecuteQuery(selectStmt);

            stud = getStudentFromResultSet(rsStud);
        } catch (SQLException e) {
            System.out.println("While searching an event with " + studId + " id, an error occurred: " + e);
        }
        //Return event object
        return stud;
    }

    //Use ResultSet from DB as parameter and set Student Object's attributes and return student object.
    private static Student getStudentFromResultSet(ResultSet rs) throws SQLException
    {
        Student stud = null;
        if (rs.next()) {
            stud = new Student();
            stud.setStudentID(rs.getInt("Student_ID"));
            stud.setStudentName(rs.getString("Student_Name"));
            stud.setPronouns(rs.getString("Pronouns"));
            stud.setStudentAddress(rs.getString("Student_Address"));
            stud.setStudentPhone(rs.getString("Student_Phone"));
            stud.setStudentEmail(rs.getString("Student_Email"));
            stud.setGuardianName(rs.getString("Guardian_Name"));
            stud.setGuardianPhone(rs.getString("Guardian_Phone"));
            stud.setRelationship(rs.getString("Relationship"));
        }
        return stud;
    }

    public static ObservableList<Student> displayStudents () throws SQLException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM student";
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rs = DatabaseConnection.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getStudentList method and get student object
            ObservableList<Student> evtList = getStudentList(rs);
            //Return event list
            return evtList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            throw e;
        }
    }

    private static ObservableList<Student> getStudentList(ResultSet rs) throws SQLException {
        //Declare an observable List which comprises Student objects
        ObservableList<Student> studList = FXCollections.observableArrayList();
        while (rs.next()) {
            Student stud = new Student();
            stud.setStudentID(rs.getInt("Student_ID"));
            stud.setStudentName(rs.getString("Student_Name"));
            stud.setPronouns(rs.getString("Pronouns"));
            stud.setStudentAddress(rs.getString("Student_Address"));
            stud.setStudentPhone(rs.getString("Student_Phone"));
            stud.setStudentEmail(rs.getString("Student_Email"));
            stud.setGuardianName(rs.getString("Guardian_Name"));
            stud.setGuardianPhone(rs.getString("Guardian_Phone"));
            stud.setRelationship(rs.getString("Relationship"));

            studList.add(stud);
        }
        //return studList (ObservableList of Students)
        return studList;
    }

    public static void addStudent(String studentName, String pronoun, String studentAddress, String studentPhone,
                                  String studentEmail, String guardianName, String guardianPhone, String relationship) throws SQLException {
        //Declare an INSERT statement
        String insertStmt = "INSERT INTO student (Student_Name, Pronouns, Student_Address, Student_Phone, Student_Email, Guardian_Name, Guardian_Phone, Relationship) " +
                "VALUES ('" + studentName + "','" + pronoun + "','" + studentAddress + "','" + studentPhone + "','" + studentEmail + "','" + guardianName + "','" + guardianPhone + "','" + relationship + "')";

        try {
            DatabaseConnection.dbExecuteUpdate(insertStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while INSERT Operation: " + e);
            throw e;
        }
    }

    public static void deleteStudentWithId(int studentID) throws SQLException {
        //Declare a DELETE statement
        String updateStmt = "DELETE FROM student WHERE student_id = '" + studentID + "'";
        //Execute UPDATE operation
        try {
            DatabaseConnection.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }

    public static void updateStudent(int studId, String stdName, String pronoun, String stdAddress, String stdPhone,
                                     String stdEmail, String guardianName, String guardianPhone, String relationship) throws SQLException {
        //Declare a UPDATE statement
        String updateStmt = "UPDATE student SET "
                + "Student_Name = '" + stdName + "',"
                + "Pronouns = '" + pronoun + "',"
                + "Student_Address = '" + stdAddress + "',"
                + "Student_Phone = '" + stdPhone + "',"
                + "Student_Email = '" + stdEmail + "',"
                + "Guardian_Name = '" + guardianName + "',"
                + "Guardian_Phone = '" + guardianPhone + "',"
                + "Relationship = '" + relationship + "' WHERE Student_ID =" + studId;

        //Execute UPDATE operation
        try {
            DatabaseConnection.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }
}
