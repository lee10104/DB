import java.sql.Connection;
import java.sql.DriverManager;
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
    
    public void printBuildings() {
        
    }
    
    public void printPerformances() {
        
    }
    
    public void printAudiences() {
        
    }
  
    public void insertBuilding(String name, String location, int capacity) {
        
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
