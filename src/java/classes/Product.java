/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import database.Connection;
import static database.Connection.connect;
import database.connectionection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
                printer.println(resultMethod("SELECT * FROM products WHERE Product_id= ?", String.valueOf(id)));
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
            
            //checking for valid table fields
            if (keyValues.contains("productId") && keyValues.contains("productName") && keyValues.contains("productDescription")          && keyValues.contains("productQuantity")) {
                
                
                String productId = request.getParameter("productId");
                String productName = request.getParameter("productName");
                String productDescription = request.getParameter("productDescription");
                String productQuantity = request.getParameter("productQuantity");
                
                doUpdate("insert into products "
                        + "(product_id,product_name,product_description,product_quantity)"
                        + " values (?, ?, ?, ?)", productId, productName, productDescription, productQuantity);

            }
            else 
            {
                //set status to internal srerver error..
                response.setStatus(500);
                printer.println("No match data found for this input");
            }

        } catch (IOException ex) {
            System.err.println("IO Exception " + ex.getMessage());
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
    
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {

        Set<String> keySet = request.getParameterMap().keySet();
        
        
        try (PrintWriter printer = response.getWriter())
        {
            if (keySet.contains("ProductId") && keySet.contains("productName") && keySet.contains("productDescription") && keySet.contains("productQuantity")) {
                
                String ProductId = request.getParameter("ProductId");
                String productName = request.getParameter("productName");
                String productDescription = request.getParameter("productDescription");
                String productQuantity = request.getParameter("productQuantity");
                
                
                doUpdate("update products"
                        + " set Product_id = ?,"
                        + " product_name = ?, "
                        + "product_description = ?, "
                        + "product_quantity = ? "
                        + "where Product_id = ?", ProductId, productName, productDescription, productQuantity, ProductId);
            } else {
                printer.println("No matching data found..");
            }
        } catch (IOException ex) {
            System.out.println("IO exception" + ex.getMessage());
            //setting status to internal server error
            response.setStatus(500);
            
        }
    }

    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Set<String> keySet = request.getParameterMap().keySet();
        try (PrintWriter printer = response.getWriter()) {
            Connection connection = connect();
            if (keySet.contains("productId")) {
                
                PreparedStatement pStatement;
                String query="DELETE FROM `product` WHERE `ProductID`=" + request.getParameter("ProductID");
                pStatement = connection.prepareStatement(query);
                try {
                    pStatement.executeUpdate();
                } catch (SQLException ex) {
                    System.err.println("SQL Exception " + ex.getMessage());
                    
                   
                }
            } else {
                out.println("Error: Not enough data in table to delete");
                
            }
        } catch (SQLException ex) {
            System.err.println("SQL Exception Error: " + ex.getMessage());
        } catch (SQLException ex) {
            Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
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

    /**
     * resultMethod accepts two arguments It executes the Query get ProductID,
     * name, description, quantity
     *
     * @param query
     * @param params
     * @throws SQLException
     * @return
     */
    private String resultMethod(String query, String... params) {
        StringBuilder sb = new StringBuilder();
        String jsonString = "";
        try (Connection connection = Credentials.getConnection()) {
            PreparedStatement pStatement = connection.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                pStatement.setString(i, params[i - 1]);
            }
            ResultSet rs = pStatement.executeQuery();
            List l1 = new LinkedList();
            while (rs.next()) {
                //Refernce Example 5-2 - Combination of JSON primitives, Map and List
                //https://code.google.com/p/json-simple/wiki/EncodingExamples
                Map m1 = new LinkedHashMap();
                m1.put("ProductID", rs.getInt("ProductID"));
                m1.put("name", rs.getString("name"));
                m1.put("description", rs.getString("description"));
                m1.put("quantity", rs.getInt("quantity"));
                l1.add(m1);

            }

            jsonString = JSONValue.toJSONString(l1);
        } catch (SQLException ex) {
            System.err.println("SQL Exception Error: " + ex.getMessage());
        }
        return jsonString.replace("},", "},\n");
    }

    /**
     * doUpdate Method accepts two arguments Update the entries in the table
     * 'product'
     *
     * @param query
     * @param params
     * @return numChanges
     */
    private int doUpdate(String query, String... params) {
        int numChanges = 0;
        try (Connection connection = Credentials.getConnection()) {
            PreparedStatement pStatement = connection.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                pStatement.setString(i, params[i - 1]);
            }
            numChanges = pStatement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("SQL EXception in doUpdate Method" + ex.getMessage());
        }
        return numChanges;
    }

}


}
