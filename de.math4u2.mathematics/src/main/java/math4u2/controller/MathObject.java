package math4u2.controller;

import java.util.Set;

import math4u2.controller.reference.RootPathObject;
import math4u2.controller.relation.RelationContainer;
import math4u2.mathematics.functions.MathException;

/**
 * Objekte mit dem Interface MathObject k�nnen von einem Broker verwaltet
 * werden. Die Objekte werden dabei �ber einen Schl�ssel verwaltet, den sie �ber
 * die Methode getKey() selbst liefern. Dar�ber hinaus wird auch das
 * gegenseitige Abh�ngigkeitsverh�ltnis der Objekte untereinander vom Broker
 * verwaltet. Ein Objekt, das andere Objekte kennt, wird dabei als Beziehung
 * bezeichnet. Definiert man z.B. f(x) = 3*x , dann g(x)= sin(x) und schlie�lich
 * h(x) = g(x)*4*f(x), dann hat das Objekt h(x) eine
 * Function-Subfunction-Beziehung mit den Objekten g(x) und f(x) .
 */
public interface MathObject extends RootPathObject {

	/**
	 * Liefert den Schl�ssel, mit dem das Objekt beim Broker verwaltet wird.
	 */
	Object getIdentifier();

	/**
	 * Pr�ft, ob das aktuelle Objekt das Objekt oldObject ersetzen kann.
	 * oldAggregateSet ist dabei das Set derjenigen Aggregate, in denen
	 * oldObject als Teil verwendet wird.
	 */
	boolean testSubstitution(MathObject oldObject, Set oldAggregateSet);

	/**
	 * Pr�ft, ob das aktuelle Objekt gel�scht werden kann.
	 */
	boolean testDelete();

	/**
	 * Teilt dem aktuellen Objekt mit, dass es nicht mehr aktuell ist.
	 * Ausgangspunkt f�r die Ver�nderung ist das Objekt source.
	 */
	void renew(MathObject source);

	/**
	 * Gibt den Beziehungscontainer zur�ck.
	 */
	RelationContainer getRelationContainer();

	/**
	 * Setzt den Namen, mit der das Objekt angesprochen werden kann.
	 * 
	 * @param name
	 *            neuer Name des Objkets
	 */
	void setName(String name);

	/**
	 * Ersetzt im aktuellen Objekt jede referenz auf oldObject durch eine
	 * Referenz auf newObject.
	 * 
	 * @param oldObject
	 *            zunaechst referenziertes Objekt
	 * @param newObject
	 *            statt oldObjekt zu referenzierendes Objekt
	 * @throws MathException
	 */
	void swapLinks(MathObject oldObject, MathObject newObject) throws Exception;

	/**
	 * Hier k�nnen noch Aufr�um-Arbeiten geschehen, bevor das Objekt vom Broker
	 * gel�scht wird. Z.B. kann sich hier der Graph aus der Zeichenfl�che
	 * l�schen.
	 */
	void prepareDelete();

} //interface MathObject
