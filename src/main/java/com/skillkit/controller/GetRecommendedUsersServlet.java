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
import java.util.*;

import static com.skillkit.utils.Constants.*;

/**
 * Created by Allan on 25/10/2015.
 */
@WebServlet("/GetRecommendedUsers")
public class GetRecommendedUsersServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException{
        process(request, response);

    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
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
                            if (userNode.hasProperty(ROLE_KEY)) {
                                String role = userNode.getProperty(ROLE_KEY).getString();
                                if (role.equals("1")){
                                    Map<String, Object> recommendedUser = GetRecommendUsersFromProject(tid,
                                            project, jcrSession);
                                    String json = new Gson().toJson(recommendedUser);
                                    response.setCharacterEncoding("UTF-8");
                                    response.setContentType("application/json");
                                    response.getWriter().write(json);
                                }
                            }
                        }
                    }
                    jcrUtils.repoLogout(jcrSession);
                }
            }
        }catch (RepositoryException e){
            e.printStackTrace();
        }
    }

    private Map<String, Object> GetRecommendUsersFromProject(String tid, String project, Session jcrSession){
        Map<String, Object> recommendUsers = new HashMap<>();
        List<Map<String, Object>> usersFromProject = new ArrayList<>();
        List<Map<String, Object>> recommendUsersFromProject = new ArrayList<>();
        List<Map<String, Object>> recommendUsersFromUsers = new ArrayList<>();
        String mainSkill = BLANK;
        String mainSkillRate = BLANK;
        JCRUtils jcrUtils = new JCRUtils();
        if (jcrSession != null){
            try {
                if ((tid != null) && (!tid.isEmpty()) && (project != null) && (!project.isEmpty())) {
                    Node root = jcrSession.getRootNode();
                    ArrayList<String> teammateNames = new ArrayList<>();
                    if (root.hasNode(SKILLKIT_PROJECTS_PATH + project)){
                        Node projectNode = root.getNode(SKILLKIT_PROJECTS_PATH + project);
                        if (projectNode != null) {
                            if (projectNode.hasNode(TEAM_KEY)) {
                                Node teamNode = projectNode.getNode(TEAM_KEY);
                                if (teamNode != null) {
                                    if (teamNode.hasNodes()) {
                                        Iterator<Node> teammates = teamNode.getNodes();
                                        while (teammates.hasNext()) {
                                            Node mate = teammates.next();
                                            if (mate != null) {
                                                teammateNames.add(mate.getName());
                                            }
                                        }
                                    }
                                }
                                if (projectNode.hasNode(TASKS_KEY + SLASH + TASK_KEY + tid )){
                                    Node task = projectNode.getNode(TASKS_KEY + SLASH + TASK_KEY + tid);
                                    if (task != null){
                                        if (task.hasProperty(MAIN_SKILL)) {
                                            mainSkill = task.getProperty(MAIN_SKILL).getString();
                                        }
                                        if (task.hasProperty(SKILL_RATE)){
                                            mainSkillRate = task.getProperty(SKILL_RATE).getString();
                                        }
                                    }
                                }
                            }
                        }
                        if ((!mainSkill.equals(BLANK))&&(!mainSkillRate.equals(BLANK))
                                && (mainSkill != null) && (mainSkillRate != null)){
                            Double msrate = Double.parseDouble(mainSkillRate);
                            for (String name : teammateNames){
                                if (root.hasNode(SKILLKIT_USERS_PATH + name)){
                                    Node user =root.getNode(SKILLKIT_USERS_PATH + name);
                                    if (user != null) {
                                        Map<String, Object> userMap = jcrUtils
                                                .getPropertiesFromNode(user);
                                        userMap.put(USERNAME_KEY, user.getName());
                                        userMap.put(SKILL_NAME, mainSkill);
                                        if (user.hasNode(SKILLS_NODE_KEY + SLASH + mainSkill)) {
                                            Node mainSkillNode = user.getNode(SKILLS_NODE_KEY + SLASH + mainSkill);
                                            if (mainSkillNode != null){
                                                if (mainSkillNode.hasProperty(SKILL_RATE)){
                                                    String userSkillRateS = mainSkillNode.getProperty(SKILL_RATE)
                                                                                        .getString();
                                                    Double userSkillRate = Double.parseDouble(userSkillRateS);
                                                    recommendUsers.put(SKILLS_NODE_KEY, mainSkill);
                                                    recommendUsers.put(SKILL_RATE, msrate);
                                                    userMap.put(SKILL_RATE, userSkillRate);
                                                    usersFromProject.add(userMap);
                                                    if ((userSkillRate > 0.5) && (msrate > 0.5)) {
                                                        if (userSkillRate > msrate - 0.5) {
                                                            recommendUsersFromProject.add(userMap);
                                                        }
                                                    }
                                                }else{
                                                    mainSkillNode.setProperty(SKILL_RATE, "0");
                                                    jcrSession.save();
                                                }
                                            }
                                        }else{
                                            userMap.put(SKILL_RATE, 0);
                                            usersFromProject.add(userMap);
                                        }
                                    }
                                }
                            }

                        }
                    }
                    if ((!mainSkill.equals(BLANK))&&(!mainSkillRate.equals(BLANK))
                            && (mainSkill != null) && (mainSkillRate != null)){
                        if (root.hasNode(SKILLKIT_USERS_PATH)){
                            Node users = root.getNode(SKILLKIT_USERS_PATH);
                            Double msrate = Double.parseDouble(mainSkillRate);
                            if (users != null){
                                if (users.hasNodes()) {
                                    Iterator<Node> usersIterator = users.getNodes();
                                    while(usersIterator.hasNext()){
                                        Node user = usersIterator.next();
                                        if (user.hasNode(SKILLS_NODE_KEY + SLASH + mainSkill)){
                                            Node skillNode = user.getNode(SKILLS_NODE_KEY + SLASH + mainSkill);
                                            if (skillNode.hasProperty(SKILL_RATE)){
                                                String userSkillRateS = skillNode.getProperty(SKILL_RATE)
                                                        .getString();
                                                Double userSkillRate = Double.parseDouble(userSkillRateS);
                                                recommendUsers.put(SKILLS_NODE_KEY, mainSkill);
                                                recommendUsers.put(SKILL_RATE, msrate);
                                                if ((userSkillRate > 0.5) && (msrate > 0.5)) {
                                                    if (userSkillRate > msrate - 0.5) {
                                                        Map<String, Object> userMap = jcrUtils
                                                                .getPropertiesFromNode(user);
                                                        userMap.put(USERNAME_KEY, user.getName());
                                                        userMap.put(SKILL_NAME, mainSkill);
                                                        userMap.put(SKILL_RATE, userSkillRate);
                                                        recommendUsersFromUsers.add(userMap);
                                                    }
                                                }
                                            }else{
                                                skillNode.setProperty(SKILL_RATE, "0");
                                                jcrSession.save();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }catch (RepositoryException r){
                r.printStackTrace();
            }
        }
        recommendUsers.put(USERS_FROM_PROJECT, recommendUsersFromProject);
        if (!recommendUsersFromUsers.isEmpty()){
            if (!recommendUsersFromProject.isEmpty()){
                List<Map<String, Object>> usersFromUsersList = recommendUsersFromUsers;
                for (int indexx = 0; indexx < usersFromUsersList.size(); indexx++) {
                    Map<String, Object> userFromProject = usersFromUsersList.get(indexx);
                    if (userFromProject.containsKey(USERNAME_KEY)) {
                        if (userFromProject.get(USERNAME_KEY) instanceof String) {
                            String username = (String) userFromProject.get(USERNAME_KEY);
                            for (int index = 0; index < recommendUsersFromProject.size(); index++) {
                                Map<String, Object> userFromUsers = recommendUsersFromProject.get(index);
                                if (userFromUsers.get(USERNAME_KEY) instanceof String) {
                                    String usernam = (String) userFromProject.get(USERNAME_KEY);
                                    if (usernam.equals(username)) {
                                        recommendUsersFromUsers.remove(userFromUsers);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        recommendUsers.put(OTHER_USERS, recommendUsersFromUsers);
        recommendUsers.put(USERS_KEY, usersFromProject);
        return recommendUsers;
    }
}
