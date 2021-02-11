<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@page import="java.util.List"%> 
<%@page import="java.awt.Desktop"%>      
<%@page import="java.net.URL"%>      
<%@page import="java.util.Iterator"%> 
<%@page import="logic.model.Trip"%>
<%@page import="logic.model.Day"%>
<%@page import="logic.model.PlaceBean"%>
<%@page import="logic.control.PlanTripController"%>
<%@page import="logic.persistence.exceptions.DatabaseException"%>
<%@page import="logic.model.exceptions.TripNotCompletedException"%>
<%@page import="logic.model.exceptions.FormInputException"%>
<%@page import="logic.model.exceptions.APIException"%>
<jsp:useBean id="planTripBean" scope="session" class="logic.bean.PlanTripBean"/>
<jsp:useBean id="tripBean" scope="session" class="logic.bean.TripBean"/>
<jsp:useBean id="activityBean" scope="request" class="logic.bean.ActivityBean"/> 
<jsp:useBean id="sessionBean" scope="session" class="logic.bean.SessionBean"/>
<jsp:useBean id="dayBean" scope="request" class="logic.bean.DayBean"/>

<jsp:setProperty name="activityBean" property="*" />
<jsp:setProperty name="planTripBean" property="location" />

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
    <title>GoGlobe - Plan Trip</title>
    
    <link rel="icon" href="../res/images/favicon.ico">
    <link rel="stylesheet" href="../bootstrap-css/bootstrap.css">
    <link rel="stylesheet" href="../css/planTrip.css">
    <link rel="stylesheet" href="../css/style.css">
    
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.min.js"></script>
</head>
<body id="bootstrap-override">

	<% List<PlaceBean> suggestions = null;
	   PlanTripController controller = new PlanTripController();
	%>
    
	<%@ include file="html/loggedNavbar.html" %>
    
    
    <div class="sb-wrapper">
        <!-- SIDEBAR -->
        <nav id="sidebar" aria-label="sidebar">
            <form  action="planTrip.jsp" method="POST">	
            	<div class="sidebar-header">
            	
            	<button type="submit" name="save-trip-btn" class="btn btn-primary btn-lg btn-block">Save Trip</button>
            	
            	<div class="separator"> OR </div>
            	
            	<button type="submit" name="share-trip-btn" class="btn btn-primary btn-lg btn-block">Share Trip</button>
            	
<% 
			if (planTripBean.getTripBean().getDays() != null){
				int i = 0;
				while(i < planTripBean.getTripBean().getDays().size()){
%>				
	                <button type="submit" class="btn btn-colors btn-lg btn-block" name="daybtn" value=<%= i%> >Day <%= i + 1 %></button>	               	    
<%
					i++;
				}
			}
%>
<%
			if (request.getParameter("daybtn") != null){
				planTripBean.setPlanningDay(Integer.parseInt(request.getParameter("daybtn")));
			}
%>  
				
           		</div> 
           </form>    
       </nav>
	   
        <!-- PAGE CONTENT -->
        <div class="plantrip-content">
            <!-- DAY DESCRIPTION-->
            
            <div class="day" id="day">
            	<div class="form">
					<div id="header">
						<h1 id="trip-title"><%= tripBean.getTitle() %></h1>
	            		<h1 id="country"><%= tripBean.getCountry() %></h1>
					</div>
	           
	           <!--  IF SAVE TRIP IS CLICKED -->  
				<%
				if (request.getParameter("save-trip-btn") != null){
					try {
						planTripBean.validateTrip();
				    		controller.saveTrip(tripBean, sessionBean.getSessionEmail()); 
							%>
							<jsp:forward page="home.jsp"/>
							<% 
						
					}catch (TripNotCompletedException e){
						%>
						<!-- VALIDATE TRIP ERROR -->
						<p style="color: red"><%= e.getMessage() %></p>	
						<%	
					} catch (DatabaseException e) {
						request.setAttribute("errType", e.getMessage());
						if (e.getCause()!=null) request.setAttribute("errLog", e.getCause().toString());
						%>
						 <jsp:forward page="error.jsp"/>
						<%
					}				
				}
				%>
			<!--  IF SHARE TRIP IS CLICKED -->
<%
				if (request.getParameter("share-trip-btn") != null){
					try{
						
					planTripBean.validateTrip();
					
%>
						<jsp:forward page="shareTrip.jsp"/>
<% 
					}catch(TripNotCompletedException e){
%>
						<!-- VALIDATE TRIP ERROR -->
						<p style="color: red"><%=e.getMessage() %></p>	
<%				
					}				
				}
%>

	            <h2>Day <%= planTripBean.getPlanningDay() + 1 %></h2>   
  		
 <%		
		 if (request.getParameter("save-location-btn") != null){
			try {
				 if (controller.checkLocationValidity(planTripBean.getLocation(), planTripBean.getTripBean())) {
					 if (planTripBean.validateLocation()){
							planTripBean.saveLocation();
							planTripBean.setLocation("");
						 }
				 } else {
					 request.setAttribute("locErr", "the location is not in country "+planTripBean.getTripBean().getCountry());
				 }
			} catch (APIException e) {
				request.setAttribute("locErr", "the location doesn't exist");
			}
		}
 
 		if (planTripBean.checkDay()){
 			
 
 %>             
               	<!--  Location Form -->
			   <form method="POST" action="planTrip.jsp">
			   	 <div class="location-form form">
			   	 	  <h6 style="color: red">${locErr}</h6>
				   	  <div class="form-group row">
					    <label for="inputPassword" class="col-sm-2 col-form-label"><h6 style="width: fit-content;">Location</h6></label>
					    <div class="col-sm-10">
					      <input type="text" name="location" class="form-control" id="inputLocation" style="width: 100%M" placeholder="Insert Location...">
					    </div>
					  </div>
                	<button type="submit" class="btn btn-colors btn-lg btn-block" name="save-location-btn"  >Save Location</button>	               	                           	
                   </div> 
               </form>
               
 <%
 		}else{
 %>            
               
             	<h3>Location: <%=planTripBean.getTripBean().getDays().get(planTripBean.getPlanningDay()).getLocationCity() %></h3>
             </div>
                <div class="day-plan">
                	<!-- NEW ACTIVITY FORM -->
                	<form action="planTrip.jsp" method="POST">
                  		<div class="activity-form">
<%
		if (request.getParameter("save-activity-btn") != null){
			try{
				if (activityBean.validateActivity()){	
					controller.addActivity(planTripBean.getTripBean(), planTripBean.getPlanningDay(), activityBean);
				}else{
					activityBean.setDescription("");
					activityBean.setTitle("");
					activityBean.setEstimatedCost("");
					activityBean.setTime("");
%>
					<p style="color: red">ERRORE</p>
<% 
				}
			}catch(FormInputException e){
%>
				<p style="color: red"><%=e.getMessage() %></p>	
<% 
		}
	}		                    
%>
                        	<div class = "flex-form-row">
                        		<h4>Title</h4>
                        		<input type="text" name="title" id="act-title"
                        		placeholder="Enter activity name" maxlength="15">
                        	</div>
                        	<div class = "flex-form-row">
                        		<h4>Time</h4>
                        		<input type="text" name="time" id="act-time"
                        		placeholder="hh:mm" maxlength="5">
                        	</div>
                        	<div class = "flex-form-row">
                        		<h4>Cost</h4>
                        		<input type="text" name="estimatedCost" id="act-cost"
                        		placeholder="Insert activity cost">
                        	</div>
                        	<div class = "activity-description">
                        		<h4>Plan</h4>
                               <textarea class="activity-plan" name="description" style="resize:none;"></textarea>
                        		
                        	</div>
                        	<button type="submit" class="btn btn-colors btn-lg btn-block" name="save-activity-btn"  >Add activity</button>	               	                           	
                        </div> 
                    </form>
                    <div>
                   	 <!-- ACTIVITIES -->
 <%
						int activitiesNum = planTripBean.getTripBean().getDays().get(planTripBean.getPlanningDay()).getActivities().size();
					 	for ( int j = 0; j < activitiesNum; j++){ 
					 		System.out.println("Activity Found!\n");
	            		
%>		
	            			  
	            			<div class="activity form">		
		            			<h2><%=planTripBean.getTripBean().getDays().get(planTripBean.getPlanningDay()).getActivities().get(j).getTitle() %></h2>
		            			<h3><%=planTripBean.getTripBean().getDays().get(planTripBean.getPlanningDay()).getActivities().get(j).getTime() %></h3>
		            			<h3><%=planTripBean.getTripBean().getDays().get(planTripBean.getPlanningDay()).getActivities().get(j).getEstimatedCost() + "€" %></h3>
		            			<div class="description">
		            				<p><%=planTripBean.getTripBean().getDays().get(planTripBean.getPlanningDay()).getActivities().get(j).getDescription() %></p>
		            			</div>		            			
	         				</div>                        
 <%
					 	}
 	}
 %>                  
 					</div>        
	            </div>	            
		     </div>
		  </div>
		  <div class="place-suggestions scrollable">
<%
		if (!(planTripBean.checkDay())){
					
			suggestions = controller.getNearbyPlaces(planTripBean.getTripBean().getDays().get(planTripBean.getPlanningDay()).getLocationCity(),
															tripBean.getCategory1());
			for (int i = 0; i < suggestions.size(); i++){
%>
				
				<!--  INSERIRE LAYOUT SUGGESTION QUI -->
		       		      
<%
				request.setAttribute("ic_src" , suggestions.get(i).getIcCategoryRef());
				request.setAttribute("placeName" , suggestions.get(i).getName());
				request.setAttribute("placeAddress" , suggestions.get(i).getAddress());
				request.setAttribute("placeOH" , suggestions.get(i).getOpeningHours());
				request.setAttribute("suggestion_id" , i);
%>			
				 <form>
				 
					<%@ include file="html/suggestionCard.html" %>
	       
		         </form>


<% 			
			}
%>		  		  
		  </div>
<%
		}
%>
	   </div>
	   
<%
	if (request.getParameter("btn-lookup-place") != null){
		PlaceBean suggestion = suggestions.get(Integer.parseInt(request.getParameter("btn-lookup-place")));
    	String url = "https://maps.google.com/?q=" + suggestion.getName().replace(" ", "+");
		Desktop.getDesktop().browse(new URL(url).toURI());
	}
%>
	  
    
</body>
</html>