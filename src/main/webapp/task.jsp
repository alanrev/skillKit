<%--
  Created by IntelliJ IDEA.
  User: Allan
  Date: 04/10/2015
  Time: 5:08 PM
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
<%String id = request.getParameter("id");%>
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
<div class="container" align="center">
  <% if (user == null){ %>
  <div class="alert alert-danger" role="alert"><a href="index.jsp" class="alert-link">Please log in</a></div><%
} else { %>
  <%-- Navbar End --%>
  <div ng-app="userInfo">
    <div ng-controller="GetTaskInfoController" data-ng-init= "getDataFromServer('<%=user%>', '<%=project%>', '<%=id%>')">
      <div class="thumbnail">
        <div class="caption">
          <h3>Task-{{task.id}} {{task.name}}</h3>
          <p>{{task.description}}</p>
          <h5>Assign to: {{task.Assign}}</h5>
          <h5>Main Skill Required: {{task.mainSkill}}</h5>
          <progress value="{{task.skillrate}}" max="5"></progress> {{task.skillrate}} / 5
          <h5>Hours:{{task.hours}} hours</h5>
          <h5>Priority: {{task.priority}}</h5>
          <h5>Status:{{task.status}}</h5>
          <button type="button" class="btn btn-primary" data-toggle="modal" data-target=".bs-example-modal-lg">Assign To</button>

          <div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
            <div class="modal-dialog modal-lg">
              <div class="modal-content">
                <h3>Assign Task</h3>
                <form class="form-control-static" action="AsignTask" method="POST"
                      ng-controller="GetRecommendedUsersController"
                      data-ng-init= "getDataFromServer('<%=user%>', '<%=project%>', '<%=id%>')"
                        >
                    <div class="row">
                        <input type="hidden"  name="username" id="username" value= <%= user %> />
                        <input type="hidden"  name="project" id="project" value= <%= project %> />
                        <div class="col-xs-6">
                            <p>Recommended</p>
                            <div ng-repeat="user in users.UsersFromProject">
                              <label>
                                  <input type="checkbox" name="users[]" value="net" data-toggle="popover"
                                         title="Popover title"
                                         data-content="And here's some amazing content. It's very engaging. Right?" />
                                  <a role="button" data-toggle="collapse" href="#collapse{{$index}}a" aria-expanded="false" aria-controls="collapseExample">
                                      {{user.firstname}} {{user.lastname}}
                                  </a>
                                  <div class="collapse" id="collapse{{$index}}a">
                                      <div class="well">
                                          <h5>{{user.skillname}} {{user.skillrate}}/5</h5>
                                      </div>
                                  </div>
                              </label>
                            </div>
                        </div>
                        <div class="col-xs-6">
                            <p>Other Options</p>
                            <ul class="list-unstyled">
                                <li ng-repeat="user in users.otherUsers">
                                    <a role="button" data-toggle="collapse" href="#collapse{{$index}}" aria-expanded="false" aria-controls="collapseExample">
                                        {{user.firstname}} {{user.lastname}}
                                    </a>
                                    <div class="collapse" id="collapse{{$index}}">
                                        <div class="well">
                                            <div ng-if="user.currentProject != null">
                                                <p>Current Project</p>
                                                <h4>{{user.currentProject}}</h4>
                                            </div>
                                            <h5>{{user.skillname}} {{user.skillrate}}/5</h5>
                                        </div>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-primary btn-lg btn-block">Assign Task</button>

                </form>
              </div>
            </div>
          </div>
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

