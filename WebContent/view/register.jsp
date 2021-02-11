<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@page import="logic.control.RegistrationController"%>
<%@page import="logic.persistence.exceptions.DatabaseException"%>

<!-- declaration and initialization of a register bean -->
<jsp:useBean id="registerBean" scope="request" class="logic.bean.RegistrationBean" />
<jsp:useBean id="sessionBean" scope="session" class="logic.bean.SessionBean"/>

<!-- map attributes of the bean from the form fields -->
<jsp:setProperty name="registerBean" property="*"/>
    
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GoGlobe - Register</title>
    
    <link rel="icon" href="../res/images/favicon.ico">
    <link rel="stylesheet" type="text/css" href="../bootstrap-css/bootstrap.css">
    <link rel="stylesheet" href="../css/style.css">
    <link rel="stylesheet" href="../css/register-style.css">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.min.js"></script>
</head>
<body id="bootstrap-override">

<%
	RegistrationController controller = new RegistrationController();
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

    <!-- content -->
    <div class="content-register">
        <div class="container">

            <!-- registration form -->
            <div class="registration-form">
                <form action="register.jsp" name="myform" method="POST">
                <%
    				if (request.getParameter("signin") != null) {
    					try {
        					if (registerBean.validate()) {
        						registerBean.setSession(controller.register(registerBean.getRegBeanEmail(), registerBean.getPassword(), registerBean.getRegBeanName(), registerBean.getRegBeanSurname(), registerBean.getBirthday()));
        						sessionBean.setSessionEmail(registerBean.getSession().getSessionEmail());
        						sessionBean.setSessionName(registerBean.getSession().getSessionName());
        						sessionBean.setSessionSurname(registerBean.getSession().getSessionSurname());
        						sessionBean.setSessionPoints(registerBean.getSession().getSessionPoints());
							%>
        					 <jsp:forward page="home.jsp"/>
							<%
        					} else {
        						System.out.println("No data.\n");
							%>
								<p style="color: red">This user is already registered.</p>
							<%
        					}	
    					} catch (DatabaseException e) {
    						request.setAttribute("errType", e.getMessage());
    						if (e.getCause()!=null) request.setAttribute("errLog", e.getCause().toString());
    						%>
    						 <jsp:forward page="error.jsp"/>	
    						<%
    					}
    				}
				%>
                    <div class="form-row">
                        <h1>Register</h1>
                    </div>
                    
                    <!-- email and password -->
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label for="inputEmail4">Email</label>
                            <input type="email" name="regBeanEmail" class="form-control" id="inputEmail4" aria-describedby="emailHelp" placeholder="Email">
                        </div>
                        <div class="form-group col-md-6">
                            <label for="inputPassword4">Password</label>
                            <input type="password" name="password" class="form-control" id="inputPassword4" placeholder="Password">
                        </div>
                    </div>
                    <!-- name and surname -->
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label for="inputName2">Name</label>
                            <input type="text" name="regBeanName" class="form-control" id="inputName2" placeholder="Name">
                        </div>
                        <div class="form-group col-md-6">
                            <label for="inputSurname2">Surname</label>
                            <input type="text" name="regBeanSurname" class="form-control" id="inputSurname2" placeholder="Surname">
                        </div>
                    </div>
                    <!-- date of birth and password confirmation -->
                    <div class="form-row">
                    	<div class="form-group col-md-6">
                    		<label for="date">Birthday</label>
                    		<input class="form-control" id="date" name="birthday" placeholder="mm/dd/yyyy" type="text"/>
                    	</div>
                    	<div class="form-group col-md-6">
                    		<label for="password-confirm">Confirm Password</label>
                    		<input type="password" name="passwordconf" class="form-control" id="password-confirm" placeholder="Confirm Password">
                    	</div>
                    </div>
                    <hr>
                    <button type="submit" name="signin" class="btn btn-primary">Sign in</button>
                </form>
            </div>

            <!-- Page description -->
            <div class="description">
                <h2 class="page-state">plan your trip, share it with the community and you are ready to go.</h2>
            </div>
        </div>
    </div>
</body>
</html>