/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;


/**
 *
 * @author DELL GAMER
 */
public class IngresoViajesbus extends javax.swing.JInternalFrame {

    /**
     * Creates new form IngresoViajes
     */
    public IngresoViajesbus() {
        initComponents();
//       setLocationRelativeTo(this);
//        cargarComboCiudadesOrigen();
//        cargarComboCiudadesDestino();
//        cargarComboBus();
      this.setIconifiable(true);
    }

//    public void cargarComboCiudadesOrigen() {
//        try {
////            conexion cc = new conexion();
////            Connection cn = cc.conectar();
//
//            String sql = "SELECT NOM_OFI FROM OFICINAS";
//            //Statement psd = cn.createStatement();
//            ResultSet rs = psd.executeQuery(sql);
//            while (rs.next()) {
//                jcbxOrigen.addItem(rs.getString("NOM_OFI"));
//            }
//
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(this, ex);
//        }
//    }

//    public void cargarComboCiudadesDestino() {
//        try {
//            conexion cc = new conexion();
//            Connection cn = cc.conectar();
//
//            String sql = "SELECT NOM_OFI FROM OFICINAS";
//            Statement psd = cn.createStatement();
//            ResultSet rs = psd.executeQuery(sql);
//            while (rs.next()) {
//                jcbxDestino.addItem(rs.getString("NOM_OFI"));
//            }
//
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(this, ex);
//        }
//    }

//    public void cargarComboFrecuencia() {
//        try {
//            conexion cc = new conexion();
//            Connection cn = cc.conectar();
//
//            String sql = "SELECT HORA_SALIDA "
//                    + "FROM FRECUENCIAS "
//                    + "WHERE COD_RUTA_PER = (SELECT COD_RUTA "
//                    + "                         FROM RUTAS"
//                    + "                         WHERE COD_OFI_ORI = (SELECT COD_OFI "
//                    + "                                                 FROM OFICINAS "
//                    + "                                                 WHERE NOM_OFI = '" + String.valueOf(jcbxOrigen.getSelectedItem()) + "')"
//                    + "                         AND COD_OFI_DES = ( SELECT COD_OFI "
//                    + "                                                 FROM OFICINAS "
//                    + "                                                 WHERE NOM_OFI = '" + String.valueOf(jcbxDestino.getSelectedItem()) + "')) ";
//            Statement psd = cn.createStatement();
//            ResultSet rs = psd.executeQuery(sql);
//            while (rs.next()) {
//                jcbxHora_Salida.addItem(rs.getString("HORA_SALIDA"));
//            }
//
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(this, ex);
//        }
//    }

//    public String obtenerCodigoFrecuencia(String hor_sal) {
//        String codigo = "";
//        try {
//            conexion cc = new conexion();
//            Connection cn = cc.conectar();
//
//            String sql = "SELECT COD_FRE "
//                    + "FROM FRECUENCIAS "
//                    + "WHERE COD_RUTA_PER = (SELECT COD_RUTA "
//                    + "                         FROM RUTAS"
//                    + "                         WHERE COD_OFI_ORI = (SELECT COD_OFI "
//                    + "                                                 FROM OFICINAS "
//                    + "                                                 WHERE NOM_OFI = '" + String.valueOf(jcbxOrigen.getSelectedItem()) + "')"
//                    + "                         AND COD_OFI_DES = ( SELECT COD_OFI "
//                    + "                                                 FROM OFICINAS "
//                    + "                                                 WHERE NOM_OFI = '" + String.valueOf(jcbxDestino.getSelectedItem()) + "') "
//                    + "AND HORA_SALIDA = '" + hor_sal + "') ";
//            Statement psd = cn.createStatement();
//            ResultSet rs = psd.executeQuery(sql);
//
//            while (rs.next()) {
//                codigo = (rs.getString("COD_FRE"));
//            }
//
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(this, ex);
//        }
//
//        return codigo;
//    }
//
//    public void cargarComboBus() {
//        try {
//            conexion cc = new conexion();
//            Connection cn = cc.conectar();
//
//            String sql = "SELECT PLACA FROM BUS";
//            Statement psd = cn.createStatement();
//            ResultSet rs = psd.executeQuery(sql);
//            while (rs.next()) {
//                jcbxBus.addItem(rs.getString("PLACA"));
//            }
//
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(this, ex);
//        }
//    }
//
//    public String obtenerCodigoBus(String placa) {
//        String codigo = "";
//        try {
//            conexion cc = new conexion();
//            Connection cn = cc.conectar();
//
//            String sql = "SELECT COD_BUS "
//                    + "FROM BUS "
//                    + "WHERE PLACA = '" + placa + "'";
//            Statement psd = cn.createStatement();
//            ResultSet rs = psd.executeQuery(sql);
//
//            while (rs.next()) {
//                codigo = (rs.getString("COD_BUS"));
//            }
//
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(this, ex);
//        }
//
//        return codigo;
//    }

//
//    private java.sql.Date convertUtilToSql(java.util.Date uDate) {
//
//        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
//
//        return sDate;
//
//    }
//
//    public void guardarViaje() throws ParseException {
//        try {
//            
//            conexion cc = new conexion();
//            Connection cn = cc.conectar();
//
//            String sql2 = "select VI.NEXTVAL from dual";           
//
//            Statement psd2 = cn.createStatement();
//            ResultSet rs2 = psd2.executeQuery(sql2);
//            String cod = "";
//
//            while (rs2.next()) {
//                cod = rs2.getString("NEXTVAL");
//            }
//
//            int valor = Integer.valueOf(cod);
//            
//            String sql = "INSERT INTO VIAJES(COD_VIAJE, CED_PER_VIA, COD_FRE_VIA, COD_BUS_VIA, FEC_VEN, FEC_VIAJE, OBS) VALUES(?,?,?,?,?,?,?) ";
//            PreparedStatement psd = cn.prepareStatement(sql);
//            String cod_fre, cod_bus, obs, fec_ven, fec_via;
//            cod_fre = obtenerCodigoFrecuencia(jcbxHora_Salida.getSelectedItem().toString());
//            cod_bus = obtenerCodigoBus(jcbxBus.getSelectedItem().toString());
//
//            SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
//
//            fec_ven = formatoDeFecha.format(jDateChooser_venta.getDate());
//            fec_via = formatoDeFecha.format(jDateChooser_viaje.getDate());
//
//            Date dateForBBDD = formatoDeFecha.parse(fec_ven);
//            Date dateForBBDD2 = formatoDeFecha.parse(fec_via);
//            java.sql.Date sDate = convertUtilToSql(dateForBBDD);
//            java.sql.Date sDate2 = convertUtilToSql(dateForBBDD2);
//           
//            obs = txtObservacion.getText();
//
//            psd.setInt(1, valor);
//            psd.setString(2, txtCedulaPersonal.getText());
//            psd.setString(3, cod_fre);
//            psd.setString(4, cod_bus);
//            psd.setDate(5, sDate);
//            psd.setDate(6, sDate2);
//            psd.setString(7, obs);
//
//            int n = psd.executeUpdate();
//            if (n > 0) {
//                JOptionPane.showMessageDialog(this, "Se Inserto correctamente el VIAJE");
//                limpiarTextos();
//            }
//
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(this, ex);
//        }
//    }

//    public void limpiarTextos() {
//        txtCedulaPersonal.setText(null);
//        jcbxOrigen.setSelectedIndex(0);
//        jcbxDestino.setSelectedIndex(0);
//        jcbxHora_Salida.setSelectedIndex(0);
//        jcbxBus.setSelectedIndex(0);
//        jDateChooser_venta.setDate(null);
//        jDateChooser_viaje.setDate(null);
//        txtObservacion.setText(null);
//    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jcbxHora_Salida = new javax.swing.JComboBox<>();
        jcbxBus = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jcbxDestino = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        btnNuevo3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        btnGuardar3 = new javax.swing.JButton();
        btnCancelar3 = new javax.swing.JButton();
        btnSalir4 = new javax.swing.JButton();
        btnBorrar3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel1.setText("Cedula Personal: ");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel2.setText("Frecuencia:");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel3.setText("Bus:");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel4.setText("Fecha del viaje:");

        jcbxHora_Salida.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione" }));

        jcbxBus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione", "42", "45", "48" }));
        jcbxBus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbxBusActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel7.setText("Origen:");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel8.setText("Ciudad Destino:");

        jcbxDestino.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione" }));
        jcbxDestino.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbxDestinoItemStateChanged(evt);
            }
        });

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/viajes-797 (2).jpg"))); // NOI18N

        jPanel3.setBackground(new java.awt.Color(0, 153, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(0, 30));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jLabel10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel5.setText("Datos Personal");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel11.setText("Placa Bus:");

        jLabel12.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel13.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel14.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnNuevo3.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnNuevo3.setText("Nuevo");
        btnNuevo3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevo3ActionPerformed(evt);
            }
        });

        jButton1.setText("Boletos");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnGuardar3.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnGuardar3.setText("Guardar");
        btnGuardar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardar3ActionPerformed(evt);
            }
        });

        btnCancelar3.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnCancelar3.setText("Cancelar");
        btnCancelar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelar3ActionPerformed(evt);
            }
        });

        btnSalir4.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnSalir4.setText("Salir");
        btnSalir4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalir4ActionPerformed(evt);
            }
        });

        btnBorrar3.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnBorrar3.setText("Borrar");
        btnBorrar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrar3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel7)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jcbxHora_Salida, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(4, 4, 4)
                                        .addComponent(jLabel5))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel11)
                                        .addComponent(jLabel4))
                                    .addComponent(jLabel8))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jcbxDestino, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addComponent(jcbxBus, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(73, 73, 73))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnNuevo3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGuardar3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSalir4, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBorrar3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jLabel9)
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jcbxDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8))
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel2)
                        .addComponent(jcbxHora_Salida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jcbxBus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11))
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo3)
                    .addComponent(jButton1)
                    .addComponent(btnGuardar3)
                    .addComponent(btnCancelar3)
                    .addComponent(btnSalir4)
                    .addComponent(btnBorrar3))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 617, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jcbxDestinoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbxDestinoItemStateChanged
        //cargarComboFrecuencia();
    }//GEN-LAST:event_jcbxDestinoItemStateChanged

    private void btnNuevo3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevo3ActionPerformed

    }//GEN-LAST:event_btnNuevo3ActionPerformed

    private void btnGuardar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardar3ActionPerformed

    }//GEN-LAST:event_btnGuardar3ActionPerformed

    private void btnCancelar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar3ActionPerformed

    }//GEN-LAST:event_btnCancelar3ActionPerformed

    private void btnSalir4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalir4ActionPerformed
        // TODO add your handling code here:
        this.dispose();
        //para cerrar la ventana
        //exit 0 se sale de todo el sistema
    }//GEN-LAST:event_btnSalir4ActionPerformed

    private void btnBorrar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrar3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBorrar3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       if(jcbxBus.getSelectedItem()=="42"){
           Asientos42 s= new Asientos42();
           s.setVisible(true);
           
       }else if(jcbxBus.getSelectedItem()=="45"){
           Asientos45 s= new Asientos45();
           s.setVisible(true);
           
       } else if(jcbxBus.getSelectedItem()=="48"){
           Asientos48 s= new Asientos48();
           s.setVisible(true);
           
       }
       
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jcbxBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbxBusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbxBusActionPerformed

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
            java.util.logging.Logger.getLogger(IngresoViajesbus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IngresoViajesbus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IngresoViajesbus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IngresoViajesbus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IngresoViajesbus().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBorrar3;
    private javax.swing.JButton btnCancelar3;
    private javax.swing.JButton btnGuardar3;
    private javax.swing.JButton btnNuevo3;
    private javax.swing.JButton btnSalir4;
    private javax.swing.JButton jButton1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JComboBox<String> jcbxBus;
    private javax.swing.JComboBox<String> jcbxDestino;
    private javax.swing.JComboBox<String> jcbxHora_Salida;
    // End of variables declaration//GEN-END:variables
}
