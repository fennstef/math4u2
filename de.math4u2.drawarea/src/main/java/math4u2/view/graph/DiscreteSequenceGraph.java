package math4u2.view.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.IScalarDoubleHolder;
import math4u2.view.graph.util.IScalarStringHolder;
import math4u2.view.graph.util.IVectorDoubleValueHolder;

public class DiscreteSequenceGraph extends AbstractAreaSimpleGraph {
	private String name;
	private IScalarStringHolder layout;
	private IVectorDoubleValueHolder xVector;
	private IVectorDoubleValueHolder yVector;
	private IScalarDoubleHolder radius;

	public DiscreteSequenceGraph(DrawAreaInterface da,
			IAreaGraphSettings settings, String name,
			IScalarStringHolder layout, IVectorDoubleValueHolder xVector,
			IVectorDoubleValueHolder yVector, IScalarDoubleHolder radius) {
		super(da, settings);
		this.name = name;
		this.layout = layout;
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
				String layoutStr = layout.getScalar();

				double[] xVectorD = xVector.getVector();
				double[] yVectorD = yVector.getVector();

				int r = (int) (radius.getScalar());
				if (r < 1)
					r = 1;
				int zweiR = 2 * r;

				int dim = Math.min(xVectorD.length, yVectorD.length);

				g.setColor(getBorderColor());

				if (layoutStr.compareTo("P") == 0
						|| layoutStr.compareTo("PS") == 0
						|| layoutStr.compareTo("SP") == 0) {

					for (int i = 0; i < dim; i++) {
						int x = da.xCoordToPix(xVectorD[i]);
						int y = da.yCoordToPix(yVectorD[i]);
						g.drawOval(x - r, y - r, zweiR, zweiR);
					}
				}

				if (layoutStr.compareTo("S") == 0
						|| layoutStr.compareTo("PS") == 0
						|| layoutStr.compareTo("SP") == 0) {
					GeneralPath gp = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
					gp.moveTo(da.xCoordToPix(xVectorD[0]),
							da.yCoordToPix(yVectorD[0]));
					for (int i = 1; i < dim; i++) {
						gp.lineTo(da.xCoordToPix(xVectorD[i]),
								da.yCoordToPix(yVectorD[i]));
					}
					g.draw(gp);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			g.setColor(ca);
		}
	}

	public String getKey() {
		return name;
	}

	public void detach() throws Exception {
	}
}
