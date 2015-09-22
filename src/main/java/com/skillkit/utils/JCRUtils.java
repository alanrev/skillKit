package com.skillkit.utils;

import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;

import javax.jcr.*;
import java.net.MalformedURLException;
import java.util.*;

import static com.skillkit.utils.Constants.*;
import static com.skillkit.utils.Constants.SKILLKIT_USERS_PATH;

/**
 * Created by Allan on 16/09/2015.
 */
public class JCRUtils {

    public Boolean checkSession(String username, String ip, Session jcrSession){
        if ((!(username.isEmpty())) && (!(username.equals(EMPTY_SPACE)))
                && (!(ip.equals(EMPTY_SPACE))) &&(!(ip.isEmpty()))){
            if (jcrSession != null){
                try {
                    Node rootNode = jcrSession.getRootNode();
                    if (rootNode.hasNode(SKILLKIT_SESSIONS_PATH)){
                        Node sessionsNode = rootNode.getNode(SKILLKIT_SESSIONS_PATH);
                        if (sessionsNode != null) {
                            if (sessionsNode.hasNode(username + DASH + ip)) {
                                return true;
                            }
                        }
                    }
                    jcrSession.save();
                } catch (RepositoryException e) {
                    e.printStackTrace();
                    System.out.print("error en el repo");
                    return false;
                }
            }
        }
        return false;
    }


    public Node authenticate(String userName, String ip){
        try{
            Session jcrSession = repoLogin();
            if(userName != null){
                if(jcrSession != null) {
                    Node root = jcrSession.getRootNode();
                    if (root.hasNode(SKILLKIT_USERS_PATH + userName)) {
                        Node userNode = root.getNode(SKILLKIT_USERS_PATH + userName);
                        if (checkSession(userName, ip, jcrSession)){
                            return userNode;
                        }else{
                            return null;
                        }

                    }
                    jcrSession.save();
                    repoLogout(jcrSession);
                }
            }
            return null;
        } catch(RepositoryException re){
            System.out.println("Error can't connect to jackrabbit");
            return null;
        }
    }
    public Session repoLogin(){
        try{
            /**
             * using the url lolahost:8080 where is up the jackrabbit repository
             */

            Repository repository =
                    new URLRemoteRepository("http://localhost:8080/rmi");
            /**
             * Enter to jackrabbit using the credentials admin:admin,password:admin
             */
            SimpleCredentials creds = new SimpleCredentials("admin","admin".toCharArray());
            Session jcrSession = repository.login(creds);
            return jcrSession;
        }catch(RepositoryException RE){
            System.out.println(RE);
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * This method is used to exit the session that is used in the program.
     *
     * @param jcrSession receive the session that is going to be closed.
     */
    public void repoLogout(Session jcrSession){
        if(jcrSession != null){
            jcrSession.logout();
        } else {
            System.out.println("Session can't be closed");
        }
    }

    public List<Map<String, Object>> getNodeListProperties(Node rootNode){
        List<Map<String, Object>> nodesPropertiesList = new ArrayList<>();
        try {
            if (rootNode != null) {
                if (rootNode.hasNodes()) {
                    Iterator<Node> nodeIterator = rootNode.getNodes();
                    while(nodeIterator.hasNext()){
                        Node node = nodeIterator.next();
                        if (node.hasProperties()) {
                            Map<String, Object>  nodePropertiesMap = new HashMap<>();
                            Iterator<Property> propertyIterator = node.getProperties();
                            nodePropertiesMap.put(NAME, node.getName());
                            while(propertyIterator.hasNext()){
                                Property property = propertyIterator.next();
                                String propertyName = property.getName();
                                if(!(property.isMultiple())){
                                    Value value = property.getValue();
                                    String valueString = value.getString();
                                    nodePropertiesMap.put(propertyName, valueString);
                                }else{
                                    Value[] values = property.getValues();
                                    ArrayList<String> listOfValues = new ArrayList<>();
                                    for(Value value : values){
                                        listOfValues.add(value.getString());
                                    }
                                    nodePropertiesMap.put(propertyName, listOfValues);
                                }
                            }
                            nodesPropertiesList.add(nodePropertiesMap);
                        }
                    }
                }
            }
        } catch (RepositoryException re){
            re.printStackTrace();
            return nodesPropertiesList;
        }
        return nodesPropertiesList;
    }
}
