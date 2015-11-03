<%--
  Created by IntelliJ IDEA.
  User: Allan
  Date: 16/10/2015
  Time: 6:17 PM
  To change this template use File | Settings | File Templates.
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
<%String project = request.getParameter("project");%>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title><%=project%></title>
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
  <div class="container" align="center">
    <% if (user == null){ %>
    <div class="alert alert-danger" role="alert"><a href="index.jsp" class="alert-link">Please log in</a></div><%
  } else { %>
  <%-- Navbar End --%>
      <div ng-app="userInfo">
        <div ng-controller="getProjectInfoController" data-ng-init= "getDataFromServer('<%=user%>', '<%=project%>')">
            <div class="thumbnail">
              <div class="caption">
                <h2>{{project.name}}</h2>
                <p>{{project.projectdescription}}</p>
                <h3>Project Manager: {{project.projectmanager}}</h3>
                <h3>Team</h3>
                <table class="table  table-condensed table-striped">
                  <thead>
                  <tr>
                    <th>Name</th>
                    <th>Role</th>
                    <th>Email</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr class="active" ng-repeat = "member in project.team" >
                    <td><strong> {{member.firstname}} {{member.lastname}}</strong></td>
                    <td>{{member.role}}</td>
                    <td>{{member.email}}</td>
                  </tr>
                  </tbody>
                </table>
                <p><a href="tasks.jsp?username=<%=user%>&project=<%=project%>" class="btn btn-primary" role="button">View tasks</a>
              </div>
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
