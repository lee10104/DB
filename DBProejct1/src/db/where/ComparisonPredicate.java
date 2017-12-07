package db.where;

public class ComparisonPredicate {
    public static final int EQ = 0; // ==
    public static final int NE = 1; // !=
    public static final int GT = 2; // >
    public static final int GE = 3; // >=
    public static final int LT = 4; // <
    public static final int LE = 5; // <=
    
    private int compOp;
    private CompOperand left;
    private CompOperand right;
    
    public void setType(int compOp) {
        this.compOp = compOp;
    }
    
    public void setCompOperand(CompOperand left, CompOperand right) {
        this.left = left;
        this.right = right;
    }
}
