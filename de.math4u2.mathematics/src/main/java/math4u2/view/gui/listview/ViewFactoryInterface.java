package math4u2.view.gui.listview;

import math4u2.controller.Broker;
import math4u2.mathematics.functions.UserFunction;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.GraphInterfaceFactory;
import math4u2.view.graph.drawarea.DrawAreaInterface;

/**
 * Hilft die richtigen Views zu erzeugen.
 * 
 * @author Fenn Stefan
 */
public interface ViewFactoryInterface {

	ListViewItemInterface createCompleteAreaViewItem(UserFunction mo,
			ListViewInterface alv, Broker broker);

	ListViewItemInterface createCompleteBezierViewItem(UserFunction mo,
			ListViewInterface alv, Broker broker);

	ListViewItemInterface createCompleteCircleViewItem(UserFunction mo,
			ListViewInterface alv, Broker broker);

	ListViewItemInterface createCompleteCompoundCurveViewItem(UserFunction mo,
			ListViewInterface alv, Broker broker);

	ListViewItemInterface createCompleteCurveViewItem(UserFunction mo,
			ListViewInterface alv, Broker broker);

	ListViewItemInterface createCompleteFunctionViewItem(UserFunction mo,
			ListViewInterface alv, Broker broker);

	ListViewItemInterface createCompletePointViewItem(UserFunction mo,
			ListViewInterface alv, Broker broker);

	ListViewItemInterface createCompleteMarkerViewItem(UserFunction mo,
			ListViewInterface alv, Broker broker);

	ListViewItemInterface createCompleteMatrixViewItem(UserFunction mo,
			ListViewInterface alv, Broker broker);

	ListViewItemInterface createCompleteStraightViewItem(UserFunction mo,
			ListViewInterface alv, Broker broker);

	ListViewItemInterface createCompleteStretchViewItem(UserFunction mo,
			ListViewInterface alv, Broker broker);

	ListViewItemInterface createCompleteVectorViewItem(UserFunction mo,
			ListViewInterface alv, Broker broker);

	ListViewItemInterface createCompleteArrowViewItem(UserFunction mo,
			ListViewInterface alv, Broker broker);

	ListViewItemInterface createCompleteAngleViewItem(UserFunction mo,
			ListViewInterface alv, Broker broker);

	ListViewItemInterface createCompleteTextBubbleViewItem(UserFunction mo,
			ListViewInterface alv, Broker broker);

	ListViewItemInterface createCompleteMapViewItem(UserFunction mo,
			ListViewInterface alv, Broker broker);

	ListViewItemInterface createCompleteDiscreteSequenceViewItem(
			UserFunction mo, ListViewInterface alv, Broker broker);

	ListViewItemInterface createCompleteDefaultViewItem(UserFunction f,
			ListViewInterface alv, Broker broker);

	ListViewItemInterface createCompleteMouseViewItem(UserFunction f,
			ListViewInterface alv, Broker broker);

	GraphInterfaceFactory getGraphFactory();

}
