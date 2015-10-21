<%--
  Created by IntelliJ IDEA.
  User: Allan
  Date: 04/10/2015
  Time: 5:07 PM
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html lang="en">
<%String user = request.getParameter("username");%>
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
    <%String success = request.getParameter("success");%>
  <% if (project == null) {
     response.sendRedirect("projects.jsp?username="+user);
    }else{
      if (success != null){
            if (success.equals("0")) { %>
                <div class="alert alert-success" role="alert"><strong>The task have been successfully created</strong>
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
            <div class="alert alert-danger" role="alert"><strong>Oops!</strong> Can't create tasks to a project,
                you need to be a Project Manager
            </div><%
            }
          if (error.equals("3")) { %>
          <div class="alert alert-danger" role="alert"><strong>Oops!</strong> Can't create task, please try again
          </div><%
          }
        }
    }%>
  <div ng-app="userInfo">
    <div ng-controller="GetProjectTasksController" data-ng-init= "getDataFromServer('<%=user%>','<%=project%>')">
      <h2><%=project%>'s Task </h2>
        <button type="button" class="btn btn-primary" data-toggle="modal" data-target=".bs-example-modal-lg">New Task</button>

        <div class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <h3>Create a new task</h3>
                    <form class="form-control-static" action="CreateTask" method="POST"
                          ng-controller="getDevSkillsController" data-ng-init= "getDataFromServer('<%=user%>')">
                        <div class="row" align="center" style="text-overflow: ellipsis;">
                            <input type="hidden"  name="username" id="username" value= <%= user %> />
                            <input type="hidden"  name="project" id="project" value= <%= project %> />
                            <input type="text" class="form-control" id="taskname" name="name" placeholder="Task Name"
                                   required="required">
                            <textarea class="form-control" rows="3" name="description" required="required"
                            placeholder="Task Description"></textarea>
                            <input type="number" class="form-control" name="hours" placeholder="Hours" required="required">
                            <select class="form-control" name="priority">
                                <option>priority</option>
                                <option value="High">High</option>
                                <option value="Normal">Normal</option>
                                <option value="Low">Low</option>
                             </select>
                            <select class="form-control" name="mainSkill" >
                                <option>Main Skill</option>
                                <option ng-repeat="skill in skills" value="{{skill}}">{{skill}}</option>
                            </select>
                            <div class="stars">
                                <input class="star star-5" id="star-5" type="radio" name="skillrate" value="5"/>
                                <label class="star star-5" for="star-5"></label>
                                <input class="star star-4" id="star-4" type="radio" name="skillrate" value="4"/>
                                <label class="star star-4" for="star-4"></label>
                                <input class="star star-3" id="star-3" type="radio" name="skillrate" value="3"/>
                                <label class="star star-3" for="star-3"></label>
                                <input class="star star-2" id="star-2" type="radio" name="skillrate" value="2"/>
                                <label class="star star-2" for="star-2"></label>
                                <input class="star star-1" id="star-1" type="radio" name="skillrate" value="1"/>
                                <label class="star star-1" for="star-1"></label>
                            </div>
                            <button type="submit" class="btn btn-primary btn-lg btn-block">Create Task</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
      <table class="table  table-condensed table-striped">
        <thead>
        <tr>
          <th>Task Id</th>
          <th>Task Name</th>
          <th>Priority</th>
          <th>Status</th>
          <th>Assign To<th
          <th></th>
        </tr>
        </thead>
        <tbody>
        <tr class="active" ng-repeat="task in tasks">
                <td><strong>{{task.id}}</strong></td>
                <td>{{task.name}}</td>
                <td>{{task.priority}}</td>
                <td>{{task.status}}</td>
                <td>{{task.Assign}}</td>
                <td><a href="task.jsp?username=<%=user%>&project=<%=project%>&id={{task.id}}"
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
