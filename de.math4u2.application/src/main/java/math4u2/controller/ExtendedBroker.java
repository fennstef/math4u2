package math4u2.controller;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import math4u2.controller.relation.ObjectNotInRelationException;
import math4u2.controller.relation.RelationContainer;
import math4u2.controller.relation.RelationInterface;
import math4u2.mathematics.affine.MousePosition;
import math4u2.mathematics.functions.StandardFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.gui.listview.ListBox;
import math4u2.view.gui.listview.complete.CompleteViewBox;

public class ExtendedBroker extends Broker{
	/**
	 * Löscht alle <code>MathObject</code> -Objekte aus dem Dictionary.
	 * 
	 * Dabei wird nicht auf die Beziehungen untereinander geachtet.
	 *  
	 */
	public void deleteAllObjects() {
		Set trash = new HashSet();
		for (Iterator iter = objectDic.values().iterator(); iter.hasNext();) {
			MathObject element = (MathObject) iter.next();

			if (element instanceof StandardFunction
					|| element instanceof ListBox
					|| element instanceof CompleteViewBox
					|| element.getIdentifier().equals(MousePosition.NAME)) {
				RelationContainer rc = element.getRelationContainer();
				LinkedList trashRel = new LinkedList();
				//Beziehungen sammeln
				for (Iterator iter2 = rc.getAllRelationsIterator(); iter2
						.hasNext();) {
					RelationInterface ri = (RelationInterface) iter2.next();
					trashRel.add(ri);
					try {
						trash.add(ri.getPartner(element));
					} catch (ObjectNotInRelationException e) {
						ExceptionManager.doError("Fehler beim Löschen aller Objekte",e);
					}
				}//for

				//Gesammelte Beziehungen löschen
				for (Iterator iter2 = trashRel.iterator(); iter2.hasNext();) {
					RelationInterface ri = (RelationInterface) iter2.next();
					try {
						RelationContainer.disconnect(ri);
					} catch (ObjectNotInRelationException e) {
						ExceptionManager.doError("Fehler beim Löschen aller Objekte",e);
					}//catch
				}//for
				continue;
			}//if

			trash.add(element);
		}//for iter

		try {
			deleteProvedObjectSet(trash);
		} catch (ObjectNotInRelationException e) {
			ExceptionManager.doError("Fehler beim Löschen aller Objekte",e);
		}
	}
}
