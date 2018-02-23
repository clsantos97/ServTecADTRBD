package app.config.db;

import com.mysql.jdbc.Connection;
import java.sql.*;
import java.sql.DriverManager;
/**
 * @desc A singleton database access class for MySQL
 * @author Ramindu
 */
public final class DbManager {
    private Connection conn;
    private Statement statement;
    public static DbManager db;
    
    private DbManager() {
        String url= "jdbc:mysql://localhost:3306/";
        String dbName = "servtecrdb";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "ubuntu";
        try {
            Class.forName(driver).newInstance();
            this.conn = (Connection)DriverManager.getConnection(url+dbName,userName,password);
        }
        catch (Exception sqle) {
            sqle.printStackTrace();
        }
    }
    /**
     *
     * @return MysqlConnect Database connection object
     */
    public static synchronized DbManager getDbConn() {
        if ( db == null ) {
            db = new DbManager();
        }
        return db;
 
    }
    /**
     *
     * @param query String The query to be executed
     * @return a ResultSet object containing the results or null if not available
     * @throws SQLException
     */
    public ResultSet query(String query) throws SQLException{
        setStatement(db.getConn().createStatement());
        ResultSet res = getStatement().executeQuery(query);
        return res;
    }
    /**
     * @desc Method to insert data to a table
     * @param insertQuery String The Insert query
     * @return boolean
     * @throws SQLException
     */
    public int insert(String insertQuery) throws SQLException {
        setStatement(db.getConn().createStatement());
        int result = getStatement().executeUpdate(insertQuery);
        return result;
 
    }

    /**
     * @return the conn
     */
    public Connection getConn() {
        return conn;
    }

    /**
     * @param conn the conn to set
     */
    public void setConn(Connection conn) {
        this.conn = conn;
    }

    /**
     * @return the statement
     */
    public Statement getStatement() {
        return statement;
    }

    /**
     * @param statement the statement to set
     */
    public void setStatement(Statement statement) {
        this.statement = statement;
    }
    
    
 
}
