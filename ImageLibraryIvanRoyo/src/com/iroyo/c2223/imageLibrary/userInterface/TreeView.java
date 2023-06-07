package com.iroyo.c2223.imageLibrary.userInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.iroyo.c2223.imageLibrary.exceptions.BadArgumentException;

/**
 * Clase que se encarga de crear un arbol del directorio de imagenes que se ha analizado recientemente.
 * Utiliza la clase ImageView para mostrar las imagenes cuando se selecciona un nodo del arbol y es un
 * archivo (hojas del arbol), cuando se selecciona una carpeta no hace nada.
 * @author ivano
 *
 */
public class TreeView {
	
	private JTree tree;
	private DataModel data;
	private JScrollPane scroll;
	
	ImageView imageViewerReference;

	
	public void loadDataModel(DataModel dataModel) {
		
		this.data = dataModel;
		tree = new JTree(data);
		imageViewerReference = new ImageView();
		
	}
	
	public void refreshImageDirectory(Path newImageLocation) {
		try {
			this.data = new DataModel(newImageLocation);
			tree.setModel(data);
		} catch (BadArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void inicializeTree() {
		
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// TODO Auto-generated method stub
				Path p = (Path) tree.getLastSelectedPathComponent();
				if(p != null) {
					
					if(!Files.isDirectory(p)) {
						
						imageViewerReference.loadImage(p.toFile());
						
						SwingUtilities.invokeLater(new Runnable() {
				            public void run() {
				                JDialog dialog = new JDialog();
				                //Ponemos de titulo en el dialogo el nombre de la imagen
				                dialog.setTitle(((Path)(tree.getLastSelectedPathComponent())).toString());
				                
				                dialog.setLocationRelativeTo(null);
				                dialog.setModal(true);
				                
				                //Añadimos el panel que nos mostrara la imagen
				                dialog.getContentPane().add(imageViewerReference, BorderLayout.CENTER);
				                
				                //Finalmente lo hacemos visible para que el usuario lo pueda ver
				                BufferedImage read = null;
				                try {
									read = ImageIO.read(((Path)tree.getLastSelectedPathComponent()).toFile());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				                
				                dialog.setPreferredSize(new Dimension(read.getWidth(), read.getHeight()));
				                
				                dialog.pack();
				                dialog.setVisible(true);
				            }
				        });
						
					}
					
				}
				
			}
			
		});
		
		scroll = new JScrollPane(tree);
		
	}
	
	public JScrollPane getScrollPane() {
		return scroll;
	}

}
