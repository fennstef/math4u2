package math4u2.controller.reference;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import math4u2.controller.Broker;
import math4u2.controller.MathObject;
import math4u2.mathematics.collection.MathList;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.Variable;
import math4u2.util.exception.ExceptionManager;

/**
 * Allgemeines Verhalten von einem PathStep
 * 
 * Ein PathStep ist z.B in kreis.mitte.x die Methode "mitte" oder "x"
 * 
 * @author Fenn Stefan
 */
public abstract class AbstractPathStep implements PathStep {

	/** Argumente der Methode */
	protected TermNode[] args;

	/** Methode, die aufgerufen werden soll */
	protected Method stepMethod;

	/** nächste Methode */
	protected PathStep nextStep;

	/** Name der Methode */
	protected String name;

	/** Flag, ob unter der Methode, noch gebundene Variablen vorkommen */
	protected boolean hasVarTerms;

	/** gecachtes Kind */
	protected Object cachedChild;
	

	protected AbstractPathStep() {
	}

	/**
	 * Initialisierung
	 */
	public void instantiate(String name, Method stepMethod, TermNode[] args,
			PathStep nextStep) {
		this.name = name;
		this.stepMethod = stepMethod;
		this.args = args;
		this.nextStep = nextStep;
		hasVarTerms = checkVarTerms();
	} //instantiate

	/**
	 * @return
	 */
	private boolean checkVarTerms() {
		for (int i = 0; i < args.length; i++) {
			if (args[i].containsAnyVar())
				return true;
		} //for i
		return false;
	} //checkVarTerms

	/**
	 * Allgemein: Holt sich den Repräsentaten den nächsten Methodenschrittes
	 * 
	 * Hier wird das gleiche Objekt zurück gegeben, welches auch beim evaluieren
	 * zurück kommen würde.
	 */
	protected Object getLocalDummyObject(CreatesPath obj) {
		try {
			return getObjectFromMethod(obj);
		} catch (Exception e) {
			ExceptionManager.doError("Das Stellvertreter-Objekt wurde nicht gefunden ("+obj+").", e);
			throw new RuntimeException(e);
		} //catch
	} //getDummyObject

	/**
	 * gibt das Ergebnis zurück, wenn alle Methoden aufgerufen werden. (läuft
	 * rekursiv ab)
	 */
	public Object getObject(Object obj) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, MathException {

		//Kindobjekt holen
		cachedChild = getObjectFromMethod(obj);

		return _getObject();
	} //getObject

	private Object _getObject() throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, MathException {

		//falls es die letze Methode ist, aktuelles Kind zurückgeben
		if (nextStep == null)
			return cachedChild;
		
		//ansonsten rekursiv wiederholen
		return nextStep.getObject(cachedChild);
	} //_getObject

	/**
	 * Wenn keine Variable gebunden ist, wird ein zwischengespeichertes Ergebnis
	 * zurück gegeben
	 */
	public Object getShallowObject(Object root)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, MathException {

		if (hasVarTerms) {	
			cachedChild = getObjectFromMethod(root);
			if(nextStep==null)
				return cachedChild;
			else return nextStep.getObject(cachedChild);
		}
		
		if(nextStep==null)
			return cachedChild;
		else return nextStep.getShallowObject(cachedChild);
	} //getShallowObject

	public String getName() {
		return name;
	}

	/**
	 * gibt den letzen Methodenaufruf in der Hierarchie zurück. Z.B. k.mitte.x
	 * gibt den Step "x" zurück
	 */
	public PathStep getLastStep() {
		return (nextStep == null) ? this : nextStep.getLastStep();
	} //getLastStep

	/**
	 * gibt den nächsten Methoden-Schritt zurück
	 */
	public PathStep getNextStep() {
		return nextStep;
	} //getNextStep

	/**
	 * Vereinfacht im aktuellen Schritt und rekursiv in allen seinen
	 * Folgeschritten jeweils saemtliche Argumentterme
	 */

	public void simplify() throws Exception {
		// Im aktuellen Schritt alle Argumentterme vereinfachen
		for (int i = 0; i < args.length; i++) {
			args[i] = args[i].simplify();
		}
		// Falls vorhanden, im Folgeschritt rekursiv
		if (nextStep != null) {
			nextStep.simplify();
		}
	}

	public Object getObjectWithoutLastStep(Object obj)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, MathException {
		if (nextStep == null)
			return getObjectFromMethod(obj);
		if (nextStep.getNextStep() == null)
			return getObjectFromMethod(obj);
		return nextStep.getObjectWithoutLastStep(getObjectFromMethod(obj));
	} // getObjectWithoutLastStep
	
	protected Object[] result;
	protected Object[] result2;

	/**
	 * Rückgabetyp des Methodenaufrufs mit dem Objekt obj Z.B. "kreis.mitte.x"
	 * mit Objekt "kreis" gibt es den Punkt bei "mitte" zurück.
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws MathException
	 */
	public Object getObjectFromMethod(Object obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, MathException {
		//DoubleResult muß sich noch ändern
		if(result == null || result.length!=args.length)
			result = new Object[args.length];
		
		for (int i = 0; i < args.length; i++) {
			result[i] = args[i].eval();
		} //for i
		
		if(result2 == null || result2.length!=1)
			result2 = new Object[1];
		result2[0]= result;
		return stepMethod.invoke(obj,result2);
	} //getObjectFromMethod

	/**
	 * Update bei Redefinition
	 */
	public void swapLinks(MathObject oldObj, MathObject newObj) {
		for (int i = 0; i < args.length; i++) {
			args[i].swapLinks(oldObj, newObj);
		} //for i
		if (nextStep != null)
			nextStep.swapLinks(oldObj, newObj);
	} //swapLinks

	public String getPathString() {
		try {
		    String bodyMeth = getBodyString() + getArgumentsAsString();
		    
			if (nextStep == null)
				return bodyMeth;

			return bodyMeth	+ nextStep.getPathString();

		} catch (MathException e) {
			ExceptionManager.doError(e);
			throw new RuntimeException(e);
		} //catch
	} //getPathString

	/**
	 * Gibt die Argumente des Steps als String zurück
	 * 
	 */
	protected String getArgumentsAsString() {
		String argsStr = "";
		for (int i = 0; i < args.length; i++) {
			if (i != 0)
				argsStr += ", ";
			argsStr += args[i].getTermString();
		} //for i
		argsStr = (args.length == 0) ? "" : bracketArguments(argsStr);
		return argsStr;
	} //getArgumentsAsString

	/**
	 * Umklammert die einzelenen Argumente mit der 
	 * jeweiligen Klammer
	 * 
	 * @param argsStr Argumente als String
	 * @return geklammerte Argumente als String
	 */
	protected String bracketArguments(String argsStr){
	    return "(" + argsStr + ")";
	}//bracketArguments

	/**
	 * Wie muß sich der aktuelle Step darstellen ?
	 */
	protected abstract String getBodyString() throws MathException;

	/**
	 * Fuegt alle Funktionen zu indexFunctions hinzu, die in einem Index (Liste,
	 * Matrix, Vektor) des aktuellen Knotens und seines Teilbaums verwendet
	 * werden. Muss von den abgeleiteten Klassen ueberschrieben werden, in denen
	 * tatsaechlich Indices verwendet werden.
	 * 
	 * @param indexFunctions
	 *            Menge, in der die in Indices verwendeten Funktionen gesammelt
	 *            werden.
	 */
	public void insertIndexFunctions(Set indexFunctionSet) {
	}

	public void insertAllFunctions(Set functionSet) {
		for (int i = 0; i < args.length; i++) {
			args[i].insertAllFunctions(functionSet);
		}
		if (nextStep != null)
			nextStep.insertAllFunctions(functionSet);
	}

	/**
	 * Falls ein Kind oder ein Parameter eine gebundene Variable besitzt, wird
	 * <code>false</code> zurück geben und im Flag hasVar abgespeichert.
	 */
	public boolean containsAnyVar() {
		hasVarTerms = false;

		if (nextStep != null) {
			hasVarTerms = hasVarTerms || nextStep.containsAnyVar();
		} //if

		//Parameter überprüfen
		for (int i = 0; i < args.length; i++) {
			hasVarTerms = hasVarTerms || args[i].containsAnyVar();
		}
		return hasVarTerms;
	} //containsAnyVar

	public Class getReturnType(CreatesPath dummyObject) {
		if (nextStep == null) {
			if (name.startsWith("set_"))
				return Void.class;
			return dummyObject.getReturnType(this);
		}
		CreatesPath cp = (CreatesPath) getLocalDummyObject(dummyObject);

		//		if(cp==null) throw new NullPointerException("Fehler
		// "+nextStep.getMethodContext().getMethodName()+" "+dummyObject+"
		// "+dummyObject.getClass());
		return (Class) nextStep.getReturnType(cp);
	} //getReturnType

	public void setNextStep(PathStep nextStep) {
		this.nextStep = nextStep;
	} //setNextStep

	public TermNode[] getArgs() {
		return args;
	} //getArgs

	/**
	 * Gibt Methodeninformationen zurück
	 */
	public MethodContext getMethodContext() {
		return new MethodContext(name, args);
	} //getMethodeContext

	public String toString() { 
	    return getPathString();
	} //toString

	public PathStep getClone(Variable[] oldVars, Variable[] newVars)
			throws Exception {
		// Argumentterme clonen
		TermNode[] clonedArgs = new TermNode[args.length];
		for (int i = 0; i < args.length; i++) {
			clonedArgs[i] = args[i].getClone(oldVars, newVars);
		}

		// Naechsten Schritt clonen
		PathStep clonedNextStep = null;
		if (nextStep != null) {
			clonedNextStep = nextStep.getClone(oldVars, newVars);
		}

		PathStep newStep = this.getEmptyClone();
		newStep.instantiate(name, stepMethod, clonedArgs, clonedNextStep);
		return newStep;
	}//getClone

	public PathStep getClone(TermNode[] newArgTerms) {

		PathStep newStep = this.getEmptyClone();
		newStep.instantiate(name, stepMethod, newArgTerms, nextStep);
		return newStep;
	}//getClone
	
	public List getMethodContextList() {
		List contextList;
		MethodContext currentContext = this.getMethodContext();
		if (getNextStep() == null) {
			contextList = new ArrayList();
			contextList.add(currentContext);
		}
		else {
			contextList = getNextStep().getMethodContextList();
			contextList.add(0,currentContext);		
		}
		return contextList;
	}

	public PathStep substitute(Variable[] vars, TermNode[] terms)
			throws Exception {

		// In Argumenttermen substituieren
		TermNode[] substArgs = new TermNode[args.length];
		for (int i = 0; i < args.length; i++) {
			substArgs[i] = args[i].substitute(vars, terms);
		}

		// Im naechsten Schritt substituieren
		PathStep substNextStep = null;
		if (nextStep != null) {
			substNextStep = nextStep.substitute(vars, terms);
		}

		PathStep newStep = this.getEmptyClone();

		newStep.instantiate(name, stepMethod, substArgs, substNextStep);
		return newStep;
	}//subsitute

	/**
	 * Berechnet den Ableitungsterm, falls möglich.
	 * 
	 * @param var
	 * @param obj
	 * @param broker
	 * @return
	 * @throws Exception
	 */
	public TermNode derive(Variable var, CreatesPath obj, Broker broker)
			throws Exception {
		TermNode[] derivedArgumentTerms;
		Object OFM;
		// Es muss mindestens einen weiteren Schritt, naemlich einen
		// eval-Schritt geben, denn dieser weitere Schritt enthaelt
		// wie bei ll[3](x*x) den Term x*x

		if (nextStep == null) // es gibt keinen weiteren Schritt
			throw new MathException("Kein Argumentterm bei Ableitung");

		else { // es gibt mindestens einen weiteren Schritt
			OFM = getObjectFromMethod(obj); 
			if (    Function.class.isAssignableFrom(OFM.getClass())
					&& (nextStep instanceof CalcEvalPathStep)) {
				// es liegt also eine Sequenz Funktion , Argumentterm (im
				// eval-Schritt) vor

				// es darf nach dem nextStep (eval) kein weiterer Schritt folgen
				if (nextStep.getNextStep() != null) 
					throw new MathException("Fehler im abzuleitenden Term");

				Function func = (Function) OFM;
				TermNode[] arguments = nextStep.getArgs();

				derivedArgumentTerms = new TermNode[arguments.length];
				for (int i = 0; i < arguments.length; i++) {
					derivedArgumentTerms[i] = arguments[i].derive(var);
				}
				return func.buildDeriveTerm(arguments, derivedArgumentTerms, broker);
			} else {
				return nextStep.derive(var, (CreatesPath) OFM, broker);
			}//else
		}//else
	}//derive

	public void removeLastEval() {
		if (nextStep == null)
			return;
		if ((nextStep.getNextStep() == null)
				&& ((nextStep instanceof EvalPathStep)))
			nextStep = null;
		else
			nextStep.removeLastEval();
	}//removeLastEval

	public abstract PathStep getEmptyClone();

    /**
     * Erzeugt einen passenden Step in Abhängigkeit des
     * Methodennamens
     * 
     * @param methodName Methoden-Name
     * @param m Methoden-Objekt
     * @param args
     */
    public static PathStep createStep(Object invoker, String methodName, Method m, TermNode[] args) {
        PathStep ps = null;
        if(invoker instanceof MathList && "index".equals(methodName)){
            ps = new ListPathStep();
        }else if("eval".equals(methodName)){
            ps = new EvalPathStep();
        }else if("calceval".equals(methodName)){
            ps = new CalcEvalPathStep();
        }else if("index".equals(methodName)){
            ps = new IndexPathStep();
        }else{
            ps = new SimplePathStep();
        }//else
        
		ps.instantiate(methodName, m, args, null);
		return ps;
    }//createStep

} //class AbstractPathStep
