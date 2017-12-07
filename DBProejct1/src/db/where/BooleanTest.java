package db.where;

public class BooleanTest {
    public static final int PREDICATE = 0;
    public static final int BOOLEAN_VALUE_EXPRESSION = 1;
    
    private int predicateOrBooleanValueExpression; // Predicate or BooleanValueExpression
    private Predicate predicate;
    private BooleanValueExpression booleanValueExpression;
    
    public BooleanTest(Predicate predicate) {
        this.predicate = predicate;
        this.predicateOrBooleanValueExpression = PREDICATE;
    }
    
    public BooleanTest(BooleanValueExpression booleanValueExpression) {
        this.booleanValueExpression = booleanValueExpression;
        this.predicateOrBooleanValueExpression = BOOLEAN_VALUE_EXPRESSION;
    }
    
    public boolean isPredicate() {
        if (this.predicateOrBooleanValueExpression == PREDICATE) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isBooleanValueExpression() {
        if (this.predicateOrBooleanValueExpression == BOOLEAN_VALUE_EXPRESSION) {
            return true;
        } else {
            return false;
        }
    }
}