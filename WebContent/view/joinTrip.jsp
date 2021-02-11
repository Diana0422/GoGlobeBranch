<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!-- declaration of a join trip bean -->
<jsp:useBean id="joinTripBean" scope="session" class="logic.bean.JoinTripBean"/>
<jsp:useBean id="sessionBean" scope="session" class="logic.bean.SessionBean"/>

<%@page import="java.util.List"%>      <%--Importing all the dependent classes--%>
<%@page import="java.util.Iterator"%> 
<%@page import="java.util.concurrent.TimeUnit"%> 
<%@ page autoFlush="true" buffer="1094kb"%>

<%@page import="logic.control.JoinTripController"%>
<%@page import="logic.bean.TripBean"%>
<%@page import="logic.bean.DayBean"%>
<%@page import="logic.model.FilterType"%>
<%@page import="logic.model.TripCategory"%>
<%@page import="logic.view.filterstrategies.StrategyContext"%>
<%@page import="logic.persistence.exceptions.DatabaseException"%>
<%@page import="logic.model.exceptions.APIException"%>
<%@page import="logic.view.filterstrategies.TripFilterManager"%>
<%
	if (request.getParameter("search") != null ) {
    %>
    	<!-- map class attributes to values of the form -->
    	<jsp:setProperty name="joinTripBean" property="searchVal"/>     
    <%
      	 System.out.println(joinTripBean.getSearchVal());
      	 /*response.setIntHeader("refresh", 0);*/
   }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GoGlobe-JoinTrip</title>
    
    <link rel="icon" href="../res/images/favicon.ico">
    <link rel="stylesheet" href="../bootstrap-css/bootstrap.css">
    <link rel="stylesheet" href="../css/cards-bootstrap.css">
    <link rel="stylesheet" href="../css/style.css">
    <link rel="stylesheet" href="../css/jointrip.css">
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
     <div class="content-join-trip">

		<%@ include file="html/joinTripHeader.html" %>
        
        <%
          	if (request.getParameter("plantrip") != null) {
          		if (sessionBean.getSessionEmail() == null) {
          			%>
          			<jsp:forward page="login.jsp"/>
          			<%
          		} else {
        		%>
          		<jsp:forward page="SelectTripPreferences.jsp"/>
         		<%
          		}
          	}
         %>
	</div>
	<div class="trips">
        <!--cards for the results-->
        <form action="joinTrip.jsp" method="POST">
        <div class="results scrollable">
        	
        	<%
        		JoinTripController controller = new JoinTripController();
        		TripFilterManager filterManager = new TripFilterManager();
        		List<TripBean> trips = null;
				try {
					if (joinTripBean.getSearchVal() != null) {
						System.out.println("Searchval is not null.");
						System.out.println(joinTripBean.getSearchVal());
						joinTripBean.setObjects(controller.searchTrips(joinTripBean.getSearchVal()));
		        		System.out.println(joinTripBean.getObjects());
		        		joinTripBean.setSearchVal(null);
					} else {
						System.out.println("Searchval is null.");
						System.out.println(joinTripBean.getSearchVal());
						joinTripBean.setObjects(controller.getSuggestedTrips(sessionBean.getSessionEmail()));
		        		System.out.println(joinTripBean.getObjects());
					}
				} catch(DatabaseException e) {
					request.setAttribute("errType", e.getMessage());
					if (e.getCause()!=null)request.setAttribute("errLog", e.getCause().toString());
					%>
					<jsp:forward page="error.jsp"/>
					<%
				}
				
        		//If ADVENTURE filter button is clicked
        		if (request.getParameter("btn-adv-filter")!= null){
        			joinTripBean.setFilteredTrips(controller.applyFilterToTrips(joinTripBean.getObjects(), null, TripCategory.ADVENTURE));
        			trips = joinTripBean.getFilteredTrips();
        			System.out.println("# of filtered trips is: " + joinTripBean.getFilteredTrips().size());
        		}
        		//If CULTURE filter button is clicked
				if (request.getParameter("btn-clt-filter")!= null ){
	    			joinTripBean.setFilteredTrips(controller.applyFilterToTrips(joinTripBean.getObjects(), null, TripCategory.CULTURE));
	    			trips = joinTripBean.getFilteredTrips();
	    			System.out.println("# of filtered trips is: " + joinTripBean.getFilteredTrips().size());
        		}
				//If RELAX filter button is clicked
				if (request.getParameter("btn-rlx-filter")!= null){
	    			joinTripBean.setFilteredTrips(controller.applyFilterToTrips(joinTripBean.getObjects(), null, TripCategory.RELAX));
	    			trips = joinTripBean.getFilteredTrips();
	    			System.out.println("# of filtered trips is: " + joinTripBean.getFilteredTrips().size());
        		}
				//If FUN filter button is clicked
				if (request.getParameter("btn-fun-filter")!= null){
	    			joinTripBean.setFilteredTrips(controller.applyFilterToTrips(joinTripBean.getObjects(), null, TripCategory.FUN));
	    			trips = joinTripBean.getFilteredTrips();
	    			System.out.println("# of filtered trips is: " + joinTripBean.getFilteredTrips().size());
        		} 
				//If ALPHABETICAL filter button is clicked
				if (request.getParameter("btn-alphab-filter")!= null){
	    			joinTripBean.setFilteredTrips(controller.applyFilterToTrips(joinTripBean.getObjects(), FilterType.ALPHABETIC, null));
	    			trips = joinTripBean.getFilteredTrips();
	    			System.out.println("# of filtered trips is: " + joinTripBean.getFilteredTrips().size());
        		}
				//If PRICE filter button is clicked
				if (request.getParameter("btn-price-filter")!= null){
	    			joinTripBean.setFilteredTrips(controller.applyFilterToTrips(joinTripBean.getObjects(), FilterType.PRICE, null));
	    			trips = joinTripBean.getFilteredTrips();
	    			System.out.println("# of filtered trips is: " + joinTripBean.getFilteredTrips().size());
        		}
			

    			if(!joinTripBean.getObjects().isEmpty()) {
    				if (trips == null){
    					trips = joinTripBean.getObjects();
    				}
    				
    				for (TripBean trip: trips){
    					System.out.println(trip.getTitle());
    				}
    				
    				if (trips != null){
    					Iterator<TripBean> iter = trips.iterator();
    		
    					int elemsInRow=0;
    					Integer idx = 0;
    					while(iter.hasNext()) {
    						TripBean trip = iter.next();
    						idx++;
    						
    						if (elemsInRow == 0) {
    							%>
    		        			<div class="row" style="height: fit-content">
    							<%
    						}
    						if (elemsInRow%3 == 0 && elemsInRow != 0) {
    							%>
    							</div>
    							<div class="row" style="height: fit-content">
    							<%
    						}
    						if (!trip.getTitle().equals("")) {
    							elemsInRow++;
    							System.out.println("elemsInRow: "+elemsInRow);
    							System.out.println("jsp: trip= "+trip);
    							request.setAttribute("title", trip.getTitle());
    							request.setAttribute("price", trip.getPrice());
    							request.setAttribute("minAge", trip.getMinAge());
    							request.setAttribute("maxAge", trip.getMaxAge());
    							request.setAttribute("spots", trip.getAvailability());
    							request.setAttribute("cat1", trip.getCategory1());
    							request.setAttribute("cat2", trip.getCategory2());
    							request.setAttribute("btninfoid", idx);
    							%>
    							<%@ include file="html/tripCard.html" %>  			
								<%
    						}
    					}
    				}
    			}
        	%>
        	</div>
        	
        </div>
    		<%@ include file="html/filters.html" %>

		<%
		  if(request.getParameter("viewinfo") != null) {
       	  	int tripNum = Integer.parseInt(request.getParameter("viewinfo"));
        	System.out.println("Button pressed: "+request.getParameter("viewinfo"));
        	System.out.println(joinTripBean.getObjects().size());
        	joinTripBean.setTrip(joinTripBean.getObjects().get(tripNum-1));
        	System.out.println(joinTripBean.getObjects().get(tripNum-1));
           %>
           	<jsp:forward page="tripInfo.jsp"/>
           <%
        }
        %>
        </form>
 </div>
    
</body>
</html>