package db;

import java.io.Serializable;

public class Column implements Serializable{
    private String columnName;
    private DataType dataType;
    private boolean isNull;
    private boolean isPrivateKey;
    
    public Column(String columnName, DataType dataType, boolean isNull, boolean isPrivateKey) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.isNull = isNull;
        this.isPrivateKey = isPrivateKey;
    }
    
    @Override
    public String toString() {
        String isNullStr;
        String isPrivateKeyStr;
        
        if (isNull) {
            isNullStr = "Y";
        } else {
            isNullStr = "N";
        }
        
        if (isPrivateKey) {
            isPrivateKeyStr = "PRI";
        } else {
            isPrivateKeyStr = "";
        }
        
        return columnName + "\t" + dataType + "\t" + isNullStr + "\t" + isPrivateKeyStr;
    }
}
