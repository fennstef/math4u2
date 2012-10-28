//Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises.ui;

import java.awt.geom.*;

/**
 * <code>Graphics2D</code>-Rechteck, mit abgerundeten Ecken, wobei für
 * jede Ecke ein separater Radius angegeben werden kann.
 *
 * @version 0.1
 * @author Erich Seifert
 * @see java.awt.Graphics2D
 */
public class RoundedRect extends Rectangle2D.Double
{
	/**
	 * Die vom Benutzer gewünschten Radien
	 */
	private float
	nwRadius = 20f,
	swRadius = 20f,
	seRadius = 20f,
	neRadius = 20f;

	/**
	 * Konstruktor, der ein neues Rechteck mit abgerundeten Ecken erzeugt.
	 *
	 * @param x horizontale Position des Rechtecks
	 * @param y vertikale Position des Rechtecks
	 * @param w Breite des Rechtecks
	 * @param h Höhe des Rechtecks
	 * @param northWest Rundungsradius der linken oberen Ecke
	 * @param southWest Rundungsradius der linken unteren Ecke
	 * @param southEast Rundungsradius der rechten unteren Ecke
	 * @param northEast Rundungsradius der rechten oberen Ecke
	 */
	public RoundedRect(double x,double y,double w,double h, double northWest,double southWest,double southEast,double northEast)
	{
		super(x,y,w,h);
		setArcSizes(northWest, southWest, southEast, northEast);
	}

	/**
	 * Konstruktor, der ein neues Rechteck mit abgerundeten Ecken erzeugt.
	 *
	 * @param x horizontale Position des Rechtecks
	 * @param y vertikale Position des Rechtecks
	 * @param w Breite des Rechtecks
	 * @param h Höhe des Rechtecks
	 * @param arcSize Rundungsradius der Ecken
	 */
	public RoundedRect(double x,double y,double w,double h,double arcSize) { this(x,y,w,h,arcSize,arcSize,arcSize,arcSize); }

	/**
	 * Setzt die gewünschten Radien der Ecken.
	 *
	 * @param northWest Rundungsradius der linken oberen Ecke
	 * @param southWest Rundungsradius der linken unteren Ecke
	 * @param southEast Rundungsradius der rechten unteren Ecke
	 * @param northEast Rundungsradius der rechten oberen Ecke
	 */
	public void setArcSizes(double northWest, double southWest, double southEast, double northEast)
	{
		nwRadius = (float)northWest;
		swRadius = (float)southWest;
		seRadius = (float)southEast;
		neRadius = (float)northEast;
	}

	/**
	 * Gibt die Umrisslinie des Rechtecks zurück.
	 *
	 * @return Umrisslinie als GeneralPath-Objekt
	 */
	private GeneralPath createOutline()
	{
		GeneralPath shape = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 9);

		// Hilfsvariablen
		float
		ulCornerX = (float)x,
		ulCornerY = (float)y,
		lrCornerX = (float)(x+width),
		lrCornerY = (float)(y+height);

		// Radien begrenzen
		float maximum = (width>height) ? (float)height/2f : (float)width/2f;
		float
		nwArc = (nwRadius<=maximum) ? nwRadius : maximum,
		swArc = (swRadius<=maximum) ? swRadius : maximum,
		seArc = (seRadius<=maximum) ? seRadius : maximum,
		neArc = (neRadius<=maximum) ? neRadius : maximum;

		// für Steuerpunkte der Bézier-Kurven
		float
		nwCtl = nwArc*0.5f,
		swCtl = swArc*0.5f,
		seCtl = seArc*0.5f,
		neCtl = neArc*0.5f;

		shape.moveTo(ulCornerX+nwArc,ulCornerY);
		shape.curveTo(ulCornerX+nwCtl,ulCornerY,ulCornerX,ulCornerY+nwCtl,ulCornerX,ulCornerY+nwArc);
		shape.lineTo(ulCornerX,lrCornerY-swArc);
		shape.curveTo(ulCornerX,lrCornerY-swCtl, ulCornerX+swCtl,lrCornerY,ulCornerX+swArc,lrCornerY);
		shape.lineTo(lrCornerX-seArc,lrCornerY);
		shape.curveTo(lrCornerX-seCtl,lrCornerY,lrCornerX,lrCornerY-seCtl,lrCornerX,lrCornerY-seArc);
		shape.lineTo(lrCornerX,ulCornerY+neArc);
		shape.curveTo(lrCornerX,ulCornerY+neCtl,lrCornerX-neCtl,ulCornerY,lrCornerX-neArc,ulCornerY);
		shape.lineTo(ulCornerX+nwArc,ulCornerY);
		shape.closePath();

		return shape;
	}

	/**
	 * Gibt ein PathIterator-Objekt zum Zeichnen zurück.
	 *
	 * @param at Transformation in der das Objekt dezeichnet werden soll
	 * @return Eine neuer PathIterator zur Darstellung auf dem Bildschirm
	 */
	public PathIterator getPathIterator(AffineTransform at)
	{
		GeneralPath shape = createOutline();
		return shape.getPathIterator(at);
	}
}
