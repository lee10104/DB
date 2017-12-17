import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class ConcertManager {
    static String serverName = "147.46.15.147";
    static String dbName = "db2013-11422";
    static String userName = "u2013-11422";
    static String password = "a5716f255ddf";
    
    static String url = "jdbc:mariadb://" + serverName + "/" + dbName;
    
    static Messages msg = new Messages();
    
    public static void main(String args[]) throws SQLException {
        Scanner reader = new Scanner(System.in);
        Connection con = DriverManager.getConnection(url, userName, password);
        
        msg.printMenu();
        
        while (true) {
            System.out.print("Select your action: ");
            int menu = reader.nextInt();
            
            if (menu == Menu.PRINT_BUILDINGS) {
                
            } else if (menu == Menu.PRINT_PERFORMANCES) {
                
            } else if (menu == Menu.PRINT_AUDIENCES) {
                
            } else if (menu == Menu.INSERT_BUILDING) {
                
            } else if (menu == Menu.REMOVE_BUILDING) {
                
            } else if (menu == Menu.INSERT_PERFORMANCE) {
                
            } else if (menu == Menu.REMOVE_PERFORMANCE) {
                
            } else if (menu == Menu.INSERT_AUDIENCE) {
                
            } else if (menu == Menu.REMOVE_AUDIENCE) {
                
            } else if (menu == Menu.ASSIGN_PERFORMANCE_TO_BUILDING) {
                
            } else if (menu == Menu.BOOK) {
                
            } else if (menu == Menu.PRINT_ASSIGNED_PERFORMANCES) {
                
            } else if (menu == Menu.PRINT_BOOKED_AUDIENCE) {
                
            } else if (menu == Menu.PRINT_BOOKING_STATUS) {
                
            } else if (menu == Menu.QUIT) {
                msg.printMessage(Messages.QUIT);
            } else {
                msg.printMessage(Messages.INVALID_ACTION);
            }
        }
    }

}
