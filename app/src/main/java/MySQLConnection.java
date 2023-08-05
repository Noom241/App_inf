import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MySQLConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://containers-us-west-206.railway.app:7879/railway";
        String user = "root";
        String password = "cPVTtNkTyCZrFOnrqk5Q";

        return DriverManager.getConnection(url, user, password);
    }

}
