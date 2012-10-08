package math4u2.view.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.IFunction2;
import math4u2.view.graph.util.IScalarDoubleHolder;
import math4u2.view.graph.util.IScalarStringHolder;
import math4u2.view.graph.util.IVectorDoubleValueHolder;
import math4u2.view.graph.util.SimpleScalarDoubleValueHolder;

/**
 * @author Dr. Weiss
 * 
 */
public class FieldVectorGraph extends AbstractSimpleGraph {
	private String name;
	private IScalarStringHolder layout;
	private IScalarDoubleHolder refX;
	private IScalarDoubleHolder refY;
	private IScalarDoubleHolder distX;
	private IScalarDoubleHolder distY;
	private IScalarDoubleHolder cutoff;
	private IFunction2<IVectorDoubleValueHolder, IScalarDoubleHolder, IScalarDoubleHolder> fieldFunction;

	public FieldVectorGraph(
			DrawAreaInterface da,
			IGraphSettings settings,
			String name,
			IScalarStringHolder layout,
			IScalarDoubleHolder refX,
			IScalarDoubleHolder refY,
			IScalarDoubleHolder distX,
			IScalarDoubleHolder distY,
			IScalarDoubleHolder cutoff,
			IFunction2<IVectorDoubleValueHolder, IScalarDoubleHolder, IScalarDoubleHolder> fieldFunction

	) {
		super(da, settings);
		this.name = name;
		this.layout = layout;
		this.refX = refX;
		this.refY = refY;
		this.distX = distX;
		this.distY = distY;
		this.cutoff = cutoff;
		this.fieldFunction = fieldFunction;
	} // Konstruktor

	public void paintGraph(Graphics gr) {
		IScalarDoubleHolder xPos = new SimpleScalarDoubleValueHolder(Double.NaN);
		IScalarDoubleHolder yPos = new SimpleScalarDoubleValueHolder(Double.NaN);

		double xGridDiff = 1;
		double yGridDiff = 1;
		double cutoffD = 1;
		double xRef;
		double yRef;

		boolean hasArrow = true;
		boolean isNormed = false;
		boolean hasPoint = false;

		if (isVisible()) {

			try {
				String layoutStr = layout.getScalar();

				hasArrow = layoutStr.indexOf('V') >= 0;
				isNormed = layoutStr.indexOf('N') >= 0;
				hasPoint = layoutStr.indexOf('P') >= 0;

				xGridDiff = distX.getScalar();
				yGridDiff = distY.getScalar();
				cutoffD = cutoff.getScalar();
				cutoffD = cutoffD * cutoffD;
				xRef = refX.getScalar();
				yRef = refY.getScalar();
			} catch (Exception e) {
				ExceptionManager.doError(
						"Fehler beim Zeichnen des Feldvektors", e);
				return;
			}// catch

			Graphics2D g = (Graphics2D) gr;

			Color ca = g.getColor();
			Stroke str = g.getStroke();

			g.setStroke(getStroke());
			g.setColor(getColor());

			// Gitter erstellen und Feld zeichnen
			double radius = 0.6 * Math.sqrt(xGridDiff * xGridDiff + yGridDiff
					* yGridDiff);

			
			double xDiff = da.getXMax() - da.getXMin();
			double yDiff = da.getYMax() - da.getYMin();

			double xMin = da.getXMin();

			double xStartValue = xRef - Math.ceil((xRef - xMin) / xGridDiff)
					* xGridDiff;
			int xNumber = (int) Math.round(Math.floor(xDiff / xGridDiff)) + 2;
			double yMin = da.getYMin();
			double yStartValue = yRef - Math.ceil((yRef - yMin) / yGridDiff)
					* yGridDiff;
			int yNumber = (int) Math.round(Math.floor(yDiff / yGridDiff)) + 2;

			Object[] arg = new Object[] { xPos, yPos };
			double[] result;
			double xValue;
			double normFac = 1;

			if (isNormed) {
				double maxNorm = 0;
				xValue = xStartValue;

				for (int ix = 0; ix < xNumber; ix++) {
					double yValue = yStartValue;
					for (int iy = 0; iy < yNumber; iy++) {
						try {
							xPos.setScalar(xValue, false);
							yPos.setScalar(yValue, false);
						} catch (Exception e1) {
							ExceptionManager.doError(e1);
						}	

						double xResult = 0;
						double yResult = 0;

						try {
							result = fieldFunction.eval(xPos, yPos).getVector();
							xResult = result[0];
							yResult = result[1];
							double nn = xResult * xResult + yResult * yResult;
							if (nn > cutoffD) {
								yValue += yGridDiff;
								break;
							}
							if (nn > maxNorm)
								maxNorm = nn;
						} catch (Exception e) {
							ExceptionManager.doError(
									"Fehler beim Zeichnen des Feldvektors", e);
						}// catch

						yValue += yGridDiff;
					}
					xValue += xGridDiff;
				} // for
				normFac = radius / Math.sqrt(maxNorm);
			}

			xValue = xStartValue;
			for (int ix = 0; ix < xNumber; ix++) {
				double yValue = yStartValue;
				for (int iy = 0; iy < yNumber; iy++) {
					try {
						xPos.setScalar(xValue, false);
						yPos.setScalar(yValue, false);
					} catch (Exception e1) {
						ExceptionManager.doError(e1);
					}					

					double xResult = 0;
					double yResult = 0;

					try {
						result = fieldFunction.eval(xPos, yPos).getVector();
						xResult = result[0];
						yResult = result[1];
						if (xResult * xResult + yResult * yResult > cutoffD) {
							yValue += yGridDiff;
							continue;
						}
					} catch (Exception e) {
						ExceptionManager.doError(
								"Fehler beim Zeichnen des Feldvektors", e);
					}// catch

					if (hasPoint) {
						g.fillOval(da.xCoordToPix(xValue) - 3,
								da.yCoordToPix(yValue) - 3, 6, 6);
					}

					if (xResult != 0.0 || yResult != 0.0) {

						if (isNormed) {
							xResult *= normFac;
							yResult *= normFac;
						}
						double xDelta = 0.5 * xResult;
						double yDelta = 0.5 * yResult;
						int x1 = da.xCoordToPix(xValue + xDelta);
						int y1 = da.yCoordToPix(yValue + yDelta);
						int x2 = da.xCoordToPix(xValue - xDelta);
						int y2 = da.yCoordToPix(yValue - yDelta);

						g.drawLine(x1, y1, x2, y2);
						if (hasArrow) {
							x2 = da.xCoordToPix(xValue + 0.74 * xDelta + 0.15
									* yDelta);
							y2 = da.yCoordToPix(yValue + 0.74 * yDelta - 0.15
									* xDelta);
							g.drawLine(x1, y1, x2, y2);
							x2 = da.xCoordToPix(xValue + 0.74 * xDelta - 0.15
									* yDelta);
							y2 = da.yCoordToPix(yValue + 0.74 * yDelta + 0.15
									* xDelta);
							g.drawLine(x1, y1, x2, y2);
						}
					}
					yValue += yGridDiff;
				}
				xValue += xGridDiff;
			} // for

			g.setColor(ca);
			g.setStroke(str);
		} // if visible
	} // paintGraph

	public String getKey() {
		return name;
	}

	public void detach() throws Exception {
	}
}
