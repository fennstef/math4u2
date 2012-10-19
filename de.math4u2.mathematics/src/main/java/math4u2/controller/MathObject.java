package math4u2.controller;

import java.util.Set;

import math4u2.controller.reference.RootPathObject;
import math4u2.controller.relation.RelationContainer;
import math4u2.mathematics.functions.MathException;

/**
 * Objekte mit dem Interface MathObject können von einem Broker verwaltet
 * werden. Die Objekte werden dabei über einen Schlüssel verwaltet, den sie über
 * die Methode getKey() selbst liefern. Darüber hinaus wird auch das
 * gegenseitige Abhängigkeitsverhältnis der Objekte untereinander vom Broker
 * verwaltet. Ein Objekt, das andere Objekte kennt, wird dabei als Beziehung
 * bezeichnet. Definiert man z.B. f(x) = 3*x , dann g(x)= sin(x) und schließlich
 * h(x) = g(x)*4*f(x), dann hat das Objekt h(x) eine
 * Function-Subfunction-Beziehung mit den Objekten g(x) und f(x) .
 */
public interface MathObject extends RootPathObject {

	/**
	 * Liefert den Schlüssel, mit dem das Objekt beim Broker verwaltet wird.
	 */
	Object getIdentifier();

	/**
	 * Prüft, ob das aktuelle Objekt das Objekt oldObject ersetzen kann.
	 * oldAggregateSet ist dabei das Set derjenigen Aggregate, in denen
	 * oldObject als Teil verwendet wird.
	 */
	boolean testSubstitution(MathObject oldObject, Set oldAggregateSet);

	/**
	 * Prüft, ob das aktuelle Objekt gelöscht werden kann.
	 */
	boolean testDelete();

	/**
	 * Teilt dem aktuellen Objekt mit, dass es nicht mehr aktuell ist.
	 * Ausgangspunkt für die Veränderung ist das Objekt source.
	 */
	void renew(MathObject source);

	/**
	 * Gibt den Beziehungscontainer zurück.
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
	 * Hier können noch Aufräum-Arbeiten geschehen, bevor das Objekt vom Broker
	 * gelöscht wird. Z.B. kann sich hier der Graph aus der Zeichenfläche
	 * löschen.
	 */
	void prepareDelete();

} //interface MathObject
