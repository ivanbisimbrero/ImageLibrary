package com.iroyo.c2223.imageLibrary.userInterface;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Vector;

import javax.swing.event.TableModelListener;
import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import com.iroyo.c2223.imageLibrary.directories.DirectoryUtils;
import com.iroyo.c2223.imageLibrary.exceptions.BadArgumentException;
import com.iroyo.c2223.imageLibrary.images.Image;
import com.iroyo.c2223.imageLibrary.images.ImageGallery;

public class DataModel extends AbstractTableModel implements TreeModel {
	
	private ImageGallery galery;
	private List<Image> imageContent;
	private List<Image> filterContent;
	
	private Path location;
	private Vector<TreeModelListener> treeModelListeners =
	        new Vector<TreeModelListener>();
	
	String[] columnNames = {"Name", "Width", "Height", "Location", "Creation Date", "Latitude",
			"Latitude Reference", "Longitude", "Longitude Reference", "Camera Model", "ISO"};
	
	public DataModel(Path location) throws BadArgumentException {
		
		this.location = location;
		galery = new ImageGallery(location);
		imageContent = galery.getImages();
		filterContent = galery.getImages();
		
	}
	
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return filterContent.size();
	}
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnNames.length;
	}
	@Override
	public String getColumnName(int columnIndex) {
		// TODO Auto-generated method stub
		return columnNames[columnIndex];
	}
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		// TODO Auto-generated method stub
		if(columnIndex == 1) return Integer.class;
		else if(columnIndex == 2) {return Integer.class;}
		else if(columnIndex == 3) {return Path.class;}
		else if(columnIndex == 5) {return Double.class;}
		else if(columnIndex == 7) {return Double.class;}
		else if(columnIndex == 10) {return Short.class;}
		else {return String.class;}
	}
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		Image im = filterContent.get(rowIndex);
		if(columnIndex == 0) {return im.getName();}
		else if(columnIndex == 1) {return im.getWidth();}
		else if(columnIndex == 2) {return im.getHeight();}
		else if(columnIndex == 3) {return im.getLocation();}
		else if(columnIndex == 4) {return im.getCreationDate();}
		else if(columnIndex == 5) {return im.getLatitude();}
		else if(columnIndex == 6) {return im.getLatReference();}
		else if(columnIndex == 7) {return im.getLongitude();}
		else if(columnIndex == 8) {return im.getLongReference();}
		else if(columnIndex == 9) {return im.getCameraModel();}
		else {return im.getISO();}
		
	}
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		super.addTableModelListener(l);
		
	}
	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		super.removeTableModelListener(l);
		
	}
	
	//Metodos para ordenar la lista de imagenes
	public void sortByName(boolean ascending) {
		filterContent = galery.sortByName(ascending);
		fireTableDataChanged();
	}
	
	public void sortByWidth(boolean ascending) {
		filterContent = galery.sortByWidth(ascending);
		fireTableDataChanged();
	}
	
	public void sortByHeight(boolean ascending) {
		filterContent = galery.sortByHeight(ascending);
		fireTableDataChanged();
	}
	
	public void sortByDate(boolean ascending) {
		filterContent = galery.sortByDate(ascending);
		fireTableDataChanged();
	}
	
	public void sortByISO(boolean ascending) {
		filterContent = galery.sortByISO(ascending);
		fireTableDataChanged();
	}
	
	//Metodos para filtrar la coleccion de imagenes
	public void filterByWidth(int widthFilter, boolean upTo) {
		filterContent = galery.filterByWidth(widthFilter, upTo);
		fireTableDataChanged();
	}
	
	public void filterByHeight(int heightFilter, boolean upTo) {
		filterContent = galery.filterByHeight(heightFilter, upTo);
		fireTableDataChanged();
	}
	
	public void filterByYear(int year, boolean upTo) {
		filterContent = galery.filterByYear(year, upTo);
		fireTableDataChanged();
	}
	
	public void filterByISO(int ISO, boolean upTo) {
		filterContent = galery.filterByISO(ISO, upTo);
		fireTableDataChanged();
	}
	
	public void filterByCamera(String cameraModel) {
		filterContent = galery.filterByCamera(cameraModel);
		fireTableDataChanged();
	}
	
	public void clearFiltersOrSorters() {
		filterContent = imageContent;
		fireTableDataChanged();
	}

	@Override
	public Object getRoot() {
		return location;
	}

	@Override
	public Object getChild(Object parent, int index) {
		// TODO Auto-generated method stub
		int directories = DirectoryUtils.countPathsInDirectoryStream((Path) parent);
		if(directories != 0) {
			Path current = (Path) parent;
			try {
				DirectoryStream<Path> directorios = Files.newDirectoryStream(current);
				int counter = 0;
				for(Path p : directorios) {
					if(counter == index) {return p;}
					counter++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		// TODO Auto-generated method stub
		return DirectoryUtils.countPathsInDirectoryStream((Path) parent);
	}

	@Override
	public boolean isLeaf(Object node) {
		// TODO Auto-generated method stub
		return !Files.isDirectory((Path) node);
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		// TODO Auto-generated method stub
		Path current = (Path) parent;
		try {
			DirectoryStream<Path> directorios = Files.newDirectoryStream(current);
			int counter = 0;
			for(Path p : directorios) {
				if(p.equals(child)) {return counter;}
				counter++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub
		treeModelListeners.addElement(l);
		
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub
		treeModelListeners.removeElement(l);
		
	}

}
