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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.skillkit.utils.Constants.*;

/**
 * Created by Allan on 12/11/2015.
 */
@WebServlet("/ProjectTasksStatus")
public class ProjectTasksStatusServlet extends HttpServlet{

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
            String ip = request.getRemoteAddr();
            JCRUtils jcrUtils = new JCRUtils();
            Session jcrSession = jcrUtils.repoLogin();
            Map<String, Object> tasksStatus = new HashMap();
            if (jcrSession != null) {
                if (stringValidations.validateUsername(username)) {
                    Node userNode = jcrUtils.authenticate(username, ip);
                    if (userNode != null) {
                        tasksStatus = getTasksStatus(jcrSession, project);
                        String json = new Gson().toJson(tasksStatus);
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json");
                        response.getWriter().write(json);
                    }
                }
            }
        }
    }

    private Map<String, Object> getTasksStatus(Session jcrSession, String projectname){
        Map<String, Object> taskStatus = new HashMap<>();
        JCRUtils jcrUtils = new JCRUtils();
        if (jcrSession != null){
            try {
                if ((projectname != null) && (!projectname.isEmpty())) {
                    Node root = jcrSession.getRootNode();
                    if (root.hasNode(SKILLKIT_PROJECTS_PATH + projectname)){
                        Node project = root.getNode(SKILLKIT_PROJECTS_PATH + projectname);
                        if (project.hasNode(TASKS_KEY)){
                            Node tasks = project.getNode(TASKS_KEY);
                            List<Map<String, Object>> tasksList = jcrUtils.getNodeListProperties(tasks);
                            if (!tasksList.isEmpty()){
                                Map<String, Object> titleMap = new HashMap<>();
                                Map<String, Object> legendMap = new HashMap<>();
                                Map<String, Object> dataMap = new HashMap<>();
                                List<Map<String, Object>> dataPoints = getDataList(tasksList);
                                List<Map<String, Object>> dataList = new ArrayList<>();
                                titleMap.put(TEXT, projectname + "'s Task Status");
                                legendMap.put(MAX_WIDTH, 350);
                                legendMap.put(ITEM_WIDTH, 120);
                                dataMap.put(TYPE, PIE);
                                dataMap.put(SHOW_LEGEND, true);
                                dataMap.put(LEGEND_TEXT, INDEX_WITH_BRACKETS);
                                dataMap.put(DATA_POINTS, dataPoints);
                                dataList.add(dataMap);
                                taskStatus.put(TITLE, titleMap);
                                taskStatus.put(LEGEND, legendMap);
                                taskStatus.put(DATA, dataList);
                            }

                        }
                    }
                }
            }catch (RepositoryException e){
                e.printStackTrace();
            }
        }
        return taskStatus;
    }

    private List<Map<String, Object>> getDataList(List<Map<String, Object>> tasks){
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Object> created = new HashMap<>();
        Map<String, Object> inProgress = new HashMap<>();
        Map<String, Object> finished = new HashMap<>();
        Map<String, Object> close = new HashMap<>();
        int createdCounter = 0;
        int ipCounter = 0;
        int finishedCounter = 0;
        int closeCounter = 0;
        for(Map<String, Object> task : tasks){
            if (task.containsKey(STATUS_KEY)){
                String taskStatus = (String) task.get(STATUS_KEY);
                if (taskStatus.equals(STATUS_CREATED)){
                    createdCounter++;
                }
                if (taskStatus.equals(STATUS_IN_PROGRESS)){
                    ipCounter++;
                }
                if (taskStatus.equals(STATUS_FINISHED)){
                    finishedCounter++;
                }
                if (taskStatus.equals(STATUS_CLOSE)){
                    closeCounter++;
                }
            }
        }
        created.put(Y_VALUE, createdCounter);
        created.put(INDEX_LABEL, STATUS_CREATED);
        inProgress.put(Y_VALUE, ipCounter);
        inProgress.put(INDEX_LABEL, STATUS_IN_PROGRESS);
        finished.put(Y_VALUE, finishedCounter);
        finished.put(INDEX_LABEL, STATUS_FINISHED);
        close.put(Y_VALUE, closeCounter);
        close.put(INDEX_LABEL, STATUS_CLOSE);
        dataList.add(created);
        dataList.add(inProgress);
        dataList.add(finished);
        dataList.add(close);
        return dataList;
    }
}
