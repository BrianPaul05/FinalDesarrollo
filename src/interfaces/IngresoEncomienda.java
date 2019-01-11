/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import Conexion.Conexion;
import java.awt.HeadlessException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
        cargarValoresIncio();
        desactivarTextos();       
        cargarComboDestino();
    }

    private void cargarValoresIncio() {
        txtFechaEmision.setText(obtenerFechaActual());
        cargarOrigenEncomienda();
        
    }

    private void desactivarTextos() {
        txtFechaEmision.setEnabled(false);
        txtOrigen.setEnabled(false);
        txtNombre.setEnabled(false);
        txtApellido.setEnabled(false);
        txtNombreDes.setEnabled(false);
        txtApellidoDes.setEnabled(false);
    }

    public void desactivarBotonesCancelar() {
        jbtnNuevo.setEnabled(true);
        jbtnGuardar.setEnabled(false);
        jbtnCancelar.setEnabled(false);
        jbtnSalir.setEnabled(true);
    }
    
    public void desactivarBotonesNuevo() {
        jbtnNuevo.setEnabled(false);
        jbtnGuardar.setEnabled(false);
        jbtnCancelar.setEnabled(true);
        jbtnSalir.setEnabled(true);
    }

    public void limpiarTextos() {
        txtRemitente.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtDestinatario.setText("");
        txtNombreDes.setText("");
        txtApellidoDes.setText("");
        txtContenido.setText("");        
        txtFechaEmision.setText("");        
        txtCosto.setText("");
        txtOrigen.setText("");
        txtHora.setText("");
        txtMinuto.setText("");
        txtSegundo.setText("");
        jDateChooser_FechaLlegada.setDate(null);
        jComboBox_HoraSalida.setSelectedIndex(0);
        jComboBox_Destino.setSelectedIndex(0);
        jDateChooser_FechaSalida.setDate(null);
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

    private void cargarComboDestino() {

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

    public void cargarHoraSalida() {
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

            java.sql.Date FEC_SALIDA;
            String horSalida = "";
            int cond = 0;

            FEC_SALIDA = convetirdorFecha(jDateChooser_FechaSalida.getDate());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Date date1 = sdf.parse(FEC_SALIDA.toString());
                Date date2 = sdf.parse(obtenerFechaActual());
                cond = date1.compareTo(date2);

                if (cond < 0) {
                    jComboBox_HoraSalida.addItem("NO HAY HORARIOS");
                }
                if (cond > 0) {
                    while (rs.next()) {
                        horSalida = rs.getString("HORA_SALIDA");
                        jComboBox_HoraSalida.addItem(horSalida);
                    }
                }
                if (cond == 0) {
                    while (rs.next()) {
                        horSalida = rs.getString("HORA_SALIDA");
                        //8:30 - 10:20
                        //Verifica si la hora actual s antes de la hora de salida, solo se carga en el combo las horas superiores
                        boolean isBeforeHour = LocalTime.parse(hora + ":" + minutos + ":" + segundos).isBefore(LocalTime.parse(horSalida));
                        if (isBeforeHour) {

                            jComboBox_HoraSalida.addItem(horSalida);
                        }
                    }
                }

            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    private boolean comprobarFechaLlegada(Date salida, Date llegada) {
        if (llegada.before(salida)) {
            return false;
        } else {
            return true;
        }
    }

    private void guardarEncomienda() {

        if (txtRemitente.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese la cédula del remitente");
            txtRemitente.requestFocus();
        } else if (txtDestinatario.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese la cédula del destinatario");
            txtDestinatario.requestFocus();
        } else if (txtCosto.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese el costo");
            txtDestinatario.requestFocus();
        } else if (jDateChooser_FechaLlegada.getDate().before(jDateChooser_FechaSalida.getDate())) {
            JOptionPane.showMessageDialog(null, "La fecha de llegada es incorrecta . . . !");
        } else if (evitarMismoRemDes(txtRemitente, txtDestinatario)) {
            JOptionPane.showMessageDialog(null, "El mismo usuario no puede ser remitente y destinatario.");
            txtDestinatario.requestFocus();
        } else {
            String contenido;
            if (txtContenido.getText().isEmpty()) {
                contenido = "NINGUNA";
            } else {
                contenido = txtContenido.getText();
            }

            try {
                Conexion cc = new Conexion();
                Connection cn = cc.conexion();

                String sql = "INSERT INTO ENCOMIENDAS (COD_VIAJE, COD_REMITENTE, COD_DESTINATARIO, COSTO, CONTENIDO, ESTADO1, ESTADO2, FEC_LLE_ENCOMIENDA, HOR_LLE_ENCOMIENDA) VALUES(?,?,?,?,?,?,?,?,?)";
                PreparedStatement psd = cn.prepareStatement(sql);
                int c = Integer.valueOf(codParaEncomienda);
                psd.setInt(1, c);
                psd.setString(2, txtRemitente.getText());
                psd.setString(3, txtDestinatario.getText());
                psd.setInt(4, Integer.valueOf(txtCosto.getText()));
                psd.setString(5, contenido);
                String estEnviado;

                psd.setString(6, "");

                psd.setString(7, "");
                boolean condFechaLlegada = comprobarFechaLlegada(jDateChooser_FechaSalida.getDate(), jDateChooser_FechaLlegada.getDate());
                if (condFechaLlegada) {
                    psd.setDate(8, convetirdorFecha(jDateChooser_FechaLlegada.getDate()));
                } else {
                    JOptionPane.showMessageDialog(null, "LA FECHA DE LLEGADA ES INCORRECTA . . . !");
                }

                String hora = txtHora.getText() + "" + txtMinuto.getText() + "" + txtSegundo.getText();

                psd.setString(9, hora);
                int n = psd.executeUpdate();
                if (n > 0) {
                    JOptionPane.showMessageDialog(null, "Encomienda guardada");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    private void cargarInformacionCliente(String cedula, JTextField txtNombre, JTextField txtApellido) {
        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            String sql = "SELECT * FROM CLIENTES WHERE CED_CLI = '" + cedula + "'";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                txtNombre.setText(rs.getString("NOM_CLI"));
                txtApellido.setText(rs.getString("APE_CLI"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(IngresoEncomienda.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean evitarMismoRemDes(JTextField ced1, JTextField ced2) {
        /*
        Metodo para evitar que un usuario sea el mismo remitente y destinatario
         */
        String cedRemitente, cedDestinatario;
        cedRemitente = ced1.getText();
        cedDestinatario = ced2.getText();
        if (cedRemitente.equals(cedDestinatario)) {
            return true;
        } else {
            return false;
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
        jPanel3 = new javax.swing.JPanel();
        PanelPrincipal = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
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
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jDateChooser_FechaLlegada = new com.toedter.calendar.JDateChooser();
        txtHora = new javax.swing.JTextField();
        txtMinuto = new javax.swing.JTextField();
        txtSegundo = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jbtnNuevo = new javax.swing.JButton();
        jbtnGuardar = new javax.swing.JButton();
        jbtnCancelar = new javax.swing.JButton();
        jbtnSalir = new javax.swing.JButton();
        btnBorrar3 = new javax.swing.JButton();
        txtFechaEmision = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtOrigen = new javax.swing.JTextField();
        jComboBox_HoraSalida = new javax.swing.JComboBox<>();
        jDateChooser_FechaSalida = new com.toedter.calendar.JDateChooser();
        txtContenido = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        txtNombreDes = new javax.swing.JTextField();
        txtApellidoDes = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();

        jScrollPane1.setViewportView(jEditorPane1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(51, 153, 255));

        PanelPrincipal.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel1.setText("ENCOMIENDAS");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Remitente");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Fecha de Salida:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Fecha Emisión:");

        txtRemitente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRemitenteActionPerformed(evt);
            }
        });
        txtRemitente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRemitenteKeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Destinatario");

        txtDestinatario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDestinatarioKeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Costo:");

        txtCosto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCostoFocusLost(evt);
            }
        });
        txtCosto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCostoKeyTyped(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Origen:");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Destino:");
        jLabel9.setToolTipText("");

        jComboBox_Destino.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione" }));
        jComboBox_Destino.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox_DestinoItemStateChanged(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Hora Salida:");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Contenido:");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ENTREGA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("Fecha Entrega:");

        txtHora.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtHoraKeyReleased(evt);
            }
        });

        txtMinuto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtMinutoKeyReleased(evt);
            }
        });

        txtSegundo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSegundoKeyReleased(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setText("Hora:");

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setText(":");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setText(":");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser_FechaLlegada, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtHora, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMinuto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSegundo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtHora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMinuto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSegundo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jbtnNuevo.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        jbtnNuevo.setText("Nuevo");
        jbtnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnNuevoActionPerformed(evt);
            }
        });

        jbtnGuardar.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        jbtnGuardar.setText("Guardar");
        jbtnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnGuardarActionPerformed(evt);
            }
        });

        jbtnCancelar.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        jbtnCancelar.setText("Cancelar");
        jbtnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCancelarActionPerformed(evt);
            }
        });

        jbtnSalir.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        jbtnSalir.setText("Salir");
        jbtnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSalirActionPerformed(evt);
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
                .addComponent(jbtnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jbtnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jbtnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jbtnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnBorrar3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnNuevo)
                    .addComponent(jbtnGuardar)
                    .addComponent(jbtnCancelar)
                    .addComponent(jbtnSalir)
                    .addComponent(btnBorrar3))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setText("$");

        jComboBox_HoraSalida.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboBox_HoraSalidaFocusGained(evt);
            }
        });

        txtContenido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtContenidoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtContenidoKeyTyped(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Nombre:");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setText("Apellido:");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setText("Nombre:");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setText("Apellido:");

        javax.swing.GroupLayout PanelPrincipalLayout = new javax.swing.GroupLayout(PanelPrincipal);
        PanelPrincipal.setLayout(PanelPrincipalLayout);
        PanelPrincipalLayout.setHorizontalGroup(
            PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPrincipalLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel15))
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(txtContenido, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(75, Short.MAX_VALUE))
                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                        .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(PanelPrincipalLayout.createSequentialGroup()
                                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtNombreDes, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtApellidoDes, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtRemitente, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDestinatario, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                                        .addGap(53, 53, 53)
                                        .addComponent(jLabel9))
                                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                                        .addGap(27, 27, 27)
                                        .addComponent(jLabel10))
                                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addComponent(jLabel5))
                                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                                        .addGap(64, 64, 64)
                                        .addComponent(jLabel7))
                                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                                        .addGap(59, 59, 59)
                                        .addComponent(jLabel8))
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(PanelPrincipalLayout.createSequentialGroup()
                                            .addComponent(txtOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(1, 1, 1))
                                        .addComponent(jComboBox_Destino, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PanelPrincipalLayout.createSequentialGroup()
                                            .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel13))
                                        .addComponent(txtFechaEmision, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                                        .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jDateChooser_FechaSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jComboBox_HoraSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(1, 1, 1))))
                            .addGroup(PanelPrincipalLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)))
                        .addGap(48, 48, 48))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelPrincipalLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(206, 206, 206))
            .addGroup(PanelPrincipalLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelPrincipalLayout.setVerticalGroup(
            PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPrincipalLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                        .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtRemitente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addGap(18, 18, 18)
                        .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDestinatario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombreDes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addGap(18, 18, 18)
                        .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtApellidoDes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16)))
                    .addGroup(PanelPrincipalLayout.createSequentialGroup()
                        .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooser_FechaSalida, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(PanelPrincipalLayout.createSequentialGroup()
                                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(txtFechaEmision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13))
                                .addGap(18, 18, 18)
                                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(txtOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)))
                        .addGap(18, 18, 18)
                        .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jComboBox_Destino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jComboBox_HoraSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(PanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtContenido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jDateChooser_FechaSalida.getDateEditor().addPropertyChangeListener(new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent e) {
                jComboBox_Destino.setSelectedIndex(0);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelPrincipal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 44, Short.MAX_VALUE)
                .addComponent(PanelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnNuevoActionPerformed
        cargarValoresIncio();
        desactivarBotonesNuevo();
    }//GEN-LAST:event_jbtnNuevoActionPerformed

    private void jbtnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnGuardarActionPerformed
        guardarEncomienda();
    }//GEN-LAST:event_jbtnGuardarActionPerformed

    private void jbtnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCancelarActionPerformed
        limpiarTextos();
        desactivarTextos();
        desactivarBotonesCancelar();
    }//GEN-LAST:event_jbtnCancelarActionPerformed

    private void jbtnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
        //para cerrar la ventana
        //exit 0 se sale de todo el sistema
    }//GEN-LAST:event_jbtnSalirActionPerformed

    private void btnBorrar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrar3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBorrar3ActionPerformed

    private void jComboBox_HoraSalidaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBox_HoraSalidaFocusGained

    }//GEN-LAST:event_jComboBox_HoraSalidaFocusGained

    private void jComboBox_DestinoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox_DestinoItemStateChanged
        cargarHoraSalida();
    }//GEN-LAST:event_jComboBox_DestinoItemStateChanged

    private void txtRemitenteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRemitenteKeyReleased
        cargarInformacionCliente(txtRemitente.getText(), txtNombre, txtApellido);
    }//GEN-LAST:event_txtRemitenteKeyReleased

    private void txtDestinatarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDestinatarioKeyReleased
        cargarInformacionCliente(txtDestinatario.getText(), txtNombreDes, txtApellidoDes);
    }//GEN-LAST:event_txtDestinatarioKeyReleased

    private void txtContenidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContenidoKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            String cad = ("" + c).toUpperCase();
            c = cad.charAt(0);
            evt.setKeyChar(c);
        }
    }//GEN-LAST:event_txtContenidoKeyTyped

    private void txtContenidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContenidoKeyReleased
        controlDescripciónEncomienda();
    }//GEN-LAST:event_txtContenidoKeyReleased

    private void txtCostoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCostoKeyTyped
        char validar = evt.getKeyChar();
        if (Character.isLetter(validar)) {
            getToolkit().beep();
            evt.consume();
            JOptionPane.showMessageDialog(rootPane, "INGRESE EL COSTO EN NÚMEROS");
        }
    }//GEN-LAST:event_txtCostoKeyTyped

    private void txtCostoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCostoFocusLost
        String numeroRecogido = txtCosto.getText();
        String numeroNuevo = numeroRecogido.replace(",", ".");
        double numero1 = Double.parseDouble(numeroNuevo);
        txtCosto.setText(String.valueOf(numero1));
        String numConvertido = completarNumeroDecimal(numero1);
        txtCosto.setText(numConvertido);

        boolean condicion = controlCostoViaje(txtCosto.getText());
        if (!condicion) {
            JOptionPane.showMessageDialog(null, "A SUPERADO EL PRECIO DE UNA ENCOMIENDA.");
            txtCosto.setText(null);
        }
    }//GEN-LAST:event_txtCostoFocusLost

    private void txtHoraKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHoraKeyReleased
        if (Integer.valueOf(txtHora.getText()) < 0 || Integer.valueOf(txtHora.getText()) > 23) {
            JOptionPane.showMessageDialog(null, "ERROR: INGRESE UNA HORA CORRECTA.");
            txtHora.setText(null);
            txtHora.requestFocus();
        }
    }//GEN-LAST:event_txtHoraKeyReleased

    private void txtMinutoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMinutoKeyReleased
        if (Integer.valueOf(txtMinuto.getText()) < 0 || Integer.valueOf(txtMinuto.getText()) > 59) {
            JOptionPane.showMessageDialog(null, "ERROR: INGRESE EL MINUTO CORRECTO.");
            txtMinuto.setText(null);
            txtMinuto.requestFocus();
        }
    }//GEN-LAST:event_txtMinutoKeyReleased

    private void txtSegundoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSegundoKeyReleased
        if (Integer.valueOf(txtSegundo.getText()) < 0 || Integer.valueOf(txtSegundo.getText()) > 59) {
            JOptionPane.showMessageDialog(null, "ERROR: INGRESE EL SEGUNDO CORRECTO.");
            txtSegundo.setText(null);
            txtSegundo.requestFocus();
        }
    }//GEN-LAST:event_txtSegundoKeyReleased

    private void txtRemitenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRemitenteActionPerformed
        cargarInformacionCliente(txtRemitente.getText(), txtNombre, txtApellido);
    }//GEN-LAST:event_txtRemitenteActionPerformed

    public boolean controlCostoViaje(String costo) {
        double num;
        num = Double.parseDouble(costo);
        if (num > 9999.99) {
            return false;
        } else {
            return true;
        }
    }

    private String completarNumeroDecimal(double numero1) {
        DecimalFormatSymbols separadoresPersonalizados = new DecimalFormatSymbols();
        separadoresPersonalizados.setDecimalSeparator('.');
        DecimalFormat formato1 = new DecimalFormat("#.00", separadoresPersonalizados);
        return formato1.format(numero1);
    }

    public void controlDescripciónEncomienda() throws HeadlessException {
        if (txtContenido.getText().length() > 100) {
            JOptionPane.showMessageDialog(null, "LA DESCRIPCIÓN DE CONTENIDO DEBE TENER MÁXIMO 100 CARACTERES");

            int n = txtContenido.getText().length() - 100;
            int indice = n > txtContenido.getText().length() ? 0 : txtContenido.getText().length() - n;
            txtContenido.setText(txtContenido.getText().substring(0, indice));
        }
    }

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
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
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
    private javax.swing.JButton jbtnCancelar;
    private javax.swing.JButton jbtnGuardar;
    private javax.swing.JButton jbtnNuevo;
    private javax.swing.JButton jbtnSalir;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtApellidoDes;
    private javax.swing.JTextField txtContenido;
    private javax.swing.JTextField txtCosto;
    private javax.swing.JTextField txtDestinatario;
    private javax.swing.JTextField txtFechaEmision;
    private javax.swing.JTextField txtHora;
    private javax.swing.JTextField txtMinuto;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNombreDes;
    private javax.swing.JTextField txtOrigen;
    private javax.swing.JTextField txtRemitente;
    private javax.swing.JTextField txtSegundo;
    // End of variables declaration//GEN-END:variables

}
