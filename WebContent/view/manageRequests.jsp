<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@page import="java.util.List"%> 
<%@page import="logic.bean.RequestBean"%>
<%@page import="logic.control.ManageRequestController"%>
<%@page import="logic.control.ProfileController"%>
<%@page import="logic.persistence.exceptions.DatabaseException"%>

<jsp:useBean id="sessionBean" scope="session" class="logic.bean.SessionBean"/>
<jsp:useBean id="profileBean" scope="request" class="logic.bean.ProfileBean"/>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>GoGlobe - Manage Requests</title>
    
    <link rel="icon" href="../res/images/goglobe-favicon.png">
    <link rel="stylesheet" type="text/css" href="../bootstrap-css/bootstrap.css">
    <link rel="stylesheet" href="../css/requests.css">
    <link rel="stylesheet" href="../css/style.css">
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.min.js"></script>
</head>
<body id="bootstrap-override">

	<%@ include file="html/loggedNavbar.html" %>


    <!-- tab panels -->
    <div class="container">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12">
            <br>
    
            <ul class="nav nav-tabs">
                <li class="nav-item">
                    <a class="nav-link active" href="#incoming" data-toggle="tab">Incoming Requests</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#sent" data-toggle="tab">Sent Requests</a>
                </li>
            </ul>
    
            <div class="tab-content">
                <div role="tabpanel" class="tab-pane active" id="incoming">

				<%
					List<RequestBean> requests = ManageRequestController.getInstance().getUserIncomingRequests(sessionBean.getSessionEmail());
					for (RequestBean req: requests) {
						request.setAttribute("tripTitle", req.getTripTitle());
						request.setAttribute("senderName", req.getSenderName());
						request.setAttribute("senderSurname", req.getSenderSurname());
						request.setAttribute("senderAge", req.getSenderAge());
						
						%>
						<%@ include file="html/incRequest.html" %>
						<%
						
						if (request.getParameter("viewprofile") != null) {
                			profileBean.setUser(ManageRequestController.getInstance().getSenderBean(req));
                			%>
                				<jsp:forward page="profile.jsp"/>
                			<%
                		}
						
						try {
							if (request.getParameter("accept") != null) {
	                			System.out.println("Accepting request.");
	                			ManageRequestController.getInstance().acceptRequest(req);
	                			response.setIntHeader("Refresh",0);
	                		}
						} catch (DatabaseException e) {
							request.setAttribute("errType", e.getMessage());
							if (e.getCause()!=null) request.setAttribute("errLog", e.getCause().toString());
							%>
							<jsp:forward page="error.jsp"/>
							<%
						}
						
						try {
                       		if (request.getParameter("decline") != null) {
                    			System.out.println("Declining request.");
                    			ManageRequestController.getInstance().declineRequest(req);
                    			response.setIntHeader("Refresh",0);
                    		}
						} catch (DatabaseException e) {
							request.setAttribute("errType", e.getMessage());
							if (e.getCause()!=null) request.setAttribute("errLog", e.getCause().toString());
						}
					}
				%>
                </div>
                
               
                <div role="tabpanel" class="tab-pane" id="sent">
                	<%
					List<RequestBean> sentRequests = ManageRequestController.getInstance().getUserSentRequests(sessionBean.getSessionEmail());
                	if (sentRequests != null) {
                		for (RequestBean req: sentRequests) {
    						request.setAttribute("tripTitle", req.getTripTitle());
    						request.setAttribute("senderName", req.getReceiverName());
    						request.setAttribute("senderSurname", req.getReceiverSurname());
    						%>
    						<%@ include file="html/sentRequest.html" %>
    						<%
                			
    						if (request.getParameter("viewprofile") != null) {
                    			profileBean.setUser(ManageRequestController.getInstance().getSenderBean(req));
                    			%>
                    				<jsp:forward page="profile.jsp"/>
                    			<%
                    		}
                		}
                	}
                	
    			%>
             </div>
          </div>
        </div>
    </div>
</body>
</html>