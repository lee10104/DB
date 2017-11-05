package db;

import java.io.Serializable;

public class DataType implements Serializable {
    public int dataType;
    public int charLength;
    public String date;
    
    public DataType(int dataType, int charLength) {
        this.dataType = dataType;
        if (dataType == Flags.CHAR) {
            this.charLength = charLength;
        }
    }
    
    @Override
    public String toString() {
        String str = "";
        
        if (dataType == Flags.CHAR) {
            str = "char(" + charLength + ")";
        } else if (dataType == Flags.INT) {
            str = "int";
        } else if (dataType == Flags.DATE) {
            str = "date";
        }
        
        return str;
    }
}
