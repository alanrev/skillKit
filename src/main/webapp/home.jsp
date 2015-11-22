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
  <title>Home</title>
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
  } else { %>
  <div ng-app="userInfo" ng-cloak>
      <div ng-controller="userInfoController" data-ng-init= "getDataFromServer('<%=user%>')">
        <h2> Welcome {{person.firstName}} {{person.lastName}} </h2>
      </div>
    <br>
      <div id="myCarousel" class="carousel slide" data-ride="carousel">
          <!-- Indicators -->
          <ol class="carousel-indicators">
              <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
              <li data-target="#myCarousel" data-slide-to="1"></li>
              <li data-target="#myCarousel" data-slide-to="2"></li>
              <li data-target="#myCarousel" data-slide-to="3"></li>
          </ol>

          <!-- Wrapper for slides -->
          <div class="carousel-inner" role="listbox">
              <div class="item active">
                  <img src="./appImages/logo.png" alt="SkillKit">
              </div>
              <div class="item">
                  <img src="./appImages/dev.png" alt="Software Development">
              </div>
              <div class="item">
                  <img src="./appImages/p.jpg" alt="Project Management">
              </div>

              <div class="item">
                  <img src="./appImages/webdesign.jpg" alt="Web Design">
              </div>

          </div>

          <!-- Left and right controls -->
          <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
              <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
              <span class="sr-only">Previous</span>
          </a>
          <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
              <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
              <span class="sr-only">Next</span>
          </a>
      </div>
        <div id="newSkills">
            <h3>  New skills added for your role</h3>
            <div ng-controller="getNewRoleSkillController" data-ng-init= "getDataFromServer('<%=user%>')">
                <form class="form-control-static" action="AddSkills" method="POST">
                    <input type="hidden" id="skills" name="skills" value="{{skillsSize}}" >
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
                                <div class="stars">
                                    <div class="row" align="center" style="text-overflow: ellipsis;">
                                        <input type="hidden"  name="username" id="username" value= <%= user %> />
                                        <input type="hidden" id="skillname" name="skillname{{$index}}" value ="{{skill.skillName}}">
                                        <input type="hidden" id="skilldescription" name="skilldescription{{$index}}" value ="{{skill.description}}">
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
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <button type="submit" class="btn btn-primary btn-block">Add Skills</button>
                </form>
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