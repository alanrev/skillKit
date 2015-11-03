<%--
  Created by IntelliJ IDEA.
  User: Allan
  Date: 02/11/2015
  Time: 7:57 PM
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
  <title>Tasks</title>
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

<%-- Navbar End --%>
<div class="container" align="center">
  <% if (user == null){ %>
  <div class="alert alert-danger" role="alert"><a href="index.jsp" class="alert-link">Please log in</a></div><%
} else {%>
  <div ng-app="userInfo">
      <div ng-controller="GetTasksController" data-ng-init= "getDataFromServer('<%=user%>')">
        <div ng-repeat="project in projects">
            <h2>{{project.project}}</h2>
            <table class="table  table-condensed table-striped">
              <thead>
              <tr>
                <th>Task Id</th>
                <th>Task Name</th>
                <th>Priority</th>
                <th>Status</th>
                <th>Assign To</th>
                <th></th>
              </tr>
              </thead>
              <tbody>
              <tr class="active" ng-repeat="task in project.tasks">
                <td><strong>{{task.id}}</strong></td>
                <td>{{task.name}}</td>
                <td>{{task.priority}}</td>
                <td>{{task.status}}</td>
                <td><ul>
                  <li ng-repeat=" user in task.assignedUsers" >
                    {{user}}
                  </li>
                </ul></td>
                <td><a href="task.jsp?project={{project.project}}&id={{task.id}}"
                       class="btn btn-primary btn-lg btn-block">
                  view Task
                </a>
                </td>
                </a>
              </tr>
              </tbody>
            </table>
        </div>
      </div>
  </div>
  <%
    }
  %>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script type="text/javascript" src="./js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/angular.min.js"></script>
<script type="text/javascript" src="js/usersinfo.js"></script>
</body>
</html>