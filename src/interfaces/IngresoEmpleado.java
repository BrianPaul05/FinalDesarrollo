/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import Conexion.Conexion;
import com.placeholder.PlaceHolder;
import static interfaces.PrimeraInterface.ventanasAbiertas;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        cargarComboCiudadOficina();
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/EmpleadoReg.png")));
        this.setTitle("REGISTRAR EMPLEADOS");
        this.setIconifiable(true);
         PlaceHolder holder = new PlaceHolder(txtNom2, "Segundo Nombre Opcional");
        tblPersonal.getTableHeader().setEnabled(false);

        // CONTROLAR LOS NOMBRES VECTOR Y  AL MOMENTO DE CARGAR   EN LOS CAMPOS TABLA/CAMPOS
    }

//    public int edad() {
//        Calendar fecha = new GregorianCalendar();
//
//        int anio = fecha.get(Calendar.YEAR);
//        int mes = fecha.get(Calendar.MONTH);
//        int dia = fecha.get(Calendar.DAY_OF_MONTH);
//        // System.out.println(anio+ " " + mes+ " "+ dia );
//        return Integer.valueOf(anio + " " + mes + " " + dia);
//    }
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
                desactivarBotones();
            }
            cn.close();
        }
    }

    public static boolean validarEmailIntemedia(String email) {

        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (matcher.find()) {
            return matcher.matches();
        } else {
            return false;

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
        String dato = "";
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
                botonesActualizar();
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

    public int edadPesonal() {
        Calendar cal = Calendar.getInstance();
        String year = Integer.toString(jdcNacimiento.getCalendar().get(Calendar.YEAR));
        int anioActual = cal.get(Calendar.YEAR);
        return (anioActual - Integer.valueOf(year));
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
        } else if (txtClave.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese su Contraseña..");
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
                    desactivarBotones();
                    cn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(IngresoClientes.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void guardarPersonal() throws ClassNotFoundException {
        String estado = "S";
        if (txtCedula.getText().length() < 10) {
            JOptionPane.showMessageDialog(null, "Cédula Incorrecta..");
            txtCedula.requestFocus();
        } else if (txtNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese Nombres...");
            txtNombre.requestFocus();
        }else if (txtApellido.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese Primer Apellido...");
            txtApellido.requestFocus();
        } else if (txtApe2.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese Segundo Apellido...");
            txtApellido.requestFocus();
        } else if (jdcNacimiento.getCalendar() == null) {
            JOptionPane.showMessageDialog(null, "Falta Fecha Nacimiento...");
        } else if (edadPesonal() < 18) {
            JOptionPane.showMessageDialog(null, "Debe ser Mayor de Edad...");
        } else if (jdcIngreso.getCalendar() == null) {
            JOptionPane.showMessageDialog(null, "Falta Fecha de Ingreso...");
        } else if (txtDireccion.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese Dirección...");
            txtDireccion.requestFocus();
        } else if (txtCorreo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese Correo...");
            txtCorreo.requestFocus();
        } else if (validarEmailIntemedia(txtCorreo.getText()) == false) {
            JOptionPane.showMessageDialog(null, "Email Incorreto...");
            txtCorreo.requestFocus();
        } else if (txtTelefono.getText().isEmpty() || txtTelefono.getText().length() < 10) {
            JOptionPane.showMessageDialog(null, "Telefono Incorrecto...");
        } else if (cbxEmpleado.getSelectedItem().equals("SELECCIONE")) {
            JOptionPane.showMessageDialog(null, "Escoja Tipo Empleado..");
        } else if (txtClave.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese su Contraseña..");
        } else if (cbxCiudad.getSelectedItem().equals("SELECCIONE")) {
            JOptionPane.showMessageDialog(null, "Escoja la Ciudad");
        } else {

            try {
                String sql1 = "";
                String idOfi;
                idOfi = idOficina();
                String sql = "";
                String cedula, direccion, telefono, mail, id_tipo_personal;
               
               
                   String dato="";
                if(txtNom2.getText().isEmpty()){
                    dato="Ninguno";
                }else{
                    dato=txtNom2.getText();
                }

               
                    direccion = txtDireccion.getText().toUpperCase();
                    telefono = txtTelefono.getText();
                    mail = txtCorreo.getText().toLowerCase();
                    cedula = txtCedula.getText();
                    id_tipo_personal = idTipoPersonal();
                    Date fec1 = jdcIngreso.getDate();
                    Date fec2 = jdcNacimiento.getDate();
                    Conexion cc = new Conexion();
                    Connection cn = cc.conexion();
                    sql = "INSERT INTO personal(CED_PER, NOM1_PER,NOM2_PER,APE1_PER,APE2_PER,FEC_NAC_PER,FEC_ING_PER,DIR_PER_PER,TIPO_PER,TELF_PER,MAIL_PER,ESTADO,CONTRASENA)"
                            + " values('" + cedula + "','" + txtNombre.getText().toUpperCase() + "','" + dato.toUpperCase()+ "','" + txtApellido.getText().toUpperCase()+ "','" + txtApe2.getText().toUpperCase()+ "','" + fec2 + "','" + fec1 + "','" + direccion + "','" + id_tipo_personal + "','" + telefono + "','" + mail + "','" + estado + "','" + txtClave.getText() + "')";
                    sql1 = "INSERT INTO PERSONAL_OFICINA(CED_PERSONAL_PER, COD_OFI_PER) values('" + cedula + "','" + idOfi + "')";
                    PreparedStatement psd = cn.prepareStatement(sql);
                    PreparedStatement psd1 = cn.prepareStatement(sql1);
                    int n = psd.executeUpdate();
                    int n2 = psd1.executeUpdate();
                    if (n > 0) {
                        if (n2 > 0) {
                            JOptionPane.showMessageDialog(this, "Guardado Correctamente");
                            limpiarCampos();
                            bloquearCampos();
                            desactivarBotones();
                        }
                    }
                    cn.close();
                }catch (SQLException ex) {
                Logger.getLogger(IngresoEmpleado.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(IngresoEmpleado.class
                    .getName()).log(Level.SEVERE, null, ex);
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

    public String idOficina() throws ClassNotFoundException {
        String id = " ";
        try {

            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            String sql = "";
            sql = "SELECT COD_OFI from oficinas where ubicacion = '" + cbxCiudad.getSelectedItem() + "'";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                id = rs.getString("COD_OFI");
            }
            cn.close();
            return id;

        } catch (SQLException ex) {
            Logger.getLogger(IngresoEmpleado.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        //  System.out.println(id);
        return id;

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
        cbxCiudad.setSelectedIndex(0);
        txtApe2.setText("");
        txtNom2.setText("");
    }

    public void activarBotones() {
        btnNuevo.setEnabled(false);
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnBorrar.setEnabled(false);
        btnSalir.setEnabled(true);
        btnActualizar.setEnabled(false);
    }

    public void botonesActualizar() {
        btnNuevo.setEnabled(false);
        btnActualizar.setEnabled(true);
        btnBorrar.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(true);
    }

    public void desactivarBotones() {
        btnNuevo.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);
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
        cbxCiudad.setEnabled(false);
        txtApe2.setEnabled(false);
        txtNom2.setEnabled(false);
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
        cbxCiudad.setEnabled(true);
        txtApe2.setEnabled(true);
        txtNom2.setEnabled(true);
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
        cbxCiudad.setEnabled(true);
        txtApe2.setEnabled(false);
        txtNom2.setEnabled(false);
    }

    public void soloNumeros(java.awt.event.KeyEvent evt) {
        char c;
        c = evt.getKeyChar();
        if ((c >= 32 && c <= 47) || (c >= 58 && c <= 255)) {
            evt.consume();
            JOptionPane.showMessageDialog(this, "ERROR Ingrese solo Números");
        }
    }

    public void soloLetras(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();

        if ((c >= 33 && c <= 64) || (c >= 91 && c <= 96) || (c >= 123 && c <= 255)) {
            evt.consume();
            getToolkit().beep();
            JOptionPane.showMessageDialog(this, "Ingrese solo Letras", "Error", JOptionPane.ERROR_MESSAGE);
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

        jPanel4 = new javax.swing.JPanel();
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
        jdcNacimiento = new com.toedter.calendar.JDateChooser();
        jdcIngreso = new com.toedter.calendar.JDateChooser();
        jLabel68 = new javax.swing.JLabel();
        txtClave = new javax.swing.JPasswordField();
        txtCedula = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnBorrar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jLabel69 = new javax.swing.JLabel();
        cbxCiudad = new javax.swing.JComboBox<>();
        jLabel67 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        txtNom2 = new javax.swing.JTextField();
        txtApe2 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPersonal = new javax.swing.JTable(){
            public boolean isCellEditable(int fila , int columna){
                return false;
            }
        };

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconifiable(true);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel58.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel58.setText("Cedula :");

        jLabel59.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel59.setText("Nombre 1 :");

        jLabel60.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel60.setText("Apellido 1:");

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

        txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreKeyTyped(evt);
            }
        });

        txtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApellidoKeyTyped(evt);
            }
        });

        cbxEmpleado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONE" }));

        txtDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDireccionKeyTyped(evt);
            }
        });

        jdcNacimiento.setMaxSelectableDate(new java.util.Date(1577858519000L));
        jdcNacimiento.setMinSelectableDate(new java.util.Date(-315597481000L));

        jdcIngreso.setMaxSelectableDate(new java.util.Date(1577858519000L));
        jdcIngreso.setMinSelectableDate(new java.util.Date(1262325719000L));

        jLabel68.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel68.setText("Contraseña:");

        txtClave.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtClaveKeyTyped(evt);
            }
        });

        txtCedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCedulaKeyTyped(evt);
            }
        });

        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyTyped(evt);
            }
        });

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

        btnActualizar.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
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
        btnBorrar.setText("Eliminar");
        btnBorrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarActionPerformed(evt);
            }
        });

        btnSalir.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        jLabel69.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel69.setText("Oficina:");

        cbxCiudad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECCIONE" }));

        jLabel67.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel67.setText("Nombre 2 :");

        jLabel70.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel70.setText("Apellido 2:");

        txtNom2.setToolTipText("Opcional");
        txtNom2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNom2KeyTyped(evt);
            }
        });

        txtApe2.setToolTipText("");
        txtApe2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtApe2KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnActualizar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBorrar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(42, 42, 42)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel59)
                                                .addComponent(jLabel60))
                                            .addComponent(jLabel62)
                                            .addComponent(jLabel63)))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel61)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                                        .addComponent(jLabel68))
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jdcNacimiento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jdcIngreso, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(txtApellido, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                                                .addComponent(txtNombre, javax.swing.GroupLayout.Alignment.LEADING)))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel64)
                                            .addComponent(jLabel66)
                                            .addComponent(jLabel69)
                                            .addComponent(jLabel67)
                                            .addComponent(jLabel70)))))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(87, 87, 87)
                                .addComponent(jLabel65)
                                .addGap(18, 18, 18)
                                .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbxEmpleado, 0, 144, Short.MAX_VALUE)
                            .addComponent(txtClave)
                            .addComponent(txtTelefono)
                            .addComponent(cbxCiudad, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtNom2)
                            .addComponent(txtApe2)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addComponent(jLabel58)
                        .addGap(18, 18, 18)
                        .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel58)
                    .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel61)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel62))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel59)
                            .addComponent(jLabel67)
                            .addComponent(txtNom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel60)
                            .addComponent(jLabel70)
                            .addComponent(txtApe2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jdcNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jdcIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(cbxEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel64)
                            .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel66)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel63))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel68)
                                .addComponent(txtClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel65)
                    .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel69)
                    .addComponent(cbxCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar)
                    .addComponent(btnSalir)
                    .addComponent(btnBorrar)
                    .addComponent(btnActualizar))
                .addGap(22, 22, 22))
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

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("EMPLEADOS EXISTENTES"));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setText("Buscar por cedula :");

        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
        });

        tblPersonal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tblPersonal);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
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

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(IngresoEmpleado.class
                    .getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(IngresoEmpleado.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBorrarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        try {
            actualizarPersonal();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(IngresoEmpleado.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void txtClaveKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClaveKeyTyped
        if (txtClave.getPassword().length >= 4) {
            JOptionPane.showMessageDialog(this, "Error Máximo 4 Caracteres");
            evt.consume();
        }
    }//GEN-LAST:event_txtClaveKeyTyped

    private void txtCedulaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaKeyTyped
        if (txtCedula.getText().length() >= 10) {
            JOptionPane.showMessageDialog(this, "Error Máximo 10 Caracteres");
            evt.consume();
        }
        soloNumeros(evt);

    }//GEN-LAST:event_txtCedulaKeyTyped

    private void txtTelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyTyped

        if (txtTelefono.getText().length() >= 10) {
            JOptionPane.showMessageDialog(this, "Error Máximo 10 Caracteres");
            evt.consume();
        }
        soloNumeros(evt);
    }//GEN-LAST:event_txtTelefonoKeyTyped

    private void txtNombreKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreKeyTyped
        soloLetras(evt);
    }//GEN-LAST:event_txtNombreKeyTyped

    private void txtApellidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApellidoKeyTyped
        soloLetras(evt);
    }//GEN-LAST:event_txtApellidoKeyTyped

    private void txtDireccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyTyped
        soloLetras(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionKeyTyped

    private void txtNom2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNom2KeyTyped
        soloLetras(evt);
    }//GEN-LAST:event_txtNom2KeyTyped

    private void txtApe2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtApe2KeyTyped
        soloLetras(evt);        // TODO add your handling code here:
    }//GEN-LAST:event_txtApe2KeyTyped

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
            java.util.logging.Logger.getLogger(IngresoEmpleado.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IngresoEmpleado.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IngresoEmpleado.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IngresoEmpleado.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JComboBox<String> cbxCiudad;
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
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser jdcIngreso;
    private com.toedter.calendar.JDateChooser jdcNacimiento;
    private javax.swing.JTable tblPersonal;
    private javax.swing.JTextField txtApe2;
    private javax.swing.JTextField txtApellido;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCedula;
    private javax.swing.JPasswordField txtClave;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtNom2;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
