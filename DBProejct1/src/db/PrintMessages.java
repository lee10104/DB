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
	        case Flags.DUPLICATE_COLUMN_DEF_ERROR:
	            System.out.println("Create table has failed: column definition is duplicated");
	            break;
	        case Flags.DUPLICATE_PRIMARY_KEY_DEF_ERROR:
	            System.out.println("Create table has failed: primary key definition is duplicated");
	            break;
	        case Flags.REFERENCE_TYPE_ERROR:
	            System.out.println("Create table has failed: foreign key references wrong type");
	            break;
	        case Flags.REFERENCE_NON_PRIMARY_KEY_ERROR:
	            System.out.println("Create table has failed: foreign key references non primary key column");
	            break;
	        case Flags.REFERENCE_COLUMN_EXISTENCE_ERROR:
	            System.out.println("Create table has failed: foreign key references non existing column");
	            break;
	        case Flags.REFERENCE_TABLE_EXISTENCE_ERROR:
	            System.out.println("Create table has failed: foreign key references non existing table");
	            break;
	        case Flags.NON_EXISTING_COLUMN_DEF_ERROR:
	            System.out.println("Create table has failed: \'" + str + "\' does not exists in column definition");
	            break;
	        case Flags.TABLE_EXISTENCE_ERROR:
	            System.out.println("Create table has failed: table with the same name already exists");
	            break;
	        case Flags.DROP_REFERENCED_TABLE_ERROR:
	            System.out.println("Drop table has failed: \'" + str + "\' is referenced by other table");
	            break;
	        case Flags.SHOW_TABLES_NO_TABLE:
	            System.out.println("No such table");
	            break;
	        case Flags.CHAR_LENGTH_ERROR:
	            System.out.println("Char length should be over 0");
	            break;
	        default:
	            break;
	    }
	}
	
	public static void printLine() {
	    System.out.println("----------------");
	}
}
