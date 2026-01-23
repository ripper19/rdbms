import java.util.*;

public class Table {
    private final String name;
    private final List<Column> columns;
    private final List<Row> rows = new ArrayList<>();
    private final Map<Object, Row> primaryKeyIndex = new HashMap<>();
    private final Map<String, Map<Object, Row>> uniqueIndex = new HashMap<>();

    private Column primaryKeyColumn;
    /*
    one primary key per table

     */

    public Table(String name, List<Column> columns) {
        this.name = name;
        this.columns = columns;

        for (Column c : columns) {
            if (c.isPrimaryKey()) {
                if (primaryKeyColumn != null) throw new RuntimeException("Multiple primary Keys arent supported");
                primaryKeyColumn = c;
            }
            if (c.isPrimaryKey() || c.isUnique()) {
                uniqueIndex.put(c.getName(), new HashMap<>());
            }
        }
    }

    public void addColumns(List<Column> inColumns) {
        columns.addAll(inColumns);
    }

    public void insert(Map<String, Object> values) {

        if (primaryKeyColumn != null) {
            String pkName = primaryKeyColumn.getName();
            Object pkValue = values.get(pkName);

            if (pkValue == null) throw new RuntimeException("Primary Key missing.");
            if (primaryKeyIndex.containsKey(pkValue))
                throw new RuntimeException("Primary Key already set: " + pkValue);
        }
        for (Map.Entry<String, Map<Object, Row>> entry : uniqueIndex.entrySet()) {
            Object val = values.get(entry.getKey());
            if (val != null && entry.getValue().containsKey(val)) {
                throw new RuntimeException("Unique key violation on " + entry.getKey());
            }
        }
        Map<String,Object> typedValues = new HashMap<>();
        for (Column c:columns){
            Object raw = values.get(c.getName());

            Object convType = convertToType(raw,c);
            typedValues.put(c.getName(),convType);
        }

        Row row = new Row(typedValues);
        rows.add(row);

        if (primaryKeyColumn != null) {
            primaryKeyIndex.put(values.get(primaryKeyColumn.getName()), row);
        }
        for (Map.Entry<String, Map<Object, Row>> entry : uniqueIndex.entrySet()) {
            Object val = values.get(entry.getKey());
            if (val != null) {
                entry.getValue().put(val, row);
            }
        }
    }

    public void update(String primary, Map<String, Object> values) {
        try {
            if (!primaryKeyIndex.containsKey(primary)) {
                throw new RuntimeException("Invalid field Argument");
            }

            Row matchingRow = primaryKeyIndex.get(primary);
            Object newPrimaryKeyValue = null;

            for (Map.Entry<String, Object> entry : values.entrySet()) {
                String columnName = entry.getKey();
                Object newValue = entry.getValue();

                Column targetColumn = null;
                for (Column c : columns) {
                    if (c.getName().equals(columnName)) {
                        targetColumn =c;
                        break;
                    }
                }
                if (targetColumn==null) throw new RuntimeException("Column " + columnName + " not found");

                Object typedValue = convertToType(newValue, targetColumn);
                matchingRow.set(columnName, typedValue);
                System.out.println("Column: " +columnName+" changed value to: "+newValue);

                if (targetColumn.isPrimaryKey()) {
                    if (!newValue.equals(primary) && primaryKeyIndex.containsKey(newValue)) {
                        throw new RuntimeException("Value already exists. Constraint violation.");
                    }
                    newPrimaryKeyValue = newValue;

                }
            }
            if (newPrimaryKeyValue != null && !newPrimaryKeyValue.equals(primary)) {
                primaryKeyIndex.remove(primary);
                primaryKeyIndex.put(newPrimaryKeyValue, matchingRow);
            }

        } catch (RuntimeException e) {
            throw new RuntimeException("Failed" + e.getMessage());
        }
    }

    public void delete(Object primary) {
        if (!primaryKeyIndex.containsKey(primary)) throw new RuntimeException("Cannot find this Field");
        Row matchingRow = primaryKeyIndex.get(primary);
        rows.remove(matchingRow);

        primaryKeyIndex.remove(matchingRow);
        for (Map<Object, Row> index : uniqueIndex.values()) {
            index.values().remove(matchingRow);
        }
    }

    public String selectAll() {
        StringBuilder builder = new StringBuilder();

        for (Column c : columns) {
            builder.append(c.getName()).append("\t");
        }
        builder.append("\n");

        for (Row row : rows) {
            for (Column column : columns) {
                builder.append(row.get(column.getName())).append("\t");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    private Object convertToType(Object value, Column column){
        /*
        Used to convert all data to strings during inserts and updates for type safety during comparisons
         */
        if (value==null) return null;
        return switch (column.getType()){
            case INT ->     Integer.parseInt(value.toString());
            case VARCHAR, BOOLEAN -> value.toString();
        };
    }
    public String[] AllColumns() {
        List<String> getCols = new ArrayList<>();
        for (Column c : columns) {
            getCols.add(c.getName());
        }
        return getCols.toArray(new String[0]);
    }
    public List<Row> getRows(){
        return rows;
    }
}
