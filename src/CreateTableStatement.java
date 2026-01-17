import java.util.List;

public class CreateTableStatement implements Statement {
    private final String tableName;
    private final List<Column> columns;

    public CreateTableStatement(String tableName, List<Column> columns){
        this.tableName=tableName;
        this.columns=columns;
    }
    @Override
    public void execute(Database db) {
        db.createTable(tableName,columns);
    }
}
