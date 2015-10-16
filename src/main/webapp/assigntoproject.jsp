<%--
  Created by IntelliJ IDEA.
  User: Allan
  Date: 04/10/2015
  Time: 5:06 PM
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html lang="en">
<%String user = request.getParameter("username");%>
<%String project = request.getParameter("project");%>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Assign to Project</title>
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
            <div class="container" align="center">
                <div ng-controller="getFreeUsersController" data-ng-init= "getDataFromServer('<%=user%>')">
                    <h2>Add team member to <%=project%></h2>
                    <form class="form-control-static" action="AssignToProject" method="POST">
                        <div class="row" align="center" style="text-overflow: ellipsis;">
                            <input type="hidden"  name="username" id="username" value= <%= user %> />
                            <input type="hidden"  name="project" id="project" value= <%= project %> />
                            <select class="form-control" name="teammate">
                                <option>Select a team member</option>
                                <option value="{{user.name}}"  ng-repeat="user in users">
                                    {{user.firstname}} {{user.lastname}}
                                </option>
                            </select>
                            <button type="submit" class="btn btn-primary btn-lg btn-block">Add member</button>
                        </div>
                    </form>
                </div>
            </div>
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