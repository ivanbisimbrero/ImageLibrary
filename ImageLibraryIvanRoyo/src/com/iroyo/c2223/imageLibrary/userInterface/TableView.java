package com.iroyo.c2223.imageLibrary.userInterface;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;
import java.util.Comparator;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.iroyo.c2223.imageLibrary.exceptions.BadArgumentException;
import com.iroyo.c2223.imageLibrary.images.Image;

/**
 * Clase que se encarga de crear una tabla del directorio de imagenes que se ha analizado recientemente.
 * Utiliza la clase ImageView para mostrar las imagenes cuando se le da doble click sobre la fila de la
 * tabla en la que esta la imagen que se desea mostrar.
 * @author ivano
 *
 */
public class TableView {
	
	private JTable table;
	private DataModel data;
	private JScrollPane scroll;
	
	ImageView imageViewerReference;
	
	int row;
	
	public TableView() {
		imageViewerReference = new ImageView();
	}
	
	public void loadDataModel(DataModel dataModel) {
		
		this.data = dataModel;
		table = new JTable(data);
		
	}
	
	public void refreshImageDirectory(Path newImageLocation) {
		try {
			this.data = new DataModel(newImageLocation);
			table.setModel(data);
		} catch (BadArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ImageView getImageViewer() {
		return imageViewerReference;
	}
	
	public void initializeTable() {
		
		//Al hacer doble click, obtenemos el valor de una fila de la tabla:
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Point point = e.getPoint();
				row = table.rowAtPoint(point);
				if(e.getClickCount() == 2) {
					System.out.println(table.getValueAt(row, 3));
					imageViewerReference.loadImage(((Path) table.getValueAt(row, 3)).toFile());
					
					SwingUtilities.invokeLater(new Runnable() {
			            public void run() {
			                JDialog dialog = new JDialog();
			                //Ponemos de titulo en el dialogo el nombre de la imagen
			                dialog.setTitle(((Path) table.getValueAt(row, 3)).toString());
			                
			                dialog.setLocationRelativeTo(null);
			                dialog.setModal(true);
			                
			                //Añadimos el panel que nos mostrara la imagen
			                dialog.getContentPane().add(imageViewerReference, BorderLayout.CENTER);
			                
			                //Añadimos un par de botones y los añadimos al dialogo
			                JButton next = new JButton("Next");
			                JButton previous = new JButton("Previous");
			                
			                next.addActionListener(new ActionListener() {

								@SuppressWarnings("deprecation")
								@Override
								public void actionPerformed(ActionEvent e) {
									// TODO Auto-generated method stub
									if(row + 1 < table.getRowCount()) {
										row++;
										imageViewerReference.loadImage(((Path) 
												table.getValueAt(row, 3)).toFile());
										dialog.resize(new Dimension((int)table.getValueAt(row, 1) + 
						                		next.getWidth() + previous.getWidth(), 
						                		(int)table.getValueAt(row, 2)));
										dialog.setTitle(((Path) table.getValueAt(row, 3)).toString());
									}
								}
			                	
			                });
			                
			                previous.addActionListener(new ActionListener() {

								@SuppressWarnings("deprecation")
								@Override
								public void actionPerformed(ActionEvent e) {
									// TODO Auto-generated method stub
									if(row - 1 >= 0) {
										row--;
										imageViewerReference.loadImage(((Path) 
												table.getValueAt(row, 3)).toFile());
										dialog.resize(new Dimension((int)table.getValueAt(row, 1) + 
						                		next.getWidth() + previous.getWidth(), 
						                		(int)table.getValueAt(row, 2)));
									}
								}
			                	
			                });
			                
			                next.setSize(new Dimension(50, (int)table.getValueAt(row, 2)));
			                previous.setSize(new Dimension(50, (int)table.getValueAt(row, 2)));
			                
			                dialog.getContentPane().add(next, BorderLayout.EAST);
			                dialog.getContentPane().add(previous, BorderLayout.WEST);
			                
			                dialog.setPreferredSize(new Dimension((int)table.getValueAt(row, 1) + 
			                		next.getWidth() + previous.getWidth(), 
			                		(int)table.getValueAt(row, 2)));
			                
			                //Finalmente lo hacemos visible para que el usuario lo pueda ver
			                dialog.pack();
			                dialog.setVisible(true);
			            }
			        });
				}
			}
		});
		
		scroll = new JScrollPane(table);
		
		//c.add(scroll,BorderLayout.CENTER);
	}
	
	public JScrollPane getScrollPane() {
		return scroll;
	}
	
	public void sortByName(boolean ascending) {
		data.sortByName(ascending);
	}
	
	public void sortByWidth(boolean ascending) {
		data.sortByWidth(ascending);
	}
	
	public void sortByHeight(boolean ascending) {
		data.sortByHeight(ascending);
	}
	
	public void sortByDate(boolean ascending) {
		data.sortByDate(ascending);
	}
	
	public void sortByISO(boolean ascending) {
		data.sortByISO(ascending);
	}
	
	public void filterByWidth(int widthFilter, boolean upTo) {
		data.filterByWidth(widthFilter, upTo);
	}
	
	public void filterByHeight(int heightFilter, boolean upTo) {
		data.filterByHeight(heightFilter, upTo);
	}
	
	public void filterByYear(int year, boolean upTo) {
		data.filterByYear(year, upTo);
	}
	
	public void filterByISO(int ISO, boolean upTo) {
		data.filterByISO(ISO, upTo);
	}
	
	public void filterByCamera(String cameraModel) {
		data.filterByCamera(cameraModel);
	}
	
	public void clearFiltersOrSorters() {
		data.clearFiltersOrSorters();
	}

}
