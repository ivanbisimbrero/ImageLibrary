package com.iroyo.c2223.imageLibrary.images;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.iroyo.c2223.imageLibrary.exceptions.BadArgumentException;

/**
 * Clase que se encarga de analizar una galeria de imagenes que ha sido generada
 * para guardar las imagenes en una lista y que posteriormente su acceso sea de mayor 
 * facilidad para el usuario (aplicar filtros, ordenar, etc.).
 * @author ivano
 *
 */
public class ImageGallery {
	
	private Path location;
	private List<Image> images;
	
	public ImageGallery(Path location) throws BadArgumentException {
		
		this.location = location;
		this.images = ImageGalleryUtils.analizeGallery(location);
		
	}
	
	public List<Image> getImages() {
		return images;
	}
	
	public int getNumberOfImages() {
		return images.size();
	}
	
	public List<Image> sortByName(boolean ascending) {
		
		List<Image> imagesList = new ArrayList<Image>(images);
	    
	    if(ascending) {
	    	Collections.sort(imagesList, new Comparator<Image>() {

				@Override
				public int compare(Image o1, Image o2) {
					
					return -(o1.getName().compareTo(o2.getName()));
					
				}
		    	
		    });
	    }
	    else {
	    	Collections.sort(imagesList, new Comparator<Image>() {

				@Override
				public int compare(Image o1, Image o2) {
					
					return o1.getName().compareTo(o2.getName());
					
				}
		    	
		    });
	    }
	    
	    return imagesList;
	    
	}
	
	public List<Image> sortByHeight(boolean ascending) {
		
		List<Image> imagesList = new ArrayList<Image>(images);
		
		if(ascending) {
	    	Collections.sort(imagesList, new Comparator<Image>() {

				@Override
				public int compare(Image o1, Image o2) {
					
					return -(o1.getHeight()-o2.getHeight());
					
				}
		    	
		    });
	    }
	    else {
	    	Collections.sort(imagesList, new Comparator<Image>() {

				@Override
				public int compare(Image o1, Image o2) {
					
					return o1.getHeight()-o2.getHeight();
					
				}
		    	
		    });
	    }
		
		return imagesList;
		
	}
	
	public List<Image> sortByWidth(boolean ascending) {
		
		List<Image> imagesList = new ArrayList<Image>(images);
		
		if(ascending) {
	    	Collections.sort(imagesList, new Comparator<Image>() {

				@Override
				public int compare(Image o1, Image o2) {
					
					return -(o1.getWidth()-o2.getWidth());
					
				}
		    	
		    });
	    }
	    else {
	    	Collections.sort(imagesList, new Comparator<Image>() {

				@Override
				public int compare(Image o1, Image o2) {
					
					return o1.getWidth()-o2.getWidth();
					
				}
		    	
		    });
	    }
		
		return imagesList;
		
	}
	
	public List<Image> sortByDate(boolean ascending) {
		
		List<Image> imagesList = new ArrayList<Image>(images);
		
		if(ascending) {
	    	Collections.sort(imagesList, new Comparator<Image>() {

				@Override
				public int compare(Image o1, Image o2) {
					
					return -(o1.getCreationDate().compareTo(o2.getCreationDate()));
					
				}
		    	
		    });
	    }
	    else {
	    	Collections.sort(imagesList, new Comparator<Image>() {

				@Override
				public int compare(Image o1, Image o2) {
					
					return o1.getCreationDate().compareTo(o2.getCreationDate());
					
				}
		    	
		    });
	    }
		
		return imagesList;
	}
	
	public List<Image> sortByISO(boolean ascending) {
		
		List<Image> imagesList = new ArrayList<Image>(images);
		
		if(ascending) {
	    	Collections.sort(imagesList, new Comparator<Image>() {

				@Override
				public int compare(Image o1, Image o2) {
					
					return -(o1.getISO() - o2.getISO());
					
				}
		    	
		    });
	    }
	    else {
	    	Collections.sort(imagesList, new Comparator<Image>() {

				@Override
				public int compare(Image o1, Image o2) {
					
					return o1.getISO() - o2.getISO();
					
				}
		    	
		    });
	    }
		
		return imagesList;
	}
	
	public List<Image> filterByWidth(int widthFilter, boolean upTo) {
		
		List<Image> filteredImages = new ArrayList<Image>();
		
		if(upTo) {
			for(int i = 0; i < images.size(); i++) {
				if(images.get(i).getWidth() >= widthFilter) {
					filteredImages.add(images.get(i));
				}
			}
		}
		else {
			for(int i = 0; i < images.size(); i++) {
				if(images.get(i).getWidth() <= widthFilter) {
					filteredImages.add(images.get(i));
				}
			}
		}
		
		return filteredImages;
		
	}
	
	public List<Image> filterByHeight(int heightFilter, boolean upTo) {
		
		List<Image> filteredImages = new ArrayList<Image>();
		
		if(upTo) {
			for(int i = 0; i < images.size(); i++) {
				if(images.get(i).getHeight() >= heightFilter) {
					filteredImages.add(images.get(i));
				}
			}
		}
		else {
			for(int i = 0; i < images.size(); i++) {
				if(images.get(i).getHeight() <= heightFilter) {
					filteredImages.add(images.get(i));
				}
			}
		}
		
		return filteredImages;
		
	}
	
	public List<Image> filterByYear(int year, boolean upTo) {
		
		List<Image> filteredImages = new ArrayList<Image>();
		
		if(upTo) {
			for(int i = 0; i < images.size(); i++) {
				
				//Rescatamos el año de la fecha, que sabemos que es de la forma --> yyyy:MM:dd HH:mm:ss
				String sYear = ((images.get(i).getCreationDate().split(" "))[0].split(":"))[0];
				
				if(Integer.parseInt(sYear) >= year) {
					filteredImages.add(images.get(i));
				}
				
			}
		}
		else {
			for(int i = 0; i < images.size(); i++) {
				
				//Rescatamos el año de la fecha, que sabemos que es de la forma --> yyyy:MM:dd HH:mm:ss
				String sYear = ((images.get(i).getCreationDate().split(" "))[0].split(":"))[0];
				
				if(Integer.parseInt(sYear) <= year) {
					filteredImages.add(images.get(i));
				}
				
			}
		}
		
		return filteredImages;
		
	}
	
	public List<Image> filterByISO(int ISO, boolean upTo) {
		
		List<Image> filteredImages = new ArrayList<Image>();
		
		if(upTo) {
			for(int i = 0; i < images.size(); i++) {
				if(images.get(i).getISO() >= ISO) {
					filteredImages.add(images.get(i));
				}
			}
		}
		else {
			for(int i = 0; i < images.size(); i++) {
				if(images.get(i).getISO() <= ISO) {
					filteredImages.add(images.get(i));
				}
			}
		}
		
		return filteredImages;
		
	}
	
	public List<Image> filterByCamera(String cameraModel) {
		
		List<Image> filteredImages = new ArrayList<Image>();
		
		for(int i = 0; i < images.size(); i++) {
			if(images.get(i).getCameraModel().equals(cameraModel)) {
				filteredImages.add(images.get(i));
			}
		}
		
		return filteredImages;
		
	}

	@Override
	public String toString() {
		return "Galeria de imagenes: " + location + "\nContiene las siguientes imagenes (Numero de imagenes: "
				+ images.size() + "):\n\n" + images;
	}
	
}
