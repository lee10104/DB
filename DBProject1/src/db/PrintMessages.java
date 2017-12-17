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
	            System.out.println("The row is inserted");
	            break;
	        case Flags.DELETE_DONE:
	            System.out.println(str + " row(s) are deleted");
	            break;
	        case Flags.SELECT_DONE:
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
	            System.out.println("There is no table");
	            break;
	        case Flags.NO_SUCH_TABLE:
	            System.out.println("No such table");
	            break;
	        case Flags.CHAR_LENGTH_ERROR:
	            System.out.println("Char length should be over 0");
	            break;
	        case Flags.INSERT_DUPLICATE_PRIMARY_KEY_ERROR:
	            System.out.println("Insertion has failed: Primary key duplication");
	            break;
	        case Flags.INSERT_REFERENTIAL_INTEGRITY_ERROR:
	            System.out.println("Insertion has failed: Referential integrity violation");
	            break;
	        case Flags.INSERT_TYPE_MISMATCH_ERROR:
	            System.out.println("Insertion has failed: Types are not matched");
	            break;
	        case Flags.INSERT_COLUMN_EXISTENCE_ERROR:
	            System.out.println("Insertion has failed: \'" + str + "\' does not exist");
	            break;
	        case Flags.INSERT_COLUMN_NON_NULLABLE_ERROR:
	            System.out.println("Insertion has failed: \'" + str + "\' is not nullable");
	            break;
	        case Flags.DELETE_REFERENTIAL_INTEGRITY_PASSED:
	            System.out.println(str + " row(s) are not deleted due to referential integrity");
	            break;
	        case Flags.SELECT_TABLE_EXISTENCE_ERROR:
	            System.out.println("Selection has failed: \'" + str + "\' does not exist");
	            break;
	        case Flags.SELECT_COLUMN_RESOLVE_ERROR:
	            System.out.println("Selection has failed: fail to resolve \'" + str + "\'");
	            break;
	        case Flags.WHERE_INCOMPARABLE_ERROR:
	            System.out.println("Where clause try to compare incomparable values");
	            break;
	        case Flags.WHERE_TABLE_NOT_SPECIFIED:
	            System.out.println("Where clause try to reference tables which are not specified");
	            break;
	        case Flags.WHERE_COLUMN_NOT_EXIST:
	            System.out.println("Where clause try to reference non existing column");
	            break;
	        case Flags.WHERE_AMBIGUOUS_REFERENCE:
	            System.out.println("Where clause contains ambiguous reference");
	        default:
	            break;
	    }
	}
	
	public static void printLine() {
	    System.out.println("----------------");
	}
}
