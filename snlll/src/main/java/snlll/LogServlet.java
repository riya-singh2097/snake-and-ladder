package snlll;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LogServlet")
public class LogServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // Get the game_id from the session
            HttpSession session = request.getSession();
            Integer gameId = (Integer) session.getAttribute("gameId");
            
            if (gameId == null) {
                out.println("<p>No active game found.</p>");
                return;
            }

            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/snakes_ladders", "root", "RIYa2097");
            
            // Query to retrieve moves for the current game_id
            String query = "SELECT * FROM moves WHERE game_id = ? ORDER BY move_id ASC"; // ASC for chronological order
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, gameId); // Set the game_id parameter
            ResultSet rs = ps.executeQuery();
            
            // Display the result in a table with an added Move No. column
            out.println("<table border='1' style='border=1px solid black;'><tr><th>Move No.</th><th>Player</th><th>Move</th><th>previous move</th><th>updated move</th></tr>");
            int moveNo = 1; // Initialize the move number counter
            while (rs.next()) {
                out.println("<tr><td>" + moveNo + "</td><td>" + rs.getString("player_id") + "</td><td>" + rs.getInt("roll_value") + "</td><td>" + rs.getInt("previous_position") + "</td><td>"+ rs.getInt("new_position") +"</td></tr>");
                moveNo++; // Increment the move number
            }
            out.println("</table>");
            
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }
    }
}
