package Conexion;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Conexion {

    private Connection connect = null;

//    public Connection conectar() {
//        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//
//            connect = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "vijes", "viajes");
////          conec = DriverManager.getConnection("jdbc:oracle:thin:@10.5.0.31:1521:XE", "RECARGAS", "RECARGAS");
//            connect = DriverManager.getConnection("jdbc:oracle:thin:@10.5.1.233:1521:XE", "vijes", "viajes");
//            JOptionPane.showMessageDialog(null, "Conexion Exitosa");
//        } catch (Exception e) {
//            //JOptionPane.showMessageDialog(null, "Error de conexion a la base");
//        }
//        return connect;
//    }
    public Connection conexion() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection cn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "vijes", "viajes");
            return cn;
        } catch (ClassNotFoundException | SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        return null;
    }
}

//jdbc:oracle:thin:@localhost:1521:XE
