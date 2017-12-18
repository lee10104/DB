import java.sql.SQLException;
import java.util.Scanner;

public class BookingApplication {    
    static Messages msg = new Messages();
    static ConcertManager cm = new ConcertManager();
    
    public static void main(String args[]) throws SQLException {
        Scanner reader = new Scanner(System.in);

        msg.printMenu();
        
        int result;
        
        while (true) {
            System.out.print("Select your action: ");
            int menu = reader.nextInt();
            reader.nextLine();
            
            if (menu == Menu.PRINT_BUILDINGS) {
                cm.printBuildings();
            } else if (menu == Menu.PRINT_PERFORMANCES) {
                cm.printPerformances();
            } else if (menu == Menu.PRINT_AUDIENCES) {
                cm.printAudiences();
            } else if (menu == Menu.INSERT_BUILDING) {
                String buildingName;
                String buildingLocation;
                int buildingCapacity;
                
                System.out.print("Building name: ");
                buildingName = reader.nextLine();
                System.out.print("Building location: ");
                buildingLocation = reader.nextLine();
                System.out.print("Building capacity: ");
                buildingCapacity = reader.nextInt();
                reader.nextLine();
                
                result = cm.insertBuilding(buildingName, buildingLocation, buildingCapacity);
                msg.printMessage(result);
            } else if (menu == Menu.REMOVE_BUILDING) {
                int buildingID;
                
                System.out.print("Building ID: ");
                buildingID = reader.nextInt();
                
                cm.removeBuilding(buildingID);
                msg.printMessage(Messages.BUILDING_REMOVED);
            } else if (menu == Menu.INSERT_PERFORMANCE) {
                String performanceName;
                String performanceType;
                int performancePrice;
                
                System.out.print("Performance name: ");
                performanceName = reader.nextLine();
                System.out.print("Performance type: ");
                performanceType = reader.nextLine();
                System.out.print("Performance price: ");
                performancePrice = reader.nextInt();
                
                result = cm.insertPerformance(performanceName, performanceType, performancePrice);
                msg.printMessage(result);
            } else if (menu == Menu.REMOVE_PERFORMANCE) {
                int performanceID;
                
                System.out.print("Performance ID: ");
                performanceID = reader.nextInt();
                
                cm.removePerformance(performanceID);
                msg.printMessage(Messages.PERFORMANCE_REMOVED);
            } else if (menu == Menu.INSERT_AUDIENCE) {
                String audienceName;
                String audienceGender;
                int audienceAge;
                
                System.out.print("Audience name: ");
                audienceName = reader.nextLine();
                System.out.print("Audience gender: ");
                audienceGender = reader.nextLine();
                System.out.print("Audience age: ");
                audienceAge = reader.nextInt();
                
                result = cm.insertAudience(audienceName, audienceGender, audienceAge);
                msg.printMessage(result);
            } else if (menu == Menu.REMOVE_AUDIENCE) {
                int audienceID;
                
                System.out.print("Audience ID: ");
                audienceID = reader.nextInt();
                
                cm.removeAudience(audienceID);
                msg.printMessage(Messages.AUDIENCE_REMOVED);
            } else if (menu == Menu.ASSIGN_PERFORMANCE_TO_BUILDING) {
                int buildingID;
                int performanceID;
                
                System.out.print("Building ID: ");
                buildingID = reader.nextInt();
                System.out.print("Performance ID: ");
                performanceID = reader.nextInt();
                
                cm.assign(buildingID, performanceID);
            } else if (menu == Menu.BOOK) {
                int performanceID;
                int audienceID;
                String seatNumbers;
                int price;
                
                System.out.print("Performance ID: ");
                performanceID = reader.nextInt();
                System.out.print("Audience ID: ");
                audienceID = reader.nextInt();
                System.out.print("Seat number: ");
                seatNumbers = reader.nextLine();
                
                cm.book(performanceID, audienceID, seatNumbers);
            } else if (menu == Menu.PRINT_ASSIGNED_PERFORMANCES) {
                int buildingID;
                
                System.out.print("Building ID: ");
                buildingID = reader.nextInt();
                
                cm.printAssignedPerformances(buildingID);
            } else if (menu == Menu.PRINT_BOOKED_AUDIENCE) {
                int performanceID;
                
                System.out.print("Performance ID: ");
                performanceID = reader.nextInt();
                
                cm.printBookedAudience(performanceID);
            } else if (menu == Menu.PRINT_BOOKING_STATUS) {
                int performanceID;
                
                System.out.print("Performance ID: ");
                performanceID = reader.nextInt();
                
                cm.printBookingStatus(performanceID);
            } else if (menu == Menu.QUIT) {
                reader.close();
                msg.printMessage(Messages.QUIT);
                System.exit(0);
            } else {
                msg.printMessage(Messages.INVALID_ACTION);
            }
        }
    }

}
