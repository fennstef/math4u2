package math4u2.view.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import math4u2.controller.MathObject;
import math4u2.mathematics.collection.MathList;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.IScalarStringHolder;
import math4u2.view.graph.util.SimpleScalarStringValueHolder;
import math4u2.view.gui.listview.GraphWrapper;

/**
 * Zeichnet eine Liste von grafischen Objekten
 * 
 * @author Fenn Stefan
 */
public class ListGraph extends AbstractGraph {

	private UserFunction list;

	/** gecachte Evaluierung des MathObject-Liste */
	private MathList cacheObject;

	private List graphList;

	public ListGraph(DrawAreaInterface da, UserFunction list) {
		super(da);
		this.list = list;
		graphList = new ArrayList(getMathList().getList().size());

		initGraphs();
	} // Konstruktor

	public MathList getMathList() {
		if (cacheObject == null)
			cacheObject = evalMathList();
		return cacheObject;
	}// getMathList

	public MathList evalMathList() {
		try {
			return (MathList) list.eval();
		} catch (MathException e) {
			ExceptionManager.doError("Fehler beim Zeichnen des Listen-Graphen "
					+ list, e);
			return null;
		}// catch
	}// evalList

	private void initGraphs() {
		int i = getMathList().firstElementIndex;
		for (Iterator iter = getMathList().getList().iterator(); iter.hasNext(); i++) {
			UserFunction mo = (UserFunction) iter.next();
			if (mo.hasCurrentObjectGraph()) {
				GraphInterface gi = mo.getGraph(da, mo);
				setShowNames(i, gi);
				graphList.add(gi);
				gi.setColor(getColor());
				da.addGraph(gi);
			} // if
		} // for iter
	} // initGraphs

	private void setShowNames(int i, GraphInterface gi) {
		if (!(gi instanceof GraphWrapper))
			return;
		GraphWrapper<?> gw = (GraphWrapper<?>) gi;
		if (!(gw.getInternal() instanceof AbstractSimpleGraph))
			return;
		AbstractSimpleGraph ag = (AbstractSimpleGraph) gw.getInternal();
		if (!getMathList().isShowNames())
			return;
		try {
			ag.getName().setScalar(list.getKey() + "", true);
			ag.getIndex().setScalar(i + "", true);
		} catch (Exception e) {
			ExceptionManager.doError(e);
		}
	}

	public void paintGraph(Graphics gr) {
	} // paintGraph

	public HasGraph getModel() {
		return list;
	} // getModel

	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {
		if (oldObject == list) {
			list = (UserFunction) newObject;
			for (Iterator iter = graphList.iterator(); iter.hasNext();) {
				GraphInterface gi = (GraphInterface) iter.next();
				da.removeGraph(gi);
			} // for iter
			graphList.clear();
			initGraphs();
		} else {
			for (Iterator iter = graphList.iterator(); iter.hasNext();) {
				GraphInterface gi = (GraphInterface) iter.next();
				gi.swapLinks(oldObject, newObject);
			} // for iter
		}// else

		cacheObject = evalMathList();
	} // swapLinks

	public void prepareDelete() {
		super.prepareDelete();
		for (Iterator iter = graphList.iterator(); iter.hasNext();) {
			GraphInterface gi = (GraphInterface) iter.next();
			gi.prepareDelete();
			da.removeGraph(gi);
		} // for iter
	} // prepareDelete

	public void renew(MathObject mo) {
		if (mo == getModel()) {
			da.graphHasChanged();
		} else {
			int i = getMathList().firstElementIndex;
			for (Iterator iter = graphList.iterator(); iter.hasNext(); i++) {
				GraphInterface gi = (GraphInterface) iter.next();
				if (gi instanceof AbstractGraph) {
					AbstractGraph ag = (AbstractGraph) gi;
					if (getMathList().isShowNames()) {
						ag.setName(list.getKey() + "", i + "");
					} else {
						ag.setName("");
					} // else
				}// if
				gi.renew(mo);
			} // for iter
		} // else
	} // renew

	public void setColor(Color c) {
		super.setColor(c);
		for (Iterator iter = graphList.iterator(); iter.hasNext();) {
			GraphInterface gi = (GraphInterface) iter.next();
			gi.getModel().setColor(c);
		}// for
	}// setColor
} // class ListGraph
