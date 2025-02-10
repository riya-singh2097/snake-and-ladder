package snlll;
import java.sql.*;


public class DBUtil {
	
	    private static final String URL = "jdbc:mysql://localhost:3306/snakes_ladders";
	    private static final String USER = "root";
	    private static final String PASS = "RIYa2097";

	    
	    static {
	        try {
	            Class.forName("com.mysql.cj.jdbc.Driver");
	        } catch (ClassNotFoundException e) {
	            throw new RuntimeException("Failed to load MySQL driver", e);
	        }
	    }
	    
	    public static Connection getConnection() throws SQLException {
	        return DriverManager.getConnection(URL, USER, PASS);
	    
	}

}
