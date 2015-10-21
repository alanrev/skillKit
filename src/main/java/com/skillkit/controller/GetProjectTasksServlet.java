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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.skillkit.utils.Constants.*;

/**
 * Created by Allan on 20/10/2015.
 */
@WebServlet("/GetProjectTasks")
public class GetProjectTasksServlet extends HttpServlet {

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
            String ip = request.getRemoteAddr();
            JCRUtils jcrUtils = new JCRUtils();
            Session jcrSession = jcrUtils.repoLogin();
            List<Map<String, Object>> tasksList = new ArrayList<>();
            if (jcrSession != null) {
                if (stringValidations.validateUsername(username)) {
                    Node userNode = jcrUtils.authenticate(username, ip);
                    if (userNode != null) {

                        tasksList = getTasks(jcrSession, jcrUtils, project);
                        String json = new Gson().toJson(tasksList);
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json");
                        response.getWriter().write(json);
                    }
                }
            }
        }
    }

    private List<Map<String, Object>> getTasks(Session session, JCRUtils jcrUtils, String project){
        List<Map<String, Object>> task = new ArrayList<>();
        if (session != null){
            try {
                Node root = session.getRootNode();
                if (project != null) {
                    if (!project.isEmpty()) {
                        if (root.hasNode(SKILLKIT_PROJECTS_PATH + project)) {
                            Node projectNode = root.getNode(SKILLKIT_PROJECTS_PATH + project);
                            if (projectNode != null) {
                                if (projectNode.hasNode(TASKS_KEY)){
                                    Node taskNode = projectNode.getNode(TASKS_KEY);
                                    if (taskNode != null){
                                        task = jcrUtils.getNodeListProperties(taskNode);
                                    }
                                }
                            }
                        }
                    }
                }
            }catch (RepositoryException re){
                re.printStackTrace();
            }
        }
        return task;
    }
}
