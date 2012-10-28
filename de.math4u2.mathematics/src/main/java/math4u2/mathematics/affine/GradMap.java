package math4u2.mathematics.affine;

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
import math4u2.mathematics.functions.UserFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.gui.listview.ViewFactoryInterface;

/**
 * Objekt zur farblichen Darstellung von Funktionswerten einer zweistelligen
 * skalaren Funktion mapFunc(x,y). Dieses Objekt verwaltet die zugehoerigen
 * Funktionen und deren Beziehungen, das eigentliche Zeichnen wird von Objekten
 * der Klasse MapGraph uebernommen.
 * 
 * @author Dr. Weiss
 * 
 */
public class GradMap extends Map {
	/**
	 * nullstellige Funktion, liefert die groesste dargestellte Hoehe
	 */
	protected UserFunction gradFunc;

	/**
	 * 
	 * @param name
	 *            Bezeichner der Karte
	 * @param mapFunc
	 *            Zweistellige skalare Funktion mapFunc(x,y), liefert das
	 *            Hoehenprofil
	 * @param bandVector
	 *            Nullstellige vektorwertige Funktion, liefert als erstes und
	 *            letztes Element die untere und obere Grenze fuer den
	 *            dargestellten Wertebereich und mit weiteren Elementen
	 *            gegebenenfalls die Lage der Hoehenlinien.
	 * @param gradFunc
	 *            Einstellige skalare Funktion, liefert die Gradation fuer die
	 *            Darstellung des Hoehenprofils
	 * @param isVisible
	 * @param broker
	 */

	public GradMap(String name, MathObject mapFunc, MathObject bandVector,
			MathObject gradFunc, boolean isVisible, Broker broker, ViewFactoryInterface viewFactory) {
		super(null, mapFunc, bandVector, isVisible, broker, viewFactory);
		this.gradFunc = (UserFunction) gradFunc;
	} // Konstruktor

	/*
	 * Konstruktor für eine anonyme Karte
	 */
	public GradMap(MathObject mapFunc, MathObject bandVector,
			MathObject gradFunc, Broker broker, ViewFactoryInterface viewFactory) {
		this(null, mapFunc, bandVector, gradFunc, true, broker, viewFactory);
	} // Konstruktor

	/**
	 * Verknüpt die Landkarte b mit ihren Beziehungspartnern (parts)
	 * 
	 * @param b
	 *            Landkarte, die verknüpft werden soll.
	 * @param parts
	 *            Liste der Objekte, die Beziehungspartner sind
	 * @param regList
	 *            Liste der Objekte, die beim scheitern gelöscht werden (anonyme
	 *            Objekte)
	 * @param broker
	 */
	public static void register(MathObject b, List partsWithPath, List regList,
			Broker broker) throws BrokerException {
		// Überprüfung, ob alles in Ordnung ist
		if (!(b instanceof Map))
			throw new IllegalArgumentException(
					"Das Objekt muß eie Landkarte sein. Ist aber "
							+ b.getClass());
		if (partsWithPath.size() != 3)
			throw new IllegalArgumentException(
					"Die Teile-Liste muß genau die Länge 3 haben.");
		if (broker == null)
			throw new NullPointerException(
					"Der Broker ist null. Dies ist hier nicht erlaubt.");
		// Beziehungen erzeugen
		String[] sa = new String[] { "mapFunction", "bandVector", "gradation" };
		RelationInterface[] ra = new RelationInterface[sa.length];
		UserFunction[] partList = new UserFunction[sa.length];
		List parts = new LinkedList();
		for (int i = 0; i < sa.length; i++) {
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
		broker.defineRelations(b, parts, relations, Broker.FIRST_OBJECT);
	} // register

	public String bodyToString() {
		String mapFuncStr = mapFunc.getName();
		String bandVectorStr = getRepresentation(bandVector);
		String gradFuncStr = gradFunc.getName();
		return type + "(" + mapFuncStr + "," + bandVectorStr + ","
				+ gradFuncStr + ")";
	} // bodyToString

	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {

		if (mapFunc == oldObject) {
			mapFunc = (UserFunction) newObject;
		} else {
			mapFunc.swapLinks(oldObject, newObject);
		}
		if (bandVector == oldObject) {
			bandVector = (UserFunction) newObject;
		} else {
			bandVector.swapLinks(oldObject, newObject);
		}
		if (gradFunc == oldObject) {
			gradFunc = (UserFunction) newObject;
		} else {
			gradFunc.swapLinks(oldObject, newObject);
		}
	} // swapLinks

	public UserFunction getGradFunc() {
		return gradFunc;
	}// getGradFunc

	/**
	 * Diese Methoden werden von Außen aufgerufen z.B. p.x, p.y usw.
	 * 
	 * Anfang --------------------------------------------------------
	 */

	public Object operator_gradation(Object[] values) {
		return mapFunc;
	} // operator_mapFunction

	public Object operator_set_gradation(Object[] values) {
		gradFunc.setFunction(((UserFunction) values[0]).getFunction());
		try {
			broker.propagateChange(gradFunc);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler bei der Methode 'gradation' ("
					+ getKey() + ").", e);
		}
		return null;
	} // operator_gradation

	public Class returnType_mapFunction(MethodContext mc) {
		return UserFunction.class;
	} // returnType_gradation

	/**
	 * Ende ----------------------------------------------------------
	 */

} // class GradMap
