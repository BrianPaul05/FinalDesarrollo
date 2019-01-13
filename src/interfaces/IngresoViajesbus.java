/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import Conexion.Conexion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author DELL GAMER
 */
public class IngresoViajesbus extends javax.swing.JInternalFrame {

    /**
     * Creates new form IngresoViajes
     */
    String c;

    public IngresoViajesbus(String cedu) {
        initComponents();
        c = cedu;
        txtCedulaVendedor.setText(c);
        txtOrigen.setText(cargarOrigen());
        cargarNumeroBus();
        cargarDestino();
        this.setIconifiable(true);
    }

    private String cargarOrigen() {
        String orige = "";
        try {
            Connection cn = new Conexion().conexion();
            String sql = "select  o.ubicacion "
                    + "from oficinas o, personal_oficina po "
                    + "where o.cod_ofi = po.cod_ofi_per "
                    + "and po.ced_personal_per = '" + c + "' ";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                orige = rs.getString("ubicacion");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
        return orige;
    }

    public void cargarDestino() {
        try {
            Connection cn = new Conexion().conexion();
            String sql = "Select ubicacion from oficinas where ubicacion <> '" + txtOrigen.getText() + "' ";
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                jcbxDestino.addItem(rs.getString("ubicacion"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    public void cargarNumeroBus() {
        try {
            Connection cn = new Conexion().conexion();
            String sql = "Select num_bus from bus";
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            jcbNumeroBus.addItem("Selecione...");
            while (rs.next()) {
                jcbNumeroBus.addItem(rs.getString("num_bus"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    public String cargarPlaca() {
        String placa = "";
        try {
            Connection cn = new Conexion().conexion();
            String sql = "Select placa from bus where num_bus = '" + jcbNumeroBus.getSelectedItem() + "'";
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                placa = rs.getString("placa");
            }
            return placa;
        } catch (Exception e) {
        }
        return placa;
    }

    private Integer obtCodigoOfinicina(String nombre) {
        /*
        oficinas --> ruta --> frecuencia
         */
        int cod = 0;
        try {
            Connection cn = new Conexion().conexion();
            String sql = "SELECT COD_OFI FROM OFICINAS WHERE UBICACION = '" + nombre + "' ";
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                cod = rs.getInt("COD_OFI");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }

        return cod;
    }

    private int obtCodigoRuta(int codOrigen, int codDestino) {
        /*
        oficinas --> ruta --> frecuencia
         */
        int codRuta = 0;
        try {
            Connection cn = new Conexion().conexion();
            String sql = "SELECT COD_RUTA FROM RUTAS WHERE COD_OFI_ORI = '" + codOrigen + "' AND COD_OFI_DES = '" + codDestino + "' ";
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                codRuta = rs.getInt("COD_RUTA");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
        return codRuta;
    }

    private void cargarFrecuencia(int codRuta) {
        jcbxHora_Salida.removeAllItems();
        jcbxHora_Salida.addItem("Seleccione");
        /*
        oficinas --> ruta --> frecuencia
         */
        try {
            Connection cn = new Conexion().conexion();
            String sql = "SELECT HORA_SALIDA FROM FRECUENCIAS WHERE COD_RUTA_PER = '" + codRuta + "'";
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            Calendar calendario = new GregorianCalendar();
            int hora, minutos, segundos;
            hora = calendario.get(Calendar.HOUR_OF_DAY);
            minutos = calendario.get(Calendar.MINUTE);
            segundos = calendario.get(Calendar.SECOND);

            java.sql.Date FEC_SALIDA;
            String horSalida = "";
            int cond = 0;

            FEC_SALIDA = convetirdorFecha(jDateChooser1_Salida.getDate());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Date date1 = sdf.parse(FEC_SALIDA.toString());
                Date date2 = sdf.parse(obtenerFechaActual());
                cond = date1.compareTo(date2);

                if (cond < 0) {
                    jcbxHora_Salida.addItem("NO HAY HORARIOS");
                }
                if (cond > 0) {
                    while (rs.next()) {
                        horSalida = rs.getString("HORA_SALIDA");
                        jcbxHora_Salida.addItem(horSalida);
                    }
                }
                if (cond == 0) {
                    while (rs.next()) {
                        horSalida = rs.getString("HORA_SALIDA");
                        //8:30 - 10:20
                        //Verifica si la hora actual s antes de la hora de salida, solo se carga en el combo las horas superiores
                        boolean isBeforeHour = LocalTime.parse(hora + ":" + minutos + ":" + segundos).isBefore(LocalTime.parse(horSalida));
                        if (isBeforeHour) {
                            jcbxHora_Salida.addItem(horSalida);
                        }
                    }
                }

            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
    }

    public java.sql.Date convetirdorFecha(Date Ingreso) {
        /*
        Convierte la fecha de jDataChooser al formato 2018-12-27
         */
        java.sql.Date regreso;
        regreso = new java.sql.Date(Ingreso.getTime());
        return regreso;
    }

    private String obtenerFechaActual() {
        String dia, mes, año, fecha;
        Calendar c = Calendar.getInstance();
        dia = Integer.toString(c.get(Calendar.DATE));
        mes = Integer.toString(c.get(Calendar.MONTH) + 1);
        año = Integer.toString(c.get(Calendar.YEAR));
        fecha = año + "-" + mes + "-" + dia;
        return fecha;
    }

    private Integer obtenerCapacidadBus(String placa, String num) {
        int numero = 0;
        try {
            Connection cn = new Conexion().conexion();
            String sql = "SELECT CAPACIDAD_BUS FROM BUS WHERE PLACA = '" + placa + "' AND NUM_BUS = '" + num + "' ";
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                numero = rs.getInt("CAPACIDAD_BUS");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e);
        }
        return numero;
    }

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
        jcbNumeroBus = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jcbxDestino = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jDateChooser1_Salida = new com.toedter.calendar.JDateChooser();
        txtOrigen = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtCedulaVendedor = new javax.swing.JLabel();
        btnNuevo3 = new javax.swing.JButton();
        jbtnAsiento = new javax.swing.JButton();
        btnGuardar3 = new javax.swing.JButton();
        btnCancelar3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        txtPlaca = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel1.setText("Cedula Personal: ");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel2.setText("Frecuencia:");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel3.setText("Número de bus:");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel4.setText("Fecha del viaje:");

        jcbNumeroBus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbNumeroBusActionPerformed(evt);
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
            .addGap(0, 617, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        txtOrigen.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 13)); // NOI18N
        jLabel11.setText("Placa Bus:");

        txtCedulaVendedor.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnNuevo3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnNuevo3.setText("Nuevo");
        btnNuevo3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevo3ActionPerformed(evt);
            }
        });

        jbtnAsiento.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jbtnAsiento.setText("Asiento");
        jbtnAsiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAsientoActionPerformed(evt);
            }
        });

        btnGuardar3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnGuardar3.setText("Guardar");
        btnGuardar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardar3ActionPerformed(evt);
            }
        });

        btnCancelar3.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        btnCancelar3.setText("Cancelar");
        btnCancelar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelar3ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton1.setText("Salir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        txtPlaca.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnNuevo3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addComponent(jbtnAsiento, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                                .addComponent(btnGuardar3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(btnCancelar3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)
                                .addComponent(jButton1)
                                .addGap(35, 35, 35))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel11))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtCedulaVendedor, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                                            .addComponent(txtOrigen, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel4))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jcbxDestino, 0, 160, Short.MAX_VALUE)
                                            .addComponent(txtPlaca, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jcbxHora_Salida, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jDateChooser1_Salida, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                                    .addComponent(jcbNumeroBus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(69, 69, 69))))))
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 617, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(txtCedulaVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(txtOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jDateChooser1_Salida, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbxDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel2)
                    .addComponent(jcbxHora_Salida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jcbNumeroBus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnAsiento, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(btnGuardar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void jcbxDestinoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbxDestinoItemStateChanged
        int codOri, codDes, codRuta;
        codOri = obtCodigoOfinicina(txtOrigen.getText());
        codDes = obtCodigoOfinicina(jcbxDestino.getSelectedItem().toString());
        codRuta = obtCodigoRuta(codOri, codDes);
        cargarFrecuencia(codRuta);

    }//GEN-LAST:event_jcbxDestinoItemStateChanged

    private void btnNuevo3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevo3ActionPerformed

    }//GEN-LAST:event_btnNuevo3ActionPerformed

    private void btnGuardar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardar3ActionPerformed

    }//GEN-LAST:event_btnGuardar3ActionPerformed

    private void btnCancelar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar3ActionPerformed

    }//GEN-LAST:event_btnCancelar3ActionPerformed

    private void jbtnAsientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAsientoActionPerformed
        try {
            java.sql.Date FEC_SALIDA;
            FEC_SALIDA = convetirdorFecha(jDateChooser1_Salida.getDate());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse(FEC_SALIDA.toString());
            Object[] datos = new Object[7];
            datos[0] = date1;
            datos[1] = jcbxHora_Salida.getSelectedItem().toString() ;
            datos[2] = txtOrigen.getText();
            datos[3] = jcbxDestino.getSelectedItem().toString();
            datos[4] = txtPlaca.getText();
            datos[5] = txtCedulaVendedor.getText();      
            int cap = obtenerCapacidadBus(txtPlaca.getText(), jcbNumeroBus.getSelectedItem().toString());
            switch (cap) {
                case 42: {
                    Asientos42 a = new Asientos42(datos);
                    a.setVisible(true);
                    break;
                }
                case 45: {
                    Asientos45 a = new Asientos45(datos);
                    a.setVisible(true);
                    break;
                }
                default: {
                    Asientos48 a = new Asientos48(datos);
                    a.setVisible(true);
                    break;
                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(IngresoViajesbus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jbtnAsientoActionPerformed

    private void jcbNumeroBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbNumeroBusActionPerformed

        if (jcbNumeroBus.getSelectedItem().equals("Selecione...")) {
            txtPlaca.setText("");
        } else {
            txtPlaca.setText(cargarPlaca());
        }
    }//GEN-LAST:event_jcbNumeroBusActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

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
                new IngresoViajesbus(Login.usuario.getText()).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar3;
    private javax.swing.JButton btnGuardar3;
    private javax.swing.JButton btnNuevo3;
    private javax.swing.JButton jButton1;
    private com.toedter.calendar.JDateChooser jDateChooser1_Salida;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton jbtnAsiento;
    private javax.swing.JComboBox<String> jcbNumeroBus;
    private javax.swing.JComboBox<String> jcbxDestino;
    private javax.swing.JComboBox<String> jcbxHora_Salida;
    private javax.swing.JLabel txtCedulaVendedor;
    private javax.swing.JLabel txtOrigen;
    private javax.swing.JLabel txtPlaca;
    // End of variables declaration//GEN-END:variables
}
