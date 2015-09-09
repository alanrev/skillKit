package com.skillkit.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.jackrabbit.commons.JcrUtils;

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
                    String user = request.getParameter("user");
                    String validate = user + "#"+ request.getRemoteAddr();
                    if(jcrSession != null){
                        Node root = jcrSession.getRootNode();
                        Node fs = root.getNode("fileServer");
                        if(fs.hasNode("Login")){
                            Node Login = fs.getNode("Login");
                            Property us = Login.getProperty(validate);
                            us.remove();
                            jcrSession.save();
                        }
                        repoLogout(jcrSession);
                        response.sendRedirect("http://intern5.xumak.gt:8084/TeamProject1/index.jsp");
                    }
                }catch(RepositoryException re){
                    System.err.println("Error: " + re);
                }catch(IOException ioe){
                    System.err.println("Error: " + ioe);
                }
            }else{
                System.err.println("response found as null");
            }           
        }else{
            System.err.println("request found as null");
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
            Repository repository = JcrUtils.getRepository(url);
            /**
            * Enter to jackrabbit using the credentials admin:admin,password:admin
            */
            SimpleCredentials creds = new SimpleCredentials("admin","admin".toCharArray());
            Session jcrSession = repository.login(creds, "default");
            return jcrSession;
        }catch(RepositoryException RE){
            System.out.println("fail conection");
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
