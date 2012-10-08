package math4u2.mathematics.affine;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.relation.Relation;
import math4u2.controller.relation.RelationFactory;
import math4u2.controller.relation.RelationInterface;
import math4u2.mathematics.functions.UserFunction;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.gui.listview.ListViewItemInterface;
import math4u2.view.gui.listview.ViewFactoryInterface;

/**
 * Nicht beeinflussbarer Punkt, der verschiedene Darstellungen erzeugen kann.
 * 
 * @author Fenn Stefan
 */

public class Marker extends AffPoint {

	public static final String CIRCLE = "CIRCLE";

	public static final String SQUARE = "SQUARE";

	public static final String CROSS1 = "CROSS1";

	public static final String CROSS2 = "CROSS2";

	public static final String CIRCLE_CROSS = "CIRCLE_CROSS";

	/**
	 * Verknüpt den Marker mit seinen Beziehungspartnern (parts)
	 * 
	 * @param m
	 *            Marker, der verknüpft werden soll
	 * @param parts
	 *            Liste der Objekte, die Beziehungspartner sind
	 * @param regList
	 *            Liste der Objekte, die beim scheitern gelöscht werden (anonyme
	 *            Objekte)
	 * @param broker
	 */
	public static void register(MathObject m, List partsWithPath, List regList,
			Broker broker) throws BrokerException {
		// Überprüfung, ob alles in Ordnung ist
		if (!(m instanceof Marker))
			throw new IllegalArgumentException(
					"Das Objekt muß ein Marker sein. Ist aber " + m.getClass());
		if (partsWithPath.size() != 2)
			throw new IllegalArgumentException(
					"Die Teile-Liste muß genau die Länge 2 haben.");
		if (broker == null)
			throw new NullPointerException(
					"Der Broker ist null. Dies ist hier nicht erlaubt.");
		// Beziehungen erzeugen
		RelationInterface[] ra = new RelationInterface[2];
		UserFunction part0 = (UserFunction) partsWithPath.get(0);
		UserFunction part1 = (UserFunction) partsWithPath.get(1);
		if (regList.contains(part0))
			ra[0] = RelationFactory.getPart_Of_Relation();
		else
			ra[0] = RelationFactory.getFunction_SubFunction_Relation();
		if (regList.contains(part1))
			ra[1] = RelationFactory.getPart_Of_Relation();
		else
			ra[1] = RelationFactory.getFunction_SubFunction_Relation();
		List parts = new LinkedList();
		parts.add(part0);
		parts.add(part1);
		// Beziehungen benennen
		ra[0].setShortName(Relation.FIRST, "x");
		ra[1].setShortName(Relation.FIRST, "y");
		_setCreationPath(ra[0], part0);
		_setCreationPath(ra[1], part1);

		List relations = Arrays.asList(ra);
		// Definieren
		broker.defineRelations(m, parts, relations, Broker.FIRST_OBJECT);
	} // register

	public Marker(MathObject x, MathObject y, Broker broker,
			ViewFactoryInterface viewFactory) throws BrokerException {
		super(x, y, broker, viewFactory);
		style = CIRCLE;
	} // Konstruktor

	public void setName(String newName) {
		if (name != null)
			throw new RuntimeException("Marker mit Name " + name
					+ " kann nicht in " + newName + " umbenannt werden");
		name = newName;
	} // setName

	public GraphInterface getGraph(DrawAreaInterface da, UserFunction f) {
		return viewFactory.getGraphFactory().createMarkerGraph(da, f);
	} // getGraph

	public String bodyToString() {
		return "marker(" + getRepresentation(getX()) + ","
				+ getRepresentation(getY()) + ")";
	} // bodyToString

	public ListViewItemInterface getCompleteView(UserFunction f,
			ListViewInterface alv, Broker broker) {
		return viewFactory.createCompleteMarkerViewItem(f, alv, broker);
	} // getCompleteView

	public void setStyle(String style) {
		style = style.toUpperCase();
		if (style.equals(CIRCLE) || style.equals(CIRCLE_CROSS)
				|| style.equals(CROSS1) || style.equals(CROSS2)
				|| style.equals(SQUARE)) {
			this.style = style;
		} else {
			throw new IllegalArgumentException(
					"Der Darstellungstil ist ungültig (" + style
							+ ").\nGültig sind " + CIRCLE + ", " + CIRCLE_CROSS
							+ ", " + CROSS1 + ", " + CROSS2 + " und " + SQUARE
							+ ".");
		}// else
	}// setStyle

} // class Point
