<%--
  Created by IntelliJ IDEA.
  User: Allan
  Date: 18/09/2015
  Time: 9:27 PM
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
<%String success = request.getParameter("success");%>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Projects</title>
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
                            <a href="home.jsp">
                                <img src="./appImages/logo.png" width="48" height="48">
                                Home
                            </a>
                        </li>
                        <li>
                            <a href="#">
                                <img src="./appImages/task.png">
                                My Task
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
    } else { %>
    <%String error = request.getParameter("error");%>
    <%  if (success != null){
            if (success.equals("0")) { %>
                <div class="alert alert-success" role="alert">The project have been successfully created
                </div><%
            }
            if (success.equals("1")) { %>
                <div class="alert alert-success" role="alert">The team member assigned to a project
                </div><%
            }
        }
        if (error != null){

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
                    <strong>Oops</strong> Can't create a new project, please check the create project's form fields
                </div><%
            }
            if (error.equals("6")) { %>
            <div class="alert alert-danger" role="alert">
                <strong>Oops</strong> Can't create a new project, you need to be a Project Manager
            </div><%
                }
            if (error.equals("7")) { %>
            <div class="alert alert-danger" role="alert">
                <strong>Oops</strong> Can't assign team members to a project, you need to be a Project Manager
            </div><%
                }
            if (error.equals("8")) { %>
            <div class="alert alert-danger" role="alert">
                <strong>Oops</strong> Can't assign to project, please check the assign project's form fields
            </div><%
                }
    } %>
      <div ng-app="userInfo">
          <div ng-controller="userInfoController" data-ng-init= "getDataFromServer('<%=user%>')">
            <div ng-controller="getProjectsController" data-ng-init= "getDataFromServer('<%=user%>')">
              <h2>Projects</h2>
                <div ng-if="person.role === 'Project Manager'">{{person.role}}</div>
                  <a href="newproject.jsp">Create new project</a>
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
                        <td><a href="projectprofile.jsp?project={{project.name}}">
                            <strong>{{project.name}}</strong></a>
                        </td>
                        <td><p>{{project.projectdescription}}</p></td>
                        <td>{{project.startdate}}</td>
                        <td>{{project.projectmanager}}</td>
                        <td>
                            <a class="btn btn-primary btn-block" href="tasks.jsp?project={{project.name}}">
                                Tasks
                            </a>
                            <a class="btn btn-primary btn-block"
                               href="assigntoproject.jsp?project={{project.name}}">
                                Assign team member
                            </a>
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