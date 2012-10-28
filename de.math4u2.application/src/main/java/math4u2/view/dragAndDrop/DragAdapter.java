package math4u2.view.dragAndDrop;

import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;

/**
 * Adapterklasse für DragGestureListener und DragSourceListener
 * 
 * @author Fenn Stefan
 */
public class DragAdapter implements DragSourceListener, DragGestureListener {

	/**
	 * @see java.awt.dnd.DragSourceListener#dragEnter(java.awt.dnd.DragSourceDragEvent)
	 */
	public void dragEnter(DragSourceDragEvent dsde) {
	}

	/**
	 * Diese Nachtright geht an den DragSourceListener, der informiert wird,
	 * dass die Drop-Aktion beendet ist.
	 * 
	 * @see java.awt.dnd.DragSourceListener#dragOver(java.awt.dnd.DragSourceDragEvent)
	 */
	public void dragOver(DragSourceDragEvent dsde) {
	}

	/**
	 * Diese Nachtright geht an den DragSourceListener, der informiert wird,
	 * dass die Drop-Aktion beendet ist.
	 * 
	 * @see java.awt.dnd.DragSourceListener#dropActionChanged(java.awt.dnd.DragSourceDragEvent)
	 */
	public void dropActionChanged(DragSourceDragEvent dsde) {
	}

	/**
	 * Diese Nachtright geht an den DragSourceListener, der informiert wird,
	 * dass die Drop-Aktion beendet ist.
	 * 
	 * @see java.awt.dnd.DragSourceListener#dragExit(java.awt.dnd.DragSourceEvent)
	 */
	public void dragExit(DragSourceEvent dse) {
	}

	/**
	 * @see java.awt.dnd.DragSourceListener#dragDropEnd(java.awt.dnd.DragSourceDropEvent)
	 */
	public void dragDropEnd(DragSourceDropEvent dsde) {
	}

	/**
	 * @see java.awt.dnd.DragGestureListener#dragGestureRecognized(java.awt.dnd.DragGestureEvent)
	 */
	public void dragGestureRecognized(DragGestureEvent dge) {
	}

}