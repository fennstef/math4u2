package math4u2.view.graph;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JButton;

import math4u2.view.graph.drawarea.DrawAreaConstants;
import math4u2.view.graph.drawarea.DrawAreaInterface;

/**
 * Button
 * 
 * @author Fenn Stefan
 */
public abstract class DragButtonAbstract {

	/** Größe der Druckfläche */
	protected final int SIZE = 25;

	/** Grafische Komponenten */
	protected HandleButton b1, b2;

	/** aktuelle Position */
	protected int x, y;

	/** Referenz zur Zeichenfläche */
	protected DrawAreaInterface da;
	
	public DragButtonAbstract(DrawAreaInterface da) {
		this.da = da;
		b1 = new HandleButton();
		b2 = new HandleButton();	
	} 
	
	public void init(){
		computeCoordinates();
		
		b1.setBounds(limitRangeX(x), limitRangeY(y), SIZE, SIZE);
		b2.setBounds(limitRangeX(x), limitRangeY(y), SIZE, SIZE);
		final DrawAreaInterface da2 = da;
		da.add(b1);
		da.add(b2);
		b1.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent evt) {
				b1.setLocation(b2.getLocation());
				b1.setVisible(true);
				b2.setVisible(false);
				da2.setCursor(DrawAreaConstants.defaultCursor);
			}// mouseReleased

			public void mousePressed(MouseEvent evt) {
				da2.requestFocus();
				da2.setCursor(DrawAreaConstants.invisibleCursor);
			}// mousePressed
		});
		b1.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent evt) {
			}

			public void mouseDragged(MouseEvent evt) {
				makeMouseDragged(evt);
			}// mouseDragged
		});
		b1.setBorder(null);
		b2.setBorder(null);
		b1.setContentAreaFilled(false);
		b2.setContentAreaFilled(false);
	}

	/**
	 * Berechnet die aktuelle Koordinaten des DragButtons und weißt sie x und y
	 * zu.
	 * 
	 * @throws MathException
	 */
	protected abstract void computeCoordinates();

	protected abstract void makeMouseDragged(java.awt.event.MouseEvent evt);

	public void addMouseListener(MouseListener al) {
		b1.addMouseListener(al);
	} // addActionListener

	public void remove() {
		da.remove(b1);
		da.remove(b2);
	} // remove

	public void renew() {
		computeCoordinates();
		b2.setLocation(x,y);
		b1.setLocation(x,y);
	}

	public void setVisible(boolean b) {
		b1.setVisible(b);
		b2.setVisible(b);
	} 
	
	/**
	 * Beschränkt die X-Koordinate auf die Größe der Zeichenfläche plus einen
	 * unsichtbaren Rand.
	 * 
	 * @param x
	 *            Zu zeichnende Breite
	 */
	protected int limitRangeX(int x) {
		x = Math.max(x, -SIZE * 2);
		return Math.min(x, da.getWidth() + SIZE * 2);
	}// limitRangeX

	/**
	 * Beschränkt die Y-Koordinate auf die Größe der Zeichenfläche plus einen
	 * unsichtbaren Rand.
	 * 
	 * @param y
	 *            Zu zeichnende Höhe
	 */
	protected int limitRangeY(int y) {
		y = Math.max(y, -SIZE * 2);
		return Math.min(y, da.getHeight() + SIZE * 2);
	}// limitRangeY
}
