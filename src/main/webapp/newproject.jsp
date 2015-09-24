<%--
  Created by IntelliJ IDEA.
  User: Allan
  Date: 23/09/2015
  Time: 7:50 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<%String user = request.getParameter("username");%>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Add a New Project</title>
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
<% if (user == null){ %>
<div class="container-fluid">
  <div class="alert alert-danger" role="alert"><a href="index.jsp" class="alert-link">Please log in</a></div>
</div>
  <%
} else { %>
<div class="container" align="center">
  <h2>Create Project</h2>
    <form class="form-control-static" action="CreateProject" method="POST">
      <div class="row" align="center" style="text-overflow: ellipsis;">
        <input type="hidden"  name="username" id="username" value= <%= user %> />
        <input type="text" class="form-control" id="projectname" name="projectname" placeholder="Project Name"
               required="required">
        <textarea class="form-control" rows="3" name="projectdescription" required="required"
                  placeholder="Project Description"></textarea>
        <input  type="date" class="date-picker form-control" name="startdate" placeholder="Start Date">
        <button type="submit" class="btn btn-primary btn-lg btn-block">Create Project</button>
      </div>
    </form>
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