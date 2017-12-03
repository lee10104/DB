package db;

import java.io.Serializable;

public class DataType implements Serializable {
    private int dataType;
    private int charLength;
    private String date;
    
    public DataType(int dataType, int charLength) {
        this.dataType = dataType;
        if (dataType == Flags.CHAR) {
            this.charLength = charLength;
        }
    }
    
    public int getType() {
        return dataType;
    }
    
    public int getCharLength() {
        return charLength;
    }
    
    @Override
    public String toString() {
        String str = "";
        
        if (dataType == Flags.CHAR) {
            str = "char(" + String.valueOf(charLength) + ")";
        } else if (dataType == Flags.INT) {
            str = "int";
        } else if (dataType == Flags.DATE) {
            str = "date";
        }
        
        return str;
    }
}
