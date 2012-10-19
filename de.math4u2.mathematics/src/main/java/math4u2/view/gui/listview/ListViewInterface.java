package math4u2.view.gui.listview;

import math4u2.controller.MathObject;

public interface ListViewInterface extends MathObject {

	void deleteSelectedItem();

	void selectItem(ListViewItemInterface listViewItem);

	void exportData(ListViewItemInterface listViewItem);

	void removeItem(ListViewItemInterface listViewItem);

	boolean itemHasExport(ListViewItemInterface listViewItem);

	void setExpandList();

}
