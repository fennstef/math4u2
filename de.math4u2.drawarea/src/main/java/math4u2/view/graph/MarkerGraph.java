package math4u2.view.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.IScalarDoubleHolder;
import math4u2.view.graph.util.IScalarStringHolder;

/**
 * Marker-Graph
 */
public class MarkerGraph extends AbstractSimpleGraph {
	public enum Style {
		CIRCLE, SQUARE, CROSS1, CROSS2, CIRCLE_CROSS
	};

	private static final int RADIUS = 5;

	private IScalarDoubleHolder xPos;
	private IScalarDoubleHolder yPos;
	private IScalarStringHolder layout;

	public MarkerGraph(DrawAreaInterface da, IGraphSettings settings, IScalarStringHolder name,
			IScalarStringHolder layout, IScalarDoubleHolder xPos,
			IScalarDoubleHolder yPos) {
		super(da, settings, name);
		this.layout = layout;
		this.xPos = xPos;
		this.yPos = yPos;
	}

	public void paintGraph(Graphics gr) {
		if (isVisible()) {
			Graphics2D g = (Graphics2D) gr;
			Color ca = g.getColor();
			g.setStroke(getStroke());
			g.setColor(getColor());
			try {

				int x = da.xCoordToPix(xPos.getScalarOrNan());
				int y = da.yCoordToPix(yPos.getScalarOrNan());

				Style style = Style.valueOf(layout.getScalar().toUpperCase());
				if (Style.CIRCLE==style) {
					g.drawOval(x - RADIUS, y - RADIUS, 2 * RADIUS, 2 * RADIUS);
				} else if (Style.SQUARE==style) {
					g.drawRect(x - RADIUS, y - RADIUS, 2 * RADIUS, 2 * RADIUS);
				} else if (Style.CROSS1==style) {
					g.drawLine(x - RADIUS, y - RADIUS, x + RADIUS, y + RADIUS);
					g.drawLine(x - RADIUS, y + RADIUS, x + RADIUS, y - RADIUS);
				} else if (Style.CROSS2==style) {
					g.drawLine(x - RADIUS, y, x + RADIUS, y);
					g.drawLine(x, y + RADIUS, x, y - RADIUS);
				} else if (Style.CIRCLE_CROSS==style) {
					g.drawOval(x - RADIUS, y - RADIUS, 2 * RADIUS, 2 * RADIUS);
					g.drawLine(x - 2 * RADIUS, y, x + 2 * RADIUS, y);
					g.drawLine(x, y + 2 * RADIUS, x, y - 2 * RADIUS);
				}
			} catch (Exception e) {
			}
			g.setColor(ca);
		}
	}

	public void detach() throws Exception {
	}
}
