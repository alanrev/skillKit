package com.skillkit.servlets;


import javax.jcr.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;
import java.io.IOException;
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
        if ((request != null) && (response != null)) {
            String username = request.getParameter(USERNAME_KEY);
            String password = request.getParameter(PASSWORD_KEY);
            if (!((username.isEmpty()) &&(password.isEmpty()))) {
                String ip = request.getRemoteAddr();
                Node authenticatedNode = authenticate(username, password, ip);
                if (authenticatedNode != null) {
                    Cookie sessionCookie = new Cookie(USERNAME_KEY, username);
                    sessionCookie.setMaxAge(-1);
                    response.addCookie(sessionCookie);
                    response.sendRedirect(SKILLKIT_HOST_PATH + SLASH + "home.jsp");
                } else {
                    response.sendRedirect(SKILLKIT_HOST_PATH + SLASH + "index.jsp?error=1");
                }
            }else{
                response.sendRedirect(SKILLKIT_HOST_PATH + SLASH + "index.jsp?error=1");
            }
        }
    }

    public void logInSession(String username, String ip, Session jcrSession){
        if ((!(username.isEmpty())) && (!(username.equals(EMPTY_SPACE)))
                && (!(ip.equals(EMPTY_SPACE))) &&(!(ip.isEmpty()))){
            if (jcrSession != null){
                try {
                    Node rootNode = jcrSession.getRootNode();
                    if (rootNode.hasNode(SKILLKIT_SESSIONS_PATH)){
                        Node sessionsNode = rootNode.getNode(SKILLKIT_SESSIONS_PATH);
                        if (sessionsNode != null) {
                            if (sessionsNode.hasNode(username + DASH + ip)) {
                               sessionsNode.getNode(username + DASH + ip);
                            }else{
                                sessionsNode.addNode(username + DASH + ip);
                            }
                        } else {
                            Node sessions = sessionsNode.addNode(SESSIONS_KEY);
                            sessions.addNode(username + DASH + ip);
                        }

                    }else{
                        Node sessions = rootNode.addNode(SKILLKIT_SESSIONS_PATH);
                        Node session = sessions.addNode(username + DASH + ip);
                    }
                    jcrSession.save();
                } catch (RepositoryException e) {
                    e.printStackTrace();
                    System.out.print("error en el repo");
                }
            }
        }
    }

    /**
     *
     * @param username
     * @param pass
     * @return
     */
    public Node authenticate(String username, String pass, String ip){
        try{
            Session jcrSession = repoLogin();
            if(jcrSession != null){
                Node user = verifyName(username, jcrSession);
                if (user != null) {
                    Node passwordNode = user.getNode(pass);
                    if (passwordNode != null) {
                        logInSession(username, ip, jcrSession);
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
