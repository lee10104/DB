package db.where;

import java.util.ArrayList;

public class BooleanValueExpression {
    private ArrayList<BooleanTerm> booleanTerms = new ArrayList<BooleanTerm>();
    
    public void addBooleanTerm(BooleanTerm booleanTerm) {
        booleanTerms.add(booleanTerm);
    }
}
