    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import Conexion.Conexion;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gus tavo
 */
public class Rutas extends javax.swing.JInternalFrame {

    DefaultTableModel modelo;

    /**
     * Creates new form Rutas
     */
    public Rutas() {
        initComponents();
        cargarOrigen();
        cargarTabla();
        limitarLetras(precio, 5);
        limitarLetras(descripcion, 50);
        desctivarBotones();
        desactivarTexto();
        cargarModificar();
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/rutas.png")));
        this.setTitle("REGISTRO RUTAS");
    }

    public void activarTexto() {
        descripcion.setEnabled(true);
        origen.setEnabled(true);
        destino.setEnabled(true);
        precio.setEnabled(true);
    }

    public void activarBotones() {
        Nuevo.setEnabled(false);
        Guardar.setEnabled(true);
        actualizar.setEnabled(false);
        Borrar.setEnabled(false);
        Cancelar.setEnabled(true);
        Salir.setEnabled(true);
    }

    public void desactivarTexto() {
        origen.setEnabled(false);
        destino.setEnabled(false);
        precio.setEnabled(false);
        descripcion.setEnabled(false);
    }

    public void desctivarBotones() {
        Nuevo.setEnabled(true);
        Nuevo.requestFocus();
        Guardar.setEnabled(false);
        Borrar.setEnabled(false);
        actualizar.setEnabled(false);
        Cancelar.setEnabled(false);
    }

    private void limpiarcampos() {
        origen.setSelectedIndex(0);
        precio.setText("");
        descripcion.setText("");
    }

    public void guardarRutas() {
        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            String anio, descrip;
            float costo;
            int orige, dest;

            if (origen.getSelectedItem().equals("Selecione...")) {
                JOptionPane.showMessageDialog(null, "Selecionar origen de la ruta");
                origen.requestFocus();
            } else if (destino.getSelectedItem().equals("Selecione...")) {
                JOptionPane.showMessageDialog(null, "Selecionar destino de la ruta");
                destino.requestFocus();
            } else if (precio.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese el telefono de la oficina");
                precio.requestFocus();
            } else {
                orige = obtenerCodCiudad(origen.getSelectedItem().toString());
                dest = obtenerCodCiudad(destino.getSelectedItem().toString());
                costo = Float.valueOf(precio.getText());
                descrip = descripcion.getText();
                String sql = "Insert into rutas (COD_OFI_ORI, COD_OFI_DES, PRECIO, DESCRIPCION, ESTADO ) values(?,?,?,?,?)";
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.setInt(1, orige);
                psd.setInt(2, dest);
                psd.setFloat(3, costo);
                psd.setString(4, descrip);
                psd.setString(5, "A");
                int n = psd.executeUpdate();
                if (n > 0) {
                    JOptionPane.showMessageDialog(null, "RUTA GUARDADA CORRECTAMENTE");
                    limpiarcampos();
                    desactivarTexto();
                    desctivarBotones();
                    cargarTabla();
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al insertar los datos " + ex);

        }
    }

    public int obtenerCodCiudad(String nombreCiudad) {
        try {
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            String sql = "Select cod_ofi FROM oficinas WHERE ubicacion='" + nombreCiudad + "'";
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            return rs.getInt("cod_ofi");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener el codigo de la ciudad " + ex);
        }
        return 0;
    }

    public void cargarTabla() {
        Conexion cc = new Conexion();
        Connection cn = cc.conexion();
        String[] titulos = {"Código", "Origen", "Destino", "Precio", "Descripcion"};
        modelo = new DefaultTableModel(null, titulos);
        String[] atributos = new String[5];
        String sql = "select r.cod_ruta, o.ubicacion as origen, o1.ubicacion as destino, r.precio, r.descripcion \n"
                + "from rutas r, oficinas o, oficinas o1 \n"
                + "where r.cod_ofi_ori = o.cod_ofi \n"
                + "and r.cod_ofi_des = o1.cod_ofi "
                + "and r.estado = 'A' "
                + "order by cod_ruta asc";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                atributos[0] = rs.getString("cod_ruta");
                atributos[1] = rs.getString("origen");
                atributos[2] = rs.getString("destino");
                atributos[3] = rs.getString("precio");
                atributos[4] = rs.getString("descripcion");
                modelo.addRow(atributos);
            }
            rutas.setModel(modelo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Eror de ejecucion" + e);
        }
    }

    public void cargarDestino() {
        if (origen.getSelectedItem().equals("Selecione...")) {
            destino.removeAllItems();
        } else {
            try {
                destino.removeAllItems();
                Conexion cc = new Conexion();
                Connection cn = cc.conexion();
                String sql = "Select ubicacion from oficinas where ubicacion <> '" + origen.getSelectedItem() + "'";
                String nom_ciu;
                Statement psd = cn.createStatement();
                ResultSet rs = psd.executeQuery(sql);
                destino.addItem("Selecione...");
                while (rs.next()) {
                    nom_ciu = rs.getString("ubicacion");
                    destino.addItem(nom_ciu);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error:" + ex);
            }
        }
    }

    private void botonesActualizar() {
        Nuevo.setEnabled(false);
        actualizar.setEnabled(true);
        Borrar.setEnabled(true);
        Guardar.setEnabled(false);
        Cancelar.setEnabled(true);
    }

    public void cargarModificar() {
        rutas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                activarTexto();
                codigo.setEnabled(false);
                origen.setEnabled(false);
                destino.setEnabled(false);
                botonesActualizar();
                int fila = rutas.getSelectedRow();
                if (fila != -1) {
                    codigo.setText(rutas.getValueAt(fila, 0).toString().trim());
                    origen.setSelectedItem(rutas.getValueAt(fila, 1).toString().trim());
                    destino.setSelectedItem(rutas.getValueAt(fila, 2));
                    precio.setText(rutas.getValueAt(fila, 3).toString().trim());
                    descripcion.setText(rutas.getValueAt(fila, 4).toString());
                }
            }
        });
    }

    public void modificar() {
        if (origen.getSelectedItem().equals("Selecione...")) {
            JOptionPane.showMessageDialog(null, "Selecionar origen de la ruta");
            origen.requestFocus();
        } else if (destino.getSelectedItem().equals("Selecione...")) {
            JOptionPane.showMessageDialog(null, "Selecionar destino de la ruta");
            destino.requestFocus();
        } else if (precio.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese el telefono de la oficina");
            precio.requestFocus();
        } else {
            float costo;
            int cod;
            String desc;
            cod = Integer.valueOf(codigo.getText().toString());
            costo = Float.valueOf(precio.getText());
            desc = descripcion.getText();
            Conexion cc = new Conexion();
            Connection cn = cc.conexion();
            String sql = "update rutas set precio=" + costo + ","
                    + "descripcion=' " + desc + " ' "
                    + "where cod_ruta = " + cod + " ";
            try {
                PreparedStatement psd = cn.prepareStatement(sql);
                int n = psd.executeUpdate();
                if (n != 0) {
                    JOptionPane.showMessageDialog(this, "Se actualizo con éxito");
                    cargarTabla();
                    limpiarcampos();
                    desactivarTexto();
                    desctivarBotones();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al actualizar" + e);
            }
        }
    }

    public void cargarOrigen() {
        Conexion cc = new Conexion();
        Connection cn = cc.conexion();
        try {
            origen.addItem("Selecione...");
            String sql = "Select ubicacion from oficinas";
            String nom_ciu;
            Statement psd = cn.createStatement();
            ResultSet rs = psd.executeQuery(sql);
            while (rs.next()) {
                nom_ciu = rs.getString("ubicacion");
                origen.addItem(nom_ciu);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error:" + ex);
        }
    }

    public void limitarLetras(final JTextField txt, final int tamaño) {
        txt.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                int cant = txt.getText().length();
                if (cant >= tamaño) {
                    e.consume();
                }
            }
        });
    }

    public void validarSoloNumeros(JTextField txt, KeyEvent evt) {
        if (!Character.isDigit(evt.getKeyChar()) && (evt.getKeyChar() != '.')) {
            evt.consume();
        }
        if (0 == txt.getText().indexOf('.')) {
            evt.consume();
        }
    }

    public void borrar() {
        Conexion cc = new Conexion();
        Connection cn = cc.conexion();
        String sql = "update rutas \n"
                + "set estado = 'D'\n"
                + "where cod_ruta = " + Integer.valueOf(codigo.getText().toString()) + "";
        if (JOptionPane.showConfirmDialog(this, "Desea borrar?") == 0) {
            try {
                PreparedStatement psd = cn.prepareStatement(sql);
                int n = psd.executeUpdate();
                if (n != 0) {
                    cargarTabla();
                    limpiarcampos();
                    desctivarBotones();
                    desactivarTexto();
                    JOptionPane.showMessageDialog(this, "Datos borrados");
                }
            } catch (SQLException ex) {
                JOptionPane.showConfirmDialog(this, "Error al eliminar" + ex);
            }
        } else {
            cargarTabla();
            limpiarcampos();
            desctivarBotones();
            desactivarTexto();
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

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        origen = new javax.swing.JComboBox<>();
        destino = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        precio = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        descripcion = new javax.swing.JTextField();
        actualizar = new javax.swing.JButton();
        Nuevo = new javax.swing.JButton();
        Guardar = new javax.swing.JButton();
        Cancelar = new javax.swing.JButton();
        Salir = new javax.swing.JButton();
        Borrar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        codigo = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        rutas = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconifiable(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel8.setText("Origen :");

        origen.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                origenItemStateChanged(evt);
            }
        });
        origen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                origenMouseEntered(evt);
            }
        });
        origen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                origenActionPerformed(evt);
            }
        });
        origen.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                origenPropertyChange(evt);
            }
        });

        destino.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                destinoMouseEntered(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel9.setText("Destino :");

        precio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                precioMouseEntered(evt);
            }
        });
        precio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                precioKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setText("Precio :");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel6.setText("Descripción :");

        descripcion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                descripcionMouseEntered(evt);
            }
        });
        descripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                descripcionKeyTyped(evt);
            }
        });

        actualizar.setText("Actualizar");
        actualizar.setFocusPainted(false);
        actualizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                actualizarMouseEntered(evt);
            }
        });
        actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actualizarActionPerformed(evt);
            }
        });

        Nuevo.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        Nuevo.setText("Nuevo");
        Nuevo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                NuevoMouseEntered(evt);
            }
        });
        Nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NuevoActionPerformed(evt);
            }
        });

        Guardar.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        Guardar.setText("Guardar");
        Guardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                GuardarMouseEntered(evt);
            }
        });
        Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GuardarActionPerformed(evt);
            }
        });

        Cancelar.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        Cancelar.setText("Cancelar");
        Cancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                CancelarMouseEntered(evt);
            }
        });
        Cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelarActionPerformed(evt);
            }
        });

        Salir.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        Salir.setText("Salir");
        Salir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SalirMouseEntered(evt);
            }
        });
        Salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SalirActionPerformed(evt);
            }
        });

        Borrar.setFont(new java.awt.Font("Palatino Linotype", 0, 14)); // NOI18N
        Borrar.setText("Eliminar");
        Borrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BorrarMouseEntered(evt);
            }
        });
        Borrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BorrarActionPerformed(evt);
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
                        .addComponent(Nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(Guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jLabel8))
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(104, 104, 104)
                                        .addComponent(actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(11, 11, 11)
                                        .addComponent(Cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                        .addComponent(origen, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel9)))
                                .addGap(3, 3, 3)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(Borrar, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(3, 3, 3)
                                        .addComponent(Salir, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(destino, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(precio, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(origen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(destino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Nuevo)
                    .addComponent(Guardar)
                    .addComponent(Salir)
                    .addComponent(Cancelar)
                    .addComponent(Borrar)
                    .addComponent(actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37))
        );

        jPanel2.setBackground(new java.awt.Color(0, 153, 255));

        codigo.setFont(new java.awt.Font("Tahoma", 0, 1)); // NOI18N
        codigo.setForeground(new java.awt.Color(0, 153, 255));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 13, Short.MAX_VALUE)
                .addComponent(codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("RUTAS EXISTENTES"));

        rutas = new javax.swing.JTable(){
            public boolean isCellEditable(int f, int c){
                return ((c!=0) && (c!=1) && (c!=2)&& (c!=3)&& (c!=4));
            }
        };
        rutas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        rutas.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(rutas);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void origenItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_origenItemStateChanged
        // TODO add your handling code here:
        cargarDestino();
    }//GEN-LAST:event_origenItemStateChanged

    private void origenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_origenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_origenActionPerformed

    private void origenPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_origenPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_origenPropertyChange

    private void precioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_precioKeyTyped
        // TODO add your handling code here:
        validarSoloNumeros(precio, evt);
    }//GEN-LAST:event_precioKeyTyped

    private void descripcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descripcionKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_descripcionKeyTyped

    private void actualizarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_actualizarMouseEntered
        // TODO add your handling code here:
        actualizar.setToolTipText("Actualizar campos");
    }//GEN-LAST:event_actualizarMouseEntered

    private void actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualizarActionPerformed
        // TODO add your handling code here:
        modificar();
    }//GEN-LAST:event_actualizarActionPerformed

    private void NuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NuevoActionPerformed
        activarBotones();
        activarTexto();
    }//GEN-LAST:event_NuevoActionPerformed

    private void GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GuardarActionPerformed
        guardarRutas();
    }//GEN-LAST:event_GuardarActionPerformed

    private void CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelarActionPerformed
        limpiarcampos();
        rutas.clearSelection();
        desctivarBotones();
        desactivarTexto();
    }//GEN-LAST:event_CancelarActionPerformed

    private void SalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_SalirActionPerformed

    private void BorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BorrarActionPerformed
        // TODO add your handling code here:
        borrar();
    }//GEN-LAST:event_BorrarActionPerformed

    private void origenMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_origenMouseEntered
        // TODO add your handling code here:
        origen.setToolTipText("Selecionar origen de ruta");
    }//GEN-LAST:event_origenMouseEntered

    private void destinoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_destinoMouseEntered
        // TODO add your handling code here:
        destino.setToolTipText("Selecionar destino de ruta");
    }//GEN-LAST:event_destinoMouseEntered

    private void precioMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_precioMouseEntered
        // TODO add your handling code here:
        precio.setToolTipText("Ingresar costo de nueva ruta ingresada");
    }//GEN-LAST:event_precioMouseEntered

    private void descripcionMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_descripcionMouseEntered
        // TODO add your handling code here:
        descripcion.setToolTipText("Describir caracteristicas especificas de nueva ruta");
    }//GEN-LAST:event_descripcionMouseEntered

    private void NuevoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NuevoMouseEntered
        // TODO add your handling code here:
        Nuevo.setToolTipText("Ingresar  nueva ruta");
    }//GEN-LAST:event_NuevoMouseEntered

    private void GuardarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GuardarMouseEntered
        // TODO add your handling code here:
        Guardar.setToolTipText("Almacenar datos de nueva ruta");
    }//GEN-LAST:event_GuardarMouseEntered

    private void CancelarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CancelarMouseEntered
        // TODO add your handling code here:
        Cancelar.setToolTipText("Cancelar ingreso de nueva ruta");
    }//GEN-LAST:event_CancelarMouseEntered

    private void SalirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SalirMouseEntered
        // TODO add your handling code here:
        Salir.setToolTipText("Cerrar ventana de rutas");
    }//GEN-LAST:event_SalirMouseEntered

    private void BorrarMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BorrarMouseEntered
        // TODO add your handling code here:
        Borrar.setToolTipText("Seleccionar primero la fila a eliminar de la Tabla");
    }//GEN-LAST:event_BorrarMouseEntered

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
            java.util.logging.Logger.getLogger(Rutas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Rutas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Rutas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Rutas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Rutas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Borrar;
    private javax.swing.JButton Cancelar;
    private javax.swing.JButton Guardar;
    private javax.swing.JButton Nuevo;
    private javax.swing.JButton Salir;
    private javax.swing.JButton actualizar;
    private javax.swing.JLabel codigo;
    private javax.swing.JTextField descripcion;
    private javax.swing.JComboBox<String> destino;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> origen;
    private javax.swing.JTextField precio;
    private javax.swing.JTable rutas;
    // End of variables declaration//GEN-END:variables
}
