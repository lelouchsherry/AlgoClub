package main;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

public class DatabaseConnection {
    // Declare JDBC Driver
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    // Connection
    public static Connection databaseLink = null;
    // Connection credentials
    private static final String DATABASE_NAME = "algoclub";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD ="toor";
    private static final String URL_LINK = "jdbc:mysql://localhost:3306/" + DATABASE_NAME;

    // Connect to Database
    public static Connection getConnection() {
        // Establish the Connection to MySQL database using credentials
        try {
            Class.forName(JDBC_DRIVER);
            databaseLink = DriverManager.getConnection(URL_LINK, DATABASE_USER, DATABASE_PASSWORD);
        } catch (Exception e){
            e.printStackTrace();
        }
        return  databaseLink;
    }

    //Close Connection
    public static void dbDisconnect() throws SQLException {
        try {
            if (databaseLink != null && !databaseLink.isClosed()) {
                databaseLink.close();
            }
        } catch (Exception e){
            throw e;
        }
    }

    //DB Execute Query Operation
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException {
        //Declare statement, resultSet and CachedResultSet as null
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSet crs = null;
        try {
            //Connect to DB (Establish Database Connection)
            getConnection();
            //Create statement
            stmt = databaseLink.createStatement();
            //Execute select (query) operation
            resultSet = stmt.executeQuery(queryStmt);
            //CachedRowSet Implementation
            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                //Close resultSet
                resultSet.close();
            }
            if (stmt != null) {
                //Close Statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
        //Return CachedRowSet
        return crs;
    }

    //DB Execute Update (For Update/Insert/Delete) Operation
    public static void dbExecuteUpdate(String sqlStmt) throws SQLException {
        //Declare statement as null
        Statement stmt = null;
        try {
            //Connect to DB (Establish Database Connection)
            getConnection();
            //Create Statement
            stmt = databaseLink.createStatement();
            //Run executeUpdate operation with given sql statement
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeUpdate operation : " + e);
            throw e;
        } finally {
            if (stmt != null) {
                //Close statement
                stmt.close();
            }
            //Close connection
            dbDisconnect();
        }
    }
}
