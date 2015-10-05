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
 * Created by Allan on 04/10/2015.
 */
@WebServlet(urlPatterns = {"/GetFreeUsers"})
public class GetFreeUsersServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException{
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if ((request != null)&&(response != null)){
            StringValidations stringValidations = new StringValidations();
            String username = request.getParameter(USERNAME_KEY);
            String ip = request.getRemoteAddr();
            JCRUtils jcrUtils = new JCRUtils();
            Session jcrSession = jcrUtils.repoLogin();
            if (jcrSession != null) {
                if (stringValidations.validateUsername(username)) {
                    Node usernode = jcrUtils.authenticate(username, ip);
                    if (usernode != null) {
                        List<Map<String, Object>> freeUsers = getListOfFreeUsers(jcrSession);
                        String json = new Gson().toJson(freeUsers);
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json");
                        response.getWriter().write(json);
                    }
                }
            }
        }
    }

    private List<Map<String, Object>> getListOfFreeUsers(Session jcrSession){
        List<Map<String, Object>> freeUsers = new ArrayList<>();
        try{
            Node root = jcrSession.getRootNode();
            JCRUtils jcrUtils = new JCRUtils();
            if (root.hasNode(SKILLKIT_USERS_PATH)){
                Node usersnode = root.getNode(SKILLKIT_USERS_PATH);
                if (usersnode != null){
                    if (usersnode.hasNodes()){
                        List<Map<String, Object>> users = jcrUtils.getNodeListProperties(usersnode);
                        for (Map<String, Object> map: users){
                            if (map.containsKey(ROLE_KEY)){
                                String role = map.get(ROLE_KEY).toString();
                                if (role.equals("2")){
                                    if (!(map.containsKey(CURRENT_PROJECT))){
                                        freeUsers.add(map);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (RepositoryException re){
            re.printStackTrace();
        }
        return freeUsers;
    }
}
