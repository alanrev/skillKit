
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<html>
<%String resp = request.getParameter("response");%>
    <style type="text/css">
        body {
            background-image:
            url('http://cdn3.crunchify.com/wp-content/uploads/2013/03/Crunchify.bg_.300.png');
        }
    </style>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>SkillKit - Create a new user</title>
        <link rel='stylesheet' href='./css/bootstrap.min.css'>
    </head>
    <style type="text/css">
        .col-centered{
            float: none;
            margin: 0 auto;
        }
    </style>
    <body>
        <%-- Navbar Start --%>
        <nav class="navbar-wrapper">
            <div class="container">
                <br>
                <div class="navbar navbar-inverse" role="navigation">
                    <div class="navbar-collapse collapse in" style="height: auto;">
                        <ul class="nav navbar-nav navbar-left">
                            <li>
                               <a href="index.jsp"> <img src="./appImages/logo.png" width="50" height="50"></a>
                            </li>
                            <li>
                                <h2>SkillKit</h2>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </nav>
        <%-- Navbar End --%>
        <div class="container">
            <%  if (!(resp == null)){
                if (resp.equals("0")){ %>
            <div class="alert alert-success" role="alert">
                <strong>Congratulations!</strong> User created now you can <a href="index.jsp" class="alert-link"> log in</a>
            </div> <%
            }
            if (resp.equals("1")){ %>
            <div class="alert alert-danger" role="alert">
                <strong>Oops!</strong> User is already created
            </div> <%
            }
            if (resp.equals("2")){ %>
            <div class="alert alert-danger" role="alert">
                <strong>Oops!</strong> Password and Confirm Password
            </div> <%
                }
            if (resp.equals("3")){ %>
            <div class="alert alert-danger" role="alert">
                <strong>Oops!</strong> Server is offline :(
            </div> <%
                }
            if (resp.equals("4")){ %>
            <div class="alert alert-danger" role="alert">
                <strong>Oops!</strong> Invalid name format
            </div> <%
                }
            }%>
            <div class="row">
                <div class="col-centered">
                    <div class="col-md-10 col-md-offset-3">
                        <form class="form-horizontal" action="createNewUser"  method="POST">
                            <div class="form-group">
                                <div class="col-sm-offset-2 col-md-5">
                                    <input type="text" class="form-control" id="username" name="username" placeholder="Username" required="required">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-2 col-md-5">
                                    <input type="text" class="form-control" id="firstName" name="firstname" placeholder="First Name" required="required">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-2 col-md-5">
                                    <input type="text" class="form-control" id="lastName" name="lastname" placeholder="Last Name" required="required">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-2 col-md-5">
                                    <input type="email" class="form-control" id="mail" name="email" placeholder="Email" required="required">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-2 col-md-5">
                                    <input type="password" class="form-control" id="password" name="password" placeholder="Password" required="required">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-2 col-md-5">
                                    <input type="password" class="form-control" id="confPass" name="confPass" placeholder="Confirm Password" required="required">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-2 col-md-5">
                                    <select class="form-control" name="role" required>
                                        <option value="" selected disabled>Select your role</option>
                                        <option value = "1">Project Manager</option>
                                        <option value = "2">Developer</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-2 col-md-5">
                                    <button type="submit" class="btn btn-primary btn-lg btn-block">Sign up</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script type="text/javascript" src="./js/bootstrap.min.js"></script>
    </body>
</html>

