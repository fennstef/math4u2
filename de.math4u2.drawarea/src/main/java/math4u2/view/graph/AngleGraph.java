package math4u2.view.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;

import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.IScalarDoubleHolder;
import math4u2.view.graph.util.IScalarStringHolder;
import math4u2.view.graph.util.IVectorDoubleValueHolder;

/**
 * Winkel-Graph
 * 
 * @author Christoph Beckmann
 */
public class AngleGraph extends AbstractSimpleGraph {
	private IVectorDoubleValueHolder vector1;
	private IVectorDoubleValueHolder vector2;
	private IScalarDoubleHolder x;
	private IScalarDoubleHolder y;
	private IScalarDoubleHolder radius;

	public AngleGraph(DrawAreaInterface da, IAreaGraphSettings settings, IScalarStringHolder name,
			IVectorDoubleValueHolder vector1, IVectorDoubleValueHolder vector2,
			IScalarDoubleHolder x, IScalarDoubleHolder y,
			IScalarDoubleHolder radius) {
		super(da, settings, name);
		this.vector1 = vector1;
		this.vector2 = vector2;
		this.x = x;
		this.y = y;
		this.radius = radius;
	} // Konstruktor

	public void paintGraph(Graphics gr) {
		IAreaGraphSettings aSetting = (IAreaGraphSettings) settings;
		if (isVisible()) {
			Graphics2D g = (Graphics2D) gr;

			Color ca = g.getColor();
			g.setStroke(getStroke());

			// ---------------------------------------
			// Den Bogen zeichnen
			try {
				double distance = radius.getScalar();

				double[] v1 = vector1.getVector();
				double[] v2 = vector2.getVector();
				double arcBegin = Math.toDegrees(Math.atan2(v1[1], v1[0]));
				double arcEnd = Math.toDegrees(Math.atan2(v2[1], v2[0]));
				double arc = arcEnd - arcBegin;
				if (arc <= -180) {
					arc = 360 + arc;
				} else if (arc > 180) {
					arc = arc - 360;
				}

				if (arcBegin != arcEnd) {
					Arc2D arcShape = new Arc2D.Double(da.xCoordToPix(x
							.getScalar() - distance), da.yCoordToPix(y
							.getScalar() + distance), 2 * distance
							/ da.xPerPix(), 2 * distance / da.yPerPix(),
							arcBegin, arc, Arc2D.OPEN);
					if (hasFillColor(aSetting)) {
						arcShape.setArcType(Arc2D.PIE);
						g.setColor(aSetting.getFillColor());
						g.fill(arcShape);
						arcShape.setArcType(Arc2D.OPEN);
						g.setColor(aSetting.getBorderColor());
					}
					g.draw(arcShape);

					// Dreieck zeichnen dass die Richtung des Winkels angibt

					double xs = 0, ys = 0, xv = 0, yv = 0, xn = 0, yn = 0, xd = 0, yd = 0;

					xs = x.getScalar();
					ys = y.getScalar();

					double _x = v2[0];
					double _y = v2[1];

					xv = xs + _x;
					yv = ys + _y;

					double xdiff = xv - xs;
					double ydiff = yv - ys;

					double norm = Math.sqrt(xdiff * xdiff + ydiff * ydiff);

					xd = xs + xdiff / norm * distance;
					yd = ys + ydiff / norm * distance;

					if (arc <= 180 && arc > 0) {
						xn = -ydiff / norm;
						yn = xdiff / norm;
					} else {
						xn = ydiff / norm;
						yn = -xdiff / norm;
					}

					double size = 0.05 / da.xPerPix();

					int x1 = da.xCoordToPix(xd);
					int y1 = da.yCoordToPix(yd);
					int x2 = da.xCoordToPix(xd + (-0.866 * xn + 0.5 * yn)
							/ size);
					int y2 = da.yCoordToPix(yd + (-0.5 * xn - 0.866 * yn)
							/ size);
					int x3 = da.xCoordToPix(xd + (-0.866 * xn - 0.5 * yn)
							/ size);
					int y3 = da
							.yCoordToPix(yd + (0.5 * xn - 0.866 * yn) / size);

					g.fillPolygon(new int[] { x1, x2, x3 }, new int[] { y1, y2,
							y3 }, 3);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			g.setColor(ca);
		} // if visible
	} // paintGraph

	private boolean hasFillColor(IAreaGraphSettings aSetting) {
		return aSetting.getFillColor().getAlpha()!=0;
	}
}