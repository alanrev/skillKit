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
        <title>My Profile</title>
        <link rel='stylesheet' href='./css/bootstrap.min.css'>
        <link rel='stylesheet' href='./css/custom.css'>
        <link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css">
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
                                    <a href="profile?username=<%= user%>">
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
            <div ng-app="userInfo">
                <div ng-controller="userInfoController" data-ng-init= "getDataFromServer('<%=user%>')">
                    <h2>{{person.firstName}} {{person.lastName}} </h2>
                    <h4>{{person.role}}</h4>
                    <br>
                    <h4>Contact information</h4>
                    <p>email : {{person.email}}</p>
                </div>
            </div>
            <div class="row">
                <a href="newskill.jsp?username=<%=user%>" class="btn btn-primary btn-lg btn-block"> Add a new Skill</a>
            </div>
        </div>

        <h2>My Skills</h2>
        list of skill
        ..
        ..
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script type="text/javascript" src="./js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/angular.min.js"></script>
        <script type="text/javascript" src="js/usersinfo.js"></script>
    </body>
</html>