package math4u2.view.graph.drawarea;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.exercises.scripting.ModuleRegistry;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.dragAndDrop.target.DropTargetOnDrawAreaListener;

/**
 * Verwaltet Zeichenflächen
 * 
 * @author Fenn Stefan
 */
public class DrawAreasManager {

	private static boolean locked = false;

	/** Holt eine Zeichenfläche */
	public static DrawAreaInterface get(Broker broker, String name) {
		if (locked)
			return null;
		DrawAreaInterface da = (DrawAreaInterface) ModuleRegistry.getInstance()
				.lookup(name);

		if (da == null) {			
			da = new ExtendedDrawArea(name, broker);
			final DrawAreaInterface daRef = da;
//			da.addChangeListener(new DrawAreaChangeListener() {
//				public void drawAreaChanged() {
//					daRef.graphHasChanged();
//				}
//			});
			new DropTargetOnDrawAreaListener(da);
		} 

		try {
			if (broker.getObject(name) == null && da instanceof ExtendedDrawArea) {
				((ExtendedDrawArea)da).register();
			} 
		} catch (BrokerException e) {
			ExceptionManager.doError(
					"Fehler beim Erzeugen einer neuen Zeichenfläche", e);
		} 

		ModuleRegistry.getInstance().bind(name, da);
		return da;
	} 

	/**
	 * Entfernt die Zeichenflächen aus der ModuleFactory und dem Broker
	 */
	public static void unregisterAllDrawAreas() {
		if (locked)
			return;

		for (Iterator iter = getAllDrawAreas().iterator(); iter.hasNext();) {
			Object o = iter.next();
			if (o instanceof DrawAreaInterface) {
				unregisterDrawArea((DrawAreaInterface) o);
			}
		} 
	}

	/**
	 * Holt alle Zeichenflächen aus der ModuleFactory
	 */
	public static List getAllDrawAreas() {
		LinkedList dais = new LinkedList();
		Iterator iter = ModuleRegistry.getInstance().nameIterator();
		while (iter.hasNext()) {
			Object obj = ModuleRegistry.getInstance().lookup(
					(String) iter.next());
			if (obj instanceof DrawAreaInterface)
				dais.add(obj);
		} // for iter
		return dais;
	} 

	public static void unregisterDrawArea(DrawAreaInterface da) {
		if (locked)
			return;
		if (da == null)
			return;
		if (da instanceof ExtendedDrawArea) {
			((ExtendedDrawArea) da).unregisterParameter();
			ModuleRegistry.getInstance().unbind(da.getName());
		}
	}

	/**
	 * Ist der DrawAreasManager aktiv?
	 */
	public static boolean isLocked() {
		return locked;
	}

	/**
	 * Wenn auf true gesetzt wird, dann wird der DrawAreasManager nicht aktiv
	 */
	public static void setLocked(boolean b) {
		locked = b;
	}

} 
