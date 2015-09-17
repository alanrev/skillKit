package com.skillkit.controller;

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

import com.google.gson.Gson;
import com.skillkit.model.skillData;
import com.skillkit.utils.JCRUtils;

import static com.skillkit.utils.Constants.*;

/**
 * Created by Allan on 16/09/2015.
 */
@WebServlet(urlPatterns = {"/GetSkill"})
public class GetUsersSkillsServlet extends HttpServlet{

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }

    protected void doPost(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException{
        process(request, response);

    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if ((request != null) && (response != null)) {
                JCRUtils jcrUtils = new JCRUtils();
                Session jcrSession = jcrUtils.repoLogin();
                String username = request.getParameter(USERNAME_KEY);
                String ip = request.getRemoteAddr();
                if (jcrSession != null) {
                    if ((username != null) && (!(username.isEmpty())) && (!(ip.isEmpty())) && (ip != null)) {
                        Node user = jcrUtils.authenticate(username, ip);
                        if (user != null) {
                            if(user.hasNode(SKILLS_NODE_KEY)){
                                Node userSkills = user.getNode(SKILLS_NODE_KEY);
                                if (userSkills.hasNodes()){
                                    Iterator<Node> skillNodeIterator = userSkills.getNodes();
                                    ArrayList<skillData> skillsArray = new ArrayList<>();
                                    while(skillNodeIterator.hasNext()){
                                        Node skill = skillNodeIterator.next();
                                        skillData skillData = new skillData();
                                        skillData.setSkillName(skill.getName());
                                        if (skill.hasProperty(SKILL_DESCRIPTION)){
                                            Property descProperty = skill.getProperty(SKILL_DESCRIPTION);
                                            if (descProperty != null) {
                                                String desc = descProperty.getString();
                                                skillData.setDescription(desc);
                                            }
                                        }
                                        if (skill.hasProperty(SKILL_RATE)){
                                            Property rateProperty = skill.getProperty(SKILL_RATE);
                                            if (rateProperty != null) {
                                                String rate = rateProperty.getString();
                                                skillData.setSkillRate(rate);
                                            }
                                        }
                                        skillsArray.add(skillData);
                                    }
                                    String json = new Gson().toJson(skillsArray);
                                    response.setContentType("application/json");
                                    response.getWriter().write(json);
                                }
                            }
                        } else {
                            skillData skillData = new skillData();
                            skillData.setError("Invalid Session");
                        }
                    }else {
                        skillData skillData = new skillData();
                        skillData.setError("Please log in again");
                    }
                    jcrUtils.repoLogout(jcrSession);
                }else {
                    skillData skillData = new skillData();
                    skillData.setError("Invalid Session");
                }
            }
        } catch (RepositoryException re) {
            System.out.println(re);
        }

    }
}
