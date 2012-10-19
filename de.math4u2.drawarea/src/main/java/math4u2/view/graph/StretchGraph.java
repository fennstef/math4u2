package math4u2.view.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.IScalarStringHolder;
import math4u2.view.graph.util.IVectorDoubleValueHolder;

/**
 * Graph einer Strecke
 */

public class StretchGraph extends AbstractSimpleGraph {
	private IVectorDoubleValueHolder start;
	private IVectorDoubleValueHolder end;

	public StretchGraph(DrawAreaInterface da, IGraphSettings settings,
			IScalarStringHolder name, IVectorDoubleValueHolder start,
			IVectorDoubleValueHolder end) {
		super(da, settings, name);
		this.start = start;
		this.end = end;
	} // Konstruktor

	public void paintGraph(Graphics gr) {
		if (isVisible()) {
			Graphics2D g = (Graphics2D) gr;
			Color ca = g.getColor();
			g.setStroke(getStroke());
			g.setColor(getColor());

			double[] d = new double[4];
			double[] vs, ve;
			try {
				vs = start.getVector();
				ve = end.getVector();
			} catch (Exception e) {
				ExceptionManager.doError("Fehler beim Zeichnen der Strecke "
						+ getIdentifier(), e);
				return;
			}
			
			d[0] = vs[0];
			d[1] = vs[1];
			d[2] = ve[0];
			d[3] = ve[1];

			d = clipLine(d);

			if (d != null) {
				int xs = da.xCoordToPix(d[0]);
				int ys = da.yCoordToPix(d[1]);
				int xd = da.xCoordToPix(d[2]);
				int yd = da.yCoordToPix(d[3]);

				g.drawLine(xs, ys, xd, yd);
			}// if

			g.setColor(ca);
		} // if visible
	} // paintGraph

	/**
	 * Es wird zum Clippen der Algorithmus von Liang-Barsky benutzt
	 * 
	 * @param d
	 *            Punktkoordinaten
	 * @return neue Punktkoordinaten
	 */
	public double[] clipLine(double[] d) {
		double xMin = da.getXMin();
		double xMax = da.getXMax();
		double yMin = da.getYMin();
		double yMax = da.getYMax();

		double p1x = d[0];
		double p1y = d[1];
		double p2x = d[2];
		double p2y = d[3];

		double u1 = 0.0, u2 = 1.0, dx = p2x - p1x, dy;

		double[] test = clipTest(-dx, p1x - xMin, u1, u2);
		u1 = test[1];
		u2 = test[2];
		if (test[0] != 0) {
			test = clipTest(dx, xMax - p1x, u1, u2);
			u1 = test[1];
			u2 = test[2];
			if (test[0] != 0) {
				dy = p2y - p1y;
				test = clipTest(-dy, p1y - yMin, u1, u2);
				u1 = test[1];
				u2 = test[2];
				if (test[0] != 0)
					test = clipTest(dy, yMax - p1y, u1, u2);
				u1 = test[1];
				u2 = test[2];
				if (test[0] != 0) {
					if (u2 < 1.0) {
						p2x = p1x + u2 * dx;
						p2y = p1y + u2 * dy;
					}
					if (u1 > 0.0) {
						p1x += u1 * dx;
						p1y += u1 * dy;
					}

					return new double[] { p1x, p1y, p2x, p2y };
				}
			}
		}
		return null;
	}// clipLine

	private static double[] clipTest(double p, double q, double u1, double u2) {
		double r;
		boolean retval = true;

		if (p < 0.0) {
			r = q / p;
			if (r > u2)
				retval = false;
			else if (r > u1)
				u1 = r;
		} else if (p > 0.0) {
			r = q / p;
			if (r < u1)
				retval = false;
			else if (r < u2)
				u2 = r;
		} else
		/* p = 0, so line is parallel to this clipping edge */
		if (q < 0.0)
			/* Line is outside clipping edge */
			retval = false;

		return new double[] { (retval ? 1.0 : 0), u1, u2 };
	}

	public void detach() throws Exception {
	}
}
