package math4u2.mathematics.affine;

import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.reference.MethodContext;
import math4u2.controller.relation.Relation;
import math4u2.controller.relation.RelationFactory;
import math4u2.controller.relation.RelationInterface;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.HasGraph;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.gui.listview.ListViewItemInterface;
import math4u2.view.gui.listview.ViewFactoryInterface;

public class Bar extends AbstractArea {

	private UserFunction xVector;
	private UserFunction yVector;
	private UserFunction thickFunc = null;

	public Bar(String name, MathObject xVector, MathObject yVector, MathObject thickFunc, 
			Color color, int lineStyle, boolean isVisible, Broker broker, ViewFactoryInterface viewFactory) {

		super("balken", name, color, true, lineStyle, isVisible, broker, viewFactory);
		this.xVector = (UserFunction) xVector;
		this.yVector = (UserFunction) yVector;
		this.thickFunc = (UserFunction)thickFunc;
	} //Konstruktor
	
	
	
	public Bar(MathObject xVector, MathObject yVector, MathObject thickFunc, Broker broker, ViewFactoryInterface viewFactory){
		this(null, xVector, yVector, thickFunc, Color.black, HasGraph.SOLID_STROKE, true, broker, viewFactory);
	} // 
	

	/**
	 * Verknüpt das Diagramm b mit seinen Beziehungspartnern (parts)
	 * 
	 * @param b
	 *            Diagramm, der verknüpft werden soll.
	 * @param parts
	 *            Liste der Objekte, die Beziehungspartner sind
	 * @param regList
	 *            Liste der Objekte, die beim scheitern gelöscht werden (anonyme
	 *            Objekte)
	 * @param broker
	 */
	public static void register(MathObject b, List partsWithPath, List regList,
			Broker broker) throws BrokerException {
		//Überprüfung, ob alles in Ordnung ist
		if (!(b instanceof Bar))
			throw new IllegalArgumentException(
					"Das Objekt muß ein Balkendiagramm sein. Ist aber "
							+ b.getClass());
		if (partsWithPath.size() != 3)
			throw new IllegalArgumentException(
					"Die Teile-Liste muß genau die Länge 3 haben.");
		if (broker == null)
			throw new NullPointerException(
					"Der Broker ist null. Dies ist hier nicht erlaubt.");
		//Beziehungen erzeugen
		RelationInterface[] ra = new RelationInterface[3];
		List parts = new LinkedList();
		for (int i = 0; i < ra.length; i++) {
			UserFunction part_i = (UserFunction) partsWithPath.get(i);
			if (regList.contains(part_i))
				ra[i] = RelationFactory.getPart_Of_Relation();
			else
				ra[i] = RelationFactory.getFunction_SubFunction_Relation();
			parts.add(part_i);
			_setCreationPath(ra[i], part_i);
		}
		//Beziehungen bennenen
		ra[0].setShortName(Relation.FIRST, "x-werte");
		ra[1].setShortName(Relation.FIRST, "y-werte");
		ra[1].setShortName(Relation.FIRST, "dicke");
		List relations = Arrays.asList(ra);

		//Definieren lassen
		broker.defineRelations(b, parts, relations, Broker.FIRST_OBJECT);
	} //register
	
	public UserFunction getXVector(){
		return xVector;
	}
	
	public UserFunction getYVector(){
		return yVector;
	}
	
	public UserFunction getThickness(){
		return thickFunc;
	}
	
	public void setFreeze(boolean freeze) {
		super.setFreeze(freeze);
		xVector.setFreeze(freeze);
		yVector.setFreeze(freeze);
		thickFunc.setFreeze(freeze);
	}//setFreeze
	
	public String bodyToString() {
		String vector1Str = getRepresentation(xVector);
		String vector2Str = getRepresentation(yVector);
		String thickStr = getRepresentation(thickFunc);
		return type + "(" + vector1Str + "," + vector2Str+ "," + thickStr + ")";
	} //bodyToString
	
	public GraphInterface getGraph(DrawAreaInterface da, UserFunction f) {
		return viewFactory.getGraphFactory().createBarGraph(da, f);
	} //getGraph
	
	
	public MatrixDoubleResult[] getVectorsEval() throws MathException {
		MatrixDoubleResult[] mdr = { (MatrixDoubleResult) xVector.eval(),
				(MatrixDoubleResult) yVector.eval() };
		return mdr;
	} //getVectorEval
	
	public double getThick() throws MathException {		
		if ( thickFunc == null ) return -1;
		return ((ScalarDoubleResult)thickFunc.eval()).value;
	} //getThick
	
	public void swapLinks(MathObject oldObject, MathObject newObject)
	throws Exception {

		if (xVector == oldObject) {
			xVector = (UserFunction) newObject;
		} else {
			xVector.swapLinks(oldObject, newObject);
		}
		if (yVector == oldObject) {
			yVector = (UserFunction) newObject;
		} else {
			yVector.swapLinks(oldObject, newObject);
		}
		if (thickFunc == oldObject) {
			thickFunc = (UserFunction) newObject;
		} else {
			thickFunc.swapLinks(oldObject, newObject);
		}
	} //swapLinks
	
	public Object operator_set_vektor1(Object[] values) {
		TermNode tm;
		tm = ((UserFunction) values[0]).getFunction();

		xVector.setFunction(tm);
		try {
			broker.propagateChange(xVector);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler bei der Methode 'vektor1' ("+getIdentifier()+").",e);
		}
		return null;
	} //operator_vektor1

	public Class returnType_vektor1(MethodContext mc) {
		return UserFunction.class;
	} //returnType_vektor1

	public Object operator_set_vektor2(Object[] values) {
		TermNode tm;
		tm = ((UserFunction) values[0]).getFunction();

		yVector.setFunction(tm);
		try {
			broker.propagateChange(yVector);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler bei der Methode 'vektor1' ("+getIdentifier()+").",e);
		}
		return null;
	} //operator_vektor2

	public Class returnType_vektor2(MethodContext mc) {
		return UserFunction.class;
	} //returnType_vektor2
	
	public ListViewItemInterface getCompleteView(UserFunction f,
			ListViewInterface alv, Broker broker) {
		return viewFactory.createCompleteDefaultViewItem(f, alv, broker);
	}//getCompleteView


} //class Bar
