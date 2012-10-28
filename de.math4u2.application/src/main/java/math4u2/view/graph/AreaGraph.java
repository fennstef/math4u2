package math4u2.view.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

import math4u2.controller.MathObject;
import math4u2.mathematics.affine.Area;
import math4u2.mathematics.affine.Curve;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.drawarea.DrawAreaInterface;

/**
 * Area-Graph
 * 
 * @author Fenn Stefan
 */
public class AreaGraph extends AbstractGraph {

	private UserFunction area;
	
	/** gecachte Evaluierung der Fläche */
	private Area cacheObject;

	private MathObject[] parts;

	public AreaGraph(DrawAreaInterface da, UserFunction area) {
		super(da);
		this.area = area;
		refresh();
	} //Konstruktor

	public void renew(MathObject source) {
		refresh();
	} //renew

	public Area getArea(){
		if(cacheObject==null)
			cacheObject=evalArea();
		return cacheObject;
	}//getArea
	
	public Area evalArea(){
		try {
			return (Area) area.eval();
		} catch (MathException e) {
			ExceptionManager.doError("Fehler beim Zeichnen des Flächen-Graphen "+area,e);
			return null;
		}//catch
	}//evalArea
	
	private void refresh() {
		parts = new MathObject[getArea().getParts().length];
		for (int i = 0; i < parts.length; i++) {
			parts[i] = getArea().getParts()[i];
			Object o = parts[i];
			if (o instanceof Curve) {
				HasGraph hg = (HasGraph) o;
				parts[i] = hg.getGraph(da,area);
			} //if Curve
		} //for i
	}//refresh

	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {
		for (int i = 0; i < parts.length; i++) {
			if (oldObject == parts[i]) {
				parts[i] = newObject;
			} else {
				parts[i].swapLinks(oldObject, newObject);
			}//else
		}//for i
		if (oldObject == area)
			area = (UserFunction) newObject;
		else
			area.swapLinks(oldObject, newObject);
		
		cacheObject = evalArea();
	} //swapLinks

	public void paintGraph(Graphics gr) {
		if (isVisible()) {
			Graphics2D g = (Graphics2D) gr;
			Color ca = g.getColor();
			g.setStroke(getStroke());

			GeneralPath gp = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
			for (int i = 0; i < parts.length; i++) {
				Object obj = parts[i];

				try {
					if(obj instanceof UserFunction){
						UserFunction f = (UserFunction) obj;
						Object o = f.eval();
						if(o instanceof AppendShapeInterface){
							((AppendShapeInterface)o).appendShape(gp, da, f);
						}else{
							ExceptionManager.doError("Objekt kann nich gezeichnet werden: "+obj);
						}//else
					}//if
				} catch (MathException e) {
					ExceptionManager.doError(
							"Fehler beim Zeichnen des Flächen-Graphen " + area,
							e);
					return;
				}
			} // for
			if (gp.getCurrentPoint() == null)
				return;
			if (getArea().isFillArea()) {
				gp.closePath();
			}// if

			Color c = getColor();
			g.setColor(getArea().getFillColor());
			if (getArea().isFillArea()) {
				g.fill(gp);
			}// if

			g.setColor(c);
			g.draw(gp);
			g.setColor(ca);
		} // if visible
	} // paintGraph

	public HasGraph getModel() {
		return area;
	}//getModel

} //AreaGraph
