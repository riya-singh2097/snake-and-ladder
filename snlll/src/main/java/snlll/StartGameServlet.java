package snlll;

import java.io.IOException;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/StartGameServlet")
public class StartGameServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Database Connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/snakes_ladders", "root", "RIYa2097");

            // Get the last game ID
            String lastGameQuery = "SELECT MAX(game_id) FROM games";
            PreparedStatement lastGameStmt = conn.prepareStatement(lastGameQuery);
            ResultSet rsGame = lastGameStmt.executeQuery();

            int gameId = 1; // Default game ID if no games exist

            if (rsGame.next() && rsGame.getInt(1) > 0) {
                gameId = rsGame.getInt(1) + 1; // Increment last game ID
            }

            rsGame.close();
            lastGameStmt.close();

            // Insert a new game record
            String insertGameQuery = "INSERT INTO games (game_id, game_status) VALUES (?, 'ongoing')";
            PreparedStatement gameStmt = conn.prepareStatement(insertGameQuery);
            gameStmt.setInt(1, gameId);
            gameStmt.executeUpdate();
            gameStmt.close();

            // Store game ID in session
            HttpSession session = request.getSession();
            session.setAttribute("gameId", gameId);

            conn.close();

            response.getWriter().write("Game started with ID: " + gameId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
