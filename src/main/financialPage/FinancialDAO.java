package main.financialPage;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.DatabaseConnection;
import main.Utils;
import main.models.Transaction;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FinancialDAO {
    //*******************************
    //SELECT a Transaction
    //*******************************
    public static Transaction searchTransaction (String transId) {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM financial WHERE Transaction_ID =" + transId;
        Transaction trans = null;
        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsTrans = DatabaseConnection.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getTransFromResultSet method and get event object
            trans = getTransFromResultSet(rsTrans);
        } catch (SQLException e) {
            System.out.println("While searching an event with " + transId + " id, an error occurred: " + e);
        }
        //Return transaction object
        return trans;
    }

    //Use ResultSet from DB as parameter and set Transaction Object's attributes and return transaction object.
    private static Transaction getTransFromResultSet(ResultSet rs) throws SQLException
    {
        Transaction trans = null;
        if (rs.next()) {
            trans.setTransId(rs.getInt("Transaction_ID"));
            trans.setCost(rs.getDouble("Cost"));
            trans.setDate(rs.getString("Transaction_Date"));
            trans.setPurpose(rs.getString("Transaction_Description"));
        }
        return trans;
    }

    //*******************************
    //EXPORT a Transaction
    //*******************************
    public static void exportTrans(MouseEvent event, int evtId) throws SQLException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM financial WHERE Event_ID =" + evtId
                + " ORDER BY Transaction_Date";

        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsTrans = DatabaseConnection.dbExecuteQuery(selectStmt);

            //Send ResultSet to export excel file
            getTransToFile(event, rsTrans);

        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
        }
    }

    //*******************************
    //EXPORT Transactions
    //*******************************
    public static void exportAllTrans(MouseEvent event) throws SQLException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM financial ORDER BY Event_ID";

        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsTrans = DatabaseConnection.dbExecuteQuery(selectStmt);

            //Send ResultSet to export excel file
            getTransToFile(event, rsTrans);

        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
        }
    }

    private static void getTransToFile(MouseEvent event, ResultSet rs) throws SQLException {
        // For export to excel
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transactions");
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Transaction ID");
        header.createCell(1).setCellValue("Event ID");
        header.createCell(2).setCellValue("Date");
        header.createCell(3).setCellValue("Transaction Description");
        header.createCell(4).setCellValue("Cost");

        int index = 1;
        while (rs.next()) {
            Row row = sheet.createRow(index);
            row.createCell(0).setCellValue(rs.getInt("Transaction_ID"));
            row.createCell(1).setCellValue(rs.getInt("Event_ID"));
            row.createCell(2).setCellValue(rs.getString("Transaction_Date"));
            row.createCell(3).setCellValue(rs.getString("Transaction_Description"));
            row.createCell(4).setCellValue(rs.getDouble("Cost"));
            index++;
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel");

        //Set extension filter to .xlsx files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xls)", "*.xls");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(stage);

        //If file is not null, write to file using output stream.
        if (file != null) {
            try (FileOutputStream outputStream = new FileOutputStream(file.getAbsolutePath())) {
                workbook.write(outputStream);
                Utils.showInfo("Transaction", "Transaction exported in excel sheet.");
            }
            catch (IOException ex) {
                Logger.getLogger(FinancialDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //*******************************
    //SELECT Transactions
    //*******************************
    public static ObservableList<Transaction> displayTransactions(String evtId) throws SQLException {
        //Declare a SELECT statement
        String selectStmt = "SELECT * FROM financial WHERE Event_ID = " + evtId;

        //Execute SELECT statement
        try {
            //Get ResultSet from dbExecuteQuery method
            ResultSet rsTrans = DatabaseConnection.dbExecuteQuery(selectStmt);
            //Send ResultSet to the getTransactionsList method and get transaction object
            ObservableList<Transaction> transList = getTransactionsList(rsTrans);
            //Return event list
            return transList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            //Return exception
            throw e;
        }
    }

    // populate transaction lists based on query
    private static ObservableList<Transaction> getTransactionsList(ResultSet rs) throws SQLException {
        //Declare an observable List which comprises Transaction objects
        ObservableList<Transaction> transList = FXCollections.observableArrayList();
        while (rs.next()) {
            Transaction trans = new Transaction();
            trans.setTransId(rs.getInt("Transaction_ID"));
            trans.setCost(rs.getDouble("Cost"));
            trans.setDate(rs.getString("Transaction_Date"));
            trans.setPurpose(rs.getString("Transaction_Description"));

            //Add transaction to the ObservableList
            transList.add(trans);
        }
        //return evtList (ObservableList of Events)
        return transList;
    }

    //*************************************
    //ADD a Transaction
    //*************************************
    public static void addTrans(int evtId, String purpose, double cost, String transDate) throws SQLException {
        //Declare a INSERT statement
        String updateStmt = "INSERT INTO financial(Event_ID, Transaction_Description, Cost, Transaction_Date) " +
                "VALUES('"+ evtId +"','"+ purpose +"','"+ cost +"','"+ transDate +"')";

        //Execute INSERT operation
        try {
            DatabaseConnection.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while INSERT Operation: " + e);
            throw e;
        }
    }

    //*************************************
    //UPDATE a Transaction
    //*************************************
    public static void updateTrans (int transId, String purpose, double cost, String transDate) throws SQLException {
        //Declare a UPDATE statement
        String updateStmt = "UPDATE financial SET "
                + "Transaction_Description = '"+ purpose +"',"
                + "Cost = '"+ cost +"',"
                + "Transaction_Date = '"+ transDate +"' WHERE Transaction_ID = '"+ transId +"'";

        //Execute UPDATE operation
        try {
            DatabaseConnection.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while UPDATE Operation: " + e);
            throw e;
        }
    }

    //*************************************
    //DELETE a Transaction
    //*************************************
    public static void deleteTransWithId (int transID) throws SQLException {
        //Declare a DELETE statement
        String updateStmt = "DELETE FROM financial WHERE Transaction_ID = '"+ transID +"'";
        //Execute UPDATE operation
        try {
            DatabaseConnection.dbExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Error occurred while DELETE Operation: " + e);
            throw e;
        }
    }
}
