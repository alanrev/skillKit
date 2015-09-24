<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<html>
<style type="text/css">
    body {
        background-image:
        url('http://cdn3.crunchify.com/wp-content/uploads/2013/03/Crunchify.bg_.300.png');
    }
</style>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>SkillKit - login</title>
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
                    <ul class="nav navbar-nav navbar-right">
                        <li>
                            <a href="NewUser.jsp">
                                Sign up
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </nav>
    <%-- Navbar End --%>
    <%String error = request.getParameter("error");%>
    <% if (error != null){
        if (error.equals("1")) { %>
    <div class="alert alert-danger" role="alert">Can't log in, wrong user or wrong password, please try again</div><%
        }
        } %>
    <div class="container">
        <div class="row">
            <div class="col-lg-1 col-centered">
                <img src="./appImages/logo.png">
            </div>
        </div>
    </div>
    <br><br>
    <div class="container">
        <div class="row">
            <div class="col-centered">
                <div class="col-md-10 col-md-offset-3">
                    <form class="form-horizontal" action="autentication"  method="POST">
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-md-5">
                                <input type="text" class="form-control" id="username" name="username" placeholder="Username">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-md-5">
                                <input type="password" class="form-control" id="password" name="password" placeholder="Password">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-md-5">
                                <button type="submit" class="btn btn-primary btn-lg btn-block">Sign in</button>
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
