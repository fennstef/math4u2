package math4u2.mathematics.collection;

import java.awt.Color;
import java.awt.geom.GeneralPath;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.reference.AbstractPathStep;
import math4u2.controller.reference.CreatesPath;
import math4u2.controller.reference.MethodContext;
import math4u2.controller.reference.PathStep;
import math4u2.controller.relation.RelationContainer;
import math4u2.controller.relation.RelationFactory;
import math4u2.controller.relation.RelationInterface;
import math4u2.mathematics.affine.AbstractAffineObject;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.results.Result;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.types.ScalarType;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.NotImplementedException;
import math4u2.view.graph.AppendShapeInterface;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.HasGraph;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.gui.listview.ViewFactoryInterface;

public class MathList implements MathObject, CreatesPath, HasGraph,
		AppendShapeInterface {

	/** Beziehungs-Container */
	private RelationContainer relationContainer;

	/** Name der Liste */
	private String name;

	/** Index, mit dem das erste Element der Liste angesprochen wird. */
	public final int firstElementIndex;

	/** Dummy für spezielle Auskünfte */
	private Object dummy;

	/** die eigentliche Liste */
	private ArrayList elementList;

	/** Farbe aller Listeninhalte */
	private Color color;

	/** Linienstil aller Listeninhalte */
	private int lineStyle;

	/** Sichtbarkeit aller Listeninhalte */
	private boolean visible = true;

	/** Ist die Liste modifizierbar */
	private boolean freeze = false;

	/** Sollen die Graphen ihre Namen ebenfalls zeichnen */
	private boolean showNames = true;
	
	private ViewFactoryInterface viewFactory;

	/**
	 * Verknüpft die Liste mit seinen Beziehungspartnern (parts)
	 * 
	 * @param l
	 *            Liste, die verknüpft werden soll
	 * @param parts
	 *            Liste der Objekte, die Beziehungspartner sind
	 * @param regList
	 *            Liste der Objekte, die beim scheitern gelöscht werden (anonyme
	 *            Objekte)
	 * @param broker
	 */
	public static void register(MathObject l, List partsWithPath, List regList,
			Broker broker) throws BrokerException {
		// Überprüfung, ob alles in Ordnung ist
		if (!(l instanceof MathList))
			throw new IllegalArgumentException(
					"Das Objekt muß eine Liste sein. Ist aber " + l.getClass());
		if (broker == null)
			throw new NullPointerException(
					"Der Broker ist null. Dies ist hier nicht erlaubt.");
		// Beziehungen erzeugen
		RelationInterface[] ra = new RelationInterface[partsWithPath.size()];
		List parts = new LinkedList();
		int i = 0;
		for (Iterator iter = partsWithPath.iterator(); iter.hasNext(); i++) {
			Object ob = iter.next();
			UserFunction part = (UserFunction) ob;
			if (regList.contains(part))
				ra[i] = RelationFactory.getPart_Of_Relation();
			else
				ra[i] = RelationFactory.getFunction_SubFunction_Relation();
			parts.add(part);
			AbstractAffineObject._setCreationPath(ra[i], part);
		} // for iter
		List relations = Arrays.asList(ra);
		// Definieren
		broker.defineRelations(l, parts, relations, Broker.FIRST_OBJECT);
	} // register

	/**
	 * Erzeugt eine neue Liste und initialisiert sie.
	 * 
	 * @param name
	 *            Name der neuen Liste
	 * @param firstElementIndex
	 *            Indes, mit dem in dieser Liste das erste Element indiziert
	 *            wird
	 * @param listElementClass
	 *            Typ (Klasse) der Listenelemente
	 * @param elements
	 *            Liste von Elementen zur Initialisierung
	 */
	public MathList(String name, int firstElementIndex, Class listElementClass,
			List elements, Broker broker, ViewFactoryInterface viewFactory) {
		// if(name==null) throw new NullPointerException();
		this.name = name;
		this.firstElementIndex = firstElementIndex;
		relationContainer = new RelationContainer(this);
		elementList = new ArrayList();
		elementList.addAll(elements);

		Iterator iter = elements.iterator();
		if (iter.hasNext())
			dummy = iter.next();
		this.viewFactory = viewFactory;
	} // Konstruktor 1

	/**
	 * Erzeugt eine neue Liste ohne Namen und initialisiert sie.
	 * 
	 * @param firstElementIndex
	 *            Indes, mit dem in dieser Liste das erste Element indiziert
	 *            wird
	 * @param listElementClass
	 *            Typ (Klasse) der Listenelemente
	 * @param elements
	 *            Liste von Elementen zur Initialisierung
	 */
	public MathList(int firstElementIndex, Class listElementClass,
			List elements, Broker broker, ViewFactoryInterface viewFactory) {
		this("", firstElementIndex, listElementClass, elements, broker, viewFactory);
		name = null;
	} // Konstruktor 2

	/**
	 * Erzeugt eine neue Liste und initialisiert sie. Das Object dummy liefert
	 * spezielle Auskünfte über die Elemente der Liste.
	 * 
	 * @param name
	 *            Name der neuen Liste
	 * @param firstElementIndex
	 *            Indes, mit dem in dieser Liste das erste Element indiziert
	 *            wird
	 * @param listElementClass
	 *            Typ (Klasse) der Listenelemente
	 * @param elements
	 *            Liste von Elementen zur Initialisierung
	 */
	public MathList(String name, int firstElementIndex, Class listElementClass,
			Object dummy, List elements, Broker broker, ViewFactoryInterface viewFactory) {

		this(name, firstElementIndex, listElementClass, elements, broker, viewFactory);
		this.dummy = dummy;
	} // Konstruktor 3

	/**
	 * @return Dummy-Objekt der Liste, falls vorhanden, sonst null
	 */
	public Object getDummy() {
		return dummy;
	} // getDummy

	/**
	 * @return Anzahl der Elemente in der aktuellen Liste
	 */
	public int size() {
		return elementList.size();
	} // size

	/**
	 * @return Index, mit dem in dieser Liste das erste Element indiziert wird.
	 */
	public int getFirstElementIndex() {
		return firstElementIndex;
	} // getFirstElementIndex

	/**
	 * @return Index, mit dem in dieser Liste das letzte Element indiziert wird.
	 */
	public int getLastElementIndex() {
		return firstElementIndex - 1 + elementList.size();
	}// getLastElementIndex

	/**
	 * Liefert den Index des aktuell letzten Elements der Liste. Wenn die Liste
	 * leer ist, wird der Wert geliefert, der um 1 kleiner ist als der erste
	 * Index der Liste.
	 * 
	 * @return Index des aktuell letzten Elements.
	 */
	public int getCurrentLastElementIndex() {
		return elementList.size() + firstElementIndex - 1;
	} // getCurrentLastElementIndex

	/**
	 * Gibt das Element zurück, das sich an der Postion index befindet. Das
	 * erste Element der Liste finde sich bei der Position firstElementIndex.
	 * 
	 * @param index
	 *            Position des gewuenschten Elements
	 * @return
	 */
	public MathObject get(int index) throws MathException {
		try {
			return (MathObject) (elementList.get(index - firstElementIndex));
		} catch (IndexOutOfBoundsException e) {
			throw new MathException("Liste " + name
					+ "hat kein Element an Position " + index);
		}
	} // get

	public Object getKey() {
		return name;
	} // getKey

	public boolean testSubstitution(MathObject oldObject, Set oldAggregateSet) {
		return true;
	} // testSubstitution

	public MathObject constructSubstitution(MathObject oldObject)
			throws Exception {
		throw new NotImplementedException();
	} // constructSubstitution

	public void renew(MathObject source) {
	}

	public void prepareDelete() {
	}

	public void swapLinks(MathObject oldObject, MathObject newObject) {
	}

	public void setName(String name) {
		this.name = name;
	} // setName

	public RelationContainer getRelationContainer() {
		return relationContainer;
	} // getRelationContainer

	public boolean testDelete() {
		return true;
	} // testDelete

	public String toString() {
		return name + " := " + toBodyString();
	} // toString

	public String getTypeString() {
		return getElementType() + "liste";
	}// getTypeString

	public String toBodyString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getTypeString());
		if (firstElementIndex == 0)
			buffer.append("0");
		buffer.append("({");
		for (Iterator iter = elementList.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (element instanceof AbstractAffineObject) {
				buffer.append(((AbstractAffineObject) element).bodyToString());
			} else if (element instanceof UserFunction) {
				buffer.append(((UserFunction) element).getTermString(this));
			} else if (element instanceof MathObject) {
				buffer.append(((MathObject) element).getKey() + "");
			} else {
				buffer.append(element.toString());
			} // else
			if (iter.hasNext())
				buffer.append(", ");
		} // for iter
		buffer.append("})");
		return buffer.toString();
	}// toBodyString

	public String getElementType() {
		if (dummy instanceof HasTypeString) {
			return "<" + ((HasTypeString) dummy).getTypeString() + "> ";
		}
		return " ";
	}// getElementType

	/**
	 * Es wird irgend ein Objekt aus der Liste zurück gegeben.
	 */
	public MathObject getDummyObject() {
		return (MathObject) elementList.iterator().next();
	} // getDummyObject

	/**
	 * Diese Methoden werden von außen aufgerufen z.B. sin.funktion.stellen usw.
	 * 
	 * Anfang --------------------------------------------------------
	 */

	public Object operator_index(Object[] value) throws MathException {
		int index = (int) ((ScalarDoubleResult) value[0]).value;
		try {
			return elementList.get(index - firstElementIndex);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new MathException("Index " + (index)
					+ " ist keine gültige Position in der Liste.");
		}
	} // operator_index

	public Object operator_set_index(Object[] value) throws MathException {
		int index = (int) ((ScalarDoubleResult) value[0]).value;
		elementList.set(index - firstElementIndex, value[1]);
		return null;
	} // operator_set_index

	public Class returnType_index(MethodContext mc) {
		return getDummyObject().getClass();
	} // class returnType_index

	// -----------------------------------------------------------------

	public Object operator_laenge(Object[] value) throws MathException {
		return new ScalarDoubleResult(elementList.size());
	} // operator_laenge

	public Class returnType_laenge(MethodContext mc) {
		return ScalarType.class;
	} // class returnType_laenge

	public Object operator_startindex(Object[] value) throws MathException {
		return new ScalarDoubleResult(firstElementIndex);
	} // operator_startindex

	public Class returnType_startindex(MethodContext mc) {
		return ScalarType.class;
	} // class returnType_startindex

	public Object operator_endeindex(Object[] value) throws MathException {
		return new ScalarDoubleResult(getLastElementIndex());
	} // operator_endeindex

	public Class returnType_endeindex(MethodContext mc) {
		return ScalarType.class;
	} // class returnType_endeindex

	/**
	 * Ende ----------------------------------------------------------
	 */

	public PathStep createPathStep(List methods) {
		if (methods.isEmpty())
			return null;

		MethodContext mc = (MethodContext) methods.get(0);
		methods.remove(0);
		try {
			Method m = this.getClass().getMethod(
					"operator_" + mc.getMethodName(),
					new Class[] { Object[].class });

			PathStep ps = AbstractPathStep.createStep(this, mc.getMethodName(),
					m, mc.getArgs());

			CreatesPath nextObj = null;
			if (mc.getMethodName().equals("index")) {
				nextObj = (CreatesPath) getDummyObject();
			} else {
				nextObj = (CreatesPath) ps.getObjectFromMethod(this);
			}// else

			if (nextObj != null && !(nextObj instanceof Result)) {
				PathStep nextStep = nextObj.createPathStep(methods);
				ps.setNextStep(nextStep);
			}// if

			return ps;
		} catch (Exception e) {
			throw new RuntimeException(
					"Fehler beim Erzeugen des Methodenpfads (" + name + ")", e);
		}// catch
	} // createPathStep

	public Class getReturnType(PathStep nextStep) {
		if (nextStep == null)
			return MathList.class;
		MethodContext mc;
		mc = nextStep.getMethodContext();
		return getReturnType(mc);
	} // getReturnType

	public Class getReturnType(MethodContext mc) {
		try {
			Method m = this.getClass().getMethod(
					"returnType_" + mc.getMethodName(),
					new Class[] { MethodContext.class });
			return (Class) m.invoke(this, new Object[] { mc });
		} catch (Exception e) {
			ExceptionManager.doError("Fehler beim Ermitteln des Rückgabetyps ("
					+ name + ").", e);
			throw new RuntimeException(e);
		} // catch
	} // getReturnType

	public Color getColor() {
		if (color == null)
			color = Color.BLACK;
		return color;
	} // getColor

	public void setColor(Color color) {
		for (Iterator iter = elementList.iterator(); iter.hasNext();) {
			HasGraph hg = (HasGraph) iter.next();
			hg.setColor(color);
		}// for iter
		this.color = color;
	} // setColor

	public GraphInterface getGraph(DrawAreaInterface da, UserFunction f) {
		return viewFactory.getGraphFactory().createListGraph(da, f);
	} // getGraph

	public int getLineStyle() {
		return lineStyle;
	} // getLineStyle

	public void setLineStyle(int lineStyle) {
		for (Iterator iter = elementList.iterator(); iter.hasNext();) {
			HasGraph hg = (HasGraph) iter.next();
			hg.setLineStyle(lineStyle);
		}// for iter
		this.lineStyle = lineStyle;
	}// setLineStyle

	public boolean isFreeze() {
		return freeze;
	}// isFreeze

	public void setFreeze(boolean freeze) {
		this.freeze = freeze;
	}// setFreeze

	public boolean hasCurrentObjectGraph() {
		if (dummy instanceof HasGraph) {
			return ((HasGraph) dummy).hasCurrentObjectGraph();
		}
		return false;
	} // hasCurrentObjectGraph

	public List getList() {
		return elementList;
	} // getList

	public boolean isVisible() {
		return visible;
	}// isVisible

	public void setVisible(boolean visible) {
		for (Iterator iter = elementList.iterator(); iter.hasNext();) {
			HasGraph hg = (HasGraph) iter.next();
			hg.setVisible(visible);
		}// for iter
		this.visible = visible;
	}// setVisible

	public void setShowNames(boolean b) {
		showNames = b;
	}// setShowNames

	public boolean isShowNames() {
		return showNames;
	}

	public void appendShape(GeneralPath gp, DrawAreaInterface da,
			UserFunction own) throws MathException {
		for (Iterator iter = elementList.iterator(); iter.hasNext();) {
			UserFunction func = (UserFunction) iter.next();
			Object obj = func.eval();
			if (obj instanceof AppendShapeInterface) {
				AppendShapeInterface appender = (AppendShapeInterface) obj;
				appender.appendShape(gp, da, func);
			}// if
		}// for iter
	}// appendShape

	public void applyProperties(HasGraph oldObject) {
		setColor(oldObject.getColor());
		setLineStyle(oldObject.getLineStyle());
		setVisible(oldObject.isVisible());
	}

} // class MathList
