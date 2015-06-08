/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Hiren Patel
 */
public class connection {
    
    public static Connection connect() throws SQLException {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            String jdbc = "jdbc:mysql://localhost/java2_c0651609";
            String user = "root";
            String password = "";
            connection = DriverManager.getConnection(jdbc, user, password);
            String query = "SELECT * FROM products";

        } catch (ClassNotFoundException ex ) {
            System.err.println(" classnot  found exception" + ex.getMessage());
        }
        catch(SQLException ex)
        {
            System.err.println("SQL Exception" + ex.getMessage());
        }
        return connection;
    }
    
}
