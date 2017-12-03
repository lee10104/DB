package db;

public class Value {
    private DataType dataType;
    private String value;
    private String columnName = null;
    private Column column = null;
    
    public Value(DataType dataType, String value) {
        this.dataType = dataType;
        this.value = value;
    }
    
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    
    public String getColumnName() {
        return columnName;
    }
    
    public DataType getDataType() {
        return dataType;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setColumn(Column column) {
        this.column = column;
    }
    
    public Column getColumn() {
        return column;
    }
}
