package com.skillkit.controller;

/**
 * Created by Allan on 13/09/2015.
 */

import java.io.IOException;
import java.net.MalformedURLException;

import javax.jcr.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;

import static com.skillkit.utils.Constants.*;
import static com.skillkit.utils.Constants.DASH;

@WebServlet(urlPatterns = {"/skillsHandler"})
public class skillsHandlerServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException{
        doPost(request, response);

    }

    protected void doPost(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        if ((request != null) && (response != null)) {
            String username = request.getParameter(USERNAME_KEY);
            String skillname = request.getParameter(SKILL_NAME);
            String skilldescription = request.getParameter(SKILL_DESCRIPTION);
            String skillrate = request.getParameter(SKILL_RATE);
            String ip = request.getRemoteAddr();
            //System.out.println(username+ip+skillname+skilldescription+skillrate);
            if ((!(username.isEmpty()))&& (username != null) && (ip != null) && (!(ip.isEmpty()))) {
                Node userNode = authenticate(username, ip, skillname, skilldescription, skillrate);
                if (userNode != null) {
                    response.sendRedirect(SKILLKIT_HOST_PATH + SLASH + "profile.jsp" + EXCLAMATION_MARK + USERNAME_KEY
                            + EQUAL_KEY+ username);
                } else {
                    response.sendRedirect(SKILLKIT_HOST_PATH + SLASH + "newskill.jsp" + USERNAME_KEY
                            + EQUAL_KEY+ username + AND + EXCLAMATION_MARK + ERROR
                            + EQUAL_KEY + "1");
                }
            }
        }
    }

    private Boolean checkSession(String username, String ip, Session jcrSession){
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


    private Node authenticate(String userName, String ip, String skillname, String skillDescription, String skillRate){
        try{
            Session jcrSession = repoLogin();
            if(userName != null){
                if(jcrSession != null) {
                    Node root = jcrSession.getRootNode();
                    if (root.hasNode(SKILLKIT_USERS_PATH + userName)) {
                        Node userNode = root.getNode(SKILLKIT_USERS_PATH + userName);
                        if (checkSession(userName, ip, jcrSession)){
                            Node skillsNode = getSkillNode(jcrSession,skillname, skillDescription, userNode);
                            if (skillsNode != null){
                                if (userNode.hasNode(SKILLS_NODE_KEY)){
                                    Node userSkills = userNode.getNode(SKILLS_NODE_KEY);
                                    if (userSkills.hasNode(skillname)){
                                        return null;
                                    }else{
                                        Node skill = userSkills.addNode(skillname);
                                        skill.setProperty(SKILL_DESCRIPTION, skillDescription);
                                        skill.setProperty(SKILL_RATE, skillRate);
                                        jcrSession.save();
                                        return userNode;
                                    }
                                } else{
                                    Node userSkills = userNode.addNode(SKILLS_NODE_KEY);
                                    Node skill = userSkills.addNode(skillname);
                                    skill.setProperty(SKILL_DESCRIPTION, skillDescription);
                                    skill.setProperty(SKILL_RATE, skillRate);
                                    jcrSession.save();
                                }
                            }
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
            re.printStackTrace();
            return null;
        }
    }


    private Node getSkillNode(Session jcrsession, String skillname, String skilldescription,
                              Node userNode) throws RepositoryException {
        if (jcrsession !=null){
            if ((!(skillname.isEmpty())) &&(userNode != null)
                    && (!(skillname.isEmpty())) && (!(skilldescription.isEmpty()))){
                Node rootNode = jcrsession.getRootNode();
                if (rootNode.hasNode(SKILLKIT_KEY)) {
                    Node skillkitNode = rootNode.getNode(SKILLKIT_KEY);
                    if (skillkitNode != null) {
                        if (userNode.hasProperty(ROLE_KEY)) {
                            Property property = userNode.getProperty(ROLE_KEY);
                            if (property != null) {
                                String role = property.getString();
                                if (skillkitNode.hasNode(SKILLS_NODE_KEY)) {
                                    Node skillsNode = skillkitNode.getNode(SKILLS_NODE_KEY);
                                    if (skillsNode.hasNode(role)) {
                                        Node roleNode = skillsNode.getNode(role);
                                        if (roleNode.hasNode(skillname)) {
                                            Node skill = roleNode.getNode(skillname);
                                            skill.setProperty(SKILL_DESCRIPTION, skilldescription);
                                            jcrsession.save();
                                            return skill;
                                        } else {
                                            Node skill = roleNode.addNode(skillname);
                                            skill.setProperty(SKILL_DESCRIPTION, skilldescription);
                                            jcrsession.save();
                                            return skill;
                                        }
                                    } else {
                                        Node roleNode = skillsNode.addNode(role);
                                        Node skill = roleNode.addNode(skillname);
                                        skill.setProperty(SKILL_DESCRIPTION, skilldescription);
                                        jcrsession.save();
                                        return skill;

                                    }
                                } else {
                                    Node skillsNode = skillkitNode.addNode(SKILLS_NODE_KEY);
                                    Node roleNode = skillsNode.addNode(role);
                                    Node skill = roleNode.addNode(skillname);
                                    skill.setProperty(SKILL_DESCRIPTION, skilldescription);
                                    jcrsession.save();
                                    System.out.println("ok4");
                                    return skill;
                                }
                            }
                        }
                    }
                }
                jcrsession.save();
            }
        }
        System.out.println("session nulaaa" + jcrsession);
        return null;
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
