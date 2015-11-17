package com.skillkit.servlets;

import com.skillkit.utils.JCRUtils;
import com.skillkit.utils.StringValidations;

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
import java.util.ArrayList;
import java.util.Map;

import static com.skillkit.utils.Constants.*;
import static com.skillkit.utils.Constants.ROLE_KEY;

/**
 * Created by Allan on 03/11/2015.
 */
@WebServlet("/UpdateStatus")
public class UpdateStatusServlet extends HttpServlet {
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
            StringValidations stringValidations = new StringValidations();
            String username = request.getParameter(USERNAME_KEY);
            String project = request.getParameter(PROJECT);
            String tid = request.getParameter(ID);
            String status = request.getParameter(STATUS_KEY);
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
                                Boolean updateStatus;
                                if (role.equals("1")) {
                                    updateStatus = updateStatusByPM(jcrSession, project, tid, status, username);
                                    if (updateStatus == false){
                                        error = "10";
                                    }else{
                                        error = BLANK;
                                    }
                                }
                                if (role.equals("2")) {
                                    updateStatus = updateStatusByDev(jcrSession, project, tid, status, username);
                                    if (updateStatus == false){
                                        error = "10";
                                    }else{
                                        error = BLANK;
                                    }
                                }
                            }else{
                                error = "0";
                            }
                        }else{
                            error = "0";
                        }
                    }else{
                        error = "0";
                    }
                }catch (RepositoryException r){
                    r.printStackTrace();
                }
            }else{
                error = "0";
            }
            String path = SKILLKIT_HOST_PATH + SLASH + "task.jsp?" + PROJECT + EQUAL_KEY + project +
                    AND + ID + EQUAL_KEY + tid;
            if (error.equals(BLANK)) {
                response.sendRedirect(path);
            }else{
                response.sendRedirect(path + AND + ERROR + EQUAL_KEY + error);
            }
        }
    }

    private Boolean updateStatusByPM(Session jcrSession, String project, String tid, String status, String username){
        if (jcrSession != null){
            try {
                Node root = jcrSession.getRootNode();
                StringValidations sv = new StringValidations();
                JCRUtils jcrUtils = new JCRUtils();
                if ((project != null) && (!project.isEmpty()) && (tid != null) && (!tid.isEmpty())
                                                                && (sv.validateUsername(username))) {
                    if (root.hasNode(SKILLKIT_PROJECTS_PATH + project)){
                        Node projectNode = root.getNode(SKILLKIT_PROJECTS_PATH + project);
                        if (projectNode != null) {
                            if (projectNode.hasProperty(PM_KEY)){
                                String projPm = projectNode.getProperty(PM_KEY).getString();
                                if (projPm.equals(username)) {
                                    if (projectNode.hasNode(TASKS_KEY + SLASH + TASK_KEY +  tid)) {
                                        Node task = projectNode.getNode(TASKS_KEY + SLASH + TASK_KEY + tid);
                                        Map<String, Object> taskMap = jcrUtils.getPropertiesFromNode(task);
                                        if ((status != null) && (!status.isEmpty())){
                                            if (status.equals("0")){
                                                task.setProperty(STATUS_KEY, STATUS_CREATED);
                                                jcrSession.save();
                                                return true;
                                            }
                                            if (status.equals("1")){
                                                if (task.hasProperty(STATUS_KEY)) {
                                                    task.setProperty(STATUS_KEY, STATUS_IN_PROGRESS);
                                                    if (taskMap.containsKey(STATUS_KEY)) {
                                                        if (taskMap.get(STATUS_KEY) instanceof String) {
                                                            String currentStatus = (String) taskMap.get(STATUS_KEY);
                                                            if (currentStatus.equals(STATUS_FINISHED)) {
                                                                if (taskMap.containsKey(RESOLVED_BY)) {
                                                                    if (taskMap.get(RESOLVED_BY) instanceof ArrayList) {
                                                                        ArrayList<String> resolvedUsers = (ArrayList) taskMap.get(RESOLVED_BY);
                                                                        String[] rusers = new String[resolvedUsers.size()];
                                                                        for (int index = 0; index < resolvedUsers.size(); index++) {
                                                                            String user = resolvedUsers.get(index);
                                                                            rusers[index] = user;
                                                                        }
                                                                        if (task.hasProperty(RETURN_TASK_COUNTER)){
                                                                            String returnedTasksCounter = task.getProperty(RETURN_TASK_COUNTER)
                                                                                                           .getString();
                                                                            int rtc = Integer.parseInt(returnedTasksCounter);
                                                                            rtc++;
                                                                            task.setProperty(RETURN_TASK_COUNTER, BLANK + rtc);
                                                                        }else{
                                                                            task.setProperty(RETURN_TASK_COUNTER, "1");

                                                                        }
                                                                        task.setProperty(ASSIGNED_USERS, rusers);
                                                                        Property resolvedBy = task.getProperty(RESOLVED_BY);
                                                                        resolvedBy.remove();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    jcrSession.save();
                                                    return true;
                                                }
                                            }
                                            if (status.equals("2")){
                                                task.setProperty(STATUS_KEY, STATUS_FINISHED);
                                                jcrSession.save();
                                                return true;
                                            }
                                            if (status.equals("3")){
                                                task.setProperty(STATUS_KEY, STATUS_CLOSE);
                                                jcrSession.save();
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }catch (RepositoryException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    private Boolean updateStatusByDev(Session jcrSession, String project, String tid, String status, String username){
        if (jcrSession != null){
            try {
                Node root = jcrSession.getRootNode();
                StringValidations sv = new StringValidations();
                if ((project != null) && (!project.isEmpty()) && (tid != null) && (!tid.isEmpty())
                        && (sv.validateUsername(username))) {
                    if (root.hasNode(SKILLKIT_PROJECTS_PATH + project)){
                        Node projectNode = root.getNode(SKILLKIT_PROJECTS_PATH + project);
                        if (projectNode != null) {
                            if (projectNode.hasProperty(PM_KEY)){
                                String projPm = projectNode.getProperty(PM_KEY).getString();
                                if (sv.validateUsername(projPm)) {
                                    if (projectNode.hasNode(TASKS_KEY + SLASH + TASK_KEY + tid)) {
                                        Node task = projectNode.getNode(TASKS_KEY + SLASH + TASK_KEY + tid);
                                        JCRUtils jcrUtils = new JCRUtils();
                                        Map<String, Object> taskMap = jcrUtils.getPropertiesFromNode(task);
                                        if (taskMap.containsKey(ASSIGNED_USERS)) {
                                            Boolean isUsersAssign = false;
                                            ArrayList<String> users = new ArrayList<>();
                                            if (taskMap.get(ASSIGNED_USERS) instanceof ArrayList) {
                                                users = (ArrayList) taskMap.get(ASSIGNED_USERS);
                                                if (!users.isEmpty()) {
                                                    for (String user : users) {
                                                        if (user.equals(username)) {
                                                            isUsersAssign = true;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            if (isUsersAssign){
                                                if (taskMap.containsKey(STATUS_KEY)) {
                                                    if (taskMap.get(STATUS_KEY) instanceof String) {
                                                        String currentStatus = (String) taskMap.get(STATUS_KEY);
                                                        if ((status != null) && (!status.isEmpty())) {
                                                            if (status.equals("1")) { // status in progress
                                                                if (currentStatus.equals(STATUS_CREATED)) {
                                                                    task.setProperty(STATUS_KEY, STATUS_IN_PROGRESS);
                                                                    jcrSession.save();
                                                                    return true;
                                                                }
                                                                if (currentStatus.equals(STATUS_IN_PROGRESS)){
                                                                    return true;
                                                                }
                                                                if (currentStatus.equals(STATUS_FINISHED)){
                                                                    if (taskMap.containsKey(RESOLVED_BY)){
                                                                        if (taskMap.get(RESOLVED_BY)
                                                                                instanceof ArrayList){
                                                                            ArrayList<String> unresolved =  (ArrayList)
                                                                                    taskMap.get(RESOLVED_BY);
                                                                            if (unresolved.size() > 0) {
                                                                                Property resolvedBy = task.getProperty(RESOLVED_BY);
                                                                                resolvedBy.remove();
                                                                                String[] usersArray = new String[unresolved.size()];
                                                                                for (int index = 0; index < unresolved.size(); index++) {
                                                                                    String user = unresolved.get(index);
                                                                                    usersArray[index] = user;
                                                                                }
                                                                                task.setProperty(ASSIGNED_USERS, usersArray);
                                                                                jcrSession.save();
                                                                                System.out.println("ok");
                                                                                return true;
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                            }
                                                            if (status.equals("2")) { // status finished
                                                                if (currentStatus.equals(STATUS_IN_PROGRESS)) {
                                                                    task.setProperty(STATUS_KEY, STATUS_FINISHED);
                                                                    String[] usersArray = new String[users.size()];
                                                                    for (int index = 0; index < users.size(); index++){
                                                                        String user = users.get(index);
                                                                        usersArray[index] = user;
                                                                    }
                                                                    if (usersArray.length > 0) {
                                                                        task.setProperty(RESOLVED_BY, usersArray);
                                                                        String[] reassign = new String[1];
                                                                        reassign[0] = projPm;
                                                                        task.setProperty(ASSIGNED_USERS, reassign);
                                                                        jcrSession.save();
                                                                        return true;
                                                                    }
                                                                }
                                                                if (currentStatus.equals(STATUS_FINISHED)){
                                                                    return true;
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
                }
            }catch (RepositoryException e){
                e.printStackTrace();
            }
        }
        return false;
    }
}
