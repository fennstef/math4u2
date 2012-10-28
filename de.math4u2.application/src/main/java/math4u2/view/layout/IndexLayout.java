package math4u2.view.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;

import math4u2.view.formula.FormulaElement;

/**
 * <code>LayoutManager</code>, der den Inhalt eines Behälters mit
 * Indices, Exponenten, sowie Dekorationen (Vektorpfeilen,
 * Über-/Unterstriche, usw.) versehen kann. Es existieren sieben
 * Positionen an denen der Inhalt platziert werden darf:
 * 
 * <pre>
 * +-------------+----------+--------------+
 * |             |   TOP    |              |
 * |    LEFT_TOP +----------+ RIGHT_TOP    |
 * |             |          |              |
 * +-------------+  CENTER  +--------------+ <-- axisHeight
 * |             |          |              |
 * | LEFT_BOTTOM +----------+ RIGHT_BOTTOM |
 * |             |  BOTTOM  |              |
 * +-------------+----------+--------------+
 * </pre>
 * 
 * In jeder Position kann nur ein Element gespeichert werden.
 * @author Erich Seifert
 * @version $Revision: 1.7 $
 */
public class IndexLayout implements LayoutManager2 {
	/** Positionsangabe für Indizes/Exponenten links oben */
	public static final String LEFT_TOP = "topLeft";
	/** Positionsangabe für Indizes/Exponenten links unten */
	public static final String LEFT_BOTTOM = "bottomLeft";
	/** Positionsangabe für Indizes/Exponenten/Dekoration oben */
	public static final String TOP = "top";
	/** Positionsangabe für den Hauptinhalt */
	public static final String CENTER = "center";
	/** Positionsangabe für Indizes/Exponenten/Dekoration unten */
	public static final String BOTTOM = "bottom";
	/** Positionsangabe für Indizes/Exponenten rechts oben */
	public static final String RIGHT_TOP = "topRight";
	/** Positionsangabe für Indizes/Exponenten rechts unten */
	public static final String RIGHT_BOTTOM = "bottomRight";

	private Component leftTop;
	private Component leftBottom;
	private Component top;
	private Component center;
	private Component bottom;
	private Component rightTop;
	private Component rightBottom;

	/** horizontaler Abstand zwischen Hauptteil und Indizes */
	private Insets innerGaps;
	/** vertikaler Abstand im Mittelteil */
	private int indexGap;

	private final Rectangle prfLeftSize;
	private final Rectangle prfRightSize;
	private final Rectangle prfCenterSize;
	private final Rectangle prfSize;
	private final Rectangle maxCenterSize;
	private final Rectangle maxSize;

	/** Gedachter Bruchstrich, der obere und untere Indizes trennt */
	private int axisHeight;
	/** Höhe des x-Buchstabens in der Schriftgröße des Hauptelements */
	private float xHeight;
	
	private boolean sizeValid;

	/**
	 * Konstruktor.
	 */
	public IndexLayout() {
		this(new Insets(0,0,0,0), 0);
	}

	/**
	 * Konstruktor, der ein neues <code>IndexLayout</code>-Objekt
	 * erzeugt und initialisiert.
	 * @param innerGaps
	 * @param indexGap
	 */
	public IndexLayout(Insets innerGaps, int indexGap) {
		prfLeftSize = new Rectangle();
		prfRightSize = new Rectangle();
		prfCenterSize = new Rectangle();
		prfSize = new Rectangle();
		maxCenterSize = new Rectangle();
		maxSize = new Rectangle();

		setGaps(innerGaps, indexGap);
	}

	/**
	 * 
	 * @see java.awt.LayoutManager2#getLayoutAlignmentX(java.awt.Container)
	 */
	public float getLayoutAlignmentX(Container cont) {
		return 0.5f;
	}

	/**
	 * 
	 * @see java.awt.LayoutManager2#getLayoutAlignmentY(java.awt.Container)
	 */
	public float getLayoutAlignmentY(Container cont) {
		return 0.5f;
	}

	/**
	 * 
	 * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String, java.awt.Component)
	 */
	public void addLayoutComponent(String name, Component comp) {
		addLayoutComponent(comp, name);
	}

	/**
	 * 
	 * @see java.awt.LayoutManager2#addLayoutComponent(java.awt.Component, java.lang.Object)
	 */
	public void addLayoutComponent(Component comp, Object constraint) {
		if (!(comp instanceof FormulaElement))
			throw new IllegalArgumentException("This layout can only handle components that implement 'FormulaElement'.");
		
		if (constraint==LEFT_TOP) {
			leftTop = comp;
		}
		else if (constraint==LEFT_BOTTOM) {
			leftBottom = comp;
		}
		else if (constraint==TOP) {
			top = comp;
		}
		else if (constraint==CENTER) {
			center = comp;
		}
		else if (constraint==BOTTOM) {
			bottom = comp;
		}
		else if (constraint==RIGHT_TOP) {
			rightTop = comp;
		}
		else if (constraint==RIGHT_BOTTOM) {
			rightBottom = comp;
		}
		else {
			throw new IllegalArgumentException("Wrong value for layout contraint.");
		}
		sizeValid = false;
	}

	/**
	 * 
	 * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
	 */
	public void removeLayoutComponent(Component comp) {
		if (comp==leftTop) {
			leftBottom = null;
		}
		else if (comp==leftBottom) {
			leftBottom = null;
		}
		else if (comp==top) {
			top = null;
		}
		else if (comp==center) {
			center = null;
		}
		else if (comp==bottom) {
			bottom = null;
		}
		else if (comp==rightTop) {
			rightTop = null;
		}
		else if (comp==rightBottom) {
			rightBottom = null;
		}
		sizeValid = false;
	}

	/**
	 * 
	 * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
	 */
	public void layoutContainer(Container cont) {
		Insets insets = cont.getInsets();
		int numComps = cont.getComponentCount();

		if (numComps==0)
			return;

		calcLayoutSize(cont);

		int x, y, w, h;
		int colX, colY;

		colX = insets.left;
		colY = insets.top + (prfLeftSize.y-prfSize.y);
		if (leftTop!=null) {
			Dimension prf = leftTop.getPreferredSize();
			Dimension max = leftTop.getMaximumSize();

			w = Math.min(max.width, prfLeftSize.width);
			h = prf.height;
			x = Math.max(prfLeftSize.width-w, 0);
			y = 0;

			leftTop.setBounds(colX+x, colY+y, w, h);
			colY += h+indexGap;
		}
		if (leftBottom!=null) {
			Dimension prf = leftBottom.getPreferredSize();
			Dimension max = leftBottom.getMaximumSize();

			w = Math.min(max.width, prfLeftSize.width);
			h = prf.height;
			x = Math.max(prfLeftSize.width-w, 0);
			y = 0;

			leftBottom.setBounds(colX+x, colY+y, w, h);
		}

		colX += prfLeftSize.width;
		colY = insets.top + (prfCenterSize.y-prfSize.y);
		if (top!=null) {
			Dimension prf = top.getPreferredSize();
			Dimension max = top.getMaximumSize();

			w = Math.min(max.width, maxCenterSize.width);
			h = prf.height;
			x = Math.max((maxCenterSize.width-w)/2, 0);
			y = 0;

			top.setBounds(colX+x, colY+y, w, h);
			colY += h+innerGaps.top;
		}
		if (center!=null) {
			Dimension prf = center.getPreferredSize();
			Dimension max = center.getMaximumSize();

			w = Math.min(max.width, maxCenterSize.width);
			h = prf.height;
			x = Math.max((maxCenterSize.width-w)/2, 0);
			y = 0;

			center.setBounds(colX+x, colY+y, w, h);
			colY += h+innerGaps.bottom;
		}
		if (bottom!=null) {
			Dimension prf = bottom.getPreferredSize();
			Dimension max = bottom.getMaximumSize();

			w = Math.min(max.width, maxCenterSize.width);
			h = prf.height;
			x = Math.max((maxCenterSize.width-w)/2, 0);
			y = 0;

			bottom.setBounds(colX+x, colY+y, w, h);
		}

		colX += prfCenterSize.width;
		colY = insets.top + (prfRightSize.y-prfSize.y);
		if (rightTop!=null) {
			Dimension prf = rightTop.getPreferredSize();
			Dimension max = rightTop.getMaximumSize();

			w = Math.min(max.width, prfRightSize.width);
			h = prf.height;
			x = 0;
			y = 0;

			rightTop.setBounds(colX+x, colY+y, w, h);
			colY += h+indexGap;
		}
		if (rightBottom!=null) {
			Dimension prf = rightBottom.getPreferredSize();
			Dimension max = rightBottom.getMaximumSize();

			w = Math.min(max.width, prfRightSize.width);
			h = prf.height;
			x = 0;
			y = 0;

			rightBottom.setBounds(colX+x, colY+y, w, h);
		}
	}

	private void calcLayoutSize(Container cont) {
		if (!sizeValid) {
			Insets insets = cont.getInsets();
	
			if (cont.getComponentCount()==0)
				return;
	
			axisHeight = 0;
			if (center!=null) {
				FormulaElement c = (FormulaElement)center;
				float baseline = c.getBaseline();
				xHeight = c.getXHeight();
				axisHeight = Math.round(baseline - xHeight*0.6f);
			}
	
			// Bevorzugte Größe der linken Elemente berechnen (Mitte-Oben, Hauptteil, Mitte-Unten);
			// Die Berechnung muss vor den beiden Seitenteilen erfolgen, da diese sich am Mittelteil
			// ausrichten. Dies ist vor allem für den Versatz bei Mehrfachindizes (wie z. B. x^2^2)
			// wichtig.
			calcCenterSize(prfCenterSize, maxCenterSize);
			if (rightTop!=null || rightBottom!=null) {
				prfCenterSize.width += innerGaps.right;
				maxCenterSize.width += innerGaps.right;
			}

			// Bevorzugte Größe der linken Elemente berechnen (oberer/unterer Index links)
			calcSideSize(prfLeftSize, leftTop, leftBottom);
			if (leftTop!=null || leftBottom!=null) {
				prfLeftSize.width += innerGaps.left;
			}
	
			// Bevorzugte Größe der rechten Elemente berechnen (oberer/unterer Index rechts)
			calcSideSize(prfRightSize, rightTop, rightBottom);
	
			// Oberste bevorzugte Kante (kleinsten y-Wert) aus allen drei Einzelteilen berechnen
			int prfMinY = min(prfLeftSize.y, prfCenterSize.y, prfRightSize.y);
			// Unterste bevorzugte Kante (größten y-Wert) aus allen drei Einzelteilen berechnen
			int prfMaxY = max(prfLeftSize.y+prfLeftSize.height, prfCenterSize.y+prfCenterSize.height, prfRightSize.y+prfRightSize.height);

			// Bevorzugte Gesamtgröße berechnen
			prfSize.setBounds(
				0, prfMinY,
				insets.left + (prfLeftSize.width+prfCenterSize.width+prfRightSize.width) + insets.right,
				insets.top + (prfMaxY-prfMinY) + insets.bottom
			);
	
			// Oberste maximale Kante (kleinsten y-Wert) aus allen drei Einzelteilen berechnen
			int maxMinY = min(prfLeftSize.y, maxCenterSize.y, prfRightSize.y);
			// Unterste maximale Kante (größten y-Wert) aus allen drei Einzelteilen berechnen
			int maxMaxY = max(prfLeftSize.y+prfLeftSize.height, maxCenterSize.y+maxCenterSize.height, prfRightSize.y+prfRightSize.height);

			// Maximal mögliche Gesamtgröße berechnen
			maxSize.setBounds(
				0, maxMinY,
				insets.left + (prfLeftSize.width+maxCenterSize.width+prfRightSize.width) + insets.right,
				insets.top + (maxMaxY-maxMinY) + insets.bottom
			);
			
			// Größenangaben wurden berechnet und sind nun gültig
			sizeValid = true;
		}
	}

	/**
	 * Hilfsmethode zur Berechnung der bevorzugten Größe von Seitenelementen (links oder rechts),
	 * also der Indizes.
	 * @param prfSize Größenangabe in die die bevorzugte Größe eingetragen wird
	 * @param sideTop oberes Element (Index) der Seite
	 * @param sideBottom unteres Element (Index) der Seite
	 */
	private void calcSideSize(Rectangle prfSize, Component sideTop, Component sideBottom) {
		prfSize.setBounds(0,0,0,0);

		if (sideTop==null && sideBottom==null)
			return;

		Dimension prfTop = new Dimension();
		if (sideTop!=null) {
			prfTop.setSize(sideTop.getPreferredSize());
		}

		Dimension prfBottom = new Dimension();
		if (sideBottom!=null) {
			prfBottom.setSize(sideBottom.getPreferredSize());
		}

		// horizontaler Versatz
		prfSize.x = 0;

		// vertikaler Versatz gegenüber dem Hauptelement (center)
		prfSize.y = 0;
		float gap = (float)indexGap/2f;
		if (sideTop!=null) {
			prfSize.y = axisHeight-(int)Math.ceil(gap)-prfTop.height;
			// Mindestversatz gegenüber dem Hauptelement garantieren:
			// Wichtig vor allem bei Mehrfachindizes (wie z. B. x^2^2)
			int minOffset = Math.round(xHeight*0.3f);
			prfSize.y = Math.min(-minOffset, prfSize.y);
		}
		else if (sideBottom!=null) {
			prfSize.y = axisHeight+(int)Math.floor(gap);
		}
		
		// Breite des Seitenteils
		prfSize.width = Math.max(prfTop.width, prfBottom.width);

		// Höhe des Seitenteils
		prfSize.height = prfTop.height + prfBottom.height;
		if (sideTop!=null && sideBottom!=null)
			prfSize.height += indexGap;
	}

	/**
	 * Hilfsmethode zur Berechnung der Größen des Inhaltsbereichs
	 * @param prfSize Rechteck mit bevorzugter Größe
	 * @param maxSize Rechteck mit maximaler Größe
	 */
	private void calcCenterSize(Rectangle prfSize, Rectangle maxSize) {
		prfSize.setBounds(0, 0, 0, 0);
		maxSize.setBounds(0, 0, 0, 0);

		// Bevorzugte und maximale Größe des Hauptelements
		Dimension prfCenter = new Dimension();
		Dimension maxCenter = new Dimension();
		if (center!=null) {
			prfCenter.setSize(center.getPreferredSize());
			maxCenter.setSize(center.getMaximumSize());
		}

        // Bevorzugte und maximale Größe des Elements über dem Hauptelement
        Dimension prfTop = new Dimension();
        Dimension maxTop = new Dimension();
        if (top!=null) {
            prfTop.setSize(top.getPreferredSize());
            prfTop.height += innerGaps.top;
            if (top.getMaximumSize().width==Integer.MAX_VALUE | top.getMaximumSize().height==Integer.MAX_VALUE)
                maxTop.setSize(Math.min(top.getMaximumSize().width, center.getMaximumSize().width),
                        Math.min(top.getMaximumSize().height, center.getMaximumSize().height));
            else
                maxTop.setSize(top.getMaximumSize());
            maxTop.height += innerGaps.top;
        }

		// Bevorzugte und maximale Größe des Elements unter dem Hauptelement
		Dimension prfBottom = new Dimension();
		Dimension maxBottom = new Dimension();
		if (bottom!=null) {
			prfBottom.setSize(bottom.getPreferredSize());
			prfBottom.height += innerGaps.bottom;
            if (bottom.getMaximumSize().width==Integer.MAX_VALUE)
                maxBottom.setSize(Math.min(bottom.getMaximumSize().width, center.getMaximumSize().width),
                        Math.min(bottom.getMaximumSize().height, center.getMaximumSize().height));
            else
                maxBottom.setSize(bottom.getMaximumSize());
			maxBottom.height += innerGaps.bottom;
		}

		// Bevorzugte Größe der Gesamtheit aller Elemente in diesem Seitenteil
		prfSize.setBounds(
			0, -prfTop.height,  // x, y
			max(prfTop.width, prfCenter.width, prfBottom.width),  // Breite
			prfTop.height + prfCenter.height + prfBottom.height  // Höhe
		);

		// Miximalgröße der Gesamtheit aller Elemente in diesem Seitenteil
		maxSize.setBounds(
			0, -prfTop.height,  // x, y
			max(maxTop.width, maxCenter.width, maxBottom.width),  // Breite
			maxTop.height + maxCenter.height + maxBottom.height  // Höhe
		);
	}

	/**
	 * Layout als ungültig erklären.
	 * @see java.awt.LayoutManager2#invalidateLayout(java.awt.Container)
	 */
	public void invalidateLayout(Container cont) {
		sizeValid = false;
	}

	/**
	 * Gibt die minimale Größe des übergebenen Behälters zurück.
	 * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
	 */
	public Dimension minimumLayoutSize(Container cont) {
		return preferredLayoutSize(cont);
	}

	/**
	 * Gibt die bevorzugte Größe des übergebenen Behälters zurück.
	 * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
	 */
	public Dimension preferredLayoutSize(Container cont) {
		calcLayoutSize(cont);
		return new Dimension(prfSize.width, prfSize.height);
	}

	/**
	 * Gibt die maximale Größe des übergebenen Behälters zurück.
	 * @see java.awt.LayoutManager2#maximumLayoutSize(java.awt.Container)
	 */
	public Dimension maximumLayoutSize(Container cont) {
		calcLayoutSize(cont);
		return new Dimension(maxSize.width, maxSize.height);
	}
	
	/**
	 * Legt den Abstand zu den Indices des Behälters fest.
	 * @param innerGaps Abstand vom Inhalt zu den äußeren Indices/Exponenten
	 * @param indexGap Abstand zwischen oberen und unteren Indices/Exponenten
	 */
	public void setGaps(Insets innerGaps, int indexGap) {
		this.innerGaps = (innerGaps!=null) ? innerGaps : new Insets(0,0,0,0);
		this.indexGap = indexGap;
		sizeValid = false;
	}

	/**
	 * Hilfsmethode zur Berechnung der Minima von drei Werten.
	 * @param a erster Wert
	 * @param b zweiter Wert
	 * @param c dritter Wert
	 * @return kleinster übergebener Wert
	 */
	private static int min(int a, int b, int c) {
		int min;
		min = Math.min(a, b);
		min = Math.min(c, min);
		return min;
	}

	/**
	 * Hilfsmethode zur Berechnung der Maxima von drei Werten.
	 * @param a erster Wert
	 * @param b zweiter Wert
	 * @param c dritter Wert
	 * @return größter übergebener Wert
	 */
	private static int max(int a, int b, int c) {
		int max;
		max = Math.max(a, b);
		max = Math.max(c, max);
		return max;
	}

}
