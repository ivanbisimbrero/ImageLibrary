package com.iroyo.c2223.imageLibrary.userInterface;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.iroyo.c2223.imageLibrary.exceptions.BadArgumentException;
import com.iroyo.c2223.imageLibrary.images.ImageGalleryUtils;
import com.iroyo.c2223.imageLibrary.images.ImageUtils;

/**
 * Clase que se encarga de crear y mostrar la interfaz grafica de la aplicacion "Galeria de Imagenes"
 * añadiendo todas sus correspondientes funcionalidades
 * @author ivano
 *
 */
public class GalleryInterface extends JFrame {
	
	private static final long serialVersionUID = 2583759612441683812L;
	
	private static final int FILTER_WIDTH_OPERATION = 0;
	private static final int FILTER_HEIGHT_OPERATION = 1;
	private static final int FILTER_YEAR_OPERATION = 2;
	private static final int FILTER_ISO_OPERATION = 3;
	
	private static final int DEFAULT_DIVIDER_SPLITPANE = 300;
	
	private static final double DIRECTORY_PROBABILITY = 0.5;
	private final Set<String> eventos = new HashSet<String>(Arrays.asList("boda", "niños", "vacaciones", "abuelos", "jaime"
			, "purillos", "contenidoProhibido"));
	
	private Path location;
    private JMenuBar menuBar;
    private JMenu toolsMenu;
    private JMenuItem analizeImageFolderMenuItem;
    private JMenuItem generateImageFolderMenuItem;
    
    private JMenu edition;
    private JMenuItem launchImageEditor;
    
    private JMenu sorters;
    private JMenuItem sortByName;
    private JMenuItem sortByWidth;
    private JMenuItem sortByHeight;
    private JMenuItem sortByDate;
    private JMenuItem sortByISO;
    private JMenuItem clearSorters;
    
    private JMenu filters;
    private JMenuItem filterByWidth;
    private JMenuItem filterByHeight;
    private JMenuItem filterByYear;
    private JMenuItem filterByISO;
    private JMenuItem filterByCamera;
    private JMenuItem clearFilters;
    
    private int minImages;
    private int maxImages;
    private int fromYear;
    private int maxImageDir;
    
    private TableView table;
    private DataModel data;
    private TreeView tree;
    
    private JSplitPane divider;
    
    private void inicializeTableAndTree() {
		
		table = new TableView();
		tree = new TreeView();
		
		try {
			data = new DataModel(Paths.get("default"));
		} catch (BadArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		table.loadDataModel(data);
		tree.loadDataModel(data);
		
		table.initializeTable();
		tree.inicializeTree();
    	
    }
    
    private void inicializePaneContentPane() {
    	
    	//Creamos un SpliPane con los dos componentes dentro
    	divider = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tree.getScrollPane(), table.getScrollPane());
    	divider.setDividerLocation(DEFAULT_DIVIDER_SPLITPANE);
    			
    	//Y lo añadimos a nuestro layout
    	getContentPane().add(divider);
    			
    }
    
    private void showIntegerDialogFilter(String title, int filterAction) {
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				JDialog window = new JDialog();
				
				window.setTitle(title);
		        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        //window.setSize(400, 200);

		        window.setLayout(new GridLayout(2,2));

		        JLabel filtro = new JLabel("  Introduzca el numero por el que quiere filtrar:");
		        JTextField textField = new JTextField(14);
		        JLabel textUpTo = new JLabel("Desea filtrar por encima del valor (no marcado: por debajo)");
		        JCheckBox upTo = new JCheckBox();
		        JButton button = new JButton("Aceptar");
		        button.addActionListener(new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent e) {
		                String input = textField.getText();
		                try {
		                    int number = Integer.parseInt(input);
		                    
		                    if(filterAction == GalleryInterface.FILTER_WIDTH_OPERATION) {
		                    	table.filterByWidth(number, upTo.isSelected());
		                    }
		                    else if(filterAction == GalleryInterface.FILTER_HEIGHT_OPERATION) {
		                    	table.filterByHeight(number, upTo.isSelected());
		                    }
		                    else if(filterAction == GalleryInterface.FILTER_YEAR_OPERATION) {
		                    	table.filterByYear(number, upTo.isSelected());
		                    }
		                    else if(filterAction == GalleryInterface.FILTER_ISO_OPERATION) {
		                    	table.filterByISO(number, upTo.isSelected());
		                    }
		                    window.dispose();
		                } catch (NumberFormatException ex) {
		                    JOptionPane.showMessageDialog(window,
		                            "El valor ingresado no es un número entero",
		                            "Error", JOptionPane.ERROR_MESSAGE);
		                    window.dispose();
		                }
		            }
		        });

		        window.getContentPane().add(filtro);
		        window.getContentPane().add(textField);
		        JPanel upToPanel = new JPanel();
		        upToPanel.add(textUpTo);
		        upToPanel.add(upTo);
		        window.getContentPane().add(upToPanel);
		        window.getContentPane().add(button);

		        window.pack();
		        window.setVisible(true);
				
			}
			
		});
		
	}
    
    private void inicializeAndAddMenu() {
    	
    	menuBar = new JMenuBar();
        toolsMenu = new JMenu("Tools");
        edition = new JMenu("Edition");
        sorters = new JMenu("Sorters");
        sortByName = new JMenuItem("Sort By Name");
        sortByWidth = new JMenuItem("Sort By Width");
        sortByHeight = new JMenuItem("Sort By Height");
        sortByDate = new JMenuItem("Sort By Date");
        sortByISO = new JMenuItem("Sort By ISO");
        clearSorters = new JMenuItem("Clear Sorters");
        filters = new JMenu("Filters");
        filterByWidth = new JMenuItem("Filter By Width");
        filterByHeight = new JMenuItem("Filter By Height");
        filterByYear = new JMenuItem("Filter By Year");
        filterByISO = new JMenuItem("Filter By ISO");
        filterByCamera = new JMenuItem("Filter By Camera Model");
        clearFilters = new JMenuItem("Clear Filters");
        launchImageEditor = new JMenuItem("Launch Image Painter");
        analizeImageFolderMenuItem = new JMenuItem("Analize Image Directory");
        generateImageFolderMenuItem = new JMenuItem("Generate Image Directory");
        
        launchImageEditor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
		            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }

		        SwingUtilities.invokeLater(new Runnable() {
		            @Override
		            public void run() {
		                new PaintSimulator().createAndShowGUI();
		            }
		        });
				
			}
			
		});
		
		analizeImageFolderMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
		    	int option = fileChooser.showDialog(fileChooser, "Analizar");
		    	
		    	if(option == JFileChooser.APPROVE_OPTION) {
		    		location = Paths.get(fileChooser.getSelectedFile().toURI());
		    		System.out.println(location);
		    		table.refreshImageDirectory(location);
		    		tree.refreshImageDirectory(location);
		    		JOptionPane.showMessageDialog(GalleryInterface.this, "Las imagenes han sido"
		    				+ " actualizadas!");
		    	}
				
			}
			
		});
		
		generateImageFolderMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				try {
		            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }

		        SwingUtilities.invokeLater(new Runnable() {
		            @Override
		            public void run() {
		            	JDialog window = new JDialog();
						GridBagLayout layout = new GridBagLayout();
						GridBagConstraints c = new GridBagConstraints();
						
						//Seteamos los valores por defecto de nuestras variables por si no tocamos el slider
						minImages = 3;
						maxImages = 4;
						fromYear = 2020;
						maxImageDir = 5;
						
						window.setTitle("Generar galeria de imagenes");
						window.setLayout(layout);
						window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						
						//Creamos los slider que nos van a hacer falta para gestionar la generacion de imagenes
						//Ponemos de valores por defecto los mismos valores que hemos definido anteriormente
						JSlider minImage = new JSlider(2, 6);
						minImage.setPaintLabels(true);
						minImage.setPaintTicks(true);
						minImage.setMinorTickSpacing(1);
						minImage.setMajorTickSpacing(1);
						minImage.setValue(3);
						minImage.addChangeListener(new ChangeListener() {

							@Override
							public void stateChanged(ChangeEvent e) {
								minImages = minImage.getValue();
								
							}
							
						});
						
						JSlider maxImage = new JSlider(3, 7);
						maxImage.setPaintLabels(true);
						maxImage.setPaintTicks(true);
						maxImage.setMinorTickSpacing(1);
						maxImage.setMajorTickSpacing(1);
						maxImage.setValue(4);
						maxImage.addChangeListener(new ChangeListener() {
							
							@Override
							public void stateChanged(ChangeEvent e) {
								maxImages = maxImage.getValue();
								
							}
							
						});
						
						JSlider years = new JSlider(2010, 2022);
						years.setPaintLabels(true);
						years.setPaintTicks(true);
						years.setMinorTickSpacing(1);
						years.setMajorTickSpacing(1);
						years.setValue(2020);
						years.addChangeListener(new ChangeListener() {

							@Override
							public void stateChanged(ChangeEvent e) {
								fromYear = years.getValue();
								
							}
							
						});
						
						JSlider imageDirectoryNumber = new JSlider(1, 7);
						imageDirectoryNumber.setPaintLabels(true);
						imageDirectoryNumber.setPaintTicks(true);
						imageDirectoryNumber.setMinorTickSpacing(1);
						imageDirectoryNumber.setMajorTickSpacing(1);
						imageDirectoryNumber.setValue(5);
						imageDirectoryNumber.addChangeListener(new ChangeListener() {

							@Override
							public void stateChanged(ChangeEvent e) {
								maxImageDir = imageDirectoryNumber.getValue();
								
							}
							
						});
						
						JButton aceptar = new JButton("Aceptar");
						aceptar.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								JFileChooser fileChooser = new JFileChooser();
								fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
								
						    	int option = fileChooser.showDialog(window, "Generar");
						    	
						    	if(option == JFileChooser.APPROVE_OPTION) {
						    		ImageGalleryUtils.generateGallery(Paths.get(fileChooser.getSelectedFile().toURI())
						    				, minImages, maxImages, fromYear, DIRECTORY_PROBABILITY
						    				, eventos, maxImageDir);
						    		JOptionPane.showMessageDialog(window, "Galeria generada correctamente");
						    	}
						    	
								window.dispose();
							}
							
						});
						
						c.gridx = 0;
						c.gridy = 0;
						c.weightx = 1;
						c.weighty = 1;
						c.fill = GridBagConstraints.BOTH;
						JLabel min = new JLabel("Minimo de imagenes");
						window.getContentPane().add(min, c);
						c.gridy = 1;
						window.getContentPane().add(minImage, c);
						
						c.gridy = 2;
						JLabel max = new JLabel("Maximo de imagenes");
						window.getContentPane().add(max, c);
						c.gridy = 3;
						window.getContentPane().add(maxImage, c);
						
						c.gridy = 4;
						JLabel desde = new JLabel("Desde el año que deseas generar imagenes");
						window.getContentPane().add(desde, c);
						c.gridy = 5;
						window.getContentPane().add(years, c);
						
						c.gridy = 6;
						JLabel imagesFolder = new JLabel("Maximo de directorios de imagenes por carpeta");
						window.getContentPane().add(imagesFolder, c);
						c.gridy = 7;
						window.getContentPane().add(imageDirectoryNumber, c);
						
						c.gridy = 8;
						window.getContentPane().add(aceptar, c);
						
						window.setPreferredSize(new Dimension(500, 500));
						window.pack();
						window.setVisible(true);
		            }
		        });
				
			}
			
		});
		
		sortByName.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int option = JOptionPane.showConfirmDialog(GalleryInterface.this, 
						"¿Desea ordenar ascendentemente?");
				if(option == JOptionPane.YES_OPTION) {table.sortByName(true);}
				else if(option == JOptionPane.NO_OPTION) {table.sortByName(false);}
				
			}
			
		});
		
		sortByWidth.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int option = JOptionPane.showConfirmDialog(GalleryInterface.this, 
						"¿Desea ordenar ascendentemente?");
				if(option == JOptionPane.YES_OPTION) {table.sortByWidth(true);}
				else if(option == JOptionPane.NO_OPTION) {table.sortByWidth(false);}
				
			}
			
		});
		
		sortByHeight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int option = JOptionPane.showConfirmDialog(GalleryInterface.this, 
						"¿Desea ordenar ascendentemente?");
				if(option == JOptionPane.YES_OPTION) {table.sortByHeight(true);}
				else if(option == JOptionPane.NO_OPTION) {table.sortByHeight(false);}
				
			}
			
		});
		
		sortByDate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int option = JOptionPane.showConfirmDialog(GalleryInterface.this, 
						"¿Desea ordenar ascendentemente?");
				if(option == JOptionPane.YES_OPTION) {table.sortByDate(true);}
				else if(option == JOptionPane.NO_OPTION) {table.sortByDate(false);}
				
				
			}
			
		});
		
		sortByISO.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int option = JOptionPane.showConfirmDialog(GalleryInterface.this, 
						"¿Desea ordenar ascendentemente?");
				if(option == JOptionPane.YES_OPTION) {table.sortByISO(true);}
				else if(option == JOptionPane.NO_OPTION) {table.sortByISO(false);}
				
			}
			
		});
		
		clearSorters.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				table.clearFiltersOrSorters();
				
			}
			
		});
		
		filterByWidth.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				showIntegerDialogFilter("Filter By Width", GalleryInterface.FILTER_WIDTH_OPERATION);
				
			}
			
		});
		
		filterByHeight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				showIntegerDialogFilter("Filter By Height", GalleryInterface.FILTER_HEIGHT_OPERATION);
				
			}
			
		});
		
		filterByYear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				showIntegerDialogFilter("Filter By Year", GalleryInterface.FILTER_YEAR_OPERATION);
				
			}
			
		});
		
		filterByISO.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				showIntegerDialogFilter("Filter By ISO", GalleryInterface.FILTER_ISO_OPERATION);
			}
			
		});
		
		filterByCamera.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						JDialog window = new JDialog();
						window.setTitle("Filter By Camera Model");
						window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						window.setLayout(new FlowLayout());
						
						JLabel cameraText = new JLabel("Elija el modelo de camara");
						JComboBox<String> cameras = new JComboBox<String>(ImageUtils.getCameraBrands());
						JButton aceptar = new JButton("Aceptar");
						
						aceptar.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								table.filterByCamera((String)cameras.getSelectedItem());
								window.dispose();
								
							}
							
						});
						
						window.getContentPane().add(cameraText);
						window.getContentPane().add(cameras);
						window.getContentPane().add(aceptar);
						
						window.pack();
						window.setVisible(true);
						
					}
					
				});
				
			}
			
		});
		
		clearFilters.addActionListener(clearSorters.getActionListeners()[0]);
		
		edition.add(launchImageEditor);
		toolsMenu.add(analizeImageFolderMenuItem);
		toolsMenu.add(generateImageFolderMenuItem);
		sorters.add(sortByName);
		sorters.add(sortByWidth);
		sorters.add(sortByHeight);
		sorters.add(sortByDate);
		sorters.add(sortByISO);
		sorters.add(clearSorters);
		filters.add(filterByWidth);
		filters.add(filterByHeight);
		filters.add(filterByYear);
		filters.add(filterByISO);
		filters.add(filterByCamera);
		filters.add(clearFilters);
		
		menuBar.add(toolsMenu);
		menuBar.add(sorters);
		menuBar.add(filters);
		menuBar.add(edition);
		
		setJMenuBar(menuBar);
    	
    }

	public void createAndShowGUI() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Biblioteca de Imagenes");
		
		//Inicializamos y añadimos todo al panel del contenido del frame
		inicializeTableAndTree();
		inicializePaneContentPane();
		
		//Inicializamos la barra de menu
		inicializeAndAddMenu();
		
		pack();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		
	}

}
