package math4u2.view.gui.listview.complete;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.relation.Relation;
import math4u2.controller.relation.RelationFactory;
import math4u2.controller.relation.RelationInterface;
import math4u2.mathematics.affine.HasCompleteView;
import math4u2.mathematics.functions.UserFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.dragAndDrop.target.DropTargetOnCompleteView;
import math4u2.view.gui.Math4U2Win;
import math4u2.view.gui.component.ListCheckBox;
import math4u2.view.gui.listview.AbstractListView;
import math4u2.view.gui.listview.AbstractListViewItem;
import math4u2.view.gui.listview.ListViewItemInterface;

/**
 * Darsteller, der von MathObjects die komplette Ansicht darstellen kann
 * 
 * @author Fenn Stefan
 *  
 */
public class CompleteViewBox extends AbstractListView {
	//--------Konstanten---------------------------------
	/** Minimale Höhe des Containers */
	private final static int MIN_VIEWPORT_HEIGHT = 30;

	/** Breite des Containers */
	private final static int VIEWPORT_WIDTH = 400;

	/** Platzhalter für den Container (sollte immer vom Container überdeckt sein) */
	private JPanel dropField;

	/**
	 * Registriert von einem MathObject erzeugten vollständige Repräsentation.
	 * Diese wird in den Container eingefügt.
	 */
	public static void register(MathObject mo,
			CompleteViewBox completeViewContainer, Broker broker) {
		if (broker.knowsObject("CompleteView.$" + mo.getIdentifier()))
			return;
		UserFunction f = (UserFunction) mo;
		ListViewItemInterface abstractListViewItem = ((HasCompleteView) mo)
				.getCompleteView(f, completeViewContainer, broker);
		
		if(abstractListViewItem==null){
			System.out.println("asdf");
		}
		
		try {
			RelationInterface ri = RelationFactory.getCanvas_View_Relation();
			ri.setShortName(Relation.FIRST, "$" + mo.getIdentifier());
			LinkedList list = new LinkedList();
			list.add(abstractListViewItem);
			broker.addRelation(broker.getObject("CompleteView"),
					abstractListViewItem, ri, Broker.FIRST_OBJECT);

			ri = RelationFactory.getView_Function_Relation();
			list = new LinkedList();
			list.add(abstractListViewItem);
			broker.defineRelations(mo, list, ri, Broker.SECOND_OBJECT);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler beim Registieren des Objekts "+mo.getIdentifier()+" in der Detailsicht",e);
		} //catch
		completeViewContainer.addItem(abstractListViewItem);
		completeViewContainer.selectItem(abstractListViewItem);
	} //register

	public CompleteViewBox(Broker broker) {
		super(broker);
	} //Konstruktor

	public void init() {
		//DropField
		dropField = new JPanel();
		dropField.setPreferredSize(new Dimension(VIEWPORT_WIDTH, 30));
		dropField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		new DropTargetOnCompleteView(dropField, this);
		super.init();
		add(dropField, "0, 0, 1,0");
	} //init
	
	public boolean testDelete() {
		return false;
	} //testDelete

	public void setExpandList(boolean expand) {
		checkListExpand.setEnabled(objectList.getComponentCount() != 0);
		Object o = ((ListCheckBox)checkListExpand).getState();
		scrollPane.setVisible(o!=ListCheckBox.NOT_SELECTED);
		
		//was soll sichtbar sein
		Component[] ca = objectList.getComponents();
		if (o==ListCheckBox.SELECTED) {
			for (int i = 0; i < ca.length; i++) {
				ca[i].setVisible(true);
			}//for i
		} else {
			Component c = selectedItem;
			if ((selectedItem == null) && (objectList.getComponentCount() != 0))
				c = objectList.getComponent(0);
			for (int i = 0; i < ca.length; i++) {
				ca[i].setVisible(ca[i] == c);
			}//for i
		}//else

		Point p2 = SwingUtilities.convertPoint(dropField, 0, 0, Math4U2Win
				.getInstance().getContentPane());
		int width = dropField.getWidth();
		int heightTemp = 0;
		ca = objectList.getComponents();
		for (int i = 0; i < ca.length; i++) {
			JComponent c = (JComponent) ca[i];
			if (!c.isVisible())
				continue;
			Insets insets = c.getInsets();
			heightTemp += insets.top + insets.bottom - 1;
			heightTemp += c.getPreferredSize().height;
		} //for i
		int height = Math.min(MAX_VIEWPORT_HEIGHT, heightTemp + 1) + 1;
		height = Math.max(height, MIN_VIEWPORT_HEIGHT);
		scrollPane.setBounds(p2.x, p2.y, width, height);
		scrollPane.doLayout();
		objectList.revalidate();
	} 

	public void selectItem(AbstractListViewItem item) {
		super.selectItem(item);
		if (!checkListExpand.isSelected())
			setExpandList(false);
	}
	
	public void fitToScrollPane(JComponent c) {
		c.setPreferredSize(new Dimension(dropField.getBounds().width,c.getPreferredSize().height));
	}

	public Object getKey() {
		return "CompleteView";
	}
	
	public Object getIdentifier() {
		return getKey();
	} 
} //class CompleteViewContainer
