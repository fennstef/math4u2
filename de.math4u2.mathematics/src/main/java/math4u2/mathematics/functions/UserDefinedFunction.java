package math4u2.mathematics.functions;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Set;

import math4u2.application.MainWindow;
import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.HasBroker;
import math4u2.controller.MathObject;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.NotImplementedException;
import math4u2.view.graph.AbstractGraph;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.HasGraph;
import math4u2.view.graph.drawarea.DrawAreaInterface;

/**
 * Grundgerüst für selbstdefinierte Funktionen in einer 
 * Xml-Lektion. In der Xml-Datei wird festgelegt, welche
 * Methode mit neuen Inhalten gefüllt werden soll.
 * 
 * @author Fenn Stefan
 */
public class UserDefinedFunction extends Function implements HasGraph{
	
	/**
	 * Mit dieser Membervariable kann der Benutzer sich Zwischenergebnisse merken.
	 */
	public Object cache;
	
	/**
	 * Variablen für HasGraph-Implementierung
	 */
	private boolean visible = true;
	private int lineStyle;
	private boolean freeze;
	private Color color = Color.BLACK;
	private CallerThread callerThread;
	
	public UserDefinedFunction(){
		init();
	}
	
	public void init(){	
	}//init
	
	
	public void reEvalMe(final int millis){
		if(callerThread == null){
			callerThread = new CallerThread();
			callerThread.setMillis(millis);
			callerThread.start();
		}
		callerThread.setMillis(millis);
		callerThread.propagateChange=true;
	}//reEvalMe

	/**
	 * Berechnet den Funktionswert
	 */
	public Object eval(Object[] args) throws MathException{
		return null;
	}
	
    public void reportArgumentConflict(TermNode[] arguments)
			throws MathException {
		for (int i = 0; i < arguments.length; i++) {
			Class argType = arguments[i].getResultType();
			if (!argType.isAssignableFrom(getVariableType(i))) {
				throw new MathException("Typfehler in der Funktion "
						+ getName() + " an der Stelle " + (i+1) 
						+ "\nerwartet: \t" + getVariableType(i).getSimpleName() 
						+ "\nist: \t\t" + argType.getSimpleName());
			}
		}// for i
	}// reportArgumentConflict

	public Class getResultType(Class[] argTypes) {
		return null;
	}

	public Class getVariableType(int pos) throws MathException {
		return null;
	}

	public String[] getVariableNames() {
		throw new NotImplementedException();
	}

	public int getArity() {
		return 1;
	}

	public TermNode buildDeriveTerm(TermNode[] argumentTerms, TermNode[] derivedArgumentTerms, Broker broker) throws MathException {
		return null;
	}

	public boolean testSubstitution(MathObject oldObject, Set oldAggregateSet) {
		return true;
	}


	public void swapLinks(MathObject oldObject, MathObject newObject) throws Exception {
		// TODOs da muß noch was passieren	
		throw new NotImplementedException();
	}
	
	public String toString(){
		String str = getName();
		if(getArity()==0)
			return str;
		str+="(";
		String[] vars = getVariableNames();
		for(int i=0; i<vars.length; i++){
			if(i!=0) str+=", ";
			str+=vars[i];
		}
		str+=")";
		return str;
	}//toString
	
	public String getName(){
		return null;
	}//getName
	
	public Object getKey(){
		return getName();
	}
	
	public void paintGraph(Graphics gr, DrawAreaInterface da){
		if(!isVisible()) return;
		Color ca = gr.getColor();
		Graphics2D g = (Graphics2D) gr;
		Stroke strokeOld = g.getStroke();
		g.setColor(getColor());
		g.setStroke(AbstractGraph.getStroke(da.getStroke(), getLineStyle()));
		
		evalFunction();		
		paint(g,da);
		
		g.setColor(ca);
		g.setStroke(strokeOld);
	}//paintGraph
	
	public void paint(Graphics2D g, DrawAreaInterface da){
	}//paint

	private UserFunction f;
	
	/**
	 * HasGraph-Implementierungen
	 */	
	public GraphInterface getGraph(DrawAreaInterface da, UserFunction f) {		
		if(f.getArity()==0){
			this.f=f;
			return getViewFactory().createSimpleGraph(da);
		}else if(f.getArity()==1){
			return getViewFactory().createFunctionGraph(da, f);
		}else{
			throw new RuntimeException("Graph von "+f+" konnte nicht erzeugt werden.");
		}
	}//getGraph

	public boolean hasCurrentObjectGraph() {
		return false;
	}//hasCurrentObjectGraph

	public void setColor(Color color) {
		this.color = color;
	}//setColor
	
	public Color getColor() {
		return color;
	}//getColor

	public void setFreeze(boolean b) {
		freeze = b;
	}//setFreeze
	
	public boolean isFreeze() {
		return freeze;
	}//isFreeze

	public void setLineStyle(int lineStyle) {
		this.lineStyle = lineStyle;
	}//setLineStyle
	
	public int getLineStyle() {
		return lineStyle;
	}//getLineStyle

	public void setVisible(boolean b) {
		visible = b;
	}//setVisible
	
	public boolean isVisible() {
		return visible;
	}//isVisible
	
	public void evalFunction(){
		try {
			if(f!=null){
				f.eval();
			}
		} catch (MathException e) {
			ExceptionManager.doError(e);
		}
	}//evalFunction	
	
	class SimpleGraph extends AbstractGraph{	
		public SimpleGraph(DrawAreaInterface da){
			super(da);
		}//Konstruktor

		public HasGraph getModel() {
			return UserDefinedFunction.this.f;
		}//getModel

		public void paintGraph(Graphics gr) {
			UserDefinedFunction.this.paintGraph(gr, da);
		}//paintGraph

		public void swapLinks(MathObject oldObject, MathObject newObject) throws Exception {
			if(UserDefinedFunction.this.f==oldObject)
				UserDefinedFunction.this.f = (UserFunction) newObject;
			evalFunction();
		}//swapLinks
	}//class SimpleGraph
	
	class CallerThread extends Thread{
		private long millis;
		private long startTime;
		public boolean propagateChange=false;
		
		public void setMillis(long millis){ 
			this.millis = Math.max(millis,20);
			startTime = System.currentTimeMillis();
		}
		
		public void run(){
//			int counter=0;
			while(true){
				try {
					try {
						long curTime = System.currentTimeMillis();
						while(!propagateChange || ((curTime = System.currentTimeMillis())-startTime)<millis){
							if(!propagateChange){ 
								sleep(millis);
							}else{
								long sleepTime = millis-(curTime-startTime)-10;
								if(sleepTime<10) break;
								sleep(sleepTime);
							}
						}
						propagateChange=false;
//						if(counter++ % 10 == 0)
//							System.out.println(curTime-startTime+" "+curTime+" "+startTime);
					} catch (InterruptedException e) {
						ExceptionManager.doError(e);
					};
					try {
						synchronized(this){
							((HasBroker)MainWindow.get()).getBroker().propagateChange(UserDefinedFunction.this);
							
						}
					} catch (BrokerException e) {
						ExceptionManager.doError(e);
					}
				} catch (Throwable t) {
					ExceptionManager.doError(t);
				}
			}//while
		}// run
	}//class CallerThread

	public void applyProperties(HasGraph oldObject) {
      setColor(oldObject.getColor());
      setLineStyle(oldObject.getLineStyle());
      setVisible(oldObject.isVisible());
	}
}//class UserDefinedFunction
