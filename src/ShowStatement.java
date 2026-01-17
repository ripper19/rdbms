public class ShowStatement implements Statement{
    @Override
    public void execute(Database db) {
        db.show();
    }
}
