package math4u2.mathematics.collection;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.reference.AbstractPathStep;
import math4u2.controller.reference.CreatesPath;
import math4u2.controller.reference.MethodContext;
import math4u2.controller.reference.PathStep;
import math4u2.controller.relation.ObjectNotInRelationException;
import math4u2.controller.relation.RelationContainer;
import math4u2.controller.relation.RelationFactory;
import math4u2.controller.relation.RelationInterface;
import math4u2.mathematics.affine.AbstractAffineObject;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.results.DoubleResult;
import math4u2.mathematics.results.Result;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.mathematics.termnodes.ScalarVariable;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeDoubleResult;
import math4u2.mathematics.termnodes.Variable;
import math4u2.mathematics.types.ScalarType;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.gui.listview.ViewFactoryInterface;

public class Sequence implements MathObject, CreatesPath{

	/** Beziehungs-Container */
	private RelationContainer  relationContainer;

	/** Name der Liste */
	private String name;

	/** Index, mit dem das erste Element der Liste angesprochen wird.
	 *  Werte: 0 oder 1  */
	private  int firstElementIndex;
		
	/**
	 * Index-Variable in der Rekursionsvorschrift
	 */
	private ScalarVariable indexVar;
	
	/** Rekursionsvorschrift */
	//private TermNode recursion;
	private UserFunction recursion;
	
	/** Liste der schon berechneten Folgeglieder. 
	 *  je nach Wert von firstElementIndex:
	 *  0: von Index 0 an stehen die gueltigen Elemente
	 *  1: von Index 1 an stehen die gueltigen Elemente. In diesem 
	 *     Fall steht bei Index 0 der Wert null.*/
	private List elementList;
	
	/** Elemente, die die Basis der Rekursion bilden.
	 */
	private List basisElementList;		
	
	/** Laenge der Liste, die die Basis der Rekursion darstellt.
	 */
	private int basisLength ;
	
	/**
	 * Hoechster Index fuer ein Element aus der BasisElementList
	 */
	private int lastBasisElementIndex;
	
	/** Index des aktuell letzten Elements in der Liste elementList,
	 */
	private int currentLastElementIndex;
	
	private MathObject dummyObject;
	
	private String typeString;
	
	Broker broker;
	
	ViewFactoryInterface viewFactory;
	
	
	/**
	 * Erzeugt eine neue Folge.
	 * 
	 * @param name
	 *            Name der neuen Liste
	 * @param firstElementIndex
	 *            Index, mit dem in dieser Liste das erste Element indiziert
	 *            wird
	 *            
	 * @param basisElementList
	 *            Liste von Elementen zur Initialisierung (Basis der Rekursion).
	 *            Die Elemente stehen vom Index 0 an in dieser Liste.
	 *            Gegebenenfalls kann die Liste leer sein.
	 *            
	 * @param rec
	 *            Rekursionsvorschrift zu Erzeugung der Folge,
	 *            muss in einen auswertbaren Term transformiert werden            
	 * @throws MathException 
	 */
	public Sequence(String name, int firstElementIndex, 
			        ScalarVariable localVar, List basisElements,  
			        //TermNode recursionTerm, 
			        UserFunction recursionFunction, 
			        TermNodeDoubleResult dummyResultTerm, Broker broker, ViewFactoryInterface viewFactory) throws MathException {
		this.name = name;
		this.firstElementIndex = firstElementIndex;
		indexVar = localVar;
		basisElementList = basisElements;
		//recursion = recursionTerm;
		recursion = recursionFunction;
		basisLength = basisElementList.size();
		lastBasisElementIndex = firstElementIndex + basisLength -1;
		elementList = new ArrayList();
		currentLastElementIndex = firstElementIndex - 1;
		dummyObject = new UserFunction(dummyResultTerm, new Variable[0], broker, viewFactory);
		typeString = dummyResultTerm.getTypeStrimng();
		relationContainer = new RelationContainer(this);
		this.broker = broker;	
		this.viewFactory = viewFactory;
	} //Konstruktor 1
	
	public Sequence(int firstElementIndex, 
	        ScalarVariable localVar, List basisElementList,  
	        //TermNode recursionTerm, 
	        UserFunction recursionFunction, 
	        TermNodeDoubleResult dummyResultTerm,Broker broker, ViewFactoryInterface viewFactory) throws MathException {
		this(null,firstElementIndex, localVar, 
				  basisElementList, recursionFunction,dummyResultTerm, broker,  viewFactory);
	} //Konstruktor 2
	
	public Sequence(String name, int firstElementIndex, 
	        ScalarVariable localVar, List basisElementList, 
	        TermNodeDoubleResult dummyResultTerm,Broker broker,ViewFactoryInterface viewFactory) throws MathException {
		this(name,firstElementIndex, localVar, 
				  basisElementList, 
				  //new ScalarVariable(""),
				  new UserFunction("",0,broker, viewFactory),
				  dummyResultTerm,broker, viewFactory );
	} //Konstruktor 3
	
	
	public void setRecursion( 
			//TermNode recursionTerm
			UserFunction recursionFunction){
		recursion = recursionFunction;
		currentLastElementIndex = firstElementIndex - 1;
		name = null;
	}
	
	
	public void redefine ( int firstElementIndex, 
	                      ScalarVariable localVar, List basisElements, 
	                      TermNodeDoubleResult dummyResultTerm,Broker broker)
	throws MathException{
		if ( ((UserFunction)dummyObject).getResultType() != dummyResultTerm.getResultType()){
			throw new MathException("Eine Folge kann nicht durch eine Folge mit einem anderen Rückgabetyp ersetzt werden.");
		}
		
		try {
			getRelationContainer().disconnectSpecificRelations(RelationFactory.getFunction_SubFunction_Relation().getName(),
					                                           RelationInterface.FIRST);
			recursion.getRelationContainer().disconnectSpecificRelations(RelationFactory.getFunction_SubFunction_Relation().getName(),
					                                                     RelationInterface.FIRST );
			getRelationContainer().disconnectSpecificRelations(RelationFactory.getPart_Of_Relation().getName(),
					                                            RelationInterface.SECOND);
		} catch (ObjectNotInRelationException e) {
			ExceptionManager.doError(e);
		}
		
		this.firstElementIndex = firstElementIndex;
		indexVar = localVar;
		basisElementList = basisElements;
		basisLength = basisElementList.size();
		lastBasisElementIndex = firstElementIndex + basisLength -1;
		elementList = new ArrayList();
		currentLastElementIndex = firstElementIndex - 1;
		dummyObject = new UserFunction(dummyResultTerm, new Variable[0], broker, viewFactory);
		typeString = dummyResultTerm.getTypeStrimng();
		relationContainer = new RelationContainer(this);
		this.broker = broker;			
	}
	
	
	public static void register(MathObject l, List partsWithPath, List regList,
			Broker broker) throws BrokerException {
		//Überprüfung, ob alles in Ordnung ist
		if (!(l instanceof Sequence))
			throw new IllegalArgumentException(
					"Das Objekt muß eine Folge sein. Ist aber " + l.getClass());
		if (broker == null)
			throw new NullPointerException(
					"Der Broker ist null. Dies ist hier nicht erlaubt.");
		//Beziehungen erzeugen
		RelationInterface[] ra = new RelationInterface[partsWithPath.size()];
		List parts = new LinkedList();
		int i = 0;
		for (Iterator iter = partsWithPath.iterator(); iter.hasNext(); i++) {
			Object ob = iter.next();
			UserFunction part = (UserFunction) ob;
			if (regList.contains(part))
				ra[i] = RelationFactory.getPart_Of_Relation();
			else
				ra[i] = RelationFactory.getFunction_SubFunction_Relation();
			parts.add(part);
			AbstractAffineObject._setCreationPath(ra[i], part);
		} //for iter
		List relations = Arrays.asList(ra);
		//Definieren
		broker.defineRelations(l, parts, relations, Broker.FIRST_OBJECT);		
	} //register
	
	
	
	public Object getKey() {
			return name;
	} //getKey

	public boolean testSubstitution(MathObject oldObject, Set oldAggregateSet) {
		return true;
	}

	public boolean testDelete() {
		return true;
	}

	public void renew(MathObject source){
		currentLastElementIndex = firstElementIndex - 1;
	}

	public RelationContainer getRelationContainer() {
		return relationContainer;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {
		for (Iterator iter = basisElementList.iterator(); iter.hasNext(); ) {
			((TermNode)(iter.next())).swapLinks(oldObject, newObject);			
		}
		recursion.swapLinks(oldObject, newObject);
	}

	public void prepareDelete() {
		return;
	}
	
	
	/**
	 * Liefert das Folgenglied mit Index index als UserFunction zurück.
	 * @param index
	 * @return
	 * @throws MathException
	 */
	public Object getElementAtIndex(int index) throws MathException {
		if ( index < firstElementIndex ) {
			throw new MathException("Index "+(index)+" ist keine gültige Position in der Folge.");           
		}
		else {
			try {
				if ( index > currentLastElementIndex) {
					if ( currentLastElementIndex <= lastBasisElementIndex ){
						elementList.clear();
						for ( int i = 0; i < firstElementIndex; i++)  elementList.add(null);
						currentLastElementIndex = firstElementIndex - 1;
						for (Iterator iter = basisElementList.iterator(); iter.hasNext(); currentLastElementIndex++) {
							TermNode node = (TermNode)(iter.next());
							elementList.add( new UserFunction(new TermNodeDoubleResult((DoubleResult)node.eval()), new Variable[0], broker, viewFactory));			
						}
					}
					// neue Elemente berechnen
					for( int i = currentLastElementIndex + 1; i <= index; i++ ){
						indexVar.setValue(new ScalarDoubleResult(i));
						recursion.invalidate();
						elementList.add( new UserFunction(new TermNodeDoubleResult((DoubleResult)recursion.eval()), new Variable[0], broker, viewFactory));
						currentLastElementIndex ++;
					}
				}
	            return elementList.get(index);
	        } catch (ArrayIndexOutOfBoundsException e) {
	            throw new MathException("Index "+(index)+" ist keine gültige Position in der Folge.");           
	        }
		}
	}
	
	
	public Object operator_index(Object[] value) throws MathException {
		int index = (int) ((ScalarDoubleResult) value[0]).value;
		return getElementAtIndex(index);		
	} //operator_index
	
	/**
	 * Liefert im Fall einer Folge von Skalaren die Werte der
	 * Folgenglieder bis einschließlich zum Index i als Array von double-Werten.
	 */
	
	public double[] getScalarElementArray( int i ) throws MathException {
		// Prüfen, ob Rückgabetyp ein Skalar ist, andernfalls Ausnahme erzeugen
		if ( ! ( dummyObject instanceof UserFunction 
				 && 
				 ((UserFunction)dummyObject).getResultType() == ScalarType.class ) ) {
			throw new MathException( "Falscher Typ beim Erzeugen einer Zahlenliste aus einer Folge.");
		}
		
		// Array der passenden Größe erzeugen ( in Abhängigkeit von firstElementIndex )
		if ( i < firstElementIndex ) 
			throw new MathException( "Index kleiner als erster gueltiger Index");
		
		double [] valList = new double[ i - firstElementIndex + 1];
		
		// Array mit den entsprechenden Werten füllen.
		for ( int k = firstElementIndex; k <= i; k++ ){
			valList[k - firstElementIndex] =  ((UserFunction)getElementAtIndex(k)).evalScalar();
		}
		
		return valList;
	}
	
	
	public Object operator_startindex(Object[] value) throws MathException {
		return new ScalarDoubleResult(firstElementIndex);
	} //operator_startindex


	
	public PathStep createPathStep(List methods) {
		if (methods.isEmpty())
			return null;

		MethodContext mc = (MethodContext) methods.get(0);
		methods.remove(0);
		try {
			Method m = this.getClass().getMethod(
					"operator_" + mc.getMethodName(),
					new Class[] { Object[].class });

			PathStep ps = AbstractPathStep.createStep(this,mc.getMethodName(),m, mc.getArgs());
			
			CreatesPath nextObj = null;
			if(mc.getMethodName().equals("index")){
			    nextObj = (CreatesPath) dummyObject;
			}else{
			    nextObj = (CreatesPath) ps.getObjectFromMethod(this);
			}//else
			
			
			if (nextObj != null && !(nextObj instanceof Result)) {
				PathStep nextStep = nextObj.createPathStep(methods);
				ps.setNextStep(nextStep);
			}//if

			return ps;
		} catch (Exception e) {
			ExceptionManager.doError("Fehler beim Erzeugen des Methodenpfads ("+name+")",e);
			throw new RuntimeException(e);
		}//catch
	} //createPathStep

	public Class getReturnType(PathStep nextStep) {
		if(nextStep==null)
			return Sequence.class;
		MethodContext mc;
		mc = nextStep.getMethodContext();
		return getReturnType(mc);
	} //getReturnType
	

	
	
	public Class getReturnType(MethodContext mc) {
		try {
			Method m = this.getClass().getMethod(
					"returnType_" + mc.getMethodName(),
					new Class[] { MethodContext.class });
			return (Class) m.invoke(this, new Object[] { mc });
		} catch (Exception e) {
			ExceptionManager.doError("Fehler beim Ermitteln des Rückgabetyps ("+name+").",e);
			throw new RuntimeException(e);
		} //catch
	} //getReturnType
	
	public Class returnType_index(MethodContext mc) throws ClassNotFoundException {
		return Class.forName("math4u2.mathematics.functions.UserFunction");
	} //class returnType_index
	
	public String toString() {
		return name + " := " + toBodyString();
	} //toString
	
	
	public String toBodyString(){
		String s = "<" + typeString + ">folge";
		if ( firstElementIndex == 0) s = s + "0";		
		s = s + "(" + indexVar.getTermString() + ", { " ;
		for (Iterator iter = basisElementList.iterator(); iter.hasNext();) {
			TermNode element = (TermNode)iter.next();
				s += element.getTermString();
			if (iter.hasNext())
				s += ", ";
		} //for iter
		return s + " }," + recursion.getTermString() + ")";
	}   //toBodyString

}
