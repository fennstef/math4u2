// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises.ui;

import java.awt.geom.GeneralPath;
import javax.swing.*;

/**
 * Klasse für ein beliebig größenveränderbares Kontrollkästchen.
 * 
 * @version 0.1
 * @author Erich Seifert
 */
public class ResizableCheckBox extends JCheckBox {
	/**
	 * Konstruktor der ein neues ResizableCheckBox-Objekt erzeugt.
	 * 
	 * @param width
	 *            Breite des neuen Kontrollkästchens
	 * @param height
	 *            Höhe des neuen Kontrollkästchens
	 */
	public ResizableCheckBox(int width, int height) {
		setSize(width, height);

		GeneralPath crossSymbol = new GeneralPath(GeneralPath.WIND_NON_ZERO, 53);
		GeneralPath circleSymbol = new GeneralPath(GeneralPath.WIND_EVEN_ODD,
				10);

		/*
		 * // Rechteck außen und innen erzeugen rectSymbol.moveTo(0.07500f,
		 * 0.92500f); rectSymbol.lineTo(0.92500f, 0.92500f);
		 * rectSymbol.lineTo(0.92500f, 0.07500f); rectSymbol.lineTo(0.07500f,
		 * 0.07500f); rectSymbol.lineTo(0.07500f, 0.92500f);
		 * rectSymbol.closePath();
		 * 
		 * rectSymbol.moveTo(0.15000f, 0.15000f); rectSymbol.lineTo(0.15000f,
		 * 0.85000f); rectSymbol.lineTo(0.85000f, 0.85000f);
		 * rectSymbol.lineTo(0.85000f, 0.15000f); rectSymbol.lineTo(0.15000f,
		 * 0.15000f); rectSymbol.closePath();
		 */

		// Kreisumrisse innen und außen erzeugen
		circleSymbol.moveTo(0.50000f, 1.00000f);
		circleSymbol.curveTo(0.77538f, 1.00000f, 1.00000f, 0.77538f, 1.00000f,
				0.50000f);
		circleSymbol.curveTo(1.00000f, 0.22462f, 0.77538f, 0.00000f, 0.50000f,
				0.00000f);
		circleSymbol.curveTo(0.22462f, 0.00000f, 0.00000f, 0.22462f, 0.00000f,
				0.50000f);
		circleSymbol.curveTo(0.00000f, 0.77538f, 0.22462f, 1.00000f, 0.50000f,
				1.00000f);
		circleSymbol.closePath();

		circleSymbol.moveTo(0.50000f, 0.92500f);
		circleSymbol.curveTo(0.73408f, 0.92500f, 0.92500f, 0.73408f, 0.92500f,
				0.50000f);
		circleSymbol.curveTo(0.92500f, 0.26592f, 0.73408f, 0.07500f, 0.50000f,
				0.07500f);
		circleSymbol.curveTo(0.26592f, 0.07500f, 0.07500f, 0.26592f, 0.07500f,
				0.50000f);
		circleSymbol.curveTo(0.07500f, 0.73408f, 0.26592f, 0.92500f, 0.50000f,
				0.92500f);
		circleSymbol.closePath();

		// Kreuzumriss erzeugen
		crossSymbol.moveTo(0.39515f, 0.51346f);
		crossSymbol.lineTo(0.46502f, 0.43687f);
		crossSymbol.lineTo(0.34674f, 0.33006f);
		crossSymbol.lineTo(0.15691f, 0.16742f);
		crossSymbol.lineTo(0.03258f, 0.09156f);
		crossSymbol.lineTo(0.01028f, 0.07334f);
		crossSymbol.lineTo(0.00000f, 0.03692f);
		crossSymbol.lineTo(0.00588f, 0.00000f);
		crossSymbol.lineTo(0.09780f, 0.05591f);
		crossSymbol.lineTo(0.18406f, 0.11203f);
		crossSymbol.lineTo(0.29693f, 0.19584f);
		crossSymbol.lineTo(0.38402f, 0.26941f);
		crossSymbol.lineTo(0.51343f, 0.38486f);
		crossSymbol.lineTo(0.57108f, 0.32597f);
		crossSymbol.lineTo(0.68055f, 0.22417f);
		crossSymbol.lineTo(0.71697f, 0.19345f);
		crossSymbol.lineTo(0.75318f, 0.16479f);
		crossSymbol.lineTo(0.78905f, 0.13834f);
		crossSymbol.lineTo(0.82455f, 0.11442f);
		crossSymbol.lineTo(0.85951f, 0.09322f);
		crossSymbol.lineTo(0.89395f, 0.07505f);
		crossSymbol.lineTo(0.92317f, 0.06216f);
		crossSymbol.lineTo(0.94665f, 0.05315f);
		crossSymbol.lineTo(0.97852f, 0.06809f);
		crossSymbol.lineTo(1.00000f, 0.11597f);
		crossSymbol.lineTo(0.94722f, 0.13151f);
		crossSymbol.lineTo(0.91813f, 0.14434f);
		crossSymbol.lineTo(0.88710f, 0.16072f);
		crossSymbol.lineTo(0.85491f, 0.18022f);
		crossSymbol.lineTo(0.82174f, 0.20258f);
		crossSymbol.lineTo(0.78782f, 0.22760f);
		crossSymbol.lineTo(0.71823f, 0.28451f);
		crossSymbol.lineTo(0.64743f, 0.34893f);
		crossSymbol.lineTo(0.56356f, 0.43317f);
		crossSymbol.lineTo(0.61676f, 0.48475f);
		crossSymbol.lineTo(0.74285f, 0.61932f);
		crossSymbol.lineTo(0.82814f, 0.72196f);
		crossSymbol.lineTo(0.87874f, 0.79012f);
		crossSymbol.lineTo(0.92150f, 0.85480f);
		crossSymbol.lineTo(0.94242f, 0.87944f);
		crossSymbol.lineTo(0.92766f, 0.92695f);
		crossSymbol.lineTo(0.89673f, 0.92759f);
		crossSymbol.lineTo(0.87026f, 0.89917f);
		crossSymbol.lineTo(0.82972f, 0.83786f);
		crossSymbol.lineTo(0.78127f, 0.77258f);
		crossSymbol.lineTo(0.69866f, 0.67316f);
		crossSymbol.lineTo(0.51583f, 0.48346f);
		crossSymbol.lineTo(0.43929f, 0.56735f);
		crossSymbol.lineTo(0.26063f, 0.78163f);
		crossSymbol.lineTo(0.13321f, 0.94364f);
		crossSymbol.lineTo(0.06876f, 0.99058f);
		crossSymbol.lineTo(0.00376f, 1.00000f);
		crossSymbol.lineTo(0.39515f, 0.51346f);
		crossSymbol.closePath();

		GeneralPath[] checkedSymbol = { crossSymbol, circleSymbol };

		int curWidth = getSize().width;
		int curHeight = getSize().height;
		int symbolSize = (curWidth > curHeight) ? curHeight : curWidth;

		ResizableIcon nIcon = new ResizableIcon(circleSymbol, symbolSize,
				symbolSize);
		ResizableIcon sIcon = new ResizableIcon(checkedSymbol, symbolSize,
				symbolSize);

		setIcon(nIcon);
		setSelectedIcon(sIcon);
	}

	/**
	 * Standardkonstruktor der ein neues ResizableCheckBox-Objekt erzeugt.
	 */
	public ResizableCheckBox() {
		this(15, 15);
	}
}