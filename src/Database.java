import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private final HashMap<String, Table> tables = new HashMap<>();

    public void createTable(String name, List<Column> columns){
        if (tables.containsKey(name)){
            throw new RuntimeException("Table already exists");
        }
        Table table = new Table(name, columns);
        tables.put(name,table);
    }

    public Table getTable(String name){
        Table table = tables.get(name);
        if (table == null){
            throw new RuntimeException("Table not found");
        }
        return table;
    }

    public void insertInto(String name, Map<String, Object> values){
        Table table = getTable(name);
        table.insert(values);
    }

    public void updates(String name, String primary, Map<String,Object> values){
        Table table = getTable(name);
        table.update(primary,values);
    }

    public void delete(String name, String primary){
        Table table = getTable(name);
        table.delete(primary);
    }

    //LOOP THROUGH AND PRINT
    public void show(){
        tables.keySet().forEach(System.out::println);
    }

    public void selectAllData(String name){
        Table table = getTable(name);
        table.selectAll();
    }
}
