package db;

import java.util.ArrayList;

public class Table {
    private String tableName;
    private ArrayList<Column> columns;

    public Table(String tableName) {
        this.tableName = tableName;
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
}
