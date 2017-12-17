package db.where;

public class NullPredicate implements Predicate {
    private Item item;
    private boolean isNull;
    
    public NullPredicate(Item item, boolean isNull) {
        this.item = item;
        this.isNull = isNull;
    }
}
