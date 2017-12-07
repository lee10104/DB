package db.where;

import db.*;

public class Item {
    private Table table = null;
    private Column column = null;
    private String tableName;
    private String columnName;
    private Value value;
    private boolean isValue; // true면 value, false면 column 정보
    
    public Item(Value value) {
        this.value = value;
        this.isValue = true;
    }

    public Item(String tableName, String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.isValue = false;
    }
    
    public boolean getIsValue() {
        return isValue;
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public String getColumnName() {
        return columnName;
    }
    
    public void setColumnInfo(Table table, Column column) {
        this.table = table;
        this.column = column;
    }
}
