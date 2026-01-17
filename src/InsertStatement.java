import java.util.Map;

public class InsertStatement implements Statement{
private final String name;
private final Map<String,Object> values;

public InsertStatement(String name, Map<String,Object> values){
    this.name=name;
    this.values=values;
}

    @Override
    public void execute(Database db) {
    db.insertInto(name,values);
    }
}
