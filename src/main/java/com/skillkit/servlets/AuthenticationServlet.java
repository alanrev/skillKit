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

import static com.skillkit.utils.Constants.*;
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
        String username = request.getParameter(USERNAME_KEY);
        String password = request.getParameter(PASSWORD_KEY);
        Node authenticatedNode = authenticate(username, password);
       if (authenticatedNode != null) {
           response.sendRedirect(SKILLKIT_HOST_PATH + SLASH + "index.jsp");
       } else {
           response.sendRedirect(SKILLKIT_HOST_PATH + SLASH +"/home.jsp");
        }



    }

    /**
     *
     * @param username
     * @param pass
     * @return
     */
    public Node authenticate(String username, String pass){
        try{
            Session jcrSession = repoLogin();
            if(jcrSession != null){
                Node user = verifyName(username, jcrSession);
                if (user != null) {
                    Node passwordNode = user.getNode(pass);
                    if (passwordNode != null) {
                        return user;
                    }
                }
                jcrSession.save();
                repoLogout(jcrSession);
            }
            return null;
        } catch(RepositoryException re){
            System.out.println(re);
            return null;
        }
    }

    /**
     *
     * @param userName
     * @param jcrSession
     * @return
     */
    public Node verifyName(String userName,Session jcrSession){
        try{
            if(userName != null){
                if(jcrSession != null) {
                    Node root = jcrSession.getRootNode();
                    if (root.hasNode(SKILLKIT_USERS_PATH + userName)) {
                        Node userNode = root.getNode(SKILLKIT_USERS_PATH + userName);
                        return userNode;
                     }
                }
            }
            return null;
        } catch(RepositoryException re){
            System.out.println("Error can't connect to jackrabbit");
            return null;
        }
    }
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

}
