package snlll;

import java.io.IOException;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/PlayerMoveServlet")
public class PlayerMoveServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Retrieve game ID from session
            HttpSession session = request.getSession();
            Integer gameId = (Integer) session.getAttribute("gameId");

            if (gameId == null) {
                response.getWriter().write("Error: Game ID is not set. Please start a new game.");
                return;
            }

            int playerId = Integer.parseInt(request.getParameter("playerId"));
            int diceRoll = Integer.parseInt(request.getParameter("diceRoll"));
            int oldPosition = Integer.parseInt(request.getParameter("oldPosition"));
            int newPosition = Integer.parseInt(request.getParameter("newPosition"));

            System.out.println("Received Data: gameId=" + gameId + ", playerId=" + playerId + ", diceRoll=" + diceRoll + ", oldPosition=" + oldPosition + ", newPosition=" + newPosition);

            // Database Connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/snakes_ladders", "root", "RIYa2097");

            // Ensure the player exists
            String checkPlayerQuery = "SELECT player_id FROM players WHERE player_id = ?";
            PreparedStatement checkPlayerStmt = conn.prepareStatement(checkPlayerQuery);
            checkPlayerStmt.setInt(1, playerId);
            ResultSet rsPlayer = checkPlayerStmt.executeQuery();

            if (!rsPlayer.next()) { // If the player does not exist, insert them
                String insertPlayerQuery = "INSERT INTO players (player_id, name) VALUES (?, ?)";
                PreparedStatement insertPlayerStmt = conn.prepareStatement(insertPlayerQuery);
                insertPlayerStmt.setInt(1, playerId);
                insertPlayerStmt.setString(2, "Player " + playerId); // Assigning a default name
                insertPlayerStmt.executeUpdate();
                insertPlayerStmt.close();
                System.out.println("Player " + playerId + " inserted.");
            }

            rsPlayer.close();
            checkPlayerStmt.close();

            // Insert the move into the moves table
            String insertMoveQuery = "INSERT INTO moves (game_id, player_id, roll_value, previous_position, new_position) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement moveStmt = conn.prepareStatement(insertMoveQuery);
            moveStmt.setInt(1, gameId);
            moveStmt.setInt(2, playerId);
            moveStmt.setInt(3, diceRoll);
            moveStmt.setInt(4, oldPosition);
            moveStmt.setInt(5, newPosition);

            int rows = moveStmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Move inserted successfully!");
            } else {
                System.out.println("Move insertion failed.");
            }

            moveStmt.close();
            conn.close();

            response.getWriter().write("Move recorded successfully for Game ID: " + gameId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
