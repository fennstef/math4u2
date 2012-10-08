package math4u2.controller.relation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import math4u2.controller.Broker;
import math4u2.controller.MathObject;
import math4u2.mathematics.affine.AbstractAffineObject;
import math4u2.mathematics.functions.UserFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.string.StringPath;

/**
 * Verwaltet für ein Objekt alle Beziehungen.
 * 
 * @author Fenn Stefan
 * @see RelationInterface
 */
public class RelationContainer{

	/** Zugehöriges MathObject */
	private MathObject owner;

	/** Liste aller Relationen im Container */
	private List list = new LinkedList();

	/** Liste aller ContainerListeners */
	private List containerListeners;

	/**
	 * Dictonary für Objekte, die einen Namen besitzen.
	 */
	private Map objDictonary;

	/**
	 * Dieser Konstruktor sollte nur vom Besitzer dieses Containers aufgerufen
	 * werden
	 * 
	 * @param parent
	 *            Besitzer dieses Container
	 */
	public RelationContainer(MathObject parent) {
		this.owner = parent;
	} //Konstruktor

	/**
	 * Diese Methode dient zum Verbinden von Objekt zur Beziehung einerseit und
	 * zur Verknüpfung vom BeziehungsConatiner und Beziehung anderseits.
	 * 
	 * @param objFirst
	 *            Erstes Objekt
	 * @param objSecond
	 *            Zweites Objekt
	 * @param r
	 *            Zu verknüpfende Beziehung
	 * @param isSwap
	 *            Gibt an, ob die Objekte vertauscht eingetragen werden sollen
	 */
	public static void connect(MathObject objFirst, MathObject objSecond,
			RelationInterface r, boolean isSwap, Broker broker)
			throws IllegalRelationConnectionException,
			ObjectNotInRelationException {
		RelationContainer rcFirst = objFirst.getRelationContainer();
		RelationContainer rcSecond = objSecond.getRelationContainer();

		if (isSwap) {
			RelationContainer temp = rcFirst;
			rcFirst = rcSecond;
			rcSecond = temp;

			MathObject tmp = objFirst;
			objFirst = objSecond;
			objSecond = tmp;

			String strFirst = r.getShortName(Relation.FIRST);
			String strSecond = r.getShortName(Relation.SECOND);
			r.setShortName(Relation.SECOND, strFirst);
			r.setShortName(Relation.FIRST, strSecond);
		} //if isSwap

		//Setzten der Objekte in der Beziehung
		((Relation) r).setObjects(objFirst, objSecond, broker);

		//endgültiges setzten der Beziehung in die beiden Container
		if ( rcFirst == null ){
			rcFirst = null;
		}
		rcFirst.addRelation(r);
		rcSecond.addRelation(r);

		//gibt es einen namen, der eingetragen werden muß?
		String nameFirst = r.getShortName(Relation.FIRST);
		String nameSecond = r.getShortName(Relation.SECOND);

		if (isSwap) {
			String temp = nameFirst;
			nameFirst = nameSecond;
			nameSecond = temp;
		} //if isSwap

		if ((nameFirst != null) && (nameSecond != null))
			throw new IllegalArgumentException(
					"Nur eine von beiden Rollen, darf einen Namen haben");

		if (nameFirst != null) {
			objFirst.getRelationContainer().setNamedRelation(nameFirst, r);
		}
		if (nameSecond != null) {
			objSecond.getRelationContainer().setNamedRelation(nameSecond, r);
		} //if
	}//connect

	/**
	 * Zerstört die Beziehung und trägt die Objekte aus den Beziehungslisten
	 * aus.
	 * 
	 * @param r
	 *            Beziehung, die zerstört werden soll
	 */
	public static void disconnect(RelationInterface r)
			throws ObjectNotInRelationException {
		MathObject[] moa = r.getObjects();
		RelationContainer rcFirst = moa[0].getRelationContainer();
		RelationContainer rcSecond = moa[1].getRelationContainer();

		String shortNameFirst = r.getShortName(Relation.FIRST);
		String shortNameSecond = r.getShortName(Relation.SECOND);
		if (shortNameFirst != null && rcFirst.objDictonary.containsKey(shortNameFirst)) {
			rcFirst.removeName(shortNameFirst);
		}//if
		if (shortNameSecond != null && rcSecond.objDictonary.containsKey(shortNameSecond)) {
			rcSecond.removeName(shortNameSecond);
		}//if

		rcFirst.removeRelation(r);
		rcSecond.removeRelation(r);
	} //disconnect
	
	/**
	 * Zerstört spezifische Beziehungen und trägt sie aus den Beziehungslisten aus.
	 * Zerstört werden alle Beziehungen vom Typ RelName, in denen der Bessitzer des aktuellen 
	 * Containers die angegebene Position OwnPos hat.
	 * @param RelName
	 *        Bezeichnet den Typ der Beziehungen, die zerstört werden sollen.
	 * @param OwnPos
	 * 		  Position, die der Besitzer des aktuellen Containers haben soll.
	 * @throws ObjectNotInRelationException
	 */
	
	public void disconnectSpecificRelations(String RelName, int OwnPos) throws ObjectNotInRelationException{
		Iterator it = getSpecificRelationsIterator(RelName);
		List trash = new ArrayList();
		while( it.hasNext()) {
			RelationInterface rel = (RelationInterface) it.next();
			if(rel.getPositionFromObject(owner)==OwnPos){
				trash.add(rel);
			}
		}
		for (Iterator iter = trash.iterator(); iter.hasNext();) {
			RelationInterface rel = (RelationInterface) iter.next();
			disconnect(rel);
		}
	}
	

	/**
	 * Verbindet eine schon existente Beziehung mit einen Namen
	 * 
	 * @param name
	 *            Namen der Beziehung
	 * @param ri
	 *            die Beziehung
	 */
	private void setNamedRelation(String name, RelationInterface ri) {
		if (objDictonary == null)
			objDictonary = new HashMap();
		//		if (objDictonary.containsKey(name))
		//			throw new IllegalRelationConnectionException(
		//			"Der Rollenname (Shortname) " + name + " wurde schon vergeben");
		if (!list.contains(ri))
			throw new IllegalRelationConnectionException("Die Beziehung " + ri
					+ " existiert nicht in " + owner);
		objDictonary.put(name, ri);
	} //setNamedRelation

	private void removeName(String name) throws ObjectNotInRelationException {
		if (objDictonary.containsKey(name))
			objDictonary.remove(name);
		else
			throw new ObjectNotInRelationException("Die benannte Beziehung "
					+ name + " ist zum löschen nicht vorhanden");
	}//removeName

	/**
	 * Manche Beziehungen werden mit Namen eingetragen. Diese kann man mit
	 * dieser Methode erhalten.
	 * 
	 * @param name
	 *            Name des Objekts
	 * @return die Beziehung mit dem Rollenname
	 */
	public RelationInterface getRelationByName(String key)
			throws ObjectNotInRelationException {
		if (!(key instanceof String))
			throw new ClassCastException("String wird erwartet. (" + key + ")");
		String strKey = (String) key;
		int pos = strKey.indexOf('.');
		//nicht etwas in der Art p.x
		if (pos == -1) {
			if (objDictonary == null)
				return null;
			RelationInterface ri = (RelationInterface) objDictonary.get(key);
			return ri;
		} //if pos ==-1

		String begin = strKey.substring(0, pos);
		String end = strKey.substring(pos + 1);

		RelationInterface ri = (RelationInterface) objDictonary.get(begin);
		if (ri == null)
			throw new ObjectNotInRelationException(
					"Das Teil-Objekt (Beziehung) " + begin
							+ " wurde nicht gefunden.");
		MathObject mo = (MathObject) ri.getPartner(owner);
		if (mo == null)
			throw new NullPointerException("Das Teil-Objekt " + begin
					+ " wurde nicht gefunden (" + strKey + ")");

		return mo.getRelationContainer().getRelationByName(end);
	} //getRelationByName

	/**
	 * Sucht den Erzeugungspfad. Die Suche nimmt bezug auf die Klasse des
	 * Objekts der Beziehung.
	 * 
	 * @return Erzeugungspfad oder null
	 */
	public StringPath getCreationPath() {
		int pos = RelationInterface.SECOND;
		if (owner instanceof AbstractAffineObject)
			pos = RelationInterface.FIRST;

		Iterator iter = getAllRelationsIterator();
		while (iter.hasNext()) {
			RelationInterface ri = (RelationInterface) iter.next();
			try {
				if (ri.getPositionFromObject(owner) == pos)
					continue;
			} catch (ObjectNotInRelationException e) {
				ExceptionManager.doError(e);
			}
			if (ri.getCreationPath() == null)
				continue;
			return ri.getCreationPath();
		} //while
		return null;
	}//getRelationPath

	/**
	 * Manche Beziehungen werden mit Namen eingetragen. Die Objekte kann man mit
	 * dieser Methode erhalten.
	 * 
	 * Diese Methode sucht nur in diesem Container (flat search)
	 * 
	 * @param name
	 *            Name des Objekts
	 * @return Das Objekt mit dem Rollenname oder null
	 * @throws ObjectNotInRelationException
	 */
	private MathObject _getObjectByName(String key) throws ObjectNotInRelationException {
		try {
			// Entweder benannte Beziehung ...
			RelationInterface ri = getRelationByName(key);
			if (ri != null) {
				return ri.getPartner(owner);
			}//if
			
			// ... oder eine Funktion, die nach der Evaluierung einen 
			// benannte Beziehung besitzt.
			if (owner instanceof UserFunction) {
				MathObject mo2 = (MathObject) ((UserFunction) owner).eval();
				RelationInterface ri2 = mo2.getRelationContainer().getRelationByName(key);
				if(ri2 !=null){
					return ri2.getPartner(mo2);
				}
			}//if
			return null;
		} catch (Throwable e) {
			throw new ObjectNotInRelationException("Fehler bei der Suche von '" + key
					+ "' in " + owner, e);
		}//catch
	} //_getObjectByName

	/**
	 * Sucht rekursiv nach den Unterobjekt z.B. begin.x
	 * 
	 * @param key
	 *            Pfad zum Unterobjekt
	 * @throws ObjectNotInRelationException
	 */
	public MathObject getObjectByName(String key)
			throws ObjectNotInRelationException {
		int pos = Broker.firstPointPosition(key);
		//nicht etwas in der Art p.x
		if (pos == -1) {
			MathObject mo = _getObjectByName(key);
			return mo;
		}//if

		// aus p.x.y wird p und x.y
		String begin = key.substring(0, pos);
		String end = key.substring(pos + 1);

		try {
			MathObject mo = (MathObject) _getObjectByName(begin);
			if (mo == null)
				throw new ObjectNotInRelationException("Das Objekt " + begin
						+ " wurde nicht gefunden (" + key + ")");

			return mo.getRelationContainer().getObjectByName(end);
		} catch (ObjectNotInRelationException e) {
			throw new ObjectNotInRelationException("Das Objekt " + key
					+ " wurde nicht gefunden.\n" + e.getMessage(), e);
		} //catch
	} //getObjectByName

	/**
	 * Gibt eine Liste der benannten Beziehungen zurück
	 * 
	 * @return Namensliste (String) der benannten Beziehungen
	 */
	public Set getAllNamedRelationNames() {
		if (objDictonary == null)
			return new HashSet();
		return new HashSet(objDictonary.keySet());
	} //getAllNamedRelationNames

	/**
	 * Gibt alle Beziehungen zurück
	 */
	public Iterator getAllRelationsIterator() {
		return list.iterator();
	} //getAllRelationsIterator

	/**
	 * Gibt Beziehungen zurück, die einen bestimmten Namen haben. Es wird von
	 * der Relation getName() aufgerufen.
	 * 
	 * @see RelationInterface#getName()
	 */
	public Iterator getSpecificRelationsIterator(String type) {
		List specRels = new LinkedList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			RelationInterface element = (RelationInterface) iter.next();
			if (type.equals(element.getName()))
				specRels.add(element);
		} //for iter
		return specRels.iterator();
	} //getSpecificRelationsIterator

	/**
	 * Fügt einen ContainerListener hinzu
	 */
	public void addRelationContainerListener(RelationContainerListener rcl) {
		if (containerListeners == null)
			containerListeners = new LinkedList();
		containerListeners.add(rcl);
	} //addRelationContainerListener

	/**
	 * Entfernt einen ContainerListener
	 */
	public void removeRelationContainerListener(RelationContainerListener rcl) {
		if (containerListeners.contains(rcl)) {
			containerListeners.remove(rcl);
		} else
			throw new NullPointerException("Der RelationContainerListener "
					+ rcl
					+ " ist nicht registriert und kann nicht gelöscht werden.");
	} //removeRelationContainerListener

	/**
	 * Gibt alle Relationen zurück, die Renewable sind.
	 * 
	 * @see RelationInterface#receivesRenew
	 */
	public Iterator getRenewableObjectsIterator(MathObject fromObj) {
		List renewObjects = new LinkedList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			RelationInterface element = (RelationInterface) iter.next();
			try {
				if (element.receivesRenew(fromObj))
					renewObjects.add(element);
			} catch (ObjectNotInRelationException e) {
				ExceptionManager.doError(e);
			} //catch
		} //for iter
		return renewObjects.iterator();
	} //getRenewableObjectsIterator

	/**
	 * Sammelt von fromObject aus alle Objekte zusammen, die mitgelöscht werden
	 * sollen, und trägt diese in die Menge ein. <br>
	 * Achtung! Es wird nicht geprüft ob, diese Objekte sich löschen dürfen.
	 * 
	 * @param fromObject
	 * @param objectsToDelete
	 */
	public void getAllObjectsToDelete(MathObject fromObject, Set objectsToDelete)
			throws DeleteRelationException {
		//eigenes Objekt hinzufügen, wenn dieses gelöscht werden darf
		if (!fromObject.testDelete())
			throw new DeleteRelationException("Das Objekt mit dem Schlüssel '"
					+ fromObject.getKey() + "' läßt sich nicht löschen.");
		objectsToDelete.add(fromObject);

		Iterator iter = getAllRelationsIterator();
		while (iter.hasNext()) {
			RelationInterface r = (RelationInterface) iter.next();
			try {
				MathObject partner = r.getPartner(fromObject);

				/*
				 * wenn der Partner nicht schon in der Menge steht wird auch
				 * diese Beziehung gefragt.
				 */
				if (objectsToDelete.contains(partner))
					continue;
				r.addObjectsToDelete(fromObject, objectsToDelete);
			} catch (ObjectNotInRelationException e) {
				ExceptionManager.doError(e);
			} //catch
		} //while
	} //getAllObjectsToDelete

	/**
	 * Speichert alle Objekte, die durch die Veränderung von changedObjects
	 * verursacht wurden, in objForRenew
	 */
	public void getAllMessageableObjects(MathObject changedObject,
			Set objForRenew) {
		//eigenes Objekt hinzufügen
		objForRenew.add(changedObject);

		Iterator iter = getAllRelationsIterator();
		while (iter.hasNext()) {
			RelationInterface r = (RelationInterface) iter.next();
			try {
				MathObject partner = r.getPartner(changedObject);
				//Beziehungen, die kein Renew wollen auslassen
				if (!r.receivesRenew(changedObject))
					continue;
				// Partner, die schon in der Menge stehen auslassen
				if (objForRenew.contains(partner))
					continue;
				partner.getRelationContainer().getAllMessageableObjects(
						partner, objForRenew);
			} catch (ObjectNotInRelationException e) {
				ExceptionManager.doError(e);
			} //catch
		} //while
	} //getAllMessageableObjects

	/**
	 * Diese Methode überprüft, ob mit dieser Objektmenge, sich alle Objekte
	 * löschen dürfen.
	 * 
	 * @param objectsToDelete
	 *            Menge der Objekte, die gelöscht werden sollen.
	 * @param broker
	 *            Wir nur zur Fehleranalyse gebraucht
	 */
	public void checkDeleteOperation(Set objectsToDelete, Broker broker)
			throws IllegalRelationConnectionException,
			ObjectNotInRelationException, DeleteRelationException {
		for (Iterator iter = objectsToDelete.iterator(); iter.hasNext();) {
			MathObject obj = (MathObject) iter.next();
			_checkDeleteOperation(obj, objectsToDelete, broker);
		} //for
	} //checkDeleteOperation

	public String toString() {
		return toString("");
	} //toString

	/**
	 * @param indent
	 *            String für das Einrücken der Ausgabe
	 */
	public String toString(String indent) {
		StringBuffer sb = new StringBuffer();
		String name = (String) owner.getKey();
		if (name == null)
			name = "?" + owner.toString().replaceAll("null:=", "");
		//name = tryToGetFullName(owner, "");
		Collections.sort(list);
		sb.append(name
				+ " "
				+ list
				+ (((containerListeners == null) || containerListeners
						.isEmpty()) ? "" : containerListeners.toString()));
		if ((objDictonary != null) && (!objDictonary.isEmpty())) {
			indent += "\t";
			Set keySet = new TreeSet(objDictonary.keySet());
			for (Iterator iter = keySet.iterator(); iter.hasNext();) {
				Object key = iter.next();
				RelationInterface ri = (RelationInterface) objDictonary
						.get(key);
				try {
					MathObject mo = ri.getPartner(owner);
					sb.append("\n" + indent + key + " => "
							+ mo.getRelationContainer().toString(indent));
				} catch (ObjectNotInRelationException e) {
					ExceptionManager.doError(e);
				} //catch
			} //for iter
		} //if
		return sb.toString();
	} //toString

	/**
	 * gibt die Anzahl der Beziehungen aus
	 */
	public int size() {
		return list.size();
	} //size

	/**
	 * Versucht von einem Objekt den vollständigen Namen zu bekommen
	 * 
	 * @param partName
	 *            Teilname, der schon bekannt ist
	 * @return Vollständiger Name
	 */
	public static String tryToGetFullName(MathObject mo, String partName) {
		if (mo.getKey() != null) {
			return (String) mo.getKey() + partName;
		}//if
		Iterator iter = mo.getRelationContainer().getAllRelationsIterator();
		MathObject partner=null;
		while (iter.hasNext()) {
			try {
				RelationInterface ri = (RelationInterface) iter.next();
				partner = ri.getPartner(mo);
//				if(partner instanceof UserFunction && ((UserFunction)partner).isEncapsulated())
//					partner = (MathObject) ((UserFunction)partner).eval();
				RelationContainer rc = partner.getRelationContainer();
				Set set = rc.getAllNamedRelationNames();

				//Suche nach benannten Partner
				if (set != null){					
					for (Iterator iter2 = set.iterator(); iter2.hasNext();) {
						String key = (String) iter2.next();
						if (rc.getObjectByName(key) != mo)
							continue;
						return RelationContainer.tryToGetFullName(
								ri.getPartner(mo), Broker.SEPERATOR + key
										+ partName);
					}//for
				}//if
				//Suche nach Part-Of-Elter
				Iterator iter2 = rc.getSpecificRelationsIterator(RelationFactory.getPart_Of_Relation().getName());
				while(iter2.hasNext()){
					RelationInterface ri2 = (RelationInterface) iter2.next();
					if(ri2.getPartner(partner)!=mo) continue;
					if(ri2.getPositionFromObject(mo)==RelationInterface.FIRST)
						continue;
					return RelationContainer.tryToGetFullName(
							partner, partName);
				}//while

			} catch (ObjectNotInRelationException e) {
				ExceptionManager.doError(e);
//			} catch (MathException e) {
//				ExceptionManager.doError("Fehler beim Entkapseln der Funktion "+partner,e);
			}//catch
		} //while
		return partName;
	} //tryToGetFullName

	//---------------- Hilfsmethoden
	// -----------------------------------------------

	/**
	 * Überprüft für ein Objekt, ob dieses in der Objektmenge gelöscht werden
	 * darf.
	 * 
	 * @param fromObject
	 *            Objekt, das untersucht wird
	 * @param objectsToDelete
	 *            Objektmenge, in der fromObjekt eingetragen ist
	 */
	private void _checkDeleteOperation(MathObject fromObject,
			Set objectsToDelete, Broker broker)
			throws ObjectNotInRelationException,
			IllegalRelationConnectionException, DeleteRelationException {

		Iterator iter = fromObject.getRelationContainer()
				.getAllRelationsIterator();
		while (iter.hasNext()) {
			RelationInterface r = (RelationInterface) iter.next();

			//wenn der Partner auch in der Menge ist, braucht er nicht
			// Überprüft werden.
			if (objectsToDelete.contains(r.getPartner(fromObject)))
				continue;

			int isDele = r.getRoleFromObject(fromObject).isDeleteable();
			if (isDele == Role.DELETE_NO) {
				String strA = RelationContainer.tryToGetFullName(r
						.getPartner(fromObject), "").toString();
				String strB = RelationContainer.tryToGetFullName(
						fromObject, "").toString(); 
				throw new DeleteRelationException(
						"Das Objekt mit dem Schlüssel '"
								+ strA				
								+ "' hängt noch von '"
								+ strB						
								+ "' ab. Löschen von '"
								+ strB
								+ "' nicht möglich.(" + r + ")\n");
			} else if (isDele == Role.DELETE_YES) {
				//alles o.k.
			} else if (isDele == Role.DELETE_ASK_PARTNER) {
				//Partner fragen, ob gelöscht werden darf
				int isDele2 = r.getRoleFromObject(r.getPartner(fromObject))
						.isDeleteable();
				if (isDele2 == Role.DELETE_YES) {
					//alles o.k.
					continue;
				} else if (isDele2 == Role.DELETE_NO) {
					throw new DeleteRelationException("Der Partner '"
							+ r.getPartner(fromObject).getKey()
							+ "' des Objekts '" + fromObject.getKey()
							+ "' erlaubt nicht das Löschen.");
				} else if (isDele2 == Role.DELETE_ASK_PARTNER) {
					throw new IllegalRelationConnectionException(
							"Beide Beziehungen haben eine 'Ask Partner Delete'-Beziehung");
				} else {
					throw new IllegalArgumentException(
							"isDeletable wurde falsch gesetzt");
				} //else
			} else {
				throw new IllegalArgumentException(
						"isDeletable wurde falsch gesetzt");
			} //else
		} //while hasNext()
	} //_checkDeleteOperation

	/**
	 * Fügt eine Beziehung in diesen RelationContainer hinzu. Wenn der Partner
	 * einen Shortname besitzt, wird dieser ebenfalls notiert.
	 * 
	 * @param ri
	 * @throws ObjectNotInRelationException
	 * @see MathObject#getShortName()
	 */
	private void addRelation(RelationInterface ri) {
		list.add(ri);
		//gibt es Listener, die informiert werden wollen?
		if ((containerListeners == null) || containerListeners.isEmpty())
			return;
		for (Iterator iter = containerListeners.iterator(); iter.hasNext();) {
			RelationContainerListener element = (RelationContainerListener) iter
					.next();
			//Event auslösen
			element.relationAdded(this, ri);
		} //for iter
	} //addRelation

	/**
	 * Entfernt eine Beziehung in diesem RelationContainer
	 */
	private void removeRelation(RelationInterface ri)
			throws ObjectNotInRelationException {
		if (!list.contains(ri)) {
			throw new NullPointerException("Die Relation " + ri
					+ "wurde nicht gefunden. (" + list + ")");
		} //if
		//eventuell Namen austragen
		String key = ri.getShortName(ri.getPositionFromObject(ri
				.getPartner(owner)));
		if ((key != null) && (objDictonary != null)
				&& objDictonary.containsKey(key))
			objDictonary.remove(key);

		//eigentliches austragen
		list.remove(ri);

		//gibt es Listner, die informiert werden wollen?
		if ((containerListeners == null) || containerListeners.isEmpty())
			return;
		for (Iterator iter = containerListeners.iterator(); iter.hasNext();) {
			RelationContainerListener element = (RelationContainerListener) iter
					.next();
			//Event auslösen
			element.relationRemoved(this, ri);
		} //for iter
	} //removeRelation
	
	
	

} //class RelationContianer
