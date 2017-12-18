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
    
    public boolean isExist(String table, int id) {
        try {
            String sql = "SELECT count(id) FROM " + table + " WHERE id = " + id;
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            int count = -1;
            if (rs.next()) {
                count = rs.getInt("count(id)");
            }
            
            if (count > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            return false;
        }
    }
    
    public boolean removeByID(String table, int id) {
        try {
            String sql = "DELETE FROM " + table + " WHERE id = " + id;
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
    
    public int getPrice(int pID, int aID, String s) {
        String[] seatNumbers = s.replace("\\s+", "").split(",");
        
        try {
            int age = 0;
            int price = 0;
            
            String sql = "SELECT age FROM audience WHERE id = " + aID;
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                age = rs.getInt("age");
            }
            
            sql = "SELECT price FROM performance WHERE id = " + pID;
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                price = rs.getInt("price");
            }
            
            if (age <= 7) {
                price = 0;
            } else if (age <= 12) {
                price = (int) Math.round((float) price * 0.5);
            } else if (age <= 18) {
                price = (int) Math.round((float) price * 0.8);
            }
            
            return price * seatNumbers.length;
        } catch (SQLException e) {
            e.printStackTrace();
            
            return -1;
        }
    }
    
    public void printBuildings() {
        try {
            String sql = "SELECT * FROM building";
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
        try {
            String sql = "SELECT * FROM performance";
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
                
                String sql_ = "SELECT COUNT(number) FROM seat WHERE performance = " + id + " and audience is not null";
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
        try {
            String sql = "SELECT * FROM audience";
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
        if (capacity <= 0)
            return Messages.CAPACITY_ERROR;
        
        try {
            String sql = "INSERT INTO building VALUES(null, ?, ?, ?)";
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
        if (price < 0) {
            return Messages.PRICE_ERROR;
        }
        
        try {
            String sql = "INSERT INTO performance VALUES(null, ?, ?, ?, null)";
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
        if (!gender.equals("M") && !gender.equals("F")) {
            return Messages.GENDER_ERROR;
        }
        
        if (age <= 0) {
            return Messages.AGE_ERROR;
        }
        
        try {
            String sql = "INSERT INTO audience VALUES(null, ?, ?, ?)";
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
    
    public int assign(int bID, int pID) {
        if (!isExist("building", bID)) {
            return Messages.NO_BUILDING_ID;
        }
        
        if (!isExist("performance", pID)) {
            return Messages.NO_PERFORMANCE_ID;
        }
        
        try {
            String sql = "SELECT building FROM performance WHERE id = " + pID;
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("building");
                if (id > 0) {
                    return Messages.PERFORMANCE_ALREADY_ASSIGNED;
                }
            }

            sql = "UPDATE performance SET building = " + bID + " WHERE id = " + pID;
            stmt = con.prepareStatement(sql);
            stmt.executeUpdate();
            
            stmt.close();
            
            sql = "SELECT capacity FROM building WHERE id = " + bID;
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            int capacity = 0;
            if (rs.next()) {
                capacity = rs.getInt("capacity");
            }
            
            stmt.close();
            
            sql = "INSERT into seat VALUES(" + pID + ", ?, null)";
            stmt = con.prepareStatement(sql);
            for (int i = 1; i <= capacity; i++) {
                stmt.setInt(1, i);
                stmt.executeUpdate();
            }
            
            stmt.close();
            
            return Messages.PERFORMANCE_ASSIGNED;
        } catch (SQLException e) {
            e.printStackTrace();
            
            return Messages.OTHER_ERROR;
        }
    }
    
    public int printAssignedPerformances(int bID) {
        if (!isExist("building", bID)) {
            return Messages.NO_BUILDING_ID;
        }
        
        try {
            String sql = "SELECT * FROM performance WHERE building = " + bID;
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
                
                String sql_ = "SELECT COUNT(number) FROM seat WHERE performance = " + id + " and audience is not null";
                PreparedStatement stmt_ = con.prepareStatement(sql_);
                ResultSet rs_ = stmt_.executeQuery();
                
                int booked = 0;
                if (rs_.next()) {
                    booked = rs_.getInt("count(number)");
                }
                
                System.out.println(id + "\t" + name + "\t" + type + "\t\t" + price + "\t\t" + booked);
            }
            printLine();
            return Messages.SUCCEED;
        } catch (SQLException e) {
            e.printStackTrace();
            
            return Messages.OTHER_ERROR;
        }
    }
    
    public int book(int pID, int aID, String s) {
        String[] seatNumbers = s.replaceAll("\\s+", "").split(",");
        
        if (!isExist("performance", pID)) {
            return Messages.NO_PERFORMANCE_ID;
        }
        
        try {
            String sql = "SELECT count(number) FROM seat WHERE performance = " + pID;
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            int capacity = 0;
            if (rs.next()) {
                capacity = rs.getInt("count(number)");
            }
            
            for (String seatNumber: seatNumbers) {
                if (Integer.parseInt(seatNumber) > capacity) {
                    return Messages.SEAT_NUMBER_ERROR;
                }
                
                sql = "SELECT audience FROM seat WHERE performance = " + pID + " and number = " + seatNumber;
                stmt = con.prepareStatement(sql);
                rs = stmt.executeQuery();
                
                if (rs.next()) {
                    if (rs.getInt("audience") > 0) {
                        return Messages.BOOK_FAILED;
                    }
                }
            }
            
            for (String seatNumber: seatNumbers) {
                sql = "UPDATE seat SET audience = " + aID + " WHERE performance = " + pID + " and number = " + seatNumber;
                stmt = con.prepareStatement(sql);
                stmt.executeUpdate();
            }
            
            return Messages.BOOK_SUCCEED;
        } catch (SQLException e) {
            e.printStackTrace();
            
            return Messages.OTHER_ERROR;
        }
    }
    
    public int printBookedAudience(int pID) {
        if (!isExist("performance", pID)) {
            return Messages.NO_PERFORMANCE_ID;
        }
        
        try {
            String sql = "SELECT distinct id, name, gender, age FROM audience, seat WHERE seat.performance = " + pID + " and seat.audience = audience.id";
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
            printLine();

            return Messages.SUCCEED;
        } catch (SQLException e) {
            e.printStackTrace();
            
            return Messages.OTHER_ERROR;
        }
    }
    
    public int printBookingStatus(int pID) {
        if (!isExist("performance", pID)) {
            return Messages.NO_PERFORMANCE_ID;
        }
        
        try {
            String sql = "SELECT * FROM seat WHERE performance = " + pID;
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            printLine();
            System.out.println("seat_number\t\t\taudience_id");
            printLine();
            while (rs.next()) {
                int seat_number = rs.getInt("number");
                int audience_id = rs.getInt("audience");
                
                if (audience_id > 0) {
                    System.out.println(seat_number + "\t\t\t" + audience_id);
                } else {
                    System.out.println(seat_number);
                }
            }
            printLine();
            
            return Messages.SUCCEED;
        } catch (SQLException e) {
            e.printStackTrace();
            
            return Messages.OTHER_ERROR;
        }
    }
}
