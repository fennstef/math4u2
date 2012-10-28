package math4u2.view.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;

import math4u2.util.formulaUtils.Pair;
import math4u2.view.formula.BinBox;
import math4u2.view.formula.BracketedBox;
import math4u2.view.formula.FormulaElement;
import math4u2.view.formula.FormulaRenderContext;
import math4u2.view.formula.GridBox;
import math4u2.view.formula.InnerBox;
import math4u2.view.formula.InteractionBox;
import math4u2.view.formula.OpBox;
import math4u2.view.formula.OrdBox;
import math4u2.view.formula.RelBox;
import math4u2.view.formula.SliderBox;

/**
 * FormulaLayout ist ein <code>LayoutManager</code>, der die
 * gespeicherten Elemente auf eine Grundlinie ausrichtet.
 * Diese Grundlinie wird mit der Methode <code>getBaseline()</code>
 * abgefragt bzw. muss die Schnittstelle <code>FormulaElement</code>
 * implementieren. <code>AtomicBox</code> implementiert diese
 * standardmäßig. Andernfalls wird die Höhe des jeweiligen
 * Elements als Grundlinie genommen.
 */

public class FormulaLayout implements LayoutManager2 {
	private int hgap;
	private int minWidth;
	private int minHeight;
	private float maxAscend;
	private float maxDescend;

	private static final Map spacing;
	private boolean sizeValid;

	static {
	    // Abstände zwischen linkem und rechtem Element
		spacing = new HashMap();

		// BinBox
		spacing.put(new Pair(BinBox.class, OrdBox.class),           new Float(4f));
		spacing.put(new Pair(BinBox.class, InteractionBox.class),   new Float(4f));
		spacing.put(new Pair(BinBox.class, OpBox.class),            new Float(4f));
		spacing.put(new Pair(BinBox.class, BracketedBox.class),     new Float(4f));
		spacing.put(new Pair(BinBox.class, InnerBox.class),         new Float(4f));
		spacing.put(new Pair(BinBox.class, SliderBox.class),        new Float(4f));

		// BracketedBox
		spacing.put(new Pair(BracketedBox.class, OrdBox.class),     new Float(3f));
		spacing.put(new Pair(BracketedBox.class, OpBox.class),      new Float(3f));
		spacing.put(new Pair(BracketedBox.class, BinBox.class),     new Float(4f));
		spacing.put(new Pair(BracketedBox.class, RelBox.class),     new Float(5f));
		spacing.put(new Pair(BracketedBox.class, InnerBox.class),   new Float(3f));
		spacing.put(new Pair(BracketedBox.class, GridBox.class),    new Float(3f));

		// GridBox
		spacing.put(new Pair(GridBox.class, OrdBox.class),          new Float(3f));
		spacing.put(new Pair(GridBox.class, InteractionBox.class),  new Float(3f));
		spacing.put(new Pair(GridBox.class, OpBox.class),           new Float(3f));
		spacing.put(new Pair(GridBox.class, BinBox.class),          new Float(4f));
		spacing.put(new Pair(GridBox.class, RelBox.class),          new Float(5f));
		spacing.put(new Pair(GridBox.class, BracketedBox.class),    new Float(3f));
		spacing.put(new Pair(GridBox.class, InnerBox.class),        new Float(3f));
		spacing.put(new Pair(GridBox.class, GridBox.class),         new Float(3f));

		// InnerBox
		spacing.put(new Pair(InnerBox.class, OrdBox.class),         new Float(3f));
		spacing.put(new Pair(InnerBox.class, InteractionBox.class), new Float(3f));
		spacing.put(new Pair(InnerBox.class, OpBox.class),          new Float(3f));
		spacing.put(new Pair(InnerBox.class, BinBox.class),         new Float(4f));
		spacing.put(new Pair(InnerBox.class, RelBox.class),         new Float(5f));
		spacing.put(new Pair(InnerBox.class, BracketedBox.class),   new Float(3f));
		spacing.put(new Pair(InnerBox.class, InnerBox.class),       new Float(3f));
		spacing.put(new Pair(InnerBox.class, GridBox.class),        new Float(3f));

		// OpBox
		spacing.put(new Pair(OpBox.class, OrdBox.class),            new Float(3f));
		spacing.put(new Pair(OpBox.class, InteractionBox.class),    new Float(3f));
		spacing.put(new Pair(OpBox.class, OpBox.class),             new Float(3f));
		spacing.put(new Pair(OpBox.class, RelBox.class),            new Float(5f));
		spacing.put(new Pair(OpBox.class, InnerBox.class),          new Float(3f));

		// OrdBox
		spacing.put(new Pair(OrdBox.class, OpBox.class),            new Float(3f));
		spacing.put(new Pair(OrdBox.class, BinBox.class),           new Float(4f));
		spacing.put(new Pair(OrdBox.class, RelBox.class),           new Float(5f));
		spacing.put(new Pair(OrdBox.class, InnerBox.class),         new Float(3f));
		spacing.put(new Pair(OrdBox.class, GridBox.class),          new Float(3f));

		// RelBox
		spacing.put(new Pair(RelBox.class, OrdBox.class),           new Float(5f));
		spacing.put(new Pair(RelBox.class, InteractionBox.class),   new Float(5f));
		spacing.put(new Pair(RelBox.class, OpBox.class),            new Float(5f));
		spacing.put(new Pair(RelBox.class, BracketedBox.class),     new Float(5f));
		spacing.put(new Pair(RelBox.class, InnerBox.class),         new Float(5f));

		// InteractionBox
		spacing.put(new Pair(InteractionBox.class, OpBox.class),    new Float(3f));
		spacing.put(new Pair(InteractionBox.class, BinBox.class),   new Float(4f));
		spacing.put(new Pair(InteractionBox.class, RelBox.class),   new Float(5f));
		spacing.put(new Pair(InteractionBox.class, InnerBox.class), new Float(3f));
		spacing.put(new Pair(InteractionBox.class, GridBox.class),  new Float(3f));
		
		// SliderBox
		spacing.put(new Pair(SliderBox.class, OpBox.class),         new Float(3f));
		spacing.put(new Pair(SliderBox.class, BinBox.class),        new Float(4f));
		spacing.put(new Pair(SliderBox.class, RelBox.class),        new Float(5f));
		spacing.put(new Pair(SliderBox.class, InnerBox.class),      new Float(3f));
		spacing.put(new Pair(SliderBox.class, GridBox.class),       new Float(3f));
	}

	protected static final float getSpacing(FormulaElement right, FormulaElement left) {
		Float sp = (Float)spacing.get(new Pair(right.getSpacingClass(), left.getSpacingClass()));
		if (sp!=null)
			return sp.floatValue()*FormulaRenderContext.MU*right.getDisplayHeight();
		else
			return 0f;
	}

	public FormulaLayout() {
		this(0);
	}

	public FormulaLayout(int hgap) {
		this.hgap = hgap;
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
	 * Adds the specified component to the layout.
	 * This method does nothing in this implementation.
	 * @param comp Component to be added
	 * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String, java.awt.Component)
	 */
	public void addLayoutComponent(String name, Component comp) {
		sizeValid = false;
	}

	/**
	 * 
	 * @see java.awt.LayoutManager2#addLayoutComponent(java.awt.Component, java.lang.Object)
	 */
	public void addLayoutComponent(Component comp, Object constraint) {
		sizeValid = false;
	}

	/**
	 * Removes the specified component from the layout.
	 * This method does nothing in this implementation.
	 * @param comp Component to be removed
	 * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
	 */
	public void removeLayoutComponent(Component comp) {
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

		int x = Math.max(insets.left, (cont.getWidth() - minWidth)/2);
		for (int i = 0; i < numComps; i++) {
			Component c  = cont.getComponent(i);
			Dimension prfDim = c.getPreferredSize();
			Dimension maxDim = c.getMaximumSize();

			int y;
			if (maxDim.getHeight()>=cont.getHeight()) {
				y = 0;
			}
			else {
				float baseline; 
				if (c instanceof FormulaElement)
					baseline = ((FormulaElement)c).getBaseline();
				else
					baseline = (float)prfDim.getHeight()*c.getAlignmentY();
				y = Math.round(maxAscend - baseline) + insets.top;
			}

			int w = prfDim.width;
			int h = Math.min(maxDim.height, cont.getHeight());

			c.setBounds(x, y, w, h);
			x += w;

			if (i < numComps-1) {
				x += hgap;
				Component cNext = cont.getComponent(i+1);
				if ((c instanceof FormulaElement) && (cNext instanceof FormulaElement))
					x += getSpacing((FormulaElement)c, (FormulaElement)cNext);
			}
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
		return new Dimension(minWidth,minHeight);
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
		// FIXME: Warum wird die Layoutgröße nicht richtig gepuffert (true)
		if (true || !sizeValid) {
			int numComps = cont.getComponentCount();
	
			if (numComps<=0)
				return;
	
			Insets insets = cont.getInsets();
	
			minWidth   = 0;
			maxAscend  = 0;
			maxDescend = 0;
	
			for (int i = 0; i < numComps; i++) {
				Component c = cont.getComponent(i);
				Dimension dim = c.getPreferredSize();
	
				float baseline;
				if (c instanceof FormulaElement) {
					baseline = ((FormulaElement)c).getBaseline();
				}
				else {
					baseline = (float)dim.getHeight()*c.getAlignmentY();
				}
				maxAscend = Math.max(baseline, maxAscend);
				maxDescend = Math.max(dim.height-baseline, maxDescend);
	
				minWidth += dim.width;
				if (i < numComps-1) {
					minWidth += hgap;
					Component cNext = cont.getComponent(i+1);
					if ((c instanceof FormulaElement) && (cNext instanceof FormulaElement))
						minWidth += getSpacing((FormulaElement)c, (FormulaElement)cNext);
				}
			}
			minWidth += insets.left + insets.right;
			minHeight = insets.top + (int)Math.ceil(maxAscend+maxDescend) + insets.bottom;
			sizeValid = true;
		}
	}

}
