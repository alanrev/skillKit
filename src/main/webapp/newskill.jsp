<%--
  User: Allan
  Date: 04/09/2015
  Time: 10:48 PM
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html lang="en">
<%String user = request.getParameter("username");%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Add a New Skill</title>
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
                            <a href="ViewDocumentServlet?username=<%= user%>">
                                <img src="./appImages/task.png">
                                My Task
                            </a>
                        </li>
                        <li>
                            <a href="ViewDocumentServlet?username=<%= user%>">
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
            <h2>Have you learned anything new?</h2>
            <div class="stars">
                <form class="form-control-static" action="skillsHandler" method="POST">
                    <div class="row" align="center" style="text-overflow: ellipsis;">
                        <input type="hidden"  name="username" id="username" value= <%= user %> />
                        <input type="text" class="form-control" id="skillname" name="skillname" placeholder="Skill name" required="required">
                        <textarea class="form-control" rows="3" name="skilldescription" required="required"
                                  placeholder="Tell us about your skill"></textarea>
                        <p></p>
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
                        <button type="submit" class="btn btn-primary btn-lg btn-block">Add Skill</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script type="text/javascript" src="./js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/angular.min.js"></script>
<script type="text/javascript" src="js/usersinfo.js"></script>
</body>
</html>