<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page autoFlush="true" buffer="1094kb"%>
<!-- declaration of a join trip bean -->
<jsp:useBean id="joinTripBean" scope="session" class="logic.bean.JoinTripBean"/>
<jsp:useBean id="sessionBean" scope="session" class="logic.bean.SessionBean"/>
<jsp:useBean id="profileBean" scope="session" class="logic.bean.ProfileBean"/>

<%@page import="java.util.List"%>      <%--Importing all the dependent classes--%> 
<%@page import="logic.bean.DayBean"%>
<%@page import="logic.bean.ActivityBean"%>
<%@page import="logic.bean.UserBean"%>
<%@page import="logic.control.JoinTripController"%>
<%@page import="logic.control.FlightController"%>
<%@page import="logic.persistence.exceptions.DatabaseException"%>
<%@page import="logic.model.exceptions.UnloggedException"%>
<%@page import="logic.model.exceptions.DuplicateException"%>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GoGlobe - JoinTrip</title>
    
    <link rel="icon" href="../res/images/favicon.ico">
    <link rel="stylesheet" href="../bootstrap-css/bootstrap.css">
    <link rel="stylesheet" href="../css/cards-bootstrap.css">
    <link rel="stylesheet" href="../css/style.css">
    <link rel="stylesheet" href="../css/tripinfo.css">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.min.js"></script>
</head>
<body id="bootstrap-override">

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
    	<div class="trip-info displayer">
    	<div class="trip-title">
    		
    		<%
 			request.setAttribute("tripTitle", joinTripBean.getTrip().getTitle());
    		%>
    		<%@ include file="html/tripTitle.html" %>
    		<%
    		try {
    			if (request.getParameter("jointrip") != null ){
     				JoinTripController controller = new JoinTripController();
     				if (controller.sendRequest(joinTripBean.getTrip().getTitle(), sessionBean.getSessionEmail())) {
     					request.setAttribute("message", "Request to join sent to the organizer.");
     				} else {
     					request.setAttribute("message", "You cannot join this trip, please choose another trip.");
     				}
    			}
			} catch (UnloggedException | DuplicateException e) {
 				System.out.println(e.getMessage());
				request.setAttribute("message", e.getMessage());
			}
    		%>
    		<h6 style="color: red">${message}</h6>
    	</div>
    	
    		<ul class="nav nav-tabs">
    			<li class="nav-item">
    				<a class="nav-link active" href="#general" data-toggle="tab">General Info</a>
    			</li>
    			
    			<!--  one tab for each day of the trip -->
    			<%
    				List<DayBean> days = joinTripBean.getTrip().getDays();
					System.out.println(days);
    				System.out.println(days.size());
    					
    				for (int i=0; i<days.size(); i++) {
    					DayBean dayBean = days.get(i);
    					System.out.println(dayBean);
    					%>
    					<li class="nav-item">
    						<a class="nav-link" href="#day<%= i+1 %>" data-toggle="tab">Day <%= i+1 %></a>
    					</li>
						<%
    				}    			
    			%>
    			<li class="nav-item">
    				<a class="nav-link" href="#users" data-toggle="tab">Participants</a>
    			</li>
    		</ul>
    		<div class="tab-content">
 
 			<%
 			request.setAttribute("orgName", joinTripBean.getTrip().getOrganizer().getName());
 			request.setAttribute("orgSurname", joinTripBean.getTrip().getOrganizer().getSurname());
 			request.setAttribute("tripCat1", joinTripBean.getTrip().getCategory1());
 			request.setAttribute("tripCat2", joinTripBean.getTrip().getCategory2());
 			request.setAttribute("tripDesc", joinTripBean.getTrip().getDescription());
 			request.setAttribute("tripPrice", joinTripBean.getTrip().getPrice());
 			request.setAttribute("depDate", joinTripBean.getTrip().getDepartureDate());
 			request.setAttribute("retDate", joinTripBean.getTrip().getReturnDate());
 			
 			try {
 				FlightController flightCtrl = new FlightController();
 				flightCtrl.loadFlightInfo(joinTripBean.getTrip());
 				request.setAttribute("flightOri", joinTripBean.getTrip().getFlight().getOriginAirport());
 				request.setAttribute("flightArr", joinTripBean.getTrip().getFlight().getDestAirport());
 				request.setAttribute("carrier", joinTripBean.getTrip().getFlight().getCarrier());
 				int ticket = joinTripBean.getTrip().getFlight().getPrice();
 				if (ticket == 0) {
 					request.setAttribute("ticket", "N/D");
 				} else {
 					request.setAttribute("ticket", ticket);
 				}
 			} catch (DatabaseException e) {
 				request.setAttribute("errType", e.getMessage());
 				if (e.getCause()!=null) request.setAttribute("errLog", e.getCause());
 				%>
 				<jsp:forward page="error.jsp"/>
 				<%
 			}
 			
			if (request.getParameter("viewprofile") != null) {
				System.out.println(joinTripBean.getTrip().getOrganizer().getName());
				profileBean.setUser(joinTripBean.getTrip().getOrganizer());
				%>
				<jsp:forward page="profile.jsp"/>
				<% 
			}
    		
    		for (int i=0; i<days.size(); i++) {
    			DayBean dayBean = days.get(i);
    			System.out.println(dayBean);
    			System.out.println(days.size());
    		%>
    			<div class="tab-pane" role="tabpanel" id="day<%= i+1 %>">
					<div class="day-location">
						<h3>Location:</h3>
    					<h4 id="loc"><%= dayBean.getLocationCity() %></h4>
					</div>
    				<h3 id="panel-title">Activities of the day:</h3>
    				<div class="list scrollable">
    				<%
        				for (int j=0; j<dayBean.getActivities().size(); j++) {
        					ActivityBean activityBean = dayBean.getActivities().get(j);
        					System.out.println(activityBean.getTitle());
        					request.setAttribute("activityTitle", activityBean.getTitle());
        					request.setAttribute("activityTime", activityBean.getTime());
        					request.setAttribute("activityDesc", activityBean.getDescription());
        					request.setAttribute("activityPrice", activityBean.getEstimatedCost());
        					
        					%>
        					<%@ include file="html/activityItem.html" %>
        					<%
        				}
        			%>
    					
    				</div>
        		</div>
        	<%
	    		}
    		
 			%>
    		
    		<%@ include file="html/tripInfo.html" %>
 
    			
    		<%
    		
    		%>
    			
    		<!-- separator to inferior part -->
        	<div class="tab-pane" role="tabpanel" id="users">
	        	<div class="participants list scrollable">
	        		<h3 id="panel-title">Current participants:</h3>
	        		<%
	        			List<UserBean> travelers = joinTripBean.getTrip().getParticipants();
	        			for (UserBean bean: travelers) {
	        				request.setAttribute("userName", bean.getName());
	        				request.setAttribute("userSurname", bean.getSurname());
	        				
							if (request.getParameter("viewparticipant") != null) {
								System.out.println(bean.getName()+bean.getSurname());
								profileBean.setUser(bean);
								%>
								<jsp:forward page="profile.jsp"/>
								<% 
							}
							
							%>
							<%@ include file="html/userItem.html" %>
							<%
	        			}
					%>
	        	</div>
    		</div>
    	</div>    
    </div>		
</body>
</html>