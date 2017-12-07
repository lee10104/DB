package db.where;

public class ComparisonPredicate implements Predicate {
    public static final int EQ = 0; // ==
    public static final int NE = 1; // !=
    public static final int GT = 2; // >
    public static final int GE = 3; // >=
    public static final int LT = 4; // <
    public static final int LE = 5; // <=
    
    private int compOp;
    private Item left;
    private Item right;
    
    public ComparisonPredicate(Item left, Item right, String compOp) {
        this.left = left;
        this.right = right;
        if (compOp.equals("=")) {
            this.compOp = EQ;
        } else if (compOp.equals("!=")) {
            this.compOp = NE;
        } else if (compOp.equals(">")) {
            this.compOp = GT;
        } else if (compOp.equals(">=")) {
            this.compOp = GE;
        } else if (compOp.equals("<")) {
            this.compOp = LT;
        } else if (compOp.equals("<=")) {
            this.compOp = LE;
        }
    }
}
