import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class ApiServer {
    public static void main(String[] args) throws Exception{
        Database db = new Database();
        Parser parser = new Parser();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080),0);

        server.createContext("/sql", exchange -> {
            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")){
                exchange.sendResponseHeaders(405,-1);
                return;
            }
            String sql = new String(exchange.getRequestBody().readAllBytes());

            String response;
            try {
                Statement statement = parser.parse(sql);
                response = statement.execute(db);
            } catch (SQLException e) {
                response= "Error: " + e.getMessage();
                exchange.sendResponseHeaders(400,response.length());
                exchange.getResponseBody().write(response.getBytes());
                exchange.close();
                return;
            }
            exchange.sendResponseHeaders(200,response.length());
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        });
        server.start();
        System.out.println("API on 8080");
    }
}
