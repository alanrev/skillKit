package com.skillkit.servlets;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.skillkit.utils.StringValidations;
import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;

import static com.skillkit.utils.Constants.*;
/**
 *
 * @author xumakgt5
 */
@MultipartConfig
@WebServlet(urlPatterns = {"/createNewUser"})
public class createNewUserServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String show = BLANK;
            if ((null != response) && (request != null)){
                String firstname = request.getParameter(FIRSTNAME_KEY).toString();
                String lastname = request.getParameter(LASTNAME_KEY).toString();
                String email = request.getParameter(EMAIL_KEY).toString();
                String userName = request.getParameter(USERNAME_KEY).toString();
                String password = request.getParameter(PASSWORD_KEY).toString();
                String role = request.getParameter(ROLE_KEY).toString();
                String confPassword = request.getParameter(CONFIRM_PASSWORD_KEY);
                StringValidations stringValidations = new StringValidations();
                if (stringValidations.validateUsername(userName)) {
                    show = autentication(userName, firstname, lastname, email, role, password, confPassword);
                }else{
                    show = "4";
                }
                response.sendRedirect(SKILLKIT_HOST_PATH + SLASH + "NewUser.jsp" + EXCLAMATION_MARK + RESPONSE
                        + EQUAL_KEY + show );
        } else {
                response.sendRedirect(SKILLKIT_HOST_PATH + SLASH + "NewUser.jsp" + EXCLAMATION_MARK + RESPONSE
                        + EQUAL_KEY + show );
            System.out.println("response empty");
        }
    }

    /**
     * This method is created to verify if the fields of CreateUser.jsp are correct
     *
     * @param userName a String that is from the field user of CreateUser.jsp
     * @param pass     a String that is from the field pass of CreateUser.jsp
     * @param confPass a String that is used to confirm the password
     * @return         a String that contains the message of error or accept of the verification.
     */
    public String autentication(String userName, String firstname, String lastname,
                                String email, String role,String pass, String confPass){
        try{
            String message = "";
            Session jcrSession = repoLogin();
            if(jcrSession != null) {
                if(pass.equals(confPass)){
                    Node user = verifyName(userName, jcrSession);
                    if(user != null){
                        if (!firstname.isEmpty()) {
                            user.setProperty(FIRSTNAME_KEY, firstname);
                            jcrSession.save();
                        }
                        if (!lastname.isEmpty()) {
                            user.setProperty(LASTNAME_KEY, lastname);
                            jcrSession.save();
                        }
                        if (!email.isEmpty()){
                            user.setProperty(EMAIL_KEY, email);
                            jcrSession.save();
                        }
                        if (!role.isEmpty()){
                            user.setProperty(ROLE_KEY, role);
                            jcrSession.save();
                        }
                        user.addNode(pass);
                        message =  "0";
                    }else{
                        return "1";
                    }
                } else {
                    message = "2";
                }
                jcrSession.save();
                repoLogout(jcrSession);
                return message;
            } else {
                return "3";
            }
        } catch(RepositoryException re){
            System.out.println(re);
            return "3";
        }
    }


    /**
     * This method is used to verify that the Name don't exist at the repository and to be sure that the repository is already created.
     *
     * @param userName   a String that is used to search on the repository if don't exist/
     * @param jcrSession a Session of Jackrabbit that is used to contains de Repository.
     * @return           a Node with the userName as Name that is used to create a user.
     */
    public Node verifyName(String userName, Session jcrSession){
        try{
            if(userName != null){
                if(jcrSession != null){
                    Node root = jcrSession.getRootNode();
                    if(root.hasNode(SKILLKIT_KEY)){
                        Node skillKit = root.getNode(SKILLKIT_KEY);
                        if(skillKit.hasNode(USERS_KEY)){
                            Node users = skillKit.getNode(USERS_KEY);
                            if(!users.hasNode(userName)){
                                Node currentUser = users.addNode(userName);
                                return currentUser;
                            } else {
                                return null;
                            }
                        } else {
                            Node users = skillKit.addNode(USERS_KEY);
                            Node currentUser = users.addNode(userName);
                            return currentUser;
                        }
                    } else {
                        Node skillKit = root.addNode(SKILLKIT_KEY);
                        Node users = skillKit.addNode(USERS_KEY);
                        Node currentUser = users.addNode(userName);
                        return currentUser;
                    }
                }else {
                    return null;
                }

            } else {
                return null;
            }
        } catch(RepositoryException re){
            System.out.println("Error can't connect to jackrabbit");
            return null;
        }
    }


    /**
     * This method is used to Login the JackRabbit Repository
     *
     * @return a Session of jackrabbit
     */
    public Session repoLogin(){
        try{
            /**
             * using the url lolahost:8080 where is up the jackrabbit repository
             */
            String url = "http://intern5.xumak.gt:8080/rmi";
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

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}