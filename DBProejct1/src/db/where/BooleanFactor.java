package db.where;

public class BooleanFactor {
    private BooleanTest booleanTest;
    private boolean isNot;
    
    public void setIsNot(boolean isNot) {
        this.isNot = isNot;
    }
    
    public void setBooleanTest(BooleanTest booleanTest) {
        this.booleanTest = booleanTest;
    }
}
