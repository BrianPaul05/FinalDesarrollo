/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import Conexion.*;

/**
 *
 * @author DELL GAMER
 */
public class IngresoBus extends javax.swing.JInternalFrame {

    /**
     * Creates new form IngresBus
     */
    public IngresoBus() {
        initComponents();
        cargarTablaBus("");
        cargarModificar();
        codigoBus();
        desactivarBotones();
        this.setTitle("REGISTRO BUSES");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/bus2.png")));
        
    }

    public void desactivarTextos() {
        txtCodigoBus.setEnabled(false);
        txtPlaca.setEnabled(false);
        txtNumeroBus.setEnabled(false);
        txtAño.setEnabled(false);
        cbxCapacidad.setEnabled(false);
        cbxEstado.setEnabled(false);
    }

    public void activarTextos() {
        txtCodigoBus.setEnabled(true);
        txtPlaca.setEnabled(true);
        txtNumeroBus.setEnabled(true);
        txtAño.setEnabled(true);
        cbxCapacidad.setEnabled(true);
        cbxEstado.setEnabled(true);
    }

    public void desactivarBotones() {
        btnNuevo.setEnabled(true);
        btnSalir1.setEnabled(true);
        btnBorrar.setEnabled(false);
        btnModificar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnGuardar.setEnabled(false);
    }

    public void activarBotones() {
        btnNuevo.setEnabled(false);
        btnSalir1.setEnabled(false);
        btnBorrar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnGuardar.setEnabled(true);
    }

    public void limpiarTextos() {
        txtCodigoBus.setText("");
        txtPlaca.setText("");
        txtNumeroBus.setText("");
        txtAño.setText("");
        cbxCapacidad.setSelectedIndex(0);
        cbxEstado.setSelectedIndex(0);
    }

    public void codigoBus() {
        try {
            String sql = "";

            int codigo = 0;
            Conexion cn = new Conexion();
            Connection n = cn.conexion();
            sql = "SELECT COUNT(*) FROM BUS";
            Statement psd = n.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                codigo = rs.getInt(1);
            }
            codigo += 1;

             txtCodigoBus.setEnabled(false);
            txtCodigoBus.setText(String.valueOf(codigo));
        } catch (SQLException ex) {
            Logger.getLogger(IngresoBus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void guardarBus() {
        String sql = "";
        if (txtPlaca.getText() == null) {
            JOptionPane.showMessageDialog(this, "Ingrese la placa");
        } else if (txtNumeroBus.getText() == null) {
            JOptionPane.showMessageDialog(this, "Ingrese numero de bus");
        } else if (txtAño.getText() == null) {
            JOptionPane.showMessageDialog(this, "Ingrese el año");
        } else if (cbxCapacidad.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione la capacidad");
        } else if (cbxEstado.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione el estado");
        } else {

            try {
                Conexion cn = new Conexion();
                Connection n = cn.conexion();
                sql = "INSERT INTO BUS(PLACA, NUM_BUS, ANIO, ESTADO_BUS, CAPACIDAD_BUS, ESTADO) VALUES(?,?,?,?,?,?)";
                PreparedStatement psd = n.prepareStatement(sql);

                psd.setString(1, txtPlaca.getText());
                psd.setString(2, txtNumeroBus.getText());
                psd.setString(3, txtAño.getText());
                psd.setString(4, cbxEstado.getSelectedItem().toString());
                psd.setInt(5, Integer.valueOf(cbxCapacidad.getSelectedItem().toString()));
                psd.setString(6, "S");

                int m = psd.executeUpdate();
                if (m > 0) {
                    JOptionPane.showMessageDialog(this, "GUARDADO EXITOSO..");
                    limpiarTextos();
                    desactivarBotones();
                    desactivarTextos();
                    cargarTablaBus("");
                }

            } catch (SQLException ex) {
                Logger.getLogger(IngresoBus.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    DefaultTableModel model;

    public void cargarTablaBus(String Placa) {
        try {
            String titulos[] = {"CODIGO", "PLACA", "NUMERO", "AÑO", "ESTADO", "CAPACIDAD"};
            String registros[] = new String[7];             //7 columnas
            String sql = "";
            Conexion cc = new Conexion();                   //Primero hago la conexion
            Connection cn = cc.conexion();
            sql = "select COD_BUS, \n"
                    + "PLACA,\n"
                    + "NUM_BUS, \n"
                    + "ANIO, \n"
                    + "ESTADO_BUS,\n"
                    + "CAPACIDAD_BUS \n"
                    + "FROM bus "
                    + "where PLACA  LIKE '%" + Placa + "%'"
                    + "AND ESTADO = 'S' ";
            model = new DefaultTableModel(null, titulos);
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                registros[0] = rs.getString("COD_BUS");
                registros[1] = rs.getString("PLACA");
                registros[2] = rs.getString("NUM_BUS");
                registros[3] = rs.getString("ANIO");
                registros[4] = rs.getString("ESTADO_BUS");
                registros[5] = rs.getString("CAPACIDAD_BUS");
                model.addRow(registros);
            }
            tblBuses.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    public void cargarModificar() {
        tblBuses.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (tblBuses.getSelectedRow() != -1) {
                    activarTextos();
                    botonesActualizar();
                    int fila = tblBuses.getSelectedRow();
                    txtCodigoBus.setText(tblBuses.getValueAt(fila, 0).toString());
                    txtPlaca.setText(tblBuses.getValueAt(fila, 1).toString());
                    txtNumeroBus.setText(tblBuses.getValueAt(fila, 2).toString());
                    txtAño.setText(tblBuses.getValueAt(fila, 3).toString());
                    cbxEstado.setSelectedItem(tblBuses.getValueAt(fila, 4).toString());
                    cbxCapacidad.setSelectedItem(tblBuses.getValueAt(fila, 5).toString());
                    txtPlaca.setEnabled(false);
                    txtCodigoBus.setEnabled(false);
                    txtAño.setEnabled(false);
                }

            }
        });

    }

    public void botonesActualizar() {
        btnBorrar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnModificar.setEnabled(true);
        btnNuevo.setEnabled(false);
        btnSalir1.setEnabled(true);
    }

    public void controlPlaca(java.awt.event.KeyEvent evt) {

        char c = evt.getKeyChar();

        if ((c >= 32 && c <= 44) || (c >= 58 && c <= 64) || (c >= 91 && c <= 255)
                || txtPlaca.getText().length() > 6) {
            evt.consume();
            getToolkit().beep();
            JOptionPane.showMessageDialog(this, "ERROR Formato de Placa no Válido");
        }

    }

    public void controlAño(java.awt.event.KeyEvent evt) {
        char caracter = evt.getKeyChar();//  metodos propios // obtener  la  tecla que yo  presiono ,  char un caracter 
        String texto = txtAño.getText();
        if (((caracter < '0')
                || //  solo numeros
                (caracter > '9'))
                &&// solo numero y no letras  
                (caracter != '\b') && (caracter != '.')) { //   no haya  espacio
            evt.consume();// ignorar el evento del  teclado 
        }
    }

    public void modificar() throws ClassNotFoundException {
        try {
            String sql = "";
            Conexion cc = new Conexion();                   //Primero hago la conexion
            Connection cn = cc.conexion();
            sql = "update bus set NUM_BUS = '" + txtNumeroBus.getText() + "' ,"
                    + " ESTADO_BUS = '" + cbxEstado.getSelectedItem() + "' ,"
                    + " CAPACIDAD_BUS = '" + cbxCapacidad.getSelectedItem() + "'"
                    + "where COD_BUS = '" + txtCodigoBus.getText() + "'";
            PreparedStatement psd = cn.prepareCall(sql);
            int n = psd.executeUpdate();
            if (n > 0) {
                JOptionPane.showMessageDialog(this, "Se modifico correctamente");
                cargarTablaBus("");
                desactivarTextos();
                limpiarTextos();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }

    }

    public void controlNumeroBus(java.awt.event.KeyEvent evt) {
        char caracter = evt.getKeyChar();//  metodos propios // obtener  la  tecla que yo  presiono ,  char un caracter 
        String texto = txtNumeroBus.getText();
        if (((caracter < '1')
                || //  solo numeros
                (caracter > '9'))
                &&// solo numero y no letras  
                (caracter != '\b') && (caracter != '.')) { //   no haya  espacio
            evt.consume();// ignorar el evento del  teclado 
        }
    }

    public void controlAnio(java.awt.event.KeyEvent evt) {
        char caracter = evt.getKeyChar();//  metodos propios // obtener  la  tecla que yo  presiono ,  char un caracter 
        String texto = txtAño.getText();
        if (((caracter < '0')
                || //  solo numeros
                (caracter > '9'))
                &&// solo numero y no letras  
                (caracter != '\b') && (caracter != '.')) { //   no haya  espacio
            evt.consume();// ignorar el evento del  teclado 
        }
        if (txtAño.getText().length() == 4) {
            evt.consume();

        }
    }

    public void borrar() {

        if (JOptionPane.showConfirmDialog(this,
                "Estas seguro de borrar el registro",
                "Borrar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                Conexion cc = new Conexion();
                Connection cn = cc.conexion();
                String sql = "update bus set ESTADO ='" + "N" + "' where COD_BUS = '" + txtCodigoBus.getText() + "'";
                PreparedStatement psd = cn.prepareStatement(sql);
                JOptionPane.showMessageDialog(this, "Borrado  exitosamente");
                cargarTablaBus("");
                limpiarTextos();
//                int n = psd.executeUpdate();
//                if (n > 0) {
//                    JOptionPane.showMessageDialog(this, "Borrado  exitosamente");
//                    cargarTablaBus("");
//                    limpiarTextos();
//                    desactivarBotones();
//                    desactivarTextos();
//                }
            } catch (SQLException ex) {
                Logger.getLogger(IngresoBus.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

//    public void enviarDatos() {
//        try {
//            if (txtCodigoBus.getText().isEmpty()) {
//                JOptionPane.showMessageDialog(this, "INGRESE EL CODIGO DEL BUS");
//            } else if (txtPlaca.getText().isEmpty()) {
//                JOptionPane.showMessageDialog(this, "INGRESE LA PLACA DEL BUS");
//            } else if (txtNumeroBus.getText().isEmpty()) {
//                JOptionPane.showMessageDialog(this, "INGRESE EL NÚMERO DEL BUS");
//            } else if (txtAño.getText().isEmpty()) {
//                JOptionPane.showMessageDialog(this, "INGRESE EL AÑO DEL BUS");
//            } else if (txtEstadoBus.getText().isEmpty()) {
//                JOptionPane.showMessageDialog(this, "INGRESE EL ESTADO DEL BUS");
//            } else if (txtCapacidad.getText().isEmpty()) {
//                JOptionPane.showMessageDialog(this, "INGRESE LA CAPACIDAD DEL BUS");
//            } else if (txtEstado.getText().isEmpty()) {
//                JOptionPane.showMessageDialog(this, "INGRESE EL ESTADO DEL BUS");
//
//            } else {
//                conexion cc = new conexion();
//                Connection cn = cc.conectar();
//
//                String sql = "INSERT INTO BUS(COD_BUS, PLACA, NUM_BUS, ANIO, ESTADO_BUS, CAPACIDAD_BUS, ESTADO) VALUES(?,?,?,?,?,?,?)";
//                PreparedStatement psd = cn.prepareStatement(sql);
//
//                psd.setString(1, txtCodigoBus.getText());
//                psd.setString(2, txtPlaca.getText());
//                psd.setString(3, txtNumeroBus.getText());
//                psd.setString(4, txtAño.getText());
//                psd.setString(5, txtEstadoBus.getText());
//                psd.setString(6, txtCapacidad.getText());
//                psd.setString(7, txtEstado.getText());
//
//                int n = psd.executeUpdate();
//                if (n > 0) {
//                    JOptionPane.showMessageDialog(this, "Se Inserto corerctamente el Auto");
//                    limpiarTextos();
//                }
//            }
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(this, ex);
//        }
//
//    }
//
//    public void limpiarTextos() {
//        txtCodigoBus.setText(null);
//        txtPlaca.setText(null);
//        txtNumeroBus.setText(null);
//        txtAño.setText(null);
//        txtEstadoBus.setText(null);
//        txtCapacidad.setText(null);
//        txtEstado.setText(null);
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
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtCodigoBus = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtNumeroBus = new javax.swing.JTextField();
        txtAño = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cbxEstado = new javax.swing.JComboBox<>();
        cbxCapacidad = new javax.swing.JComboBox<>();
        txtPlaca = new javax.swing.JFormattedTextField();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnSalir1 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBuses = new javax.swing.JTable();
        txtPlacaB = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconifiable(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/bus.fw.png"))); // NOI18N

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setText("Codigo Bus");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setText("Placa");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setText("Número Bus");

        txtNumeroBus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNumeroBusKeyTyped(evt);
            }
        });

        txtAño.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAñoActionPerformed(evt);
            }
        });
        txtAño.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtAñoKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setText("Año");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setText("Capacidad");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel7.setText("Estado");

        cbxEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione", "Activo", "Inactivo" }));

        cbxCapacidad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione", "40", "42", "45", "48" }));

        try {
            txtPlaca.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("UUU-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

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

        btnModificar.setText("Actualizar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnBorrar.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnBorrar.setText("Eliminar");
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });

        btnSalir1.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnSalir1.setText("Salir");
        btnSalir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalir1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNumeroBus)
                            .addComponent(txtCodigoBus)
                            .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSalir1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(18, 18, 18))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbxEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbxCapacidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtAño, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 16, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtCodigoBus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(txtNumeroBus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtAño, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(66, 66, 66)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cbxCapacidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(cbxEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar)
                    .addComponent(btnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(btnBorrar)
                    .addComponent(btnSalir1))
                .addGap(13, 13, 13))
        );

        jPanel7.setBackground(new java.awt.Color(0, 153, 255));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("BUSES EXISTENTES"));

        tblBuses = new javax.swing.JTable(){
            public boolean isCellEditable(int f, int c){
                return ((c!=0) && (c!=1) && (c!=2)&&(c!=4) && (c!=5) && (c!=6));
            }
        };
        tblBuses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblBuses);

        try {
            txtPlacaB.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("UUU-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtPlacaB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPlacaBActionPerformed(evt);
            }
        });
        txtPlacaB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPlacaBKeyReleased(evt);
            }
        });

        jLabel10.setText("Buscar por Placa  :");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtPlacaB, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(556, 556, 556))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 679, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtPlacaB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(116, 116, 116))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        activarTextos();
        activarBotones();
        codigoBus();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardarBus();       
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        limpiarTextos();
        desactivarBotones();
        desactivarTextos();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnSalir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalir1ActionPerformed
        // TODO add your handling code here:
        this.dispose();
        //para cerrar la ventana
        //exit 0 se sale de todo el sistema
    }//GEN-LAST:event_btnSalir1ActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        // TODO add your handling code here:
        borrar();
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void txtAñoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAñoKeyTyped
        // TODO add your handling code here:
        controlAnio(evt);
    }//GEN-LAST:event_txtAñoKeyTyped

    private void txtNumeroBusKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroBusKeyTyped
        // TODO add your handling code here:
        controlNumeroBus(evt);
    }//GEN-LAST:event_txtNumeroBusKeyTyped

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            modificar();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(IngresoBus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void txtPlacaBKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlacaBKeyReleased
        // TODO add your handling code here:
        cargarTablaBus(txtPlacaB.getText());
    }//GEN-LAST:event_txtPlacaBKeyReleased

    private void txtAñoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAñoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAñoActionPerformed

    private void txtPlacaBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPlacaBActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaBActionPerformed

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
            java.util.logging.Logger.getLogger(IngresoBus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IngresoBus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IngresoBus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IngresoBus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IngresoBus().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir1;
    private javax.swing.JComboBox<String> cbxCapacidad;
    private javax.swing.JComboBox<String> cbxEstado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblBuses;
    private javax.swing.JTextField txtAño;
    private javax.swing.JTextField txtCodigoBus;
    private javax.swing.JTextField txtNumeroBus;
    private javax.swing.JFormattedTextField txtPlaca;
    private javax.swing.JFormattedTextField txtPlacaB;
    // End of variables declaration//GEN-END:variables
}
