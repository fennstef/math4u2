package math4u2.view.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.drawarea.DrawAreaConstants;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.IScalarDoubleHolder;
import math4u2.view.graph.util.IScalarStringHolder;

/**
 * @author Fenn Stefan
 */
public class DragButtonPoint extends DragButtonAbstract implements DrawAreaConstants {
	private IScalarStringHolder layout;
	private IScalarDoubleHolder xPos;
	private IScalarDoubleHolder yPos;
	private IScalarStringHolder name;
	private IScalarStringHolder index;

	public DragButtonPoint(DrawAreaInterface da, IScalarStringHolder layout,
			IScalarDoubleHolder xPos, IScalarDoubleHolder yPos,
			IScalarStringHolder name, IScalarStringHolder index) {
		super(da);
		this.layout = layout;
		this.xPos = xPos;
		this.yPos = yPos;
		this.name = name;
		this.index = index;

		init();
	}

	private int getXInt() throws Exception {
		double xd = xPos.getScalar();
		return da.xCoordToPix(xd) - SIZE / 2;
	}// getXInt

	private int getYInt() throws Exception {
		double yd = yPos.getScalar();
		return da.yCoordToPix(yd) - SIZE / 2;
	}// getYInt
	
	/**
	 * Berechnet die aktuelle Koordinaten des DragButtons und weiﬂt sie x und y
	 * zu.
	 * 
	 * @throws MathException
	 */
	protected void computeCoordinates() {
		try {
			x = da.xCoordToPix(xPos.getScalar()) - SIZE / 2;
			y = da.yCoordToPix(yPos.getScalar()) - SIZE / 2;
		} catch (Exception e) {
			ExceptionManager.doError("Fehler beim Erstellen des Punkt-Graphen "
					+ name, e);
		}
	}

	public void makeMouseDragged(MouseEvent evt) {
		x = (int) b1.getLocation().getX() + evt.getX();
		y = (int) b1.getLocation().getY() + evt.getY();
		if (!xPos.isFixed()) {
			try {
				xPos.setScalar(da.xPixToCoord(x), false);
			} catch (Exception e) {
				ExceptionManager.doError(
						"Fehler beim Zeichnen des Punkt-Graphen " + getName(),
						e);
			} 
		} 
		if (!yPos.isFixed()) {
			try {
				yPos.setScalar(da.yPixToCoord(y), false);
			} catch (Exception e) {
				ExceptionManager.doError(
						"Fehler beim Zeichnen des Punkt-Graphen " + getName(),
						e);
			} 
		} 
		//Set x again with propagate update
		try {
			xPos.setScalar(da.xPixToCoord(x), true);
		} catch (Exception e) {
			ExceptionManager.doError(
					"Fehler beim Zeichnen des Punkt-Graphen " + getName(),
					e);
		}
		
		b1.setVisible(false);
		b2.setVisible(true);
	} // makeMouseDragged

	public void paint(Graphics gr, Color c) {
		Graphics2D g = (Graphics2D) gr;
		g.setRenderingHints(antialiasOn); // AntiAliasing aktivieren
		Color ac = g.getColor();

		try {
			x = getXInt();
			y = getYInt();
			x = limitRangeX(x);
			y = limitRangeY(y);			
			
			PointDrawStyle style = PointDrawStyle.valueOf(layout.getScalarOrNull().toUpperCase());
			style.paint(c, g, x, y, SIZE, name, index);
		} catch (Exception e) {
			ExceptionManager.doError("Fehler beim Zeichnen des Punkt-Graphen "
					+ getName(), e);
		}
		g.setColor(ac);
	} // paintGraph
	
	public int getX() {
		return x + SIZE / 2;
	}

	public int getY() {
		return y + SIZE / 2;
	}
	
	public String getName() {
		if(index==null) return name.getScalarOrNull();
		return name.getScalarOrNull() + "[" + index.getScalarOrNull() + "]";
	}

}
