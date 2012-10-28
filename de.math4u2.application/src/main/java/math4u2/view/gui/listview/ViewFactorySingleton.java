package math4u2.view.gui.listview;

import java.awt.geom.GeneralPath;

import math4u2.controller.Broker;
import math4u2.mathematics.affine.Area;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.GraphInterfaceFactory;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.gui.listview.complete.CompleteAngleViewItem;
import math4u2.view.gui.listview.complete.CompleteAreaViewItem;
import math4u2.view.gui.listview.complete.CompleteArrowViewItem;
import math4u2.view.gui.listview.complete.CompleteCircleViewItem;
import math4u2.view.gui.listview.complete.CompleteCompoundCurveViewItem;
import math4u2.view.gui.listview.complete.CompleteCurveViewItem;
import math4u2.view.gui.listview.complete.CompleteDefaultViewItem;
import math4u2.view.gui.listview.complete.CompleteFunctionViewItem;
import math4u2.view.gui.listview.complete.CompleteMapViewItem;
import math4u2.view.gui.listview.complete.CompleteMarkerViewItem;
import math4u2.view.gui.listview.complete.CompleteMouseViewItem;
import math4u2.view.gui.listview.complete.CompletePointViewItem;
import math4u2.view.gui.listview.complete.CompleteStraightViewItem;
import math4u2.view.gui.listview.complete.CompleteStretchViewItem;
import math4u2.view.gui.listview.complete.CompleteTextBubbleViewItem;

public class ViewFactorySingleton implements ViewFactoryInterface{
	private static ViewFactorySingleton instance;
	private GraphInterfaceFactory graphFactory;
	
	private ViewFactorySingleton(){
		graphFactory = new GraphFactory();
	}
	
	public static ViewFactorySingleton getInstance(){
		if(instance==null){
			instance = new ViewFactorySingleton();
		}
		return instance;
	}
	
	public ListViewItemInterface createCompleteAreaViewItem(
			UserFunction mo, ListViewInterface alv, Broker broker) {
		Object obj = null;
		try {
			obj = mo.eval();
		} catch (MathException e) {
			ExceptionManager.doError("Fehler beim Evaluieren des Objekts "+mo.getKey(),e);
		}
		if (((Area) obj).isFillArea())
			return new CompleteAreaViewItem(mo, alv, broker);
		else
			return new CompleteCompoundCurveViewItem(mo, alv,
					broker);
	} //createCompleteAreaViewItem

	public ListViewItemInterface createCompleteBezierViewItem(
			UserFunction mo, ListViewInterface alv, Broker broker) {
		return new CompleteDefaultViewItem(mo, alv, broker);
	} //createCompleteBezierViewItem

	public ListViewItemInterface createCompleteCircleViewItem(
			UserFunction mo, ListViewInterface alv, Broker broker) {
		return new CompleteCircleViewItem(mo, alv, broker);
	} //createCompleteCircleViewItem

	public ListViewItemInterface createCompleteCompoundCurveViewItem(
			UserFunction mo, ListViewInterface alv, Broker broker) {
		return new CompleteDefaultViewItem(mo, alv, broker);
	} //createCompleteCompoundCurveViewItem

	public ListViewItemInterface createCompleteCurveViewItem(
			UserFunction mo, ListViewInterface alv, Broker broker) {
		return new CompleteCurveViewItem(mo, alv, broker);
	} //createCompleteCurveViewItem

	public ListViewItemInterface createCompleteFunctionViewItem(
			UserFunction mo, ListViewInterface alv, Broker broker) {
		return new CompleteFunctionViewItem(mo, alv, broker);
	} //createCompleteFunctionViewItem

	public ListViewItemInterface createCompletePointViewItem(
			UserFunction mo, ListViewInterface alv, Broker broker) {
		return new CompletePointViewItem(mo, alv, broker);
	} //createCompletePointViewItem

	public ListViewItemInterface createCompleteMarkerViewItem(
			UserFunction mo, ListViewInterface alv, Broker broker) {
		return new CompleteMarkerViewItem(mo, alv, broker);
	} //createCompleteMarkerViewItem
	
	public ListViewItemInterface createCompleteMatrixViewItem(
			UserFunction mo, ListViewInterface alv, Broker broker) {
		return new CompleteFunctionViewItem(mo, alv, broker);
	} //createCompleteMatrixViewItem	

	public ListViewItemInterface createCompleteStraightViewItem(
			UserFunction mo, ListViewInterface alv, Broker broker) {
		return new CompleteStraightViewItem(mo, alv, broker);
	} //createCompleteStraightViewItem

	public ListViewItemInterface createCompleteStretchViewItem(
			UserFunction mo, ListViewInterface alv, Broker broker) {
		return new CompleteStretchViewItem(mo, alv, broker);
	} //createCompleteStretchViewItem

	public ListViewItemInterface createCompleteVectorViewItem(
			UserFunction mo, ListViewInterface alv, Broker broker) {
		return new CompleteDefaultViewItem(mo, alv, broker);
	}

	public ListViewItemInterface createCompleteArrowViewItem(
			UserFunction mo, ListViewInterface alv, Broker broker) {
		return new CompleteArrowViewItem(mo, alv, broker);
	} //createCompleteVectorViewItem

	public ListViewItemInterface createCompleteAngleViewItem(
			UserFunction mo, ListViewInterface alv, Broker broker) {
		return new CompleteAngleViewItem(mo, alv, broker);
	} //createCompleteVectorViewItem

	public ListViewItemInterface createCompleteTextBubbleViewItem(
			UserFunction mo, ListViewInterface alv, Broker broker) {
		return new CompleteTextBubbleViewItem(mo, alv, broker);
	}

	public ListViewItemInterface createCompleteMapViewItem(
			UserFunction mo, ListViewInterface alv, Broker broker) {
		return new CompleteMapViewItem(mo, alv, broker);
	}
	
	public ListViewItemInterface createCompleteDiscreteSequenceViewItem(
			UserFunction mo, ListViewInterface alv, Broker broker) {
		return new CompleteDefaultViewItem(mo, alv, broker);
	}

	public ListViewItemInterface createCompleteDefaultViewItem(UserFunction f,
			ListViewInterface alv, Broker broker) {
		return new CompleteDefaultViewItem(f, alv, broker);
	}

	public ListViewItemInterface createCompleteMouseViewItem(UserFunction f,
			ListViewInterface alv, Broker broker) {
		return new CompleteMouseViewItem(f, alv, broker);
	}

	public GraphInterfaceFactory getGraphFactory() {
		return graphFactory;
	}
}
