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
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.Variable;
import math4u2.mathematics.types.VectorType;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.HasGraph;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.gui.listview.ListViewItemInterface;
import math4u2.view.gui.listview.ViewFactoryInterface;

public class Arrow extends AbstractAffineObject {
	private UserFunction startPoint;

	private UserFunction vector;

	private Broker broker;

	public Arrow(UserFunction startPoint, UserFunction direction, Color color,
			int lineStyle, boolean isVisible, Broker broker,
			ViewFactoryInterface viewFactory) {
		super("pfeil", null, color, lineStyle, isVisible, broker, viewFactory);
		this.startPoint = startPoint;
		this.vector = direction;
		this.broker = broker;
	} // Konstruktor

	public Arrow(MathObject startPoint, MathObject direction, Broker broker,
			ViewFactoryInterface viewFactory) {
		this((UserFunction) startPoint, (UserFunction) direction, Color.black,
				HasGraph.SOLID_STROKE, true, broker, viewFactory);
	} // Konstruktor 2

	/**
	 * Verknüpft den Pfeil s mit seinen Beziehungspartnern (parts)
	 * 
	 * @param s
	 *            Pfeil, der verknüpft werden soll
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
		if (!(s instanceof Arrow))
			throw new IllegalArgumentException(
					"Das Objekt muß ein Pfeil sein. Ist aber " + s.getClass());
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
		ra[1].setShortName(Relation.FIRST, "richtung");
		_setCreationPath(ra[0], part0);
		_setCreationPath(ra[1], part1);
		List relations = Arrays.asList(ra);
		// Definieren
		broker.defineRelations(s, parts, relations, Broker.FIRST_OBJECT);
	} // register

	public GraphInterface getGraph(DrawAreaInterface da, UserFunction f) {
		return viewFactory.getGraphFactory().createArrowGraph(da, f, broker);
	} // getGraph

	public void setName(String newName) {
		if (name == null)
			name = newName;
		else
			throw new RuntimeException("Pfeil mit Name " + name
					+ " kann nicht in " + newName + " umbenannt werden");
	} // setName

	public void setFreeze(boolean freeze) {
		super.setFreeze(freeze);
		startPoint.setFreeze(freeze);
		vector.setFreeze(freeze);
	}// setFreeze

	public String bodyToString() {
		String pointStr = null;
		pointStr = getRepresentation(startPoint);

		String vectorStr;
		vectorStr = getRepresentation(vector);

		return type + "(" + pointStr + "," + vectorStr + ")";
	} // bodyToString

	public AffPoint getStart() throws MathException {
		return (AffPoint) startPoint.eval();
	} // getPoint

	public UserFunction getVector() throws MathException {
		if (vector.getFunction() instanceof PathReference) {
			PathReference pr = (PathReference) vector.getFunction();
			try {
				pr = (PathReference) pr.getClone(new Variable[0],
						new Variable[0]);
				pr.removeLastEval();
			} catch (Exception e) {
				ExceptionManager.doError("Fehler beim Holen des Vektors ("
						+ getKey() + ").", e);
			}
			return (UserFunction) pr.eval();
		} else
			return vector;
	} 
	
	public UserFunction getVectorFunction() throws MathException {
		return vector;
	} 

	public MatrixDoubleResult getVectorEval() throws MathException {
		return (MatrixDoubleResult) vector.eval();
	}

	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {
		if (oldObject == startPoint)
			startPoint = (UserFunction) newObject;
		else
			startPoint.swapLinks(oldObject, newObject);
		if (oldObject == vector)
			vector = (UserFunction) newObject;
		else
			vector.swapLinks(oldObject, newObject);
	} // swapLinks

	public ListViewItemInterface getCompleteView(UserFunction f,
			ListViewInterface alv, Broker broker) {
		return viewFactory.createCompleteArrowViewItem(f, alv, broker);
	} // getCompleteView

	/**
	 * Diese Methoden werden von Außen aufgerufen z.B. pf.start, pf.richtung
	 * usw.
	 * 
	 * Anfang --------------------------------------------------------
	 */

	public Object operator_start(Object[] values) {
		try {
			return getStart();
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

	public Object operator_richtung(Object[] values) {
		try {
			return getVector();
		} catch (MathException e) {
			e.printStackTrace();
			return null;
		}
	} // operator_richtung

	public Object operator_set_richtung(Object[] values) {
		TermNode tm;
		tm = ((UserFunction) values[0]).getFunction();

		vector.setFunction(tm);
		try {
			broker.propagateChange(vector);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler bei der Methode 'richtung' ("
					+ getKey() + ").", e);
		}
		return null;
	} // operator_richtung

	public Class returnType_richtung(MethodContext mc) {
		return VectorType.class;
	} // returnType_richtung

	/**
	 * Ende ----------------------------------------------------------
	 */

} // class Arrow
