package com.iroyo.c2223.imageLibrary.directories;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import com.iroyo.c2223.imageLibrary.exceptions.NotEnoughtStringsException;

/**
 * Clase que pertmite generar una estructura de directorios completamente aleatoria, simulando el paso
 * del tiempo
 * @author ivano
 *
 */
public class DirectoryUtils {
	
	/**
	 * Funcion que elige una cadena aleatoria de una coleccion cualquiera de Strings
	 * @param c: coleccion que tiene las cadenas deseadas
	 * @return cadena aleatoria elegida de la coleccion
	 */
	private static String chooseRandomString(Collection<String> c) {
		
		Iterator<String> iter = c.iterator();
		Random rand = new Random();
		String rtrn = new String();
		
		for(int i = 0; i < rand.nextInt(c.size())+1; i++) {
			
			rtrn = iter.next();
			
		}
		
		return rtrn;
		
	}
	
	/**
	 * Funcion que genera un numero de carpetas especificado por el usuario, en una ruta
	 * determinada y con unos nombres aleatorios seleccionados de un Set pasado como parametro
	 * @param source: Path en el que se van a generar las carpetas. Si no existe, este sera creado
	 * @param dirNumber: numero de directorios que se generaran dentro del Path
	 * @param dirNames: Set con los nombres que podran tener los directorios
	 * @throws NotEnoughtStringsException en caso de que el tamaño de directorios que solicita el
	 * para crear sea mayor al tamaño del set
	 */
	public static void generateRandomNamedDirectories(Path source, int dirNumber, Set<String> dirNames) throws NotEnoughtStringsException {
		
		if(dirNumber > dirNames.size()) {
			throw new NotEnoughtStringsException("El numero de directorios excede al tamaño del set");
		}
		
		//Si el directorio pasado como parametro no existe, lo creamos
		if(!Files.exists(source)) {
			try {
				Files.createDirectories(source);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Para garantizar que se crean el numero de directorios especificado sin duplicados usaremos otro set
		Set<String> s = new HashSet<String>();
		
		while(s.size() < dirNumber) {
			
			s.add(chooseRandomString(dirNames));
			
		}
		
		//Finalmente, creamos las carpetas en el path especificado
		Iterator<String> iter = s.iterator();
		
		while(iter.hasNext()) {
			Path newPath = source.resolve(iter.next());
			try {
				if(!Files.exists(newPath)) {
					Files.createDirectory(newPath);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Esta funcion se encaraga de generar una jerarquia de carpetas en el path especificado. La 
	 * jerarquia contendra años aleatorios desde el año que se le especifique hasta 2023, y dentro
	 * de estos años, se generaran meses aleatoriamente, pudiendo contener desde 1 hasta 12 meses
	 * @param source: Path en el que se desea hacer la jerarquia (si no existe se creara).
	 * @param fromYear: año desde el que se desea genearar
	 */
	public static void generateRandomYearsAndMonths(Path source, int fromYear) {
		
		Random rand = new Random();
		
		//Si no existe la ruta especificada, la creamos
		if(!Files.exists(source)) {
			try {
				Files.createDirectories(source);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
				
		//Creamos los años
		Set<String> years = new HashSet<String>();
				
		for(int i = fromYear; i <= 2023; i++) { //Como no hemos ido mas alla de 2023, lo voy a prefijar
					
			years.add(Integer.toString(i));
					
		}
				
		try {
			generateRandomNamedDirectories(source, rand.nextInt(years.size())+1, years);
		} catch (NotEnoughtStringsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Ahora, creamos los meses, dentro de todos los años que se hayan creado
		Set<String> meses = new HashSet<String>(Arrays.asList("Enero", "Febrero", "Marzo", "Abril", 
				"Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"));
		DirectoryStream<Path> dirS = null;
		try {
			dirS = Files.newDirectoryStream(source);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Path p : dirS) {
			try {
				generateRandomNamedDirectories(p, rand.nextInt(meses.size())+1, meses);
			} catch (NotEnoughtStringsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private static void generateRandomEvents(Path source, double probability, Set<String> eventos, int maxImageDir, boolean noCreatedEvents) {
		
		if(!Files.exists(source)) {
			try {
				Files.createDirectories(source);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Random rand = new Random();
		
		if(rand.nextDouble() > probability) {
			
			return;
			
		}
		
		//Si los eventos no han sido creados, los creamos
		if(noCreatedEvents) {
			try {
				generateRandomNamedDirectories(source, rand.nextInt(eventos.size())+1, eventos);
			} catch (NotEnoughtStringsException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		//Si han sido creados, generaremos carpetas dentro de ellos para que tenga sentido el contenido
		else {
			for(int i = 0; i < rand.nextInt(maxImageDir)+1; i++) {
				
				Path newPath = source.resolve("images_"+i);
				
				if(!Files.exists(newPath)) {
					try {
						Files.createDirectories(newPath);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		
		DirectoryStream<Path> dir = null;
		
		try {
			dir = Files.newDirectoryStream(source);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Path p : dir) {
			
			generateRandomEvents(p, probability / 2, eventos, maxImageDir, false);
			
		}
		
	}
	
	/**
	 * Funcion que se encarga de crear carpetas con nombres de eventos aleatorios dentro del path 
	 * especificado, con probabilidad de que se cree una profundiudad dentro de las carpetas de 
	 * los eventos, o directamente que ni se creen eventos.
	 * @param source: Path en el que se crearan las carpetas
	 * @param probability: probabilidad de que se creen las capetas
	 * @param eventos: set con los nombres de los eventos
	 * @param maxImageDir: maximo de directorios por carpeta (en caso de generarse profundidad)
	 */
	public static void generateRandomEvents(Path source, double probability, Set<String> eventos, int maxImageDir) {
		
		generateRandomEvents(source, probability, eventos, maxImageDir, true);
		
	}
	
	/**
	 * Funcion que se encarga de generar una estructura de directorios completamente aleatoria en el 
	 * path especificado. La funcion generara años aleatorios que contrendran meses aleatorios. Dentro
	 * de estos meses se generaran eventos aleatorios.
	 * @param source: Path en el que se desea generar la estructura de directorios
	 * @param fromYear: Año desde el que se desean generar carpetas
	 * @param probability: Probabilidad de que se cree profundidad
	 * @param eventos: Set de eventos que se desean incluir
	 * @param maxImageDir: maximo de directorios por carpeta (en caso de generarse profundidad)
	 */
	public static void generateRandomFolderStructure(Path source, int fromYear, double probability, Set<String> eventos, int maxImageDir) {
		
		generateRandomYearsAndMonths(source, fromYear);
		
		//Ahora, generaremos aleatoriamente carpetas dentro de los meses de los años
		DirectoryStream<Path> years = null;
		
		try {
			years = Files.newDirectoryStream(source);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Path year: years) {
			
			DirectoryStream<Path> months = null;
			
			try {
				months = Files.newDirectoryStream(year);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for(Path month : months) {
				
				generateRandomEvents(month, probability, eventos, maxImageDir);
				
			}
			
		}
		
	}
	
	/**
	 * Metodo que se encarga de contar el numero de archivos que hay en una capeta
	 * @param directory: capeta deseada para contar los archivos
	 * @return numero de archivos que hay en la carpeta
	 */
	@SuppressWarnings("unused")
	public static int countPathsInDirectoryStream(Path directory) {
		
	    int count = 0;
	    
	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
	    	
	        for (Path path : stream) {
	        	
	            count++;
	            
	        }
	    } catch (IOException e) {
	    	
	        e.printStackTrace();
	        
	    }
	    
	    return count;
	}
	
	/**
	 * Funcion que se encarga de borrar recursivamente el contenido de una carpeta
	 * @param directory: carpeta de la que se desea borrar el contenido
	 * @throws IOException
	 */
	public static void deleteDirectoryContents(Path directory) throws IOException {
		
	    if (Files.isDirectory(directory)) {
	    	
	        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
	            for (Path path : stream) {
	            	
	                deleteDirectoryContents(path);
	                
	            }
	        }
	    }

	    Files.deleteIfExists(directory);
	}

}
