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
        System.out.println("id\tname\t\t\ttype\t\tprice\t\tbooked");
    }
    
    public void printAudienceColumn() {
        System.out.println("id\tname\t\t\tgender\t\tage");
    }
    
    public boolean removeByID(String table, int id) {
        String sql = "DELETE FROM " + table + " WHERE id = " + id;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            int success = stmt.executeUpdate();
            
            if (success == 0) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            return false;
        }
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
        String sql = "SELECT * FROM performance";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            printLine();
            printPerformanceColumn();
            printLine();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                int price = rs.getInt("price");
                
                String sql_ = "SELECT COUNT(number) FROM seat WHERE audience is not null";
                PreparedStatement stmt_ = con.prepareStatement(sql_);
                ResultSet rs_ = stmt_.executeQuery();
                
                int booked = 0;
                if (rs_.next()) {
                    booked = rs_.getInt("count(number)");
                }
                
                System.out.println(id + "\t" + name + "\t" + type + "\t\t" + price + "\t\t" + booked);
            }
            printLine();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void printAudiences() {
        String sql = "SELECT * FROM audience";
        
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            printLine();
            printAudienceColumn();
            printLine();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                int age = rs.getInt("age");
                
                System.out.println(id + "\t" + name + "\t\t" + gender + "\t\t" + age);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
  
    public int insertBuilding(String name, String location, int capacity) {
        String sql = "INSERT INTO building VALUES(null, ?, ?, ?)";
        
        if (capacity <= 0)
            return Messages.CAPACITY_ERROR;
        
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            
            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.setInt(3, capacity);
            
            stmt.executeUpdate();
            
            stmt.close();
            
            return Messages.BUILDING_INSERTED;
        } catch (SQLException e) {
            e.printStackTrace();
            
            return Messages.OTHER_ERROR;
        }
    }
    
    public int removeBuilding(int bID) {
        if (removeByID("building", bID)) {
            return Messages.BUILDING_REMOVED;
        } else {
            return Messages.NO_BUILDING_ID;
        }
    }
    
    public int insertPerformance(String name, String type, int price) {
        String sql = "INSERT INTO performance VALUES(null, ?, ?, ?, null)";
        
        if (price < 0) {
            return Messages.PRICE_ERROR;
        }
        
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            
            stmt.setString(1, name);
            stmt.setString(2, type);
            stmt.setInt(3, price);
            
            stmt.executeUpdate();
            
            stmt.close();
            
            return Messages.PERFORMANCE_INSERTED;
        } catch (SQLException e) {
            e.printStackTrace();
            
            return Messages.OTHER_ERROR;
        }   
    }
    
    public int removePerformance(int pID) {
        if (removeByID("performance", pID)) {
            return Messages.PERFORMANCE_REMOVED;
        } else {
            return Messages.NO_PERFORMANCE_ID;
        }
    }
    
    public int insertAudience(String name, String gender, int age) {
        String sql = "INSERT INTO audience VALUES(null, ?, ?, ?)";
        
        if (!gender.equals("M") && !gender.equals("F")) {
            return Messages.GENDER_ERROR;
        }
        
        if (age <= 0) {
            return Messages.AGE_ERROR;
        }
        
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            
            stmt.setString(1, name);
            stmt.setString(2, gender);
            stmt.setInt(3, age);
            
            stmt.executeUpdate();
            
            stmt.close();
            
            return Messages.AUDIENCE_INSERTED;
        } catch (SQLException e) {
            e.printStackTrace();
            
            return Messages.OTHER_ERROR;
        }
    }
    
    public int removeAudience(int aID) {
        if (removeByID("audience", aID)) {
            return Messages.AUDIENCE_REMOVED;
        } else {
            return Messages.NO_AUDIENCE_ID;
        }
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
