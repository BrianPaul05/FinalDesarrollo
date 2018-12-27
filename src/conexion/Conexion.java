package Conexion;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Conexion {

    private Connection connect = null;

    public Connection conexion() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection cn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "desarrollo", "desarrollo");
            return cn;
        } catch (ClassNotFoundException | SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return null;
    }
}
