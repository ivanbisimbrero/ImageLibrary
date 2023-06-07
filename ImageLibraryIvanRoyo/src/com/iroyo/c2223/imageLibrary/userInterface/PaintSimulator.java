package com.iroyo.c2223.imageLibrary.userInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;

import com.iroyo.c2223.imageLibrary.images.ImageUtils;

/**
 * Clase que extiende de un dialogo que sirve para crear y mostrar un simulador de la herramienta
 * Paint de windows (unicamente hace lineas cuando se presiona el raton). La clase esta adaptada a
 * la creacion de imagenes con metadatos legibles para la clase Image del proyecto.
 * @author ivano
 *
 */
public class PaintSimulator extends JDialog {
	
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem createImageMenuItem;
    private JMenuItem openImageMenuItem;
    private JMenuItem saveMenuItem;
    private JColorChooser colorChooser;
    
    private File selectedFile;

    private JPanel canvas;
    
    private GridBagLayout layout;

    private boolean painting;
    private int startPointX;
    private int startPointY;
    private int lastX;
    private int lastY;
    private Color currentColor;

    private BufferedImage image;
    private Graphics2D graphics;
    
    private int widthTam;
    private int heightTam;
    
    /**
     * Funcion que se encarga de crear una imagen en un path elegido por el usuario mediante un
     * JFileChooser.
     */
    private void createNewImage() {
    	
    	SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				JDialog window = new JDialog();
				window.setTitle("Elegir tamaño imagen");
				window.setLayout(new GridLayout(5,1));
				window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
				JLabel textoAncho = new JLabel("Ancho de imagen");
				JLabel textoAlto = new JLabel("Alto de imagen");
				
				JSlider ancho = new JSlider(300, 800);
				ancho.setPaintLabels(true);
				ancho.setPaintTicks(true);
				ancho.setMinorTickSpacing(100);
				ancho.setMajorTickSpacing(100);
				ancho.setValue(500);
				widthTam = 500; //Default value
				ancho.addChangeListener(new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						widthTam = ancho.getValue();
						
					}
					
				});
				
				JSlider alto = new JSlider(300, 600);
				alto.setPaintLabels(true);
				alto.setPaintTicks(true);
				alto.setMinorTickSpacing(100);
				alto.setMajorTickSpacing(100);
				alto.setValue(500);
				heightTam = 500; //Default value
				ancho.addChangeListener(new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						heightTam = alto.getValue();
						
					}
					
				});
				
				JButton aceptar = new JButton("Aceptar");
				aceptar.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						JFileChooser fileChooser = new JFileChooser();
				    	int option = fileChooser.showDialog(window, "Crear");
				    	
				    	if(option == JFileChooser.APPROVE_OPTION) {
				    		
				    		selectedFile = fileChooser.getSelectedFile();
				    		fileChooser.setFileFilter(new FileNameExtensionFilter("JPEG Image", "jpg",
				    				"jpeg"));
				    		
				    		if(!Files.exists(Paths.get(selectedFile.getPath()))) {
				    			
				    			try {
									Files.createFile(Paths.get(selectedFile.getPath()));
									//Aqui se añadirian los metadatos en la practica, antes de leer la imagen
									image = new BufferedImage(widthTam, heightTam, 
											BufferedImage.TYPE_INT_RGB);
								} catch (IOException ex) {
									// TODO Auto-generated catch block
									ex.printStackTrace();
								}
				    			
				                graphics = image.createGraphics();
				                graphics.setColor(Color.WHITE);
				        		graphics.fillRect(0, 0, widthTam, heightTam);
				        		try {
									ImageIO.write(image, "jpg", selectedFile);
									ImageUtils.editMetadata(selectedFile.toPath());
									
									//Volvemos a leer la imagen
									image = ImageIO.read(selectedFile);
					                graphics = image.createGraphics();
					                canvas.repaint();
					                setTitle("Paint Simulator - " + selectedFile.getName());
								} catch (IOException ex) {
									// TODO Auto-generated catch block
									ex.printStackTrace();
								} catch (ImageReadException ex) {
									// TODO Auto-generated catch block
									ex.printStackTrace();
								} catch (ImageWriteException ex) {
									// TODO Auto-generated catch block
									ex.printStackTrace();
								}
				        		
				        		window.dispose();
				    			
				    		}
				    		else {
				    			JOptionPane.showMessageDialog(fileChooser, "Error: el archivo especificado"
				    					+ " ya existe");
				    			window.dispose();
				    		}
				    		
				    	}
						
					}
					
				});
				
				window.getContentPane().add(textoAncho);
				window.getContentPane().add(ancho);
				window.getContentPane().add(textoAlto);
				window.getContentPane().add(alto);
				window.getContentPane().add(aceptar);
				
				window.pack();
				window.setVisible(true);
				
			}
    		
    	});
    	
    }
    
    /**
     * Funcion que se encarga de abrir una imagen y mostrarla para su edicion en la herramienta
     */
    private void openImage() {
    	
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("JPEG Image", "jpg", "jpeg"));
        
        int option = fileChooser.showOpenDialog(PaintSimulator.this);
        
        if (option == JFileChooser.APPROVE_OPTION) {
        	
            selectedFile = fileChooser.getSelectedFile();
            
            try {
                image = ImageIO.read(selectedFile);
                graphics = image.createGraphics();
                canvas.repaint();
                setTitle("Paint Simulator - " + selectedFile.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(PaintSimulator.this, "Error opening image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        }
        
    }
    
    /**
     * Funcion que se encarga de guardar la imagen que ha sido editada en la herramienta. A la hora
     * de guardarla, se encarga de que sus metadatos sean los que requiere la clase Image del proyecto
     */
    private void saveImage() {
    	
    	int option = JOptionPane.showConfirmDialog(this, "Estas seguro de que deseas guardar?");
    	
    	if(option == JOptionPane.YES_OPTION) {
    		
    		if(selectedFile != null) {
    			
            	try {
    				ImageIO.write(image, "jpg", selectedFile);
    				ImageUtils.editMetadata(selectedFile.toPath());
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (ImageReadException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ImageWriteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            }
    		
    		JOptionPane.showMessageDialog(this, "Imagen guardada correctamente");
    	}
        
        
    }
    
    /**
     * Funcion que se encarga de crear y mostrar la GUI del usuario, añadiendo todas las funcionalidades
     * que tiene la utilidad a un menu.
     */
    public void createAndShowGUI() {
    	
    	//Preparamos nuestro GridBagLayout
    	layout = new GridBagLayout();
    	setLayout(layout);
    	GridBagConstraints c = new GridBagConstraints();
    	
    	setTitle("Editor de Imagenes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 900));
        
        colorChooser = new JColorChooser();
        colorChooser.setPreviewPanel(new JPanel());
        menuBar = new JMenuBar();
        fileMenu = new JMenu("Vamos a pintar");
        createImageMenuItem = new JMenuItem("Crear nueva imagen");
        openImageMenuItem = new JMenuItem("Abrir imagen");
        saveMenuItem = new JMenuItem("Guardar");
        currentColor = Color.WHITE;
        
        //Adaptamos el JPanel para que se pueda pintar sobre el
        canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (image != null) {
                	// Obtener el ancho y alto del panel
                    int panelWidth = getWidth();
                    int panelHeight = getHeight();

                    // Obtener el ancho y alto de la imagen
                    int imageWidth = image.getWidth(this);
                    int imageHeight = image.getHeight(this);

                    // Calcular las coordenadas de dibujo en el centro del panel
                    startPointX = (panelWidth - imageWidth) / 2;
                    startPointY = (panelHeight - imageHeight) / 2;

                    // Dibujar la imagen en el centro del panel
                    g.drawImage(image, startPointX, startPointY, null);
                }
            }
        };

        //Creamos los listener que nos permitiran dibujar trazas sobre nuestra imagen
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                painting = true;
                lastX = e.getX() - startPointX;
                lastY = e.getY() - startPointY;
                canvas.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                painting = false;
                canvas.repaint();
            }
        });

        canvas.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (painting) {
                    int x = e.getX() - startPointX;
                    int y = e.getY() - startPointY;
                    graphics.setColor(currentColor);
                    graphics.drawLine(lastX, lastY, x, y);
                    lastX = x;
                    lastY = y;
                    canvas.repaint();
                }
            }
        });

        //Finalmente, seteamos las correspondientes acciones en los items de nuestro menu
        createImageMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				createNewImage();
			}
        	
        });
        
        openImageMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openImage();
            }
        });

        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveImage();
            }
        });
        
        //Añadimos un change listener para que nos cambie el color
        colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				currentColor = colorChooser.getColor();
			}
        	
        });
        
        //Añadimos todo al menu y a la barra de menu, y mostramos la interfaz (lo mismo de siempre)
        fileMenu.add(createImageMenuItem);
        fileMenu.add(openImageMenuItem);
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
        
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 2;
		c.weighty = 2;
		c.fill = GridBagConstraints.BOTH;
        getContentPane().add(canvas, c);
        
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.1;
		c.weighty = 0.1;
        getContentPane().add(colorChooser, c);
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
    }

}