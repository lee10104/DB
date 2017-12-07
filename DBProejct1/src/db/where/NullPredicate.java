package db.where;

public class NullPredicate implements Predicate {
    private boolean isNull;
    private Item item;
    
    public void setIsNull(boolean isNull) {
        this.isNull = isNull;
    }
    
    public void setItem(Item item) {
        this.item = item;
    }
}
