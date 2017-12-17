package db.where;

public class BooleanFactor {
    private BooleanTest booleanTest;
    private boolean isNot;
    
    public BooleanFactor(BooleanTest booleanTest, boolean isNot) {
        this.booleanTest = booleanTest;
        this.isNot = isNot;
    }
}
