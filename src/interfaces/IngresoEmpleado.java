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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ronni
 */
public class IngresoEmpleado extends javax.swing.JInternalFrame {

    /**
     * Creates new form IngresoEmpleado
     */
   DefaultTableModel modelo;

    public IngresoEmpleado() {
        initComponents();
        cargarComboPersonal();
        cargarTablaPersonal("");
        cargarModificar();
        desactivarBotones();
        bloquearCampos();

        // CONTROLAR LOS NOMBRES VECTOR Y  AL MOMENTO DE CARGAR   EN LOS CAMPOS TABLA/CAMPOS
    }

    public int edad() {
        Calendar fecha = new GregorianCalendar();

        int anio = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH);
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        // System.out.println(anio+ " " + mes+ " "+ dia );
        return Integer.valueOf(anio + " " + mes + " " + dia);
    }

    public void borrar() throws SQLException {

        if (JOptionPane.showConfirmDialog(new JInternalFrame(), "Estas seguro de borar el  registro", "Ventana Borrar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            String dato = "N";
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            String sql = "UPDATE PERSONAL set ESTADO='" + dato + "'WHERE CED_PER='" + txtCedula.getText() + "'";
            PreparedStatement psd = cn.prepareStatement(sql);
            int n = psd.executeUpdate();
            if (n > 0) {
                JOptionPane.showMessageDialog(this, "Borrado  exitosamente");
                cargarTablaPersonal("");
                limpiarCampos();
            }
            cn.close();
        }
    }

    public String cargarNA(String dato1, String dato2) {
        //  String nom1 = "";
        // String nom2 = "";  NOM1_PER,NOM2_PER
        try {

            String sql = "";
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            sql = "select " + dato1 + "," + dato2 + " from personal WHERE CED_PER = '" + txtCedula.getText() + "'";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);

            while (rs.next()) {
                //codmar.add(rs.getString("MAR_COD"));
                dato1 = rs.getString(dato1);
                dato2 = rs.getString(dato2);

            }

            cn.close();
            // Thread.sleep(150);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return dato1 + " " + dato2;
    }
 public String cargarClave() {
       String dato="";
        try {

            String sql = "";
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            sql = "select contrasena from personal WHERE CED_PER = '" + txtCedula.getText() + "'";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);

            while (rs.next()) {
                //codmar.add(rs.getString("MAR_COD"));
                dato = rs.getString("contrasena");
              

            }

            cn.close();
            // Thread.sleep(150);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return dato;
    }
    public Date cargarFecha(String dato) {
        Date fecha = null;
        try {

            String sql = "";
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            sql = "select " + dato + " from personal where CED_PER= '" + txtCedula.getText() + "'";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);

            while (rs.next()) {

                fecha = rs.getDate(dato);
            }

            cn.close();
            // Thread.sleep(150);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return fecha;
    }

    public void cargarModificar() {

        tblPersonal.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (tblPersonal.getSelectedRow() != -1) {
                    /// si no esta seleccioando  nada  en la  tabla
                    //  activarCampos();
                    //  botonActualizar();
                    int fila = tblPersonal.getSelectedRow();//  tomar la  posicion
                    txtCedula.setText(tblPersonal.getValueAt(fila, 0).toString().trim());
                    txtNombre.setText(cargarNA("NOM1_PER", "NOM2_PER"));
                    txtApellido.setText(cargarNA("APE1_PER", "APE2_PER"));
                    txtDireccion.setText(tblPersonal.getValueAt(fila, 4).toString().trim());
                    txtTelefono.setText(tblPersonal.getValueAt(fila, 5).toString().trim());
                    txtCorreo.setText(tblPersonal.getValueAt(fila, 6).toString().trim());
                    txtClave.setText(cargarClave());
                    jdcIngreso.setDate(cargarFecha("FEC_ING_PER"));
                    jdcNacimiento.setDate(cargarFecha("FEC_NAC_PER"));
                    cbxEmpleado.setSelectedItem(cargarComboCargoPersonal());
                }
                activarCampos();
                activarBotones();
                bloquearCed();
            }
        });
    }

    public String cargarComboCargoPersonal() {
        String personal = "";
        try {

            String sql = "";
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            sql = "select TIPO_PER \n"
                    + "FROM TIPO_PERSONAL\n"
                    + "WHERE COD_TIP_PER IN (SELECT TIPO_PER\n"
                    + "                       FROM PERSONAL \n"
                    + "                       WHERE CED_PER='" + txtCedula.getText() + "')";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);

            while (rs.next()) {
                //codmar.add(rs.getString("MAR_COD"));
                personal = rs.getString("TIPO_PER");

            }
            cn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        return personal;

    }

    public void cargarComboPersonal() {

        try {
            String personal;
            String sql = "";
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            sql = "select TIPO_PER from tipo_personal ";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);

            while (rs.next()) {
                //codmar.add(rs.getString("MAR_COD"));
                personal = rs.getString("TIPO_PER");
                cbxEmpleado.addItem(personal);
            }
            cn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    public String idTipoPersonal() throws ClassNotFoundException {
        String id = " ";
        try {

            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            String sql = "";
            sql = "SELECT COD_TIP_PER from tipo_personal where tipo_per= '" + cbxEmpleado.getSelectedItem() + "'";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                id = rs.getString("COD_TIP_PER");
            }
            cn.close();
        } catch (SQLException ex) {
            Logger.getLogger(IngresoEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
        return id;
    }

    public String fechaNacimiento() {
        String dia = Integer.toString(jdcNacimiento.getCalendar().get(Calendar.DAY_OF_MONTH));
        String mes = Integer.toString(jdcNacimiento.getCalendar().get(Calendar.MONTH) + 1);
        String year = Integer.toString(jdcNacimiento.getCalendar().get(Calendar.YEAR));
        String fecha = (dia + "-" + mes + "-" + year);
        return fecha;
    }

    public String fechaIngreso() {
        String dia = Integer.toString(jdcIngreso.getCalendar().get(Calendar.DAY_OF_MONTH));
        String mes = Integer.toString(jdcIngreso.getCalendar().get(Calendar.MONTH) + 1);
        String year = Integer.toString(jdcIngreso.getCalendar().get(Calendar.YEAR));
        String fecha = (dia + "-" + mes + "-" + year);
        return fecha;
    }

    public void actualizarPersonal() throws ClassNotFoundException {
        if (txtDireccion.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese Dirección...");
        } else if (txtCorreo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese Correo");
        } else if (txtTelefono.getText().isEmpty() || txtTelefono.getText().length() < 10) {
            JOptionPane.showMessageDialog(null, "Telefono Incorrecto");
        } else if (cbxEmpleado.getSelectedItem().equals("SELECCIONE")) {
            JOptionPane.showMessageDialog(null, "Escoja Tipo Empleado");
        } else {

            try {
                Conexion cc = new Conexion();
                Connection cn = cc.conexion();
                String sql = "";
                sql = "UPDATE personal set DIR_PER_PER ='" + txtDireccion.getText().toUpperCase() + "',"
                        + "MAIL_PER = '" + txtCorreo.getText().toLowerCase() + "',"
                        + "TELF_PER = '" + txtTelefono.getText() + "',"
                        + "TIPO_PER = '" + idTipoPersonal() + "'"
                        + "where CED_PER= '" + txtCedula.getText() + "'";
                PreparedStatement psd = cn.prepareStatement(sql);
                int n = psd.executeUpdate();
                if (n > 0) {
                    JOptionPane.showMessageDialog(this, "Modificado Exitosamente....");
                    cargarTablaPersonal("");
                    bloquearCampos();
                    limpiarCampos();
                    cn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(clientes.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void guardarPersonal() throws ClassNotFoundException {
        String estado = "S";
        if (txtCedula.getText().length() < 10) {
            JOptionPane.showMessageDialog(null, "Cédula Incorrecta");
            txtCedula.requestFocus();
        } else if (txtNombre.getText().isEmpty() || !txtNombre.getText().contains(" ")) {
            JOptionPane.showMessageDialog(null, "Ingrese Nombres...");
        } else if (txtApellido.getText().isEmpty() || !txtNombre.getText().contains(" ")) {
            JOptionPane.showMessageDialog(null, "Ingrese Apellidos...");
        } else if (jdcNacimiento.getCalendar() == null) {
            JOptionPane.showMessageDialog(null, "Falta Fecha Nacimiento...");
        } else if (jdcIngreso.getCalendar() == null) {
            JOptionPane.showMessageDialog(null, "Falta Fecha de Ingreso...");
        } else if (txtDireccion.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese Dirección...");
        } else if (txtCorreo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese Correo");
        } else if (txtTelefono.getText().isEmpty() || txtTelefono.getText().length() < 10) {
            JOptionPane.showMessageDialog(null, "Telefono Incorrecto");
        } else if (cbxEmpleado.getSelectedItem().equals("SELECCIONE")) {
            JOptionPane.showMessageDialog(null, "Escoja Tipo Empleado");
        } else {

            try {

                String sql = "";
                String cedula, direccion, telefono, mail, id_tipo_personal;
                String nombres = txtNombre.getText().toUpperCase();
                String apellidos = txtApellido.getText().toUpperCase();

                String[] sepNom = nombres.split(" ");
                String[] sepApe = apellidos.split(" ");

                String nom1 = sepNom[0];
                String nom2 = sepNom[1];
                String ape1 = sepApe[0];
                String ape2 = sepApe[1];
                char[] aux = txtClave.getPassword();
                String clave = new String(aux);

                direccion = txtDireccion.getText().toUpperCase();
                telefono = txtTelefono.getText();
                mail = txtCorreo.getText().toLowerCase();
                cedula = txtCedula.getText();
                id_tipo_personal = idTipoPersonal();
                Date fec1 = jdcIngreso.getDate();
                Date fec2 = jdcNacimiento.getDate();
                Conexion cc = new Conexion();
                Connection cn = cc.conexion();
                sql = "INSERT INTO personal(CED_PER,NOM1_PER,NOM2_PER,APE1_PER,APE2_PER,FEC_NAC_PER,FEC_ING_PER,DIR_PER_PER,TIPO_PER,TELF_PER,MAIL_PER,ESTADO,CONTRASENA)"
              + " values('" +cedula +"','" +nom1 +"','" +nom2 +"','" +ape1 +"','" +ape2 +"','" +fec2 +"','" +fec1 +"','" +direccion +"','" +id_tipo_personal +"','" +telefono +"','" +mail +"','" +estado +"','" +clave +"')";
                PreparedStatement psd = cn.prepareStatement(sql);

                int n = psd.executeUpdate();

                if (n > 0) {
                    JOptionPane.showMessageDialog(this, "Guardado Correctamente");
                }
                cn.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    public void cargarTablaPersonal(String dato) {
        try {
            String titulos[] = {"CÉDULA", "NOMBRE", "APELLIDO", "FECHA-INGRESO", "DIRECCIÓN", "TELEFONO", "CORREO"};
            String registro[] = new String[7];
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            String sql = "";

            sql = "SELECT CED_PER,NOM1_PER,APE1_PER,FEC_ING_PER,DIR_PER_PER,TELF_PER,MAIL_PER\n"
                    + "FROM PERSONAL\n"
                    + "WHERE ESTADO='S' and CED_PER LIKE'%" + dato + "%'";
            modelo = new DefaultTableModel(null, titulos);
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                registro[0] = rs.getString("CED_PER");
                registro[1] = rs.getString("NOM1_PER");
                registro[2] = rs.getString("APE1_PER");
                registro[3] = rs.getString("FEC_ING_PER");
                registro[4] = rs.getString("DIR_PER_PER");
                registro[5] = rs.getString("TELF_PER");
                registro[6] = rs.getString("MAIL_PER");
                modelo.addRow(registro);
            }
            tblPersonal.setModel(modelo);
            cn.close();
        } catch (SQLException ex) {
            Logger.getLogger(IngresoEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void limpiarCampos() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtDireccion.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
        txtClave.setText("");
        jdcIngreso.setDate(null);
        jdcNacimiento.setDate(null);
        cbxEmpleado.setSelectedIndex(0);
    }

    public void activarBotones() {
        btnNuevo.setEnabled(true);
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnBorrar.setEnabled(true);
        btnSalir.setEnabled(true);
        btnActualizar.setEnabled(true);
    }

    public void desactivarBotones() {
        btnNuevo.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(true);
        btnBorrar.setEnabled(false);
        btnSalir.setEnabled(true);
        btnActualizar.setEnabled(false);
    }

    public void bloquearCampos() {
        txtApellido.setEnabled(false);
        txtCedula.setEnabled(false);
        txtNombre.setEnabled(false);
        jdcIngreso.setEnabled(false);
        jdcNacimiento.setEnabled(false);
        txtCorreo.setEnabled(false);
        txtDireccion.setEnabled(false);
        txtTelefono.setEnabled(false);
        cbxEmpleado.setEnabled(false);
        txtClave.setEnabled(false);
    }

    public void activarCampos() {
        txtApellido.setEnabled(true);
        txtCedula.setEnabled(true);
        txtNombre.setEnabled(true);
        jdcIngreso.setEnabled(true);
        jdcNacimiento.setEnabled(true);
        txtCorreo.setEnabled(true);
        txtDireccion.setEnabled(true);
        txtTelefono.setEnabled(true);
        cbxEmpleado.setEnabled(true);
        txtClave.setEnabled(true);
    }

    public void bloquearCed() {
        txtApellido.setEnabled(false);
        txtCedula.setEnabled(false);
        txtNombre.setEnabled(false);
        jdcIngreso.setEnabled(false);
        jdcNacimiento.setEnabled(false);
        txtCorreo.setEnabled(true);
        txtDireccion.setEnabled(true);
        txtTelefono.setEnabled(true);
        cbxEmpleado.setEnabled(true);
        btnGuardar.setEnabled(false);
        txtClave.setEnabled(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPersonal = new javax.swing.JTable(){
            public boolean isCellEditable(int fila , int columna){
                return false;
            }
        };
        txtBuscar = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtApellido = new javax.swing.JTextField();
        cbxEmpleado = new javax.swing.JComboBox<>();
        txtCorreo = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        jLabel67 = new javax.swing.JLabel();
        jdcNacimiento = new com.toedter.calendar.JDateChooser();
        jdcIngreso = new com.toedter.calendar.JDateChooser();
        txtTelefono = new javax.swing.JFormattedTextField();
        txtCedula = new javax.swing.JFormattedTextField();
        jLabel68 = new javax.swing.JLabel();
        txtClave = new javax.swing.JPasswordField();
        jPanel2 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        tblPersonal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblPersonal);

        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setText("Buscar por cedula :");

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel58.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel58.setText("Cedula :");

        jLabel59.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel59.setText("Nombres :");

        jLabel60.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel60.setText("Apellidos :");

        jLabel61.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel61.setText("Fec. Nacimiento :");

        jLabel62.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel62.setText("Fec. Ingreso :");

        jLabel63.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel63.setText("Dirección :");

        jLabel64.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel64.setText("Teléfono :");

        jLabel65.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel65.setText("Correo :");

        jLabel66.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel66.setText("Tipo Empleado :");

        cbxEmpleado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONE" }));

        jLabel67.setFont(new java.awt.Font("Times New Roman", 1, 48)); // NOI18N
        jLabel67.setText("EMPLEADOS");

        jdcNacimiento.setMaxSelectableDate(new java.util.Date(1577858519000L));
        jdcNacimiento.setMinSelectableDate(new java.util.Date(-315597481000L));

        jdcIngreso.setMaxSelectableDate(new java.util.Date(1577858519000L));
        jdcIngreso.setMinSelectableDate(new java.util.Date(1262325719000L));

        txtTelefono.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("##########"))));

        try {
            txtCedula.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##########")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel68.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel68.setText("Contraseña:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel65)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel59)
                                .addComponent(jLabel60))
                            .addComponent(jLabel62)
                            .addComponent(jLabel58)
                            .addComponent(jLabel63)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel61)))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtNombre)
                                            .addComponent(txtApellido)
                                            .addComponent(jdcNacimiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jdcIngreso, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE))
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addGap(45, 45, 45)
                                                .addComponent(jLabel68)
                                                .addGap(24, 24, 24))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabel64)
                                                    .addComponent(jLabel66))
                                                .addGap(18, 18, 18))))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(txtCorreo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtDireccion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(167, 167, 167)))
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cbxEmpleado, 0, 144, Short.MAX_VALUE)
                                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtClave))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel67)
                        .addGap(211, 211, 211))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel67)
                .addGap(19, 19, 19)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel61)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel62))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel58)
                            .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel59))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel60))
                        .addGap(18, 18, 18)
                        .addComponent(jdcNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jdcIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbxEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel64)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel66)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel63)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel65)
                            .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel68)
                                .addComponent(txtClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(51, 153, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

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

        btnSalir.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnBorrar.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnBorrar.setText("Borrar");
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });

        btnActualizar.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnActualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 57, Short.MAX_VALUE)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar)
                    .addComponent(btnSalir)
                    .addComponent(btnBorrar)
                    .addComponent(btnActualizar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        cargarTablaPersonal(txtBuscar.getText());
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        limpiarCampos();
        activarCampos();
        activarBotones();
        txtCedula.requestFocus();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try {
            guardarPersonal();
            cargarTablaPersonal("");
            limpiarCampos();
            bloquearCampos();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(IngresoEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        limpiarCampos();
        desactivarBotones();
        bloquearCampos();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
        //para cerrar la ventana
        //exit 0 se sale de todo el sistema
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnBorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarActionPerformed
        try {
            borrar();
        } catch (SQLException ex) {
            Logger.getLogger(IngresoEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        try {
            actualizarPersonal();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(IngresoEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnActualizarActionPerformed

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
            java.util.logging.Logger.getLogger(IngresoEmpleado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IngresoEmpleado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IngresoEmpleado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IngresoEmpleado.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IngresoEmpleado().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnBorrar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cbxEmpleado;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdcIngreso;
    private com.toedter.calendar.JDateChooser jdcNacimiento;
    private javax.swing.JTable tblPersonal;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JFormattedTextField txtCedula;
    private javax.swing.JPasswordField txtClave;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JFormattedTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
