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
import java.util.HashMap;
import java.util.Map;

import static com.skillkit.utils.Constants.*;

/**
 * Created by Allan on 20/10/2015.
 */
@WebServlet("/GetTaskInfo")
public class GetTaskInfoServlet  extends HttpServlet {

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
            String ip = request.getRemoteAddr();
            JCRUtils jcrUtils = new JCRUtils();
            Session jcrSession = jcrUtils.repoLogin();
            if (jcrSession != null) {
                if (stringValidations.validateUsername(username)) {
                    Node userNode = jcrUtils.authenticate(username, ip);
                    if (userNode != null) {
                        Map<String, Object>  taskInfoMap = getTaskInfoMap(project, tid, jcrSession);
                        String json = new Gson().toJson(taskInfoMap);
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json");
                        response.getWriter().write(json);
                    }
                }
            }
            jcrUtils.repoLogout(jcrSession);
        }
    }

    public Map<String, Object> getTaskInfoMap(String project, String tid, Session jcrSession){
        Map<String, Object>  taskInfoMap = new HashMap<>();
        if (jcrSession != null){
            if ((project != null) && (tid != null)){
                if ((!project.isEmpty())&&(!tid.isEmpty())){
                    try {
                        Node root = jcrSession.getRootNode();
                        String taskPath = SKILLKIT_PROJECTS_PATH + project + SLASH +TASKS_KEY + SLASH + TASK_KEY +tid;
                        if (root.hasNode(taskPath)){
                            Node tIdNode = root.getNode(taskPath);
                            if (tIdNode != null){
                                JCRUtils jcrUtils = new JCRUtils();
                                taskInfoMap = jcrUtils.getPropertiesFromNode(tIdNode);
                            }
                        }
                    } catch (RepositoryException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return taskInfoMap;
    }
}
