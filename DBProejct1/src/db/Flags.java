package db;

public class Flags {
    /* 임시 flag */
    public static final int NOTHING = -1;
    
    /* 성공 */
    public static final int CREATE_TABLE_DONE = 1;
    public static final int DROP_TABLE_DONE = 2;
    public static final int DESC_DONE = 3;
    public static final int INSERT_DONE = 4;
    public static final int DELETE_DONE = 5;
    public static final int SELECT_DONE = 6;
    public static final int SHOW_TABLES_DONE = 7;

    /* 에러 */
    // syntax error
    public static final int SYNTAX_ERROR = 0;
    // create table
    public static final int DUPLICATE_COLUMN_DEF_ERROR = 8;
    public static final int REFERENCE_TYPE_ERROR = 9;
    public static final int REFERENCE_NON_PRIMARY_KEY_ERROR = 10;
    public static final int REFERENCE_COLUMN_EXISTENCE_ERROR = 11;
    public static final int REFERENCE_TABLE_EXISTENCE_ERROR = 12;
    public static final int NON_EXISTING_COLUMN_DEF_ERROR = 13;
    public static final int TABLE_EXISTENCE_ERROR = 14;
    // drop table
    public static final int DROP_REFERENCED_TABLE_ERROR = 15;
    // show table
    public static final int SHOW_TABLES_NO_TABLE = 16;
    // else
    public static final int NO_SUCH_TABLE = 17;
    public static final int CHAR_LENGTH_ERROR = 18;
    
    /* column 타입 */
    public static final int INT = 100;
    public static final int CHAR = 101;
    public static final int DATE = 102;
    public static final int FOREIGN_KEY = 103;
}
