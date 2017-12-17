package db.where;

import java.util.ArrayList;

public class BooleanTerm {
    private ArrayList<BooleanFactor> booleanFactors = new ArrayList<BooleanFactor>();
    
    public void addBooleanFactor(BooleanFactor booleanFactor) {
        booleanFactors.add(booleanFactor);
    }
}
