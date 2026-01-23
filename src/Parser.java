import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    ParseColumns parseColumns = new ParseColumns();

    public Statement parse(String sql) throws SQLException {
        String[] tokens = sql.trim().split("\\s+");
        String command = tokens[0].toUpperCase();

        switch (command){
            case "CREATE":
                return parseCreate(sql);
            case "INSERT":
                return parseInsert(sql);
            case "UPDATE":
                return parseUpdate(sql);
            case "DELETE":
                return parseDelete(sql);
            case "SHOW":
                return new ShowStatement();
            case"SELECT":
                return parseSelectAll(sql);
            default:
                throw new SQLException("No such command" + command);
        }
    }
    private CreateTableStatement parseCreate(String sql) throws SQLException {
        try {
            Pattern pattern = Pattern.compile(
                    "CREATE TABLE (\\w+) \\((.+)\\)",
                    Pattern.CASE_INSENSITIVE
            );
            Matcher matcher = pattern.matcher(sql);
            if (!matcher.matches()) {
                throw new SQLException("Invalid Create Statement");
            }
            String tablename = matcher.group(1);
            String columnDefine = matcher.group(2);
            List<Column> columns = parseColumns.parseColumns(columnDefine);
            System.out.println("Starting[*]");
            return new CreateTableStatement(tablename, columns);
        }catch (RuntimeException e){
            throw new RuntimeException("Error occurred: "+e.getMessage());
        }
    }
    private InsertStatement parseInsert(String sql) throws SQLException {

        Pattern pattern = Pattern.compile(
                "INSERT INTO (\\w+) \\(([^)]+)\\) VALUES\\(([^)]+)\\)",
                Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = pattern.matcher(sql);
        if (!matcher.find()){
            throw new SQLException("Invalid Insert Statement");
        }
        String tablename = matcher.group(1);
        String columns = matcher.group(2);
        Map<String, Object> insertions = getStringObjectMap(matcher, columns);
        return new InsertStatement(tablename,insertions);
    }
    private static Map<String, Object> getStringObjectMap(Matcher matcher, String columns) {
        String values = matcher.group(3);

        String[] columnPart = columns.split(",");
        String[] valuePart = values.split(",");

        if (columnPart.length != valuePart.length){
            throw new RuntimeException("Fewer values than expected");
        }
        Map<String,Object> insertions = new HashMap<>();
        for (int i=0; i< columnPart.length;i++){
            String col = columnPart[i].trim();
            String val = valuePart[i].trim();

            if (val.startsWith("'") && val.endsWith("'")){
                val = val.substring(1, val.length() -1);
            }
            insertions.put(col,val);
        }
        return insertions;
    }

    private UpdateStatement parseUpdate(String sql){
        Pattern pattern = Pattern.compile(
                "UPDATE (\\w+) SET\\(([^)]+)\\) WHERE\\(([^)]+)\\)",
                Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = pattern.matcher(sql);
        if (!matcher.matches()){
            throw new RuntimeException("Invalid Update Statement");
        }
        String tableName= matcher.group(1);
        String colValues = matcher.group(2);
        String contPrimary  = matcher.group(3);

        String[] params= colValues.split(",");

        Map<String,Object> updates = new HashMap<>();

        for (String p:params){
            String[] pair = p.split("=",2);
            String col = pair[0].trim();
            String val = pair[1].trim();

            if (val.startsWith("'") && val.endsWith("'")){
                val = val.substring(1,val.length()-1);
            }
            updates.put(col,val);
        }
        String[] primary = contPrimary.split("=",2);
        String column = primary[0].trim();
        String index = primary[1].trim();

        if (index.startsWith("'") && index.endsWith("'")){
            index = index.substring(1, index.length()-1);
        }

        return new UpdateStatement(tableName,index,updates);
    }

    private DeleteStatement parseDelete(String sql){
        Pattern pattern = Pattern.compile(
                "DELETE FROM (\\w+) WHERE\\(([^)]+)\\)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(sql);
        if (!matcher.find()){
            throw new RuntimeException("Invalid Delete Statement");
        }
        String tableName = matcher.group(1);
        String primary = matcher.group(2);

        String[]contPrime = primary.split("=",2);
        String column = contPrime[0];
        String index = contPrime[1];

        if (index.startsWith("'") && index.endsWith("'")) index = index.substring(1,index.length()-1);

        return new DeleteStatement(tableName,index);
    }

    private Statement parseSelectAll(String sql) {
        //joins using select
        if (sql.toUpperCase().contains("JOIN")) {
            Pattern pattern = Pattern.compile(
                    "SELECT +(.+) +FROM +(\\w+) +JOIN +(\\w+) +ON +(.+)",
                    Pattern.CASE_INSENSITIVE
            );
            Matcher matcher = pattern.matcher(sql);
            String rows = matcher.group(1);
            String firstTable = matcher.group(2);
            String secondTable = matcher.group(3);
            String condition = matcher.group(4);

            String[]individualRows = rows.split(",");
            String[]checkCond = condition.split("=",2);

            return new joinStatement(firstTable,secondTable,individualRows,checkCond);
        }
        else
        {
            Pattern pattern = Pattern.compile(
                    "SELECT ALL FROM (\\w+)",
                    Pattern.CASE_INSENSITIVE
            );

            Matcher matcher = pattern.matcher(sql);
            if (!matcher.matches()) throw new RuntimeException("Invalid Select All Statement");

            String tableName = matcher.group(1);

            return new SelectAllStatement(tableName);
        }
    }
}
