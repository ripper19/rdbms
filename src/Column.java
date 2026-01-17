public class Column {
    private String name;
    private DataType type;
    private boolean isPrimaryKey;
    private boolean isUnique;

    public Column(String name, DataType type, boolean isPrimaryKey, boolean isUnique) {
        this.name=name;
        this.type=type;
        this.isPrimaryKey=isPrimaryKey;
        this.isUnique=isUnique();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public boolean isUnique() {
        return isUnique;
    }

    public void setUnique(boolean unique) {
        isUnique = unique;
    }
}
