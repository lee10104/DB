package db;

public class PrintMessages {
    private int flag;
    private String str;
    
    public PrintMessages(int flag) {
        this.flag = flag;
    }
    
    public PrintMessages(int flag, String str) {
        this.flag = flag;
        this.str = str;
    }

	public void print() {
	    System.out.print("DB_2013-11422> ");

	    switch(flag)
	    {
	        case Flags.SYNTAX_ERROR:
	            System.out.println("Syntax error");
	           break;
	        case Flags.CREATE_TABLE_DONE:
	            System.out.println("\'" + str + "\' table is created");
	            break;
	        case Flags.DROP_TABLE_DONE:
	            System.out.println("\'" + str + "\' table is dropped");
	            break;
	        case Flags.DESC_DONE:
	            break;
	        case Flags.INSERT_DONE:
	            System.out.println("\'INSERT\' requested");
	            break;
	        case Flags.DELETE_DONE:
	            System.out.println("\'DELETE\' requested");
	            break;
	        case Flags.SELECT_DONE:
	            System.out.println("\'SELECT\' requested");
	            break;
	        case Flags.SHOW_TABLES_DONE:
		    	break;
	        default:
	            break;
	    }
	}
	
	public static void printLine() {
	    System.out.println("----------------");
	}
}
