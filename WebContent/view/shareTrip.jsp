<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
   <%@page import="logic.control.PlanTripController"%>
   <%@page import="logic.persistence.exceptions.DatabaseException"%>
      <%@page import="logic.model.exceptions.FormInputException"%>
   
    
   <jsp:useBean id="planTripBean" scope="session" class="logic.bean.PlanTripBean"/> 
   <jsp:useBean id="tripBean" scope="session" class="logic.bean.TripBean"/> 
   <jsp:useBean id="sessionBean" scope="session" class="logic.bean.SessionBean"/>
   <jsp:setProperty name="tripBean" property="*"/>
   
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
    <title>GoGlobe - Select trip preferences</title>    
    <link rel="icon" href="../res/images/favicon.ico">
    <link rel="stylesheet" type="text/css" href="../bootstrap-css/bootstrap.css">
    <link rel="stylesheet" href="../css/planTrip.css">
    <link rel="stylesheet" href = "../css/style.css">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.min.js"></script>
</head>
<body id="selectPreferences-body">
    
	<%@ include file="html/loggedNavbar.html" %>

    <!-- PAGE CONTENT -->
    <div id="preferences-bg">
        <!-- preferences form-->
        <div id="preferences-form">
        <h2>To share your trip insert the following info.</h2>
<%
	if (request.getParameter("share-btn") != null){
		try {
				planTripBean.validateSharingPref();
				tripBean.setShared(true);
	    		PlanTripController.getInstance().saveTrip(planTripBean.getTripBean(), sessionBean); 
				System.out.println("VIAGGIO SALVATO COME CONDIVISO");
	%>		
				
				<jsp:forward page="home.jsp"/>
	
				
	<%
		} catch (DatabaseException e) {
			request.setAttribute("errType", e.getMessage());
			if (e.getCause()!=null) request.setAttribute("errLog", e.getCause().toString());
			%>
			 <jsp:forward page="error.jsp"/>
			<%
		} catch (FormInputException e){
%>
			<p style="color: red"><%= e.getMessage() %></p>
<% 
			planTripBean.getTripBean().setMaxParticipants("");
			planTripBean.getTripBean().setMaxAge("");
			planTripBean.getTripBean().setMinAge("");
			planTripBean.getTripBean().setDescription("");
		}
	}
%>
            <form method="POST" action="shareTrip.jsp">
            
            		
            		<div class="form-row">
					    <div class="form-group col-md-3">
					      <label >Max participants</label>
					      <input type="text" class="form-control" name="maxParticipants" id="inputMaxParticipants" placeholder="Insert max participants for your trip">
					    </div>
				    </div>
            	   <div class="form-row">
				    <div class="form-group col-md-6">
				      <label>Min age</label>
				      <input type="text" class="form-control" name="minAge" id="inputMinAge" placeholder="Insert minimum age">
				    </div>
				    <div class="form-group col-md-6">
				      <label>Max age</label>
				      <input type="text" class="form-control" name="maxAge" id="inputMaxAge" placeholder="Insert maximum age">
				    </div>
				  </div>
            	
                  <div class="form-group">
				    <label for="exampleFormControlTextarea1">Description</label>
				    <textarea name="description" class="form-control" id="tripDescription-ta" rows="3" placeholder="Describe your trip to other users!"></textarea>
				  </div>
                
                  <div class="custom-file">
					  <input type="file" class="custom-file-input" id="customFile">
					  <label class="custom-file-label" for="customFile">Choose image</label>
				  </div>
                  
                  <div id="form-btns" class="form-btns">
                      <button type="submit" class="btn btn-secondary btn-lg" name="back-btn">Back</button>
                      <button type="submit" class="btn btn-primary btn-lg preferences-btn" name="share-btn">Share</button>
                  </div>
                
            </form>
        </div>
    </div>
</body>
</html>