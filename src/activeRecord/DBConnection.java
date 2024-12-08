package activeRecord;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    // variables de connection
    String userName = "root";
    String password = "roi_morin";
    String serverName = "localhost";
    String portNumber = "3306";
    static String dbName = "testpersonne";

    private static Connection instance = null;

    private DBConnection() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", userName);
        props.setProperty("password", password);
        String urlDB = "jdbc:mysql://" + serverName + ":" + portNumber + "/" + dbName;

        instance = DriverManager.getConnection(urlDB, props);
    }

    public static synchronized Connection getConnection() {
        if (instance == null) {
            try {
                new DBConnection();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return instance;
    }

    public static synchronized void setNomDB(String nomDB) {
        instance = null;
        dbName = nomDB;
    }
}
