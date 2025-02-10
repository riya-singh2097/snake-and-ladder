package snlll;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

       // System.out.println("Username from user: " + username);
       // System.out.println("Password from user: " + password);

        try (Connection conn = DBUtil.getConnection()) {
            // Query to fetch the user's ID and password from the database
            String sql = "SELECT id, password FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
              //  System.out.println("Password retrieved from database: " + storedPassword);

                // Compare the stored password with the user-entered password
                if (password.equals(storedPassword)) {
                  //  System.out.println("Password matched. User authenticated.");
                    
                    // Create session and store user details
                    HttpSession session = request.getSession();
                    session.setAttribute("userId", rs.getInt("id"));
                    session.setAttribute("username", username);
                    session.setMaxInactiveInterval(30 * 60); // Session expires in 30 mins

                    response.sendRedirect("start.jsp");
                } else {
                   // System.out.println("Password mismatch.");
                    request.setAttribute("error", "Invalid credentials");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                }
            } else {
            //    System.out.println("User not found in the database.");
                request.setAttribute("error", "Invalid credentials");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
}
