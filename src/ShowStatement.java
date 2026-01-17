public class ShowStatement implements Statement{
    @Override
    public String execute(Database db) {
        return db.show();
    }
}
