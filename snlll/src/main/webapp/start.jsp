<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Snakes and Ladders</title>
    <link rel="stylesheet" href="basicstyle.css">
</head>
<body>
<script src="authentic.js"></script>
    <div class="container">
        <h1>Snakes and Ladders</h1>
      
        
        <p>Choose the number of players (Min: 2, Max: 4)</p>
        
        <form action="" method="POST">
            <label for="numPlayers">Number of Players:</label>
            <select name="numPlayers" id="numPlayers" required>
                <option value="2">2</option>
                <option value="3">3</option>
                <option value="4">4</option>
            </select>
            
           
            
            <button type="submit">Start Game</button>
            <button type="button" onclick="window.close()">Exit</button>
               <button onclick="window.location.href='logout.jsp'">Logout</button>
        </form>
    </div>

    <script>
        document.getElementById('numPlayers').addEventListener('change', function() {
            let num = parseInt(this.value);
            for (let i = 1; i <= 4; i++) {
                document.getElementById('player' + i).style.display = i <= num ? 'block' : 'none';
            }
        });
    </script>

    <%
        if (request.getMethod().equalsIgnoreCase("POST")) {
            int numPlayers = Integer.parseInt(request.getParameter("numPlayers"));
            String[] players = new String[numPlayers];
            for (int i = 0; i < numPlayers; i++) {
                String playerName = request.getParameter("player" + (i + 1));
                if (playerName != null && !playerName.trim().isEmpty()) {
                    players[i] = playerName;
                }
            }
            session.setAttribute("players", players);
            if(players.length == 4)
            	response.sendRedirect("4players.html"); // Redirecting to the board page
            else if(players.length == 3)
            	response.sendRedirect("3players.html"); 
            else
            	response.sendRedirect("2players.html"); 
        }
    %>
</body>
</html>
