package com.skillkit.controller;

import com.google.gson.Gson;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.skillkit.utils.Constants.*;

/**
 * Created by Allan on 23/09/2015.
 */
@WebServlet(urlPatterns = {"/GetProjects"})
public class GetProjectsServlet extends HttpServlet {


    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException{
        process(request, response);

    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if ((request != null) &&(response != null)) {
            StringValidations stringValidations = new StringValidations();
            String username = request.getParameter(USERNAME_KEY);
            String ip = request.getRemoteAddr();
            JCRUtils jcrUtils = new JCRUtils();
            Session jcrSession = jcrUtils.repoLogin();
            List<Map<String, Object>> projectsList = new ArrayList<>();
            if(jcrSession != null) {
                if (stringValidations.validateUsername(username)) {
                    Node userNode = jcrUtils.authenticate(username, ip);
                    if (userNode != null) {
                        projectsList = getProjects(jcrSession, userNode, jcrUtils);
                        String json = new Gson().toJson(projectsList);
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json");
                        response.getWriter().write(json);
                    }
                }
            }
        }
    }

    private List<Map<String, Object>> getProjects(Session session, Node usernode, JCRUtils jcrUtils){
        List<Map<String, Object>> projects = new ArrayList<>();
        if ((session != null) && (usernode != null)) {
            try{
                Node root = session.getRootNode();
                if (root.hasNode(SKILLKIT_PROJECTS_PATH)) {
                    Node projectsNode = root.getNode(SKILLKIT_PROJECTS_PATH);
                    if (projectsNode != null) {
                        if (usernode.hasProperty(ROLE_KEY)) {
                            Property roleP = usernode.getProperty(ROLE_KEY);
                            String role = roleP.getString();
                            if (role.equals("1")) {
                                List<Map<String, Object>> listOfProjects = jcrUtils.getNodeListProperties(projectsNode);
                                for (Map<String, Object> proj: listOfProjects){
                                    if (proj.containsKey(PM_KEY)){
                                        String projPM = proj.get(PM_KEY).toString();
                                        if (projPM.equals(usernode.getName())){
                                            projects.add(proj);
                                        }
                                    }
                                }
                            }else{
                                if (role.equals("2")){
                                   if (projectsNode.hasNodes()){
                                       Iterator<Node> projectsIterator = projectsNode.getNodes();
                                       while(projectsIterator.hasNext()){
                                           Node proj = projectsIterator.next();
                                           if (proj.hasNodes()){
                                               if (proj.hasNode(TEAM_KEY)) {
                                                   Node team = proj.getNode(TEAM_KEY);
                                                   if (team.hasNode(usernode.getName())){
                                                       Map<String, Object> properties =
                                                               jcrUtils.getPropertiesFromNode(proj);
                                                       properties.put(NAME, proj.getName());
                                                       projects.add(properties);
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
            } catch (RepositoryException re) {

            }
        }
        return projects;
    }

}
