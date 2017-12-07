package db.where;

public class Result {
    public static final int TRUE = 0;
    public static final int FALSE = 1;
    public static final int UNKNOWN = 2;
    
    private int result;
    
    public Result(int result) {
        this.result = result;
    }
    
    public int getResult() {
        return result;
    }
    
    public Result and(Result other) {
        if (result == TRUE && other.getResult() == TRUE) {
            return new Result(TRUE);
        } else if (result == FALSE || other.getResult() == FALSE) {
            return new Result(FALSE);
        } else {
            return new Result(UNKNOWN);
        }
    }
    
    public Result or(Result other) {
        if (result == FALSE && other.getResult() == FALSE) {
            return new Result(FALSE);
        } else if (result == TRUE || other.getResult() == TRUE) {
            return new Result(TRUE);
        } else {
            return new Result(UNKNOWN);
        }
    }
    
    public Result not() {
        if (result == TRUE) {
            return new Result(FALSE);
        } else if (result == FALSE) {
            return new Result(TRUE);
        } else {
            return new Result(UNKNOWN);
        }
    }
}
