package math4u2.view.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.IScalarStringHolder;
import math4u2.view.graph.util.IVectorDoubleValueHolder;

/**
 * Graph der die Vektorelemente als Balkendiagramm anzeigt.
 * 
 * @author Fenn Stefan
 */
public class VectorElementGraph extends AbstractSimpleGraph {
	private static final float HALF_THICKNESS = 0.35f;

	private IScalarStringHolder name;
	private IVectorDoubleValueHolder vector;

	public VectorElementGraph(DrawAreaInterface da, IGraphSettings settings,
			IScalarStringHolder name, IVectorDoubleValueHolder vector) {
		super(da, settings);
		this.name = name;
		this.vector = vector;
	}

	public void paintGraph(Graphics gr) {
		if (!isVisible()) {
			return;
		}
		Graphics2D g = (Graphics2D) gr;
		Color ca = g.getColor();
		g.setStroke(getStroke());

		try {
			double[] vdr = vector.getVector();
			for (int i = 0; i < vdr.length; i++) {
				int xS = da.xCoordToPix(i + 1 - HALF_THICKNESS);
				int xE = da.xCoordToPix(i + 1 + HALF_THICKNESS);
				int yS = da.yCoordToPix(0);
				int yE = da.yCoordToPix(vdr[i]);

				Polygon polygon = new Polygon(new int[] { xS, xE, xE, xS, xS },
						new int[] { yS, yS, yE, yE, yS }, 4);
				g.setColor(Color.BLACK);
				g.drawPolygon(polygon);
				Color c = getColor();
				g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 128));
				g.fillPolygon(polygon);
			}// for i

		} catch (Exception e) {
			ExceptionManager.doError("Fehler beim Zeichnen des Vektor-Graphen "
					+ getKey(), e);
		}
		g.setColor(ca);
	}

	public String getKey() {
		return name.getScalarOrNull();
	}

	public void detach() throws Exception {
	}
}