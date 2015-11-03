<%--
  User: Allan
  Date: 04/09/2015
  Time: 10:48 PM
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html lang="en">
<%String user = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("username")) {
                user = cookie.getValue();
            }
        }
    }
%>
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
                                    <a href="home.jsp">
                                        <img src="./appImages/logo.png" width="48" height="48">
                                        Home
                                    </a>
                                </li>
                                <li>
                                    <a href="mytasks.jsp">
                                        <img src="./appImages/task.png">
                                        My Tasks
                                    </a>
                                </li>
                                <li>
                                    <a href="projects.jsp">
                                        <img src="./appImages/projects.png"  width="48" height="48">
                                        Projects
                                    </a>
                                </li>
                            </ul>
                            <ul class="nav navbar-nav navbar-right">
                                <li>
                                    <a href="profile.jsp">
                                        <img src="./appImages/profile.png">
                                        Profile
                                    </a>
                                </li>
                                <li>
                                    <a href="LogoutServlet">
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
        <div class="container" align="center">
        <% if (user == null){ %>
        <div class="alert alert-danger" role="alert"><a href="index.jsp" class="alert-link">Please log in</a></div><%
        } else { %>
        <%-- Navbar End --%>
                <div ng-app="userInfo">
                    <div ng-controller="userInfoController" data-ng-init= "getDataFromServer('<%=user%>')">
                        <div class="thumbnail">
                            <div class="caption">
                                <h2>{{person.firstName}} {{person.lastName}} </h2>
                                <h4>{{person.role}}</h4>
                                <h4>Contact information</h4>
                                <p>email : {{person.email}}</p>
                                <p><a href="newskill.jsp" class="btn btn-primary" role="button">Add Skill</a>
                                   <a href="#" class="btn btn-default" role="button">Edit Profile</a>
                                </p>
                            </div>
                        </div>
                    </div>
                    <div class="container">
                        <h2>My Skills</h2>
                        <div ng-controller="getSkillController" data-ng-init= "getDataFromServer('<%=user%>')">
                                <table class="table  table-condensed table-striped">
                                    <thead>
                                    <tr>
                                        <th>Skill Name</th>
                                        <th>Skill Description</th>
                                        <th>Skill Knowledge</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr class="active" ng-repeat="skill in skillData">
                                        <td><strong>{{skill.skillName}}</strong></td>
                                        <td><p>{{skill.description}}</p></td>
                                        <td>
                                            <progress value="{{skill.skillRate}}" max="5"></progress> {{skill.skillRate}} / 5
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                    </div>
                </div>
            </div>
        <%
            }
        %>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script type="text/javascript" src="./js/bootstrap.min.js"></script>
        <script type="text/javascript" src="js/angular.min.js"></script>
        <script type="text/javascript" src="js/usersinfo.js"></script>
    </body>
</html>