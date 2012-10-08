package math4u2.application.resource;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

/**
 * Verwaltung allgemeiner Einstellungen, die verwendet werden
 * 
 * @author Fenn Stefan
 */
public class Settings {

	/** Bildauflösung */
	public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit()
			.getScreenSize();

	/**
	 * Berechnet das Rechteck mit einer prozentualen angabe und mittiger
	 * Positionierung
	 * 
	 * @param px
	 *            Ausdehnung in die Breite in Prozent
	 * @param py
	 *            Ausdehnung in die Höhe in Prozent
	 * @param comp Referenzkomponente für Größenausrichtung
	 * @return Rechteck für z.B. setBounds()
	 */
	public static Rectangle computeBounds(Component comp, double px, double py) {
		Dimension size = SCREEN_SIZE;
		Point location = new Point(0,0);
		if(comp!=null && comp.getSize().width*comp.getSize().height!=0){
			size = comp.getSize();			
		}//if
		if(comp!=null){
			location = comp.getLocation();
		}//if
		Rectangle rec = new Rectangle(new Point(
				(int) (location.x + size.width * ((1 - px) / 2)),
				(int) (location.y + size.height * ((1 - py) / 2))),
				 new Dimension((int) (size.width * px),	(int) (size.height * py)));
		if(rec.x<0){ 
			rec.x=0;
		}
		if(rec.y<0){
			rec.y=0;
		}
		return rec;
	} //computeBounds

	public static Rectangle computeBounds(Component comp, int x, int y) {
		Dimension size = SCREEN_SIZE;
		Point location = new Point(0,0);
		if(comp!=null){
			size = comp.getSize();
			location = comp.getLocation();
		}//if		
		Rectangle rec = new Rectangle(new Point(location.x + (size.width - x) / 2,
				location.y + (size.height - y) / 2), new Dimension(x, y));
		if(rec.x<0){ 
			rec.x=0;
		}
		if(rec.y<0){ 
			rec.y=0;
		}
		return rec;
	} //computeBounds

} //class Settings
