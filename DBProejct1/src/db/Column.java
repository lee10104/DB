package db;

import java.io.Serializable;

public class Column implements Serializable{
    private String columnName;
    private DataType dataType;
    private boolean isNull;
    private boolean isPrimaryKey;
    private boolean isForeignKey;
    
    public Column(String columnName, DataType dataType, boolean isNull) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.isNull = isNull;
        this.isPrimaryKey = false;
        this.isForeignKey = false;
    }
    
    public String getName() {
        return columnName;
    }
    
    public DataType getDataType() {
        return dataType;
    }
    
    public void setIsPrimaryKey(boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }
    
    public boolean getIsPrimaryKey() {
        return isPrimaryKey;
    }
    
    public void setIsForeignKey(boolean isForeignKey) {
        this.isForeignKey = isForeignKey;
    }
    
    public boolean getIsForeignKey() {
        return isForeignKey;
    }
    
    @Override
    public String toString() {
        String isNullStr;
        String keyStr;
        
        if (isNull) {
            isNullStr = "Y";
        } else {
            isNullStr = "N";
        }
        
        if (isPrimaryKey && isForeignKey) {
            keyStr = "PRI/FOR";
        } else if (isPrimaryKey) {
            keyStr = "PRI";
        } else if (isForeignKey) {
            keyStr = "FOR";
        } else {
            keyStr = "";
        }
        
        return columnName + "\t" + dataType + "\t" + isNullStr + "\t" + keyStr;
    }
}
