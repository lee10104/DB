package db;

public class Value {
    private DataType dataType;
    private String value;
    private String columnName = null;
    
    public Value(DataType dataType, String value) {
        this.dataType = dataType;
        this.value = value;
    }
    
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    
    public String getColumn() {
        return columnName;
    }
}
