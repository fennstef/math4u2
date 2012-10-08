package math4u2.view.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.Arrays;

import math4u2.util.exception.ExceptionManager;

/**
 * Layout, welches den verfügbaren Platz Prozentual verteilt. Es wird keine
 * Rücksicht darauf genommen, wieviel Platz eine Komponente benötigt
 * 
 * Beispiel-Layout ####|##|## 
 * 				   ####|##|## 
 *                 ---------- 
 *                 ####|####
 * 
 * layoutX [ [0.5, 0.25, 0.25], [0.5 , 0.5] ] layoutY [ 0.67, 0.33 ]
 * 
 * Werden später zu viele Komponent eingesetzt, wird eine
 * <code>ArrayOutOfBoundsException</code> geworfen.
 * 
 * @author Fenn Stefan
 */
public class PercentLayout implements LayoutManager {

	private double[][] layoutSchemeX;

	private double[] layoutSchemeY;

	public PercentLayout(double[][] layoutX, double[] layoutY) {
		init(layoutX, layoutY);
	}//Konstruktor 1

	/**
	 * Hier wird Layout-Y gleichverteilt, d.h. die Komponenten haben alle die
	 * gleiche Höhe
	 */
	public PercentLayout(double[][] layoutX) {
		int row = layoutX.length;
		double[] layoutY = new double[row];
		Arrays.fill(layoutY, 1.0 / row);
		init(layoutX, layoutY);
	}//Konstruktor 2

	private void init(double[][] layoutX, double[] layoutY) {
		layoutSchemeX = layoutX;
		layoutSchemeY = layoutY;

		if (layoutSchemeX.length != layoutSchemeY.length)
			throw new IllegalArgumentException("Das Layout ist ungültig:\n"
					+ toString());

		//Überprüfe layoutSchemeX
		for (int y = 0; y < layoutSchemeX.length; y++)
			_checkLayoutArray(layoutSchemeX[y], true);

		//Überprüfe layoutSchemY
		_checkLayoutArray(layoutSchemeY, true);
	}//init

	/**
	 * Überprüft einen Array mit %-Werten, ob dieser im Gesamten 100% ergibt.
	 * Andernfalls, wird dieser auf 100% angepasst
	 */
	private void _checkLayoutArray(double[] layout, boolean withMessage) {
		double ges = 0;
		for (int i = 0; i < layout.length; i++) {
			ges += layout[i];
		}//for
		if (ges > 1)
			throw new IllegalArgumentException("Der Layout-Array "
					+ printLayout(layout) + " geht über 100%");
		if (ges < 0.98) {
			if (withMessage)
				ExceptionManager.doError("DerLayout-Array " + printLayout(layout)
						+ " ist kleiner 100%. Layout wird angepasst");

			//Layout wird gleichverteilt
			for (int i = 0; i < layout.length; i++) {
				layout[i] = layout[i] * 1.0 / ges;
				if (Double.isNaN(layout[i]))
					layout[i] = 0;
			}//for

			if (withMessage)
				ExceptionManager.doError(" (Neu: " + printLayout(layout) + " )");
		}// if <1
	}//_checkLayoutArray

	public Dimension preferredLayoutSize(Container parent) {
		return new Dimension(50, 50);
	} //preferredLayoutSize

	/* Verlangt von LayoutManager. */
	public Dimension minimumLayoutSize(Container parent) {
		return preferredLayoutSize(parent);
	}

	/** Dies ist die eigentliche Verteilungsmethode */
	public void layoutContainer(Container parent) {

		Insets insets = parent.getInsets();
		int nComps = parent.getComponentCount();
		int width = parent.getSize().width - insets.right - insets.left;
		int height = parent.getSize().height - insets.bottom - insets.top;
		int previousWidth = 0, previousHeight = 0;
		int x = insets.left, y = insets.top;

		double[][] layoutX = getTempXLayoutScheme(parent);
		double[] layoutY = getTempYLayoutScheme(layoutX);

		setSizes(parent);
		int row = 0, col = 0;
		x = insets.left;
		y = insets.top;
		previousWidth = 0;
		previousHeight = 0;
		for (int i = 0; i < nComps; i++) {
			Component c = parent.getComponent(i);

			//Eventueller Umbruch zur nächsten Zeile
			if (col >= layoutX[row].length) {
				row++;
				col = 0;
				previousWidth = 0;
				x = insets.left;
				y += previousHeight;
			}//if

			int curHeight = (int) (height * layoutY[row]);

			Dimension d = new Dimension((int) (width * layoutX[row][col]),
					curHeight);
			x += previousWidth;

			// Endgültiges setzen der Koordinaten und der Größe
			c.setBounds(x, y, d.width, d.height);

			col++;
			previousWidth = d.width;
			previousHeight = d.height;
		}//for
	}//layoutContainer

	/**
	 * Diese Methode erzeugt das aktuelle Layout-Schema, da zur Laufzeit
	 * Komponenten unsichtbar geschalten werden könnten.
	 * 
	 * @return Layout-Schema Array
	 */
	private double[][] getTempXLayoutScheme(Container parent) {
		double[][] tmpXLayout = cloneArray(layoutSchemeX);

		int nComps = parent.getComponentCount();
		int row = 0, col = 0;
		for (int i = 0; i < nComps; i++) {
			Component c = parent.getComponent(i);

			//Eventueller Umbruch zur nächsten Zeile
			if (col >= layoutSchemeX[row].length) {
				row++;
				col = 0;
			}//if

			//Wenn nicht sichtbar 0%
			if (!c.isVisible()) {
				tmpXLayout[row][col] = 0;
			}//if

			col++;
		}//for

		//Überprüfe layoutSchemeX
		for (int y = 0; y < tmpXLayout.length; y++)
			_checkLayoutArray(tmpXLayout[y], false);

		return tmpXLayout;
	}//getTempXLayoutScheme

	/**
	 * Diese Methode erzeugt das aktuelle Layout-Schema, da zur Laufzeit
	 * Komponenten unsichtbar geschalten werden könnten.
	 * 
	 * @return Layout-Schema Array
	 */
	private double[] getTempYLayoutScheme(double[][] layoutX) {
		double[] tmpYLayout = cloneArray(layoutSchemeY);
		for (int y = 0; y < layoutX.length; y++) {
			boolean isEmpty = true;
			for (int x = 0; x < layoutX[y].length; x++) {
				if (layoutX[y][x] != 0) {
					isEmpty = false;
					break;
				}//if
			}//for x
			if (isEmpty)
				tmpYLayout[y] = 0;
		}//for y
		_checkLayoutArray(tmpYLayout, false);

		return tmpYLayout;
	}//getTempXLayoutScheme

	private static double[][] cloneArray(double[][] src) {
		double[][] target = new double[src.length][];
		for (int i = 0; i < src.length; i++) {
			target[i] = cloneArray(src[i]);
		}//for i
		return target;
	}//cloneArray

	private static double[] cloneArray(double[] src) {
		double[] target = new double[src.length];
		for (int i = 0; i < src.length; i++) {
			target[i] = src[i];
		}//for i
		return target;
	}//cloneArray

	public String toString() {
		return getClass().getName() + "\nLayout-Scheme X: "
				+ printLayout(layoutSchemeX) + "\nLayout-Scheme Y: "
				+ printLayout(layoutSchemeY);
	}//toString

	private String printLayout(double[] layout) {
		String s = "[";
		for (int i = 0; i < layout.length; i++) {
			s += layout[i] * 100 + "%";
			if (i != layout.length - 1)
				s += " ,";
		}//for
		s += "]";
		return s;
	}//printLayout

	private String printLayout(double[][] array) {
		String s = "[ ";
		for (int i = 0; i < array.length; i++) {
			s += printLayout(array[i]) + " ";
			if (i != array.length - 1)
				s += ",";
		}//for
		s += "]";
		return s;
	}//printLayout
	
    public double[][] getLayoutSchemeX() {
        return layoutSchemeX;
    }//getLayoutSchemeX
    
    public double[] getLayoutSchemeY() {
        return layoutSchemeY;
    }//getLayoutSchemeY	

	/* Verlangt von LayoutManager. */
	public void addLayoutComponent(String name, Component comp) {
	}

	public void removeLayoutComponent(Component comp) {
	}

	private void setSizes(Container parent) {
	}
}//PercentLayout
