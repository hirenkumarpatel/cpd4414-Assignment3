/*
 * To change this license header, choose License HeaderesultSet in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import database.DatabaseConnection;
import static database.DatabaseConnection.connect;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONValue;

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
     * @throws ServletException if a servlet-specific error occuresultSet
     * @throws IOException if an I/O error occuresultSet
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charesultSetet=UTF-8");
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
     * @throws ServletException if a servlet-specific error occuresultSet
     * @throws IOException if an I/O error occuresultSet
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
                printer.println(getResult(query));
            } 
            else {
                int id = Integer.parseInt(request.getParameter("productId"));
                printer.println(getResult("SELECT * FROM products WHERE Product_id= ?", String.valueOf(id)));
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
     * @throws ServletException if a servlet-specific error occuresultSet
     * @throws IOException if an I/O error occuresultSet
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
                
                printer.println("No match data found for this input");
            }

        } catch (IOException ex) {
            System.err.println("IO Exception " + ex.getMessage());
        }

    }


    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Set<String> keySet = request.getParameterMap().keySet();
        try (PrintWriter printer = response.getWriter()) {
            Connection connection = connect();
            if (keySet.contains("productId")) {
                
                PreparedStatement pStatement;
                String query=""
                        + "delete FROM products WHERE Product_id=" + request.getParameter("productId");
                pStatement = connection.prepareStatement(query);
               
                try {
                    pStatement.executeUpdate();
                } catch (SQLException ex) {
                    System.out.println("SQL Exception " + ex.getMessage());
                    
                   
                }
            } else {
                printer.println("No dta found");
                
            }
        }
        catch (SQLException ex) {
            System.err.println("SQL Exception Error: " + ex.getMessage());
        } 
    }
    
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

    private String getResult(String query, String... params) {
       
        StringBuilder sb = new StringBuilder();
        String strJSON = "";
        
        try (Connection connection = DatabaseConnection.connect()) {
            
            
            PreparedStatement pStatement = connection.prepareStatement(query);
            
            for (int i = 1; i <= params.length; i++) 
            {
                pStatement.setString(i, params[i - 1]);
            }
            
            ResultSet resultSet = pStatement.executeQuery();
            
            
            
            List list1 = new LinkedList();
            
            while (resultSet.next()) {
                
                
                
                Map map = new LinkedHashMap();
                
                
                map.put("productId", resultSet.getInt("productId"));
                map.put("productName", resultSet.getString("productName"));
                map.put("productDescription", resultSet.getString("productDescription"));
                map.put("productQuantity", resultSet.getInt("productQuantity"));
                list1.add(map);

            }

            strJSON = JSONValue.toJSONString(list1);
        } 
        catch (SQLException ex)
        
        {
            System.err.println("SQL Exception Error: " + ex.getMessage());
        }
        
        return strJSON.replace("},", "},\n");
    }

    
    private int doUpdate(String query, String... params) {
        int rowAffected = 0;
        try (Connection connection = DatabaseConnection.connect())
        {
            PreparedStatement pStatement = connection.prepareStatement(query);
           
            
            for (int i = 1; i <= params.length; i++) {
                pStatement.setString(i, params[i - 1]);
            }
            rowAffected = pStatement.executeUpdate();
        } catch (SQLException ex) 
        
        {
            System.err.println("SQL Exception" + ex.getMessage());
        }
        return rowAffected;
    }

}



