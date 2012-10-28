// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import math4u2.controller.Broker;
import math4u2.exercises.ui.EDocument;
import math4u2.exercises.ui.ESkin;
import math4u2.exercises.ui.ScrollablePanel;
import math4u2.exercises.ui.StyledText;
import math4u2.view.layout.PercentLayout;

import org.w3c.dom.Node;

/**
 * Klasse für alle Übungsthemen.
 * 
 * @version 0.1
 * @author Erich Seifert
 */
public class Exercise extends EDocument implements Solvable {
	private EIssue parent;

	private final List elements = new LinkedList(); // Alle Übungsfragen/-elemente

	private GridBagLayout mgbl;

	private GridBagConstraints mgbc;

	private JPanel elemContainer = new JPanel();

	private GridBagLayout egbl;

	private GridBagConstraints egbc;

	/**
	 * Konstruktor der ein neues Exercise-Objekt erzeugt.
	 * 
	 * @param title
	 *            Titel bzw. Überschrift der Übung
	 * @param description
	 *            Beschreibungstext der Übung
	 * @param summary
	 *            Zusammenfassung/Erklärung am Schluss der Übung
	 * @param elements
	 *            Liste mit initialen Übungselementen
	 * @param layout
	 *            geparstes Layout für die Zeichenflächen
	 * @param drawAreaNames
	 *            Namen der Zeichenflächen
	 * @param parser
	 *            Referenz auf den Parser
	 */
	public Exercise(StyledText title, Node descriptionNode, StyledText summary,
			List elements, PercentLayout layout, String[] drawAreaNames,
			Broker broker, EParser parser) {
		super(title, descriptionNode, summary, layout, drawAreaNames, broker,
				parser);
		if (elements != null)
			this.elements.addAll(elements);

		init();
		//		createView();
	}//Konstruktor

	/**
	 * Dieser Konstruktor wird für die Leerimplementierung des Baumes genutzt
	 */
	public Exercise(StyledText title, StyledText title2, EParser parser) {
		super(title, null, null, null, null, null, parser);
	}//Konstrukotr 2

	public void init() {
		elemContainer = new JPanel();
		egbl = new GridBagLayout();
		elemContainer.setLayout(egbl);
		elemContainer.setOpaque(false);

		egbc = new GridBagConstraints();
		egbc.insets = new Insets(5, 0, 10, 0);
		egbc.anchor = GridBagConstraints.WEST;
		egbc.fill = GridBagConstraints.HORIZONTAL;
		egbc.gridwidth = GridBagConstraints.REMAINDER;
		egbc.weightx = 1.0;

	}//init

	public void setParent(EIssue parent) {
		this.parent = parent;
	}//setParent

	public EIssue getParent() {
		return parent;
	}

	/**
	 * Fügt ein neues Übungselement hinzu.
	 * 
	 * @param item
	 *            Übung die hinzugefügt werden soll
	 */
	public void addElement(EElement item) {
		if (item != null) {
			elements.add(item);

			item.setSkin(new ESkin());
			JComponent childView = item.getSkinnedView();
			egbl.setConstraints(childView, egbc);
			elemContainer.add(childView, egbc);
		}
	}

	/**
	 * Gibt einen Iterator zum Durchlaufen der gespeicherten Elemente zurück.
	 * 
	 * @return Iterator zum Ansprechen der Elemente
	 */
	public Iterator getElementIterator() {
		return elements.iterator();
	}

	/**
	 * Zeigt dem Benutzer alle Fehler im Übungsthema an.
	 */
	public void showFailures() {
		Iterator iter = elements.iterator();
		while (iter.hasNext()) {
			((Solvable) iter.next()).showFailures();
		}
	}

	/**
	 * Zeigt dem Benutzer die Lösungen aller Aufgaben an.
	 */
	public void showSolution() {
		Iterator iter = elements.iterator();
		while (iter.hasNext()) {
			((Solvable) iter.next()).showSolution();
		}
	}

	/**
	 * Erzeugt die Darstellung und speichert diese intern.
	 */
	protected void createView() {
		mgbl = new GridBagLayout();
		view = new ScrollablePanel(); // Rollbarer Behälter
		view.setLayout(mgbl);

		head.setEditable(false); // Kopftext ist nicht vom Benutzer bearbeitbar
		head.setOpaque(false); // Kopftext ist durchsichtig (nicht opak)
		foot.setEditable(false); // Fußext ist nicht vom Benutzer bearbeitbar
		foot.setOpaque(false); // Fußtext ist durchsichtig (nicht opak)

		// Kopftext der Ansicht hinzufügen
		mgbc = new GridBagConstraints();
		mgbc.insets = new Insets(10, 20, 10, 20);
		mgbc.anchor = GridBagConstraints.WEST;
		mgbc.fill = GridBagConstraints.HORIZONTAL;
		mgbc.gridwidth = GridBagConstraints.REMAINDER;
		mgbc.weightx = 1.0;
		mgbl.setConstraints(head, mgbc);
		view.add(head, mgbc);

		// Alle Übungselemente der Ansicht hinzufügen
		mgbc.insets = new Insets(5, 15, 10, 15);
		mgbl.setConstraints(elemContainer, mgbc);
		view.add(elemContainer, mgbc);

		Iterator iter = elements.iterator();
		while (iter.hasNext()) {
			EElement item = (EElement) iter.next();

			item.setSkin(new ESkin());
			JComponent childView = item.getSkinnedView();
			egbl.setConstraints(childView, egbc);
			elemContainer.add(childView, egbc);
		}

		// Fußtext der Ansicht hinzufügen
		mgbc.insets = new Insets(0, 20, 10, 20);
		mgbl.setConstraints(foot, mgbc);
		view.add(foot, mgbc);
	}//createView

	public JComponent getView() {
		super.getView();
		init();
		createView();
		return view;
	}

	public boolean equals(Object o) {
		if (!(o instanceof Exercise))
			throw new NullPointerException();
		return getTitle().toString().equals(
				((Exercise) o).getTitle().toString());
	}//equals

}//class Exercise
