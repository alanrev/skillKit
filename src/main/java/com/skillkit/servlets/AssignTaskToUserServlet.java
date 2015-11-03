package com.skillkit.servlets;

import com.skillkit.utils.JCRUtils;
import com.skillkit.utils.StringValidations;

import javax.jcr.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.skillkit.utils.Constants.*;

/**
 * Created by Allan on 28/10/2015.
 */
@WebServlet("/AssignTaskToUser")
public class AssignTaskToUserServlet extends HttpServlet {

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
            StringValidations stringValidations = new StringValidations();
            String username = request.getParameter(USERNAME_KEY);
            String project = request.getParameter(PROJECT);
            String tid = request.getParameter(ID);
            String[] users = request.getParameterValues(USERS_KEY);
            String ip = request.getRemoteAddr();
            JCRUtils jcrUtils = new JCRUtils();
            Session jcrSession = jcrUtils.repoLogin();
            if (jcrSession != null) {
                try {
                    if (stringValidations.validateUsername(username)) {
                        Node userNode = jcrUtils.authenticate(username, ip);
                        if (userNode != null) {
                            if (userNode.hasProperty(ROLE_KEY)) {
                                String role = userNode.getProperty(ROLE_KEY).getString();
                                if (role.equals("1")){
                                    Boolean assignTask = AssignTask(users,project, tid, jcrSession);
                                    if (assignTask){
                                        success = "0";
                                    }else{
                                        error = "3";
                                    }
                                }else{
                                    error = "2";
                                }
                            }else{
                                error = "2";
                            }
                        }else {
                            error = "1";
                        }
                    }
                } catch (RepositoryException r){
                    r.printStackTrace();
                }
                jcrUtils.repoLogout(jcrSession);
            }else{
                error = "0";
            }
            if (error.equals(BLANK)) {
                response.sendRedirect(SKILLKIT_HOST_PATH + SLASH + "task.jsp?" + "project="+ project +"&id="
                        + tid+ "&success=" + success);
            }else{
                response.sendRedirect(SKILLKIT_HOST_PATH + SLASH + "task.jsp?"+ "project="+ project +"&id="
                        + tid + "&error="+error);
            }
        }
    }

    private boolean AssignTask(String[] users, String project, String tid, Session jcrSession){
        if ((!project.isEmpty()) && (project != null) && (!tid.isEmpty()) && (tid != null)
                 && (jcrSession != null) ){
            try {
                Node root = jcrSession.getRootNode();
                if (root != null){
                    String path = SKILLKIT_PROJECTS_PATH + project + SLASH + TASKS_KEY + SLASH + TASK_KEY + tid;
                    if (root.hasNode(path)){
                        Node task = root.getNode(path);
                        if (task != null){
                            if (users != null) {
                                task.setProperty(ASSIGNED_USERS, users);
                                jcrSession.save();
                                return true;
                            }else{
                                String[] userArray = new String[1];
                                userArray[0] = UNASSINGED;
                                task.setProperty(ASSIGNED_USERS, userArray);
                                jcrSession.save();
                            }
                        }
                    }
                }
            }catch (RepositoryException r){
                r.printStackTrace();
            }
        }
        return false;
    }
}
