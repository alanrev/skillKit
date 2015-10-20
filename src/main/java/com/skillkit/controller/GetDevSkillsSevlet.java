package com.skillkit.controller;

import com.google.gson.Gson;
import com.skillkit.utils.JCRUtils;
import org.w3c.dom.traversal.NodeIterator;

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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.skillkit.utils.Constants.*;

/**
 * Created by Allan on 18/10/2015.
 */

@WebServlet("/getDevSkills")
public class GetDevSkillsSevlet extends HttpServlet {

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
            String ip = request.getRemoteAddr();
            JCRUtils jcrUtils = new JCRUtils();
            Session jcrSession = jcrUtils.repoLogin();
            if (jcrSession != null) {
                Node usernode = jcrUtils.authenticate(username, ip);
                if (usernode != null) {
                    List<String> skills = getDevSkill(jcrSession);
                    if (!(skills.isEmpty())){
                        String json = new Gson().toJson(skills);
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json");
                        response.getWriter().write(json);
                    }
                }
            }
        }
    }

    private List<String> getDevSkill(Session jcrSession){
        List<String> skills = new ArrayList<>();
        if (jcrSession != null) {
            try {
                Node root = jcrSession.getRootNode();
                if (root.hasNode(SKILLKIT_SKILLS_PATH + "1")){
                    Node devSkills = root.getNode(SKILLKIT_SKILLS_PATH + "2");
                    if (devSkills.hasNodes()){
                        Iterator<Node> nodeIterator = devSkills.getNodes();
                        while(nodeIterator.hasNext()){
                            skills.add(nodeIterator.next().getName());
                        }
                    }
                }
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        }
        return skills;
    }
}
