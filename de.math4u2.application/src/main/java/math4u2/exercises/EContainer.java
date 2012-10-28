//Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import math4u2.exercises.ui.StyledText;

/**
 * Abstrakte Basisklasse f�r alle Beh�lter.
 * 
 * @version 0.2
 * @author Erich Seifert
 */
public abstract class EContainer extends EElement {
	protected final List items = new LinkedList();

	/**
	 * Konstruktor, der ein neues EContainer-Objekt erzeugt.
	 * 
	 * @param description
	 *            Die Beschreibung
	 * @param items
	 *            Liste mit initialen Elementen
	 */
	public EContainer(StyledText description, List items) {
		super(description);

		if (items != null)
			this.items.addAll(items);
	}

	/**
	 * Konstruktor, der ein neues EContainer-Objekt erzeugt.
	 * 
	 * @param description
	 *            Die Beschreibung
	 */
	public EContainer(StyledText description) {
		this(description, null);
	}

	/**
	 * Gibt den aktuellen Korrektheitszustand zur�ck.
	 * 
	 * @return Den aktuellen Korrektheitszustand: korrekt/inkorrekt
	 */
	public final boolean getValid() {
		boolean valid = true;

		Iterator iter = items.iterator();
		while (iter.hasNext())
			valid &= ((EItem) iter.next()).getValid();

		return valid;
	}

	/**
	 * F�gt dem Beh�lter ein neues EItem-Element hinzu.
	 * 
	 * @param item
	 *            Das EItem-Objekt das hinzugef�gt werden soll
	 */
	public final void add(EItem item) {
		items.add(item);
	}

	/**
	 * Gibt einen Iterator zum Durchlaufen der gespeicherten Elemente zur�ck.
	 */
	public final Iterator iterator() {
		return items.iterator();
	}
}