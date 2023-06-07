package com.iroyo.c2223.imageLibrary.tests;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.junit.jupiter.api.Test;

import com.iroyo.c2223.imageLibrary.directories.DirectoryUtils;
import com.iroyo.c2223.imageLibrary.exceptions.BadArgumentException;
import com.iroyo.c2223.imageLibrary.images.Image;
import com.iroyo.c2223.imageLibrary.images.ImageGallery;
import com.iroyo.c2223.imageLibrary.images.ImageUtils;

class TestClases {
	
	@Test
	void correctImageCreated() {
		
		if(Files.exists(Paths.get("testImage.jpg"))) {
			try {
				Files.delete(Paths.get("testImage.jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ImageUtils.createRandomImagesImage(300, 400, Paths.get("testImage.jpg"), 20);
		assert(Files.exists(Paths.get("testImage.jpg")));
		
		//Despues de ejecutar el test, borramos
		try {
			Files.delete(Paths.get("testImage.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	void correctAssedImageSize() {
		
		if(Files.exists(Paths.get("testImage.jpg"))) {
			try {
				Files.delete(Paths.get("testImage.jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ImageUtils.createRandomImagesImage(300, 500, Paths.get("testImage.jpg"), 20);
		
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(Paths.get("testImage.jpg").toFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(image.getWidth(), 300);
		assertEquals(image.getHeight(), 500);
		
		//Despues de ejecutar el test, borramos
		try {
			Files.delete(Paths.get("testImage.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	void createdFolderStructureCorrectly() {
		
		Set<String> eventos = new HashSet<String>(Arrays.asList("boda", "primos", "familia"));
		
		Path p = Paths.get("testDirectorios");
		
		DirectoryUtils.generateRandomFolderStructure(p, 2020, 0.5, eventos, 3);
		
		assert(Files.exists(p));
		
		//Despues de ejecutar el test, borramos
		try {
			DirectoryUtils.deleteDirectoryContents(p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	void correctlyWritedMetadata() {
		
		if(Files.exists(Paths.get("testImage.jpg"))) {
			try {
				Files.delete(Paths.get("testImage.jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ImageUtils.createRandomImagesImage(300, 500, Paths.get("testImage.jpg"), 20);
		try {
			ImageUtils.editMetadata(Paths.get("testImage.jpg"));
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
		
		Image test = new Image(Paths.get("testImage.jpg"));
		
		assert(test.getCameraModel() != null);
		assert(test.getISO() != 0);
		assert(test.getCreationDate() != null);
		assert(test.getLatReference() != null);
		assert(test.getLongReference() != null);
		
		//Despues de ejecutar el test, borramos
		try {
			Files.delete(Paths.get("testImage.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	void analizedCorrectly() {
		
		ImageGallery test = null;
		
		try {
			test = new ImageGallery(Paths.get("default"));
		} catch (BadArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assert(test.getImages().size() != 0);
		
	}

}
