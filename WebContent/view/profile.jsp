<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page autoFlush="true" buffer="1094kb"%>
    
<jsp:useBean id="profileBean" scope="session" class="logic.bean.ProfileBean"/>
<jsp:useBean id="userBean" scope="request" class="logic.bean.UserBean"/>
<jsp:useBean id="sessionBean" scope="session" class="logic.bean.SessionBean"/>
<jsp:useBean id="joinTripBean" scope="session" class="logic.bean.JoinTripBean"/>

<%@page import="logic.bean.ReviewBean"%>
<%@page import="logic.bean.TripBean"%>
<%@page import="logic.control.ReviewUserController"%>
<%@page import="logic.control.ProfileController"%>
<%@page import="logic.persistence.exceptions.DatabaseException"%>
<%@page import="logic.model.exceptions.UnloggedException"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="logic.bean.DayBean"%>
<jsp:setProperty name="profileBean" property="comment"/>
<jsp:setProperty name="profileBean" property="title"/>


<% 
	ProfileController controller = new ProfileController();
	ReviewUserController reviewCtrl = new ReviewUserController();

	userBean = profileBean.getUser();
	if (userBean == null) {
		userBean = controller.getProfileUser(sessionBean.getSessionEmail());
	}
	
	List<TripBean> myTripBeans = null;
	List<TripBean> upcomingTripBeans = null;
	List<TripBean> previousTripBeans = null;
	
	upcomingTripBeans = controller.getUpcomingTrips(userBean.getEmail());
	previousTripBeans = controller.getRecentTrips(userBean.getEmail());
	myTripBeans = controller.getMyTrips(userBean.getEmail());
	
	if (request.getParameter("prevBtn") != null){
		System.out.println("qualcosa");
		int idx = Integer.parseInt(request.getParameter("prevBtn"));
			joinTripBean.setTrip(previousTripBeans.get(idx));
%>
		<jsp:forward page="tripInfo.jsp" />
<% 
		
	}

	
	if (request.getParameter("upcomBtn") != null){
		System.out.println("qualcosa");

		int idx = Integer.parseInt(request.getParameter("upcomBtn"));
		joinTripBean.setTrip(upcomingTripBeans.get(idx));
%>
		<jsp:forward page="tripInfo.jsp" />
<% 	
	}
	
	if (request.getParameter("myinfoBtn") != null){
		System.out.println("qualcosa");

		int idx = Integer.parseInt(request.getParameter("myinfoBtn"));
		joinTripBean.setTrip(myTripBeans.get(idx));
%>
		<jsp:forward page="tripInfo.jsp" />
<% 	
	}
	
	if (request.getParameter("save-bio") != null){
		System.out.println(userBean.getBio());
		controller.updateUserBio(sessionBean.getSessionEmail(), userBean.getBio());
	}
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GoGlobe - Profile</title>
    
    <link rel="icon" href="../res/images/favicon.ico">
    <link rel="stylesheet" type="text/css" href="../bootstrap-css/bootstrap.css">
    <link rel="stylesheet" href="../css/profile.css">
    <link rel="stylesheet" href="../css/review.css">
    <link rel="stylesheet" href="../css/style.css">
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.min.js"></script>
</head>

<body id="bootstrap-override">
	
	<%
		if (sessionBean.getSessionEmail() == null) {
			%>
				<%@ include file="html/unloggedNavbar.html" %>
			<%
		} else {
			%>
				<%@ include file="html/loggedNavbar.html" %>
			<%
		}
	%>

    <div class="displayer">
        <div class="wrapper">

            <!-- left profile info-->
            <div class="box-profile-info rounded-circle">
                <div class="profile-pic">
                    <img src="../res/images/default-profile.png" alt="profile image" class="rounded-circle border border-dark">
                </div>

                <div class="info">
                    <div class="name profile-element">
                        <h2><%= userBean.getName() %></h2>
                        <h2><%= userBean.getSurname() %></h2>
                    </div>

                    <div class="age profile-element">
                        <h4>Age: <span class="text-val"><%= userBean.getAge() %></span></h4>
                    </div>

                    <div class="travel-attitude profile-element">
                        <h4>Traveler Attitude</h4>
                        <%
                        Map<String, Integer> attitude = controller.getPercentageAttitude(userBean.getEmail());
                        System.out.println(attitude);
                        %>
                        <ul>
                            <li class="adventure">
                                <label for="adv-img"><%= attitude.get("ADVENTURE") %>%</label>
                                <img src="../res/images/icons8-mountain-50.png" id="adv-img" alt="advIcon">
                                <label for="adv-img">Adventure</label>
                            </li>
                            <li class="relax">
                                <label for="rel-img"><%= attitude.get("RELAX") %>%</label>
                                <img src="../res/images/icons8-holiday-50.png" id="rel-img" alt="relIcon">
                                <label for="rel-img">Relax</label>
                            </li>
                            <li class="fun">
                                <label for="fun-img"><%= attitude.get("FUN") %>%</label>
                                <img src="../res/images/icons8-cocktail-50.png" id="fun-img" alt="funIcon">
                                <label for="fun-img">Fun</label>
                            </li>
                            <li class="culture">
                                <label for="cult-img"><%= attitude.get("CULTURE") %>%</label>
                                <img src="../res/images/icons8-greek-pillar-capital-50.png" id="cult-img" alt="cultIcon">
                                <label for="cult-img">Culture</label>
                            </li>
                        </ul>
                    </div>
                    
                    <div class="organizerRating">
                    	<h4>Organizer Rating:</h4>
                    	<%
                    		int count = 5;
                    		for (double d=0; d<userBean.getStatsBean().getOrgRating(); d++) {
                    			count--;
                    			%>
                    			<span class="fa fa-star checked"></span>
                    			<% 
                    		}
                    		
                    		for (int i=0; i<count; i++) {
                    			%>
                    			<span class="fa fa-star"></span>
                    			<%
                    		}
                    	%>
                    </div>
                    
                    <div class="travelerRating">
                    	<h4>Traveler Rating:</h4>
                    	<%
                    		int count2 = 5;
                    		for (double d=0; d<userBean.getStatsBean().getTravRating(); d++) {
                    			count2--;
                    			%>
                    			<span class="fa fa-star checked"></span>
                    			<% 
                    		}
                    		
                    		for (int i=0; i<count2; i++) {
                    			%>
                    			<span class="fa fa-star"></span>
                    			<%
                    		}
                    	%>
                    </div>

                    <form method="POST" action="profile.jsp">
                        <button type="submit" name="change-btn" class="btn btn-light">
                            <img src="../res/images/icons8-edit-50.png" id="change-icon" style="width: 20px; height: 20px; margin-right: 1%;" alt="edit-icon">
                            <label for="change-icon">Change Info</label>
                        </button>
                    </form>
                </div>
            </div>


            <div class="content-tabs">
                <ul class="nav nav-tabs">
                    <li class="nav-item">
                        <a class="nav-link active" href="#bio" data-toggle="tab">Bio</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#reviews" data-toggle="tab">Reviews</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#prev-trips" data-toggle="tab">Previous Trips</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#up-trips" data-toggle="tab">Upcoming Trips</a>
                    </li>
                    <li class="nav-item">
                    	<a class="nav-link" href="#my-trips" data-toggle="tab">My Trips</a>
                    </li>
                </ul>

                <div class="tab-content">
					<div class="tab-pane active" role="tabpanel" id="bio">
                        <%
							String profileBio = userBean.getBio();
                            		
                            if (profileBio == null || profileBio.equals("")){
                            	profileBio = "User has no bio";
                            }
                            
							if (!sessionBean.getSessionEmail().equals(userBean.getEmail())){
%>
								<h1><%= profileBio%></h1>
<% 
							}else{
															
								request.setAttribute("bio" , profileBio);
								
%>								
								<form  method="post" action="profile.jsp" >
									<%@ include file="html/profileBioTab.html" %>	
								</form>							
<% 							
							}
%>
                    </div>

                    <div class="tab-pane" role="tabpanel" id="reviews">
                        <!-- review form-->
                        <div class="review-form card">
                            <h3>Post a review</h3>
                            <form action="profile.jsp" method="POST">
            
                                <div class="post-card">
                                    <div class="review-part1">
                                    	<!-- type of review input-->
                                    	<div class="form-check">
                                        	<input class="form-check-input" type="radio" name="type-radio" id="traveler" value="TRAVELER" checked>
                                        	<label class="form-check-label" for="traveler">As traveler</label>
                                    	</div>
                                    	<div class="form-check">
                                        	<input class="form-check-input" type="radio" name="type-radio" id="organizer" value="ORGANIZER">
                                        	<label class="form-check-label" for="organizer">As organizer</label>
                                    	</div>
                
                                    	<!-- star rating input -->
                                    	<div class="stars">
                                            <input class="star star-5" id="star-5" type="radio" name="star" value="5"/>
                                            <label class="star star-5" for="star-5"></label>
                                            <input class="star star-4" id="star-4" type="radio" name="star" value="4"/>
                                            <label class="star star-4" for="star-4"></label>
                                            <input class="star star-3" id="star-3" type="radio" name="star" value="3"/>
                                            <label class="star star-3" for="star-3"></label>
                                            <input class="star star-2" id="star-2" type="radio" name="star" value="2"/>
                                            <label class="star star-2" for="star-2"></label>
                                            <input class="star star-1" id="star-1" type="radio" name="star" value="1"/>
                                            <label class="star star-1" for="star-1"></label>
                                    	</div>
                                    </div>
                
                                    <div class="review-part2">
                                    	<!-- input for review title -->
                                    	<input name="title" type="text" class="form-control" id="reviewTitle" placeholder="Add a title for the review...">
                                    
                                    	<textarea name="comment" class="form-control" id="reviewComment" placeholder="Add a comment..."></textarea>     
                                    </div>
                                </div>
                                
                                <%
                                	if (!userBean.getEmail().equals(sessionBean.getSessionEmail())) {
                                		%>
                                		<button type="submit" class="btn btn-primary" name="reviewbtn">Post</button>
                                		<%
                                	} else {
                                		%>
                                		<button type="submit" class="btn btn-primary" name="reviewbtn" disabled>Post</button>
                                		<%
                                	}
                                %>
                                
                                <%
                                	if (request.getParameter("reviewbtn") != null) {
                                		profileBean.setVote(Double.parseDouble(request.getParameter("star")));
                                		try {
                                    		ReviewBean review = new ReviewBean();
                                    		review.setTitle(profileBean.getTitle());
                                    		review.setComment(profileBean.getComment());
                                    		review.setDate(profileBean.getDate());
                                    		review.setReviewerName(sessionBean.getSessionName());
                                    		review.setReviewerSurname(sessionBean.getSessionSurname());
                                    		review.setVote(profileBean.getVote());                  
                                    		reviewCtrl.postReview(request.getParameter("type-radio"), profileBean.getVote(), profileBean.getComment(), profileBean.getTitle(), sessionBean.getSessionEmail(), userBean.getEmail(), userBean);
                                    		userBean.getReviews().add(review);
                                    		//System.out.println("web view: org rating:"+profileBean.getUser().getStatsBean().getOrgRating()+" trav rating:"+profileBean.getUser().getStatsBean().getTravRating());
                                    		response.setIntHeader("Refresh",0);
                                		} catch (DatabaseException e) {
                                			request.setAttribute("errType", e.getMessage());
                                			if (e.getCause()!=null) {
                                				request.setAttribute("errLog", e.getCause().toString());
                                			}
                                			%>
                                			<jsp:forward page="error.jsp"/>
                                			<%
                                		} catch (UnloggedException e) {
                                			%>
                                			<p style="color: red"><%= e.getMessage() %></p>
                                			<%
                                		}
                                	}
                                
                                %>
                            </form>
                        </div>

                        <!-- review list-->
                        <div class="reviews scrollable">

                            <%
                            	for (ReviewBean bean: userBean.getReviews()) {
                            		%>
                            		<!--SINGLE REVIEW CARD (repeatable)-->
                            		<div class="review card">
                                		<div class="review-header">
	                                		<h4 id="user"><%= bean.getReviewerName()+" "+bean.getReviewerSurname() %></h4>
	                               			<div class="stars">
	                                   			<%
	                    							int max = 5;
	                    							for (double d=0; d<bean.getVote(); d++) {
	                    								max--;
	                    						%>
	                    							<span class="fa fa-star checked"></span>
	                    						<% 
	                    							}
	                    		
	                    							for (int i=0; i<max; i++) {
	                    							%>
	                    								<span class="fa fa-star"></span>
	                    							<%
	                    							}
	                    						%>
	                                		</div>
	                                		<div class="review-date">
	                                			<p>Reviewed on date <%= bean.getDate() %></p>
	                                		</div>
	                                		<div class="review-title">
	                                			<h5 style="font-style: italic;"><%= bean.getTitle() %></h5>
	                                		</div>
                                		</div>
                                		<div class="review-text">
                                    		<p><%= bean.getComment() %></p>
                                		</div>
                            		</div>
                            		
                            		
                            		<% 
                            	}
                            
                            %>

                        </div>

                    </div>
                    
                    <%
					upcomingTripBeans = controller.getUpcomingTrips(userBean.getEmail());
					previousTripBeans = controller.getRecentTrips(userBean.getEmail());
                    %>

                    <div class="tab-pane" role="tabpanel" id="prev-trips">

                        <!-- previous trips list-->
                        <div class="trips scrollable">
                        	<form method="POST" action="profile.jsp" >
							<%                     	
							for (int i = 0; i < previousTripBeans.size(); i++){
								request.setAttribute("tripName", previousTripBeans.get(i).getTitle());
								request.setAttribute("depDate",  previousTripBeans.get(i).getDepartureDate());
								request.setAttribute("retDate",  previousTripBeans.get(i).getReturnDate());
								request.setAttribute("category1",  previousTripBeans.get(i).getCategory1());
								request.setAttribute("category2", previousTripBeans.get(i).getCategory2());
								String locations="";
								for (DayBean day: previousTripBeans.get(i).getDays()) {
									locations = locations.concat(day.getLocationCity()+" • ");
								}
								request.setAttribute("locationList", locations);
								request.setAttribute("moreinfo", "prevBtn");
								request.setAttribute("btnVal", i );
							%>                      				
                        		<%@include file="html/hzTripCard.html" %>
							<%
                        	}
							%>                       	                      	
                        	</form>
                        
                        </div>
                    </div>

                    <div class="tab-pane" role="tabpanel" id="up-trips">

                        <!-- upcoming trips list-->
                        <div class="trips scrollable">
                        	<form method="POST" action="profile.jsp" >
 							<%                     	
                       		for (int i = 0; i < upcomingTripBeans.size(); i++){
                       			request.setAttribute("tripName", upcomingTripBeans.get(i).getTitle());
                       			request.setAttribute("depDate",  upcomingTripBeans.get(i).getDepartureDate());
                       			request.setAttribute("retDate",  upcomingTripBeans.get(i).getReturnDate());
                       			request.setAttribute("category1",  upcomingTripBeans.get(i).getCategory1());
                       			request.setAttribute("category2", upcomingTripBeans.get(i).getCategory2());
								String locations="";
								for (DayBean day: upcomingTripBeans.get(i).getDays()) {
									locations = locations.concat(day.getLocationCity()+"•");
								}
								request.setAttribute("locationList", locations);
								request.setAttribute("moreinfo", "upcomBtn");

                       			request.setAttribute("btnVal", i );
							%>                      				
                       			<%@include file="html/hzTripCard.html" %>
							<%
                        	}
 							%>                       	                      	
                        	</form>
                        
                        </div>
                    </div>
                    
                    <div class="tab-pane" role="tabpanel" id="my-trips">
                  		<div class="trips scrollable">
                        
                       	    <form method="POST" action="profile.jsp" >
 							<%                     	
                       		for (int i = 0; i < myTripBeans.size(); i++){
                       			request.setAttribute("tripName",  myTripBeans.get(i).getTitle());
                       			request.setAttribute("depDate",  myTripBeans.get(i).getDepartureDate());
                       			request.setAttribute("retDate",  myTripBeans.get(i).getReturnDate());
                       			request.setAttribute("category1",  myTripBeans.get(i).getCategory1());
                       			request.setAttribute("category2",  myTripBeans.get(i).getCategory2());
								String locations="";
								for (DayBean day: myTripBeans.get(i).getDays()) {
									locations = locations.concat(day.getLocationCity()+"•");
								}
								request.setAttribute("locationList", locations);
								request.setAttribute("moreinfo", "myinfoBtn");
                       			request.setAttribute("btnVal", i );
							%>                      				
                       			<%@include file="html/hzTripCard.html" %>
							<%
                        	}
 							%>                       	                      	
                        	</form>
                        </div>    
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>