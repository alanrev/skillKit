<%--
  User: Allan
  Date: 04/09/2015
  Time: 10:48 PM
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html lang="en">
<%String user = request.getParameter("username");%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Home</title>
    <link rel='stylesheet' href='./css/bootstrap.min.css'>
    <link rel='stylesheet' href='./css/custom.css'>
</head>

<body>
<%-- Navbar Start --%>
<nav class="navbar-wrapper">
    <div class="container">
        <br>
        <div class="navbar navbar-inverse" role="navigation">
            <div class="container-fluid">
                <div class="navbar-collapse collapse in" style="height: auto;">
                    <ul class="nav navbar-nav">
                        <li>
                            <a href="home.jsp?username=<%= user%>">
                                <img src="./appImages/logo.png" width="48" height="48">
                                Home
                            </a>
                        </li>
                        <li>
                            <a href="ViewDocumentServlet?username=<%= user%>">
                                <img src="./appImages/task.png">
                                My Task
                            </a>
                        </li>
                        <li>
                            <a href="ViewDocumentServlet?username=<%= user%>">
                                <img src="./appImages/projects.png"  width="48" height="48">
                                Projects
                            </a>
                        </li>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <li>
                            <a href="ViewImageServlet?username=<%= user%>">
                                <img src="./appImages/profile.png">
                                Profile
                            </a>
                        </li>
                        <li>
                            <a href="LogoutServlet?username=<%= user%>">
                                <img src="./appImages/log_out-48.png">
                                Log Out
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    </div>
</nav>



<%-- Navbar End --%>
<div class="container" align="center">
    <div ng-app="myApp">
        <div ng-controller="MyController" data-ng-init= "getDataFromServer('<%=user%>')">
            <h2>{{person.firstName}} {{person.lastName}} </h2>
            <h4>{{person.role}}</h4>
            <p>email : {{person.email}}</p>
        </div>
    </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script type="text/javascript" src="./js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/angular.min.js"></script>
<script type="text/javascript" src="js/usersinfo.js"></script>
</body>
</html>