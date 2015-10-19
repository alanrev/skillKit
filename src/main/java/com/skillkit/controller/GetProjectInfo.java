package com.skillkit.controller;

import com.google.gson.Gson;
import com.skillkit.utils.JCRUtils;

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

/**
 * Created by Allan on 16/10/2015.
 */
@WebServlet("/projectInfo")
public class GetProjectInfo extends HttpServlet {
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
            String ip = request.getRemoteAddr();
            JCRUtils jcrUtils = new JCRUtils();
            Session jcrSession = jcrUtils.repoLogin();
            if (jcrSession != null){
                Node usernode = jcrUtils.authenticate(username,ip);
                if (usernode != null){
                    if (projectName != null){
                        if (!(projectName.isEmpty())){
                           Map<String, Object> projectMap = getProjectMap(projectName, jcrSession);
                           if (!(projectMap.isEmpty())){
                               String json = new Gson().toJson(projectMap);
                               response.setCharacterEncoding("UTF-8");
                               response.setContentType("application/json");
                               response.getWriter().write(json);
                           }
                        }
                    }
                }
            }
        }
    }

    private Map<String, Object> getProjectMap(String project, Session jcrSession){
        Map<String, Object> projectMap = new HashMap<>();
        if (jcrSession != null){
            try {
                Node root = jcrSession.getRootNode();
                if (root.hasNode(SKILLKIT_PROJECTS_PATH + project)){
                    Node projectNode = root.getNode(SKILLKIT_PROJECTS_PATH + project);
                    if (projectNode != null){
                        JCRUtils jcrUtils = new JCRUtils();
                        projectMap = jcrUtils.getPropertiesFromNode(projectNode);
                        projectMap.put(NAME, projectNode.getName());
                        if (projectNode.hasNode(TEAM_KEY)){
                            Node team = projectNode.getNode(TEAM_KEY);
                            if (team.hasNodes()){
                                Iterator<Node> nodeIterator = team.getNodes();
                                ArrayList<Map<String, Object>> teammates = new ArrayList<>();
                                while (nodeIterator.hasNext()){
                                    Node teammateNode = nodeIterator.next();
                                    String username = teammateNode.getName();
                                    if (root.hasNode(SKILLKIT_USERS_PATH + username)) {
                                        Node user = root.getNode(SKILLKIT_USERS_PATH + username);
                                        Map<String, Object> map = jcrUtils.getPropertiesFromNode(user);
                                        if (!(map.isEmpty())) {
                                            teammates.add(map);
                                        }
                                    }
                                }
                                if (!(teammates.isEmpty())){
                                    projectMap.put(TEAM_KEY, teammates);
                                }
                            }
                        }
                    }
                }
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        }
        return projectMap;
    }
}
