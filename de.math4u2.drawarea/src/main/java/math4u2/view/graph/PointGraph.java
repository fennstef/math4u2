package math4u2.view.graph;

import java.awt.Graphics;

import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.IScalarDoubleHolder;
import math4u2.view.graph.util.IScalarStringHolder;

/**
 * Grafischer Darsteller von AffPoint <br>
 * <br>
 * Es wird als verschiebbarer Punkt zwei Buttens benutzt. Während der eine
 * gedrückt wird, wird der andere Button verschoben. Danach wird die Sichbarkeit
 * getauscht und die Koordinaten werden korrigiert.
 * 
 */

public class PointGraph extends AbstractSimpleGraph {
	private DragButtonPoint db;

	public PointGraph(DrawAreaInterface da, IGraphSettings settings,
			IScalarStringHolder layout, IScalarDoubleHolder xPos,
			IScalarDoubleHolder yPos, IScalarStringHolder name,
			IScalarStringHolder index) {
		super(da, settings, name);
		db = new DragButtonPoint(da, layout, xPos, yPos, this);
	} 

	public String getIdentifier() {
		return "%%Handle_" + da.getName() + "_" + name.getScalarOrNull();
	}

	public void paintGraph(Graphics gr) {		
		db.setVisible(isVisible());
		if (!isVisible())
			return;		
		db.renew();
		db.paint(gr, getColor());
	}// paintGraph

	public void setVisible(boolean b) {
		super.setVisible(b);
		db.setVisible(b);
	} // setVisible

	public void detach() throws Exception {
		db.remove();
		// beim letzten mal zeichen verstecken
		setVisible(false);
	}
	
	public void renew() {
		db.renew();
	}
} 
