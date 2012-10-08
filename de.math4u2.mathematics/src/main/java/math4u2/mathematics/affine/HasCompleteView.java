package math4u2.mathematics.affine;

import math4u2.controller.Broker;
import math4u2.mathematics.functions.UserFunction;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.gui.listview.ListViewItemInterface;

/**
 * Gewährleistet, dass das Objekt ein vollständige Ansicht erzeugen kann.
 * 
 * @author Fenn Stefan
 */
public interface HasCompleteView {
	ListViewItemInterface getCompleteView(UserFunction f, ListViewInterface alv, Broker broker);
}//HasCompleteView
