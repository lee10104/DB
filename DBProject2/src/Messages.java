
public class Messages {
    public static final int OTHER_ERROR = -1;
    public static final int BUILDING_INSERTED = 0;
    public static final int PERFORMANCE_INSERTED = 1;
    public static final int AUDIENCE_INSERTED = 2;
    public static final int BUILDING_REMOVED = 3;
    public static final int PERFORMANCE_REMOVED = 4;
    public static final int AUDIENCE_REMOVED = 5;
    public static final int CAPACITY_ERROR = 6;
    public static final int GENDER_ERROR = 7;
    public static final int AGE_ERROR = 8;
    public static final int PRICE_ERROR = 9;
    public static final int PERFORMANCE_ASSIGNED = 10;
    public static final int NO_BUILDING_ID = 11;
    public static final int NO_PERFORMANCE_ID = 12;
    public static final int NO_AUDIENCE_ID = 13;
    public static final int PERFORMANCE_ALREADY_ASSIGNED = 14;
    public static final int PERFORMANCE_NOT_ASSIGNED = 15;
    public static final int SEAT_NUMBER_ERROR = 16;
    public static final int BOOK_FAILED = 17;
    public static final int BOOK_SUCCEED = 18;
    public static final int QUIT = 19;
    public static final int INVALID_ACTION = 20;
    
    public void printMenu() {
        System.out.println(
                "============================================================\n" + 
                "1. print all buildings\n" + 
                "2. print all performances\n" + 
                "3. print all audiences\n" + 
                "4. insert a new building\n" + 
                "5. remove a building\n" + 
                "6. insert a new performance\n" + 
                "7. remove a performance\n" + 
                "8. insert a new audience\n" + 
                "9. remove an audience\n" + 
                "10. assign a performance to a building\n" + 
                "11. book a performance\n" + 
                "12. print all performances which assigned at a building\n" + 
                "13. print all audiences who booked for a performance\n" + 
                "14. print ticket booking status of a performance\n" + 
                "15. exit\n" + 
                "============================================================\n");
    }
    
    public void printMessage(int flag) {
        if (flag == BUILDING_INSERTED) {
            System.out.println("A building is successfully inserted");
        } else if (flag == PERFORMANCE_INSERTED) {
            System.out.println("A performance is successfully inserted");
        } else if (flag == AUDIENCE_INSERTED) {
            System.out.println("An audience is successfully inserted");
        } else if (flag == BUILDING_REMOVED) {
            System.out.println("A building is successfully removed");
        } else if (flag == PERFORMANCE_REMOVED) {
            System.out.println("A performance is successfully removed");
        } else if (flag == AUDIENCE_REMOVED) {
            System.out.println("An audience is successfully removed");
        } else if (flag == CAPACITY_ERROR) {
            System.out.println("Capacity should be larger than 0");
        } else if (flag == GENDER_ERROR) {
            System.out.println("Gender should be \'M\' or \'F\'");
        } else if (flag == AGE_ERROR) {
            System.out.println("Age should be more than 0");
        } else if (flag == PRICE_ERROR) {
            System.out.println("Price should be 0 or more");
        } else if (flag == PERFORMANCE_ASSIGNED) {
            System.out.println("Successfully assign a performance");
        } else if (flag == SEAT_NUMBER_ERROR) {
            System.out.println("Seat number out of range");
        } else if (flag == BOOK_FAILED) {
            System.out.println("The seat is already taken");
        } else if (flag == QUIT) {
            System.out.println("Bye!");
        } else if (flag == INVALID_ACTION) {
            System.out.println("Invalid action");
        } else if (flag == OTHER_ERROR) {
            System.out.println("Unknown error");
        }
    }
    
    public void printMessage(int flag, int num) {
        if (flag == NO_BUILDING_ID) {
            System.out.println("Building " + num + " doesn’t exist");
        } else if (flag == NO_PERFORMANCE_ID) {
            System.out.println("Performance "+ num + " doesn’t exist");
        } else if (flag == NO_AUDIENCE_ID) {
            System.out.println("Audience " + num + " doesn’t exist");
        } else if (flag == PERFORMANCE_ALREADY_ASSIGNED) {
            System.out.println("Performance " + num + " is already assigned");
        } else if (flag == PERFORMANCE_NOT_ASSIGNED) {
            System.out.println("Performance " + num + " isn't assigned");
        } else if (flag == BOOK_SUCCEED) {
            System.out.println(
                    "Successfully book a performance\n" + 
                    "Total ticket price is " + 
                    num);
        }
    }
}
