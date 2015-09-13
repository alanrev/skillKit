package com.skillkit.servlets;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;

import static com.skillkit.utils.Constants.*;
/**
 *
 * @author xumakgt5
 */
@MultipartConfig
@WebServlet(urlPatterns = {"/createNewUser"})
public class createNewUserServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if(null != response){
            if(request != null){
                String firstname = request.getParameter(FIRSTNAME_KEY).toString();
                String lastname = request.getParameter(LASTNAME_KEY).toString();
                String email = request.getParameter(EMAIL_KEY).toString();
                String userName = request.getParameter(USERNAME_KEY).toString();
                String password = request.getParameter(PASSWORD_KEY).toString();
                String confPassword = request.getParameter(CONFIRM_PASSWORD_KEY);
                String show = autentication(userName, firstname, lastname, email, password, confPassword);
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();
                    out.println("<!DOCTYPE html>");
                    out.println("<html>\n" +
                            "<style type=\"text/css\">\n" +
                            "    body {\n" +
                            "        background-image:\n" +
                            "        url('http://cdn3.crunchify.com/wp-content/uploads/2013/03/Crunchify.bg_.300.png');\n" +
                            "    }\n" +
                            "</style>\n" +
                            "\n" +
                            "<head>\n" +
                            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n" +
                            "    <title>SkillKit - Create a new user</title>\n" +
                            "    <link rel='stylesheet' href='./css/bootstrap.min.css'>\n" +
                            "</head>\n" +
                            "<style type=\"text/css\">\n" +
                            "    .col-centered{\n" +
                            "        float: none;\n" +
                            "        margin: 0 auto;\n" +
                            "    }\n" +
                            "</style>\n" +
                            "<nav class=\"navbar-wrapper\">\n" +
                            "    <div class=\"container\">\n" +
                            "        <br>\n" +
                            "        <div class=\"navbar navbar-inverse\" role=\"navigation\">\n" +
                            "            <div class=\"navbar-collapse collapse in\" style=\"height: auto;\">\n" +
                            "                <ul class=\"nav navbar-nav navbar-left\">\n" +
                            "                    <li>\n" +
                            "                        <img src=\"./appImages/logo.png\" width=\"50\" height=\"50\">\n" +
                            "                    </li>\n" +
                            "                    <li>\n" +
                            "                        <h2>SkillKit</h2>\n" +
                            "                    </li>\n" +
                            "                </ul>\n" +
                                    "                <ul class=\"nav navbar-nav navbar-right\">\n" +
                                    "                    <li>\n" +
                                    "                        <a href=\"index.jsp\">Log in</a>\n" +
                                    "                    </li>\n" +
                                    "                </ul>\n" +
                            "            </div>\n" +
                            "        </div>\n" +
                            "    </div>\n" +
                            "</nav>\n" +
                            "<div class=\"container\">\n" +
                            "    <div class=\"row\">\n" +
                            "        <div class=\"col-centered\">\n");
                    out.println("<h1>"+ userName+"</h1>");
                    out.println("<h1>"+ firstname + " "+ lastname+"</h1>");
                    out.println("<h1>"+ email +"</h1>");
                    out.println("<h1>"+show+"</h1>");
                    out.println("</div></div></div>");
                    out.println("</body>");
                    out.println("</html>");
                out.close();
            } else {
                System.out.println("request empty");
            }
        } else {
            System.out.println("response empty");
        }
    }

    /**
     * This method is created to verify if the fields of CreateUser.jsp are correct
     *
     * @param userName a String that is from the field user of CreateUser.jsp
     * @param pass     a String that is from the field pass of CreateUser.jsp
     * @param confPass a String that is used to confirm the password
     * @return         a String that contains the message of error or accept of the verification.
     */
    public String autentication(String userName, String firstname, String lastname,
                                String email,String pass, String confPass){
        try{
            String message = "";
            Session jcrSession = repoLogin();
            if(jcrSession != null){
                if(pass.equals(confPass)){
                    Node user = verifyName(userName, jcrSession);
                    if(user != null){
                        if (!firstname.isEmpty()) {
                            user.setProperty(FIRSTNAME_KEY, firstname);
                            jcrSession.save();
                        }
                        if (!lastname.isEmpty()) {
                            user.setProperty(LASTNAME_KEY, lastname);
                            jcrSession.save();
                        }
                        if (!email.isEmpty()){
                            user.setProperty(EMAIL_KEY, email);
                            jcrSession.save();
                        }
                        user.addNode(pass);
                        message =  "The user " + userName + " is created.";
                    }else{
                        return "The user " + userName + " is already created.";
                    }
                } else {
                    System.out.println("the password don't match");
                    message = "the password and the confirm password don't match";
                }
                jcrSession.save();
                repoLogout(jcrSession);
                return message;
            } else {
                return "can't access to servlet";
            }
        } catch(RepositoryException re){
            System.out.println(re);
            return "can't connect to repository";
        }
    }

    public String verifyUsername(String userName,Session jcrSession){
        try{
            if ((userName != null)|| (!userName.equals(""))){
                if(jcrSession != null){
                    Node root = jcrSession.getRootNode();
                    if(root.hasNode(SKILLKIT_KEY)){
                        Node skillKit = root.getNode(SKILLKIT_KEY);
                        if(skillKit.hasNode(USERS_KEY)){
                            Node users = skillKit.getNode(USERS_KEY);
                            if(users.hasNode(userName)){
                                return "The user " + userName + " is already created.";
                            }
                        }
                    }
                }else {
                    return "session close";
                }

            } else {
                return "Empty username";
            }
            return "";
        } catch(RepositoryException re){
            System.out.println("Erron can't connect to jackrabbit");
            return "Repository exception";
        }
    }

    /**
     * This method is used to verify that the Name don't exist at the repository and to be sure that the repository is already created.
     *
     * @param userName   a String that is used to search on the repository if don't exist/
     * @param jcrSession a Session of Jackrabbit that is used to contains de Repository.
     * @return           a Node with the userName as Name that is used to create a user.
     */
    public Node verifyName(String userName, Session jcrSession){
        try{
            if(userName != null){
                if(jcrSession != null){
                    Node root = jcrSession.getRootNode();
                    if(root.hasNode(SKILLKIT_KEY)){
                        Node skillKit = root.getNode(SKILLKIT_KEY);
                        if(skillKit.hasNode(USERS_KEY)){
                            Node users = skillKit.getNode(USERS_KEY);
                            if(!users.hasNode(userName)){
                                Node currentUser = users.addNode(userName);
                                return currentUser;
                            } else {
                                return null;
                            }
                        } else {
                            Node users = skillKit.addNode(USERS_KEY);
                            Node currentUser = users.addNode(userName);
                            return currentUser;
                        }
                    } else {
                        Node skillKit = root.addNode(SKILLKIT_KEY);
                        Node users = skillKit.addNode(USERS_KEY);
                        Node currentUser = users.addNode(userName);
                        return currentUser;
                    }
                }else {
                    return null;
                }

            } else {
                return null;
            }
        } catch(RepositoryException re){
            System.out.println("Erron can't connect to jackrabbit");
            return null;
        }
    }


    /**
     * This method is used to Login the JackRabbit Repository
     *
     * @return a Session of jackrabbit
     */
    public Session repoLogin(){
        try{
            /**
             * using the url lolahost:8080 where is up the jackrabbit repository
             */
            String url = "http://intern5.xumak.gt:8080/rmi";
            Repository repository =
                    new URLRemoteRepository("http://localhost:8080/rmi");
            /**
             * Enter to jackrabbit using the credentials admin:admin,password:admin
             */
            SimpleCredentials creds = new SimpleCredentials("admin","admin".toCharArray());
            Session jcrSession = repository.login(creds);
            return jcrSession;
        }catch(RepositoryException RE){
            System.out.println(RE);
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * This method is used to exit the session that is used in the program.
     *
     * @param jcrSession receive the session that is going to be closed.
     */
    public void repoLogout(Session jcrSession){
        if(jcrSession != null){
            jcrSession.logout();
        } else {
            System.out.println("Session can't be closed");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}