package math4u2.mathematics.affine;

import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.reference.MethodContext;
import math4u2.controller.reference.PathReference;
import math4u2.controller.relation.Relation;
import math4u2.controller.relation.RelationFactory;
import math4u2.controller.relation.RelationInterface;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.types.ScalarType;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.HasGraph;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.gui.listview.ListViewItemInterface;
import math4u2.view.gui.listview.ViewFactoryInterface;

/**
 * @author Max Weiss
 * @version 1.0
 */

public class Circle extends AbstractArea {
	private UserFunction center;

	private UserFunction radius;

	public Circle(String name, MathObject center, MathObject radius,
			Color color, int lineStyle, boolean isVisible, Broker broker,
			ViewFactoryInterface viewFactory) {

		super("kreis", name, color, true, lineStyle, isVisible, broker,
				viewFactory);
		setColor(Color.BLACK);
		setFillColor(new Color(0, 0, 0, 0));
		setBorderColor(Color.BLACK);
		this.center = (UserFunction) center;
		this.radius = (UserFunction) radius;
	} // Konstruktor

	public Circle(MathObject center, MathObject radius, Broker broker,
			ViewFactoryInterface viewFactory) {
		this(null, center, radius, Color.black, HasGraph.SOLID_STROKE, true,
				broker, viewFactory);
	} // Konstruktor 2

	/**
	 * Verknüpt den Kreis k mit seinen Beziehungspartnern (parts)
	 * 
	 * @param k
	 *            Kreis, der verknüpft werden soll.
	 * @param parts
	 *            Liste der Objekte, die Beziehungspartner sind
	 * @param regList
	 *            Liste der Objekte, die beim scheitern gelöscht werden (anonyme
	 *            Objekte)
	 * @param broker
	 */
	public static void register(MathObject k, List partsWithPath, List regList,
			Broker broker) throws BrokerException {
		// Überprüfung, ob alles in Ordnung ist
		if (!(k instanceof Circle))
			throw new IllegalArgumentException(
					"Das Objekt muß ein Kreis sein. Ist aber " + k.getClass());
		if (partsWithPath.size() != 2)
			throw new IllegalArgumentException(
					"Die Teile-Liste muß genau die Länge 2 haben.");
		if (broker == null)
			throw new NullPointerException(
					"Der Broker ist null. Dies ist hier nicht erlaubt.");
		// Beziehungen erzeugen
		RelationInterface[] ra = new RelationInterface[2];
		UserFunction part0 = (UserFunction) (partsWithPath.get(0));
		UserFunction part1 = (UserFunction) (partsWithPath.get(1));
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
		// Beziehungen bennenen
		ra[0].setShortName(Relation.FIRST, "mitte");
		ra[1].setShortName(Relation.FIRST, "radius");

		_setCreationPath(ra[1], part1);
		_setCreationPath(ra[0], part0);

		List relations = Arrays.asList(ra);

		// Definieren lassen
		broker.defineRelations(k, parts, relations, Broker.FIRST_OBJECT);
	} // register

	public AffPoint getCenter() throws MathException {
		AffPoint ap = (AffPoint) ((UserFunction) center).eval();
		if (ap == null)
			throw new MathException(
					"Der Kreismittelpunkt wurde nicht gefunden.");
		return ap;
	} // getCenter

	public UserFunction getRadius() {
		return radius;
	} // getRadius

	public String bodyToString() {
		String centerStr = null;
		centerStr = getRepresentation(center);
		return type + "(" + centerStr + "," + getRepresentation(radius) + ")";
	} // bodyToString

	public GraphInterface getGraph(DrawAreaInterface da, UserFunction f) {
		return viewFactory.getGraphFactory().createCircleGraph(da, f);
	} 

	public double evalX() throws MathException {
		return getCenter().evalX();
	} // getX

	public double evalY() throws MathException {
		return getCenter().evalY();
	} // getY

	public double evalRadius() throws MathException {
		return getRadius().evalScalar();
	} // evalRadius

	public void setColor(Color c) {
		setBorderColor(c);
		setFillColor(new Color(0, 0, 0, 0));
	}// setColor

	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {
		if (radius == oldObject) {
			radius = (UserFunction) newObject;
		} else {
			radius.swapLinks(oldObject, newObject);
		}
		if (center == oldObject) {
			center = (UserFunction) newObject;
		} else {
			center.swapLinks(oldObject, newObject);
		}// else
	} // swapLinks

	public void setFreeze(boolean freeze) {
		super.setFreeze(freeze);
		center.setFreeze(freeze);
		radius.setFreeze(freeze);
	}// setFreeze

	public ListViewItemInterface getCompleteView(UserFunction f,
			ListViewInterface alv, Broker broker) {
		return viewFactory.createCompleteCircleViewItem(f, alv, broker);
	} // getCompleteView

	/**
	 * Diese Methoden werden von Außen aufgerufen z.B. p.x, p.y usw.
	 * 
	 * Anfang --------------------------------------------------------
	 */

	public Object operator_radius(Object[] values) {
		return radius;
	} // operator_radius

	public Object operator_set_radius(Object[] values) {
		radius.setFunction(((UserFunction) values[0]).getFunction());
		try {
			broker.propagateChange(radius);
		} catch (BrokerException e) {
			ExceptionManager.doError(
					"Fehler beim Erneuern (" + getKey() + ").", e);
		}
		return null;
	} // operator_set_radius

	public Class returnType_radius(MethodContext mc) {
		return ScalarType.class;
	}// returnType_radius

	// ---------------------------------------------------

	public Object operator_mitte(Object[] values) {
		try {
			return center.eval();
		} catch (MathException e) {
			ExceptionManager.doError("Fehler bei der Methode 'mitte' ("
					+ getKey() + ").", e);
			throw new RuntimeException();
		}
	} // operator_mitte

	public Object operator_set_mitte(Object[] values) {
		Object obj = values[0];
		TermNode tm;
		if (obj instanceof AffPoint) {
			tm = new PathReference((AffPoint) obj, new LinkedList(), broker);
		} else {
			tm = ((UserFunction) values[0]).getFunction();
		}

		center.setFunction(tm);
		try {
			broker.propagateChange(center);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler bei der Methode 'mitte' ("
					+ getKey() + ").", e);
		}
		return null;
	} // operator_set_mitte

	public Class returnType_mitte(MethodContext mc) {
		return AffPoint.class;
	}// returnType_mitte

	// ---------------------------------------------------

	public Object operator_flaeche(Object[] values) throws Exception {
		return new ScalarDoubleResult(evalRadius() * evalRadius() * Math.PI);
	} // operator_flaeche

	public Class returnType_flaeche(MethodContext mc) {
		return ScalarType.class;
	}// returnType_flaeche

	/**
	 * Ende ----------------------------------------------------------
	 */

} // class Circle
