package math4u2.view.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.IScalarDoubleHolder;

/**
 * Kreis-Graph
 */
public class CircleGraph extends AbstractAreaSimpleGraph {
	private String name;
	private IScalarDoubleHolder x;
	private IScalarDoubleHolder y;
	private IScalarDoubleHolder radius;

	public CircleGraph(DrawAreaInterface da, IAreaGraphSettings settings,
			String name, IScalarDoubleHolder x, IScalarDoubleHolder y,
			IScalarDoubleHolder radius) {
		super(da, settings);
		this.name = name;
		this.x = x;
		this.y = y;
		this.radius = radius;
	} 

	public void paintGraph(Graphics gr) {
		if (isVisible()) {
			Graphics2D g = (Graphics2D) gr;
			Color ca = g.getColor();
			g.setStroke(getStroke());
			g.setColor(getBorderColor());
			try {
				double radiusD = radius.getScalar();

				int x1 = da.xCoordToPix(x.getScalar() - radiusD);
				int y1 = da.yCoordToPix(y.getScalar() + radiusD);
				int x2 = (int) (2 * radiusD / da.xPerPix());
				int y2 = (int) (2 * radiusD / da.yPerPix());
				g.drawOval(x1, y1, x2, y2);

				if (getFillColor().getAlpha() != 0) {
					g.setColor(getFillColor());
					g.fillOval(x1, y1, x2, y2);
				}// if
			} catch (Exception e) {
			}
			g.setColor(ca);
		} // if visible
	} // paintGraph

	public String getKey() {
		return name;
	}

	public void detach() throws Exception {
	}
}