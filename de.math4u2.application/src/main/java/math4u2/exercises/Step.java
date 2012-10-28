// Copyright (c) 2002 Fachhochschule Augsburg
package math4u2.exercises;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.exercises.ui.EDocument;
import math4u2.exercises.ui.Math4u2TextPane;
import math4u2.exercises.ui.ScrollablePanel;
import math4u2.exercises.ui.StyledText;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.formula.interactive.StepInteractive;
import math4u2.view.layout.PercentLayout;

import org.w3c.dom.Node;

/**
 * Klasse für einen Schritt pro Thema
 * 
 * @version 0.1
 * @author Erich Seifert
 */
public class Step extends EDocument implements Comparable {

	private EIssue parentIssue;

	private List parameterBoxesToDeleteAfterUse;

	private static List tempList = new LinkedList();

	/**
	 * Konstruktor, der ein neues Step-Objekt erzeugt.
	 * 
	 * @param title
	 *            Ein Titel, der als Überschrift dargestellt wird
	 * @param descriptionNode
	 *            Knoten einer Beschreibung des Themas
	 * @param summary
	 *            Eine Zusammenfassung
	 * @param layout
	 *            geparstes Layout für die Zeichenflächen
	 * @param drawAreaNames
	 *            Namen der Zeichenflächen
	 * @param parser
	 *            Referenz auf den Parser
	 */
	public Step(StyledText title, Node descriptionNode, StyledText summary,
			PercentLayout layout, String[] drawAreaNames, Broker broker,
			EParser parser) {
		super(title, descriptionNode, summary, layout, drawAreaNames, broker,
				parser);
	} //Konstruktor

	/**
	 * Elter des Steps festlegen
	 * 
	 * @param issue
	 */
	public void setParentIssue(EIssue issue) {
		parentIssue = issue;
	} //setParentIssue

	/**
	 * Elter des Steps kriegen
	 */
	public EIssue getParentIssue() {
		return parentIssue;
	} //getParentIssue

	/**
	 * Gibt das nächste Step-Element zurück
	 */
	public Step getNextStep() {
		List steps = parentIssue.getSteps();
		int index = steps.indexOf(this);
		if ((index == -1) || (index + 1 >= steps.size()))
			return null;
		return (Step) steps.get(index + 1);
	} //getNextStep

	/**
	 * Gibt das vorherige Step-Element zurück
	 */
	public Step getPreviousStep() {
		List steps = parentIssue.getSteps();
		int index = steps.indexOf(this);
		if (index <= 0)
			return null;
		return (Step) steps.get(index - 1);
	} //getNextStep

	/**
	 * Überprüft, ob es einen nächsten Step gibt
	 */
	public boolean hasNextStep() {
		return getNextStep() != null;
	} //hasNextStep

	/**
	 * Gibt das erste Step-Element zurück
	 */
	public Step getFirstStep() {
		List steps = parentIssue.getSteps();
		return (Step) steps.get(0);
	} //getFirstStep
	
	
	public boolean hasBreakAction(){
		return actions.hasBreakAction();
	}

	/**
	 * Wenn der Parser gerade einen Step parst, so wird diese ParameterBoxView
	 * in die History-Löschliste eingetragen.
	 */
	public static void addParameterBoxesToDeleteAfterUse(StepInteractive view) {
		if (tempList != null)
			tempList.add(view);
	}//addParameterBoxesToDeleteAfterUse

	/**
	 * Erzeugt die Darstellung und speichert diese intern.
	 */
	protected void initView() {
		view = new ScrollablePanel(); // Rollbarer Behälter
		// Neues GridbagLayout erzeugen
		GridBagLayout gridbag = new GridBagLayout();
		view.setLayout(gridbag);
		//History holen
		LinkedList history = parentIssue.getHistory();
		//History einbauen
		int i = 0;
		for (Iterator iter = history.iterator(); iter.hasNext(); i++) {
			//Layout für History
			GridBagConstraints gbc = new GridBagConstraints();
			if (i != 0)
				gbc.insets = new Insets(5, 20, 5, 20);
			else
				gbc.insets = new Insets(20, 20, 5, 20);
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.weightx = 1.0;
			HistoryCompound h = (HistoryCompound) iter.next();
			Math4u2TextPane m = (Math4u2TextPane) h.view;
			m.setScrollOnUpdate(false);
			m.setOpaque(true);
			m.setHistory(true);
			gridbag.setConstraints(m, gbc);
			view.add(m, gbc);

			if (!iter.hasNext())
				unregisterAllTextFields();
		} //for i
		//alte Elemente vom Broker abhängen

		//Layout für Head
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 20, 10, 20);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1.0;
		head.setScrollOnUpdate(true);
		head.setOpaque(false);
		head.setHistory(false);
		gridbag.setConstraints(head, gbc);
		view.add(head, gbc);
		gridbag.setConstraints(foot, gbc);
		view.add(foot, gbc);
		HistoryCompound h = new HistoryCompound(head, description);
		history.add(h);
	} //initView

	public void clearHistory() {
		parentIssue.getHistory().clear();
	} //clearHistory

	protected void createView() {
		parameterBoxesToDeleteAfterUse = tempList;
		initView();
		tempList = new LinkedList();
	} //createView

	public int compareTo(Object o) {
		String s = getTitle().toString();
		String t = ((Step) o).getTitle().toString();
		return s.compareTo(t);
	} //compareTo

	public void unregisterAllTextFields() {
		if (getPreviousStep() == null)
			return;
		List params = getPreviousStep().parameterBoxesToDeleteAfterUse;
		if (params == null)
			return;
		for (Iterator iter = params.iterator(); iter.hasNext();) {
			StepInteractive mo = (StepInteractive) iter.next();
			try {
				mo.deactivate(broker);
			}catch (BrokerException e) {
				String name = mo.toString();
				if(mo instanceof MathObject) name = ((MathObject)mo).getIdentifier().toString();
				ExceptionManager.doError("Fehler beim Deaktivieren von Eingabeelement"+name+".",e);
			}
		} //for iter
		params.clear();
	}//unregisterAllTextFields

	/** Zusammenschluß von History-Sicht und den zu löschenden ParameterBoxen */
	class HistoryCompound {
		public JComponent view;

		public List parameterBoxViews;

		public HistoryCompound(JComponent v, StyledText st) {
			view = v;
			if (st == null)
				parameterBoxViews = null;
			else
				parameterBoxViews = st.getComponents();
		} //Konstruktor
	} //HistoryCompound
} //class Step
