package math4u2.view.graph;

import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.IScalarDoubleHolder;
import math4u2.view.graph.util.IVectorDoubleValueHolder;

/**
 * Button für die Pfeilspitze
 * 
 * @author Fenn Stefan
 */
public class DragButtonArrow extends DragButtonAbstract {

	private IScalarDoubleHolder startX;
	private IScalarDoubleHolder startY;
	private IVectorDoubleValueHolder vector;
	private String name;

	public DragButtonArrow(DrawAreaInterface da, String name,
			IScalarDoubleHolder startX, IScalarDoubleHolder startY,
			IVectorDoubleValueHolder vector) {
		super(da);
		this.name = name;
		this.startX = startX;
		this.startY = startY;
		this.vector = vector;	
		init();
	}

	/**
	 * Berechnet die aktuelle Koordinaten des DragButtons und weißt sie x und y
	 * zu.
	 * 
	 * @throws MathException
	 */
	protected void computeCoordinates() {
		try {
			double[] v = vector.getVector();
			double sx = startX.getScalar();
			double sy = startY.getScalar();
			x = da.xCoordToPix((v[0]) + sx) - SIZE / 2;
			y = da.yCoordToPix((v[1]) + sy) - SIZE / 2;
		} catch (Exception e) {
			ExceptionManager.doError("Fehler beim Erstellen des Pfeil-Graphen "
					+ name, e);
		}
	}

	public void makeMouseDragged(java.awt.event.MouseEvent evt) {
		x = (int) b1.getLocation().getX() + evt.getX() - SIZE / 2;
		y = (int) b1.getLocation().getY() + evt.getY() - SIZE / 2;

		try {
			double newX = da.xPixToCoord(x) - startX.getScalar();
			double newY = da.yPixToCoord(y) - startY.getScalar();

			vector.setVector(new double[] { newX, newY }, true);
		} catch (Exception e) {
			ExceptionManager.doError("Fehler beim Setzen des Pfeil-Graphen "
					+ name, e);
		}

		b1.setVisible(false);
		b2.setVisible(true);
	}
}
