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
        try (PrintWriter out = response.getWriter()) {
            if (keySet.contains("ProductID") && keySet.contains("name") && keySet.contains("description") && keySet.contains("quantity")) {
                String ProductID = request.getParameter("ProductID");
                String name = request.getParameter("name");
                String description = request.getParameter("description");
                String quantity = request.getParameter("quantity");
                doUpdate("update product set ProductID = ?, name = ?, description = ?, quantity = ? where ProductID = ?", ProductID, name, description, quantity, ProductID);
            } else {
                out.println("Error: Not data found for this input. Please use a URL of the form /products?id=xx&name=XXX&description=XXX&quantity=xx");
            }
        } catch (IOException ex) {
            response.setStatus(500);
            System.out.println("Error in writing output: " + ex.getMessage());
        }
    }

    /**
     * doDelete Method took two Arguments and This method Will delete Entries
     * from product Table This will also catch SQLException and Display an error
     * message
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Set<String> keySet = request.getParameterMap().keySet();
        try (PrintWriter out = response.getWriter()) {
            Connection conn = getConnection();
            if (keySet.contains("ProductID")) {
                PreparedStatement pstmt = conn.prepareStatement("DELETE FROM `product` WHERE `ProductID`=" + request.getParameter("ProductID"));
                try {
                    pstmt.executeUpdate();
                } catch (SQLException ex) {
                    System.err.println("SQL Exception Error in Update prepared Statement: " + ex.getMessage());
                    out.println("Error in deleting entry.");
                   
                }
            } else {
                out.println("Error: Not enough data in table to delete");
                
            }
        } catch (SQLException ex) {
            System.err.println("SQL Exception Error: " + ex.getMessage());
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
        try (Connection conn = Credentials.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }
            ResultSet rs = pstmt.executeQuery();
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
        try (Connection conn = Credentials.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }
            numChanges = pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("SQL EXception in doUpdate Method" + ex.getMessage());
        }
        return numChanges;
    }

}


}
