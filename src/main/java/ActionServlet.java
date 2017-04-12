/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dao.JpaUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import metier.modele.Activite;
import metier.service.ServiceMetier;

/**
 *
 * @author bzhong
 */
public class ActionServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action"); 
        System.out.println("Appel de ActionServlet");
        System.out.println(action);
        JpaUtil.init();
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            if(action == null)
            {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet ActionServlet</title>");            
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Servlet ActionServlet at " + request.getContextPath() + "</h1>");
                List<Activite> la = listerActivites();
                printListeActivites(out, la);
                for(Activite a : la)
                {
                    out.println(" - Activité : " + a.getDenomination()+"<br/>");
                }
                out.println("</body>");
                out.println("</html>");
            }
            else if(action.equals("listerActivites"))
            {
                List<Activite> la = listerActivites();
                printListeActivites(out, la);
                /* for(Activite a : la)
                {
                    out.println(" - Activité : " + a.getDenomination());
                }*/

            } else if(action.equals("detailActivite"))
            {
                System.out.println("Activite");
                long id = Long.parseLong(request.getParameter("idActivite"));
                Activite a = detailActivite(id);
                printActivite(out,a);
            }
            
        }
        JpaUtil.destroy(); 
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
    
    public List<Activite> listerActivites(){
        
        List<Activite> l;
        ServiceMetier s = new ServiceMetier();
        try{
             l= s.recupererActivites();
        } catch (Exception e){
            return null;
        }
        
       
       return l; 
    }
    
    public void printListeActivites(PrintWriter out, List<Activite> activites)
    {
        JsonArray jsonListe = new JsonArray();
        for(Activite a : activites)
        {
            JsonObject jsonActivite = new JsonObject();
            jsonActivite.addProperty("id", a.getId());
            jsonActivite.addProperty("denomination", a.getDenomination());
            jsonActivite.addProperty("nbParticipants", a.getNbParticipants());
            jsonActivite.addProperty("payant", a.getPayant());
            
            jsonListe.add(jsonActivite);
            
        }
        
        JsonObject container = new JsonObject();
        container.add("activites",jsonListe);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(container);
        out.println(json);
        
    }
    
    public Activite detailActivite(long id)
    {
        Activite a;
        ServiceMetier s = new ServiceMetier();
        try{
             a= s.recupererActivite(id);
        } catch (Exception e){
            return null;
        }
        
       
       return a; 
    }
    
    public void printActivite(PrintWriter out, Activite a)
    {
            JsonObject jsonActivite = new JsonObject();
            jsonActivite.addProperty("id", a.getId());
            jsonActivite.addProperty("denomination", a.getDenomination());
            jsonActivite.addProperty("nbParticipants", a.getNbParticipants());
            jsonActivite.addProperty("payant", a.getPayant());
            
            
        
        JsonObject container = new JsonObject();
        container.add("activite",jsonActivite);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(container);
        out.println(json);
    }

}
