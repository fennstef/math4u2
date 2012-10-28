// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Implementation eines rollbaren <code>JPanel</code> s v. a. zur Darstellung
 * in einer <code>JScrollPane</code>.
 * 
 * @author Erich Seifert
 * @version 0.1
 * @see javax.swing.JPanel
 * @see javax.swing.JScrollPane
 */
public class ScrollablePanel extends JPanel implements Scrollable {
	/**
	 * Variablen die festlegen, ob sich die Ausma�e an den Container anpassen
	 */
	private boolean tracksWidth, tracksHeight;

	/**
	 * Konstruktor der ein neues ScrollablePanel-Objekt erzeugt.
	 * 
	 * @param tracksVpWidth
	 *            Legt fest, ob sich die Breite anpassen soll
	 * @param tracksVpHeight
	 *            Legt fest, ob sich die H�he anpassen soll
	 */
	public ScrollablePanel(boolean tracksVpWidth, boolean tracksVpHeight) {
		super();
		tracksWidth = tracksVpWidth;
		tracksHeight = tracksVpHeight;
	}

	/**
	 * Standardkonstruktor der ein neues ScrollablePanel-Objekt erzeugt.
	 */
	public ScrollablePanel() {
		this(true, false);
	}

	/**
	 * Gibt die bevorzugte Gr��e des Beh�lters zur�ck.
	 * 
	 * @return Ein Dimension-Objekte in dem die bevorzugte Gr��e des Beh�lters
	 *         gespeichert ist
	 */
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	/**
	 * Gibt die Schrittweite beim inkrementellen Rollen zur�ck.
	 * 
	 * @param visibleRect
	 *            Die aktuell sichtbare Fl�che
	 * @param orientation
	 *            Die Richtung in der gerollt wird
	 * @param direction
	 *            Die Richtung in der gerollt wird
	 * @return Die Schrittweite zum inkrementellen Rollen
	 */
	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return 20;
	}

	/**
	 * Gibt die Schrittweite beim blockweisen Rollen zur�ck.
	 * 
	 * @param visibleRect
	 *            Die aktuell sichtbare Fl�che
	 * @param orientation
	 *            Die Richtung in der gerollt wird
	 * @param direction
	 *            Die Richtung in der gerollt wird
	 * @return Die Schrittweite zum blockweisen Rollen
	 */
	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		if (orientation == SwingConstants.VERTICAL)
			return visibleRect.height;
		else
			return visibleRect.width;
	}

	/**
	 * Gibt zur�ck, ob sich das Panel an die Breite seines Beh�lters anpasst.
	 * 
	 * @return Einen boolschen Wert der anzeigt, ob sich das Panel an die Breite
	 *         anpasst
	 */
	public boolean getScrollableTracksViewportWidth() {
		return tracksWidth;
	}

	/**
	 * Gibt zur�ck, ob sich das Panel an die H�he seines Beh�lters anpasst.
	 * 
	 * @return Einen boolschen Wert der anzeigt, ob sich das Panel an die H�he
	 *         anpasst
	 */
	public boolean getScrollableTracksViewportHeight() {
		return tracksHeight;
	}
}//class ScrollablePanel
