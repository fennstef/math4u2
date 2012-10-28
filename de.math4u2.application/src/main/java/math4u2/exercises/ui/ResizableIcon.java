//Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises.ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.*;
import javax.swing.*;

/**
 * Ein Java-Icon, das mehrere Umrissgrafiken speichert.
 * 
 * @version 0.1
 * @author Erich Seifert
 */
public class ResizableIcon implements Icon {
	private static final RenderingHints antialiasOn = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	/**
	 * Konstante, um festzulegen, dass die Umrisse gef�llt dargestellt werden
	 * sollen.
	 */
	public static final byte FILL_MODE = 0x00;

	/**
	 * Konstante, um festzulegen, dass die Umrisse nur gezeichnet und nicht
	 * gef�llt werden sollen.
	 */
	public static final byte DRAW_MODE = 0x01;

	private byte mode = FILL_MODE; // Der aktuelle Modus (F�llen oder Zeichnen)

	private Color color = Color.black; // Die aktuellen Farbeinstellungen

	private Stroke stroke; // Die aktuellen Stricheinstellungen

	private int hSize; // Breite

	private int vSize; // H�he

	private BufferedImage bi; // Pufferbild f�r die Vektorgrafiken

	private Graphics2D big; // Graphics2D-Objekt f�r Pufferbild

	/**
	 * Die Vektorgrafiken, die dargestellt werden.
	 */
	protected GeneralPath[] vectImgs;

	/**
	 * Konstruktor der ein neues ResizableIcon-Objekt mit mehreren
	 * Umrissgrafiken erzeugt.
	 * 
	 * @param icons
	 *            Die Umrissgrafiken, die dargestellt werden sollen
	 * @param width
	 *            Die Breite des Icon-Objekts
	 * @param height
	 *            Die H�he des Icon-Objekts
	 */
	public ResizableIcon(GeneralPath[] icons, int width, int height) {
		vectImgs = icons;
		hSize = width;
		vSize = height;
		buildImage();
	}

	/**
	 * Konstruktor der ein neues ResizableIcon-Objekt mit einer Umrissgrafik
	 * erzeugt.
	 * 
	 * @param icon
	 *            Die Umrissgrafik, die dargestellt werden sollen
	 * @param width
	 *            Die Breite des Icon-Objekts
	 * @param height
	 *            Die H�he des Icon-Objekts
	 */
	public ResizableIcon(GeneralPath icon, int width, int height) {
		this(new GeneralPath[] { icon }, width, height);
	}

	/**
	 * Gibt die Breite des Icons zur�ck.
	 * 
	 * @return Die Breite des Icons
	 */
	public int getIconWidth() {
		return hSize;
	}

	/**
	 * Gibt die H�he des Icons zur�ck.
	 * 
	 * @return Die H�he des Icons
	 */
	public int getIconHeight() {
		return vSize;
	}

	/**
	 * Legt die Gr��e, d. h. die Breite und H�he des Icons fest.
	 * 
	 * @param width
	 *            Die neue Breite des Icon-Objekts
	 * @param height
	 *            Die neue H�he des Icon-Objekts
	 */
	public void setIconSize(int width, int height) {
		hSize = width;
		vSize = height;
		bi = null;
	}

	/**
	 * Legt den Zeichenmodus (gef�llt/ungef�llt) fest.
	 * 
	 * @param mode
	 *            Zeichenmodus der verwendet werden soll
	 */
	public void setMode(byte mode) {
		this.mode = mode;
		bi = null;
	}

	/**
	 * Legt die gew�nschte Strichart zum Zeichnen fest.
	 * 
	 * @param stroke
	 *            Strichart der verwendet werden soll
	 */
	public final void setStroke(Stroke stroke) {
		this.stroke = stroke;
		bi = null;
	}

	/**
	 * Legt die gew�nschte Farbe zum F�llen oder Zeichnen fest.
	 * 
	 * @param mode
	 *            Farbe die verwendet werden soll
	 */
	public final void setColor(Color color) {
		this.color = color;
		bi = null;
	}

	protected void buildImage() {
		bi = new BufferedImage(hSize, vSize, BufferedImage.TYPE_INT_ARGB);
		big = bi.createGraphics();

		if (vectImgs != null) {
			big.setColor(color); // Benutzerdefinierte Farbe setzen
			big.setRenderingHints(antialiasOn); // AntiAliasing aktivieren
			big.scale(hSize, vSize); // Gr��e des Koordinatensystems anpassen

			if (mode == FILL_MODE) // Wenn Umrisse gef�llt werden sollen, ...
			{
				for (int i = 0; i < vectImgs.length; i++) // Mit allen
														  // gespeicherten
														  // Umrissen:
				{
					big.fill(vectImgs[i]); // Umriss f�llen
				}
			} else if (mode == DRAW_MODE) // Wenn Umrisse nicht gef�llt werden
										  // sollen, ...
			{
				big.setStroke(stroke); // Strichtyp festelgen
				for (int i = 0; i < vectImgs.length; i++) // Mit allen
														  // gespeicherten
														  // Umrissen:
				{
					big.draw(vectImgs[i]); // Umriss zeichnen
				}
			}
		}
	}

	/**
	 * Methode zum Zeichnen der einzelnen Umrisse.
	 * 
	 * @param c
	 *            Komponente von der die Methode aufgerufen wurde
	 * @param g
	 *            Graphics-Objekt auf der sich das Icon zeichnen soll
	 * @param x
	 *            horizontale Position des Icons
	 * @param y
	 *            vertikale Position des Icons
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {
		if (vectImgs != null) {
			if (bi == null)
				buildImage();
			g.drawImage(bi, x, y, null);
		}
	}
}