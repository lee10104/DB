package db;

import java.util.ArrayList;

public class Record {
    private ArrayList<Value> values;
    private int size = 0;
    
    public void addValue(Value value) {
        values.add(value);
        size++;
    }

    public ArrayList<Value> getValues() {
        return values;
    }
    
    public int getSize() {
        return size;
    }
    
    public String valueListToString() {
        ArrayList<String> vl = new ArrayList<String>();

        for (Value v: values) {
            vl.add(v.getValue());
        }

        return String.join("\t", vl);
    }
}
