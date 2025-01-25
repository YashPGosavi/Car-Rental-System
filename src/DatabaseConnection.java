import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface DatabaseConnection {

    String url = "jdbc:mysql://localhost:3306/CarRental";
    String username = "root";
    String password = "9274@Indian";

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

}
