package math4u2.view.layout;

import java.awt.*;

import math4u2.view.formula.FormulaElement;

/**
 * <code>LayoutManager</code>, der den Inhalt eines Behälters
 * von matrixähnlich formatiert.
 * 
 * Zum Beispiel:<br/>
 * <pre>
 * +---+-----+---+
 * | A | B,C | D |
 * +---+-----+---+
 * | E |  F  | G |
 * +---+-----+---+
 * </pre>
 * 
 * @author Erich Seifert
 * @version $Revision: 1.6 $
 */
public class CompactGridLayout implements LayoutManager2 {
	private final int cols;
	private final int rows;
	private int hgap;
	private int vgap;

	private final Bounds hDims, vDims;

	private static final class Bounds {
		public int minimum;
		public int preferred;
		public int maximum;

		public void init() {
			minimum = 0;
			preferred = 0;
			maximum = 0;
		}
	}

	private final Bounds[] colDims;
	private final Bounds[] rowDims;

	private final float[] rowBaselines;
	
	private boolean sizeValid;
	
	/**
	 * Konstruktor.
	 * @param cols
	 * @param rows
	 */
	public CompactGridLayout(int cols, int rows) {
		this(cols, rows, 0, 0);
	}

	/**
	 * Konstruktor.
	 * @param cols
	 * @param rows
	 * @param hgap
	 * @param vgap
	 */
	public CompactGridLayout(int cols, int rows, int hgap, int vgap) {
		if (cols<1 || rows<1)
			throw new IllegalArgumentException("Number of cols or rows too small.");

		this.cols = cols;
		this.rows = rows;
		setGaps(hgap, vgap);

		hDims = new Bounds();
		vDims = new Bounds();

		colDims = new Bounds[cols];
		for (int col=0; col<cols; col++)
			colDims[col] = new Bounds();

		rowDims = new Bounds[rows];
		for (int row=0; row<rows; row++)
			rowDims[row] = new Bounds();
		
		rowBaselines = new float[rows];
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
	 * 
	 * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
	 */
	public void removeLayoutComponent(Component comp) {
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

		int i=0;
		int yRow = Math.max(insets.top, (cont.getHeight() - vDims.preferred)/2);

		for (int row=0; row<rows && i<numComps; row++) {
			int x = Math.max(insets.left, (cont.getWidth() - hDims.preferred)/2);
			int h = rowDims[row].preferred;

			for (int col=0; col<cols && i<numComps; col++) {
				Component c = cont.getComponent(i++);

				int w = colDims[col].preferred;
				int y = yRow;
				if (c instanceof FormulaElement) {
					FormulaElement f = (FormulaElement)c;
					y += Math.round(rowBaselines[row] - f.getBaseline());
				}

				c.setBounds(x,y,w,h);
				x += w+hgap;
			}

			yRow += h+vgap;
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
		return new Dimension(hDims.minimum, vDims.minimum);
	}

	/**
	 * 
	 * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
	 */
	public Dimension preferredLayoutSize(Container cont) {
		calcLayoutSize(cont);
		return new Dimension(hDims.preferred, vDims.preferred);
	}

	/**
	 * 
	 * @see java.awt.LayoutManager2#maximumLayoutSize(java.awt.Container)
	 */
	public Dimension maximumLayoutSize(Container cont) {
		calcLayoutSize(cont);
		return new Dimension(hDims.maximum, vDims.maximum);
	}
	
	private void calcLayoutSize(Container cont) {
		if (!sizeValid) {
			Insets insets = cont.getInsets();
			int numComps = cont.getComponentCount();
	
			hDims.init();
			vDims.init();
	
			if (numComps==0)
				return;
	
			for (int col=0; col<cols; col++) {
				colDims[col].init();
			}
	
			for (int row=0; row<rows; row++) {
				rowDims[row].init();
				rowBaselines[row] = 0f;
			}
	
			for (int i=0; i<numComps; i++) {
				Component c = cont.getComponent(i);
				Dimension minDim = c.getMinimumSize();
				Dimension prfDim = c.getPreferredSize();
				Dimension maxDim = c.getMaximumSize();
	
				int col = i%cols;
				Bounds colDim = colDims[col];
				colDim.minimum   = Math.max(minDim.width, colDim.minimum);
				colDim.preferred = Math.max(prfDim.width, colDim.preferred);
				colDim.maximum   = Math.max(maxDim.width, colDim.maximum);
	
				int row = i/cols;
				Bounds rowDim = rowDims[row];
				rowDim.minimum   = Math.max(minDim.height, rowDim.minimum);
				rowDim.preferred = Math.max(prfDim.height, rowDim.preferred);
				rowDim.maximum   = Math.max(maxDim.height, rowDim.maximum);

				if (c instanceof FormulaElement) {
					FormulaElement f = (FormulaElement)c;
					rowBaselines[row] = Math.max(f.getBaseline(), rowBaselines[row]);
				}
			}
	
			hDims.minimum = hDims.preferred = hDims.maximum = insets.left+insets.right + (cols-1)*hgap;
			for (int col=0; col<cols; col++) {
				hDims.minimum   += colDims[col].minimum;
				hDims.preferred += colDims[col].preferred;
				hDims.maximum   += colDims[col].maximum;
			}
	
			vDims.minimum = vDims.preferred = vDims.maximum = insets.top+insets.bottom + (rows-1)*vgap;
			for (int row=0; row<rows; row++) {
				vDims.minimum   += rowDims[row].minimum;
				vDims.preferred += rowDims[row].preferred;
				vDims.maximum   += rowDims[row].maximum;
			}

			sizeValid = true;
		}
	}

	public void setGaps(int hgap, int vgap) {
		this.hgap = hgap;
		this.vgap = vgap;

		sizeValid = false;
	}
}
