package db;

import java.util.ArrayList;

public class Table {
    private String tableName;
    private ArrayList<Column> columns;
    private ArrayList<Record> records;

    public Table(String tableName) {
        this.tableName = tableName;
        columns = new ArrayList<Column>();
    }
    
    public Table(String newTableName, Table t) {
        this.tableName = newTableName;
        this.columns = t.getColumns();
        this.records = t.getRecords();
    }
    
    public String getName() {
        return tableName;
    }
    public void addColumn(Column c) {
        columns.add(c);
    }
    
    public ArrayList<Column> getColumns() {
        return columns;
    }
    
    public void addRecord(Record record) {
        records.add(record);
    }
    
    public void removeRecord(Record record) {
        records.remove(record);
    }
    
    public ArrayList<Record> getRecords() {
        return records;
    }
    
    public int removeAllRecords() {
        int count = records.size();
        records = new ArrayList<Record>();
        
        return count;
    }
    
    public ArrayList<Value> getColumnValues(Column column) {
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).equals(column)) {
                ArrayList<Value> vl = new ArrayList<Value>();
                for (Record r: records) {
                    vl.add(r.getValues().get(i));
                }
                return vl;
            }
        }
        
        return null;
    }
    
    public String recordsToString() {
        ArrayList<String> recordStringList = new ArrayList<String>();
        for (Record r: records) {
            recordStringList.add(r.toString());
        }
        return String.join("\t\t", recordStringList);
    }
}
