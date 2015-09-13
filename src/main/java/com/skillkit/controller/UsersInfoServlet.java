package com.skillkit.controller;

/**
 * Created by Allan on 12/09/2015.
 */
import java.io.IOException;
import java.net.MalformedURLException;

import javax.jcr.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.skillkit.model.PersonData;
import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;

import static com.skillkit.utils.Constants.*;
import static com.skillkit.utils.Constants.DASH;

@WebServlet(urlPatterns = {"/usersInfo"})
public class UsersInfoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UsersInfoServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        if ((request != null) && (response != null)) {
            String username = request.getParameter(USERNAME_KEY);
            String ip = request.getRemoteAddr();
            System.out.println(username+ip);
            if ((!(username.isEmpty()))&&(username != null) && (ip != null) && (!(ip.isEmpty()))) {
                try{
                    Node userNode = authenticate(username, ip);
                    System.out.println("usernode" + userNode);
                    if (userNode != null) {
                        String firstname = BLANK;
                        String lastname = BLANK;
                        String email = BLANK;
                        String role = BLANK;
                        PersonData personData = new PersonData();
                        if (userNode.hasProperty(FIRSTNAME_KEY)){
                            Property firstProperty = userNode.getProperty(FIRSTNAME_KEY);
                            firstname = firstProperty.getString();
                        }
                        if (userNode.hasProperty(LASTNAME_KEY)){
                            Property lastProperty = userNode.getProperty(LASTNAME_KEY);
                            lastname = lastProperty.getString();
                        }
                        if (userNode.hasProperty(EMAIL_KEY)){
                            Property mailProperty = userNode.getProperty(EMAIL_KEY);
                            email = mailProperty.getString();
                        }
                        if (userNode.hasProperty(ROLE_KEY)){
                            Property roleProperty = userNode.getProperty(ROLE_KEY);
                            role = roleProperty.getString();
                            if (role.equals("1")){
                                role = PROJECT_MANAGER_ROLE;
                            }
                            if (role.equals("2")){
                                role = DEVELOPER_ROLE;
                            }
                        }
                        personData.setFirstName(firstname);
                        personData.setLastName(lastname);
                        personData.setEmail(email);
                        personData.setRole(role);
                        String json = new Gson().toJson(personData);
                        response.setContentType("application/json");
                        response.getWriter().write(json);
                    } else {
                        PersonData personData = new PersonData();
                        personData.setFirstName("Mohaideen");
                        personData.setLastName("Jamil");
                        String json = new Gson().toJson(personData);
                        response.setContentType("application/json");
                        response.getWriter().write(json);
                    }
                } catch (RepositoryException e) {
                    e.printStackTrace();
                    System.out.println("exception");
                }
            }
        }
    }

    public Boolean checkSession(String username, String ip, Session jcrSession){
        if ((!(username.isEmpty())) && (!(username.equals(EMPTY_SPACE)))
                && (!(ip.equals(EMPTY_SPACE))) &&(!(ip.isEmpty()))){
            if (jcrSession != null){
                try {
                    Node rootNode = jcrSession.getRootNode();
                    if (rootNode.hasNode(SKILLKIT_SESSIONS_PATH)){
                        Node sessionsNode = rootNode.getNode(SKILLKIT_SESSIONS_PATH);
                        if (sessionsNode != null) {
                            if (sessionsNode.hasNode(username + DASH + ip)) {
                                return true;
                            }
                        }
                    }
                    jcrSession.save();
                } catch (RepositoryException e) {
                    e.printStackTrace();
                    System.out.print("error en el repo");
                    return false;
                }
            }
        }
        return false;
    }


    public Node authenticate(String userName, String ip){
        try{
            Session jcrSession = repoLogin();
            if(userName != null){
                if(jcrSession != null) {
                    Node root = jcrSession.getRootNode();
                    if (root.hasNode(SKILLKIT_USERS_PATH + userName)) {
                        Node userNode = root.getNode(SKILLKIT_USERS_PATH + userName);
                        if (checkSession(userName, ip, jcrSession)){
                            return userNode;
                        }else{
                            return null;
                        }

                    }
                    jcrSession.save();
                    repoLogout(jcrSession);
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
