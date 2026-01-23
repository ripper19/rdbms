public class joinStatement implements Statement{
    private final String table1;
    private final String table2;
    private final String[] columns;
    private final String[] conditions;

    public joinStatement(String table1, String table2, String[] columns, String[] conditions) {
        this.table1 = table1;
        this.table2 = table2;
        this.columns = columns;
        this.conditions = conditions;
    }

    @Override
    public String execute(Database db) {
        return db.joins(table1,table2,columns,conditions);
    }
}
