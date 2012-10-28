package math4u2.mathematics.affine;

/**
 * Parametrische Kurve
 * 
 * @author Max Weiss
 * @version 1.0
 */

import java.awt.Color;
import java.awt.geom.GeneralPath;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.reference.MethodContext;
import math4u2.controller.relation.Relation;
import math4u2.controller.relation.RelationFactory;
import math4u2.controller.relation.RelationInterface;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.types.ScalarType;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.AppendShapeInterface;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.HasGraph;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.gui.listview.ListViewItemInterface;
import math4u2.view.gui.listview.ViewFactoryInterface;

public class Curve extends AbstractAffineObject implements AppendShapeInterface {
	private UserFunction xFunction, yFunction, minFunction, maxFunction;

	private String parameterName;

	public Curve(String name, String parameterName, UserFunction minFunction,
			UserFunction maxFunction, UserFunction xFunction,
			UserFunction yFunction, Color color, int lineStyle,
			boolean isVisible, Broker broker, ViewFactoryInterface viewFactory) {
		super("kurve", null, color, lineStyle, isVisible, broker, viewFactory);
		this.parameterName = parameterName;
		this.xFunction = xFunction;
		this.yFunction = yFunction;
		this.minFunction = minFunction;
		this.maxFunction = maxFunction;
	} // Konstruktor

	public Curve(String parameterName, UserFunction minTerm,
			UserFunction maxTerm, UserFunction xFunction,
			UserFunction yFunction, Broker broker,
			ViewFactoryInterface viewFactory) {
		this(null, parameterName, minTerm, maxTerm, xFunction, yFunction,
				Color.BLACK, HasGraph.SOLID_STROKE, true, broker, viewFactory);
	} // Konstruktor 2

	/**
	 * Verknüpt die paramterische Kurve k mit seinen Beziehungspartnern (parts)
	 * 
	 * @param k
	 *            Kurve, die verknüpft werden soll.
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
		if (!(k instanceof Curve))
			throw new IllegalArgumentException(
					"Das Objekt muß eine Kurve sein. Ist aber " + k.getClass());
		if (partsWithPath.size() != 4)
			throw new IllegalArgumentException(
					"Die Teile-Liste muß genau die Länge 4 haben.");
		if (broker == null)
			throw new NullPointerException("Der Broker ist null.");
		// Beziehungen erzeugen
		RelationInterface[] ra = new RelationInterface[4];
		UserFunction[] partList = new UserFunction[4];
		String[] sa = new String[] { "min", "max", "x", "y" };
		List parts = new LinkedList();
		for (int i = 0; i < 4; i++) {
			partList[i] = (UserFunction) partsWithPath.get(i);

			if (regList.contains(partList[i]))
				ra[i] = RelationFactory.getPart_Of_Relation();
			else
				ra[i] = RelationFactory.getFunction_SubFunction_Relation();

			parts.add(partList[i]);

			// Beziehungen bennenen
			ra[i].setShortName(Relation.FIRST, sa[i]);
			_setCreationPath(ra[i], partList[i]);
		}// for

		List relations = Arrays.asList(ra);

		// Definieren lassen
		broker.defineRelations(k, parts, relations, Broker.FIRST_OBJECT);
	} // register

	public void appendShape(GeneralPath gp, DrawAreaInterface da,
			UserFunction own) throws MathException {
		GraphInterface graph = getGraph(da, own);
		viewFactory.getGraphFactory().appendShape(gp,graph);
	}// appendShape

	public String bodyToString() {
		return type + "(" + parameterName + "," + minFunction.getTermString()
				+ "," + maxFunction.getTermString() + ","
				+ getXFunction().getTermString() + ","
				+ getYFunction().getTermString() + ")";
	} // bodyToString

	public void setFreeze(boolean freeze) {
		super.setFreeze(freeze);
		maxFunction.setFreeze(freeze);
		minFunction.setFreeze(freeze);
		xFunction.setFreeze((freeze));
		yFunction.setFreeze(freeze);
	}// setFreeze

	public GraphInterface getGraph(DrawAreaInterface da, UserFunction f) {
		return viewFactory.getGraphFactory().createCurveGraph(da, f);
	} // getGraph

	public UserFunction getXFunction() {
		return xFunction;
	} // getXFunction

	public UserFunction getYFunction() {
		return yFunction;
	} // getYFunction

	public double getMin() throws MathException {
		return minFunction.evalScalar();
	} // getMin

	public double getMax() throws MathException {
		return maxFunction.evalScalar();
	} // getMax
	
	public UserFunction getMinFunction(){
		return minFunction;
	}
	
	public UserFunction getMaxFunction(){
		return maxFunction;
	}

	/**
	 * @see math4u2.controller.MathObject#swapLinks(math4u2.controller.MathObject,
	 *      math4u2.controller.MathObject)
	 */
	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {
		if (xFunction == oldObject)
			xFunction = (UserFunction) newObject;
		else
			xFunction.swapLinks(oldObject, newObject);
		if (yFunction == oldObject)
			yFunction = (UserFunction) newObject;
		else
			yFunction.swapLinks(oldObject, newObject);
		if (minFunction == oldObject)
			minFunction = (UserFunction) newObject;
		else
			minFunction.swapLinks(oldObject, newObject);
		if (maxFunction == oldObject)
			maxFunction = (UserFunction) newObject;
		else
			maxFunction.swapLinks(oldObject, newObject);
	} // swapLinks

	/**
	 * Diese Methoden werden von Außen aufgerufen z.B. p.x, p.y usw.
	 * 
	 * Anfang --------------------------------------------------------
	 */

	public Object operator_min(Object[] values) {
		return minFunction;
	} // operator_min

	public Object operator_set_min(Object[] values) {
		minFunction.setFunction(((UserFunction) values[0]).getFunction());
		try {
			broker.propagateChange(minFunction);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler bei der Methode 'min' ("
					+ getKey() + ").", e);
		}
		return null;
	} // operator_set_min

	public Class returnType_min(MethodContext mc) {
		return ScalarType.class;
	}// returnType_min

	// ---------------------------------------------------

	public Object operator_max(Object[] values) {
		return maxFunction;
	} // operator_max

	public Object operator_set_max(Object[] values) {
		maxFunction.setFunction(((UserFunction) values[0]).getFunction());
		try {
			broker.propagateChange(maxFunction);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler bei der Methode 'max' ("
					+ getKey() + ").", e);
		}
		return null;
	} // operator_set_max

	public Class returnType_max(MethodContext mc) {
		return ScalarType.class;
	}// returnType_max

	// ---------------------------------------------------

	public Object operator_x(Object[] values) {
		return xFunction;
	} // operator_x

	public Object operator_set_x(Object[] values) {
		xFunction.setFunction(((UserFunction) values[0]).getFunction());
		try {
			broker.propagateChange(xFunction);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler bei der Methode 'x' (" + getKey()
					+ ").", e);
		}
		return null;
	} // operator_set_x

	public Class returnType_x(MethodContext mc) {
		return UserFunction.class;
	}// returnType_x

	// ---------------------------------------------------

	public Object operator_y(Object[] values) {
		return yFunction;
	} // operator_y

	public Object operator_set_y(Object[] values) {
		yFunction.setFunction(((UserFunction) values[0]).getFunction());
		try {
			broker.propagateChange(yFunction);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler bei der Methode 'y' (" + getKey()
					+ ").", e);
		}
		return null;
	} // operator_set_y

	public Class returnType_y(MethodContext mc) {
		return UserFunction.class;
	}// returnType_min

	/**
	 * Ende ----------------------------------------------------------
	 */

	public ListViewItemInterface getCompleteView(UserFunction f,
			ListViewInterface alv, Broker broker) {
		return viewFactory.createCompleteCurveViewItem(f, alv, broker);
	} // getCompleteView

} // class Curve
