package com.skillkit.controller;

import com.google.gson.Gson;
import com.skillkit.model.skillData;
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
 * Created by Allan on 20/09/2015.
 */
@WebServlet(urlPatterns = {"/GetUserRoleSkills"})
public class GetUserRoleSkillsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException{
        process(request, response);

    }

    private void process(HttpServletRequest request, HttpServletResponse response){
        try {
            if ((request != null) && (response != null)) {
                JCRUtils jcrUtils = new JCRUtils();
                StringValidations stringValidations = new StringValidations();
                Session jcrSession = jcrUtils.repoLogin();
                String username = request.getParameter(USERNAME_KEY);
                String ip = request.getRemoteAddr();
                if (jcrSession != null) {
                    if (stringValidations.validateUsername(username)){
                        Node userNode = jcrUtils.authenticate(username, ip);
                        if (userNode != null) {
                            if (userNode.hasProperty(ROLE_KEY)) {
                                Property roleProperty = userNode.getProperty(ROLE_KEY);
                                String role = roleProperty.getString();
                                Node rootNode = jcrSession.getRootNode();
                                if (rootNode != null) {
                                    if (rootNode.hasNode(SKILLKIT_SKILLS_PATH + role)) {
                                        Node roleSkillsNode = rootNode.getNode(SKILLKIT_SKILLS_PATH + role);
                                        if (roleSkillsNode != null) {
                                            List<Map<String, Object>> roleSkillsMapList =
                                                    jcrUtils.getNodeListProperties(roleSkillsNode);
                                            if (!(userNode.hasNode(SKILLS_NODE_KEY))) {
                                                ArrayList<skillData> skillsArray = new ArrayList<>();
                                                if (!(roleSkillsMapList.isEmpty())){
                                                    for (Map<String, Object> map: roleSkillsMapList){
                                                        skillData skillData = new skillData();
                                                        if (map.containsKey(NAME)){
                                                            String name = map.get(NAME).toString();
                                                            skillData.setSkillName(name);
                                                        }
                                                        if (map.containsKey(SKILL_DESCRIPTION)){
                                                            String desc = map.get(SKILL_DESCRIPTION).toString();
                                                            skillData.setDescription(desc);
                                                        }
                                                        skillsArray.add(skillData);
                                                    }
                                                }
                                                String json = new Gson().toJson(skillsArray);
                                                response.setContentType("application/json");
                                                response.getWriter().write(json);
                                            }else{
                                                Node userSkills = userNode.getNode(SKILLS_NODE_KEY);
                                                ArrayList<skillData> skillsArray = new ArrayList<>();
                                                List<Map<String, Object>> roleSkillsMapList2 =
                                                        jcrUtils.getNodeListProperties(roleSkillsNode);
                                                if ((userSkills.hasNodes()) && (!(roleSkillsMapList.isEmpty()))){
                                                    Iterator<Node> skillNodes = userSkills.getNodes();
                                                    while (skillNodes.hasNext()){
                                                        Node skillNode = skillNodes.next();
                                                        String skillName = skillNode.getName();
                                                        for (Map<String, Object> map : roleSkillsMapList2){
                                                            if(map.containsKey(NAME)){
                                                                String name = map.get(NAME).toString();
                                                                if (name.equals(skillName)){
                                                                    roleSkillsMapList.remove(map);
                                                                }
                                                            }
                                                        }
                                                    }
                                                    for(Map<String, Object> map : roleSkillsMapList){
                                                        skillData skillData = new skillData();
                                                        if (map.containsKey(NAME)){
                                                            String name = map.get(NAME).toString();
                                                            skillData.setSkillName(name);
                                                        }
                                                        if (map.containsKey(SKILL_DESCRIPTION)){
                                                            String desc = map.get(SKILL_DESCRIPTION).toString();
                                                            skillData.setDescription(desc);
                                                        }
                                                        skillsArray.add(skillData);
                                                    }
                                                }
                                                String json = new Gson().toJson(skillsArray);
                                                response.setContentType("application/json");
                                                response.getWriter().write(json);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    jcrUtils.repoLogout(jcrSession);
                }
            }
        }catch (RepositoryException re){
                re.printStackTrace();
            } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
