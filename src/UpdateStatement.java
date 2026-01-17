import java.util.Map;

public class UpdateStatement implements Statement{
    private final String name;
    private final String primary;
    private final Map<String,Object> values;
    public UpdateStatement(String name, String primary, Map<String,Object>values){
        this.name=name;
        this.primary=primary;
        this.values=values;
    }
    @Override
    public void execute(Database db) {
        db.updates(name,primary,values);
    }
}
