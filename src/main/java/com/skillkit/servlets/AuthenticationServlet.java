package com.skillkit.servlets;

import org.apache.jackrabbit.commons.JcrUtils;

import javax.jcr.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;

/**
 * Created by Allan on 02/09/2015.
 */
@WebServlet("/autentication")
public class AuthenticationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
       doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // reading the user input
        Session jcrSession = repoLogin();
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            PrintWriter out = response.getWriter();
            Node rootNode = jcrSession.getRootNode();
            Node user =rootNode.getNode("SkillKit");
            Node authenticatedNode = authenticate(username, password);
            String validationString = "";
            if (authenticatedNode != null) {
                try {
                    String firstName = authenticatedNode.getProperty("firstName").getString();
                    String lastName = authenticatedNode.getProperty("lastName").getString();
                    validationString = firstName + lastName;
                } catch (RepositoryException e) {
                    e.printStackTrace();
                }
            }
            out.println(
                    "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" +" +
                            "http://www.w3.org/TR/html4/loose.dtd\">\n" +
                            "<html> \n" +
                            "<head> \n" +
                            "<meta http-equiv=\"Content-Type\" content=\"text/html; " +
                            "charset=ISO-8859-1\"> \n" +
                            "<title> Home  </title> \n" +
                            "</head> \n" +
                            "<body> <div align='center'> \n" +
                            "<style= \"font-size=\"12px\" color='black'\"" + "\">" +
                            "Username: " + username + " <br> " +
                            "Password: " + password +
                            "<br>Hellow" + validationString +
                            "<br>a pata" + user.getPath() +
                            "</font></body> \n" +
                            "</html>"
            );


        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is created to verify if the fields of CreateUser.jsp are correct
     *
     * @param userName a email
     * @param pass     a password
     * @return         a the user node
     */
    public Node authenticate(String userName, String pass){
        try{
            Session jcrSession = repoLogin();
            if(jcrSession != null){
                Node user = verifyName(userName,jcrSession);
                if(user != null){
                   Node passwordNode = user.getNode("pwNode");
                    if (passwordNode != null) {
                        Property passProperty =passwordNode.getProperty("password");
                        if (null != passProperty){
                            String validate = passProperty.getString();
                                return user;
                        }
                    }else{
                        return null;
                    }
                }
                jcrSession.save();
                repoLogout(jcrSession);
                return null;
            } else {
                return null;
            }
        } catch(RepositoryException re){
            System.out.println(re);
            return null;
        }
    }

    /**
     * This method is used to verify that the Name don't exist at the repository and to be sure that the repository is already created.
     *
     * @param userName   a String that is used to search on the repository if don't exist/
     * @param jcrSession a Session of Jackrabbit that is used to contains de Repository.
     * @return           a Node with the userName as Name that is used to create a user.
     */
    public Node verifyName(String userName,Session jcrSession){
        try{
            if(userName != null){
                if(jcrSession != null) {
                    Node root = jcrSession.getRootNode();
                    if (root.hasNode("SkillKit")) {
                        Node skillKit = root.getNode("SkillKit");
                        if (skillKit.hasNode("users")) {
                            Node users = skillKit.getNode("users");
                            if (!users.hasNode(userName)) {
                                Node currentUser = users.getNode(userName);
                                if (currentUser != null) {
                                    return currentUser;
                                }
                            }
                        }
                    }
                }
            }
            return null;
        } catch(RepositoryException re){
            System.out.println("Error can't connect to jackrabbit");
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
            String url = "http://localhost:8080/rmi";
            //Repository repository = JcrUtils.getRepository(url);
            /*Repository repository =
                    new RMIRemoteRepository("//localhost/jackrabbit.repository");*/
            Repository repository =
                    new URLRemoteRepository("http://localhost:8080/rmi");
            /**
             * Enter to jackrabbit using the credentials admin:admin,password:admin
             */
            SimpleCredentials creds = new SimpleCredentials("admin","admin".toCharArray());
            Session jcrSession = repository.login(creds, "default");
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

}
