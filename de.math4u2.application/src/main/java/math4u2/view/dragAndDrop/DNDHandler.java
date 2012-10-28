package math4u2.view.dragAndDrop;

import java.awt.Component;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.InvalidDnDOperationException;

import math4u2.controller.Broker;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.dragAndDrop.flavour.MathObjectTransferable;

/**
 * Drag-Button Klasse
 */
public class DNDHandler extends DragAdapter {

	/** DragSource: enables this component to be a Drag Source */
	private DragSource dragSource = null;

	/** Globaler DragSourceContext */
	public static DragSourceContext dsc = null;

	/** MathObject-Schlüssel for the Drop-Target */
	private String key;

	/** Referenz zum Broker */
	private Broker broker;

	/**
	 * Konstruktor
	 * 
	 * @param c
	 *            Drag-Komponente
	 * @param key
	 *            Schlüssel des Drag-Objekts (Typ MathObject)
	 * @param broker
	 */
	public DNDHandler(Component c, String key, Broker broker) {
		this.broker = broker;
		this.key = key;
		init(c);
	} //Konstruktor 1

	/**
	 * Initialsierung der Drag-Komponente
	 * 
	 * @param c
	 *            Sichtbare Komponente, die mit dem dragSource verbunden werden
	 *            soll
	 */
	private void init(Component c) {
		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(c,
				DnDConstants.ACTION_MOVE, this);
	} //init

	/**
	 * Drag-Geste wurde erkannt
	 */
	public void dragGestureRecognized(DragGestureEvent event) {
		MathObjectTransferable transfer = new MathObjectTransferable(key,
				broker);
		try {
			dragSource.startDrag(event, DragSource.DefaultMoveDrop, transfer,
					this);
		} catch (InvalidDnDOperationException e) {
			ExceptionManager.doError("Ungültige Drag&Drop-Aktion ",e);
		}
	} //dragGestureRecognized

	/**
	 * Diese Nachricht geht an den DragSourceListener, der informiert wird, dass
	 * die Drop-Aktion beginnt.
	 */
	public void dragEnter(DragSourceDragEvent event) {
		dsc = event.getDragSourceContext();
	} //dragEnter

	/**
	 * Drag-Aktion zu Ende.
	 */
	public void dragDropEnd(DragSourceDropEvent event) {
		dsc = null;
	} //dragDropEnd

} //class DNDButton
