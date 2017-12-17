package db;

import db.where.*;
import java.util.ArrayList;

public class TableExpression {
    public ArrayList<Table> tableList;
    public BooleanValueExpression booleanValueExpression;
    
    public TableExpression(ArrayList<Table> tableList, BooleanValueExpression booleanValueExpression) {
        this.tableList = tableList;
        this.booleanValueExpression = booleanValueExpression;
    }
}
