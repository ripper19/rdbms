import java.util.ArrayList;
import java.util.List;

public class ParseColumns {
    public List<Column> parseColumns(String columnDef){
        List<Column> columns = new ArrayList<>();

        String[] oneColumn = columnDef.split(",");
        for (String col: oneColumn){
            String c = col.trim();

            String[] parts = c.split("\\s+");

            String name = parts[0];
            String type = parts[1];

            DataType t;
            try {
                t = DataType.valueOf(type.toUpperCase());
            }catch (IllegalArgumentException e){
                throw new IllegalArgumentException("Wrong Data Type" + e.getMessage());
            }

            boolean primaryKey = false;
            boolean unique = false;

            for (int i=2; i< parts.length; i++){
                if (parts[i].equalsIgnoreCase("PRIMARY") && (i+1<parts.length) && (parts[i+1].equalsIgnoreCase("KEY"))){
                    primaryKey = true;
                }
                if (parts[i].equalsIgnoreCase("UNIQUE")){
                    unique = true;
                }

            }
            columns.add(new Column(name,t,primaryKey,unique));
        }
        return columns;
    }
}
