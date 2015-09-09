<%--
  User: Allan
  Date: 04/09/2015
  Time: 10:48 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
  <%String user = request.getParameter("user");%>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Home</title>
  <link rel='stylesheet' href='./css/bootstrap.min.css'>
</head>

<style type ="text/css">
  .form-control-static{
    background-color: #dcdcdc;
    width: 210px;
    border-radius: 6px 6px 6px 6px;
  }
  div.row{
    overflow: hidden;
    width: 200px;
  }
</style>
<body>
<%-- Navbar Start --%>
<nav class="navbar-wrapper">
  <div class="container">
    <br>
    <div class="navbar navbar-inverse" role="navigation">
      <div class="container-fluid">
        <div class="navbar-header">
          <a class="navbar-brand" href="#"> Home</a>
        </div>
        <div class="navbar-collapse collapse in" style="height: auto;">
          <ul class="nav navbar-nav">
            <li>
              <a href="ViewDocumentServlet?user=<%= user%>">
                Documents
              </a>
            </li>
            <li>
              <a href="ViewImageServlet?user=<%= user%>">
                Images
              </a>
            </li>
            <li>
              <a href="ViewMusicServlet?user=<%= user%>">
                Music
              </a>
            </li>
            <li>
              <a href="ViewVideoServlet?user=<%= user%>">
                Video
              </a>
            </li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            <li>
              <a href="LogoutServlet?user=<%= user%>">
                Logout&nbsp&nbsp&nbsp&nbsp
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
  <h1>File Upload!</h1>
  <form id ="form" action="UploadFiles" method="POST" enctype="multipart/form-data" class="form-control-static">
    File:
    <div class="row" align="center" style="text-overflow: ellipsis;">
      <input type="hidden"  name="user" id="user" value= <%= user %> />
      <input type="file"  name="file" id="file" accept="audio/mp3, audio/wav, image/png, image/jpeg, image/jpg,
                                                                          image/bmp, video/mp4, video/webm, application/pdf, text/html"/>
    </div>
    <br>
    <input type="submit" value="Upload" name="upload" id="upload"/>
  </form>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script type="text/javascript" src="./js/bootstrap.min.js"></script>

</body>
</html>