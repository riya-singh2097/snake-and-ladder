<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<style>
          * {
            margin: 0;
            padding: 0;
            font-family: sans-serif;
        }
        body {
            height: 100vh;
            background: url('snl.jpg') no-repeat center center/cover;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .container {
            max-width: 320px;
            width: 100%;
            background: rgba(0, 0, 0, 0.6);
            padding: 25px;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.5);
            text-align: center;
        }

        .container h2 {
            font-size: 22px;
            color: #fff;
            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.7);
            margin-bottom: 15px;
        }

        .error {
            color: red;
            font-size: 14px;
            margin-bottom: 10px;
        }

        .form-group {
            margin: 15px 0;
            text-align: left;
        }

        .form-group label {
            color: #fff;
            display: block;
            font-size: 14px;
            margin-bottom: 5px;
        }

        .form-group input {
            width: 90%;
            padding: 10px;
            border: 2px solid #aaaaaa;
            border-radius: 5px;
            outline: none;
            transition: border-color 0.3s;
        }

        .form-group input:focus {
            border-color: #ffcc00;
        }

        button {
            width: 100%;
            padding: 10px;
            background: #ffcc00;
            border: none;
            border-radius: 10px;
            font-size: 1rem;
            cursor: pointer;
            transition: background 0.3s;
        }

        button:hover {
            background: #e6b800;
        }

        .login-link {
            text-align: center;
            font-size: 13px;
            margin-top: 15px;
            color: #fff;
        }

        .login-link a {
            text-decoration: none;
            color: rgb(45, 144, 197);
        }

        .login-link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Snake and Ladder Login</h2>
        
        <% if(request.getAttribute("error") != null) { %>
            <div class="error">${error}</div>
        <% } %>
        
        <form action="login" method="post">
            <div class="form-group">
                <label>User name:</label>
                <input type="text" name="username" required>
            </div>
            <div class="form-group">
                <label>Password:</label>
                <input type="password" name="password" required>
            </div>
            <button type="submit">Login</button>
        </form>
      <div class="login-link">
   		 New user? <a href="signup.jsp">Sign up here</a>
	  </div>
    </div>
</body>
</html>