package db;

import java.io.Serializable;

public class Column implements Serializable{
    private String columnName;
    private DataType dataType;
    private boolean isNull;
    private boolean isPrimaryKey;
    private boolean isForeignKey;
    private boolean isReferenced;
    private Table rTable;
    private Column rColumn;
    
    public Column(String columnName, DataType dataType, boolean isNull) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.isNull = isNull;
        this.isPrimaryKey = false;
        this.isForeignKey = false;
        this.isReferenced = false;
    }
    
    public String getName() {
        return columnName;
    }
    
    public DataType getDataType() {
        return dataType;
    }
    
    public void setIsNull(boolean isNull) {
        this.isNull = isNull;
    }
    
    public boolean getIsNull() {
        return isNull;
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
    
    public void setIsReferenced(boolean isReferenced) {
        this.isReferenced = isReferenced;
    }
    
    public boolean getIsReferenced() {
        return isReferenced;
    }
    
    public void setForeignKey(Table t, Column c) {
        this.rTable = t;
        this.rColumn = c;
    }
    
    public String printColumn() {
        String isNullStr;
        String keyStr;
        String refStr;

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
        
        return columnName + "\t" + dataType.toString() + "\t" + isNullStr + "\t" + keyStr;
    }
    
    @Override
    public String toString() {
        String isNullStr;
        String isReferencedStr;
        String keyStr;
        String refStr;

        if (isNull) {
            isNullStr = "Y";
        } else {
            isNullStr = "N";
        }
        
        if (isReferenced) {
            isReferencedStr = "Y";
        } else {
            isReferencedStr = "N";
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
        
        if (isForeignKey) {
            refStr = rTable.getName() + "\t" + rColumn.getName();
        } else {
            refStr = "";
        }
        
        return columnName + "\t" + dataType.toString() + "\t" + isNullStr + "\t" + keyStr + "\t" + isReferencedStr + "\t" + refStr;
    }
}
