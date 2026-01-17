public class SelectAllStatement implements Statement{
    private String name;

    public SelectAllStatement(String name){
        this.name=name;
    }
    @Override
    public void execute(Database db) {
        db.selectAllData(name);
    }
}
