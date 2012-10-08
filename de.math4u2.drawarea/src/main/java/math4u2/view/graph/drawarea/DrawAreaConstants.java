package math4u2.view.graph.drawarea;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;

/**
 * Konstanten für die Zeichenfläche
 * 
 * @author Fenn Stefan
 */
public interface DrawAreaConstants {
	public final static RenderingHints antialiasOn = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	public final static RenderingHints antialiasOff = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);		

	final static int FAST_DETAIL = 10;

	final static Cursor boxZoomCursor = new Cursor(Cursor.HAND_CURSOR);

	final static Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);

	final static Cursor loadCursor = new Cursor(Cursor.WAIT_CURSOR);

	final static Cursor moveCursor = new Cursor(Cursor.MOVE_CURSOR);

	final static Cursor xZoomCursor = new Cursor(Cursor.E_RESIZE_CURSOR);

	final static Cursor yZoomCursor = new Cursor(Cursor.N_RESIZE_CURSOR);

	final static Cursor zoomCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);

	final static Cursor invisibleCursor = Toolkit.getDefaultToolkit()
			.createCustomCursor(Toolkit.getDefaultToolkit().getImage(""),
					new Point(0, 0), "invisible");
} //class DrawArea Constants
