package com.skillkit.servlets;

import java.io.IOException;
import java.net.MalformedURLException;
import javax.jcr.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;

import static com.skillkit.utils.Constants.*;
/**
 *
 * @author xumakgt1
 */
@WebServlet(urlPatterns = {"/LogoutServlet"})
public class LogoutServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response){
        if(request != null){
            if(response != null){
                try{
                    Session jcrSession = repoLogin();
                    String user = BLANK;
                    Cookie[] cookies = request.getCookies();
                    if (cookies != null) {
                        for (Cookie cookie : cookies) {
                            if (cookie.getName().equals(USERNAME_KEY)) {
                                user = cookie.getValue();
                            }
                        }
                    }
                    String validate = user + DASH + request.getRemoteAddr();
                    if(jcrSession != null){
                        Node root = jcrSession.getRootNode();
                        Node sessions = root.getNode(SKILLKIT_SESSIONS_PATH);
                        if(sessions.hasNode(validate)){
                            Node sessionNode = sessions.getNode(validate);
                            sessionNode.remove();
                            jcrSession.save();
                        }
                        repoLogout(jcrSession);
                        Cookie cookie = new Cookie(USERNAME_KEY, null);
                        cookie.setPath(SKILLKIT_HOST_PATH + "index.jsp");
                        cookie.setHttpOnly(true);
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                        response.sendRedirect(SKILLKIT_HOST_PATH + SLASH + "index.jsp");
                    }else {
                    }
                }catch(RepositoryException re){
                    System.out.println("Error: " + re);
                }catch(IOException ioe){
                    System.out.println("Error: " + ioe);
                }
            }else{
                System.out.println("response found as null");
            }           
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
