import java.util.*;

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
    public String show(){
        StringBuilder builder = new StringBuilder();
        for (String table: tables.keySet()){
            builder.append(table);
        }
        return builder.toString();
    }

    public String selectAllData(String name){
        Table table = getTable(name);
        return table.selectAll();
    }
    public String joins(String oneTable, String twoTable, String[] columns, String[]conditions){
        Table firstTable = getTable(oneTable);
        Table secondTable = getTable(twoTable);

        Set<String> firstColumSet = new HashSet<>(Arrays.asList(firstTable.AllColumns()));
        Set<String> secondColumnSet = new HashSet<>(Arrays.asList(secondTable.AllColumns()));

        for (String c: columns){
            if (!firstColumSet.contains(c) && !secondColumnSet.contains(c)){
                throw new IllegalArgumentException("Wrong column provided "+c);
            }
       }
        if (!firstColumSet.contains(conditions[0])) throw new RuntimeException("Table "+oneTable+" does not contain column "+conditions[0]);
        if (!secondColumnSet.contains(conditions[1])) throw new RuntimeException("Table "+twoTable+" does not contain column "+conditions[1]);

        Map<Object, List<Row>> index = new HashMap<>();
        for (Row r:secondTable.getRows()){
            Object key =r.get(conditions[1]);
            index.computeIfAbsent(key, k->new ArrayList<>()).add(r);
        }
        StringBuilder result = new StringBuilder();
        for (Row leftRow: firstTable.getRows()) {
            Object key = leftRow.get(conditions[0]);

            List<Row> matches = index.get(key);
            if (matches == null) continue;

            for (Row row : matches) {
                for (String c : columns) {
                    if (firstColumSet.contains(c)) {
                        result.append(leftRow.get(c)).append("\t");
                    } else {
                        result.append(row.get(c)).append("\t");
                    }
                }
                result.append("\n");
            }
                 }
        return result.toString();
    }
}
