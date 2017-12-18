import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ConcertManager {
    private String serverName;
    private String dbName;
    private String userName;
    private String password;
    private String url;
    private Connection con;
    
    public ConcertManager() {
        this.serverName = "147.46.15.147";
        this.dbName = "db2013-11422";
        this.userName = "u2013-11422";
        this.password = "a5716f255ddf";

        this.url = "jdbc:mariadb://" + this.serverName + "/" + this.dbName;
        
        try {
            this.con = DriverManager.getConnection(this.url, this.userName, this.password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void printLine() {
        System.out.println("--------------------------------------------------------------------------------");
    }
    
    public void printBuildingColumn() {
        System.out.println("id\tname\t\t\tlocation\tcapacity\tassigned");
    }
    
    public void printPerformanceColumn() {
        System.out.println("id\tname\t\t\ttype\tprice\tbooked");
    }
    
    public void printAudienceColumn() {
        System.out.println("id\tname\t\t\tgender\tage");
    }
    
    public void printBuildings() {
        String sql = "SELECT * FROM building";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            printLine();
            printBuildingColumn();
            printLine();           
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String location = rs.getString("location");
                int capacity = rs.getInt("capacity");
                
                String sql_ = "SELECT COUNT(id) FROM performance WHERE building = " + id;
                PreparedStatement stmt_ = con.prepareStatement(sql_);
                ResultSet rs_ = stmt_.executeQuery();

                int assign = 0;
                if (rs_.next()) {
                    assign = rs_.getInt("count(id)");
                }
                
                System.out.println(id + "\t" + name + "\t" + location + "\t\t" + capacity + "\t\t" + assign);
            }
            printLine();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void printPerformances() {

    }
    
    public void printAudiences() {
        
    }
  
    public void insertBuilding(String name, String location, int capacity) {
        String sql = "INSERT INTO building VALUES(null, ?, ?, ?)";
        
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            
            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.setInt(3, capacity);
            
            int success = stmt.executeUpdate();
            
            
            stmt.close();            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void removeBuilding(int bID) {
        
    }
    
    public void insertPerformance(String name, String type, int price) {
        
    }
    
    public void removePerformance(int pID) {
        
    }
    
    public void insertAudience(String name, String gender, int age) {
        
    }
    
    public void removeAudience(int aID) {
        
    }
    
    public void assign(int bID, int pID) {
        
    }
    
    public void printAssignedPerformances(int bID) {
        
    }
    
    public void book(int pID, int aID, String s) {
        String[] seatNumbers = s.split(", ");
    }
    
    public void printBookedAudience(int pID) {
        
    }
    
    public void printBookingStatus(int pID) {
        
    }
}
