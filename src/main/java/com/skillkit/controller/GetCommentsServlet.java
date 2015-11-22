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


@WebServlet(urlPatterns = {"/GetComments"})

/**
 * Created by Allan on 21/11/2015.
 */
public class GetCommentsServlet extends HttpServlet {

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
            JCRUtils jcrUtils = new JCRUtils();
            StringValidations stringValidations = new StringValidations();
            Session jcrSession = jcrUtils.repoLogin();
            String username = request.getParameter(USERNAME_KEY);
            String project = request.getParameter(PROJECT);
            String tid = request.getParameter(ID);
            String ip = request.getRemoteAddr();
            if (jcrSession != null) {
                if (stringValidations.validateUsername(username)) {
                    Node userNode = jcrUtils.authenticate(username, ip);
                    if (userNode != null) {
                        String taskPath = SKILLKIT_PROJECTS_PATH + project + SLASH + TASKS_KEY +SLASH + TASK_KEY + tid;
                        List<Map<String, Object>> list = getComments(taskPath, jcrSession);
                        String json = new Gson().toJson(list);
                        response.setContentType("application/json");
                        response.getWriter().write(json);
                    }
                }
             jcrUtils.repoLogout(jcrSession);
            }
        }
    }


    private List<Map<String, Object>> getComments(String path, Session jcrSession) {
        List<Map<String, Object>> commentsList = new ArrayList();
        JCRUtils jcrUtils = new JCRUtils();
        if ((jcrSession != null)&&(!path.isEmpty())&&(path!= null)){
            try {
                Node root = jcrSession.getRootNode();
                if (root.hasNode(path)){
                    Node task = root.getNode(path);
                    if (task != null) {
                        if (task.hasNode(COMMENTS)){
                            Node comments = task.getNode(COMMENTS);
                            commentsList  = jcrUtils.getNodeListProperties(comments);
                            return commentsList;
                        }
                    }
                }
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        }
        return commentsList;
    }
}
