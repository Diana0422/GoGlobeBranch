<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- Dichiarazione e istanziazione di un loginBean -->
<!--  id è il nome della variabile che andiamo ad istanziare -->

<jsp:useBean id="loginBean" scope="request" class="logic.bean.LoginBean"/>
<jsp:useBean id="sessionBean" scope="session" class="logic.bean.SessionBean"/>

<!-- Mappare gli attributi di un oggetto sui campi della form -->
<jsp:setProperty name="loginBean" property="*"/>

<%@page import="logic.control.LoginController"%>
<%@page import="logic.persistence.exceptions.DatabaseException"%>

<%
	System.out.println(sessionBean.getSessionName());
	System.out.println(sessionBean.getSessionSurname());
%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>GoGlobe - Login</title>
	
	<link rel="icon" href="../res/images/favicon.ico">
	<link href="../bootstrap-css/bootstrap.css" rel="stylesheet">
	<link href="../css/login.css" rel="stylesheet">
	<link href="../css/style.css" rel="stylesheet">
	<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
	<script src="js/bootstrap.min.js"></script> 
	
</head>
<body id="login-body">
		
<%
		if (sessionBean.getSessionEmail() != null) {
			%>
			  <%@ include file="html/loggedNavbar.html" %>
			<%
		} else {
			%>
			  <%@ include file="html/unloggedNavbar.html" %>
			<%
		}
%>
    
        <div class="login-wrapper">
            <div class="login-box">
                <h1 id="Login-text">Login</h1>
                
<%
	if (request.getParameter("signin") != null){
		if (loginBean.validate()){
			LoginController controller = new LoginController();
			try {
				if ((loginBean = controller.login(loginBean.getUsername(), loginBean.getPassword())) != null) {
					sessionBean.setSessionName(loginBean.getNome());
					sessionBean.setSessionSurname(loginBean.getCognome());
					sessionBean.setSessionEmail(loginBean.getUsername());
					
				%>
					<jsp:forward page="home.jsp"/>
				<%
				} else {
					%>
					<p style="color: red"> Dati Errati</p>
					<% 
				}
			} catch (DatabaseException e) {
				request.setAttribute("errType", e.getMessage());
				if (e.getCause()!= null) request.setAttribute("errLog", e.getCause().toString());
				%>
				<jsp:forward page="error.jsp"/>
				<%
			}		
		}
	}
%>
                <form action="login.jsp" name="login-form" method="POST">
                
               		<!-- username -->
                    <div class="form-group">
                    
                      <label for="username">Email address</label>
                      <input type="email" name="username" class="form-control" id="username" 
                       aria-describedby="emailHelp" placeholder="Enter email">
                      <small id="emailHelp" class="form-text text-muted">
                      We'll never share your email with anyone else.</small>
                    </div>
                    
                    <!-- password -->
                    <div class="form-group">
                      <label for="password">Password</label>
                      <input type="password" name="password" class="form-control" 
                      id=password placeholder="Password">
                    </div>
                    <!-- SIGN IN  button -->
                    <div class="signin-btn-div">
                        <input type="submit" class="btn btn-primary btn-lg" name="signin" value="Sign In"></input>
                        
                        <!-- No account, sign up -->
                        <p>Don't have an account? 
                            <a href="register.jsp" id="signup">Sign Up</a>
                        </p>
                    </div>
                  </form>
                </div> 
            </div>
</body>
</html>