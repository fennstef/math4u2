package math4u2.view.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;

import math4u2.view.formula.FormulaElement;

/**
 * <code>LayoutManager</code>, der umklammerte Elemente auf eine
 * Grundlinie ausrichtet und dabei Ober- und Unterlängen  - z.B. durch
 * Exponenten, Indices oder Dekrationen - berücksichtigt.
 * Es können nur drei Elemente vom Layout verwaltet werden:
 * eine öffnende Klammer, der Inhalt und die schließende Klammer
 */
public class BracketLayout implements LayoutManager2 {
	/** Positionsangabe für Klammer links */
	public static final String LEFT_BRACKET = "left";
	/** Positionsangabe für Klammer rechts */
	public static final String RIGHT_BRACKET = "right";
	/** Positionsangabe für den Hauptinhalt */
	public static final String CENTER = "center";

	/** Horizontaler Abstand zwischen Klammern und Inhalt. */
	private int hgap;
	/** Vertikaler Überstand der Klammern gegenüber dem Inhalt. */
	private int voffset;

	private Component bracketLeft;
	private Component bracketRight;
	private Component content;

	/** Statusvariable, um Layout-Berechnungen zu puffern. */
	private boolean sizeValid;
	private int minWidth;
	private int minHeight;

	private float maxAscend;
	private float maxDescend;
	private float maxRealAscend;
	private float maxRealDescend;

	/**
	 * Konsturktor der ein neues Objekt vom Typ <code>BracketLayout</code>
	 * erzeugt und initialisiert.
	 */
	public BracketLayout() {
		this(0, 0);
	}

	/**
	 * Konsturktor der ein neues Objekt vom Typ <code>BracketLayout</code>
	 * erzeugt und initialisiert.
	 * @param hgap Horizontaler Abstand zwischen Klammern und Inhalt
	 * @param voffset Vertikaler Überstand der Klammern gegenüber dem Inhalt
	 */
	public BracketLayout(int hgap, int voffset) {
		this.hgap = hgap;
		this.voffset = voffset;
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
		sizeValid = false;
	}

	/**
	 * 
	 * @see java.awt.LayoutManager2#addLayoutComponent(java.awt.Component, java.lang.Object)
	 */
	public void addLayoutComponent(Component comp, Object constraint) {
		if (!(comp instanceof FormulaElement))
			throw new IllegalArgumentException("This layout can only handle components that implement 'FormulaElement'.");

		if (constraint==LEFT_BRACKET) {
			bracketLeft = comp;
		}
		else if (constraint==RIGHT_BRACKET) {
			bracketRight = comp;
		}
		else if (constraint==CENTER) {
			content = comp;
		}
		else {
			throw new IllegalArgumentException("Wrong value for layout contraint.");
		}
		sizeValid = false;
	}

	/**
	 * Removes the specified component from the layout.
	 * This method does nothing in this implementation.
	 * @param comp Component to be removed
	 * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
	 */
	public void removeLayoutComponent(Component comp) {
		if (comp==bracketLeft) {
			bracketLeft = null;
		}
		else if (comp==bracketRight) {
			bracketRight = null;
		}
		else if (comp==content) {
			content = null;
		}
		sizeValid = false;
	}

	/**
	 * Lays out the container in the specified container.
	 * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
	 */
	public void layoutContainer(Container cont) {
		int numComps = cont.getComponentCount();

		if (numComps<=0)
			return;

		calcLayoutSize(cont);
		Insets insets = cont.getInsets();
		
		int x, y, w, h;
		
		x = insets.left;

		if (bracketLeft!=null) {
		    w = bracketLeft.getPreferredSize().width;
		    h = Math.round(maxRealAscend + maxRealDescend) + 2*voffset;
		    y = Math.round(maxAscend - maxRealAscend) + insets.top;

		    bracketLeft.setBounds(x, y, w, h);
		    x += w;
		    if (content!=null || bracketRight!=null) x += hgap;
		}

		if (content!=null) {
		    w = content.getPreferredSize().width;
		    h = content.getPreferredSize().height;
			FormulaElement elem = (FormulaElement)content;
		    y = Math.round(maxAscend-elem.getBaseline()) + insets.top + voffset;

		    content.setBounds(x, y, w, h);
		    x += w;
		}

		if (bracketRight!=null) {
		    w = bracketRight.getPreferredSize().width;
		    h = Math.round(maxRealAscend + maxRealDescend) + 2*voffset;
		    y = Math.round(maxAscend - maxRealAscend) + insets.top;

		    if (content!=null || bracketLeft!=null) x += hgap;
		    bracketRight.setBounds(x, y, w, h);
		}
	}

	/**
	 * 
	 * @see java.awt.LayoutManager2#invalidateLayout(java.awt.Container)
	 */
	public void invalidateLayout(Container cont) {
		sizeValid = false;
	}

	/**
	 * 
	 * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
	 */
	public Dimension minimumLayoutSize(Container cont) {
		calcLayoutSize(cont);
		return new Dimension(minWidth, minHeight);
	}

	/**
	 * 
	 * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
	 */
	public Dimension preferredLayoutSize(Container cont) {
		return minimumLayoutSize(cont);
	}

	/**
	 * 
	 * @see java.awt.LayoutManager2#maximumLayoutSize(java.awt.Container)
	 */
	public Dimension maximumLayoutSize(Container cont) {
		return minimumLayoutSize(cont);
	}

	/**
	 * Hilfsmethode, die die Maße bestimmt, die für das Layout
	 * herangezogen werden. Zum Beispiel: größte Ober- und Unterlänge
	 * oder Mindesthöhe und -breite.
	 * @param cont <code>Container</code>-Objekt für das die Maße
	 * berechnet werden sollen
	 */
	private void calcLayoutSize(Container cont) {
		if (!sizeValid) {
			int numComps = cont.getComponentCount();
	
			if (numComps<=0)
				return;
	
			Insets insets = cont.getInsets();
			
			minWidth   = 0;
			minHeight  = 0;
			maxAscend  = 0f;
			maxDescend = 0f;
			maxRealAscend  = 0f;
			maxRealDescend = 0f;

			if (bracketLeft!=null) {
				Dimension dim = bracketLeft.getPreferredSize();
				minWidth += dim.width;
				if (content!=null)
				    minWidth += hgap;
				FormulaElement elem = (FormulaElement)bracketLeft;
				maxAscend  = Math.max(maxAscend, elem.getBaseline());
				maxDescend = Math.max(maxDescend, dim.height-elem.getBaseline());
				maxRealAscend  = Math.max(maxRealAscend, elem.getRealAscend());
				maxRealDescend = Math.max(maxRealDescend, elem.getRealDescend());
			}

			if (content!=null) {
				Dimension dim = content.getPreferredSize();
				minWidth += dim.width;
				FormulaElement elem = (FormulaElement)content;
				maxAscend  = Math.max(maxAscend, elem.getBaseline());
				maxDescend = Math.max(maxDescend, dim.height-elem.getBaseline());
				maxRealAscend  = Math.max(maxRealAscend, elem.getRealAscend());
				maxRealDescend = Math.max(maxRealDescend, elem.getRealDescend());
			}

			if (bracketRight!=null) {
				Dimension dim = bracketRight.getPreferredSize();
				minWidth += dim.width;
				if (content!=null || bracketLeft!=null)
				    minWidth += hgap;
				FormulaElement elem = (FormulaElement)bracketRight;
				maxAscend  = Math.max(maxAscend, elem.getBaseline());
				maxDescend = Math.max(maxDescend, dim.height-elem.getBaseline());
				maxRealAscend  = Math.max(maxRealAscend, elem.getRealAscend());
				maxRealDescend = Math.max(maxRealDescend, elem.getRealDescend());
			}

			minWidth += insets.left + insets.right;
			minHeight = insets.top + (int)Math.ceil(maxAscend+maxDescend) + insets.bottom + 2*voffset;

			sizeValid = true;
		}
	}

	/**
	 * Legt die Abstände innerhalb des Layouts fest.
	 * @param hgap Horizontaler Abstand zwischen Klammern und Inhalt
	 * @param voffset Vertikaler Überstand der Klammern gegenüber dem Inhalt
	 */
	public void setGaps(int hgap, int voffset) {
		this.hgap = hgap;
		this.voffset = voffset;
		sizeValid = false;
	}
}
