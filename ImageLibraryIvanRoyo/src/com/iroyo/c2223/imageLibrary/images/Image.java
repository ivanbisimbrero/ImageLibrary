package com.iroyo.c2223.imageLibrary.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata.GPSInfo;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;

/**
 * Clase que se encarga de albergar la informacion necesaria de una imagen de formato jpg o jpeg
 * (metadatos exif)
 * @author ivano
 *
 */
public class Image {
	
	private String name;
	private int width;
	private int height;
	private Path location;
	
	//Metadatos:
	
	//Fecha
	private String creationDate;
	
	//GPS
	private double latitude;
	private String latReference;
	private double longitude;
	private String longReference;
	
	//Modelo de camara
	private String cameraModel;
	
	//ISO
	private short ISO;
	
	public Image(Path location) {
		
		this.location = location;
		this.name = location.getFileName().toString();
		
		//Editamos los metadatos de la imagen creada
		try {
			setMetadata();
		} catch (ImageReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.gc();
		
	}
	
	/**
	 * Metodo que se encarga de leer los metadatos deseados para guardarlos en las variables de la clase,
	 * para que posteriormente pueda usarlos el usuario por su propio interes
	 * @throws ImageReadException
	 * @throws IOException
	 */
	private void setMetadata() throws ImageReadException, IOException {
		
		File image = location.toFile();
		ImageMetadata metadata = null;
				
		metadata = Imaging.getMetadata(image);
				
		if(metadata instanceof JpegImageMetadata) {
					
			JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
			TiffImageMetadata exif = jpegMetadata.getExif();
			
			//Rescatamos los datos de la fecha
			this.creationDate = (exif.getFieldValue(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL))[0];
			
			//Rescatamos los datos del GPS
			GPSInfo gps = exif.getGPS();
			this.latitude = gps.getLatitudeAsDegreesNorth();
			this.latReference = gps.latitudeRef;
			this.longitude = gps.getLongitudeAsDegreesEast();
			this.longReference = gps.longitudeRef;
			
			//Rescatamos los datos del tipo de camara
			this.cameraModel = (exif.getFieldValue(ExifTagConstants.EXIF_TAG_LENS_MODEL))[0];
			
			//Rescatamos el ISO
			this.ISO = (exif.getFieldValue(ExifTagConstants.EXIF_TAG_ISO))[0];
			
			//Rescatamos el tamaño
			BufferedImage auxImage = ImageIO.read(location.toFile());
			
			this.width = auxImage.getWidth();
			this.height = auxImage.getHeight();
			
		}
		
	}

	public String getName() {
		return name;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Path getLocation() {
		return location;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public double getLatitude() {
		return latitude;
	}

	public String getLatReference() {
		return latReference;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getLongReference() {
		return longReference;
	}

	public String getCameraModel() {
		return cameraModel;
	}

	public short getISO() {
		return ISO;
	}

	@Override
	public String toString() {
		String image = new String();
		
		image+="\nImagen " + this.name + ": Tamaño " + width + "x" + height + "\n";
		image+="Localizacion: " + location + "\n\n";
		
		//Fecha
		image+="Fecha de creacion: " + creationDate + "\n\n";
		
		//GPS
		image+="GPS: \nLatitud: " + latitude + " - " + latReference + "\nAltitud: " + longitude + " - " +
				longReference + "\n\n";
		
		//Modelo camara:
		image+="Modelo camara: " + cameraModel + "\n";
		
		//ISO
		image+="ISO: " + ISO + "\n";
		
		return image;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {return true;}
		
		if(obj instanceof Image) {
			Image other = (Image) obj;
			if(this.location.equals(other.location)) {return true;}
		}
		
		return false;
		
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.width, this.height, this.location, this.creationDate,
				this.latitude, this.latReference, this.longitude, this.longReference, this.cameraModel,
				this.ISO);
	}
	
}
