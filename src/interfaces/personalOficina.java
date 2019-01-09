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
public class personalOficina extends javax.swing.JInternalFrame {

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
        this.setTitle("PERSONAL OFICINAS");
         tblOficinaPer.getTableHeader().setReorderingAllowed(false);
         this.setIconifiable(true);
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
                    + "where cod_ofi_per = cod_ofi";

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
         txtCedula.requestFocus();

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

     private void comprobarEmpleado(String cedula) {
        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            Statement psd = cn.createStatement();
            String sql = "SELECT * FROM personal WHERE CED_PER='" + cedula + "'";
            ResultSet rs = psd.executeQuery(sql);
            int pos = 0;
            while (rs.next()) {
                pos = pos + 1;
            }
            if (pos > 0) {         
            }else{
                JOptionPane.showMessageDialog(null, "No existe el usuario");
            }
        } catch (SQLException ex) {
            Logger.getLogger(personalOficina.class.getName()).log(Level.SEVERE, null, ex);
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
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cbxCiudad = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        cbxUbicacion = new javax.swing.JComboBox<>();
        btnSalir = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        txtCedula = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblOficinaPer = new javax.swing.JTable(){
            public boolean isCellEditable(int fila , int columna){
                return false;
            }
        };
        jLabel1 = new javax.swing.JLabel();

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tblClientes);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(51, 153, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(0, 37));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 37, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("CEDULA :");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("CIUDAD :");

        cbxCiudad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONE" }));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("UBICACIÓN :");

        cbxUbicacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONE" }));

        btnSalir.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnBorrar.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnBorrar.setText("Eliminar");
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnGuardar.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnNuevo.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        txtCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCedulaKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNuevo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbxUbicacion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbxCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCedula)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBorrar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSalir)))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cbxCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cbxUbicacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnCancelar)
                    .addComponent(btnBorrar)
                    .addComponent(btnSalir)
                    .addComponent(btnGuardar))
                .addGap(26, 26, 26))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("EMPLEADOS POR OFICINA"));

        tblOficinaPer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(tblOficinaPer);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Palatino Linotype", 1, 18)); // NOI18N
        jLabel1.setText("PERSONAL POR OFICNA");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(177, 177, 177))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addGap(10, 10, 10)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void txtCedulaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaKeyReleased
        if(txtCedula.getText().length() == 10){
          comprobarEmpleado(txtCedula.getText());
      }
    }//GEN-LAST:event_txtCedulaKeyReleased

   

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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTable tblOficinaPer;
    private javax.swing.JTextField txtCedula;
    // End of variables declaration//GEN-END:variables
}
