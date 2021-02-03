<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<jsp:useBean id="sessionBean" scope="session" class="logic.bean.SessionBean"/>

<%@page import="logic.control.GainPointsController"%>
<%@page import="logic.bean.TripBean"%>
<%@page import="logic.persistence.exceptions.DatabaseException"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GoGlobe - Gain Points</title>
    
    <link rel="icon" href="../res/images/favicon.ico">
    <link rel="stylesheet" type="text/css" href="../bootstrap-css/bootstrap.css">
    <link rel="stylesheet" href="../css/gainpoints.css">
    <link rel="stylesheet" href="../css/style.css">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.min.js"></script>
</head>
<body id="bootstrap-override">
  	<%@ include file="html/loggedNavbar.html" %>
  	
<div class="content">
	<%@ include file="html/gainPoints.html" %>
	
	<%
		try {
			TripBean todayTrip = GainPointsController.getInstance().getTripOfTheDay(sessionBean.getSessionEmail());
			
			if (request.getParameter("gainpoints") != null) {
				if (GainPointsController.getInstance().verifyParticipation(sessionBean.getSessionEmail(), todayTrip)) {
					request.setAttribute("mess", "Trip successfully validated. You gained 100 points.");
				} else {
					request.setAttribute("mess", "Cannot validate trip. You don't gain any points.");
				}
			}
			%>
				<%@ include file="html/alertMessage.html" %>
			<%
		
			if(todayTrip != null) {
				request.setAttribute("title", todayTrip.getTitle());
				request.setAttribute("departure", todayTrip.getDepartureDate());
				request.setAttribute("return_date", todayTrip.getReturnDate());
				request.setAttribute("points", sessionBean.getSessionPoints());
			%>
				<%@ include file="html/currentTripCard.html" %>
			<%
			
			}
		} catch(DatabaseException e) {
			request.setAttribute("errType", e.getMessage());
			request.setAttribute("errLog", e.getCause().toString());
		
			%>
				<jsp:forward page="error.jsp"/>
			<%
		}
	%>
</div>
</body>
</html>