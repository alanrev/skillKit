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
<%String success = request.getParameter("success");%>
<%String error = request.getParameter("error");%>
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
<div class="container">
    <div class="row">
        <div class="col-lg-10">
            <a href="tasks.jsp?project=<%=project%>"><img src=".\appImages\back.png"><strong><%=project%></strong></a>
        </div>
    </div>
</div>
<div class="container" align="center">
    <%  if (success != null){
        if (success.equals("0")) { %>
    <div class="alert alert-success" role="alert">The task assigned successfully
    </div><%
    }}
    if (error != null){

        if (error.equals("10")) { %>
    <div class="alert alert-danger" role="alert"><strong>Opps!</strong> can't update status!
    </div><%
    }
    }
    %>

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
            <h5>Main Skill Required: {{task.mainSkill}}</h5>
            <progress value="{{task.skillrate}}" max="5"></progress> {{task.skillrate}} / 5
            <h5>Hours:{{task.hours}} hours</h5>
            <h5>Priority: {{task.priority}}</h5>
            <h5>Status:{{task.status}}</h5>
            <h5>Assign to:</h5>
            <ul class="list-unstyled">
              <li ng-repeat=" user in task.assignedUsers" >
                  <strong>{{user}}</strong>
              </li>
            </ul>
            <div class="row">
                <div ng-controller="userInfoController" data-ng-init= "getDataFromServer('<%=user%>','<%=project%>')">
                    <div class="col-xs-4">
                        <button type="button" id="pm" class="btn btn-primary" data-toggle="modal" data-target=".assign-modal-lg">Assign To</button>
                    </div>
                </div>
                <div class="col-xs-4">
                    <button type="button" id="updateButton" class="btn btn-primary" data-toggle="modal" data-target=".update-modal-modal-lg">Update Status</button>
                </div>
            </div>
              <div class="modal fade assign-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
                <div class="modal-dialog modal-lg">
                  <div class="modal-content">
                    <h3>Assign Task</h3>
                    <form class="form-control-static" action="AssignTaskToUser" method="POST"
                          ng-controller="GetRecommendedUsersController"
                          data-ng-init= "getDataFromServer('<%=user%>', '<%=project%>', '<%=id%>')"
                            >
                        <div class="row">
                            <input type="hidden"  name="username" id="username" value= <%= user %> />
                            <input type="hidden"  name="project" id="project" value= <%= project %> />
                            <input type="hidden"  name="id" id="id" value= <%= id %> />
                            <h5>Main Skill Required: {{task.mainSkill}}</h5>
                            <progress value="{{task.skillrate}}" max="5"></progress> {{task.skillrate}} / 5
                            <h5>Assign to:</h5>
                            <ul class="list-unstyled">
                                <li ng-repeat=" user in task.assignedUsers" >
                                    <strong>{{user}}</strong>
                                </li>
                            </ul>
                            <div class="col-xs-6" id="recommendedUser">
                                <p>Recommend</p>
                                <div ng-repeat="user in users.UsersFromProject">
                                    <label>
                                        <input type="checkbox" name="users" value="{{user.username}}" />
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
                            <div class="col-xs-6" id="allUsers">
                                <p>Recommend</p>
                                <p><strong>Not Found</strong></p>
                                <p>Team</p>
                                <div ng-repeat="user in users.users">
                                  <label>
                                      <input type="checkbox" name="users" value="{{user.username}}" />
                                      <a role="button" data-toggle="collapse" href="#collapse{{$index}}b" aria-expanded="false" aria-controls="collapseExample">
                                          {{user.firstname}} {{user.lastname}}
                                      </a>
                                      <div class="collapse" id="collapse{{$index}}b">
                                          <div class="well">
                                              <h5>{{user.skillname}} {{user.skillrate}}/5</h5>
                                          </div>
                                      </div>
                                  </label>
                                </div>
                            </div>
                            <div class="col-xs-6" id="otherOptions">
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
                        <button type="submit" class="btn btn-primary btn-block">Assign Task</button>

                    </form>
                  </div>
                </div>
              </div>
                <div class="modal fade update-modal-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
                    <div class="modal-dialog modal-lg">
                        <div class="modal-content">
                            <h3>Update Status</h3>
                            <form class="form-control-static" action="UpdateStatus" method="POST"
                                  ng-controller="UpdateStatusController"
                                  data-ng-init= "getDataFromServer('<%=user%>', '<%=project%>', '<%=id%>')"
                                    >
                                <div class="row">
                                    <input type="hidden"  name="username" id="user" value= <%= user %> />
                                    <input type="hidden"  name="project" id="proj" value= <%= project %> />
                                    <input type="hidden"  name="id" id="idTask" value= <%= id %> />
                                    <div class="form-group">
                                        <select class="form-control" name="status" required>
                                            <option ng-repeat="stat in status"  value="{{stat.value}}">{{stat.name}}</option>
                                        </select>
                                    </div>
                                </div>
                                <button type="submit" class="btn btn-primary btn-block">Update Status</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
      </div>
        <div class="stars" id="evaluateForm">
            <form class="form-control-static" action="EvaluateTask" method="POST">
                <input type="hidden"  name="username" id="userE" value= <%= user %> />
                <input type="hidden"  name="project" id="projE" value= <%= project %> />
                <input type="hidden"  name="id" id="idTaskE" value= <%= id %> />
                <div ng-repeat=" user in task.resolvedBy">
                    <h4>{{user}}</h4>
                    {{resolvedBySize}}
                    <div class="row" align="center" style="text-overflow: ellipsis;">
                        <input type="hidden" name="user{{$index}}" value="{{user}}">
                        <input type="hidden" name="users" value="{{resolvedByLength}}">

                        <input class="star star-5" id="star-5{{$index}}" type="radio" name="skillrate{{$index}}" value="5"/>
                        <label class="star star-5" for="star-5{{$index}}"></label>
                        <input class="star star-4" id="star-4{{$index}}" type="radio" name="skillrate{{$index}}" value="4"/>
                        <label class="star star-4" for="star-4{{$index}}"></label>
                        <input class="star star-3" id="star-3{{$index}}" type="radio" name="skillrate{{$index}}" value="3"/>
                        <label class="star star-3" for="star-3{{$index}}"></label>
                        <input class="star star-2" id="star-2{{$index}}" type="radio" name="skillrate{{$index}}" value="2"/>
                        <label class="star star-2" for="star-2{{$index}}"></label>
                        <input class="star star-1" id="star-1{{$index}}" type="radio" name="skillrate{{$index}}" value="1"/>
                        <label class="star star-1" for="star-1{{$index}}"></label>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary btn-lg btn-block">Evaluate Task</button>
            </form>
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

