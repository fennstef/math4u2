package math4u2.util.swing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class SaveImageUtil {
	
	public static void saveComponentAsJPEG(JComponent component, File file, double scale, int wait) {
		int width = (int) (component.getWidth()*scale);
		int height = (int) (component.getHeight()*scale);
		BufferedImage capturedImage = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		saveImageAsJPEG(capturedImage, file, component, scale, wait);
	}

	public static void saveImageAsJPEG(BufferedImage capturedImage, File file,
			JComponent component, double scale, int wait) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Graphics captureG = capturedImage.getGraphics();
		((Graphics2D)captureG).scale(scale, scale);
		component.paint(captureG);
		
		if(wait>0){
			try {
				Thread.sleep(wait*1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			component.paint(captureG);
		}
		
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		BufferedImage bufferedImage = (BufferedImage) capturedImage;
		JPEGEncodeParam param = encoder
				.getDefaultJPEGEncodeParam(bufferedImage);
		
		// the first parameter here is the "quality v file size" trade-off
		// play with this value (0-1) to determine the best results for you
		param.setQuality(1.0f, true);
		try {
			encoder.encode(bufferedImage, param);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println("Have saved " + component.getName() + " to file "
//				+ file);
	}
}
