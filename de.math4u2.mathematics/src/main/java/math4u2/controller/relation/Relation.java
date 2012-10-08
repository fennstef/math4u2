package math4u2.controller.relation;

import math4u2.controller.*;
import math4u2.util.string.*;
import java.util.*;

/**
 * Eine Beziehung besteht aus zwei Rollen und zwei Objekten. Durch Angabe des
 * Objekts, weiss man, ob diese Objekt Nachrichten erhalten kann, oder ob es
 * gelöscht werden kann.
 * 
 * @author Fenn Stefan
 * @see RelationFactory
 * @see Role
 */
public class Relation implements RelationInterface, Comparable {

	/** die beiden Rollen der Beziehungen */
	private RoleInterface roleFirst, roleSecond;

	/** die beiden Objekte der Beziehungen */
	private MathObject objFirst, objSecond;

	/** Rollenname */
	private String relationName;

	/** Erzeugungspfad */
	private StringPath creationPath;

	/**
	 * Dieser Konstruktor sollte vom der RelationFactory aufgerufen werden.
	 * 
	 * Die Objekte werden erst nachträglich mit setObjects eingefügt.
	 * 
	 * @param relationName
	 *            Name der Beziehung
	 * @param roleFirst
	 *            Rolle des ersten Objekts
	 * @param roleSecond
	 *            Rolle des zweiten Objekts
	 * @see RelationFactory
	 * @see Relation#setObjects(MathObject,MathObject)
	 */
	public Relation(String relationName, RoleInterface roleFirst,
			RoleInterface roleSecond) throws IllegalRelationConnectionException {
		this.relationName = relationName;
		this.roleFirst = roleFirst;
		this.roleSecond = roleSecond;
		if ((roleFirst.isDeleteable() == Role.DELETE_ASK_PARTNER)
				&& (roleSecond.isDeleteable() == Role.DELETE_ASK_PARTNER))
			throw new IllegalRelationConnectionException(
					"Beide Beziehungen haben eine 'Ask Partner Delete'-Beziehung");
	} //Konstruktor

	/**
	 * @see RelationInterface#getObjects()
	 */
	public MathObject[] getObjects() {
		return new MathObject[] { objFirst, objSecond };
	} //getObjects

	/**
	 * @see RelationInterface#getRoleFromObject(MathObject)
	 */
	public RoleInterface getRoleFromObject(MathObject fromObj)
			throws ObjectNotInRelationException {
		if (fromObj == objFirst)
			return roleFirst;
		else if (fromObj == objSecond)
			return roleSecond;
		else
			throw new ObjectNotInRelationException(
					"Das Object existiert nicht in dieser Beziehung ("
							+ fromObj + ")");
	} //getRoleFromObject

	/**
	 * @see RelationInterface#getPositionFromObject(MathObject)
	 */
	public int getPositionFromObject(MathObject fromObj)
			throws ObjectNotInRelationException {
		if (fromObj == objFirst)
			return FIRST;
		else if (fromObj == objSecond)
			return SECOND;
		else
			throw new ObjectNotInRelationException(
					"Das Object existiert nicht in dieser Beziehung ("
							+ fromObj + ")");
	} //getRoleFromObject

	/**
	 * @see math4u2.controller.relation.RelationInterface#receivesRenew(math4u2.controller.MathObject)
	 */
	public boolean receivesRenew(MathObject fromObject)
			throws ObjectNotInRelationException {
		return getRoleFromObject(fromObject).receivesRenew();
	} //receivesRenew

	/**
	 * @see RelationInterface#addObjectsToDelete(MathObject,Set) throws
	 *      ObjectNotInRelationException
	 */
	public void addObjectsToDelete(MathObject fromObject, Set objectsToDelete)
			throws ObjectNotInRelationException, DeleteRelationException {
		//wenn dieses Objekt den Partner löschen würde ...
		if (getRoleFromObject(fromObject).deletesPartner()) {
			MathObject partner = getPartner(fromObject);
			// ... finde alle zu löschende Objekte beim Partner.
			partner.getRelationContainer().getAllObjectsToDelete(partner,
					objectsToDelete);
		} //if
	} //addObjectsToDelete

	/**
	 * @see RelationInterface#getPartner(math4u2.controller.MathObject)
	 */
	public MathObject getPartner(MathObject fromObj)
			throws ObjectNotInRelationException {
		if (fromObj == objFirst)
			return objSecond;
		else if (fromObj == objSecond)
			return objFirst;
		else
			throw new ObjectNotInRelationException(
					"Das Object existiert nicht in dieser Beziehung ("
							+ fromObj + ")");
	} //getPartner

	/**
	 * Verknüpft die Beziehung mit zwei Objekten
	 * 
	 * @param objFirst
	 *            erstes Objekt
	 * @param objSecond
	 *            zweites Objekt
	 */
	public void setObjects(MathObject objFirst, MathObject objSecond,
			Broker broker) throws IllegalRelationConnectionException {
		this.objFirst = objFirst;
		this.objSecond = objSecond;
		checkConnection(broker);
	} //setFirstObject

	/**
	 * @see RelationInterface#createsPartner(MathObject)
	 */
	public boolean createsPartner(MathObject fromObject)
			throws ObjectNotInRelationException {
		return getRoleFromObject(fromObject).createsPartner();
	} //createsPartener

	/**
	 * Gibt die Beziehungsbezeichnung zurück
	 */
	public String getName() {
		return relationName;
	} //getName

	public String toString() {
		if (relationName != null) {
			Object key = objFirst.getKey();
			String strFirst;
			if (key != null)
				strFirst = key.toString();
			else
				strFirst = "!!!null!!!";
			String strSecond = (String) objSecond.getKey();
			strFirst = RelationContainer.tryToGetFullName(objFirst, "");
			strSecond = RelationContainer.tryToGetFullName(objSecond, "");
			if ((strFirst == null) || strFirst.equals(""))
				strFirst = "?" + objFirst.toString().replaceAll("null:=", "");
			if ((strSecond == null) || strSecond.equals(""))
				strSecond = "?" + objSecond.toString().replaceAll("null:=", "");
			String path = "";
			if (getCreationPath() != null)
				path = getCreationPath().toString();
			if (/* path.equals(strSecond) || */path.equals(""))
				path = "";
			else
				path = "{" + path + "} ";
			return relationName + "( " + strFirst + " | " + strSecond + " "
					+ path + ")";
		} //if
		return "Relation( " + objFirst + ":(" + roleFirst + ") | " + objSecond
				+ ":(" + roleSecond + ") )";
	} //toString

	/**
	 * Zwei Beziehungen sind gleich, wenn diese die gleichen Rollen besitzen und
	 * die gleichen Objekte besitzen
	 */
	public boolean equals(Object o) {
		if (!(o instanceof Relation))
			return false;
		Relation tmp = (Relation) o;
		if (!roleFirst.equals(tmp.roleFirst))
			return false;
		if (!roleSecond.equals(tmp.roleSecond))
			return false;
		if ((objFirst != tmp.objFirst) || (objSecond != tmp.objSecond))
			return false;
		return true;
	} //equals

	/**
	 * Überprüft, ob die Position 1 oder 2 ist.
	 * 
	 * @param pos
	 */
	public static void checkPosition(int pos) {
		if ((pos < 1) || (pos > 2))
			throw new IllegalArgumentException(
					"Die Position muß entweder 1 oder 2 sein.");
	} //checkPosition

	/**
	 * Erzeugt eine Kopie der Relation ohne die Objekte
	 */
	public RelationInterface cloneRelation() {
		return new Relation(relationName, roleFirst, roleSecond);
	} //cloneRelation

	/**
	 * @see math4u2.controller.relation.RelationInterface#getRoleName(int)
	 */
	public String getShortName(int pos) throws ObjectNotInRelationException {
		checkPosition(pos);
		if (pos == 1)
			return roleFirst.getShortName();
		else
			return roleSecond.getShortName();
	} //getRoleName

	/**
	 * @see math4u2.controller.relation.RelationInterface#setRoleName(int,String)
	 */
	public void setShortName(int pos, String name) {
		checkPosition(pos);
		if (pos == 1)
			roleFirst.setShortName(name);
		else
			roleSecond.setShortName(name);
	} //setRoleName

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object arg0) {
		return this.toString().compareTo(arg0.toString());
	} //compareTo

	public void setCreationPath(String[] creationPath) {
		if ((creationPath == null) || (creationPath.length == 0))
			return;
		this.creationPath = new StringPath(creationPath);
	} //setCreationPath

	/**
	 * Gibt einen Erzeugungspfad als Array aus z.B. wird "a.b.c.def" als
	 * [a,b,c,de] Array zurück gegeben
	 */
	public StringPath getCreationPath() {
		return creationPath;
	} //getCreationPath

	//--------------------------Hilfsmethoden-------------------------------

	/**
	 * Überprüft ob die Beziehungsverknüpfung korrekt ist.
	 */
	private void checkConnection(Broker broker)
			throws IllegalRelationConnectionException {
		if (objFirst == null)
			throw new IllegalRelationConnectionException(
					"Das erste Objekt in der Beziehung ist null (" + this + ")");
		if (objSecond == null)
			throw new IllegalRelationConnectionException(
					"Das erste Objekt in der Beziehung ist null (" + this + ")");
		if (objFirst == objSecond) {
			throw new IllegalRelationConnectionException(
					"Es darf keine Beziehung mit zwei gleichen Objekten geknüpft werden ("
							+ RelationContainer.tryToGetFullName(objFirst, "")
							+ ")");
		} //if
	} // checkConnection
} //class Relation
