<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<!-- declaration of a  register bean -->
<jsp:useBean id="registerBean" scope="request" class="logic.bean.RegistrationBean"/>
<jsp:useBean id="sessionBean" scope="session" class="logic.bean.SessionBean"/>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GoGlobe - Home</title>
    
    <link rel="icon" href="../res/images/favicon.ico">
    <link rel="stylesheet" type="text/css" href="../bootstrap-css/bootstrap.css">
    <link rel="stylesheet" href="../css/home.css">
    <link rel="stylesheet" href="../css/style.css">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.min.js"></script>
</head>
<body id="bootstrap-override-home">
	
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
    
    <!-- Welcome tag -->
    <div class="welcome">
    	<h1>Welcome <jsp:getProperty name="sessionBean" property="sessionName"/> <jsp:getProperty property="sessionSurname" name="sessionBean"/></h1>
    </div>
    <!-- get started -->
    <div class="get-started">
        <div class="join">
        	<form action="home.jsp" method="POST">
        		<h3>Join an organized trip with other people</h3>
            	<button type="submit" name="jointrip" class="btn btn-primary">Join a Trip</button>
          		<%
          			if (request.getParameter("jointrip") != null) {
          		%>
          				<jsp:forward page="joinTrip.jsp"/>
          		<%
          			}
          		%>
        	</form>
        </div>
        <div class="plan">
        	<form action="home.jsp" method="POST">
        		<h3>Plan your personal trip and share with the community</h3>
            	<button type="submit" name="plantrip" class="btn btn-primary">Plan a Trip</button>
            	<%
          			if (request.getParameter("plantrip") != null) {
          		%>
          				<jsp:forward page="SelectTripPreferences.jsp"/>
          		<%
          			}
          		%>
        	</form>
        </div>
    </div>
    

</body>
</html>