package math4u2.view.gui.listview;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Stroke;
import java.util.List;
import java.util.Set;

import math4u2.controller.MathObject;
import math4u2.controller.reference.PathStep;
import math4u2.controller.relation.RelationContainer;
import math4u2.mathematics.affine.AbstractArea;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.NotImplementedException;
import math4u2.view.graph.AbstractSimpleGraph;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.IAreaGraphSettings;
import math4u2.view.graph.SimpleGraphInterface;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.util.FixedScalarStringValueHolder;
import math4u2.view.graph.util.IScalarStringHolder;
import math4u2.view.graph.util.SimpleScalarStringValueHolder;

public class GraphWrapper<T> implements MathObject, GraphInterface,
		SimpleGraphInterface {
	protected DrawAreaInterface da;
	protected UserFunction userFunction;
	protected T cacheObject;
	protected RelationContainer relationContainer;
	protected SimpleGraphInterface simpleGraph;

	public GraphWrapper(UserFunction userFunction, DrawAreaInterface da) {
		this.userFunction = userFunction;
		this.da = da;
		this.relationContainer = new RelationContainer(this);		
	}

	public void setSimpleGraph(SimpleGraphInterface simpleGraph){
		this.simpleGraph = simpleGraph;
	}

	public T getAffineObject() {
		if (cacheObject == null)
			cacheObject = evalAffineObject();
		return cacheObject;
	}

	public T evalAffineObject() {
		try {
			return (T) userFunction.eval();
		} catch (MathException e) {
			ExceptionManager.doError("Fehler beim Zeichnen des Graphen "
					+ userFunction, e);
			return null;
		}// catch
	}

	public UserFunction getModel() {
		return userFunction;
	}

	public void setColor(Color c) {
		getModel().setColor(c);
	} // setColor
	
	protected void setFillColor(Color fillColor) {
		T aff = getAffineObject();
		if(aff instanceof AbstractArea){
			((AbstractArea)aff).setFillColor(fillColor);
		}else{
			throw new RuntimeException();
		}
	}

	public Color getColor() {
		return getModel().getColor();
	}

	public void setLineStyle(int style) {
		getModel().setLineStyle(style);
		da.graphHasChanged();
	} // setLineStyle

	public int getLineStyle() {
		return getModel().getLineStyle();
	}

	public void setVisible(boolean b) {
		getModel().setVisible(b);
		da.graphHasChanged();
	} // setVisible

	public boolean isVisible() {
		return getModel().isVisible();
	}

	/**
	 * @see math4u2.controller.MathObject#swapLinks(math4u2.controller.MathObject,
	 *      math4u2.controller.MathObject)
	 */
	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {
		if (userFunction == oldObject) {
			userFunction = (UserFunction) newObject;
		} else {
			userFunction.swapLinks(oldObject, newObject);
		}
		cacheObject = null;
	}

	/** Erstellt ein Strichmuster-Objekt */
	public Stroke getStroke() {
		return AbstractSimpleGraph.getStroke(da.getStroke(), getLineStyle());
	}
	
	protected Color getBorderColor() {
		T aff = getAffineObject();
		if(aff instanceof AbstractArea){
			return ((AbstractArea)aff).getBorderColor();
		}else{
			throw new RuntimeException();
		}
	}

	protected Color getFillColor() {
		T aff = getAffineObject();
		if(aff instanceof AbstractArea){
			return ((AbstractArea)aff).getFillColor();
		}else{
			throw new RuntimeException();
		}
	}

	protected void setBorderColor(Color borderColor) {
		T aff = getAffineObject();
		if(aff instanceof AbstractArea){
			((AbstractArea)aff).setBorderColor(borderColor);
		}else{
			throw new RuntimeException();
		}
	}

	public String toString() {
		return getModel().toString();
	}

	// MathObject Implementierungen
	public void renew(MathObject source) {
		renew();
//		da.graphHasChanged();
	}

	public boolean testSubstitution(MathObject a, Set oldAggregateSet) {
		return true;
	}

	public boolean testDelete() {
		return true;
	}

	public void prepareDelete() {
		da.removeGraph(this);
	}

	public String getKey() {
		if(getModel().getKey()==null){
			getModel().toString();
			System.out.println(getModel().getName());
			return null;
		}
		return getModel().getKey().toString();
	}
	
	public Object getIdentifier(){
		return "$graph"+getModel().getIdentifier();
	}

	public void paintGraph(Graphics g) {
		simpleGraph.paintGraph(g);
	}

	public RelationContainer getRelationContainer() {
		return relationContainer;
	}

	public PathStep createPathStep(List methods) {
		throw new NotImplementedException();
	}// createPathStep

	public Class getReturnType(PathStep nextStep) {
		throw new NotImplementedException();
	}// getReturnType

	public void setName(String name) {
		// do nothing
	}

	public void detach() throws Exception {
		prepareDelete();
	}

	public void renew() {
		simpleGraph.renew();
	}
	
	public IAreaGraphSettings getAreaSettings(){
		return new IAreaGraphSettings() {
			
			public void setVisible(boolean b) {
				GraphWrapper.this.setVisible(b);
			}
			
			public void setLineStyle(int style) {
				GraphWrapper.this.setLineStyle(style);
			}
			
			public void setColor(Color c) {
				GraphWrapper.this.setColor(c);
			}
			
			public boolean isVisible() {
				return GraphWrapper.this.isVisible();
			}
			
			public int getLineStyle() {
				return GraphWrapper.this.getLineStyle();
			}
			
			public Color getColor() {
				return GraphWrapper.this.getColor();
			}
			
			public void setFillColor(Color fillColor) {
				GraphWrapper.this.setFillColor(fillColor);
			}
			
			public void setBorderColor(Color borderColor) {
				GraphWrapper.this.setBorderColor(borderColor);
			}
			
			public Color getFillColor() {
				return GraphWrapper.this.getFillColor();
			}
			
			public Color getBorderColor() {
				return GraphWrapper.this.getBorderColor();
			}
		};
	}
	
	public IScalarStringHolder getNameHolder(){
		return new FixedScalarStringValueHolder(){
			private String name;
			
			public String getScalarOrNull() {
				if(name==null){
					Object id = getModel().getKey();
					if(id==null) return null;
					name=id.toString();	
				}
				return name;
			}

			@Override
			public void setScalar(String value, boolean propagateChange)
					throws Exception {
				name = value;
			}
			
		};
	}
	
	public SimpleGraphInterface getInternal(){
		return simpleGraph;
	}
}
