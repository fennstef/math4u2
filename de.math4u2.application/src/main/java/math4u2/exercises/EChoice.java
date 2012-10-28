// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises;

import java.awt.Insets;
import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import math4u2.exercises.ui.ESkin;
import math4u2.exercises.ui.Math4u2TextPane;
import math4u2.exercises.ui.ResizableCheckBox;
import math4u2.exercises.ui.StyledText;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.parser.ParseException;

import org.w3c.dom.Node;

/**
 * Klasse die eine einfache Ja-/Nein-Frage repr�sentiert.
 * 
 * @version 0.1
 * @author Erich Seifert
 */
public class EChoice extends EItem implements ItemSelectable {
	private boolean solution; // Die L�sung

	private EParser parser;

	private Node descriptionNode;

	private Node explanationNode;

	/**
	 * Eine Checkbox zum Ankreuzen.
	 */
	protected final ResizableCheckBox answer = ESkin.getCheckBox();

	/**
	 * Registrierte Komponenten, die darauf warten, dass sich etwas �ndert.
	 */
	protected final Set itemListeners = new HashSet();

	/**
	 * Erzeugt ein neues EChoice-Objekt.
	 * 
	 * @param descriptionNode
	 *            Die dargestellte Frage- bzw. Antworttext (Knoten)
	 * @param solution
	 *            Die richtige Antwort auf die Frage
	 * @param explanation
	 *            Eine optionale Erkl�rung
	 */
	public EChoice(Node descriptionNode, boolean solution,
			Node explanationNode, EParser parser) {
		super(null, null); // geerbte Werte initialisieren
		setSolution(solution);
		this.descriptionNode = descriptionNode;
		this.explanationNode = explanationNode;
		this.parser = parser;

		// Schaltfl�che initialisieren
		answer.setMargin(new Insets(0, 0, 0, 0));
		answer.setSelected(false); // Am Anfang nicht angew�hlt
		answer.addItemListener(new ItemListener() // Wenn Schaltfl�che angew�hlt
												  // wird, ...
				{
					public void itemStateChanged(ItemEvent e) {
						notifyItemListeners(e); // Listener benachrichtigen
						setValid(answer.isSelected() == getSolution()); // Entspricht
																		// der
																		// angew�hlte
																		// Wert
																		// der
																		// L�sung?
					}
				});
		//		createView();
	}

	/**
	 * Erzeugt ein neues EChoice-Objekt.
	 * 
	 * @param descriptionNode
	 *            Die dargestellte Frage- bzw. Antworttext (Knoten)
	 * @param solution
	 *            Die richtige Antwort auf die Frage
	 */
	public EChoice(Node descriptionNode, boolean solution, EParser parser) {
		this(descriptionNode, solution, null, parser);
	}

	/**
	 * Gibt den aktuellen Wert der L�sung zur�ck.
	 * 
	 * @return Aktuelle L�sung
	 */
	public final boolean getSolution() {
		return solution;
	}

	/**
	 * Setzt den Wert der L�sung.
	 * 
	 * @param solution
	 *            Der Wert der L�sung; kann <code>true</code> oder
	 *            <code>false</code> sein
	 */
	public final void setSolution(boolean solution) {
		this.solution = solution; // L�sung setzen
		setValid(!getSolution()); // Wenn die L�sung "falsch" ist wird valid auf
								  // "wahr" gesetzt
	}

	/**
	 * Zeigt dem Benutzer alle Fehler an.
	 */
	public final void showFailures() {
		if (skin != null) // Wenn ein Erscheinungsbild (Skin) gesetzt wurde, ...
		{
			if (getValid() == false) // Skin je nach Wert von valid �ndern
				skin.setScheme(ESkin.FAILURE_SCHEME);
			else
				skin.setScheme(ESkin.CORRECT_SCHEME);
		}
	}

	/**
	 * Zeigt dem Benutzer die L�sung an.
	 */
	public final void showSolution() {
		answer.setSelected(solution); // Schaltfl�che auf L�sung setzen
		if ((skin != null) && (getValid() == true))
			skin.setScheme(ESkin.NORMAL_SCHEME);
	}

	/**
	 * Erzeugt die Darstellung und speichert diese intern.
	 */
	protected final void createView() {
		view = new Math4u2TextPane(); // Neuer Textbereich

		// Aufgabenbeschreibung einf�gen
		StyledDocument doc = (StyledDocument) ((Math4u2TextPane) view)
				.getDocument();
		try {
			parser.parseText(descriptionNode).insertText(doc);
		} catch (BadLocationException ble){
			 // Falsche Position im Text
			ExceptionManager.doError("Starttext konnte nicht gesetzt werden.");
		} catch (ParseException e) {
			ExceptionManager.doError("Fehler beim Erzeugen der aktuellen Sicht",e, descriptionNode);
		}

		view.setOpaque(false); // Texthintergrund druchsichtig machen (Opazit�t
		// = Deckkraft/Undurchsichtigkeit)
		((Math4u2TextPane) view).setEditable(false); // Der Text darf vom
		// Benutzer nicht
		// bearbeitet werden.
	}//createView

	/**
	 * Setzt das Erscheinungsbild (Skin) des Objekts.
	 * 
	 * @param skin
	 *            Skin-Objekt, das das Erscheinungsbild repr�sentiert
	 */
	public final void setSkin(ESkin skin) {
		this.skin = skin;

		skin.setExplanation(getExplanation()); // Beschreibungstext setzen
		skin.plug(this.getView(), answer); // Aktuelles Objekt mit dem Skin
										   // verbinden
	}//setSkin

	/**
	 * F�gt einen ItemListener hinzu, der bei Ver�nderungen am Objekt
	 * benachrichtigt wird.
	 * 
	 * @param l
	 *            Ein ItemListener, der bei Zustands�nderungen benachrichtigt
	 *            wird
	 */
	public final void addItemListener(ItemListener l) {
		itemListeners.add(l);
	}//addItemListener

	/**
	 * Entfernt einen ItemListener. Dieser wird danach nicht mehr �ber
	 * Zustands�nderungen des Objekts informiert.
	 * 
	 * @param l
	 *            Der ItemListener, der entfernt werden soll
	 */
	public final void removeItemListener(ItemListener l) {
		itemListeners.remove(l);
	}//removeItemListener

	/**
	 * Gibt alle ausgew�hlten Objekte zur�ck. Implementation f�r das
	 * ItemSelectable-Interface
	 * 
	 * @return Ein Array mit allen zur Zeit angew�hlten Elementen
	 */
	public Object[] getSelectedObjects() {
		Object[] ret = null;
		if (answer.isSelected()) {
			ret = new Object[1];
			ret[0] = this;
		}
		return ret;
	}//getSelectedObjects

	/**
	 * Benachrichtigt alle Registrierten ItemListener, dass eine
	 * Zustands�nderung eingetreten ist.
	 * 
	 * @param e
	 *            ItemEvent-Objekt, das an alle Listener geschickt werden soll
	 */
	protected final void notifyItemListeners(ItemEvent e) {
		ItemEvent itemEvent = new ItemEvent(this, e.getID(), this, e
				.getStateChange());
		Iterator iter = itemListeners.iterator();
		while (iter.hasNext())
			((ItemListener) iter.next()).itemStateChanged(itemEvent);
	}//notifyItemListeners

	/**
	 * Legt fest, ob das Objekt angew�hlt sein soll oder nicht.
	 * 
	 * @param selcted
	 *            Neuer Zustand, den das Objekt annehmen soll
	 */
	public final void setSelected(boolean selected) {
		answer.setSelected(selected);
	}//setSelected

	public JComponent getView() {
		createView();
		return view;
	}//getView

	public StyledText getExplanation() {
		try {
			return parser.parseText(explanationNode);
		} catch (ParseException e) {
			ExceptionManager.doError("Fehler beim Parsen der Erkl�rung",e,explanationNode);
			return null;
		}//catch
		catch (NullPointerException e) {
			return null;
		} //Wenn die Initialisierung noch nicht fertig war

	}//getExplanation

	public StyledText getDescription() {
		try {
			return parser.parseText(descriptionNode);
		} catch (ParseException e) {
			ExceptionManager.doError("Fehler beim Parsen der Beschreibung",e,descriptionNode);
			return null;
		}//catch
	}//getDescription
}//class EChoice
