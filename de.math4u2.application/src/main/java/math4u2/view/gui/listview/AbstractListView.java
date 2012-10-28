package math4u2.view.gui.listview;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import math4u2.application.MainWindow;
import math4u2.application.resource.Colors;
import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.reference.PathStep;
import math4u2.controller.relation.ObjectNotInRelationException;
import math4u2.controller.relation.RelationContainer;
import math4u2.parser.importdata.WhitespaceSeperated;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.NotImplementedException;
import math4u2.view.gui.Math4U2Win;
import math4u2.view.gui.component.ListCheckBox;
import math4u2.view.layout.StackLayout;
import math4u2.view.layout.TableLayout;

/**
 * Grafische Repräsentation von Listen
 * 
 * @author Fenn Stefan
 */
public abstract class AbstractListView extends JPanel implements MathObject, ListViewInterface{
	//-------- Konstanten ---------------------------------
	/** Gibt die Höhe an, ab wann der ScorllBar zu sehen ist */
	protected static int MAX_VIEWPORT_HEIGHT = 400;

	//-------- Model --------------------------------------
	/** Referenz zum Broker */
	protected Broker broker;

	/** Beziehungs-Container */
	protected RelationContainer relationContainer;

	/** merkt sich das aktuell ausgewähltes Element */
	protected Component selectedItem = null;

	//-------- GUI --------------------------------------
	/** Wenn zu viele Komponenten eingefügt werden, erscheint ein ScrollBar */
	protected JScrollPane scrollPane;

	/** Komponente, die anzeigt, ob die Liste eingefahren wird oder nicht */
	protected ListCheckBox checkListExpand;

	/** Container, der alle Objekte hält */
	protected JPanel objectList = new JPanel(new StackLayout(0));

	public AbstractListView(Broker broker) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.broker = broker;
		relationContainer = new RelationContainer(this);
		init();
	} //Konstruktor

	/**
	 * Initialisiert die grafische Komponenten (wird nur einmal aufgerufen)
	 */
	protected void init() {
		//ScrollPane
		scrollPane = new JScrollPane(objectList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		Math4U2Win.getInstance().getLayeredPane().add(scrollPane,
				JLayeredPane.PALETTE_LAYER);

		checkListExpand = new ListCheckBox(null, ListCheckBox.SELECTED);
		checkListExpand.setPreferredSize(new Dimension(12, 30));
		checkListExpand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setExpandList(isExpand());
			}//actionPerformed
		});

		//Layout
		double P = TableLayout.PREFERRED, F = TableLayout.FILL;
		double size[][] = {
		/* Zeilen */// 
				{ 20, F, P },
				/* Spalten */// 
				{ F } };
		setLayout(new TableLayout(size));
		add(checkListExpand, "2, 0, L, C");

		try {
			broker.publishObject(this, (String) getKey());
		} catch (ObjectNotInRelationException e) {
			ExceptionManager.doError("Fehler beim Erzeugen der Definitionsliste",e);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler beim Erzeugen der Definitionsliste",e);
		}
	} //init

	public abstract void setExpandList(boolean expand);

	public void setExpandList() {
		setExpandList(isExpand());
	}//setExpandList
	
	public JCheckBox getExpandControl(){		
		return checkListExpand;
	}

	public boolean isExpand() {
		return checkListExpand.isSelected();
	}//isExpand
	
	public String getExpandState(){
		if(checkListExpand.getState()==ListCheckBox.SELECTED){
			return "all";
		} else if (checkListExpand.getState()==ListCheckBox.NOT_SELECTED){
			return "none";
		} else {
			return "one";
		}
	}

	public abstract Object getKey();

	public boolean testSubstitution(MathObject oldObject, Set oldAggregateSet) {
		return false;
	} //testSubstitution

	public boolean testDelete() {
		return true;
	} //testDelete

	public void prepareDelete() {
	}

	public void renew(MathObject source) {
	}

	public RelationContainer getRelationContainer() {
		return relationContainer;
	} //getRelationContainer

	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {
	} //class ListBoxInputListener

	public String toString() {
		return (String) getKey();
	} //toString

	/**
	 * Fügt eine neue Komponente hinzu
	 */
	public void addItem(ListViewItemInterface abstractListViewItem) {
		fitToScrollPane((JComponent) abstractListViewItem);
		objectList.add((Component) abstractListViewItem,0);
		setExpandList();
	}//addItem
	
	public abstract void fitToScrollPane(JComponent c);
	
	public void scrollPaneHasResized() {
		Component[] ca = objectList.getComponents();
		for (int i = 0; i < ca.length; i++) {
			fitToScrollPane((JComponent)ca[i]);
		}//for i
	}//scrollPaneHasResized

	/**
	 * Entfernt ein bestimmtes Element
	 */
	public void removeItem(ListViewItemInterface item) {
		item.setVisible(false);
		organizeSelectionAfterDelete(item);
		objectList.remove((Component)item);
		if (selectedItem == item)
			selectedItem = null;
		setExpandList();
	}//removeElement

	/**
	 * Verarbeitet die Auswahl eines Elements
	 * 
	 * @param item
	 *            ausgewähltes Element
	 */
	public void selectItem(ListViewItemInterface item) {
		selectedItem = (Component) item;
		Component[] ca = objectList.getComponents();
		for (int i = 0; i < ca.length; i++) {
			if (ca[i] == selectedItem)
				ca[i].setBackground(Colors.SELECTED);
			else
				ca[i].setBackground(Colors.BACKGROUND);
		}//For i
		if (selectedItem != null)
			selectedItem.requestFocus();
	}//selectItem

	/**
	 * Diese Methode wird kurz vor dem Löschen von item aufgerufen und
	 * entscheidet, welches Element nun ausgewählt werden soll.
	 * 
	 * @param item
	 */
	public void organizeSelectionAfterDelete(ListViewItemInterface item) {
		if (item == null) {
			if (objectList.getComponentCount() == 0)
				selectItem(null);
			//erstes Element auswählen
			selectItem((AbstractListViewItem) objectList.getComponent(0));
		}//if
		Component[] ca = objectList.getComponents();
		int pos = 0;
		for (int i = 0; i < ca.length; i++) {
			if (ca[i] == item)
				pos = i;
		}//for i
		int size = objectList.getComponentCount();
		if (pos < size - 1) {
			selectItem((AbstractListViewItem) objectList.getComponent(pos + 1));
		} else if (size > 1) {
			selectItem((AbstractListViewItem) objectList.getComponent(pos - 1));
		} else {
			selectItem(null);
		}
	}//organizeSelectionAfterDelete

	/**
	 * Löschen des selektierten Elements
	 */
	public void deleteSelectedItem() {
		if (selectedItem == null)
			return;
		AbstractListViewItem item = (AbstractListViewItem) selectedItem;
		selectedItem = null;
		try {
			broker.deleteObject((MathObject) item);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler beim Löschen des Objekts "+item.getName(),e);
		}//catch
		if (selectedItem != null)
			selectedItem.requestFocus();
	}//deleteSelectedItem

	public PathStep createPathStep(List methods) {
		throw new NotImplementedException();
	}//createPathStep

	public Class getReturnType(PathStep nextStep) {
		throw new NotImplementedException();
	}//getReturnType

	/**
	 * Exportiert die Daten eines Objekts in eine Datei
	 * @param item Objekt, das exportiert werden soll
	 */
	public void exportData(ListViewItemInterface item) {
		MathObject obj = item.getModel();
		WhitespaceSeperated.exportData(obj, MainWindow.get());
	}//exportData

	/**
	 * Gibt <code>true</code> zurück, falls das Objekt item
	 * eine Export-Funktion besitzt.
	 */
	public boolean itemHasExport(ListViewItemInterface item) {
		MathObject obj = item.getModel();
		return WhitespaceSeperated.canExport(obj);
	}//itemHasExport

} //class AbstractListView
