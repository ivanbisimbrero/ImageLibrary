package com.iroyo.c2223.imageLibrary.userInterface;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Clase que se utiliza para mostrar la imagen, respetando sus correspondientes tamaños a la hora
 * de ser mostrada por pantalla.
 * @author ivano
 *
 */
public class ImageView extends JPanel {
	
	private BufferedImage image;

	@SuppressWarnings("deprecation")
	public void loadImage(File imageFile) {
        try {
            image = ImageIO.read(imageFile);
            repaint(); // Vuelve a pintar el panel para mostrar la nueva imagen
            setSize(new Dimension(image.getWidth(), image.getHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
        }
    }

}
