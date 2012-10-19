package math4u2.view.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.CubicCurve2D;

import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.IScalarDoubleHolder;
import math4u2.view.graph.util.IScalarStringHolder;

/**
 * Bezier-Graph
 */

public class BezierGraph extends AbstractSimpleGraph {
	private IScalarDoubleHolder startPointX;
	private IScalarDoubleHolder startPointY;
	private IScalarDoubleHolder startDirX;
	private IScalarDoubleHolder startDirY;
	private IScalarDoubleHolder endPointX;
	private IScalarDoubleHolder endPointY;
	private IScalarDoubleHolder endDirX;
	private IScalarDoubleHolder endDirY;

	public BezierGraph(DrawAreaInterface da, IGraphSettings settings,
			IScalarStringHolder name, IScalarDoubleHolder startPointX,
			IScalarDoubleHolder startPointY, IScalarDoubleHolder startDirX,
			IScalarDoubleHolder startDirY, IScalarDoubleHolder endPointX,
			IScalarDoubleHolder endPointY, IScalarDoubleHolder endDirX,
			IScalarDoubleHolder endDirY) {
		super(da, settings, name);
		this.startPointX = startPointX;
		this.startPointY = startPointY;
		this.startDirX = startDirX;
		this.startDirY = startDirY;
		this.endPointX = endPointX;
		this.endPointY = endPointY;
		this.endDirX = endDirX;
		this.endDirY = endDirY;
	} // Konstruktor

	public void paintGraph(Graphics gr) {
		if (isVisible()) {
			Graphics2D g = (Graphics2D) gr;
			Color ca = g.getColor();
			g.setStroke(getStroke());
			g.setColor(getColor());

			try {
				int xs = da.xCoordToPix(startPointX.getScalar());
				int ys = da.yCoordToPix(startPointY.getScalar());
				int xsdir = da.xCoordToPix(startDirX.getScalar());
				int ysdir = da.yCoordToPix(startDirY.getScalar());
				int xedir = da.xCoordToPix(endDirX.getScalar());
				int yedir = da.yCoordToPix(endDirY.getScalar());
				int xe = da.xCoordToPix(endPointX.getScalar());
				int ye = da.yCoordToPix(endPointY.getScalar());

				g.draw(new CubicCurve2D.Double(xs, ys, xsdir, ysdir, xedir,
						yedir, xe, ye));
			} catch (Exception e) {
				e.printStackTrace();
			}// catch

			g.setColor(ca);
		} // if visible
	} // paintGraph
	
	public void detach() throws Exception {
	}
} // class BezierGraph
