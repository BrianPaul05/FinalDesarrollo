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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author Roberto Altamirano
 */
public class IngresoEncomienda extends javax.swing.JInternalFrame {

    /**
     * Creates new form IngresoEncomienda
     */
    String codParaEncomienda;

    public IngresoEncomienda(String codEncomienda) {
        codParaEncomienda = codEncomienda;
        initComponents();
        cargarValoresPorDefecto();
//        cargarNumDocumento();
        desactivarTextos();
        cargarOrigenEncomienda();
        cargarComboDestino();
    }

    private void cargarValoresPorDefecto() {
        txtFechaEmision.setText(obtenerFechaActual());
    }

    private void desactivarTextos() {
        txtFechaEmision.setEnabled(false);
        txtN_Documento.setEnabled(false);
        txtOrigen.setEnabled(false);
    }

    private void cargarNumDocumento() {
        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();

            String sql = "SELECT ENC.NEXTVAL FROM DUAL";
            Statement psd2 = cn.createStatement();
            ResultSet rs2 = psd2.executeQuery(sql);
            String cod = "";
            while (rs2.next()) {
                cod = rs2.getString("NEXTVAL");
            }

            if (cod.isEmpty()) {
                txtN_Documento.setText("1");
            } else {
                txtN_Documento.setText(cod);
            }
        } catch (SQLException ex) {
            Logger.getLogger(IngresoEncomienda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String obtenerFechaActual() {
        String dia, mes, año, fecha;
        Calendar c = Calendar.getInstance();
        dia = Integer.toString(c.get(Calendar.DATE));
        mes = Integer.toString(c.get(Calendar.MONTH) + 1);
        año = Integer.toString(c.get(Calendar.YEAR));
        fecha = año + "-" + mes + "-" + dia;
        return fecha;
    }

    private void cargarOrigenEncomienda() {
        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();

            String sql = "SELECT UBICACION FROM OFICINAS WHERE COD_OFI = '" + codParaEncomienda + "'";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                txtOrigen.setText(rs.getString("UBICACION"));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    public void cargarComboDestino() {

        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();

            //OBTENGO EL CODIGO DEL DESTINO DE ACUERDO A LA OFICINA DE ORIGEN
            String sql = "SELECT COD_OFI_DES FROM RUTAS WHERE COD_OFI_ORI = '" + codParaEncomienda + "'";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            String cod = "";
            while (rs.next()) {
                cod = rs.getString("COD_OFI_DES");

                String sql2 = "SELECT UBICACION FROM OFICINAS WHERE COD_OFI = '" + cod + "'";
                Statement psd2 = cn.createStatement();
                ResultSet rs2 = psd2.executeQuery(sql2);
                String nombre;
                while (rs2.next()) {
                    nombre = rs2.getString("UBICACION");
                    jComboBox_Destino.addItem(nombre);
                }

            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
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

    public void cargarFechaSalida() {
        jComboBox_HoraSalida.removeAllItems();
        jComboBox_HoraSalida.addItem("Seleccione");
        try {

            Calendar calendario = new GregorianCalendar();
            int hora, minutos, segundos;
            hora = calendario.get(Calendar.HOUR_OF_DAY);
            minutos = calendario.get(Calendar.MINUTE);
            segundos = calendario.get(Calendar.SECOND);

            String codOfiOrigen, nomOfiDestino;
            codOfiOrigen = codParaEncomienda;
            nomOfiDestino = String.valueOf(jComboBox_Destino.getSelectedItem());

            System.out.println(codOfiOrigen + " ;;;;; " + nomOfiDestino);

            String sql = "SELECT HORA_SALIDA "
                    + "FROM FRECUENCIAS "
                    + "WHERE COD_RUTA_PER = (SELECT COD_RUTA "
                    + "                         FROM RUTAS "
                    + "                         WHERE COD_OFI_ORI = " + codOfiOrigen + " "
                    + "                         AND COD_OFI_DES = (SELECT COD_OFI "
                    + "                                                 FROM OFICINAS "
                    + "                                                 WHERE UBICACION = '" + nomOfiDestino + "'))";
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);

            //+++++++++++++++++++++++++++++++++++++++++
            String horSal = "";
            java.sql.Date FEC_SALIDA;
            int cond = 0;

            FEC_SALIDA = convetirdorFecha(jDateChooser_FechaSalida.getDate());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Date date1 = sdf.parse(FEC_SALIDA.toString());
                Date date2 = sdf.parse(obtenerFechaActual());
                cond = date1.compareTo(date2);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }           

            if (cond < 0) {
                JOptionPane.showMessageDialog(null, "Error: Fecha incorrecta");
            } else if (cond > 0) {
                while (rs.next()) {
                    horSal = rs.getString("HORA_SALIDA");
                    jComboBox_HoraSalida.addItem(horSal);
                }
            } else if (cond == 0){
                while (rs.next()) {
                    //8:30 - 10:20
                    //Verifica si la hora actual s antes de la hora de salida, solo se carga en el combo las horas superiores
                    boolean isBeforeHour = LocalTime.parse(hora + ":" + minutos + ":" + segundos).isBefore(LocalTime.parse(horSal));
                    if (isBeforeHour) {
                        horSal = rs.getString("HORA_SALIDA");
                        jComboBox_HoraSalida.addItem(horSal);
                    }
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    private void guardarEncomienda() {
        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();

            String sql = "INSERTO INTO ENCOMIENDAS (COD_VIAJE, COD_REMITENTE, COD_DESTINATARIO, COSTO, CONTENIDO, ESTADO1, ESTADO2,FEC_LLE_ENCOMIENDA, HOR_LLE_ENCOMIENDA) VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement psd = cn.prepareStatement(sql);

            psd.setString(1, txtCodigoViaje.getText());
            psd.setString(2, txtRemitente.getText());
            psd.setString(3, txtDestinatario.getText());
            psd.setString(4, txtCosto.getText());
            psd.setString(5, txtContenido.getText());
            String estado1 = null;
            if (jCheckBox_Estado1.isSelected()) {
                estado1 = "ENVIADO";
            } else {
                estado1 = "NO ENVIADO";
            }
            psd.setString(7, estado1);

            String estado2 = null;
            if (jCheckBox_Estado2.isSelected()) {
                estado2 = "ENTREGADO";
            } else {
                estado2 = "NO ENTREGADO";
            }

            psd.setString(8, estado2);
            psd.setString(9, estado2);
            psd.setString(10, estado2);
            int n = psd.executeUpdate();
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "Encomienda guardada");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        PanelPrincipal = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtN_Documento = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtRemitente = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtDestinatario = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtCosto = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jComboBox_Destino = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtContenido = new javax.swing.JTextArea();
        jCheckBox_Estado1 = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jCheckBox_Estado2 = new javax.swing.JCheckBox();
        jDateChooser_FechaLlegada = new com.toedter.calendar.JDateChooser();
        txtHoraLlegada = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        btnNuevo3 = new javax.swing.JButton();
        btnGuardar3 = new javax.swing.JButton();
        btnCancelar3 = new javax.swing.JButton();
        btnSalir4 = new javax.swing.JButton();
        btnBorrar3 = new javax.swing.JButton();
        txtCodigoViaje = new javax.swing.JTextField();
        txtFechaEmision = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtOrigen = new javax.swing.JTextField();
        jComboBox_HoraSalida = new javax.swing.JComboBox<>();
        jDateChooser_FechaSalida = new com.toedter.calendar.JDateChooser();
        jPanel3 = new javax.swing.JPanel();

        jScrollPane1.setViewportView(jEditorPane1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        PanelPrincipal.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setText("ENCOMIENDAS");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("N.- Documento");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Remitente");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Fecha de Salida");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Fecha Emisión");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Destinatario");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Costo");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Origen");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Destino");

        jComboBox_Destino.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione" }));
        jComboBox_Destino.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_DestinoItemStateChanged(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Hora Salida");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Contenido");

        txtContenido.setColumns(20);
        txtContenido.setRows(5);
        jScrollPane2.setViewportView(txtContenido);

        jCheckBox_Estado1.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox_Estado1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jCheckBox_Estado1.setText("ENVIADO");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ENTREGA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("Fecha Entrega");

        jCheckBox_Estado2.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox_Estado2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jCheckBox_Estado2.setText("ENTREGADO");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser_FechaLlegada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jCheckBox_Estado2)
                        .addGap(0, 101, Short.MAX_VALUE))
                    .addComponent(txtHoraLlegada))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12)
                    .addComponent(jDateChooser_FechaLlegada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtHoraLlegada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(jCheckBox_Estado2))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnNuevo3.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnNuevo3.setText("Nuevo");
        btnNuevo3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevo3ActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(btnNuevo3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnGuardar3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(btnCancelar3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(btnSalir4, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnBorrar3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo3)
                    .addComponent(btnGuardar3)
                    .addComponent(btnCancelar3)
                    .addComponent(btnSalir4)
                    .addComponent(btnBorrar3))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setText("$");

        javax.swing.GroupLayout PanelPrincipalLayout = new javax.swing.GroupLayout(PanelPrincipal);
        PanelPrincipal.setLayout(PanelPrincipalLayout);
        PanelPrincipalLayout.setHorizontalGroup(
            PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPrincipalLayout.createSequentialGroup()
                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                        .addGap(267, 267, 267)
                        .addComponent(jLabel1))
                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel11)
                            .addComponent(jLabel5))
                        .addGap(24, 24, 24)
                        .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelPrincipalLayout.createSequentialGroup()
                                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtDestinatario)
                                        .addComponent(txtN_Documento)
                                        .addComponent(txtRemitente, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtFechaEmision))
                                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                                        .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel13)))
                                .addGap(67, 67, 67)
                                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                                        .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(PanelPrincipalLayout.createSequentialGroup()
                                                .addComponent(jLabel10)
                                                .addGap(262, 262, 262)
                                                .addComponent(txtCodigoViaje))
                                            .addGroup(PanelPrincipalLayout.createSequentialGroup()
                                                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel4)
                                                    .addComponent(jLabel8)
                                                    .addComponent(jLabel9))
                                                .addGap(13, 13, 13)
                                                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(txtOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jComboBox_Destino, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jDateChooser_FechaSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jComboBox_HoraSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 170, Short.MAX_VALUE)))
                                        .addGap(52, 52, 52))
                                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                                        .addComponent(jCheckBox_Estado1)
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        PanelPrincipalLayout.setVerticalGroup(
            PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPrincipalLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtN_Documento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(17, 17, 17))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelPrincipalLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtRemitente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4))
                    .addComponent(jDateChooser_FechaSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDestinatario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel9)
                    .addComponent(jComboBox_Destino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCodigoViaje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(txtFechaEmision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(jComboBox_HoraSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13)))
                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox_Estado1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(51, 153, 255));

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(PanelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevo3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevo3ActionPerformed

    }//GEN-LAST:event_btnNuevo3ActionPerformed

    private void btnGuardar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardar3ActionPerformed
        guardarEncomienda();
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

    private void jComboBox_DestinoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_DestinoItemStateChanged
        cargarFechaSalida();
    }//GEN-LAST:event_jComboBox_DestinoItemStateChanged

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
            java.util.logging.Logger.getLogger(IngresoEncomienda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IngresoEncomienda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IngresoEncomienda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IngresoEncomienda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JPanel PanelPrincipal;
    private javax.swing.JButton btnBorrar3;
    private javax.swing.JButton btnCancelar3;
    private javax.swing.JButton btnGuardar3;
    private javax.swing.JButton btnNuevo3;
    private javax.swing.JButton btnSalir4;
    private javax.swing.JCheckBox jCheckBox_Estado1;
    private javax.swing.JCheckBox jCheckBox_Estado2;
    private javax.swing.JComboBox<String> jComboBox_Destino;
    private javax.swing.JComboBox<String> jComboBox_HoraSalida;
    private com.toedter.calendar.JDateChooser jDateChooser_FechaLlegada;
    private com.toedter.calendar.JDateChooser jDateChooser_FechaSalida;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField txtCodigoViaje;
    private javax.swing.JTextArea txtContenido;
    private javax.swing.JTextField txtCosto;
    private javax.swing.JTextField txtDestinatario;
    private javax.swing.JTextField txtFechaEmision;
    private javax.swing.JTextField txtHoraLlegada;
    private javax.swing.JTextField txtN_Documento;
    private javax.swing.JTextField txtOrigen;
    private javax.swing.JTextField txtRemitente;
    // End of variables declaration//GEN-END:variables

}
