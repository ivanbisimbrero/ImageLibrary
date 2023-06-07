package com.iroyo.c2223.imageLibrary.main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.iroyo.c2223.imageLibrary.userInterface.GalleryInterface;

public class Main {
	
	public static void main(String[] args) {
		
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GalleryInterface().createAndShowGUI();
            }
        });
		
		
	}

}
