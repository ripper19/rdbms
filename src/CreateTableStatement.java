import java.util.List;
import java.util.stream.Collectors;

public class CreateTableStatement implements Statement {
    private final String tableName;
    private final List<Column> columns;

    public CreateTableStatement(String tableName, List<Column> columns){
        this.tableName=tableName;
        this.columns=columns;
    }
    @Override
    public String execute(Database db) {
        db.createTable(tableName,columns);
        String columnNames = columns.stream()
                .map(Column::getName)
                .collect(Collectors.joining(", "));
        return "Table "+tableName+ " created with columns: " + columnNames;
    }
}
