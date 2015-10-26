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

import static com.skillkit.utils.Constants.*;
import static com.skillkit.utils.Constants.EQUAL_KEY;

/**
 * Created by Allan on 23/09/2015.
 */
@WebServlet(urlPatterns = {"/CreateProject"})
public class CreateProjectServlet extends HttpServlet {

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
                String projectName = request.getParameter(PROJECT_NAME_KEY);
                String projectdescription = request.getParameter(PROJECT_DESCRIPTION_KEY);
                String startDate = request.getParameter(START_DATE);
                String error = BLANK;
                try {
                    String ip = request.getRemoteAddr();
                    JCRUtils jcrUtils = new JCRUtils();
                    StringValidations stringValidations = new StringValidations();
                    Session jcrSession = jcrUtils.repoLogin();
                    if (jcrSession != null) {
                        if ((stringValidations.validateUsername(username)) && (ip != null) && (!(ip.isEmpty()))) {
                            Node userNode = jcrUtils.authenticate(username, ip);
                            if (userNode != null) {
                                if (userNode.hasProperty(ROLE_KEY)) {
                                    Property roleProp = userNode.getProperty(ROLE_KEY);
                                    String role = roleProp.getString();
                                    String firstname = BLANK;
                                    String lastname = BLANK;
                                    if (role.equals("1")) {
                                        if (userNode.hasProperty(FIRSTNAME_KEY)){
                                            firstname = userNode.getProperty(FIRSTNAME_KEY).getString();
                                        }
                                        if (userNode.hasProperty(LASTNAME_KEY)){
                                            lastname = userNode.getProperty(LASTNAME_KEY).getString();
                                        }
                                        Node project = createProject(projectName, jcrSession);
                                        if (project != null) {
                                            if ((!projectdescription.isEmpty()) &&(projectdescription !=null)) {
                                                project.setProperty(PROJECT_DESCRIPTION_KEY, projectdescription);
                                            }
                                            if ((!startDate.isEmpty()) &&(startDate !=null)) {
                                                project.setProperty(START_DATE, startDate);
                                            }
                                            project.setProperty(PM_KEY, username);
                                            project.setProperty(PM_NAME, firstname + EMPTY_SPACE + lastname);
                                            jcrSession.save();
                                            response.sendRedirect(SKILLKIT_HOST_PATH + SLASH + "projects.jsp" +
                                                    EXCLAMATION_MARK + "success=0");
                                        }else{
                                            error = "5";
                                        }
                                    }else{
                                        error="6";
                                    }
                                }else{
                                    error = "4";
                                }
                            } else {
                                error = "0";
                            }
                        } else {
                            error = "1";
                        }
                    } else {
                        error = "2";
                    }
                    if (!error.equals(BLANK)) {
                        response.sendRedirect(SKILLKIT_HOST_PATH + SLASH + "projects.jsp" + EXCLAMATION_MARK + ERROR
                                + EQUAL_KEY + error);
                    }
                } catch (RepositoryException re){
                    response.sendRedirect(SKILLKIT_HOST_PATH + SLASH + "projects.jsp" + EXCLAMATION_MARK + ERROR
                            + EQUAL_KEY + 3);
                    re.printStackTrace();
                }

            }
    }

    private Node createProject(String projectname, Session jcrSession) throws RepositoryException {
        if ((!projectname.isEmpty()) && (projectname != null) ){
            if (jcrSession != null){
                Node root = jcrSession.getRootNode();
                if (root.hasNode(SKILLKIT_PROJECTS_PATH)){
                    Node projectsNode = root.getNode(SKILLKIT_PROJECTS_PATH);
                    if (!(projectsNode.hasNode(projectname))) {
                        Node projectNode = projectsNode.addNode(projectname);
                        jcrSession.save();
                        return projectNode;
                    }
                }else{
                    if (root.hasNode(SKILLKIT_KEY)){
                        Node skillkit = root.getNode(SKILLKIT_KEY);
                        Node projects = skillkit.addNode(PROJECTS);
                        Node project = projects.addNode(projectname);
                        jcrSession.save();
                        return project;
                    }
                }
            }
        }

        return null;
    }
}
