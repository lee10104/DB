import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConcertManager {
    static String serverName = "147.46.15.147";
    static String dbName = "db2013-11422";
    static String userName = "u2013-11422";
    static String password = "a5716f255ddf";
    
    static String url = "jdbc:mariadb://" + serverName + "/" + dbName;
    
    Messages msg = new Messages();
    
    public static void main(String args[]) throws SQLException {
        Connection con = DriverManager.getConnection(url, userName, password);
    }
}
