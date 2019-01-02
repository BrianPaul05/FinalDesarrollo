/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.border.Border;

/**
 *
 * @author jaadc
 */
public class ImagenFondo implements Border {

    private BufferedImage imagen;

    public ImagenFondo() {
         try {
            URL imagePath = new URL(getClass().getResource("../Imagenes/fondo.jpg").toString());
            imagen = ImageIO.read(imagePath);
        } catch (Exception ex) {            
        }
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        int x0 = x + (w - imagen.getWidth()) / 2;
        int y0 = y + (h - imagen.getHeight()) / 2;
        g.drawImage(imagen, x0, y0, null);
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(0, 0, 0, 0);
    }

    public boolean isBorderOpaque() {
        return true;
    }

}
