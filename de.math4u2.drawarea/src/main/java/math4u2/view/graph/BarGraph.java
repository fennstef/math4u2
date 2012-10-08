package math4u2.view.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.IScalarDoubleHolder;
import math4u2.view.graph.util.IVectorDoubleValueHolder;

/**
 * Balken-Graph
 * 
 * @author M. Weiss
 */
public class BarGraph extends AbstractAreaSimpleGraph{
	private String name;
	private IVectorDoubleValueHolder xVector;
	private IVectorDoubleValueHolder yVector;
	private IScalarDoubleHolder thickness;

	public BarGraph(DrawAreaInterface da, IAreaGraphSettings settings,
			String name, IVectorDoubleValueHolder xVector,
			IVectorDoubleValueHolder yVector, IScalarDoubleHolder thickness) {
		super(da, settings);
		this.name = name;
		this.xVector = xVector;
		this.yVector = yVector;
		this.thickness = thickness;
	}

	public void paintGraph(Graphics gr) {
		if (isVisible()) {
			Graphics2D g = (Graphics2D) gr;
			IAreaGraphSettings set = (IAreaGraphSettings) settings;

			Color ca = g.getColor();
			g.setStroke(getStroke());

			try {
				double[] xVectorD = xVector.getVector();
				double[] yVectorD = yVector.getVector();

				int dim = Math.min(xVectorD.length, yVectorD.length);

				double thick = thickness.getScalar();

				if (thick < 0) {
					thick = 1;
					if (dim > 1) {
						thick = xVectorD[1] - xVectorD[0];
						for (int i = 2; i < dim; i++) {
							double dist = xVectorD[i] - xVectorD[i - 1];
							if (dist < thick)
								thick = dist;
						}
					}
				}

				for (int i = 0; i < dim; i++) {
					int x = da.xCoordToPix(xVectorD[i] - thick / 2);
					int y = da.yCoordToPix(yVectorD[i]);
					int y0 = da.yCoordToPix(0);
					int width = da.xCoordToPix(thick) - da.xCoordToPix(0);
					if (y0 - y > 0) {
						g.setColor(set.getFillColor());
						g.fillRect(x, y, width, y0 - y);
						g.setColor(set.getBorderColor());
						g.drawRect(x, y, width, y0 - y);
					} else {
						g.setColor(set.getFillColor());
						g.fillRect(x, y0, width, y - y0);
						g.setColor(set.getBorderColor());
						g.drawRect(x, y0, width, y - y0);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			g.setColor(ca);
		}
	} // paintGraph

	public String getKey() {
		return name;
	}

	public void detach() throws Exception {

	}

} // BarGraph
