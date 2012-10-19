package math4u2.view.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.util.Iterator;
import java.util.LinkedList;

import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.IFunction1;
import math4u2.view.graph.util.IScalarDoubleHolder;
import math4u2.view.graph.util.IScalarStringHolder;
import math4u2.view.graph.util.SimpleScalarDoubleValueHolder;

/**
 * Graph einer parametrischen Kurve
 */
public class CurveGraph extends AbstractSimpleGraph {

	private boolean validate = false;

	/** Zwischenspeicher */
	private LinkedList<int[]> ll = new LinkedList<int[]>();

	private double xMin, xMax, yMin, yMax, width, height;

	private IScalarDoubleHolder minT;
	private IScalarDoubleHolder maxT;
	private IFunction1<IScalarDoubleHolder, IScalarDoubleHolder> xFunction;
	private IFunction1<IScalarDoubleHolder, IScalarDoubleHolder> yFunction;

	public CurveGraph(DrawAreaInterface da, IGraphSettings settings,
			IScalarStringHolder name, IScalarDoubleHolder minT, IScalarDoubleHolder maxT,
			IFunction1<IScalarDoubleHolder, IScalarDoubleHolder> xFunction,
			IFunction1<IScalarDoubleHolder, IScalarDoubleHolder> yFunction) {
		super(da, settings, name);
		this.minT = minT;
		this.maxT = maxT;
		this.xFunction = xFunction;
		this.yFunction = yFunction;
	}

	public void paintGraph(Graphics gr) {
		if (isVisible()) {
			Graphics2D g = (Graphics2D) gr;
			Color ca = g.getColor();
			g.setColor(getColor());
			g.setStroke(getStroke());
			Shape s = null;
			try {
				s = computeShape();
			} catch (Exception e) { /* e.printStackTrace(); */
			}
			if (s == null)
				return;
			g.draw(s);
			g.setColor(ca);
		} // if visible
	} // paintGraph

	public Shape computeShape() throws Exception {
		double parMin = Math.min(minT.getScalar(), maxT.getScalar());
		double parMax = Math.max(minT.getScalar(), maxT.getScalar());

		if (parMin == parMax) {
			GeneralPath gp = new GeneralPath();
			IScalarDoubleHolder sdr = new SimpleScalarDoubleValueHolder(parMin);
			IScalarDoubleHolder x = xFunction.eval(sdr);
			IScalarDoubleHolder y = yFunction.eval(sdr);
			gp.moveTo(da.xCoordToPix(x.getScalar()),
					da.yCoordToPix(y.getScalar()));
		}

		boolean reverse = false;

		if (minT.getScalar() > maxT.getScalar())
			reverse = true;

		if ((da.getXMin() != xMin) || (da.getXMax() != xMax)
				|| (da.getYMin() != yMin) || (da.getYMax() != yMax)
				|| (da.getWidth() != width) || (da.getHeight() != height))
			validate = false;
		// wenn noch nicht validiert ist, wird der Zwischenspeicher neu
		// berechnet

		if (!validate) {
			// computeCurve(parMin, parMax, 1/200f, reverse);
			computeCurve(parMin, parMax, reverse);
			validate = true;
		} // if !validate

		Iterator<int[]> iterator = ll.iterator();

		int[] ia = iterator.next();
		int xa = ia[0];
		int ya = ia[1];
		int xb = xa, yb = ya;

		GeneralPath gp = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
		gp.moveTo(xb, yb);

		int i;
		for (i = 1; iterator.hasNext(); i++) {
			ia = iterator.next();
			int x = ia[0];
			int y = ia[1];

			// jedes dritte mal kubische Spline zeichnen
			if (i % 3 == 0)
				gp.curveTo(xb, yb, xa, ya, x, y);
			// gp.lineTo(x,y);
			xb = xa;
			yb = ya;
			xa = x;
			ya = y;
		} // for

		i--;
		// restlichen Pfad zeichenen
		if (i % 3 == 1)
			gp.quadTo(xb, yb, xa, ya);
		else if (i % 3 == 2)
			gp.lineTo(xa, ya);
		return gp;
	} // computeShape

	public void renew() {
		validate = false;
	} // renew

	public void computeCurve(double parMin, double parMax, boolean reverse) {
		width = da.getWidth();
		height = da.getHeight();
		// nStep hat einen Wert zwischen 1000 und 200
		// bei detailMax=1 und detailMin=20
		int nStep = (int) ((20.0 - da.getDetail()) / 19.0 * 800.0) + 200;

		double inc = (parMax - parMin) / nStep;
		double time = parMin;
		xMin = da.getXMin();
		xMax = da.getXMax();
		yMin = da.getYMin();
		yMax = da.getYMax();
		ll.clear();

		IScalarDoubleHolder sdr = new SimpleScalarDoubleValueHolder(time);
		try {
			int x = da.xCoordToPix(xFunction.eval(sdr).getScalar());
			int y = da.yCoordToPix(yFunction.eval(sdr).getScalar());
			ll.add(new int[] { x, y });
			for (int c = 1; c <= nStep; c++, time += inc) {
				sdr = new SimpleScalarDoubleValueHolder(time);
				x = da.xCoordToPix(xFunction.eval(sdr).getScalar());
				y = da.yCoordToPix(yFunction.eval(sdr).getScalar());

				if (!reverse)
					ll.add(new int[] { x, y });
				else
					ll.addFirst(new int[] { x, y });
			} // for
			sdr = new SimpleScalarDoubleValueHolder(time);
			x = da.xCoordToPix(xFunction.eval(sdr).getScalar());
			y = da.yCoordToPix(yFunction.eval(sdr).getScalar());
			if (!reverse)
				ll.add(new int[] { x, y });
			else
				ll.addFirst(new int[] { x, y });
		} catch (Exception e) {
			ExceptionManager.doError("Fehler beim Zeichnen der Kurve "
					+ getIdentifier(), e);
		}
	} 

	public void detach() throws Exception {
	}

} 
