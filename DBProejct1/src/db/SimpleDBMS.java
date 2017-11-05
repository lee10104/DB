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
    ArrayList<Table> tableList;

    public SimpleDBMS() {
        // db env 열기
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        simpleDbEnv = new Environment(new File("db/"), envConfig);

        // db 열기
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setAllowCreate(true);
        dbConfig.setSortedDuplicates(true);
        simpleDb = simpleDbEnv.openDatabase(null, "db", dbConfig);
        
        // tableList 가져오기
        tableList = getAllTables();
    }

    public void close() {
        if (simpleDb != null)
            simpleDb.close();
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
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            t = null;
        }

        return t;
    }
    
    public Table updateTable(Table t) {
        return t;
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
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return isDeleted;
    }
    
    public Table getTable(String tn) {
        Cursor cursor = simpleDb.openCursor(null, null);
        StoredClassCatalog catalog = new StoredClassCatalog(simpleDb);
        SerialBinding<Column> binding = new SerialBinding<Column>(catalog, Column.class);
        
        Table t = new Table(tn);
        
        try {
            DatabaseEntry foundKey = new DatabaseEntry(tn.getBytes("UTF-8"));
            DatabaseEntry foundData = new DatabaseEntry();
            cursor.getFirst(foundKey, foundData, LockMode.DEFAULT);
            
            do {
                if (!(binding.entryToObject(foundData) instanceof Column)) {
                    continue;
                }
                Column c = (Column) binding.entryToObject(foundData);
                t.addColumn(c);
            } while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS);
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
                String dataString = new String(foundData.getData(), "UTF-8");
                if (isExistingTable(dataString)) {
                    tableList.add(getTable(dataString));
                }
            } while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return tableList;
    }
    
    public void showTables() {
        if (tableList.isEmpty()) {
            throw new ErrorException(Flags.SHOW_TABLES_NO_TABLE);
        }
        PrintMessages.printLine();
        for (Table t: tableList) {
            System.out.println(t.getName());
        }
        PrintMessages.printLine();
    }
}
