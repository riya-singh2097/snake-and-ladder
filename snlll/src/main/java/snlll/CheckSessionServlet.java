package snlll;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/CheckSessionServlet")
public class CheckSessionServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        boolean loggedIn = (session != null && session.getAttribute("username") != null);

        // Debugging output
        if (session != null) {
            System.out.println("Session ID: " + session.getId());
            System.out.println("Username: " + session.getAttribute("username"));
        } else {
            System.out.println("Session is null");
        }
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print("{\"loggedIn\": " + loggedIn + "}");
        out.flush();
    }
}
