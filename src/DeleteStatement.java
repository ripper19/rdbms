public class DeleteStatement implements Statement{
    private String tableName;
    private String primary;
    public DeleteStatement(String tableName, String primary){
        this.tableName=tableName;
        this.primary=primary;
    }
    @Override
    public void execute(Database db) {
        db.delete(tableName,primary);
    }
}
