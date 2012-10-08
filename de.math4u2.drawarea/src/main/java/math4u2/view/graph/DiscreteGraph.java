package math4u2.view.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.IScalarDoubleHolder;
import math4u2.view.graph.util.IVectorDoubleValueHolder;

/**
 * Diskreter Graph
 * 
 * @author M. Weiss
 */
public class DiscreteGraph extends AbstractAreaSimpleGraph {
	private String name;
	private IVectorDoubleValueHolder xVector;
	private IVectorDoubleValueHolder yVector;
	private IScalarDoubleHolder radius;

	public DiscreteGraph(DrawAreaInterface da, IAreaGraphSettings settings,
			String name, IVectorDoubleValueHolder xVector,
			IVectorDoubleValueHolder yVector, IScalarDoubleHolder radius) {
		super(da, settings);
		this.name = name;
		this.xVector = xVector;
		this.yVector = yVector;
		this.radius = radius;
	}

	public void paintGraph(Graphics gr) {
		if (isVisible()) {
			Graphics2D g = (Graphics2D) gr;

			Color ca = g.getColor();
			g.setStroke(getStroke());

			try {
				double[] xVectorD = xVector.getVector();
				double[] yVectorD = yVector.getVector();

				int r = (int) (radius.getScalar());
				if (r < 1)
					r = 1;
				int zweiR = 2 * r;

				int dim = Math.min(xVectorD.length, yVectorD.length);

				g.setColor(getBorderColor());

				for (int i = 0; i < dim; i++) {
					int x = da.xCoordToPix(xVectorD[i]);
					int y = da.yCoordToPix(yVectorD[i]);

					g.drawOval(x - r, y - r, zweiR, zweiR);

				}
			} catch (Exception e) {
				ExceptionManager.doError(e);
			}
			g.setColor(ca);
		}
	}

	public String getKey() {
		return name;
	}

	public void detach() throws Exception {
	}

} // DiscreteGraph
