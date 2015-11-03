package com.skillkit.servlets;

import com.skillkit.utils.JCRUtils;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.skillkit.utils.Constants.*;

/**
 * Created by Allan on 18/10/2015.
 */
@WebServlet("/CreateTask")
public class CreateTaskServlet extends HttpServlet {
    String error = BLANK;
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
            String name = request.getParameter(NAME);
            String projectDescription = request.getParameter(DESCRIPTION_KEY);
            String mainSkill = request.getParameter(MAIN_SKILL);
            String skillRate = request.getParameter(SKILL_RATE);
            String hours = request.getParameter(HOURS_KEY);
            String priority = request.getParameter(PRIORITY);
            String ip = request.getRemoteAddr();
            JCRUtils jcrUtils = new JCRUtils();
            Session jcrSession = jcrUtils.repoLogin();
            if (jcrSession != null) {
                    try {
                        Node usernode = jcrUtils.authenticate(username, ip);
                        if (usernode != null) {
                            if (usernode.hasProperty(ROLE_KEY)) {
                                Property roleProp = usernode.getProperty(ROLE_KEY);
                                String role = roleProp.getString();
                                if (role.equals("1")) {
                                    Node task = createTask(jcrSession, projectName);
                                    if (task != null) {
                                        addProperties(name, projectDescription, priority,
                                                mainSkill, skillRate, hours, task, jcrSession);
                                        response.sendRedirect(SKILLKIT_HOST_PATH + SLASH + "tasks.jsp" +
                                                EXCLAMATION_MARK + PROJECT  + EQUAL_KEY + projectName
                                                + "&success=0");
                                    } else {
                                        error = "3";
                                    }
                                }else{
                                    error = "2";
                                }
                            }else{
                                error = "2";
                            }
                        } else {
                            error = "1";
                        }
                    }catch (RepositoryException re){
                        re.printStackTrace();
                        error = "1";
                    }
                    jcrUtils.repoLogout(jcrSession);
                } else {
                    error = "0";
                }
            if (!(error.equals(BLANK))) {
                response.sendRedirect(SKILLKIT_HOST_PATH + SLASH + "tasks.jsp" +
                        EXCLAMATION_MARK + PROJECT  + EQUAL_KEY + projectName + "&error=" + error);
            }
        }
    }

    private void addProperties(String name, String description, String priority,
                               String mainSkill, String skillRate,
                               String hours, Node node, Session jcrSession){
       try {
           if ((name != null) && (!name.isEmpty())) {
               node.setProperty(NAME, name);
           }
           if ((description != null) && (!description.isEmpty())) {
               node.setProperty(DESCRIPTION_KEY, description);
           }
           if ((mainSkill != null) && (!mainSkill.isEmpty())) {
               node.setProperty(MAIN_SKILL, mainSkill);
           }
           if ((priority != null) && (!priority.isEmpty())) {
               node.setProperty(PRIORITY, priority);
           }
           if ((skillRate != null) && (!skillRate.isEmpty())) {
               node.setProperty(SKILL_RATE, skillRate);
           }
           if ((hours != null) && (!hours.isEmpty())) {
               node.setProperty(HOURS_KEY, hours);
           }
           node.setProperty(STATUS_KEY, STATUS_CREATED);
           String[] userArray = new String[1];
           userArray[0] = UNASSINGED;
           node.setProperty(ASSIGNED_USERS, userArray);
           jcrSession.save();
       }catch (RepositoryException re){
           re.printStackTrace();
       }
    }

    private Node createTask(Session jcrSession, String project){
        if (jcrSession != null) {
            if ((!project.isEmpty()) &&(project != null)){
                try {
                    Node rootNode = jcrSession.getRootNode();
                    if (rootNode != null){
                        if (rootNode.hasNode(SKILLKIT_PROJECTS_PATH + project)){
                            Node projectNode = rootNode.getNode(SKILLKIT_PROJECTS_PATH + project);
                            if (projectNode != null){
                                if (projectNode.hasProperty(TASKS_KEY)){
                                    String counterString = projectNode.getProperty(TASKS_KEY).getString();
                                    System.out.println(counterString);
                                    int counter = Integer.parseInt(counterString);
                                    if (projectNode.hasNode(TASKS_KEY)){
                                        Node tasksNode = projectNode.getNode(TASKS_KEY);
                                        if (!(tasksNode.hasNode(TASK_KEY + counter))){
                                            Node taskNode = tasksNode.addNode(TASK_KEY + counter);
                                            taskNode.setProperty(ID, counter);
                                            counter++;
                                            projectNode.setProperty(TASKS_KEY, BLANK +counter);
                                            jcrSession.save();
                                            return taskNode;
                                        }
                                    }else{
                                        Node tasksNode = projectNode.addNode(TASKS_KEY);
                                        Node taskNode = tasksNode.addNode(TASK_KEY + counter);
                                        taskNode.setProperty(ID, counter);
                                        counter++;
                                        projectNode.setProperty(TASKS_KEY, BLANK +counter);
                                        jcrSession.save();
                                        return taskNode;
                                    }
                                }else{
                                    int counter = 0;
                                    projectNode.setProperty(TASKS_KEY, BLANK + counter);
                                    if (projectNode.hasNode(TASKS_KEY)){
                                        Node tasksNode = projectNode.getNode(TASKS_KEY);
                                        if (!(tasksNode.hasNode(TASK_KEY + counter))){
                                            Node taskNode = tasksNode.addNode(TASK_KEY + counter);
                                            taskNode.setProperty(ID, counter);
                                            counter++;
                                            projectNode.setProperty(TASKS_KEY, BLANK +counter);
                                            jcrSession.save();
                                            return taskNode;
                                        }
                                    }else{
                                        Node tasksNode = projectNode.addNode(TASKS_KEY);
                                        Node taskNode = tasksNode.addNode(TASK_KEY + counter);
                                        taskNode.setProperty(ID, counter);
                                        counter++;
                                        projectNode.setProperty(TASKS_KEY, BLANK +counter);
                                        jcrSession.save();
                                        return taskNode;
                                    }
                                }
                            }
                        }
                    }
                } catch (RepositoryException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
