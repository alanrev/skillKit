package com.skillkit.servlets;

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
import java.util.Map;

import static com.skillkit.utils.Constants.*;
import static com.skillkit.utils.Constants.STATUS_KEY;

/**
 * Created by Allan on 20/11/2015.
 */
@WebServlet("/AddComment")
public class AddCommentsServlet extends HttpServlet {
    String error = BLANK;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        process(request, response);

    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if ((request != null) && (response != null)) {
            StringValidations stringValidations = new StringValidations();
            String username = request.getParameter(USERNAME_KEY);
            String project = request.getParameter(PROJECT);
            String tid = request.getParameter(ID);
            String comment = request.getParameter(COMMENT);
            String ip = request.getRemoteAddr();
            JCRUtils jcrUtils = new JCRUtils();
            if (stringValidations.validateUsername(username) && (!comment.isEmpty()) &&(comment != null)) {
                Node userNode = jcrUtils.authenticate(username, ip);
                if (userNode != null) {
                    String path = SKILLKIT_PROJECTS_PATH+ project+ SLASH+ TASKS_KEY+ SLASH + TASK_KEY + tid;
                    Boolean isSuccess = addCommet(comment, username, path);
                    if (isSuccess){
                        error = BLANK;
                    }else{
                        error = "4";
                    }
                }else{
                    error = "4";
                }
            }else{
                error = "4";
            }
            String path = SKILLKIT_HOST_PATH + SLASH + "task.jsp?" + PROJECT + EQUAL_KEY + project +
                    AND + ID + EQUAL_KEY + tid;
            if (!error.equals(BLANK)) {
                path = path + AND + ERROR + EQUAL_KEY + error;
            }
            response.sendRedirect(path);
        }
    }

    private Boolean addCommet(String comment, String username, String path){
        JCRUtils jcrUtils = new JCRUtils();
        Session jcrSession = jcrUtils.repoLogin();
        if(jcrSession != null){
            try{
                Node root = jcrSession.getRootNode();
                if (root != null){
                    if (root.hasNode(path)){
                        Node taskNode = root.getNode(path);
                        if (taskNode != null){
                            if (taskNode.hasNode(COMMENTS)){
                                System.out.println(comment);
                                Node comments = taskNode.getNode(COMMENTS);
                                Map<String,Object> commentsMap = jcrUtils.getPropertiesFromNode(comments);
                                if (commentsMap.containsKey(COMMENTS)){
                                   String counterString = (String) commentsMap.get(COMMENTS);
                                   int counter = Integer.parseInt(counterString);
                                    counter++;
                                    if (!comments.hasNode(COMMENT+counter)){
                                        System.out.println(comment +"***");
                                        Node commentNode = comments.addNode(COMMENT+counter);
                                        commentNode.setProperty(USERNAME_KEY, username);
                                        commentNode.setProperty(COMMENT, comment);
                                        comments.setProperty(COMMENTS, ""+ counter);
                                        jcrSession.save();
                                        return true;
                                    }
                                }
                            }else{
                                Node comments = taskNode.addNode(COMMENTS);
                                Node commentNode = comments.addNode(COMMENT + 0);
                                commentNode.setProperty(USERNAME_KEY, username);
                                commentNode.setProperty(COMMENT, comment);
                                comments.setProperty(COMMENTS, "1");
                                jcrSession.save();
                                return true;
                            }
                        }
                    }
                }
            }catch (RepositoryException e){
                e.printStackTrace();
            }
            jcrUtils.repoLogout(jcrSession);
        }
        return false;
    }

}
