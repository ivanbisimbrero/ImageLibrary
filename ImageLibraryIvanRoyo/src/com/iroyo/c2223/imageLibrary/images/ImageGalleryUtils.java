package com.iroyo.c2223.imageLibrary.images;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;

import com.iroyo.c2223.imageLibrary.directories.DirectoryUtils;
import com.iroyo.c2223.imageLibrary.exceptions.BadArgumentException;

public class ImageGalleryUtils {
	
	private static int numberOfImages = 0;
	
	//Image info
	public static final List<Integer> imageSizes = (List<Integer>) Arrays.asList(300, 350, 400, 
			450, 500, 550, 600, 650);
	private static final int MAX_PRIMITIVES = 30;
	private static final int MIN_PRIMITIVES = 15;
	
	/**
	 * Funcion que se encarga de analizar un path en concreto con la finalidad de rescatar todas las
	 * imagenes de se vaya encontrando a su paso. Si hay cualquier otro tipo de archivo (.txt, .png, etc)
	 * el analizador lo ignorara y no lo añadira a la lista que devuelve.
	 * @param location: Path que se desea analizar
	 * @return ArrayList que contendra todas las imagenes encontradas en la carpeta especificada
	 * @throws BadArgumentException si el path especificado no existe
	 */
	public static ArrayList<Image> analizeGallery(Path location) throws BadArgumentException {
		
		if(!Files.exists(location)) {
			
			throw new BadArgumentException("El path especificado no existe");
			
		}

		return galleryAnalizer(location, new ArrayList<Image>());
		
	}
	
	private static ArrayList<Image> galleryAnalizer(Path location, ArrayList<Image> images) {
			
		//Llamamos al recolector de basura para que no explote
		System.gc();
			
		DirectoryStream<Path> directories = null;
	    try {
	        directories = Files.newDirectoryStream(location);
	    } catch (IOException e) {
	        	e.printStackTrace();
		}
		    
		for(Path folder : directories) {
		    	
			if(Files.isDirectory(folder)) {
		    		
		    	images = galleryAnalizer(folder, images);
		    		
		    }
	    	else {
	    		
	    		if((folder.getFileName().toString().split("\\."))[1].equals("jpg")) {
	    			images.add(new Image(folder));
	    		}
	    		
	    	}
	    	
	    }

	    return images;
	}
	
	
	private static int getRandomNumberInRange(int min, int max) {
	    Random rand = new Random();
	    return rand.nextInt((max - min) + 1) + min;
	}
	
	/**
	 * Funcion que se encarga de generar una galeria de imagenes en el Path especificado. La funcion
	 * generara una estructura de carpetas aleatoria y posteriormente generara imagenes en las carpetas
	 * de MAYOR PROFUNDIDAD de la estructura generada.
	 * Si la carpeta en la que se desean generar las imagenes existe, se borrara su contenido para
	 * adaptarse al contenido de la estructura de la galeria de imagenes
	 * @param location: Path en el que se desea generar la galeria
	 * @param minImagesPerFolder: minimo numero de imagenes por carpeta
	 * @param maxImagesPerFolder: maximo de imagenes por carpeta
	 * @param fromYear: desde el año que se desea generar la estructura de carpetas aleatoria
	 * @param probability: probabilidad de crear profundidad en la estructura de carpetas
	 * @param eventos: set con nombres de los eventos que apareceran en la estructura
	 * @param maxImageDir: maximo de directorios que se crearan en caso de que haya profundidad en los
	 * eventos
	 */
	public static void generateGallery(Path location, int minImagesPerFolder, 
			int maxImagesPerFolder, int fromYear, double probability, 
			Set<String> eventos, int maxImageDir) {
		
		numberOfImages = 0;
		
		if(Files.exists(location)) {
			
			try {
				DirectoryUtils.deleteDirectoryContents(location);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		DirectoryUtils.generateRandomFolderStructure(location, fromYear, probability, eventos, maxImageDir);

		galleryGenerator(location, minImagesPerFolder, maxImagesPerFolder);
		
	}
	
	private static void galleryGenerator(Path location, int minImagesPerFolder,
	        int maxImagesPerFolder) {
		
		if(DirectoryUtils.countPathsInDirectoryStream(location) != 0) {
			
			//Llamamos al recolector de basura para que no explote
			System.gc();
			
			DirectoryStream<Path> directories = null;
		    try {
		        directories = Files.newDirectoryStream(location);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		    
		    for(Path folder : directories) {
		    	
		    	if(Files.isDirectory(folder)) {
		    		
		    		galleryGenerator(folder, minImagesPerFolder, maxImagesPerFolder);
		    		
		    	}
		    	
		    }
			
		}
		else {
			
			Random rand = new Random();
			
			for(int i = 0; i < getRandomNumberInRange(minImagesPerFolder, maxImagesPerFolder); i++) {
				
				//We create random images
				int width = imageSizes.get(rand.nextInt(imageSizes.size()));
				int height = imageSizes.get(rand.nextInt(imageSizes.size()));
				
				if(rand.nextBoolean()) {
					ImageUtils.createRandomImagesImage(width, height, location.resolve("image" + numberOfImages + ".jpg"), 
							rand.nextInt(MAX_PRIMITIVES - MIN_PRIMITIVES + 1) + MIN_PRIMITIVES);
				}
				else {
					ImageUtils.createRandomImage(width, height, location.resolve("image" + numberOfImages + ".jpg"), 
							rand.nextInt(MAX_PRIMITIVES - MIN_PRIMITIVES + 1) + MIN_PRIMITIVES);
				}
				
				//We set his metadata
				try {
					ImageUtils.editMetadata(location.resolve("image" + numberOfImages + ".jpg"));
				} catch (ImageReadException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ImageWriteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				numberOfImages++;
				System.out.println(numberOfImages);
				
			}
			
			//Lamamos al recolector de basura para que no explote
			System.gc();
			
		}

	}

}
