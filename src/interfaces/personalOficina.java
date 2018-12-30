/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import Conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Roberto Altamirano
 */
public class personalOficina extends javax.swing.JFrame {

    /**
     * Creates new form personalOficina
     */
    public personalOficina() {
        initComponents();
        cargarComboCiudadOficina();
        cargarTablaPerOfi();
        cargarModificar();
        desactivarBotones();
        desactivarCampos();
    }

    public void cargarModificar() {

        tblOficinaPer.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (tblOficinaPer.getSelectedRow() != -1) {  /// si no esta seleccioando  nada  en la  tabla
                    //  activarCampos();
                    //  botonActualizar();
                    int fila = tblOficinaPer.getSelectedRow();//  tomar la  posicion
                    txtCedula.setText(tblOficinaPer.getValueAt(fila, 1).toString().trim());
                    cbxCiudad.setSelectedItem(tblOficinaPer.getValueAt(fila, 2).toString().trim());

                }
                //  activarCampos();
            }
        });
    }

    DefaultTableModel modelo;

    public void cargarTablaPerOfi() {

        try {
            String titulos[] = {"CÓDIGO", "CÉDULA", "CIUDAD"};
            String registro[] = new String[3];
            modelo = new DefaultTableModel(null, titulos);
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            //String sql = "SELECT * FRM CLIENTES  WHERE EST_CLI='S' and CED_CLI LIKE'%" + dato + "%'";
            String sql = "SELECT COD_PER_OFI,ced_personal_per,ubicacion\n"
                    + "from personal_oficina , oficinas \n"
                    + "where cod_ofi_per = cod_ofi;";

            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            //  System.out.println(rs);
            while (rs.next()) {
                registro[0] = String.valueOf(rs.getInt("COD_PER_OFI"));
                registro[1] = rs.getString("ced_personal_per");
                registro[2] = rs.getString("ubicacion");
                modelo.addRow(registro);
            }

            tblOficinaPer.setModel(modelo);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }

    }

    public void cargarComboCiudadOficina() {

        try {
            String personal;
            String sql = "";
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            sql = "select UBICACION from oficinas ";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);

            while (rs.next()) {
                //codmar.add(rs.getString("MAR_COD"));
                personal = rs.getString("UBICACION");
                cbxCiudad.addItem(personal);
            }
            cn.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }
//    public void cargarComboUbicacionOficina() {
//
//        try {
//            String personal;
//            String sql = "";
//            Conexion cc = new Conexion();
//            Connection cn = cc.conexion();
//            sql = "select  DIRECCION from oficinas ";
//            Statement psd = cn.createStatement();
//            ResultSet rs = psd.executeQuery(sql);
//
//            while (rs.next()) {
//                //codmar.add(rs.getString("MAR_COD"));
//                personal = rs.getString("DIRECCION");
//                cbxUbicacion.addItem(personal);
//            }
//            cn.close();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, ex);
//        }
//                    }

    public void borrar() throws SQLException {

        if (JOptionPane.showConfirmDialog(new JInternalFrame(), "Estas seguro de borar el  registro", "Ventana Borrar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            String sql = "DELETE FROM PERSONAL_OFICINA WHERE CED_PERSONAL_PER ='" + txtCedula.getText() + "'";
            PreparedStatement psd = cn.prepareStatement(sql);
            int n = psd.executeUpdate();
            if (n > 0) {
                JOptionPane.showMessageDialog(this, "Borrado  exitosamente");
                cargarTablaPerOfi();
                limpiarCampos();
            }

        }
    }

    public String idOficina() throws ClassNotFoundException {
        String id = " ";
        try {

            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            String sql = "";
            sql = "SELECT COD_OFI from oficinas where ubicacion= '" + cbxCiudad.getSelectedItem() + "'";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                id = rs.getString("COD_OFI");
            }
            cn.close();
        } catch (SQLException ex) {
            Logger.getLogger(IngresoEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
        //  System.out.println(id);
        return id;

    }

    public void guardarPerOficina() throws ClassNotFoundException {

        if (txtCedula.getText().length() < 10) {
            JOptionPane.showMessageDialog(null, "Cédula Incorrecta");
            txtCedula.requestFocus();
        } else if (cbxCiudad.getSelectedItem().equals("SELECCIONE")) {
            JOptionPane.showMessageDialog(null, "Escoja la Ciudad");
        } else {
            try {

                String sql = "";
                String cedula, idOfi;
                cedula = txtCedula.getText();
                idOfi = idOficina();
                Conexion cc = new Conexion();
                Connection cn = cc.conexion();
                sql = "insert into personal_oficina(CED_PERSONAL_PER,COD_OFI_PER) values('" + cedula + "','" + idOfi + "')";
                PreparedStatement psd = cn.prepareStatement(sql);
                int n = psd.executeUpdate();// ncuantas  filas se inserto
                if (n > 0) {
                    JOptionPane.showMessageDialog(this, "Guardado Correctamente");
                }
                 cargarTablaPerOfi();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    public void limpiarCampos() {
        txtCedula.setText("");
        cbxCiudad.setSelectedIndex(0);
    }

    public void activarBotones() {
        btnNuevo.setEnabled(true);
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnBorrar.setEnabled(true);
        btnSalir.setEnabled(true);

    }

    public void desactivarBotones() {
        btnNuevo.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(true);
        btnBorrar.setEnabled(false);
        btnSalir.setEnabled(true);

    }

    public void activarCampos() {
        txtCedula.setEnabled(true);
        cbxCiudad.setEnabled(true);
    }

    public void desactivarCampos() {
        txtCedula.setEnabled(false);
        cbxCiudad.setEnabled(false);
    }

    public void soloNumeros(java.awt.event.KeyEvent evt) {
        char c;
        c = evt.getKeyChar();
        if ((c >= 32 && c <= 47) || (c >= 58 && c <= 255)) {
            evt.consume();
            JOptionPane.showMessageDialog(this, "ERROR Ingrese solo Números");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable(){
            public boolean isCellEditable(int fila , int columna){
                return false;
            }
        };
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtCedula = new javax.swing.JFormattedTextField();
        cbxUbicacion = new javax.swing.JComboBox<>();
        cbxCiudad = new javax.swing.JComboBox<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblOficinaPer = new javax.swing.JTable(){
            public boolean isCellEditable(int fila , int columna){
                return false;
            }
        };
        jPanel2 = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tblClientes);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(51, 153, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(0, 37));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 722, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 37, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("PERSONAL OFICINA");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("CEDULA :");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("CIUDAD :");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("UBICACIÓN :");

        try {
            txtCedula.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##########")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCedulaKeyTyped(evt);
            }
        });

        cbxUbicacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONE" }));

        cbxCiudad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONE" }));

        tblOficinaPer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(tblOficinaPer);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnNuevo.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnGuardar.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnBorrar.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnBorrar.setText("Borrar");
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });

        btnSalir.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnSalir.setText("Salir");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(btnNuevo)
                .addGap(42, 42, 42)
                .addComponent(btnGuardar)
                .addGap(28, 28, 28)
                .addComponent(btnCancelar)
                .addGap(34, 34, 34)
                .addComponent(btnBorrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addComponent(btnSalir)
                .addGap(34, 34, 34))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnCancelar)
                    .addComponent(btnBorrar)
                    .addComponent(btnSalir)
                    .addComponent(btnGuardar))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(34, 34, 34)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtCedula, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                                    .addComponent(cbxUbicacion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbxCiudad, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(254, 254, 254)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbxCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbxUbicacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
       limpiarCampos();
        activarCampos();
        activarBotones();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try {
            guardarPerOficina();
             limpiarCampos();
            desactivarCampos();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(personalOficina.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        try {
            borrar();
        } catch (SQLException ex) {
            Logger.getLogger(personalOficina.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
            limpiarCampos();
        desactivarBotones();
       desactivarCampos();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtCedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaKeyTyped
        soloNumeros(evt);
    }//GEN-LAST:event_txtCedulaKeyTyped

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
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(personalOficina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(personalOficina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(personalOficina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(personalOficina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new personalOficina().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cbxCiudad;
    private javax.swing.JComboBox<String> cbxUbicacion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTable tblOficinaPer;
    private javax.swing.JFormattedTextField txtCedula;
    // End of variables declaration//GEN-END:variables
}
