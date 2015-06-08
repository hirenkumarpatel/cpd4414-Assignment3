/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Hiren Patel
 */
public class Product extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO printer your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Product</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Product at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        
        // to indicate content type
        response.setHeader("Content-Type", "text/plain-text");
        
        try 
        {
            //getting response from user
            PrintWriter printer = response.getWriter();
            
            String query = "SELECT * FROM products;";
            
            
            if (!request.getParameterNames().hasMoreElements())
            {
                printer.println(resultMethod(query));
            } 
            else {
                int id = Integer.parseInt(request.getParameter("ProductID"));
                printer.println(resultMethod("SELECT * FROM product WHERE ProductID= ?", String.valueOf(id)));
            }

        } catch (IOException ex) {
            System.err.println("Input printer Exception: " + ex.getMessage());
        }
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
        
        
        Set<String> keyValues = request.getParameterMap().keySet();

        try 
        {
            PrintWriter printer = response.getWriter();
            if (keyValues.contains("ProductID") && keyValues.contains("name") && keyValues.contains("description")
                    && keyValues.contains("quantity")) {
                String ProductID = request.getParameter("ProductID");
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                String quantity = request.getParameter("quantity");
                doUpdate("INSERT INTO product (ProductID,name,description,quantity) VALUES (?, ?, ?, ?)", ProductID, name, description, quantity);

            } else {
                response.setStatus(500);
                printer.println("Error: Not data found for this input. Please use a URL of the form /servlet?name=XYZ&age=XYZ");
            }

        } catch (IOException ex) {
            System.err.println("Input Output Issue in doPost Method: " + ex.getMessage());
        }

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
