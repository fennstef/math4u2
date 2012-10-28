package math4u2.controller;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import math4u2.controller.reference.PathReference;
import math4u2.controller.relation.DeleteRelationException;
import math4u2.controller.relation.IllegalRelationConnectionException;
import math4u2.controller.relation.ObjectNotInRelationException;
import math4u2.controller.relation.Relation;
import math4u2.controller.relation.RelationContainer;
import math4u2.controller.relation.RelationException;
import math4u2.controller.relation.RelationFactory;
import math4u2.controller.relation.RelationInterface;
import math4u2.controller.relation.RoleInterface;
import math4u2.mathematics.collection.HasTypeString;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.StandardFunction;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.Variable;
import math4u2.parser.parser;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.parser.ParseException;
import math4u2.view.graph.HasGraph;

/**
 * Objekte mit dem Interface MathObject können von einem Broker verwaltet
 * werden. Die Objekte werden dabei über einen Schlüssel verwaltet, den sie über
 * die Methode getKey() selbst liefern. Darunter können wieder Unterobjekte
 * angelegt werden. <br>
 * <br>
 * Die Objekte können zu anderen Objekten in Beziehung stehen. Der Broker
 * kontrolliert über diese Beziehungen, ob Objekte gelöscht oder benachrichtigt
 * werden dürfen.
 * 
 * @author Fenn Stefan
 * @see MathObject#getKey()
 * @see RelationInterface
 */
public class Broker {

	/** Map aller veröffentlichten Objekte */
	protected HashMap objectDic;

	/** Alle Listener, die beim Broker angemeldet sind */
	private List brokerListeners = new LinkedList();

	/** Anzahl der Initialisierungsobjekte */
	private int initializedObjectsSize;

	/** wird bei defineObject verwendet */
	public static final boolean FIRST_OBJECT = false;

	public static final boolean SECOND_OBJECT = true;

	/** Seperator, der zum Trennen von Objekten benutzt wird */
	public static final String SEPERATOR = ".";

	/**
	 * Erzeugt einen neuen Broker und initialisiert ihn mit allen
	 * Standardfunktionen.
	 */
	public Broker() {
		objectDic = new HashMap();
		try {
			Function.registerAll(this);
		} catch (Exception e) {
			ExceptionManager.doError("Fehler bei der Erzeugung der Standard-Funktionen", e);
		} //catch
		initializedObjectsSize = objectDic.size();
	} //Konstruktor

	/**
	 * Fügt einen BrokerListener hinzu
	 */
	public void addBrokerListener(BrokerListener bl) {
		brokerListeners.add(bl);
	} //addBrokerListener

	/**
	 * Entfernt einen BrokerListener
	 */
	public void removeBrokerListener(BrokerListener bl) {
		brokerListeners.remove(bl);
	} //removeBrokerListener

	/**
	 * Gibt das Objekt zurück, das durch Key identifiziert ist.
	 * 
	 * @throws BrokerException
	 *             Wenn das Objekt im Broker nicht registriert wurde, wird diese
	 *             Exception geworfen.
	 * @see getObject(Object)
	 */
	public MathObject tryToGetObject(Object key) throws BrokerException {
		MathObject mo = (MathObject) getObject(key);
		if (mo == null) {
			String extMessage = "";
			if (!(key instanceof String))
				extMessage = "\nDer Key ist kein String. Vielleicht wurde die getKey() Methode vergessen.";
			throw new BrokerException("Das Objekt mit dem Schlüssel " + key
					+ " ist nicht registriert." + extMessage);
		} //if
		else
			return mo;
	} //getObject

	/**
	 * Gibt das Objekt zurück, das durch Key identifiziert ist. Falls Key nicht
	 * bekannt ist, wird eine BrokerException ausgeloest.
	 */
	public MathObject getObject(Object key) throws BrokerException {
		MathObject mo = (MathObject) objectDic.get(key);
		if (!(key instanceof String))
			return mo;
		String strKey = (String) key;
		
		if(isExtendedObjectExpression(strKey)){
			try {
				//Erweiteter Ausdruck parsen und evaluieren
				TermNode t = parser.parsePath(strKey, this);
				PathReference pr = (PathReference) t;
				pr.removeLastEval(); 
				mo = (MathObject) pr.eval();
			} catch (ParseException e) {
				String message = "Erweiterter Audruck '"+strKey+"' konnte nicht geparst werden.";
				throw new BrokerException(message, e);
			} catch (MathException e) {
				String message = "Objekt '"+strKey+"' konnte nicht gefunden werden.";
				throw new BrokerException(message,e);
			} catch (Throwable t){
				String message = "Objekt '"+strKey+"' konnte nicht gefunden werden.";
				throw new BrokerException(message,t);
			}
			return mo;
		}//if isExtendedObjectExpression
	
		int pos = firstPointPosition(strKey);
		int endPos = pos + 1;
		//nicht etwas in der Art p.x
		if (pos == -1){
			return mo;
		}

		// aus p.x.y wird p und x.y
		String begin = strKey.substring(0, pos);
		String end = strKey.substring(endPos);
		
		mo = (MathObject) objectDic.get(begin);

		if(mo==null) return mo;
		
		try {
			return mo.getRelationContainer().getObjectByName(end);
		} catch (ObjectNotInRelationException e) {
			throw new BrokerException("Das Objekt " + strKey
					+ " wurde nicht gefunden.\n" + e.getMessage(), e);
		} //catch
	} //getObject
	
	/**
	 * Erkennt ob ein Schlüssel einen erweiterten Ausdruck enthält.
	 * 
	 * erweiterte Ausdrücke sind z.B.: pl[i+1].y, pl[1].x
	 * keine erwiterten Ausdrücke sind z.B.: p.x, s.start.b, k usw.
	 * @param key
	 * @return
	 */
	public static boolean isExtendedObjectExpression(String key){
		//Pattern unescaped: ( [\[\]\(\)] )
		String tmp = key.replaceAll("([\\[\\]\\(\\)])", "");
		return !key.equals(tmp);
	}//isExtendedObjectExpression

	/**
	 * Gibt alle Objekte des Brokers in einer Reihenfolge aus, mit der man keine
	 * Erzeugungs-Schwierigkeiten hat und listet die Elemente, die von anderen
	 * Objekten gelöscht würden, nicht auf.
	 * 
	 * @return Geordnete Liste aller Objekte
	 * @throws BrokerException
	 */
	public List getOrderedContent() throws BrokerException {
		LinkedList result = new LinkedList();
		LinkedList automaticlyCreatedObjects = new LinkedList();
		while (objectDic.size() != result.size()) {
			Iterator iter = objectDic.values().iterator();
			int eingetragen = 0;
			while (iter.hasNext()) {
				MathObject mo = (MathObject) iter.next();
				//wenn das Objekt schon in result ist auslassen
				if (result.contains(mo))
					continue;
				try {
					Set temp = new HashSet(result);
					/*
					 * alle Objekte versuchen zu löschen, wenn dies nicht geht,
					 * wird eine Exception geworfen
					 */
					collectAndTryToDeleteObject(mo, temp);
					result.addFirst(mo);

					/*
					 * alle Beziehungen noch mit eintragen, die bei der
					 * Initialisierung von diesem Objekt erstellt werden, da
					 * diese sich nicht alleine eintragen können.
					 * 
					 * Es werden zusätzlich diese Objekte noch vermerkt und in
					 * der endgültigen Liste gelöscht.
					 */
					Iterator iter2 = mo.getRelationContainer()
							.getAllRelationsIterator();
					while (iter2.hasNext()) {
						RelationInterface r = (RelationInterface) iter2.next();
						//wenn das Objekt keine anderen Objekte erzeugt ->
						// fertig
						if (!r.createsPartner(mo))
							continue;

						MathObject mo2 = r.getPartner(mo);
						automaticlyCreatedObjects.add(mo2);
						if (!result.contains(mo2))
							result.addFirst(mo2);
					} //while
					eingetragen++;
				} catch (Exception e) {
				} //catch
			} //while
			if (eingetragen == 0)
				throw new BrokerException(
						"Es konnte keine geordnete Löschreihenfolge gefunden werden.");
		} //while nicht alle Elemente eingetragen
		result.removeAll(automaticlyCreatedObjects);
		//alle nicht zu löschende Objekte entfernen
		List trash = new LinkedList();
		for (Iterator iter = result.iterator(); iter.hasNext();) {
			MathObject element = (MathObject) iter.next();
			if (element instanceof StandardFunction)
				trash.add(element);
		} //for iter
		result.removeAll(trash);
		return result;
	} //getOrderedContent

	/**
	 * Gibt alle Objekte des Brokers unsortiert zurück.
	 * 
	 * @return Ungeordnete Liste aller Objekte
	 */
	public List getUnorderedContent() {
		LinkedList result = new LinkedList();
		Iterator iter = objectDic.values().iterator();
		while (iter.hasNext()) {
			result.add(iter.next());
		}//while

		return result;
	}//getUnorderedContent

	/**
	 * Das Element wird im Broker mit dem Schlüssel verwaltet, der durch
	 * object.getKey() geliefert wird. Dieses Objekt wird bekannt gegeben. Ab
	 * diesem Zeitpunkt ist das Objekt vom Broker aus sichtbar (und damit auch
	 * für alle Unterobjekte).
	 * 
	 * @param obj
	 *            Objekt, das bekannt gegeben wird
	 * @param name
	 *            Name des Objekts
	 * @throws BrokerException
	 * @throws ObjectNotInRelationException
	 */
	public void publishObject(MathObject obj, String name, List rollBackList)
			throws BrokerException, ObjectNotInRelationException {
		obj.setName(name);
		int pos = lastPointPosition(name);
		if (pos != -1) {
			// mit Punkt z.B. s.begin.x:=3, dann Elter suchen
			String parentStr = name.substring(0, pos);
			String childStr = name.substring(pos + 1);
			// Hole den Elter z.B. p
			MathObject parent = getObject(parentStr);
			if (parent == null)
				throw new BrokerException("Es wurde nicht " + parentStr
						+ " gefunden. (In " + name + ")");
			MathObject child = null;
			try {
				//Delegiere die Suche weiter z.B. 'suche begin.x'
				child = parent.getRelationContainer().getObjectByName(childStr);
			} catch (ObjectNotInRelationException e) {
				throw new BrokerException(e);
			} //catch
			if (child == null)
				throw new BrokerException("Es wurde nicht " + childStr
						+ " gefunden. (" + name + ")");
			makeSubstitution(obj, child, name);
		} else {			
			// ohne Unterobjekt
			if (!objectDic.containsKey(name))
				objectDic.put(name, obj);
			else
				makeSubstitution(obj, getObject(name), name);
		} //else

		//Benachrichtigung an alle abhängigen Objekte
		propagateChange(obj);
		//Event werfen an alle Listener
		for (Iterator iter = brokerListeners.iterator(); iter.hasNext();) {
			BrokerListener listener = (BrokerListener) iter.next();
			listener.newObjectPublished(new BrokerEvent(obj, name));
		} //for iter
	} //publishObject

	public void publishObject(MathObject obj, String name)
			throws BrokerException, ObjectNotInRelationException {
		publishObject(obj, name, new LinkedList());
	} //publishObject

	/**
	 * Definiert das Objekt mit seinen Unterbeziehungen. Die in parts
	 * enthaltenen Einträge sind Objekte mit der Schnittstelle MathObject, die
	 * beim Broker schon bekannt sein müssen. Sie werden als Teile von object
	 * eingetragen. Falls der Broker schon ein Element oldObject mit dem
	 * gleichen Schlüssel wie object.getKey() verwaltet, wird eine
	 * BrokerException geworfen. Das Objekt wird als Ergebnis zurückgeliefert.
	 * 
	 * @param parts
	 *            Liste aller MathObjects, die Teil einer Beziehung sind.
	 * @param relations
	 *            Liste aller Beziehungen, die geknüpft werden sollen.
	 * @param isSwap
	 *            entweder FIRST_OBJECT z.B. p->x, p->y, p->z <br>
	 *            oder SECOND_OBJECT z.B. x->p, y->p, z->p
	 */
	public void defineRelations(MathObject object, List parts, List relations,
			boolean isSwap) throws BrokerException {
		Object newKey = object.getIdentifier();
		MathObject oldObject = getObject(newKey);
		Set superSet = null;
		if (oldObject != null)
			superSet = getObjectsWithSpecPositionRelation(oldObject, 2,
					RelationFactory.getFunction_SubFunction_Relation()
							.getName());
		// Prüfen, ob Ersetzung nötig und erlaubt
		if ((oldObject != null)
				&& (!object.testSubstitution(oldObject, superSet))) {
			//			deleteAnonymous(parts);
			throw new BrokerException("Objekt mit Schluessel "
					+ newKey.toString() + " kann nicht ersetzt werden");
		} //if

		// eintragen
		Iterator li = parts.iterator();
		if (parts.size() != relations.size())
			throw new IllegalArgumentException("Die Teile(" + parts.size()
					+ ") und die Beziehungen(" + relations.size()
					+ ") müßen die gleiche Größe haben.");
		Iterator relIter = relations.iterator();
		while (li.hasNext()) {
			RelationInterface r = (RelationInterface) relIter.next();
			MathObject partner = (MathObject) li.next();
			try {				
				RelationContainer.connect(object, partner, r, isSwap, this);
			} catch (IllegalRelationConnectionException e) {
				throw new BrokerException(e);
			} catch (ObjectNotInRelationException e) {
				throw new BrokerException(e);
			} //catch
		} //while
	} //defineRelations

	/**
	 * Wie vorheriges defineRelations, wobei hier nur eine Sorte von Beziehungen
	 * angegeben werden muß. Dadurch werden alle Beziehungen, die geknüpft
	 * werden, mit dieser Beziehungsart geknüpft
	 * 
	 * @param object
	 * @param parts
	 *            Liste aller MathObjects, die Teil einer Beziehung sind.
	 * @param relation
	 * @throws BrokerException
	 */
	public void defineRelations(MathObject object, List parts,
			RelationInterface relation, boolean swap) throws BrokerException {
		List relations = new LinkedList();
		for (int i = 0; i < parts.size(); i++) {
			relations.add(relation.cloneRelation());
		} //for i
		defineRelations(object, parts, relations, swap);
	} //defineRelations

	public void defineRelations(MathObject object, List parts,
			RelationInterface relation) throws BrokerException {
		defineRelations(object, parts, relation, FIRST_OBJECT);
	} //defineRelations

	/**
	 * Trägt die neue Beziehung ein. Beide Objekte müssen beim Broker bekannt
	 * sein. Die Objekte werden dabei mit getKey() neu gesucht.
	 * 
	 * @see MathObject#getKey()
	 */
	public void addRelation(MathObject firstObject, MathObject secondObject,
			RelationInterface relation, boolean isSwap) throws BrokerException {
		//Hier werden die Objekt nochmals neu vom Broker refrenziert, wenn
		// diese einen Schlüssel haben.
		//Muß das unbeding sein ???
		if (firstObject.getIdentifier() != null)
			firstObject = getObject(firstObject.getIdentifier());
		if (secondObject.getIdentifier() != null)
			secondObject = getObject(secondObject.getIdentifier());

		//sind die beiden Objekte im Broker definiert
		if (firstObject == null)
			throw new BrokerException(
					"Das erste Objekt wurde nicht gefunden.\n");
		if (secondObject == null)
			throw new BrokerException(
					"Das zweite Objekt wurde nicht gefunden.\n");
		try {
			RelationContainer.connect(firstObject, secondObject, relation,
					isSwap, this);
		} catch (IllegalRelationConnectionException e) {
			throw new BrokerException(e);
		} catch (ObjectNotInRelationException e) {
			throw new BrokerException(e);
		} //catch
	} //addRelation

	/**
	 * Löscht das Objekt mit dem Schlüssel Key beim Broker. Es werden alle
	 * Unterobjekte gelöscht, die dies von der Beziehung her verlangen
	 * (deletesPartner()). <br>
	 * <br>
	 * Das Objekt wird über isDeleteable() gefragt, ob dieser die Löschung
	 * zuläßt. <br>
	 * <br>
	 * Hierbei sind zwei Untersagungen möglich: <br>
	 * 1.Die Beziehung läßt sich nicht löschen. 2.Die Beziehung frägt den
	 * Partner und dieser läßt sich nicht löschen.
	 * 
	 * @param key
	 *            Schlüssel des zu löschenden Objekts
	 * @throws BrokerException
	 *             Wird geworfen, wenn eine Beziehung den Löschvorgang nicht
	 *             zuläßt.
	 * 
	 * @see RoleInterface#deletesPartner
	 * @see RoleInterface#isDeleteable
	 */
	public void deleteObjectByKey(Object key) throws BrokerException {
		MathObject mo = tryToGetObject(key);
		deleteObject(mo);
	} // deleteObject

	public void deleteObject(MathObject mo) throws BrokerException {
		Set objToDel = new HashSet();
		collectAndTryToDeleteObject(mo, objToDel);

		try {
			deleteProvedObjectSet(objToDel);
		} catch (ObjectNotInRelationException e) {
			throw new BrokerException(e);
		}
		//Event werfen an alle Listener
		for (Iterator iter = brokerListeners.iterator(); iter.hasNext();) {
			BrokerListener listener = (BrokerListener) iter.next();
			listener.objectDeleted(new BrokerEvent(mo, (String) mo.getIdentifier()));
		} //for iter
	} //deleteObject

	/**
	 * Löscht eine Liste von Objekten
	 * 
	 * @param objectsList
	 *            Liste aller Schlüssel der Objekten, die gelöscht werden sollen
	 * @throws BrokerException
	 */
	public void deleteObjects(List objectsList) throws BrokerException {
		Set objToDel = new HashSet();
		//alle zu löschenden Objekte einsammeln
		for (Iterator iter = objectsList.iterator(); iter.hasNext();) {
			MathObject mo = tryToGetObject(iter.next());
			RelationContainer rc = mo.getRelationContainer();
			try {
				rc.getAllObjectsToDelete(mo, objToDel);
			} catch (DeleteRelationException e) {
				throw new BrokerException(e);
			} //catch
		} //for
		//löschen überprüfen
		for (Iterator iter = objToDel.iterator(); iter.hasNext();) {
			RelationContainer rc = ((MathObject) iter.next())
					.getRelationContainer();
			try {
				rc.checkDeleteOperation(objToDel, this);
			} catch (RelationException e) {
				throw new BrokerException(e);
			} //catch
		} //for
		try {
			deleteProvedObjectSet(objToDel);
		} catch (ObjectNotInRelationException e) {
			throw new BrokerException(e);
		}
		//Event werfen an alle Listener
		for (Iterator iter = brokerListeners.iterator(); iter.hasNext();) {
			BrokerListener listener = (BrokerListener) iter.next();
			listener.objectDeleted(new BrokerEvent(objectsList, null));
		} //for iter
	} //deleteObjects
	
	/**
	 * Löscht eine Liste von anonymen Objekten
	 * 
	 * @param objectsList
	 *            Liste aller Objekten, die gelöscht werden sollen.
	 * @throws BrokerException
	 */
	public void deleteAnonymousObjects(List objectsList) throws BrokerException {
		if(objectsList.isEmpty()) return;
		Set objToDel = new HashSet();
		//alle zu löschenden Objekte einsammeln
		for (Iterator iter = objectsList.iterator(); iter.hasNext();) {
			MathObject mo = (MathObject) iter.next();
			RelationContainer rc = mo.getRelationContainer();
			try {
				rc.getAllObjectsToDelete(mo, objToDel);
			} catch (DeleteRelationException e) {
				throw new BrokerException(e);
			} //catch
		} //for
		//löschen überprüfen
		for (Iterator iter = objToDel.iterator(); iter.hasNext();) {
			RelationContainer rc = ((MathObject) iter.next())
					.getRelationContainer();
			try {
				rc.checkDeleteOperation(objToDel, this);
			} catch (RelationException e) {
				throw new BrokerException(e);
			} //catch
		} //for
		try {
			deleteProvedObjectSet(objToDel);
		} catch (ObjectNotInRelationException e) {
			throw new BrokerException(e);
		}
		//Event werfen an alle Listener
		for (Iterator iter = brokerListeners.iterator(); iter.hasNext();) {
			BrokerListener listener = (BrokerListener) iter.next();
			listener.objectDeleted(new BrokerEvent(objectsList, null));
		} //for iter
	} //deleteAonymousObjects

	/**
	 * Ermittlet sämtliche beim Broker bekannten MathObjekte, die von einer
	 * Veränderung des MathObjekts changedObject direkt oder indirekt betroffen
	 * sind und benachrichtigt jedes durch Aufruf seiner Methode renew().
	 * 
	 * @see MathObject#renew(MathObject)
	 */
	public synchronized void propagateChange(MathObject changedObject)
			throws BrokerException {
		RelationContainer rc = changedObject.getRelationContainer();
		Set objForRenew = new HashSet();
		
		//alle zu benachrichtigenden Objekte einsammeln
		rc.getAllMessageableObjects(changedObject, objForRenew);
		//sich selber nicht benachrichtigen
		objForRenew.remove(changedObject);
		
		if(objForRenew.isEmpty())
			return;
		
		//alle UserFunctions in der Liste invalidieren
		for (Iterator iter = objForRenew.iterator(); iter.hasNext();) {
			MathObject element = (MathObject) iter.next();
			if(element instanceof UserFunction)
				((UserFunction)element).invalidate();
		} //for iter
		
		//alle Objekte in der Liste benachrichtigen
		for (Iterator iter = objForRenew.iterator(); iter.hasNext();) {
			MathObject element = (MathObject) iter.next();
			element.renew(changedObject);
		} //for iter
	} //propagateChange

	/**
	 * Gibt alle Keys zurück, die beim Broker definiert wurden.
	 * Standardfunktionen werden ignoriert.
	 * 
	 * @return Menge aller Schlüssel der eingetragenen MathObjects
	 * @see #publishObject(MathObject, String)
	 */
	public Set getFirstLevelObjects() {
		Set keySet = new TreeSet(objectDic.keySet());
		List removes = new LinkedList();
		for (Iterator iter = objectDic.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			MathObject mo = (MathObject) objectDic.get(key);
			if (mo instanceof StandardFunction)
				removes.add(key);
		} //for iter
		keySet.removeAll(removes);
		return keySet;
	} //getFirstLevelObjects

	/**
	 * Gibt an, ob das Objekt schon mit publishObject(...) erzeugt wurde.
	 */
	public boolean knowsObject(String key) {
		try {
			return getObject(key) != null;
		} catch (BrokerException e) {
			return false;
		}
	} //knowsObject

	/**
	 * Wie getObject(...), jedoch wird hier die Fehlermeldung schon abgewickelt.
	 * 
	 * @param key
	 *            Schlüssel des Objekts
	 * @return MathObject oder null
	 */
	public MathObject getMathObject(String key) {
		try {
			return getObject(key);
		} catch (BrokerException e) {
			ExceptionManager.doError("Das Objekt "+key+" wurde nicht gefunden.", e);
		} //catch
		return null;
	} //getMathObject

	public MathObject getMathObject(Object key) {
		try {
			return getObject(key);
		} catch (BrokerException e) {
			ExceptionManager.doError("Das Objekt "+key+" wurde nicht gefunden.", e);
		} //catch
		return null;
	} //getMathObject

	/**
	 * Erzeugt einen String aller bekannten Objekte
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		int size = objectDic.size() - initializedObjectsSize;
		sb.append("Broker (" + (objectDic.size() - initializedObjectsSize)
				+ ((size == 1) ? " Objekt" : " Objekte") + " + "
				+ initializedObjectsSize + " Inits) :\n");
		Set keys = new TreeSet(objectDic.keySet());
		for (Iterator iter = keys.iterator(); iter.hasNext();) {
			MathObject mo = (MathObject) objectDic.get(iter.next());
			if (mo instanceof StandardFunction)
				continue;
			sb.append(mo.getRelationContainer() + "\n");
		} //for iter
		return sb.toString();
	} //toString

	/**
	 * Berechnet die erste Position eines Punktes z.B. "aaa.x.y" gibt 3 zurück
	 * 
	 * @param key
	 *            Schlüssel des Objekts
	 * @return Position des letzten Punktes
	 */
	public static int firstPointPosition(String key) {
		return key.indexOf(Broker.SEPERATOR);
	} //firstPointPosition

	/**
	 * Berechnet die letzte Position eines Punktes z.B. "aaa.x.y" gibt 5 zurück
	 * 
	 * @param key
	 *            Schlüssel des Objekts
	 * @return Position des letzten Punktes
	 */
	public static int lastPointPosition(String key) {
		return key.lastIndexOf(Broker.SEPERATOR);
	} //lastPointPosition
	
	/**
	 * Löscht alle <code>MathObject</code> -Objekte aus dem Dictionary.
	 * 
	 * Dabei wird nicht auf die Beziehungen untereinander geachtet.
	 *  
	 */
//	public void deleteAllObjects() {
//		Set trash = new HashSet();
//		for (Iterator iter = objectDic.values().iterator(); iter.hasNext();) {
//			MathObject element = (MathObject) iter.next();
//
//			if (element instanceof StandardFunction
//					|| element instanceof ListBox
//					|| element instanceof CompleteViewBox
//					|| element.getKey().equals(MousePosition.NAME)) {
//				RelationContainer rc = element.getRelationContainer();
//				LinkedList trashRel = new LinkedList();
//				//Beziehungen sammeln
//				for (Iterator iter2 = rc.getAllRelationsIterator(); iter2
//						.hasNext();) {
//					RelationInterface ri = (RelationInterface) iter2.next();
//					trashRel.add(ri);
//					try {
//						trash.add(ri.getPartner(element));
//					} catch (ObjectNotInRelationException e) {
//						ExceptionManager.doError("Fehler beim Löschen aller Objekte",e);
//					}
//				}//for
//
//				//Gesammelte Beziehungen löschen
//				for (Iterator iter2 = trashRel.iterator(); iter2.hasNext();) {
//					RelationInterface ri = (RelationInterface) iter2.next();
//					try {
//						RelationContainer.disconnect(ri);
//					} catch (ObjectNotInRelationException e) {
//						ExceptionManager.doError("Fehler beim Löschen aller Objekte",e);
//					}//catch
//				}//for
//				continue;
//			}//if
//
//			trash.add(element);
//		}//for iter
//
//		try {
//			deleteProvedObjectSet(trash);
//		} catch (ObjectNotInRelationException e) {
//			ExceptionManager.doError("Fehler beim Löschen aller Objekte",e);
//		}
//	}//deleteAll
	

	//------------------------------Hilfsmethoden---------------------------------

	/**
	 * Sammelt die zu löschenden Objekte zusammen und überprüft, ob diese
	 * Objektmenge gelöscht werden darf.
	 * 
	 * @param key
	 *            Schlüssel des zu löschenden Objekts.
	 * @return Objektmenge, die gelöscht werden muß.
	 */
	private void collectAndTryToDeleteObject(MathObject mo, Set objToDel)
			throws BrokerException {
		RelationContainer rc = mo.getRelationContainer();
		try {
			//alle zu löschenden Objekte einsammeln
			rc.getAllObjectsToDelete(mo, objToDel);
		} catch (DeleteRelationException e) {
			throw new BrokerException(e);
		} //catch
		try {
			//testen, ob diese Menge von Objekten gelöscht werden darf
			rc.checkDeleteOperation(objToDel, this);
		} catch (DeleteRelationException e) {
			throw new BrokerException(e);
		} catch (ObjectNotInRelationException e) {
			throw new BrokerException(e);
		}
	} // collectAndTryToDeleteObject

	/**
	 * Löscht eine überprüfte Objektmenge. Dies ist der eigentliche
	 * Löschvorgang.
	 * 
	 * @param objToDel
	 *            Menge der Objekte, die gelöscht werden sollen.
	 */
	protected void deleteProvedObjectSet(Set objToDel)
			throws ObjectNotInRelationException {
		for (Iterator iter = objToDel.iterator(); iter.hasNext();) {
			MathObject element = (MathObject) iter.next();

			LinkedList trash = new LinkedList();
			Iterator iter2 = element.getRelationContainer()
					.getAllRelationsIterator();
			while (iter2.hasNext()) {
				RelationInterface r = (RelationInterface) iter2.next();
				//jede Beziehung einsammeln
				trash.add(r);
			} //while
			iter2 = trash.iterator();
			while (iter2.hasNext()) {
				//Beziehung auflösen
				RelationContainer.disconnect((RelationInterface) iter2.next());
			} //while
			//Objekt aus der Brokerliste löschen
			if(element==objectDic.get(element.getIdentifier()))
				objectDic.remove(element.getIdentifier());
			element.prepareDelete();
		} //for iter
	} //deleteProvedObjectSet

	/**
	 * Gibt von einer Beziehungsart das erste oder zweite Objekt heraus.
	 * 
	 * @param mo
	 *            Objekt, in dem gesucht werden soll
	 * @param pos
	 *            Soll das erste oder das zweite Objekt gefunden werden (z.B.
	 *            Function-SubFunction -> pos=1 Function ; pos=2 SubFunction)
	 * @param type
	 *            Name der Beziehungsart
	 * @return Menge der Objekte
	 */
	private Set getObjectsWithSpecPositionRelation(MathObject mo, int pos,
			String type) throws BrokerException {
		HashSet set = new HashSet();
		Relation.checkPosition(pos);
		pos -= 1;
		Iterator iter = mo.getRelationContainer().getSpecificRelationsIterator(
				type);
		while (iter.hasNext()) {
			RelationInterface r = (RelationInterface) iter.next();
			try {
				if (r.getPositionFromObject(mo) == pos)
					set.add(r.getPartner(mo));
			} catch (ObjectNotInRelationException e) {
				throw new BrokerException(e);
			}
		} //while iter
		return set;
	} //getObjectsWithFirstPostitionRelation

	private Set getAllInternObjects(MathObject startObject)
			throws ObjectNotInRelationException {
		Set set = new HashSet();
		set.add(startObject);
		return _getAllInternObjects(startObject, set);
	} //getAllInternObjects

	private Set _getAllInternObjects(MathObject startObject, Set set)
			throws ObjectNotInRelationException {
		//		if (!isIntern(startObject, key))
		//			return set;
		set.add(startObject);
		RelationContainer rc = startObject.getRelationContainer();
		Set allNamedObjects = rc.getAllNamedRelationNames();
		if (allNamedObjects != null) {
			for (Iterator iter = allNamedObjects.iterator(); iter.hasNext();) {
				RelationInterface r = (RelationInterface) rc
						.getRelationByName((String) iter.next());
				if (!r.getName().equals(
						RelationFactory.getPart_Of_Relation().getName()))
					continue;
				_getAllInternObjects(r.getPartner(startObject), set);
			} //for iter
		} //if
		return set;
	} //getAllInternObjects

	/**
	 * Ersetzt im das Objekt oldObject mit newObject. Dabei werden auch interne
	 * Verzeigerungen neu gesetzt
	 * 
	 * Kann nicht substituiert werden, wird eine Exception an den
	 * ExceptionManger weiter gegeben und auch ausgelöst.
	 * 
	 * Gründe für eine nicht erfolgreiche substitution sind: testSubstitution
	 * schlägt fehl Die beiden Objekte sind nicht strukturgleich Die Stelligkeit
	 * der beiden Funktionen stimmt nicht überein
	 * 
	 * @param newObjectReal
	 * @param oldObjectReal
	 * @throws BrokerException
	 * @throws ObjectNotInRelationException
	 */
private void makeSubstitution(MathObject newObject, MathObject oldObject,
            String name) throws BrokerException, ObjectNotInRelationException {
        String key = (String) oldObject.getIdentifier();
        
        proveSubstitutionOperability(oldObject,newObject);
        
        MathObject newObjectReal = null, oldObjectReal = null;
        if (((UserFunction) newObject).isEncapsulated()) {
            try {
                newObjectReal = (MathObject) ((UserFunction) newObject).eval();
                oldObjectReal = (MathObject) ((UserFunction) oldObject).eval();
            } catch (MathException e) {
                ExceptionManager.doError(e);
               	throw new BrokerException(e);
            }            
        }else{
            newObjectReal = newObject;
            oldObjectReal = oldObject;
        }
        
        if(newObjectReal==oldObjectReal)
        	return;

        if (newObjectReal instanceof HasGraph) {
        	((HasGraph)newObjectReal).applyProperties((HasGraph) oldObjectReal);            
        }//if
        
//        if (newObjectReal instanceof HasGraph) {
//        	((HasGraph)newObjectReal).applyProperties((HasGraph) oldObjectReal);
//            ((HasGraph) newObjectReal).setColor(((HasGraph) oldObjectReal)
//                    .getColor());
//            ((HasGraph) newObjectReal).setLineStyle(((HasGraph) oldObjectReal)
//                    .getLineStyle());
//            ((HasGraph) newObjectReal).setVisible(((HasGraph) oldObjectReal)
//                    .isVisible());
//        }//if
//
//        if (newObjectReal instanceof AreaInterface) {
//            ((AreaInterface) newObjectReal)
//                    .setFillColor(((AreaInterface) oldObjectReal)
//                            .getFillColor());
//            ((AreaInterface) newObjectReal)
//                    .setBorderColor(((AreaInterface) oldObjectReal)
//                            .getBorderColor());
//        }//if
//
//        if (newObjectReal instanceof AffPoint) {
//            ((AffPoint) newObjectReal).setStyle(((AffPoint) oldObjectReal)
//                    .getStyle());
//        }//if

        
        Set internSet = getAllInternObjects(oldObjectReal);
        internSet.addAll(getAllInternObjects(oldObject));
        //Menge aller Beziehungen, die später gelöscht werden.
        Set trashSet = new HashSet();
        //Menge, in der alle Beziehungen vom alten Objekt vermerkt werden,
        //die danach beim neuen Objekt eingetragen werden.
        Set reConnectSet = new HashSet();

        Exception substitutionException;
        try {
            if(newObject !=newObjectReal){
                _makeSubstitution(newObjectReal, oldObjectReal, internSet, trashSet,
                        reConnectSet);    
            }
            _makeSubstitution(newObject, oldObject, internSet, trashSet,
                    reConnectSet);
            substitutionException = null;
        } catch (ObjectNotInRelationException e) {
            ExceptionManager.doError(e);
            substitutionException = e;
        } catch (IllegalRelationConnectionException e) {
            ExceptionManager.doError(e);
            substitutionException = e;
        }//catch

        //Löschen alter Beziehungen
        for (Iterator iter = trashSet.iterator(); iter.hasNext();) {
            RelationContainer.disconnect((RelationInterface) iter.next());
        } //for iter

        //Neue Beziehungen anhängen
        for (Iterator iter = reConnectSet.iterator(); iter.hasNext();) {
            RelationInterface ri = (RelationInterface) iter.next();
            MathObject[] moa = ri.getObjects();
            try {
                RelationContainer.connect(moa[0], moa[1], ri, FIRST_OBJECT,
                        this);
            } catch (IllegalRelationConnectionException e) {
                ExceptionManager.doError(ri + " konnte nicht geknüpft werden",
                        e);
                substitutionException = e;
            }//catch
        } //for iter

        //neues Objekt beim Broker veröffentlichen, falls vorhanden
        Object oldObjectKey = oldObject.getIdentifier();
        if (objectDic.get(oldObjectKey) != null){
            objectDic.put(oldObjectKey, newObject);
            newObject.setName(oldObjectKey.toString());
        }//if

        try {
            checkRecursionOfChilds(newObjectReal, newObjectReal, true, new LinkedList());
            if (substitutionException != null)
                throw new BrokerException(substitutionException);
        } catch (BrokerException e) {
            //Verzeigerung umkehren
            internSet = getAllInternObjects(newObjectReal);
            _makeSubstitution(oldObjectReal, newObjectReal, internSet, trashSet,
                    new HashSet());
            //Trash wiederherstellen
            for (Iterator iter = trashSet.iterator(); iter.hasNext();) {
                RelationInterface ri = (RelationInterface) iter.next();
                MathObject[] moa = ri.getObjects();
                try {
                    RelationContainer.connect(moa[0], moa[1], ri, FIRST_OBJECT,
                            this);
                } catch (IllegalRelationConnectionException e1) {
                    /* Könnte schon geknüft sein */
                    ExceptionManager.doError(e1);
                }
            } //for iter
            //Neuen Verknüpfungen werden wieder gelöscht
            for (Iterator iter = reConnectSet.iterator(); iter.hasNext();) {
                RelationContainer.disconnect((RelationInterface) iter.next());
            } //for iter

            //			deleteObject(newObject);
            objectDic.put(key, oldObject);
            throw new BrokerException(e);
        } //catch
    } //makeSubstiution

	/**
	 * Überprüfung, ob die Substitution zulässig ist. 
	 * 
	 * Es werden folgende Dinge überprüft:
	 * 
	 *    Gleichheit der Objekte
	 *    Bei UserFunction: Gleichheit des Rückgabetyps und ArgumentenListe
	 *    testSubstitution-Methode in einem MathObject
	 * 
	 * @param oldObject
	 * 		Ursprüngliches Objekt.
	 * @param newObject
	 * 		Zu substituierendes Objekt.1
	 * @throws BrokerException 
	 * 		Wird geworfen, falls keine Substitution durchgeführt werden kann.
	 */
	private void proveSubstitutionOperability(MathObject oldObject, MathObject newObject) throws BrokerException{
        if (oldObject instanceof HasGraph && ((HasGraph) oldObject).isFreeze()) {
            throw new BrokerException("Das Objekt mit dem Schlüssel "
                    + RelationContainer.tryToGetFullName(oldObject, "")
                    + " ist nicht modifizierbar.");
        }//if
        
        if (!newObject.testSubstitution(oldObject, new HashSet())) {
            if (oldObject instanceof StandardFunction) {
                throw new BrokerException(
                        "Das Objekt mit dem Schlüssel '"
                                + RelationContainer.tryToGetFullName(oldObject,
                                        "")
                                + "' ist eine vordefinierte Funktion.\nEine Redefinition ist nicht möglich.");
            }//if StandardFunction
            throw new BrokerException("Das Objekt mit dem Schlüssel "
                    + RelationContainer.tryToGetFullName(oldObject, "")
                    + " konnte nicht mit " + newObject.getIdentifier()
                    + " substituiert werden.");
        }//if           
      
        //Falls unterschiedlicher Typ
        if (!newObject.getClass().equals(oldObject.getClass())) {
        	String oldStr = oldObject.getClass().toString();
        	String newStr = newObject.getClass().toString();
        	if(oldObject instanceof HasTypeString){
        		oldStr = ((HasTypeString)oldObject).getTypeString()+" ("+oldStr+")";        	
        	}//if
        	if(newObject instanceof HasTypeString){
        		newStr = ((HasTypeString)newObject).getTypeString()+" ("+newStr+")";        	
        	}//if
        	
			throw new BrokerException(
					"Das Objekt mit dem Schlüssel "
							+ newObject.getIdentifier()
							+ " kann nicht substituiert werden. Das Objekt muß den gleichen Typen haben.\n\nZuvor: "
							+ oldStr + "\nDanach: "
							+ newStr
							+ "\n\nWenn Sie das alte Objekt löschen, dann können Sie eine neue Definition eintragen.");
		}//if        
        
        if (newObject instanceof UserFunction) {
			UserFunction oldFunc = (UserFunction) oldObject;
			UserFunction newFunc = (UserFunction) newObject;

			//Rückgabetyp überprüfen
			Class oldResult = oldFunc.getResultType();
			Class newResult = newFunc.getResultType();
			
			proveSubstitutionResultType(oldResult,newResult, newObject, "Das Objekt muß den gleichen Rückgabetypen haben.");
			
			//Parameter überprüfen
			Variable[] oldVars = oldFunc.getVariables();
			Variable[] newVars = newFunc.getVariables();
			
			if(oldVars.length != newVars.length){
				throw new BrokerException(
						"Das Objekt mit dem Schlüssel "
								+ newObject.getIdentifier()
								+ " kann nicht substituiert werden. Das Objekt muß genauso viele Argumente haben.\n\nZuvor: "
								+ oldFunc.getDefinitionHeader()
								+ "\nDanach: "
								+ newFunc.getDefinitionHeader()
								+ "\n\nWenn Sie das alte Objekt löschen, dann können Sie eine neue Definition eintragen.");
			}//if Länge der Parameterliste nicht gleich
			
			for (int i = 0; i < newVars.length; i++) {
				oldResult = oldVars[i].getResultType();
				newResult = newVars[i].getResultType();
				proveSubstitutionResultType(oldResult,newResult,newObject,"Der "+(i+1)+".te Parameter muß den gleichen Rückgabetypen haben.");
			}
		}//if UserFunction
          		
	}//proveSubstitutionOperability
	
	/**
	 * Überprüft, ob zwei Rückgabetypen gleich sind und
	 * wirft im Falle eines Fehlers eine Exception mit
	 * detailierter Beschreibung zum Problem.
	 * 
	 * @param oldResult alter Rückgabetyp
	 * @param newResult neuer Rückgabetyp
	 * @param newObject neues Objekt
	 * @param message Erweiterte Nachricht, die im Fehlerfall hinzugefügt wird
	 * @throws BrokerException wird geworfen, wenn die Rückgabetypen nicht kompatibel sind
	 */
	private void proveSubstitutionResultType(Class oldResult, Class newResult, MathObject newObject, String message) throws BrokerException{
		if (!oldResult.equals(newResult)) {
			String oldStr = oldResult.toString();
			String newStr = newResult.toString();

			try {
				Method m = oldResult.getMethod("getTypeString",
						new Class[0]);
				String temp = (String) m.invoke(null, new Object[0]);
				if ("".equals(temp))
					temp = "skalarer Typ";
				oldStr = temp + " (" + oldStr + ")";
			} catch (Exception e) {
			}

			try {
				Method m = newResult.getMethod("getTypeString",
						new Class[0]);
				String temp = (String) m.invoke(null, new Object[0]);
				if ("".equals(temp))
					temp = "skalarer Typ";
				newStr = temp + " (" + newStr + ")";
			} catch (Exception e) {
			}

			throw new BrokerException(
					"Das Objekt mit dem Schlüssel "
							+ newObject.getIdentifier()
							+ " kann nicht substituiert werden. "+message+"\n\nZuvor: "
							+ oldStr
							+ "\nDanach: "
							+ newStr
							+ "\n\nWenn Sie das alte Objekt löschen, dann können Sie eine neue Definition eintragen.");
		}//if Rückgabetyp falsch
	}//proveSubstitutionResultType

	private void _makeSubstitution(final MathObject newObject,
			final MathObject oldObject, final Set internSet,
			final Set trashSet, final Set reConnectSet)
			throws ObjectNotInRelationException,
			IllegalRelationConnectionException, BrokerException {

		RelationContainer rcOld = oldObject.getRelationContainer();
		RelationContainer rcNew = newObject.getRelationContainer();
		Iterator iterOld = rcOld.getAllNamedRelationNames().iterator();
		Iterator iterNew = rcNew.getAllNamedRelationNames().iterator();
		while (iterOld.hasNext()) {
			if (!iterNew.hasNext())
				throw new IllegalArgumentException("Das neue Object "
						+ RelationContainer.tryToGetFullName(oldObject, "")
						+ " hat mehr benannte Unterobjekte als "
						+ RelationContainer.tryToGetFullName(oldObject, ""));
			MathObject moOld = (MathObject) rcOld
					.getObjectByName((String) iterOld.next());
			MathObject moNew = (MathObject) rcNew
					.getObjectByName((String) iterNew.next());
			if (internSet.contains(moOld))
				_makeSingleSubstitution(moNew, moOld, internSet, trashSet,
						reConnectSet);
		}
		_makeSingleSubstitution(newObject, oldObject, internSet, trashSet,
				reConnectSet);
	} //_makesubstitiution

	private void _makeSingleSubstitution(final MathObject newObject,
			final MathObject oldObject, final Set internSet,
			final Set trashSet, final Set reConnectSet)
			throws ObjectNotInRelationException,
			IllegalRelationConnectionException, BrokerException {

		//temporäre LöschMenge
		Set deleteSet = new HashSet();

		Iterator iter = oldObject.getRelationContainer()
				.getAllRelationsIterator();
		while (iter.hasNext()) {
			RelationInterface ri = (RelationInterface) iter.next();
			//zu löschende Beziehung eintragen
			deleteSet.add(ri);
			//wenn die Beziehung nicht löschbar ist, wird sie übernommen
			//			if (ri.getRoleFromObject(oldObject).isDeleteable() !=
			// Role.DELETE_NO)
			//				continue;
			//			if(ri.getName().equals(RelationFactory.getPart_Of_Relation().getName()))
			//				continue;

			//wenn es eine Funktion-Unterfuntion-Beziehung ist
			//und das aktuelle Objekt die Funktion ist, dann
			//wird diese Beziehung nicht reconnected.
			if (ri.getName().equals(
					RelationFactory.getFunction_SubFunction_Relation()
							.getName())
					&& (ri.getPositionFromObject(oldObject) == RelationInterface.FIRST))
				continue;
			if (internSet.contains(ri.getPartner(oldObject)))
				continue;

			int pos2 = ri.getPositionFromObject(oldObject);
			//Kopie der Beziehung und die Objekte + Position werden "gemerkt"
			RelationInterface ri2 = ri.cloneRelation();
			if (pos2 == Relation.FIRST) {
				ri2.setObjects(newObject, ri.getPartner(oldObject), this);
			} else {
				ri2.setObjects(ri.getPartner(oldObject), newObject, this);
			} //else
			if (ri.getCreationPath() != null)
				ri2.setCreationPath(ri.getCreationPath().getPath());
			//zu knüpfende Beziehung eintragen
			reConnectSet.add(ri2);
			//neu verlinken
			MathObject partner = ri.getPartner(oldObject);
			if (internSet.contains(partner))
				continue;
			try {
				partner.swapLinks(oldObject, newObject);
			} catch (Exception e) {
				throw new BrokerException(e);
			} //catch
		} //while
		trashSet.addAll(deleteSet);
	} //_makeSubstitiution

	/**
	 * Überprüft, es bei einer Evaluierung, zu einer Endlosschleife kommen
	 * würde.
	 * 
	 * @param parentObj
	 *            Anfangsobjekt, nur dieses Objekt kann eine Rekursion erzeugen,
	 *            da nur dieses Objekt eine neue Definition besitzt.
	 * @param objToCheck
	 *            aktuelles Objekt, welches überprüft werden soll.
	 * @param firstRun
	 *            Beim ersten Lauf, wird das Objekt nicht auf Rekursion geprüft
	 * @param checkedObjs
	 *            Liste aller aktuell geprüften Objekte. Dient der
	 *            Fehlerausgabe.
	 * @throws BrokerException
	 * @throws ObjectNotInRelationException
	 */
	private void checkRecursionOfChilds(MathObject parentObj,
			MathObject objToCheck, boolean firstRun, List checkedObjs)
			throws BrokerException, ObjectNotInRelationException {
		if (!firstRun && (parentObj == objToCheck))
			throw new BrokerException("Rekursion von " + parentObj.getIdentifier()
					+ " entdeckt in den Definitionen " + checkedObjs);
		checkedObjs.add(objToCheck);

		//Kinder überprüfen
		Iterator iter = objToCheck.getRelationContainer()
				.getSpecificRelationsIterator(
						RelationFactory.getFunction_SubFunction_Relation()
								.getName());
		while (iter.hasNext()) {
			RelationInterface ri = (RelationInterface) iter.next();
			if (ri.getPositionFromObject(objToCheck) == RelationInterface.SECOND)
				continue;
			if (ri.getPartner(objToCheck) instanceof StandardFunction)
				continue;
			checkRecursionOfChilds(parentObj, ri.getPartner(objToCheck), false,
					checkedObjs);
		} //while
	} //checkRecursionOfChilds

} //class Broker
