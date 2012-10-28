// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import math4u2.exercises.ui.ESkin;
import math4u2.exercises.ui.Math4u2TextPane;
import math4u2.exercises.ui.Skinnable;
import math4u2.exercises.ui.StyledText;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.parser.ParseException;

import org.w3c.dom.Node;

/**
 * Klasse, die Multiple-Choice-Fragen repräsentiert.
 * 
 * @author Erich Seifert
 * @version 0.1
 */
public class EMultipleChoice extends EContainer implements ItemListener {
	private boolean mutual; // Kann nur immer ein Objekt angewählt werden?

	private Node descriptionNode;

	private EParser parser;

	/**
	 * Konstruktor, der ein neues EMultipleChoice-Objekt erzeugt.
	 * 
	 * @param description
	 *            Beschriebungstext bzw. Aufgabensstellung der Frage
	 * @param items
	 *            Liste mit initialen Elenmenten
	 * @param mutual
	 *            Option, die festlegt, ob nur eine Frage gleichzeitig
	 *            ausgewählt werden kann
	 */
	public EMultipleChoice(Node description, List items, boolean mutual,
			EParser parser) {
		super(null, items);
		descriptionNode = description;
		this.parser = parser;

		//Jedem Item sagen, dass this Listener ist
		Iterator i = items.iterator();
		while (i.hasNext()) {
			((EChoice) i.next()).addItemListener(this);
		}//while

		this.mutual = mutual;
		createView();
	}//Konstruktor

	/**
	 * Fügt eine neue Auswahlmöglichkeit hinzu.
	 * 
	 * @param item
	 *            EChoice-Objekt, das eine Auswahlmöglichkeit repräsentiert
	 */
	public void add(EChoice item) {
		super.add(item);
		item.addItemListener(this);
	}

	/**
	 * Gibt den aktuellen Auswahl-Zustand zurück (Einzel-/Mehrfachauswahl).
	 * 
	 * @return Den aktuellen zustand der Auswahl: <code>true</code> falls
	 *         immer nur ein Element gleichzeitig gewählt werden kann,
	 *         <code>false</code> wenn mehrere ausgewählt werden können.
	 */
	public boolean getMutual() {
		return mutual;
	}

	/**
	 * Legt fest, ob immer nur ein Element gleichzeitig ausgewählt werden kann (
	 * <code>true</code>) oder mehrere gleichzeitig (<code>false</code>).
	 * 
	 * @param mutual
	 *            Flag zum Festlegen des Zustands (Einzel-/Mehrfachauswahl)
	 */
	public void setMutual(boolean mutual) {
		this.mutual = mutual;
	}

	/**
	 * Legt einen neuen Beschreibungstext fest.
	 * 
	 * @param description
	 *            Neuer Beschreibungstext
	 */
	public void setDescription(StyledText description) {
		super.setDescription(description);
		buildDescr();
	}

	/**
	 * Gibt den Beschreibungstext zurück.
	 *  
	 */
	public StyledText getDescription() {
		try {
			if(parser==null) return null;
			return parser.parseText(descriptionNode);
		} catch (ParseException e) {
			ExceptionManager.doError("Fehler beim Lesen vom Tag <description>",e, descriptionNode);
			return null;
		}
	}//getDescription

	/**
	 * Zeigt dem Benutzer alle falsch beantworteten Aussagen an.
	 */
	public void showFailures() {
		if (skin != null) {
			if (getValid() == false)
				skin.setScheme(ESkin.FAILURE_SCHEME);
			else
				skin.setScheme(ESkin.CORRECT_SCHEME);
		}
	}

	/**
	 * Zeigt dem Benutzer die Lösung an.
	 */
	public void showSolution() {
		Iterator iter = iterator();
		while (iter.hasNext())
			((Solvable) iter.next()).showSolution();

		if ((skin != null) && (getValid() == true))
			skin.setScheme(ESkin.NORMAL_SCHEME);
	}

	/**
	 * Legt ein neues Erscheinungsbild (Skin) fest.
	 * 
	 * @param skin
	 *            Skin-Objekt, das das Erscheinungsbild bestimmt
	 */
	public void setSkin(ESkin skin) {
		this.skin = skin;

		skin.plug(getView());

		// Allen Unterelementen das selbe Erscheinungsbild zuweisen
		Iterator iter = iterator();
		while (iter.hasNext())
			((Skinnable) iter.next()).setSkin(skin);
	}

	/**
	 * Erzeugt die Darstellung des Beschreibungstextes.
	 */
	private void buildDescr() {
		if (view != null) {
			Math4u2TextPane tpView = (Math4u2TextPane) view; // view als
															 // Textbereich
															 // ansprechbar
															 // machen
			StyledText descr = getDescription(); // Hilfsvariable für
												 // Beschreibungstext

			if (descr != null) {
				tpView.setVisible(true);

				// Beschreibung in den Textbereich einfügen
				Document doc = tpView.getDocument();
				try {
					descr.insertText(doc);
				} catch (BadLocationException ble) {
					ExceptionManager.doError("Couldn't insert initial description text.",ble);
				}
			} else // descr==null
			{
				tpView.setVisible(false);
			}//else
		}//if view!=null
	}//buildDescr

	/**
	 * Erzeugt die Darstellung und speichert diese intern.
	 */
	protected void createView() {
		Math4u2TextPane tpView = new Math4u2TextPane(); // neuen Textbereich
														// erzeugen
		tpView.setOpaque(false); // Ansicht ist durchsichtig (nicht opak)
		tpView.setEditable(false); // Ansicht/Textbereich darf nicht bearbeitet
								   // werden.
		view = tpView;

		buildDescr();
	}

	/**
	 * Wird aufgerufen, wenn ein Element sich verändert.
	 * 
	 * @param e
	 *            ItemEvent der bei der Veränderung übergeben wird
	 */
	public void itemStateChanged(ItemEvent e) {
		if (mutual) // nur wenn nur ein Element ausgewählt werden soll
		{
			EChoice eItem = (EChoice) e.getItem();
			int state = e.getStateChange();

			if (state == ItemEvent.SELECTED) {
				Iterator iter = iterator();
				while (iter.hasNext()) {
					EChoice item = (EChoice) iter.next();
					if (item != eItem)
						item.setSelected(false);
				}//while
			}//if SELECTED
		}//if mutual
	}//itemStateChanged
}//EMultipleChoice
