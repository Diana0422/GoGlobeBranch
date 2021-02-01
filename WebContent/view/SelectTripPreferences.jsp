<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="logic.control.PlanTripController"%>
    <%@page import="java.util.Date"%>    
    <%@page import="logic.bean.TripBean"%>
     <%@page import="java.text.SimpleDateFormat"%>
    <%@page import="logic.model.exceptions.FormInputException"%>
    <%@page import="logic.control.FormatManager"%>
    
    
   <jsp:useBean id="planTripBean" scope="session" class="logic.bean.PlanTripBean"/>    
   <jsp:useBean id="tripBean" scope="session" class="logic.bean.TripBean"/>
   <jsp:setProperty name="tripBean" property="*"/>
   
<% 

	final String DATE_FORMAT = "dd/MM/yyyy";
   if (request.getParameter("back-btn")!= null){
%>
	   <jsp:forward page="home.jsp"/>
<% 
   }
%>

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
        <h2>Insert information about your trip!</h2>
<%
	if (request.getParameter("next-btn") != null){
		try{	
			planTripBean.setTripBean(tripBean);
			if (planTripBean.validateForm()){
				Date depDate = FormatManager.parseDate(tripBean.getDepartureDate());
				Date retDate = FormatManager.parseDate(tripBean.getReturnDate());
				long tripLength = PlanTripController.getInstance().calculateTripLength(depDate, retDate) + 1;
				System.out.println(tripLength);
				tripBean.setTripLength(tripLength);
				tripBean.createDays();				
				planTripBean.setPlanningDay(0);
%>			
				<jsp:forward page="planTrip.jsp"/>			
<% 
			}
		}catch(FormInputException e){
			tripBean.setTitle("");
			tripBean.setDepartureDate("");
			tripBean.setReturnDate("");
			tripBean.setCategory1("");
			tripBean.setCategory2("");
		
%>
			<p style="color: red"><%=e.getMessage() %></p>
<%
		}
	}
%>
            <form method="POST" action="SelectTripPreferences.jsp">
                <div class="form-row">
                    <!-- Trip name-->
                    <div class="form-group col-md-6">
                        <label for="inputDepDate" ><h4>Trip Title</h4></label>
                        <input type="text" class="form-control" name="title" id="tripName" maxlength="25" placeholder="Enter trip name...">
                
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group col-md-6">
                        <label for="inputDepDate"><h4>Departure Date</h4></label>
                        <input type="text" class="form-control" name="departureDate" id="depDate"
                        placeholder="dd/mm/yyyy">
                    </div>
                    <div class="form-group col-md-6">
                        <label for="inputRetDate"><h4>Return Date</h4></label>
                        <input type="text" class="form-control" name="returnDate" id="retDate"
                        placeholder="dd/mm/yyyy" >
                    </div>
                </div>

                <div class="form-row form-categories">              
                    <div class="form-group col-md-6">
                        <label for="selectCategory1"><h4>Trip category 1</h4></label>
        
                        <select class="custom-select" id="selectCategory1" name="category1">
                            <option selected value="none">Choose...</option>
                          	<option value="Adventure">Adventure</option>
                            <option value="Relax">Relax</option>
                            <option value="Culture">Culture</option>
                            <option value="Fun">Fun</option>
                    </select>
                    </div>  
                    <div class="form-group col-md-6">
                        <label for="selectCategory2"><h4>Trip category 2</h4></label>
        
                        <select class="custom-select" id="selectCategory2" name="category2">
                            <option selected value="none">Choose...</option>
                            <option value="Adventure">Adventure</option>
                            <option value="Relax">Relax</option>
                            <option value="Culture">Culture</option>
                            <option value="Fun">Fun</option>
                    </select>
                    </div>
                    
                    <div id="form-btns" class="form-btns">
                        <button type="submit" class="btn btn-secondary btn-lg" name="back-btn">Back</button>
                        <button type="submit" class="btn btn-primary btn-lg preferences-btn" name="next-btn">Next</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</body>
</html>