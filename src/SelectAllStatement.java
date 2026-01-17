public class SelectAllStatement implements Statement{
    private final String name;

    public SelectAllStatement(String name){
        this.name=name;
    }
    @Override
    public String execute(Database db) {
        return db.selectAllData(name);
    }
}
