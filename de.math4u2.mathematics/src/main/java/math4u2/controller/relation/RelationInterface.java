package math4u2.controller.relation;

import java.util.Set;

import math4u2.controller.Broker;
import math4u2.controller.MathObject;
import math4u2.util.string.StringPath;

/**
 * Eine Beziehung besteht aus zwei Rollen und zwei Objekten. Durch Angabe des
 * Objekts, weiss man, ob diese Nachrichten erhalten k�nnen, oder wer gel�scht
 * werden soll.
 * 
 * @author Fenn Stefan
 * @see RelationFactory
 * @see Role
 */
public interface RelationInterface {

	static final int FIRST = 1;

	static final int SECOND = 2;

	/**
	 * Gibt die Rolle dieser Beziehung zur�ck, mit der fromObj verkn�pft ist.
	 * 
	 * @param fromObj
	 *            Ein Objekt, dessen Rolle man erhalten will
	 * @return Rolle des Objekts
	 * @throws ObjectNotInRelationException
	 */
	RoleInterface getRoleFromObject(MathObject fromObj)
			throws ObjectNotInRelationException;

	/**
	 * Gibt die Position dieser Beziehung zur�ck, mit der fromObj verkn�pft ist.
	 * 
	 * @param fromObj
	 *            Ein Objekt, dessen Rolle man erhalten will
	 * @return Position des Objekts
	 * @throws ObjectNotInRelationException
	 */
	int getPositionFromObject(MathObject fromObj)
			throws ObjectNotInRelationException;

	/**
	 * Gibt die Objekte, die in der Beziehung stecken zur�ck;
	 * 
	 * @return Array mit der L�nge 2. {firstObject, secondObject}
	 */
	MathObject[] getObjects();

	/**
	 * holt sich die entsprechende Rolle und zeigt an, ob diese Rolle ein Renew
	 * beziehen will.
	 * 
	 * @param fromObject
	 *            Ein Objekt, dass in der Beziehung existiert
	 */
	boolean receivesRenew(MathObject fromObject)
			throws ObjectNotInRelationException;

	/**
	 * F�gt die Objekte hinzu, die beim L�schen mitgel�scht werden.
	 * 
	 * @param fromObject
	 *            Ein Objekt, dass in der Beziehung existiert.
	 * @param objectsToDelete
	 *            Menge der Objekte, die gel�scht werden.
	 * @throws ObjectNotInRelationException
	 */
	void addObjectsToDelete(MathObject fromObject, Set objectsToDelete)
			throws ObjectNotInRelationException, DeleteRelationException;

	/**
	 * holt sich von fromObject den korrespondierenden Partner und gibt diesen
	 * zur�ck.
	 * 
	 * @param fromObject
	 *            Ein Objekt, dass in der Beziehung existiert
	 * @throws ObjectNotInRelationException
	 */
	MathObject getPartner(MathObject fromObject)
			throws ObjectNotInRelationException;

	/**
	 * Gibt an, ob fromObject den Partner bei der Initialisierung sofort
	 * miterzeugt.
	 * 
	 * @param fromObject
	 *            Ein Objekt, dass in der Beziehung existiert
	 * @throws ObjectNotInRelationException
	 */
	boolean createsPartner(MathObject fromObject)
			throws ObjectNotInRelationException;

	/**
	 * gibt den Relationsnamen zur�ck
	 */
	String getName();

	/** erzeugt eine flache Kopie */
	RelationInterface cloneRelation();

	/**
	 * Gibt den Namen der Rolle
	 * 
	 * @param pos
	 *            Position 1 oder 2
	 * @return gibt den Namen zur�ck, falls vorhanden anderenfalls
	 *         <code>null</code>
	 */
	String getShortName(int pos) throws ObjectNotInRelationException;

	/**
	 * Setzt den Namen der Rolle. Position: von wo aus? und welche Beziehung mit
	 * diesem Namen gesehen werden kann.
	 * 
	 * @param pos
	 *            Position 1 oder 2
	 * @param name
	 *            Name der Kurzbezeichnung
	 * @return
	 */
	void setShortName(int pos, String name);

	/**
	 * Setzen der Objekte der Beziehung
	 * 
	 * @param objFirst
	 *            erstes Objekt
	 * @param objSecond
	 *            zweites Objekt
	 * @param broker
	 */
	void setObjects(MathObject objFirst, MathObject objSecond, Broker broker);

	/**
	 * Setzen des Erzeugungspfades
	 * 
	 * @param creationPath
	 */
	void setCreationPath(String[] creationPath);

	/**
	 * Gibt einen Erzeugungspfad als Array aus z.B. wird "a.b.c.def" als
	 * [a,b,c,de] Array zur�ck gegeben
	 */
	StringPath getCreationPath();
} //interface RelationInterface
