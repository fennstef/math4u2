package math4u2.mathematics.affine;

import java.awt.Color;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
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
import math4u2.view.graph.AppendShapeInterface;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.HasGraph;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.gui.listview.ListViewItemInterface;
import math4u2.view.gui.listview.ViewFactoryInterface;

public class Stretch extends AbstractAffineObject implements
		AppendShapeInterface {
	private UserFunction startPoint, endPoint;

	public Stretch(MathObject startPoint, MathObject endPoint, Color color,
			int lineStyle, boolean isVisible, Broker broker,
			ViewFactoryInterface viewFactory) {
		super("strecke", null, color, lineStyle, isVisible, broker, viewFactory);
		this.startPoint = (UserFunction) startPoint;
		this.endPoint = (UserFunction) endPoint;
	} // Broker broker)

	public Stretch(MathObject Start, MathObject End, Broker broker,
			ViewFactoryInterface viewFactory) {
		this(Start, End, Color.black, HasGraph.SOLID_STROKE, true, broker,
				viewFactory);
	} // Stretch(AffineProxy Start, A...

	/**
	 * Verknüpt die Strecke s mit seinen Beziehungspartnern (parts)
	 * 
	 * @param s
	 *            AffPunkt, der verknüpft werden soll
	 * @param parts
	 *            Liste der Objekte, die Beziehungspartner sind
	 * @param regList
	 *            Liste der Objekte, die beim scheitern gelöscht werden (anonyme
	 *            Objekte)
	 * @param broker
	 */
	public static void register(MathObject s, List partsWithPath, List regList,
			Broker broker) throws BrokerException {
		// Überprüfung, ob alles in Ordnung ist
		if (!(s instanceof Stretch))
			throw new IllegalArgumentException(
					"Das Objekt muß ein Punkt sein. Ist aber " + s.getClass());
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
		// Beziehungen bennenen
		ra[0].setShortName(Relation.FIRST, "start");
		ra[1].setShortName(Relation.FIRST, "ende");
		_setCreationPath(ra[0], part0);
		_setCreationPath(ra[1], part1);
		List relations = Arrays.asList(ra);
		// Definieren
		broker.defineRelations(s, parts, relations, Broker.FIRST_OBJECT);
	} // register(MathObject s, List par...

	public GraphInterface getGraph(DrawAreaInterface da, UserFunction f) {
		return viewFactory.getGraphFactory().createStretchGraph(da, f);
	} // GraphInterface getGraph(DrawAre...

	public void setName(String newName) {
		if (name == null)
			name = newName;
		else
			throw new RuntimeException("Strecke mit Name " + name
					+ " kann nicht in " + newName + " umbenannt werden");
	} // setName(String newName)

	public String bodyToString() {
		String startStr = "";
		String endStr = "";
		startStr = getRepresentation(startPoint);
		endStr = getRepresentation(endPoint);
		return type + "(" + startStr + "," + endStr + ")";
	} // String bodyToString()

	public AffPoint getStartPoint() throws MathException {
		AffPoint ap = (AffPoint) ((UserFunction) startPoint).eval();
		if (ap == null)
			throw new MathException("Der Startpunkt wurde nicht gefunden.");
		return ap;
	} // getCenter

	public AffPoint getEndPoint() throws MathException {
		AffPoint ap = (AffPoint) ((UserFunction) endPoint).eval();
		if (ap == null)
			throw new MathException("Der Endpunkt wurde nicht gefunden.");
		return ap;
	} // getCenter

	public void setFreeze(boolean freeze) {
		super.setFreeze(freeze);
		endPoint.setFreeze(freeze);
		startPoint.setFreeze(freeze);
	}// setFreeze

	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {
		if (oldObject == startPoint)
			startPoint = (UserFunction) newObject;
		else
			startPoint.swapLinks(oldObject, newObject);
		if (oldObject == endPoint)
			endPoint = (UserFunction) newObject;
		else
			endPoint.swapLinks(oldObject, newObject);
	} // swapLinks(MathObject oldObject,...

	public ListViewItemInterface getCompleteView(UserFunction f,
			ListViewInterface alv, Broker broker) {
		return viewFactory.createCompleteStretchViewItem(f, alv, broker);
	} // AbstractListViewItem getComplet...

	/**
	 * Diese Methoden werden von Außen aufgerufen z.B. p.x, p.y usw.
	 * 
	 * Anfang --------------------------------------------------------
	 */

	public Object operator_start(Object[] values) {
		try {
			return getStartPoint();
		} catch (MathException e) {
			e.printStackTrace();
			return null;
		}
	} // operator_start

	public Object operator_set_start(Object[] values) {
		Object obj = values[0];
		TermNode tm;
		if (obj instanceof AffPoint) {
			tm = new PathReference((AffPoint) obj, new LinkedList(), broker);
		} else {
			tm = ((UserFunction) values[0]).getFunction();
		}

		startPoint.setFunction(tm);
		try {
			broker.propagateChange(startPoint);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler bei der Methode 'start' ("
					+ getKey() + ").", e);
		}
		return null;
	} // operator_start

	public Class returnType_start(MethodContext mc) {
		return AffPoint.class;
	} // returnType_start

	// ---------------------------------------------------

	public Object operator_ende(Object[] values) {
		try {
			return getEndPoint();
		} catch (MathException e) {
			e.printStackTrace();
			return null;
		}
	} // operator_ende

	public Object operator_set_ende(Object[] values) {
		Object obj = values[0];
		TermNode tm;
		if (obj instanceof AffPoint) {
			tm = new PathReference((AffPoint) obj, new LinkedList(), broker);
		} else {
			tm = ((UserFunction) values[0]).getFunction();
		}

		endPoint.setFunction(tm);
		try {
			broker.propagateChange(endPoint);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler bei der Methode 'ende' ("
					+ getKey() + ").", e);
		}
		return null;
	} // operator_ende

	public Class returnType_ende(MethodContext mc) {
		return AffPoint.class;
	} // returnType_ende

	// ---------------------------------------------------

	public Object operator_laenge(Object[] values) {
		try {
			AffPoint start = getStartPoint();
			AffPoint end = getEndPoint();
			double dx = end.evalX() - start.evalX();
			dx = dx * dx;
			double dy = end.evalY() - start.evalY();
			dy = dy * dy;
			double result = Math.sqrt(dx + dy);
			return new ScalarDoubleResult(result);
		} catch (MathException e) {
			e.printStackTrace();
			return null;
		}
	} // operator_start

	public Class returnType_laenge(MethodContext mc) {
		return ScalarType.class;
	} // returnType_start

	public void appendShape(GeneralPath gp, DrawAreaInterface da,
			UserFunction own) throws MathException {
		AffPoint s, e;
		s = (AffPoint) ((UserFunction) startPoint).eval();
		e = (AffPoint) ((UserFunction) endPoint).eval();
		gp.append(
				new Line2D.Double(da.xCoordToPix(s.evalX()), da.yCoordToPix(s
						.evalY()), da.xCoordToPix(e.evalX()), da.yCoordToPix(e
						.evalY())), true);
	}

	/**
	 * Ende ----------------------------------------------------------
	 */

} // class Stretch extends Abstra...
