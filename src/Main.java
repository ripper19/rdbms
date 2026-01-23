import java.sql.SQLException;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {
        Parser parser = new Parser();
        Database db = new Database();
        Scanner scanner = new Scanner(System.in);

        while (true){
            System.out.print("mydb>");
            String sql = scanner.nextLine();

            if (sql.equalsIgnoreCase("exit")) break;
            if (sql.equalsIgnoreCase("help")){
                System.out.println("Syntax for Use: \n" +
                        "CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR, email VARCHAR UNIQUE) \n" +
                        "INSERT INTO users (name, email, id) VALUES('John Doe', 'john@example.com', 30) \n" +
                        "UPDATE users SET(name='John') WHERE(id=1) \n" +
                        "DELETE FROM users WHERE (id=1)\n" +
                        "SHOW TABLES \n" +
                        "SELECT ALL FROM users \n" +
                        "THESE DEVIATE SLIGHTLY FROM NORMAL SQL SYNTAX");
                continue;
            }
            if (sql.equalsIgnoreCase("clear")){
                System.out.print("\033[H\033[2J");
                System.out.flush();
                continue;
            }
            try {
                Statement statement = parser.parse(sql);
                String result = statement.execute(db);
                System.out.println(result);
            } catch (SQLException | RuntimeException e) {
                System.out.println("error: "+e.getMessage());
            }
        }
    }
}