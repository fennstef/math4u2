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
import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.HasGraph;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.gui.listview.ListViewItemInterface;
import math4u2.view.gui.listview.ViewFactoryInterface;

public class Angle extends AbstractArea {
	private UserFunction apex;

	private UserFunction vectorFirst;

	private UserFunction vectorSecond;

	private UserFunction radius;

	public Angle(String name, MathObject apex, MathObject vectorFirst,
			MathObject vectorSecond, MathObject distance, Color color,
			int lineStyle, boolean isVisible, Broker broker,
			ViewFactoryInterface viewFactory) {

		super("winkel", name, color, true, lineStyle, isVisible, broker,
				viewFactory);
		this.apex = (UserFunction) apex;
		this.vectorFirst = (UserFunction) vectorFirst;
		this.vectorSecond = (UserFunction) vectorSecond;
		this.radius = (UserFunction) distance;
	} // Konstruktor

	public Angle(MathObject apex, MathObject vectorFirst,
			MathObject vectorSecond, MathObject distance, Broker broker,
			ViewFactoryInterface viewFactory) {

		this(null, apex, vectorFirst, vectorSecond, distance, Color.black,
				HasGraph.SOLID_STROKE, true, broker, viewFactory);
	} // Konstruktor

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
		if (!(k instanceof Angle))
			throw new IllegalArgumentException(
					"Das Objekt muß ein Kreis sein. Ist aber " + k.getClass());
		if (partsWithPath.size() != 4)
			throw new IllegalArgumentException(
					"Die Teile-Liste muß genau die Länge 4 haben.");
		if (broker == null)
			throw new NullPointerException(
					"Der Broker ist null. Dies ist hier nicht erlaubt.");
		// Beziehungen erzeugen
		RelationInterface[] ra = new RelationInterface[4];
		List parts = new LinkedList();
		for (int i = 0; i < ra.length; i++) {
			UserFunction part_i = (UserFunction) partsWithPath.get(i);
			if (regList.contains(part_i))
				ra[i] = RelationFactory.getPart_Of_Relation();
			else
				ra[i] = RelationFactory.getFunction_SubFunction_Relation();
			parts.add(part_i);
			_setCreationPath(ra[i], part_i);
		}
		// Beziehungen bennenen
		ra[0].setShortName(Relation.FIRST, "scheitelpunkt");
		ra[1].setShortName(Relation.FIRST, "vektor1");
		ra[2].setShortName(Relation.FIRST, "vektor2");
		ra[3].setShortName(Relation.FIRST, "radius");
		List relations = Arrays.asList(ra);

		// Definieren lassen
		broker.defineRelations(k, parts, relations, Broker.FIRST_OBJECT);
	} // register

	public void setFreeze(boolean freeze) {
		super.setFreeze(freeze);
		apex.setFreeze(freeze);
		radius.setFreeze(freeze);
		vectorFirst.setFreeze(freeze);
		vectorSecond.setFreeze(freeze);
	}// setFreeze

	public AffPoint getApex() throws MathException {
		AffPoint ap = (AffPoint) ((UserFunction) apex).eval();
		if (ap == null)
			throw new MathException(
					"Der Scheitelpunkt des Winkels wurde nicht gefunden.");
		return ap;
	} // getCenter

	public UserFunction getRadius() {
		return radius;
	} // getRadius

	public String bodyToString() {
		String apexStr = null;
		String vector1Str = null;
		String vector2Str = null;
		String distanceStr = null;

		apexStr = getRepresentation(apex);
		vector1Str = getRepresentation(vectorFirst);
		vector2Str = getRepresentation(vectorSecond);
		distanceStr = getRepresentation(radius);
		return type + "(" + apexStr + "," + vector1Str + "," + vector2Str + ","
				+ distanceStr + ")";
	} // bodyToString

	public double getX() throws MathException {
		return getApex().evalX();
	} // getX

	public double getY() throws MathException {
		return getApex().evalY();
	} // getY

	public double evalRadius() throws MathException {
		return getRadius().evalScalar();
	} // evalRadius

	public GraphInterface getGraph(DrawAreaInterface da, UserFunction f) {
		return viewFactory.getGraphFactory().createAngleGraph(da, f);
	} // getGraph

	public UserFunction[] getVectors() throws MathException {
		UserFunction[] vectors = new UserFunction[2];
		if (vectorFirst.getFunction() instanceof PathReference) {
			PathReference pr1 = (PathReference) vectorFirst.getFunction();
			try {
				pr1 = (PathReference) pr1.getClone(new Variable[0],
						new Variable[0]);
				pr1.removeLastEval();
			} catch (Exception e) {
				ExceptionManager.doError("Fehler beim Holen der Vektoren ("
						+ getIdentifier() + ").", e);
			}
			vectors[0] = (UserFunction) pr1.eval();
		} else
			vectors[0] = vectorFirst;
		if (vectorSecond.getFunction() instanceof PathReference) {
			PathReference pr2 = (PathReference) vectorFirst.getFunction();
			try {
				pr2 = (PathReference) pr2.getClone(new Variable[0],
						new Variable[0]);
				pr2.removeLastEval();
			} catch (Exception e) {
				ExceptionManager.doError("Fehler beim Holen der Vektoren ("
						+ getIdentifier() + ").", e);
			}
			vectors[1] = (UserFunction) pr2.eval();
		} else
			vectors[1] = vectorFirst;
		return vectors;
	} // getVectors

	public MatrixDoubleResult[] getVectorsEval() throws MathException {
		MatrixDoubleResult[] mdr = { (MatrixDoubleResult) vectorFirst.eval(),
				(MatrixDoubleResult) vectorSecond.eval() };
		return mdr;
	} // getVectorEval

	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {
		if (apex == oldObject) {
			apex = (UserFunction) newObject;
		} else {
			apex.swapLinks(oldObject, newObject);
		}
		if (vectorFirst == oldObject) {
			vectorFirst = (UserFunction) newObject;
		} else {
			vectorFirst.swapLinks(oldObject, newObject);
		}
		if (vectorSecond == oldObject) {
			vectorSecond = (UserFunction) newObject;
		} else {
			vectorSecond.swapLinks(oldObject, newObject);
		}
		if (radius == oldObject) {
			radius = (UserFunction) newObject;
		} else {
			radius.swapLinks(oldObject, newObject);
		}// else
	} // swapLinks

	@Override
	public ListViewItemInterface getCompleteView(UserFunction f,
			ListViewInterface alv, Broker broker) {
		return viewFactory.createCompleteAngleViewItem(f, alv, broker);
	} // getCompleteView

	/**
	 * Diese Methoden werden von Außen aufgerufen z.B. p.x, p.y usw.
	 * 
	 * Anfang --------------------------------------------------------
	 */

	public Object operator_scheitelpunkt(Object[] values) {
		try {
			return apex.eval();
		} catch (MathException e) {
			ExceptionManager.doError("Fehler bei der Methode 'scheitelpunkt' ("
					+ getIdentifier() + ").", e);
			throw new RuntimeException();
		}
	} // operator_scheitelpunkt

	public Object operator_set_scheitelpunkt(Object[] values) {
		Object obj = values[0];
		TermNode tm;
		if (obj instanceof AffPoint) {
			tm = new PathReference((AffPoint) obj, new LinkedList(), broker);
		} else {
			tm = ((UserFunction) values[0]).getFunction();
		}

		apex.setFunction(tm);
		try {
			broker.propagateChange(apex);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler bei der Methode 'scheitelpunkt' ("
					+ getIdentifier() + ").", e);
		}
		return null;
	} // operator_set_scheitelpunkt

	public Class returnType_scheitelpunkt(MethodContext mc) {
		return AffPoint.class;
	}// returnType_scheitelpunkt

	// ---------------------------------------------------

	public Object operator_set_vektor1(Object[] values) {
		TermNode tm;
		tm = ((UserFunction) values[0]).getFunction();

		vectorFirst.setFunction(tm);
		try {
			broker.propagateChange(vectorFirst);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler bei der Methode 'vektor1' ("
					+ getIdentifier() + ").", e);
		}
		return null;
	} // operator_vektor1

	public Class returnType_vektor1(MethodContext mc) {
		return UserFunction.class;
	} // returnType_vektor1

	// ---------------------------------------------------

	public Object operator_set_vektor2(Object[] values) {
		TermNode tm;
		tm = ((UserFunction) values[0]).getFunction();

		vectorSecond.setFunction(tm);
		try {
			broker.propagateChange(vectorSecond);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler bei der Methode 'vektor2' ("
					+ getIdentifier() + ").", e);
		}
		return null;
	} // operator_vektor2

	public Class returnType_vektor2(MethodContext mc) {
		return UserFunction.class;
	} // returnType_vektor2

	// ---------------------------------------------------

	public Object operator_radius(Object[] values) {
		return radius;
	} // operator_radius

	public Object operator_set_radius(Object[] values) {
		radius.setFunction(((UserFunction) values[0]).getFunction());
		try {
			broker.propagateChange(radius);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler bei der Methode 'radius' ("
					+ getIdentifier() + ").", e);
		}
		return null;
	} // operator_set_radius

	public Class returnType_radius(MethodContext mc) {
		return UserFunction.class;
	}// returnType_radius

	/**
	 * Ende ----------------------------------------------------------
	 */

} // class Angle
