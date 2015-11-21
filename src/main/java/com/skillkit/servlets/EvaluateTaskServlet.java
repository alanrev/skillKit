package com.skillkit.servlets;

import com.skillkit.utils.JCRUtils;
import com.skillkit.utils.StringValidations;
import com.sun.javafx.tk.Toolkit;

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
import java.util.Map;

import static com.skillkit.utils.Constants.*;
import static com.skillkit.utils.Constants.ROLE_KEY;
import static com.skillkit.utils.Constants.STATUS_KEY;

/**
 * Created by Allan on 09/11/2015.
 */
@WebServlet("/EvaluateTask")
public class EvaluateTaskServlet extends HttpServlet{

    String error = BLANK;
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
            String userscounterP = request.getParameter(USERS_KEY);
            String ip = request.getRemoteAddr();
            JCRUtils jcrUtils = new JCRUtils();
            Session jcrSession = jcrUtils.repoLogin();
            if (jcrSession != null) {
                try {
                    if (stringValidations.validateUsername(username)) {
                        Node userNode = jcrUtils.authenticate(username, ip);
                        if (userNode != null) {
                            if (userNode.hasProperty(ROLE_KEY)) {
                                String role = userNode.getProperty(ROLE_KEY).getString();
                                if (role.equals("1")) {
                                        Node task = getTaskNode(project, tid, username, jcrSession);
                                        Map<String, Object> taskMap = jcrUtils.getPropertiesFromNode(task);
                                        if ((task != null) && (userscounterP != null)) {
                                            System.out.println("entro");
                                            int counter = Integer.parseInt(userscounterP);
                                            System.out.println(taskMap.toString());
                                            if (!taskMap.isEmpty()){
                                                if(!taskMap.containsKey(EVALUATED)) {
                                                    if (taskMap.containsKey(MAIN_SKILL)) {
                                                        if (taskMap.get(MAIN_SKILL) instanceof String) {
                                                            String mainSkill = (String) taskMap.get(MAIN_SKILL);
                                                            ArrayList<String> users = new ArrayList<>();
                                                            ArrayList<String> skillRates = new ArrayList<>();
                                                            for (int index = 0; index < counter; index++) {
                                                                String resolvedUser = request.getParameter(USER + index);
                                                                String skillrate = request.getParameter(SKILL_RATE + index);
                                                                users.add(resolvedUser);
                                                                skillRates.add(skillrate);
                                                            }
                                                            Boolean isEvaluated = evaluateTaskSkill(mainSkill, skillRates,
                                                                    users, jcrSession);
                                                            System.out.println(isEvaluated);
                                                            if (isEvaluated){
                                                                task.setProperty(EVALUATED, true);
                                                                jcrSession.save();
                                                                error = BLANK;
                                                            }else{
                                                                error = "12";
                                                            }
                                                        }
                                                    }
                                                }
                                            }else{
                                                error = "11";
                                            }
                                        }
                                }else{
                                    error = "9";
                                }
                            }else{
                                error = "9";
                            }
                        }else{
                            error = "0";
                        }
                    }else{
                        error = "0";
                    }
                }catch (RepositoryException r){
                    r.printStackTrace();
                }
            }
            String path = SKILLKIT_HOST_PATH + SLASH + "task.jsp?" + PROJECT + EQUAL_KEY + project +
                    AND + ID + EQUAL_KEY + tid;
            if (error.equals(BLANK)) {
                response.sendRedirect(path);
            }else{
                response.sendRedirect(path + AND + ERROR + EQUAL_KEY + error);
            }
        }
    }

    private Node getTaskNode(String proj, String tid, String username, Session jcrSession){
        if (jcrSession != null){
            if ((!proj.isEmpty()) && (!tid.isEmpty()) && (!username.isEmpty())) {
                if ((proj != null) &&(tid != null) && (username!= null)) {
                    try {
                        Node root = jcrSession.getRootNode();
                        if (root.hasNode(SKILLKIT_PROJECTS_PATH + proj)) {
                            Node projNode = root.getNode(SKILLKIT_PROJECTS_PATH + proj);
                            if (projNode != null) {
                                if (projNode.hasProperty(PM_KEY)) {
                                    String projPm = projNode.getProperty(PM_KEY).getString();
                                    if (projPm.equals(username)) {
                                        if (projNode.hasNode(TASKS_KEY + SLASH + TASK_KEY + tid)) {
                                            Node task = projNode.getNode(TASKS_KEY + SLASH + TASK_KEY + tid);
                                            return task;
                                        }
                                    }
                                }
                            }
                        }
                    } catch (RepositoryException r) {
                        r.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    private Boolean evaluateTaskSkill(String skillname, ArrayList<String> rates,
                                      ArrayList<String> users, Session jcrSession){
        Boolean isEvaluated = false;
        if (jcrSession != null){
         try{
             Node root = jcrSession.getRootNode();
             JCRUtils jcrUtils = new JCRUtils();
             if (root != null){
                 for (int index = 0; index < users.size(); index++){
                     String user = users.get(index);
                     String skillrate = rates.get(index);
                     isEvaluated = false;
                     if (skillrate != null) {
                         Double sRate = Double.parseDouble(skillrate);
                         String skillPath = SKILLKIT_USERS_PATH + user + SLASH + SKILLS_NODE_KEY + SLASH + skillname;
                         if (sRate > 0) {
                             if (root.hasNode(skillPath)) {
                                 Node skillNode = root.getNode(skillPath);
                                 Map<String, Object> skillMap = jcrUtils.getPropertiesFromNode(skillNode);
                                 if (skillMap.containsKey(SKILL_RATE)) {
                                     String rateS = (String) skillMap.get(SKILL_RATE);
                                     Double rate = Double.parseDouble(rateS);
                                     if (skillMap.containsKey(EVALUATIONS)) {
                                         String eva = (String) skillMap.get(EVALUATIONS);
                                         int evaluations = Integer.parseInt(eva) + 1;
                                         Double newRate =  (rate + sRate) / evaluations;
                                         skillNode.setProperty(SKILL_RATE, BLANK + newRate);
                                         skillNode.setProperty(EVALUATIONS, evaluations);
                                         jcrSession.save();
                                         isEvaluated = true;
                                     }else{
                                         int evaluations = 2;
                                         Double newRate =  (rate + sRate) / evaluations;
                                         skillNode.setProperty(SKILL_RATE, BLANK + newRate);
                                         skillNode.setProperty(EVALUATIONS, evaluations);
                                         jcrSession.save();
                                         isEvaluated = true;
                                     }
                                 }
                             }else{
                                 String usersPath = SKILLKIT_USERS_PATH + user;
                                 if (root.hasNode(usersPath)){
                                     Node userNode = root.getNode(usersPath);
                                     if (userNode != null){
                                         if (userNode.hasNode(SKILLS_NODE_KEY)){
                                             Node skills = userNode.getNode(SKILLS_NODE_KEY);
                                             if (skills != null){
                                                 Node skill = skills.addNode(skillname);
                                                 skill.setProperty(SKILL_RATE, skillrate);
                                                 skill.setProperty(EVALUATIONS, 1);
                                                 jcrSession.save();
                                                 isEvaluated = true;
                                             }
                                         }else{
                                             Node skills = userNode.addNode(SKILLS_NODE_KEY);
                                             Node skill = skills.addNode(skillname);
                                             skill.setProperty(SKILL_RATE, skillrate);
                                             skill.setProperty(EVALUATIONS, 1);
                                             jcrSession.save();
                                             isEvaluated = true;
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
        return isEvaluated;
    }

}
