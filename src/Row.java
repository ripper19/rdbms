import java.util.Map;

public class Row {
    private Map<String, Object> data;

    public Row(Map<String, Object> data) {
        this.data=data;
    }

    public Map<String, Object> getData() {
        return data;
    }
    public Object get(String column){
        return data.get(column);
    }
    public void set(String column, Object value){
        data.put(column,value);
    }
}
