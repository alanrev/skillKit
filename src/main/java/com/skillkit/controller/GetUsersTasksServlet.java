package com.skillkit.controller;

import com.google.gson.Gson;
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
import java.util.*;

import static com.skillkit.utils.Constants.*;
import static com.skillkit.utils.Constants.ROLE_KEY;

/**
 * Created by Allan on 30/10/2015.
 */
@WebServlet("/GetTasks")
public class GetUsersTasksServlet extends HttpServlet {

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
                                if (role.equals("1")) {
                                    List<Map<String, Object>> userTasks = getPMTasks(jcrSession, username);
                                    String json = new Gson().toJson(userTasks);
                                    response.setCharacterEncoding("UTF-8");
                                    response.setContentType("application/json");
                                    response.getWriter().write(json);
                                } else {
                                    if (role.equals("2")) {
                                        if (userNode.hasProperty(CURRENT_PROJECT)){
                                            String currentProject = userNode.getProperty(CURRENT_PROJECT).getString();
                                            List<Map<String, Object>> tasks = getTasks(jcrSession,
                                                    currentProject,
                                                    username);
                                            Map<String, Object> tasksMap = new HashMap<>();
                                            tasksMap.put(PROJECT, currentProject);
                                            tasksMap.put(TASKS_KEY, tasks);
                                            List<Map<String, Object>> userTask = new ArrayList<>();
                                            userTask.add(tasksMap);
                                            String json = new Gson().toJson(userTask);
                                            response.setCharacterEncoding("UTF-8");
                                            response.setContentType("application/json");
                                            response.getWriter().write(json);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }catch (RepositoryException r){
                    r.printStackTrace();
                }
            }
        }
    }

    private List<Map<String, Object>> getTasks(Session jcrSession, String currentProject, String username){
        List<Map<String, Object>> taskList = new ArrayList<>();
        StringValidations stringValidations = new StringValidations();
        JCRUtils jcrUtils = new JCRUtils();
        if (jcrSession != null){
            try {
                if ((!currentProject.isEmpty()) && (currentProject != null) &&
                        (stringValidations.validateUsername(username))) {
                    Node root = jcrSession.getRootNode();
                    if (root.hasNode(SKILLKIT_PROJECTS_PATH + currentProject)){
                        Node projectNode = root.getNode(SKILLKIT_PROJECTS_PATH + currentProject);
                        if (projectNode != null){
                            if (projectNode.hasNode(TASKS_KEY)){
                                Node taskRoot = projectNode.getNode(TASKS_KEY);
                                if (taskRoot != null){
                                    List<Map<String, Object>> projectTaskList = jcrUtils.getNodeListProperties(taskRoot);
                                    for (Map<String, Object> task : projectTaskList){
                                        if ((task.containsKey(ASSIGNED_USERS)) && (task.containsKey(STATUS_KEY))){
                                            if (task.get(STATUS_KEY) instanceof String) {
                                                String status = (String) task.get(STATUS_KEY);
                                                if (!status.equals(STATUS_CLOSE)) {
                                                    if (task.get(ASSIGNED_USERS) instanceof ArrayList) {
                                                        ArrayList<String> users = (ArrayList) task.get(ASSIGNED_USERS);
                                                        for (String user : users) {
                                                            if (user.equals(username)) {
                                                                taskList.add(task);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }catch (RepositoryException r){
                r.printStackTrace();
            }
        }
        return taskList;
    }


    private List<Map<String, Object>> getPMTasks(Session jcrSession, String username){
        List<Map<String, Object>> taskList = new ArrayList<>();
        StringValidations stringValidations = new StringValidations();
        if (jcrSession != null){
            try{
                Node root = jcrSession.getRootNode();
                if (stringValidations.validateUsername(username)) {
                    if (root.hasNode(SKILLKIT_PROJECTS_PATH)) {
                        Node projectsNode = root.getNode(SKILLKIT_PROJECTS_PATH);
                        if (projectsNode != null) {
                            Iterator<Node> projects = projectsNode.getNodes();
                            while (projects.hasNext()) {
                                Node project = projects.next();
                                if (project != null){
                                    if (project.hasProperty(PM_KEY)){
                                        if (project.getProperty(PM_KEY) != null) {
                                            String pm = project.getProperty(PM_KEY).getString();
                                            if (pm.equals(username)){
                                                Map<String, Object> projectMap = new HashMap<>();
                                                List<Map<String, Object>> tasks = getTasks(jcrSession,
                                                                                            project.getName(),
                                                                                            username);
                                                projectMap.put(PROJECT, project.getName());
                                                projectMap.put(TASKS_KEY, tasks);
                                                taskList.add(projectMap);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }catch (RepositoryException r){
                r.printStackTrace();
            }
        }
        return taskList;
    }

}
