fetch("CheckSessionServlet")
    .then(response => response.json())
    .then(data => {
        if (!data.loggedIn) {
            window.location.href = "login.jsp";
        }
    })
    .catch(error => console.error("Session check failed:", error));
