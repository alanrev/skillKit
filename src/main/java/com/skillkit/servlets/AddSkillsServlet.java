package com.skillkit.servlets;

import com.skillkit.utils.JCRUtils;
import com.skillkit.utils.StringValidations;
import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;

import javax.jcr.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import static com.skillkit.utils.Constants.*;
import static com.skillkit.utils.Constants.SKILLS_NODE_KEY;
import static com.skillkit.utils.Constants.SKILL_DESCRIPTION;

/**
 * Created by Allan on 20/11/2015.
 */
@WebServlet("/AddSkills")
public class AddSkillsServlet extends HttpServlet {

    String error = BLANK;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        if ((request != null) && (response != null)) {
            String username = request.getParameter(USERNAME_KEY);
            String skillCounterString = request.getParameter(SKILLS_NODE_KEY);
            JCRUtils jcrUtils = new JCRUtils();
            String ip = request.getRemoteAddr();
            if ((!(username.isEmpty()))&& (username != null) && (ip != null) && (!(ip.isEmpty()))) {
                Node userNode = jcrUtils.authenticate(username, ip);
                if (userNode != null) {
                    Session jcrSession = jcrUtils.repoLogin();
                    if (jcrSession != null) {
                        int counter = Integer.parseInt(skillCounterString);
                        ArrayList<String> skills = new ArrayList<>();
                        ArrayList<String> skillRates = new ArrayList<>();
                        ArrayList<String> skillDescriptions = new ArrayList<>();
                        for (int index = 0; index < counter; index++) {
                            String skillname = request.getParameter(SKILL_NAME + index);
                            String skillrate = request.getParameter(SKILL_RATE + index);
                            String skillDescrip = request.getParameter(SKILL_DESCRIPTION + index);
                            skills.add(skillname);
                            skillRates.add(skillrate);
                            skillDescriptions.add(skillDescrip);
                            System.out.println(skillname + skillrate+ skillDescriptions);
                        }
                        Boolean succesToAddSkills = addSkill(username, skills, skillDescriptions, skillRates, jcrSession);
                        if (succesToAddSkills) {
                            error = BLANK;
                        } else {
                            error = "2";
                        }
                        jcrUtils.repoLogout(jcrSession);
                    }
                } else {
                    error = "1";
                }
            }

            String path = SKILLKIT_HOST_PATH + SLASH + "profile.jsp" + EXCLAMATION_MARK + USERNAME_KEY
                    + EQUAL_KEY+ username;
            if (!error.equals(BLANK)){
                path = SKILLKIT_HOST_PATH + SLASH + "home.jsp" + EXCLAMATION_MARK + USERNAME_KEY
                        + EQUAL_KEY+ username + AND  + ERROR
                        + EQUAL_KEY + "1";
            }
            response.sendRedirect(path);
        }
    }

    private boolean addSkill(String username, ArrayList<String> skillnames, ArrayList<String> skilldescriptions,
                             ArrayList<String> skillrates, Session jcrSession){
        Boolean ok = false;
        if (jcrSession != null){
            try{
                StringValidations sv = new StringValidations();
                if ((skillnames.size() == skillrates.size()) && (sv.validateUsername(username)) &&
                        (skillnames.size() == skilldescriptions.size())) {
                    Node root = jcrSession.getRootNode();
                    if (root != null){
                        if (root.hasNode(SKILLKIT_USERS_PATH + username)){
                            Node userNode = root.getNode(SKILLKIT_USERS_PATH + username);
                            if (userNode != null){
                                if (userNode.hasNode(SKILLS_NODE_KEY)){
                                    Node skills = userNode.getNode(SKILLS_NODE_KEY);
                                    if (skills != null){
                                        for (int index = 0; index < skillnames.size(); index ++){
                                            String skillname = skillnames.get(index);
                                            String skillrate = skillrates.get(index);
                                            if (skillrate == null){
                                                skillrate = "0";
                                            }
                                            String skilldescription = skilldescriptions.get(index);
                                            if (!skills.hasNode(skillname)){
                                                Node skill = skills.addNode(skillname);
                                                skill.setProperty(SKILL_RATE, skillrate);
                                                skill.setProperty(SKILL_DESCRIPTION, skilldescription);
                                                skills.setProperty(EVALUATIONS, 1);
                                                jcrSession.save();
                                                ok = true;
                                            }
                                        }
                                    }
                                }else{
                                    Node skills = userNode.addNode(SKILLS_NODE_KEY);
                                    for (int index = 0; index < skillnames.size(); index ++){
                                        String skillname = skillnames.get(index);
                                        String skillrate = skillrates.get(index);
                                        String skilldescription = skilldescriptions.get(index);
                                        if (skillrate == null){
                                            skillrate = "0";
                                        }
                                        Node skill = skills.addNode(skillname);
                                        skill.setProperty(SKILL_RATE, skillrate);
                                        skill.setProperty(SKILL_DESCRIPTION, skilldescription);
                                        skills.setProperty(EVALUATIONS, 1);
                                        jcrSession.save();
                                        ok = true;
                                    }

                                }
                            }
                        }
                    }
                }
            }catch (RepositoryException e){
                e.printStackTrace();
                return false;
            }

        }
        return ok;
    }
}
