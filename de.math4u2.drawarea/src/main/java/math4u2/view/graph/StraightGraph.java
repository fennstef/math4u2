package math4u2.view.graph;

/**
 * <p>Überschrift: </p>
 * <p>Beschreibung: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Organisation: </p>
 * @author Dr. Weiss
 * @version 1.0
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.IScalarStringHolder;
import math4u2.view.graph.util.IVectorDoubleValueHolder;

/**
 * Graph einer Gerade
 */
public class StraightGraph extends AbstractSimpleGraph {
	private IScalarStringHolder name;
	private IVectorDoubleValueHolder point;
	private IVectorDoubleValueHolder vector;

	public StraightGraph(DrawAreaInterface da, IGraphSettings settings,
			IScalarStringHolder name, IVectorDoubleValueHolder point,
			IVectorDoubleValueHolder vector) {
		super(da, settings);
		this.name = name;
		this.point = point;
		this.vector = vector;
	}

	public void paintGraph(Graphics gr) {
		if (isVisible()) {
			Graphics2D g = (Graphics2D) gr;
			Color ca = g.getColor();

			g.setStroke(getStroke());
			g.setColor(getColor());

			double dx = 0, dy = 0, x = 0;
			try {
				double[] v = vector.getVector();
				dx = v[0];
				dy = v[1];
				x = point.getVector()[0];
			} catch (Exception e) {
				ExceptionManager.doError("Fehler beim Zeichnen der Geraden "
						+ getKey(), e);
				return;
			}

			if (dx == 0) {
				if (dy == 0)
					throw new RuntimeException("Die Strecke " + getKey()
							+ " hat eine ungültige Steigung");
				int ix = da.xCoordToPix(x);
				g.drawLine(ix, 0, ix, da.getHeight());
			} // if
			else {
				try {
					double xL = da.xPixToCoord(-1);
					double xR = da.xPixToCoord(da.getWidth() + 1);
					double yL = _computePoint(xL);
					double yR = _computePoint(xR);
					int xs = da.xCoordToPix(xL);
					int ys = da.yCoordToPix(yL);
					int xd = da.xCoordToPix(xR);
					int yd = da.yCoordToPix(yR);
					g.drawLine(xs, ys, xd, yd);
				} catch (Exception e) {
				}
			} // else
			g.setColor(ca);
		} // if visible
	} // paintGraph

	/** Berechnet für einen Abszissenwert die Ordinate */
	public double _computePoint(double x) throws Exception {
		// Formel: f(x):= m * (x - xp) + yp und m = deltaY / deltaY
		double[] v = vector.getVector();
		double[] p = point.getVector();
		double m = v[1] / v[0];
		return m * (x - p[0]) + p[1];
	} // _computePoint

	public String getKey() {
		return name.getScalarOrNull();
	}

	public void detach() throws Exception {
	}

} // StraightGraph
