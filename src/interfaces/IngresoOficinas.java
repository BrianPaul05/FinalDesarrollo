/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import Conexion.Conexion;
import com.placeholder.PlaceHolder;
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
 * @author DELL GAMER
 */
public class IngresoOficinas extends javax.swing.JInternalFrame {

    DefaultTableModel model;
    String codigoOficinaTabla;

    /**
     * Creates new form IngresoClientes
     */
    public IngresoOficinas() {
        initComponents();
//        setLocationRelativeTo(this);
        PlaceHolder holder = new PlaceHolder(txtTelefono, "Teléfono con el codigo de la ciudad");
        CargarTablaOficinas("");
        Tabla_Oficina.getTableHeader().setReorderingAllowed(false);
        Tabla_Oficina.getTableHeader().setResizingAllowed(false);
        cargarModificarTabla();
        desactivarTextos();
        desactivarBotones();
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/oficinas.png")));
        this.setTitle("REGISTRO OFICINAS");
    }

    private void guardarOficina() {
        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();

            if (txtNombreOficina.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese el nombre de la oficina");
                txtNombreOficina.requestFocus();
            } else if (txtUbicacion.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese la ubicación de la oficina");
                txtUbicacion.requestFocus();
            } else if (txtTelefono.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese el telefono de la oficina");
                txtTelefono.requestFocus();
            } else if (txtTelefono.getText().length() < 9 || txtTelefono.getText().length() > 10) {
                JOptionPane.showMessageDialog(null, "El número de cifras del número de teléfono es incorrecto");
                txtTelefono.requestFocus();
            } else if (txtDireccion.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese la dirección de la oficina");
                txtDireccion.requestFocus();
            } else {
                String sql = "INSERT INTO OFICINAS (NOM_OFI, UBICACION, TELEFONO, ESTADO, DIRECCION) VALUES(?,?,?,?,?)";
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.setString(1, txtNombreOficina.getText());
                psd.setString(2, txtUbicacion.getText());
                psd.setString(3, txtTelefono.getText());
                psd.setString(4, "S");
                psd.setString(5, txtDireccion.getText());

                int n = psd.executeUpdate();
                if (n > 0) {
                    JOptionPane.showMessageDialog(null, "OFICINA GUARDADA CORRECTAMENTE");
                    desactivarBotones();
                    desactivarTextos();
                    limpiarTextos();
                    CargarTablaOficinas("");
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(IngresoOficinas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void limpiarTextos() {
        txtNombreOficina.setText(null);
        txtUbicacion.setText(null);
        txtTelefono.setText(null);
        txtDireccion.setText(null);
    }

    private void activarTextos() {
        txtNombreOficina.setEnabled(true);
        txtUbicacion.setEnabled(true);
        txtTelefono.setEnabled(true);
        txtDireccion.setEnabled(true);
    }

    private void desactivarTextos() {
        txtNombreOficina.setEnabled(false);
        txtUbicacion.setEnabled(false);
        txtTelefono.setEnabled(false);
        txtDireccion.setEnabled(false);
    }

    private void activarBotonesNuevo() {
        jButton_Nuevo.setEnabled(false);
        jButton_Guardar.setEnabled(true);
        jButton_Actualizar.setEnabled(false);
        jButton_Cancelar.setEnabled(true);
        jButton_Eliminar.setEnabled(false);
        jButton_Salir.setEnabled(false);
    }

    private void desactivarBotones() {
        jButton_Nuevo.setEnabled(true);
        jButton_Guardar.setEnabled(false);
        jButton_Actualizar.setEnabled(false);
        jButton_Cancelar.setEnabled(false);
        jButton_Eliminar.setEnabled(false);
        jButton_Salir.setEnabled(true);
    }

    private void botonesActualizar() {
        jButton_Nuevo.setEnabled(false);
        jButton_Guardar.setEnabled(false);
        jButton_Actualizar.setEnabled(true);
        jButton_Cancelar.setEnabled(true);
        jButton_Eliminar.setEnabled(true);
        jButton_Salir.setEnabled(true);
    }
   

    private void CargarTablaOficinas(String dato) {
        try {
            String[] titulos = {"CODIGO", "NOMBRE", "UBICACIÓN", "TELÉFONO", "DIRECCIÓN"};
            String[] registros = new String[5];
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            String sql = "";
            sql = "SELECT COD_OFI, "
                    + "NOM_OFI, "
                    + "UBICACION, "
                    + "TELEFONO, "
                    + "DIRECCION "
                    + "FROM OFICINAS "
                    + "WHERE UBICACION LIKE'%" + dato + "%' "
                    + "AND ESTADO = 'S'";
            model = new DefaultTableModel(null, titulos) {
                @Override
                public boolean isCellEditable(int filas, int columnas) {
                    if (columnas < 0) {
                        return true;
                    } else {
                        return false;
                    }
                }

            };
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                registros[0] = rs.getString("COD_OFI");
                registros[1] = rs.getString("NOM_OFI");
                registros[2] = rs.getString("UBICACION");
                registros[3] = rs.getString("TELEFONO");
                registros[4] = rs.getString("DIRECCION");
                model.addRow(registros);
            }
            Tabla_Oficina.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    private void actualizarOficina() {
        if (txtNombreOficina.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el nombre de la oficina");
            txtNombreOficina.requestFocus();
        } else if (txtUbicacion.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la ubicación");
            txtUbicacion.requestFocus();
        } else if (txtTelefono.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el teléfono");
            txtTelefono.requestFocus();
        } else if (txtTelefono.getText().length() < 9 || txtTelefono.getText().length() > 10) {
            JOptionPane.showMessageDialog(null, "El número de cifras del número de teléfono es incorrecto");
            txtTelefono.requestFocus();
        } else if (txtDireccion.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese una dirección");
            txtDireccion.requestFocus();
        } else {
            try {

                Conexion cc = new Conexion();
                Connection cn = cc.conexion();
                String sql = "";

                sql = "UPDATE OFICINAS set NOM_OFI ='" + txtNombreOficina.getText() + "' "
                        + ",UBICACION ='" + txtUbicacion.getText() + "' "
                        + ",TELEFONO ='" + txtTelefono.getText() + "' "
                        + ",DIRECCION ='" + txtDireccion.getText() + "' "
                        + " WHERE COD_OFI ='" + codigoOficinaTabla + "'";

                PreparedStatement psd = cn.prepareStatement(sql);
                int n = psd.executeUpdate();
                if (n > 0) {
                    JOptionPane.showMessageDialog(this, "SE ACTUALIZO LA OFICINA.");
                    desactivarBotones();
                    desactivarTextos();
                    limpiarTextos();
                    CargarTablaOficinas("");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }

    private boolean condEliOfi1(String codOficina) {
        /*
        Metodo que permite verificar si la oficina tiene o no tiene filas hijas ,
        en la tabla PERSONAL_OFICINA sino NO tiene filas hijas elimino el registro
        DE LA TABLA OFICINA, caso contrario NO.
         */
        int pos = 0;
        try {
            String sql = "";
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            sql = "SELECT * "
                    + "FROM PERSONAL_OFICINA "
                    + "WHERE COD_OFI_PER = '" + codOficina + "'";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);

            while (rs.next()) {
                pos = pos + 1;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        if (pos > 0) {
            return false;
        }
        return true;
    }

    private boolean condEliOfi2(String codOficina) {
        /*
        Metodo que permite verificar si la oficina tiene o no tiene filas hijas ,
        en la tabla RUTAS sino NO tiene filas hijas elimino el registro
        DE LA TABLA OFICINA, caso contrario NO.
         */
        int pos = 0;
        try {
            String sql = "";
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            sql = "SELECT * "
                    + "FROM RUTAS "
                    + "WHERE COD_OFI_ORI = '" + codOficina + "' "
                    + "OR COD_OFI_DES = '" + codOficina + "'";
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);

            while (rs.next()) {
                pos = pos + 1;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        if (pos > 0) {
            return false;
        }
        return true;
    }

    private void cargarModificarTabla() {
        Tabla_Oficina.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            //Override cuando se sobrecarga un metodo o sobrecarga de valores ---- se lo pone con el obejtivo de liberar memoria
            public void valueChanged(ListSelectionEvent e) {
                if (Tabla_Oficina.getSelectedRow() != -1) {
                    activarTextos();
                    botonesActualizar();
                    int fila = Tabla_Oficina.getSelectedRow();
                    codigoOficinaTabla = Tabla_Oficina.getValueAt(fila, 0).toString().trim();
                    //trim para los espacios en blanco                                       
                    txtNombreOficina.setText(Tabla_Oficina.getValueAt(fila, 1).toString().trim());
                    txtUbicacion.setText(Tabla_Oficina.getValueAt(fila, 2).toString().trim());
                    txtTelefono.setText(Tabla_Oficina.getValueAt(fila, 3).toString().trim());
                    txtDireccion.setText(Tabla_Oficina.getValueAt(fila, 4).toString().trim());

                }
            }
        });
    }

    private void eliminarOficina() {
        if (JOptionPane.showConfirmDialog(new JInternalFrame(),
                "¿ ESTA SEGURO DE BORRAR EL REGISTRO DE OFICINA ?",
                "Ventana Borrar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (condEliOfi1(codigoOficinaTabla) && condEliOfi2(codigoOficinaTabla)) {
                try {
                    Conexion cc = new Conexion();
                    Connection cn = cc.conexion();

                    String sql = "";
                    sql = "update OFICINAS set ESTADO = 'N' where COD_OFI='" + codigoOficinaTabla + "'";
                    PreparedStatement psd = cn.prepareStatement(sql);
                    int n = psd.executeUpdate();
                    if (n > 0) {
                        JOptionPane.showMessageDialog(this, "REGISTRO ELIMINADO . . . . !");
                        CargarTablaOficinas("");
                        limpiarTextos();
                        desactivarTextos();
                        desactivarBotones();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, ex);
                }

            } else {
                JOptionPane.showMessageDialog(null, "NO SE PUEDE ELIMINAR EL REGISTRO . . . ! "
                        + "PRIMERO DEBE ELIMINAR LAS FILAS DEPENDIENTES DE LA TABLA OFICINA !");
            }
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
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla_Oficina = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtNombreOficina = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtUbicacion = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        jButton_Nuevo = new javax.swing.JButton();
        jButton_Guardar = new javax.swing.JButton();
        jButton_Actualizar = new javax.swing.JButton();
        jButton_Eliminar = new javax.swing.JButton();
        jButton_Salir = new javax.swing.JButton();
        jButton_Cancelar = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconifiable(true);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/oficina.png"))); // NOI18N

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("OFICINAS EXISTENTES"));

        Tabla_Oficina = new javax.swing.JTable(){
            public boolean isCellEditable(int f, int c){
                return ((c!=0) && (c!=1) && (c!=2)&&(c!=3) && (c!=4));
            }
        };
        Tabla_Oficina.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(Tabla_Oficina);

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setText("Buscar por ubicación");

        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtBuscarKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(0, 153, 255));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setText("Nombre de la oficina :");

        txtNombreOficina.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreOficinaKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setText("Teléfono :");

        txtTelefono.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTelefonoFocusLost(evt);
            }
        });
        txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setText("Ubicación :");

        txtUbicacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtUbicacionKeyTyped(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setText("Dirección :");

        txtDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDireccionKeyTyped(evt);
            }
        });

        jButton_Nuevo.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        jButton_Nuevo.setText("Nuevo");
        jButton_Nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_NuevoActionPerformed(evt);
            }
        });

        jButton_Guardar.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        jButton_Guardar.setText("Guardar");
        jButton_Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_GuardarActionPerformed(evt);
            }
        });

        jButton_Actualizar.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        jButton_Actualizar.setText("Actualizar");
        jButton_Actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ActualizarActionPerformed(evt);
            }
        });

        jButton_Eliminar.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        jButton_Eliminar.setText("Eliminar");
        jButton_Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EliminarActionPerformed(evt);
            }
        });

        jButton_Salir.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        jButton_Salir.setText("Salir");
        jButton_Salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SalirActionPerformed(evt);
            }
        });

        jButton_Cancelar.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        jButton_Cancelar.setText("Cancelar");
        jButton_Cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtUbicacion)
                            .addComponent(txtNombreOficina, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTelefono)
                            .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton_Nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_Guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jButton_Actualizar)
                        .addGap(28, 28, 28)
                        .addComponent(jButton_Cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jButton_Eliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jButton_Salir, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNombreOficina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtUbicacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_Nuevo)
                    .addComponent(jButton_Guardar)
                    .addComponent(jButton_Actualizar)
                    .addComponent(jButton_Eliminar)
                    .addComponent(jButton_Salir)
                    .addComponent(jButton_Cancelar))
                .addContainerGap())
        );

        txtTelefono.getAccessibleContext().setAccessibleName("");
        txtTelefono.getAccessibleContext().setAccessibleDescription("");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addGap(273, 273, 273))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_GuardarActionPerformed
        guardarOficina();
    }//GEN-LAST:event_jButton_GuardarActionPerformed

    private void txtNombreOficinaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreOficinaKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            String cad = ("" + c).toUpperCase();
            c = cad.charAt(0);
            evt.setKeyChar(c);
        }
    }//GEN-LAST:event_txtNombreOficinaKeyTyped

    private void txtUbicacionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUbicacionKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            String cad = ("" + c).toUpperCase();
            c = cad.charAt(0);
            evt.setKeyChar(c);
        }
    }//GEN-LAST:event_txtUbicacionKeyTyped

    private void txtTelefonoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoKeyTyped
        char validar = evt.getKeyChar();
        if(txtTelefono.getText().length()>=10){
             JOptionPane.showMessageDialog(this,"Teléfono Incorrecto");
             evt.consume();
        } else if (Character.isLetter(validar)) {
            getToolkit().beep();
            evt.consume();
            JOptionPane.showMessageDialog(rootPane, "SOLO NÚMEROS POR FAVOR", "Advertencia", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_txtTelefonoKeyTyped

    private void txtDireccionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            String cad = ("" + c).toUpperCase();
            c = cad.charAt(0);
            evt.setKeyChar(c);
        }
    }//GEN-LAST:event_txtDireccionKeyTyped

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        CargarTablaOficinas(txtBuscar.getText());
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void txtBuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyTyped
        char c = evt.getKeyChar();
        if (Character.isLowerCase(c)) {
            String cad = ("" + c).toUpperCase();
            c = cad.charAt(0);
            evt.setKeyChar(c);
        }
    }//GEN-LAST:event_txtBuscarKeyTyped

    private void jButton_ActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ActualizarActionPerformed
        actualizarOficina();
    }//GEN-LAST:event_jButton_ActualizarActionPerformed

    private void jButton_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EliminarActionPerformed
        eliminarOficina();
    }//GEN-LAST:event_jButton_EliminarActionPerformed

    private void jButton_NuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_NuevoActionPerformed
        activarBotonesNuevo();
        activarTextos();
        limpiarTextos();
    }//GEN-LAST:event_jButton_NuevoActionPerformed

    private void jButton_CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CancelarActionPerformed
        limpiarTextos();
        desactivarTextos();
        desactivarBotones();
        Tabla_Oficina.clearSelection();
    }//GEN-LAST:event_jButton_CancelarActionPerformed

    private void txtTelefonoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelefonoFocusLost

    }//GEN-LAST:event_txtTelefonoFocusLost

    private void jButton_SalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton_SalirActionPerformed

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
            java.util.logging.Logger.getLogger(IngresoOficinas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IngresoOficinas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IngresoOficinas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IngresoOficinas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IngresoOficinas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabla_Oficina;
    private javax.swing.JButton jButton_Actualizar;
    private javax.swing.JButton jButton_Cancelar;
    private javax.swing.JButton jButton_Eliminar;
    private javax.swing.JButton jButton_Guardar;
    private javax.swing.JButton jButton_Nuevo;
    private javax.swing.JButton jButton_Salir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtNombreOficina;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtUbicacion;
    // End of variables declaration//GEN-END:variables

}
