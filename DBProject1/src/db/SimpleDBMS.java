package db;

// import
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.io.UnsupportedEncodingException;

import com.sleepycat.je.*;
import db.where.*;

public class SimpleDBMS {
    String T = "__TABLE__";
    String R = "__RECORDS";
    String N = "__NULL__";
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
                    
                    newCursor = simpleDb.openCursor(null, null);
                    try {
                        String entry = tn + R;
                        DatabaseEntry recordKey = new DatabaseEntry(entry.getBytes("UTF-8"));
                        DatabaseEntry recordName = new DatabaseEntry();
                        newCursor.getSearchKey(recordKey, recordName, LockMode.DEFAULT);
                        do {
                            if (recordName.getData() == null) {
                                break;
                            }
                            newCursor.delete();
                        } while (newCursor.getNextDup(recordKey, recordName, LockMode.DEFAULT) == OperationStatus.SUCCESS);
                        
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
            // column 정보 읽어오기
            DatabaseEntry key = new DatabaseEntry(tn.getBytes("UTF-8"));
            DatabaseEntry column = new DatabaseEntry();
            cursor.getSearchKey(key, column, LockMode.DEFAULT);
            do {
                try {
                    String cs = new String(column.getData(), "UTF-8");
                    Column c = stringToColumn(cs);
                    t.addColumn(c);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
                
            } while (cursor.getNextDup(key, column, LockMode.DEFAULT) == OperationStatus.SUCCESS);
            
            // record 읽어오기
            String entry = tn + R;
            DatabaseEntry recordKey = new DatabaseEntry(entry.getBytes("UTF-8"));
            DatabaseEntry foundRecord = new DatabaseEntry();
            cursor.getSearchKey(recordKey, foundRecord, LockMode.DEFAULT);

            do {
                if (foundRecord.getData() == null) {
                    break;
                }
                String value = new String(foundRecord.getData(), "UTF-8");
                for (String s1: value.split("\t\t")) {
                    Record record = new Record();
                    String[] arr = s1.split("\t");
                    ArrayList<Column> cl = t.getColumns();
                    for (int i = 0; i < arr.length; i++) {
                        Value v = new Value(cl.get(i).getDataType(), arr[i]);
                        record.addValue(v);
                    }
                    t.addRecord(record);
                }
            } while (cursor.getNextDup(recordKey, foundRecord, LockMode.DEFAULT) == OperationStatus.SUCCESS);
            
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
    
    public void insertValues(String tn, ArrayList<Value> vl) {
        Table t = getTable(tn);
        
        if (t == null) {
            throw new ErrorException(Flags.NO_SUCH_TABLE);
        }
        
        ArrayList<Column> cl = t.getColumns();
        
        if (cl.size() != vl.size()) {
            throw new ErrorException(Flags.INSERT_TYPE_MISMATCH_ERROR);
        }
        
        // insert하려는 column이 유효한지 검사하고 value를 record list로 만들어 table에 저장
        for (int i = 0; i < vl.size(); i++) {
            Value v = vl.get(i);
            
            if (v.getColumnName() == null) {
                v.setColumn(cl.get(i));
            } else {
                for (Column c: cl) {
                    if (v.getColumnName().equals(c.getName())) {
                        v.setColumn(c);
                        break;
                    }
                }
            }
            if (v.getColumn() == null) {
                throw new ErrorException(Flags.INSERT_COLUMN_EXISTENCE_ERROR, v.getColumnName());
            }
            if (v.getValue() == null && !v.getColumn().getIsNull()) {
                throw new ErrorException(Flags.INSERT_COLUMN_NON_NULLABLE_ERROR, v.getColumnName());
            }
            if (v.getDataType().getType() != v.getColumn().getDataType().getType()) {
                throw new ErrorException(Flags.INSERT_TYPE_MISMATCH_ERROR);
            }
        }
        // primary key, foreign key 조사
        boolean[] isPk = new boolean[cl.size()];
        boolean[] isFk = new boolean[cl.size()];
        for (int i = 0; i < cl.size(); i++) {
            if (cl.get(i).getIsPrimaryKey()) {
                isPk[i] = true;
            } else {
                isPk[i] = false;
            }
            if (cl.get(i).getIsForeignKey()) {
                isFk[i] = true;
            } else {
                isFk[i] = false;
            }
        }
        
        // record 생성
        Record newRecord = new Record();
        for (Column c: cl) {
            for (Value v: vl) {
                if (c.getName().equals(v.getColumn().getName())) {
                    String value = v.getValue();
                    DataType dt = c.getDataType();
                    v.setDataType(dt);
                    if (dt.getType() == Flags.CHAR) {
                        if (dt.getCharLength() < value.length()) {
                            v.setValue(value.substring(0, dt.getCharLength()));
                        }
                    }
                    if (value == null) {
                        v.setValue(N);
                    }
                    newRecord.addValue(v);
                    break;
                }
            }
        }        
        // record가 primary key 조건에 부합하는지 검사
        for (Record record: t.getRecords()) {
            boolean flag = false;
            for (int i = 0; i < cl.size(); i++) {
                if (isPk[i]) {
                    if (newRecord.getValues().get(i).getValue().equals(record.getValues().get(i).getValue())) {
                        flag = true;
                    }
                }
            }
            if (flag) {
                throw new ErrorException(Flags.INSERT_DUPLICATE_PRIMARY_KEY_ERROR);
            }
        }
        // record가 foreign key 조건에 부합하는지 검사
        for (Value v: vl) {
            boolean flag = false;
            Column c = v.getColumn();
            Table rT = c.getReferencedTable();
            if (rT != null) {
                for (Value vv: rT.getColumnValues(c.getReferencedColumn())) {
                    if (vv.equals(v)) {
                        flag = true;
                    }   
                }
            } else {
                flag = true;
            }
            if (!flag) {
                throw new ErrorException(Flags.INSERT_REFERENTIAL_INTEGRITY_ERROR);
            }
        }

        // table에 record 추가
        t.addRecord(newRecord);

        // db에 저장
        Cursor cursor = simpleDb.openCursor(null, null);
        DatabaseEntry key, data;

        try {
            String entry = tn + R;
            key = new DatabaseEntry(entry.getBytes("UTF-8"));
            data = new DatabaseEntry();
            cursor.getSearchKey(key, data, LockMode.DEFAULT);
            do {
                if (data.getData() == null) {
                    break;
                }
                if (t.getRecordLength() > 1) {
                    cursor.delete();
                }
            } while (cursor.getNextDup(key, data, LockMode.DEFAULT) == OperationStatus.SUCCESS);
            cursor.close();
            
            cursor = simpleDb.openCursor(null, null);
            key = new DatabaseEntry(entry.getBytes("UTF-8"));
            data = new DatabaseEntry(t.recordsToString().getBytes("UTF-8"));
            cursor.put(key, data);

            cursor.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
    public String deleteValues(String tn, BooleanValueExpression bve) {
        ArrayList<Record> deletedRecords = new ArrayList<Record>();

        Table t = getTable(tn);
        
        if (bve == null) {
            deletedRecords = t.getRecords();
            t.removeAllRecords();
            
        } else {
            for (Record r: t.getRecords()) {
                if (bve.isTrue(r)) {
                    deletedRecords.add(r);
                    t.removeRecord(r);
                }
            }
        }
        
        Cursor cursor = simpleDb.openCursor(null, null);
       
        try {
            String entry = tn + R;
            DatabaseEntry recordKey = new DatabaseEntry(entry.getBytes("UTF-8"));
            DatabaseEntry recordData = new DatabaseEntry();
            
            // record 삭제
            cursor.getSearchKey(recordKey, recordData, LockMode.DEFAULT);
            do {
                if (recordData != null) {
                    cursor.delete();
                }
            } while (cursor.getNextDup(recordKey, recordData, LockMode.DEFAULT) == OperationStatus.SUCCESS);
            
            cursor.close();

            // record 재삽입
            if (t.getRecordLength() != 0) {
                Cursor newCursor = simpleDb.openCursor(null, null);
                recordKey = new DatabaseEntry(entry.getBytes("UTF-8"));
                recordData = new DatabaseEntry(t.recordsToString().getBytes("UTF-8"));
                newCursor.put(recordKey, recordData);

                newCursor.close();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        return Integer.toString(deletedRecords.size());
    }
    
    public void selectValues(ArrayList<Item> il, TableExpression te) {
        ArrayList<ArrayList<Value>> sl = new ArrayList<ArrayList<Value>>();
        int len = 0;
        
        if (te.booleanValueExpression == null) {
            if (il.size() == 0) {
                for (Table t: te.tableList) {
                    for (Column c: t.getColumns()) {
                        Item i = new Item(t.getName(), c.getName());
                        i.setColumnInfo(t, c);
                        il.add(i);
                    }
                }
            }
            
            for (Item i: il) {
                len = i.getTable().getRecordLength();
                sl.add(i.getTable().getColumnValues(i.getColumn()));
            }
        } else {
            
        }
        
        PrintMessages.printLine();
        for (Item i: il) {
            System.out.print("| " + i.getColumnName() + " ");
        }
        System.out.print("|\n");
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < sl.size(); j++) {
                Value v = sl.get(j).get(i);
                System.out.print("| " + v.getValue() + "\t");
            }
            System.out.print("|\n");
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
            } else if (arr[3].equals("FOR")) {
                fk = true;
            } else if (arr[3].equals("PRI")) {
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
