package math4u2.view.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.IScalarDoubleHolder;
import math4u2.view.graph.util.IScalarStringHolder;
import math4u2.view.graph.util.IVectorDoubleValueHolder;

/**
 * Pfeil-Graph
 */
public class ArrowGraph extends AbstractSimpleGraph {

	/** Grafische Interaktionen (Button) */
	private DragButtonArrow dragVector;
	
	private IScalarDoubleHolder startX;
	private IScalarDoubleHolder startY;
	private IVectorDoubleValueHolder vector;
	
	public ArrowGraph(DrawAreaInterface da, IGraphSettings settings, IScalarStringHolder name,IScalarDoubleHolder startX,
			IScalarDoubleHolder startY,IVectorDoubleValueHolder vector) {
		super(da, settings, name);
		this.startX = startX;
		this.startY = startY;
		this.vector = vector;
		dragVector = new DragButtonArrow(da, name, startX, startY, vector);
	} 

	public void paintGraph(Graphics gr) {
		dragVector.setVisible(isVisible());
		if (!isVisible())
			return;

		Graphics2D g = (Graphics2D) gr;
		Color ca = g.getColor();
		g.setColor(getColor());
		g.setStroke(getStroke());		
		try {
			double xsd = startX.getScalar();
			double ysd = startY.getScalar();
			int xs = da.xCoordToPix(xsd);
			int ys = da.yCoordToPix(ysd);

			double[] vec = vector.getVector();
			double _x = vec[0];
			double _y = vec[1];

			int xd = da.xCoordToPix(xsd + _x);
			int yd = da.yCoordToPix(ysd + _y);

			g.drawLine(xs, ys, xd, yd);

			if (_x != 0 || _y != 0) {
				double dxp = _x/da.xPerPix();
				double dyp = _y/da.yPerPix();
				double norm = Math.sqrt(dxp*dxp + dyp *dyp);
				double xn = dxp / norm * 20;
				double yn = -dyp / norm * 20;

				int x1 = (int) (xd - 0.866 * xn + 0.5 * yn);
				int y1 = (int) (yd - 0.5 * xn - 0.866 * yn);
				int x2 = (int) (xd - 0.866 * xn - 0.5 * yn);
				int y2 = (int) (yd + 0.5 * xn - 0.866 * yn);

				g.fillPolygon(new int[] { xd, x1, x2 },
						new int[] { yd, y1, y2 }, 3);
			}//if
		} catch (Exception e) {
			ExceptionManager.doError("Fehler beim Zeichnen des Pfeil-Graphen "+getIdentifier(),e);
		}
		g.setColor(ca);

		//Zeichnet den DragVector auch
		renew();
	} //paintGraph

	public void setVisible(boolean b) {
		super.setVisible(b);
		dragVector.setVisible(b);
	} //setVisible

	public void renew() {
		dragVector.renew();
	} //renew

	public void detach() throws Exception {
		dragVector.remove();
		//beim letzten mal zeichen verstecken
		setVisible(false);
	}
}

