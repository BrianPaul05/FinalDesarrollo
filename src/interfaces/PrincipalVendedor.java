/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import Conexion.Conexion;
import Conexion.ImagenFondo;
import static interfaces.PrimeraInterface.principal;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.omg.CORBA.portable.InputStream;

/**
 *
 * @author Ronni
 */
public class PrincipalVendedor extends javax.swing.JFrame {

    /**
     * Creates new form PrimeraInterface
     */
    String codEncomiendaOficina;

    public PrincipalVendedor(String codOficina, String[] nombre) {
        codEncomiendaOficina = codOficina;
        initComponents();
        txtUsuaio.setText(nombre[0] + " " + nombre[1]);

        this.setTitle("SKY WAY");
        setIconImage(new ImageIcon(getClass().getResource("/Imagenes/sk.png")).getImage());
        principal.setBorder(new ImagenFondo());
        this.setExtendedState(PrincipalVendedor.MAXIMIZED_BOTH);
        principal.setBorder(new ImagenFondo());
        this.setExtendedState(PrincipalVendedor.MAXIMIZED_BOTH);
        setExtendedState(MAXIMIZED_BOTH);
    }

    private void salir() {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int op = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea salir?", "Advertencia", dialogButton);
        if (op == 0) {
            this.dispose();
        }
    }

    private void cerrarSesion() {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int op = JOptionPane.showConfirmDialog(null, "¿Desea cerrar sesión actual?", "Advertencia", dialogButton);
        if (op == 0) {
            Login l = new Login();
            l.setVisible(true);
            this.dispose();
        }
    }

    public static boolean ventanasAbiertas(Object ventana) {
        JInternalFrame[] activos = principal.getAllFrames();
        int i = 0;
        boolean cerrada = true;
        while (i < activos.length && cerrada) {
            if (activos[i] == ventana) {
                return false;
            } else {
                activos[i].dispose();
            }
            i++;
        }
        return cerrada;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        principal = new javax.swing.JDesktopPane();
        jPanel1 = new javax.swing.JPanel();
        txtUsuaio = new javax.swing.JLabel();
        fac1 = new javax.swing.JButton();
        menuFacturacion1 = new javax.swing.JButton();
        usuario = new javax.swing.JPanel();
        fac3 = new javax.swing.JButton();
        fac4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
        );

        principal.setDragMode(javax.swing.JDesktopPane.OUTLINE_DRAG_MODE);
        principal.setPreferredSize(new java.awt.Dimension(771, 529));

        jPanel1.setBackground(new java.awt.Color(0, 153, 255));

        txtUsuaio.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtUsuaio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/account-circle.png"))); // NOI18N

        fac1.setBackground(new java.awt.Color(0, 0, 0));
        fac1.setForeground(new java.awt.Color(255, 255, 255));
        fac1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/viajes.png"))); // NOI18N
        fac1.setText("Viajes");
        fac1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        fac1.setFocusable(false);
        fac1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                fac1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                fac1MouseExited(evt);
            }
        });
        fac1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fac1ActionPerformed(evt);
            }
        });

        menuFacturacion1.setBackground(new java.awt.Color(0, 153, 255));
        menuFacturacion1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        menuFacturacion1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/arrow-down-drop-circle-outline.png"))); // NOI18N
        menuFacturacion1.setToolTipText("");
        menuFacturacion1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        menuFacturacion1.setFocusable(false);
        menuFacturacion1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuFacturacion1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuFacturacion1MouseExited(evt);
            }
        });
        menuFacturacion1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuFacturacion1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(fac1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 420, Short.MAX_VALUE)
                .addComponent(txtUsuaio, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(menuFacturacion1)
                .addGap(22, 22, 22))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtUsuaio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(fac1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(menuFacturacion1))
        );

        usuario.setBackground(new java.awt.Color(0, 153, 255));
        usuario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        fac3.setBackground(new java.awt.Color(0, 153, 255));
        fac3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/account-edit.png"))); // NOI18N
        fac3.setText("Configuraciónes");
        fac3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        fac3.setFocusable(false);
        fac3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fac3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                fac3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                fac3MouseExited(evt);
            }
        });
        usuario.add(fac3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 130, 30));

        fac4.setBackground(new java.awt.Color(0, 153, 255));
        fac4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/salir.png"))); // NOI18N
        fac4.setText("Cerrar Sesión");
        fac4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        fac4.setFocusable(false);
        fac4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fac4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                fac4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                fac4MouseExited(evt);
            }
        });
        usuario.add(fac4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 130, 30));

        principal.setLayer(jPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        principal.setLayer(usuario, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout principalLayout = new javax.swing.GroupLayout(principal);
        principal.setLayout(principalLayout);
        principalLayout.setHorizontalGroup(
            principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, principalLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(usuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        principalLayout.setVerticalGroup(
            principalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(principalLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(usuario, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(500, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(principal, javax.swing.GroupLayout.DEFAULT_SIZE, 1314, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(principal, javax.swing.GroupLayout.DEFAULT_SIZE, 689, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fac1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fac1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_fac1MouseEntered

    private void fac1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fac1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_fac1MouseExited

    private String cedulaEmpleado(String codOficina) {
        String cedula = "";
        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            String sql = "SELECT CED_PERSONAL_PER "
                    + "FROM PERSONAL_OFICINA "
                    + "WHERE COD_OFI_PER = '" + codOficina + "'";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);

            while (rs.next()) {
                cedula = rs.getString("CED_PERSONAL_PER");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }

        return cedula;
    }
 IngresoViajesbus iv;
    private void fac1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fac1ActionPerformed
        // TODO add your handling code here:
        String ced = cedulaEmpleado(codEncomiendaOficina);
       
        if (ventanasAbiertas(iv)) {
            iv = new IngresoViajesbus(Login.usuario.getText());
            principal.add(iv);
            Dimension desktopSize = principal.getSize();
            Dimension internalSize = iv.getSize();
            iv.setLocation((desktopSize.width - internalSize.width) / 2, (desktopSize.height - internalSize.height) / 2);
            iv.setVisible(true);
        }
    }//GEN-LAST:event_fac1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        salir();
    }//GEN-LAST:event_formWindowClosing

    private void menuFacturacion1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuFacturacion1MouseEntered
        // TODO add your handling code here:
        usuario.setSize(130, 60);
    }//GEN-LAST:event_menuFacturacion1MouseEntered

    private void menuFacturacion1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuFacturacion1MouseExited
        // TODO add your handling code here:
        usuario.setSize(0, 0);
    }//GEN-LAST:event_menuFacturacion1MouseExited

    private void fac3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fac3MouseEntered
        // TODO add your handling code here:
        usuario.setSize(130, 60);
    }//GEN-LAST:event_fac3MouseEntered

    private void fac3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fac3MouseExited
        // TODO add your handling code here:
        usuario.setSize(0, 0);
    }//GEN-LAST:event_fac3MouseExited

    private void fac4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fac4MouseClicked
        // TODO add your handling code here:
        cerrarSesion();
    }//GEN-LAST:event_fac4MouseClicked

    private void fac4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fac4MouseEntered
        // TODO add your handling code here:
        usuario.setSize(130, 60);
    }//GEN-LAST:event_fac4MouseEntered

    private void fac4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fac4MouseExited
        // TODO add your handling code here:
        usuario.setSize(0, 0);
    }//GEN-LAST:event_fac4MouseExited

    private void fac3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fac3MouseClicked
        // TODO add your handling code here:
        cerrarSesion();
    }//GEN-LAST:event_fac3MouseClicked

    private void menuFacturacion1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuFacturacion1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_menuFacturacion1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PrincipalVendedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PrincipalVendedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PrincipalVendedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrincipalVendedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JButton fac1;
    public static javax.swing.JButton fac3;
    public static javax.swing.JButton fac4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    public static javax.swing.JButton menuFacturacion1;
    public static javax.swing.JDesktopPane principal;
    private javax.swing.JLabel txtUsuaio;
    public static javax.swing.JPanel usuario;
    // End of variables declaration//GEN-END:variables
}
