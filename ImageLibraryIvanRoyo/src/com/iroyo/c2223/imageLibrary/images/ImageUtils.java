package com.iroyo.c2223.imageLibrary.images;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

import com.iroyo.c2223.imageLibrary.directories.DirectoryUtils;

public class ImageUtils {
	
	private static final String[] CAMERA_BRANDS = {
	        "Canon",
	        "Nikon",
	        "Sony",
	        "Fujifilm",
	        "Olympus",
	        "Panasonic",
	        "Leica",
	        "Pentax",
	        "Samsung",
	        "Hasselblad",
	        "Phase One",
	        "GoPro",
	        "DJI",
	        "Ricoh",
	        "Kodak",
	        "Hoya",
	        "Sigma",
	        "Zeiss"
	    };
	
	public static String[] getCameraBrands() {
		return CAMERA_BRANDS;
	}
	
	/**
	 * Metodo que se encarga de escoger un Shape aleatorio para generar imagenes aleatorias
	 * @param width: ancho de la imagen
	 * @param height: alto de la imagen
	 * @return
	 */
	private static Shape chooseRandomDrawShape(int width, int height) {
		
		Random rand = new Random();
		
		switch(rand.nextInt(4)) {
		case 0:
			return new Line2D.Double(rand.nextInt(width+1), rand.nextInt(height+1), 
					rand.nextInt(width+1), rand.nextInt(height+1));
		case 1:
			return new Rectangle2D.Double(rand.nextInt(width+1), rand.nextInt(height+1), 
					rand.nextInt(width+1), rand.nextInt(height+1));
		case 2:
			return new Arc2D.Double(rand.nextInt(width+1), rand.nextInt(height+1), 
					rand.nextInt(width+1), rand.nextInt(height+1), rand.nextInt(width+1), 
					rand.nextInt(height+1), Arc2D.OPEN);
		case 3:
			return new Ellipse2D.Double(rand.nextInt(width+1), rand.nextInt(height+1), 
					rand.nextInt(width+1), rand.nextInt(height+1));	
		default:
			return null;
		}
		
	}
	
	private static String getRandomImage(Path imageSource) throws IOException {
		
		Random rand = new Random();
		DirectoryStream<Path> images = Files.newDirectoryStream(imageSource);
		
		//Ahora elegimos una imagen aleatoria, para ello primero contamos cuantas imagenes hay
		int imageNumber = DirectoryUtils.countPathsInDirectoryStream(imageSource);
		
		System.gc();
		
		//Ahora elegiremos una imagen aleatoriamente para incluirla dentro de nuestro dibujo
		int counter = 0;
		int random = rand.nextInt(imageNumber);
		images = Files.newDirectoryStream(imageSource);
		for(Path image : images) {
			if(counter == random) {
				return image.toString();
			}
			counter++;
		}
		return null;
		
	}
	
	/**
	 * Funcion que se encarga de generar imagenes a partir de primitivas que pueden contener otras 
	 * imagenes dentro (contenidas en la carpeta "nano" del proyecto)
	 * @param width: ancho de la imagen generada
	 * @param height: alto de la imagen generada
	 * @param p: path completo donde estara la imagen (incluido el nombre de la imagen)
	 * @param primitiveNumber: numero de primitivas con las que se generara la imagen
	 */
	public static void createRandomImagesImage(int width, int height, Path p, int primitiveNumber) {
		
		if(p.getParent() != null) {
			if(!Files.exists(p.getParent())) {
				try {
					Files.createDirectories(p.getParent());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		ArrayList<Color> colors = new ArrayList<Color>(Arrays.asList(Color.WHITE, Color.RED,
				Color.BLUE, Color.CYAN, Color.ORANGE, Color.GREEN));
		Random rand = new Random();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();
		
		//Rellenamos el fondo de nuestra imagen
		g2d.setColor(colors.get(rand.nextInt(colors.size())));
		g2d.fillRect(0, 0, width, height);
		
		//Ahora, para el numero de primitivas especificado para la imagen, iremos dibujandolas
		//o rellenandolas de manera aleatoria
		
		for(int i = 0; i < primitiveNumber; i++) {
			
			g2d.setColor(colors.get(rand.nextInt(colors.size())));
			int random = rand.nextInt(3);
			if(random == 0) {
				g2d.draw(chooseRandomDrawShape(width, height));
			}
			else if(random == 1){
				g2d.fill(chooseRandomDrawShape(width, height));
			}
			else {
				try {
					BufferedImage component = ImageIO.read(new File(getRandomImage(Paths.get("nano"))));
					
					//Obtenemos la anchura y altura de la imagen original
					int widthComp = component.getWidth();
					int heightComp = component.getHeight();

					//Generamos una subimagen de la imagen escogida
					int subWidth = rand.nextInt(widthComp) + 1; 
					int subHeight = rand.nextInt(heightComp) + 1; 
					int x = rand.nextInt(widthComp - subWidth + 1); 
					int y = rand.nextInt(heightComp - subHeight + 1); 

					BufferedImage subImage = component.getSubimage(x, y, subWidth, subHeight);
					
					//Finalmente, dibujamos la imagen
					g2d.drawImage(subImage,rand.nextInt(width+1), rand.nextInt(height+1), 
							rand.nextInt(width+1), rand.nextInt(height+1), null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		//Finalmente, escribiremos la imagen al archivo deseado
		g2d.dispose();
		
		File file = p.toFile(); // ruta y nombre del archivo de salida
		try {
			if(Files.exists(p)) {
				Files.delete(p);
			}
			ImageIO.write(image, "jpg", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Funcion que se encarga de generar imagenes a partir de primitivas.
	 * @param width: ancho de la imagen generada
	 * @param height: alto de la imagen generada
	 * @param p: path completo donde estara la imagen (incluido el nombre de la imagen)
	 * @param primitiveNumber: numero de primitivas con las que se generara la imagen
	 */
	public static void createRandomImage(int width, int height, Path p, int primitiveNumber) {
		
		ArrayList<Color> colors = new ArrayList<Color>(Arrays.asList(Color.WHITE, Color.RED,
				Color.BLUE, Color.CYAN, Color.ORANGE, Color.GREEN));
		Random rand = new Random();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();
		
		//Rellenamos el fondo de nuestra imagen
		g2d.setColor(colors.get(rand.nextInt(colors.size())));
		g2d.fillRect(0, 0, width, height);
		
		//Ahora, para el numero de primitivas especificado para la imagen, iremos dibujandolas
		//o rellenandolas de manera aleatoria
		
		for(int i = 0; i < primitiveNumber; i++) {
			
			g2d.setColor(colors.get(rand.nextInt(colors.size())));
			if(rand.nextDouble() > 0.7) {
				g2d.draw(chooseRandomDrawShape(width, height));
			}
			else {
				g2d.fill(chooseRandomDrawShape(width, height));
			}
			
			
		}
		
		//Finalmente, escribiremos la imagen al archivo deseado
		g2d.dispose();
		
		File file = p.toFile(); // ruta y nombre del archivo de salida
		try {
			if(Files.exists(p)) {
				Files.delete(p);
			}
			ImageIO.write(image, "jpg", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static String generateRandomDate(int startYear, int endYear) {
		Random rand = new Random();
        Calendar calendar = Calendar.getInstance();
        
        calendar.set(Calendar.YEAR, rand.nextInt(endYear - startYear + 1) + startYear);
        calendar.set(Calendar.MONTH, rand.nextInt(12)); // Mes aleatorio (0-11)
        calendar.set(Calendar.DAY_OF_MONTH, rand.nextInt(28) + 1); // Día aleatorio (1-28)
        calendar.set(Calendar.HOUR_OF_DAY, rand.nextInt(24)); // Hora aleatoria (0-23)
        calendar.set(Calendar.MINUTE, rand.nextInt(60)); // Minuto aleatorio (0-59)
        calendar.set(Calendar.SECOND, rand.nextInt(60)); // Segundo aleatorio (0-59)

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        return dateFormat.format(calendar.getTime());
    }
	
	public static short generateRandomISO() {
		
		Random rand = new Random();
		
		//Segun los estandares de ISO, establecemos los siguientes valores para las ISO de la imagen
        int minISO = 100;  
        int maxISO = 6400; 

        return (short) (rand.nextInt(maxISO - minISO + 1) + minISO);
    }
	
	/**
	 * Funcion que se encarga de generar metadatos dentro de una imagen que se le pasa como parametro de
	 * entrada.
	 * Los metadatos generados son: Fecha de creacion, Localizacion GPS, ISO y Modelo de Camara
	 * @param location: path completo de la imagen
	 * @throws ImageReadException si hay problemas al leer la imagen
	 * @throws IOException
	 * @throws ImageWriteException si hay problemas al escribir la imagen
	 */
	public static void editMetadata(Path location) throws ImageReadException, IOException, 
	ImageWriteException {
		
		Random rand = new Random();
		
		//Creamos un file con nuestra imagen
		File image = location.toFile();
		
		//Obtenemos los metadatos de la imagen
		ImageMetadata metadata = Imaging.getMetadata(image);
		
		//Creamos un outputSet para usarlo mas tarde
		TiffOutputSet outputSet = null;
		
		if(metadata instanceof JpegImageMetadata) {
			
			JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
			
			if(jpegMetadata != null) {
				
				TiffImageMetadata exif = jpegMetadata.getExif();
				if(exif != null) {
					
					outputSet = exif.getOutputSet();
					
				}
				
			}
			
		}
		
		if(outputSet == null) {
			outputSet = new TiffOutputSet();
		}
		
		//Creamos un directorio de metadatos, para escribir diferentes tipos de metadatos
		
		TiffOutputDirectory exifMetadata = null;
		
		exifMetadata = outputSet.getOrCreateExifDirectory();	
		
		//Modificamos los metadatos correspondientes a la fecha de la imagen
		
		exifMetadata.add(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL, generateRandomDate(2000, 2023));
		
		//Modificamos los metadatos correspondientes al GPS
		
		final double longitude = rand.nextDouble()*180 - 90; 
		final double latitude = rand.nextDouble()*360 - 180;

		outputSet.setGPSInDegrees(longitude, latitude);
		
		//Modificamos los metadatos correspondientes al tipo de camara con el que se hizo la foto
		
		exifMetadata.add(ExifTagConstants.EXIF_TAG_LENS_MODEL, 
				CAMERA_BRANDS[rand.nextInt(CAMERA_BRANDS.length)]);
		
		//Modificamos los metadatos correspondientes al ISO
		
		exifMetadata.add(ExifTagConstants.EXIF_TAG_ISO, 
				generateRandomISO());
		
		
		//Escribimos los metadatos en nuestra imagen
		
		ExifRewriter ex = new ExifRewriter();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		ex.updateExifMetadataLossless(image, outputStream, outputSet);
		
		FileOutputStream fos = new FileOutputStream(image);
		fos.write(outputStream.toByteArray());
		fos.close();
		
	}

}
