package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class Conexion {

    //private Connection connect = null;
    String url = "jdbc:postgresql://localhost:5432/viajes";
    String user = "postgres";
    String pwd = "1234";
    Connection conectar = null;

    public Connection conexion() {
        try {
            Class.forName("org.postgresql.Driver");
            conectar = DriverManager.getConnection(url, user, pwd);
            //JOptionPane.showMessageDialog(null, "okk");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conectar;
    }
//    public Connection conexion() {
//        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//            Connection cn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "desarrollo", "desarrollo");
//            return cn;
//        } catch (ClassNotFoundException | SQLException | HeadlessException ex) {
//            JOptionPane.showMessageDialog(null, ex.getMessage());
//        }
//        return null;
//    }
}
