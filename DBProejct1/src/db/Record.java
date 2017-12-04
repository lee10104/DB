package db;

import java.util.ArrayList;

public class Record {
    private ArrayList<String> values;
    private int size = 0;
    
    public void addValue(String value) {
        values.add(value);
        size++;
    }

    public ArrayList<String> getValues() {
        return values;
    }
    
    public int getSize() {
        return size;
    }
    
    public String valueListToString() {
        return String.join("\t", values);
    }
}
