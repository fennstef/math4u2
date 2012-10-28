package math4u2.view.gui.listview.complete;

import javax.swing.JComponent;
import javax.swing.JLabel;

import math4u2.controller.Broker;
import math4u2.mathematics.functions.UserFunction;
import math4u2.view.dragAndDrop.DNDHandler;
import math4u2.view.dragAndDrop.target.DropTargetOnCompleteView;
import math4u2.view.gui.listview.AbstractListViewItem;
import math4u2.view.gui.listview.ListViewInterface;

/**
 * Darsteller eines Default-Objekts in dem CompleteViewContainer
 * 
 * @author Fenn Stefan
 * @see CompleteViewContainer
 */
public class CompleteDefaultViewItem extends AbstractListViewItem {
	private JLabel fnLabel;

	public CompleteDefaultViewItem(UserFunction model, ListViewInterface parent,
			Broker broker) {
		super(model, parent, broker);
	} //Konstruktor

	protected void onceInit() {
		fnLabel = new JLabel();
		addListener(fnLabel);
		add(fnLabel);
		super.onceInit();
	}//onceInit

	public void reInit() {
		fnLabel.setText(model.toString());
	}//init

	protected void addListener(JComponent c) {
		super.addListener(c);
		new DNDHandler(c, "CompleteView.$" + model.getKey(), broker);
		new DropTargetOnCompleteView(this, (CompleteViewBox) parent);
	}//addListener

} //class ListBoxItem
