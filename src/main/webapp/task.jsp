<%--
  Created by IntelliJ IDEA.
  User: Allan
  Date: 04/10/2015
  Time: 5:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html lang="en">
<%String user = request.getParameter("username");%>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Tasks</title>
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
              <a href="#?username=<%= user%>">
                <img src="./appImages/task.png">
                My Task
              </a>
            </li>
            <li>
              <a href="projects.jsp?username=<%= user%>">
                <img src="./appImages/projects.png"  width="48" height="48">
                Projects
              </a>
            </li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            <li>
              <a href="profile.jsp?username=<%= user%>">
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
  <% if (user == null){ %>
  <div class="alert alert-danger" role="alert"><a href="index.jsp" class="alert-link">Please log in</a></div><%
} else { %>
  <%String error = request.getParameter("error");%>
  <% if (error != null){

    if (error.equals("0")) { %>
  <div class="alert alert-danger" role="alert">The user is not log in.
    <a href="index.jsp" class="alert-link">Please log in</a>
  </div><%
  }
  if (error.equals("1")) { %>
  <div class="alert alert-danger" role="alert">Session lost.
    <a href="index.jsp" class="alert-link">Please log in</a>
  </div><%
  }
  if (error.equals("2")) { %>
  <div class="alert alert-danger" role="alert">Session lost.
    <a href="index.jsp" class="alert-link">Please log in</a>
  </div><%
  }
  if (error.equals("3")) { %>
  <div class="alert alert-danger" role="alert"><strong>Oops</strong> Server connection has lost</div><%
  }
  if (error.equals("4")) { %>
  <div class="alert alert-danger" role="alert">Session lost, please log in</div><%
  }
  if (error.equals("5")) { %>
  <div class="alert alert-danger" role="alert">
    <strong>Oops</strong> Can't create a new proyect, please check the create project's form fields
  </div><%
    }
  } %>
  <div ng-app="userInfo">
    <div ng-controller="getProjectsController" data-ng-init= "getDataFromServer('<%=user%>')">
      <h2>Projects</h2>
      <a href="newproject.jsp?username=<%= user%>">Create new project</a>
      <table class="table  table-condensed table-striped">
        <thead>
        <tr>
          <th>Project Name</th>
          <th>Description</th>
          <th>Start Date</th>
          <th>Project Manager<th>
          <th></th>
        </tr>
        </thead>
        <tbody>
        <tr class="active" ng-repeat="project in projectData">
          <td><strong>{{project.name}}</strong></td>
          <td><p>{{project.projectdescription}}</p></td>
          <td>{{project.startdate}}</td>
          <td>{{project.projectmanager}}</td>
          <td>
            <a class="btn btn-primary btn-block" href="#?username<%=user%>">Tasks</a>
            <a class="btn btn-primary btn-block">Assign team member</a>
          <td>
        </tr>
        </tbody>
      </table>
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