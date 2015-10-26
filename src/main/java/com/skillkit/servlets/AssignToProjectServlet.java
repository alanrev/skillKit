package com.skillkit.servlets;

import com.skillkit.utils.JCRUtils;
import com.skillkit.utils.StringValidations;


import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.skillkit.utils.Constants.*;

/**
 * Created by Allan on 09/10/2015.
 */
@WebServlet("/AssignToProject")
public class AssignToProjectServlet extends HttpServlet {

    String error = BLANK;
    String success = BLANK;
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException{
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if ((request != null) && (response != null)) {
            request.setCharacterEncoding(UFT_8_KEY);
            response.setCharacterEncoding(UFT_8_KEY);
            String username = request.getParameter(USERNAME_KEY);
            String projectName = request.getParameter(PROJECT);
            String teammate = request.getParameter(TEAM_MATE);
            try {
                JCRUtils jcrUtils = new JCRUtils();
                Session jcrSession = jcrUtils.repoLogin();
                String ip = request.getRemoteAddr();
                if (jcrSession != null) {
                    Node usernode = jcrUtils.authenticate(username, ip);
                    if (usernode != null) {
                        if (usernode.hasProperty(ROLE_KEY)) {
                            String role = usernode.getProperty(ROLE_KEY).getString();
                            if (role.equals("1")){
                                if ((!projectName.isEmpty())&&(projectName != null)&&(!teammate.isEmpty())
                                        &&(teammate != null)){
                                    Boolean addTeammate = assignToProject(projectName, teammate, jcrSession);
                                    if (addTeammate){
                                        success = "1";
                                    }else {
                                        error = "7";
                                    }
                                }else{
                                    error = "8";
                                }
                            }else{
                                error = "6";
                            }
                        }else{
                            error = "7";
                        }
                    } else {
                        error = "1";
                    }
                } else {
                    error = "0";
                }
                jcrUtils.repoLogout(jcrSession);
            }catch (RepositoryException re) {
                re.printStackTrace();
                error = "0";
            }
            if (error.equals(BLANK)) {
                response.sendRedirect(SKILLKIT_HOST_PATH + SLASH + "projects.jsp?"+ "success="+success);
            }else{
                response.sendRedirect(SKILLKIT_HOST_PATH + SLASH + "projects.jsp?"+ "error="+error);
            }
        }
    }

    private boolean assignToProject(String project, String username, Session jcrSession){
        Boolean success = false;
        try {
            if (jcrSession != null) {
                Node root = jcrSession.getRootNode();
                if(root != null){
                    if ((!(username.isEmpty()))&&(username != null)&&(!(project.isEmpty()))&&(project != null)) {
                        Node user = root.getNode(SKILLKIT_USERS_PATH + username);
                        Node proj = root.getNode(SKILLKIT_PROJECTS_PATH + project);
                        if ((user != null)&&(proj != null)){
                            user.setProperty(CURRENT_PROJECT, project);
                            if (proj.hasNode(TEAM_KEY)){
                                Node team = proj.getNode(TEAM_KEY);
                                if (!(team.hasNode(username))){
                                    team.addNode(username);
                                    success = true;
                                }
                            }else{
                                Node team = proj.addNode(TEAM_KEY);
                                team.addNode(username);
                                success = true;
                            }
                            jcrSession.save();
                        }
                    }
                }
            }
        } catch (RepositoryException re){
            re.printStackTrace();
        }
        return success;
    }
}
