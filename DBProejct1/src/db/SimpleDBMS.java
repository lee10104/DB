package db;

// import
import java.io.File;
import java.util.ArrayList;
import java.io.UnsupportedEncodingException;

import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
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
        // tableList = getAllTables();
    }

    public void close() {
        if (simpleDb != null)
            simpleDb.close();
        if (dummyDb != null)
            dummyDb.close();
        if (simpleDbEnv != null)
            simpleDbEnv.close();
    }
    
    public boolean isExistingTable(String tn) {
        boolean isExist;
        Cursor cursor = simpleDb.openCursor(null, null);
        
        try {
            DatabaseEntry foundKey = new DatabaseEntry(T.getBytes("UTF-8"));
            DatabaseEntry foundData = new DatabaseEntry();

            cursor.getFirst(foundKey, foundData, LockMode.DEFAULT);

            if (foundData.getData() == null)
                isExist = false;
            else
                isExist = true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            isExist = false;
        }
        
        cursor.close();
        
        return isExist;
    }

    public Table createTable(String tn) {
        Table t = new Table(tn);
        
        Cursor cursor = simpleDb.openCursor(null, null);
        
        try {
            DatabaseEntry key = new DatabaseEntry(T.getBytes("UTF-8"));
            DatabaseEntry data = new DatabaseEntry(tn.getBytes("UTF-8"));
            cursor.put(key, data);
            cursor.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            t = null;
        }

        return t;
    }
    
    public void updateTable(Table t) {
        Cursor cursor = simpleDb.openCursor(null, null);
        StoredClassCatalog catalog = new StoredClassCatalog(dummyDb);
        SerialBinding<Column> binding = new SerialBinding<Column>(catalog, Column.class);
        
        try {
            DatabaseEntry key = new DatabaseEntry(t.getName().getBytes("UTF-8"));
            DatabaseEntry data = new DatabaseEntry();
            
            for (Column c: t.getColumns()) {
                System.out.println("Column " + c.getName() + " to entry");
                binding.objectToEntry(c, data);
                cursor.put(key, data);
            }
            
            cursor.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
    public void printTable(String tn) {
        for (Table t: tableList) {
            if (t.getName().equals(tn)) {
                PrintMessages.printLine();
                System.out.println("table_name [" + tn + "]");
                System.out.println("column_name\ttype\tnull\tkey");
                for (Column c: t.getColumns()) {
                    System.out.println(c.toString());
                }
                PrintMessages.printLine();
                break;
            }
        }
    }
    
    public boolean dropTable(String tn) {
        boolean isDeleted = false;
        Cursor cursor = simpleDb.openCursor(null, null);
        
        try {
            DatabaseEntry foundKey = new DatabaseEntry(T.getBytes("UTF-8"));
            DatabaseEntry foundData = new DatabaseEntry();
            cursor.getFirst(foundKey, foundData, LockMode.DEFAULT);
            
            for (Table t: tableList) {
                if (t.getName().equals(tn)) {
                    tableList.remove(t);
                    break;
                }
            }
            
            do {
                String dataString = new String(foundData.getData(), "UTF-8");
                
                if (dataString.equals(tn)) {
                    cursor.delete();
                    isDeleted = true;
                    break;
                }
            } while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS);
            
            cursor.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return isDeleted;
    }
    
    public Table getTable(String tn) {
        Cursor cursor = simpleDb.openCursor(null, null);
        StoredClassCatalog catalog = new StoredClassCatalog(dummyDb);
        SerialBinding<Column> binding = new SerialBinding<Column>(catalog, Column.class);
        
        Table t = new Table(tn);
        
        try {
            DatabaseEntry foundKey = new DatabaseEntry(tn.getBytes("UTF-8"));
            DatabaseEntry foundData = new DatabaseEntry();
            cursor.getFirst(foundKey, foundData, LockMode.DEFAULT);
            do {
                if (foundData == null)
                    throw new ErrorException(Flags.NOTHING);
                
                Column c = (Column) binding.entryToObject(foundData);
                t.addColumn(c);
            } while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS);
            
            cursor.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return t;
    }
    
    public ArrayList<Table> getAllTables() {
        ArrayList<Table> tableList = new ArrayList<Table>();
        Cursor cursor = simpleDb.openCursor(null, null);
        
        try {
            DatabaseEntry foundKey = new DatabaseEntry(T.getBytes("UTF-8"));
            DatabaseEntry foundData = new DatabaseEntry();
            cursor.getFirst(foundKey, foundData, LockMode.DEFAULT);
            
            do {
                if (foundData.getData() == null) {
                    throw new ErrorException(Flags.SHOW_TABLES_NO_TABLE);
                }
                String dataString = new String(foundData.getData(), "UTF-8");
                if (isExistingTable(dataString)) {
                    tableList.add(getTable(dataString));
                }
            } while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS);
            
            cursor.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return tableList;
    }
    
    public void showTables() {
        tableList = getAllTables();
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
}
