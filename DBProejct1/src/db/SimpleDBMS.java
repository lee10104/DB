package db;

// import
import java.io.File;
import java.util.ArrayList;
import java.io.UnsupportedEncodingException;

import com.sleepycat.je.*;

public class SimpleDBMS {
    String T = "__TABLE__";
    // Environment & Database 정의
    Environment simpleDbEnv = null;
    Database simpleDb = null;
    Database dummyDb = null;
    ArrayList<Table> tableList;

    public SimpleDBMS() {
        // db env 열기
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        new File("db/").mkdir();
        simpleDbEnv = new Environment(new File("db/"), envConfig);

        // db 열기
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setAllowCreate(true);
        dbConfig.setSortedDuplicates(true);
        simpleDb = simpleDbEnv.openDatabase(null, "db", dbConfig);
        dbConfig.setSortedDuplicates(false);
        dummyDb = simpleDbEnv.openDatabase(null, "dummydb", dbConfig);
        
        // tableList 가져오기
        getAllTablesFromDb();
    }

    // db 닫기
    public void close() {
        if (simpleDb != null)
            simpleDb.close();
        if (dummyDb != null)
            dummyDb.close();
        if (simpleDbEnv != null)
            simpleDbEnv.close();
    }
    
    // tableList에서의 t의 존재 여부
    public boolean isExistingTable(String tn) {
        if (tableList == null) {
            return false;
        }

        for (Table t: tableList) {
            if (t.getName().equals(tn)) {
                return true;
            }
        }

        return false;
    }
  
    // table의 정보를 db에 저장
    public void createTable(Table t) {
        tableList.add(t);
        Cursor cursor = simpleDb.openCursor(null, null);
        DatabaseEntry key, data;
        
        try {
            key = new DatabaseEntry(T.getBytes("UTF-8"));
            data = new DatabaseEntry(t.getName().getBytes("UTF-8"));
            cursor.put(key, data);

            key = new DatabaseEntry(t.getName().getBytes("UTF-8"));
            
            for (Column c: t.getColumns()) {
                data = new DatabaseEntry(c.toString().getBytes("UTF-8"));
                cursor.put(key, data);
            }
            
            cursor.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
    // tableList에서 table을 가져와 print
    public void printTable(String tn) {
        if (tableList == null) {
            getAllTablesFromDb();
        }
        
        Table t = getTable(tn);
        if (t.getName().equals(tn)) {
            PrintMessages.printLine();
            System.out.println("table_name [" + tn + "]");
            System.out.println("column_name\ttype\tnull\tkey");
            for (Column c: t.getColumns()) {
                System.out.println(c.printColumn());
            }
            PrintMessages.printLine();
        }
    }
        
    // tableList에서 table를 삭제하고 db에서도 삭제
    public boolean dropTable(String tn) {
        boolean isDeleted = false;
        Cursor cursor = simpleDb.openCursor(null, null);
        
        try {
            DatabaseEntry tableKey = new DatabaseEntry(T.getBytes("UTF-8"));
            DatabaseEntry tableName = new DatabaseEntry();
            cursor.getSearchKey(tableKey, tableName, LockMode.DEFAULT);
            
            Table t = getTable(tn);
            tableList.remove(t);
            
            do {
                String tableNameStr = new String(tableName.getData(), "UTF-8");
                
                if (tableNameStr.equals(tn)) {
                    cursor.delete();
                    isDeleted = true;
                    
                    Cursor newCursor = simpleDb.openCursor(null, null);
                    try {
                        DatabaseEntry columnKey = new DatabaseEntry(tn.getBytes("UTF-8"));
                        DatabaseEntry columnName = new DatabaseEntry();
                        newCursor.getSearchKey(columnKey, columnName, LockMode.DEFAULT);
                        do {
                            newCursor.delete();
                        } while (newCursor.getNextDup(columnKey, columnName, LockMode.DEFAULT) == OperationStatus.SUCCESS);
                        
                        newCursor.close();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    break;
                }

            } while (cursor.getNextDup(tableKey, tableName, LockMode.DEFAULT) == OperationStatus.SUCCESS);
            
            cursor.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return isDeleted;
    }
    
    // tableList에서 table을 가져옴
    public Table getTable(String tn) {
        for (Table t: tableList) {
            if (t.getName().equals(tn)) {
                return t;
            }
        }
        
        return null;
    }
    
    // db에서 table 정보를 읽어옴
    public Table getTableFromDb(String tn) {
        Cursor cursor = simpleDb.openCursor(null, null);
        
        Table t = new Table(tn);
        
        try {
            DatabaseEntry key = new DatabaseEntry(tn.getBytes("UTF-8"));                   

            DatabaseEntry column = new DatabaseEntry();
            cursor.getSearchKey(key, column, LockMode.DEFAULT);
            do {
                if (column == null) {
                    throw new ErrorException(Flags.NOTHING);
                }
                
                try {
                    String cs = new String(column.getData(), "UTF-8");
                    Column c = stringToColumn(cs);
                    t.addColumn(c);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            } while (cursor.getNextDup(key, column, LockMode.DEFAULT) == OperationStatus.SUCCESS);
            
            cursor.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return t;
    }
    
    // db에 들어있는 table 정보를 모두 tableList로 읽어옴
    public void getAllTablesFromDb() {
        tableList = new ArrayList<Table>();
        Cursor cursor = simpleDb.openCursor(null, null);
        
        try {
            DatabaseEntry foundKey = new DatabaseEntry(T.getBytes("UTF-8"));
            DatabaseEntry foundData = new DatabaseEntry();
            cursor.getFirst(foundKey, foundData, LockMode.DEFAULT);
            
            do {            
                if (foundData.getData() == null) {
                    break;
                }
                String tableName = new String(foundData.getData(), "UTF-8");
                tableList.add(getTableFromDb(tableName));
            } while (cursor.getNextDup(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS);
            
            cursor.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
    // tableList에서 table 이름을 모두 print함
    public void showTables() {
        if (tableList == null) {
            getAllTablesFromDb();
        }
        
        if (tableList.isEmpty()) {
            throw new ErrorException(Flags.SHOW_TABLES_NO_TABLE);
        }
        PrintMessages.printLine();
        for (Table t: tableList) {
            System.out.println(t.getName());
        }
        PrintMessages.printLine();
    }
    
    public String compareColumnLists(ArrayList<String> columnList1, ArrayList<Column> columnList2) {
        String columnName = null;
        for (String c1: columnList1) {
            boolean flag = true;
            for (Column c2: columnList2) {
                if (c1.equals(c2.getName())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                columnName = c1;
            }
        }
        return columnName;
    }

    public Column stringToColumn(String str) {
        String[] arr = str.split("\t");

        DataType d;
        if (arr[1].equals("int")) {
            d = new DataType(Flags.INT, 0);
        } else if (arr[1].equals("date")) {
            d = new DataType(Flags.DATE, 0);
        } else {
            int s = arr[1].indexOf('(');
            int e = arr[1].indexOf(')');

            d = new DataType(Flags.CHAR, Integer.parseInt(arr[1].substring(s+ 1, e)));
        }

        boolean isNull;
        if (arr[2].equals("Y")) {
            isNull = true;
        } else {
            isNull = false;
        }
        
        boolean isReferenced;
        if (arr[3].equals("Y")) {
            isReferenced = true;
        } else {
            isReferenced = false;
        }

        boolean pk = false, fk = false;
        Table rt = null;
        Column rc = null;
        if (arr.length > 4) {
            if (arr[3].equals("PRI/FOR")) {
                pk = true;
                fk = true;
            } else if (arr[4].equals("FOR")) {
                fk = true;
            } else if (arr[4].equals("PRI")) {
                pk = true;
            }
        }
        if (fk) {
            rt = getTable(arr[5]);
            for (Column c: rt.getColumns()) {
                if (c.getName().equals(arr[6])) {
                    rc = c;
                }
            }
        }

        Column c = new Column(arr[0], d, isNull);
        c.setIsForeignKey(fk);
        c.setIsPrimaryKey(pk);
        c.setIsReferenced(isReferenced);
        if (fk && rt != null && rc != null) {
            c.setForeignKey(rt, rc);
        }

        return c;
    }
}