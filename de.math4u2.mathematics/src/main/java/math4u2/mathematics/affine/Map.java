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
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.types.VectorType;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.gui.listview.ListViewItemInterface;
import math4u2.view.gui.listview.ViewFactoryInterface;

/**
 * Objekt zur farblichen Darstellung von Funktionswerten  einer zweistelligen 
 * skalaren Funktion mapFunc(x,y). Dieses Objekt verwaltet die zugehoerigen 
 * Funktionen und deren Beziehungen, das eigentliche Zeichnen wird von Objekten der Klasse 
 * MapGraph uebernommen.
 * @author Dr. Weiss
 *
 */



public class Map extends AbstractAffineObject {

	protected UserFunction mapFunc,  // Funktion mapFunc(x,y), liefert das Hoehenprofil
	                     bandVector; // nulltellige Funktion, liefert die kleinste dargestellte Hoehe
				
    protected double contourDelta = 0.02;

    /**
     * 
     * @param name      Bezeichner der Karte
     * @param mapFunc   Zweistellige skalare Funktion mapFunc(x,y), liefert das Hoehenprofil
     * @param bandVector  Nullstellige vektorwertige Funktion, liefert als erstes und letztes 
     *                    Element die untere und obere Grenze 
     *                    fuer den dargestellten Wertebereich und mit weiteren Elementen 
     *                    gegebenenfalls die Lage der Hoehenlinien.
     * @param isVisible
     * @param broker
     */

	public Map( String name, 
			    MathObject mapFunc, MathObject bandVector,   
				boolean isVisible, Broker broker, ViewFactoryInterface viewFactory) {
		super("karte",null, new Color(0.0f,0.0f,0.0f,0.88f), 0, isVisible, broker, viewFactory);
		this.mapFunc = (UserFunction) mapFunc;
		this.bandVector = (UserFunction) bandVector;
	} //Konstruktor

	
	/*
	 * Konstruktor für eine anonyme Karte
	 */	
	public Map( MathObject mapFunc, MathObject bandVector, Broker broker, ViewFactoryInterface viewFactory) {
		this("", mapFunc, bandVector, true, broker, viewFactory);
	} //Konstruktor
	
	
	
	public UserFunction getMapFunc() {
		return mapFunc;
	}
	
	public UserFunction getBandVecor() {
		return bandVector;
	}

	/**
	 * Verknüpt die Landkarte b mit ihren Beziehungspartnern (parts)
	 * 
	 * @param b
	 *            Landkarte, die verknüpft werden soll.
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
		if (!(b instanceof Map))
			throw new IllegalArgumentException(
					"Das Objekt muß eie Landkarte sein. Ist aber "
							+ b.getClass());
		if (partsWithPath.size() != 2)
			throw new IllegalArgumentException(
					"Die Teile-Liste muß genau die Länge 2 haben.");
		if (broker == null)
			throw new NullPointerException(
					"Der Broker ist null. Dies ist hier nicht erlaubt.");
		
		//Beziehungen erzeugen
		String[] sa = new String[] { "mapFunction", "bandVector"};
        RelationInterface[] ra = new RelationInterface[sa.length];
        UserFunction[] partList = new UserFunction[sa.length];
        List parts = new LinkedList();
        for (int i = 0; i < sa.length; i++) {
            partList[i] = (UserFunction) partsWithPath.get(i);

            if (regList.contains(partList[i]))
                ra[i] = RelationFactory.getPart_Of_Relation();
            else
                ra[i] = RelationFactory.getFunction_SubFunction_Relation();

            parts.add(partList[i]);

            //Beziehungen bennenen
            ra[i].setShortName(Relation.FIRST, sa[i]);
            _setCreationPath(ra[i], partList[i]);
        }//for
		List relations = Arrays.asList(ra);

		//Definieren lassen
		broker.defineRelations(b, parts, relations, Broker.FIRST_OBJECT);
	} //register

	public String bodyToString() {
		String mapFuncStr = mapFunc.getName();
		String bandVectorStr = getRepresentation(bandVector);
		return type + "(" + mapFuncStr + "," + bandVectorStr + ")";
	} //bodyToString

	public GraphInterface getGraph(DrawAreaInterface da, UserFunction f) {
		return viewFactory.getGraphFactory().createMapGraph(da,f);
	} //getGraph


	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {

		if (mapFunc == oldObject) {
			mapFunc = (UserFunction) newObject;
		} else {
			mapFunc.swapLinks(oldObject, newObject);
		}
		if (bandVector == oldObject) {
			bandVector = (UserFunction) newObject;
		} else {
			bandVector.swapLinks(oldObject, newObject);
		}
	} //swapLinks	

	public ListViewItemInterface getCompleteView(UserFunction f,
			ListViewInterface alv, Broker broker) {
		return viewFactory.createCompleteMapViewItem(f,alv, broker);
	}//getCompleteView
    
    public UserFunction getBandVector() {
        return bandVector;
    }//getLowLimit
    
	public double getContourDelta() {
		return contourDelta;
	}//getContourDelta
	
	public void setContourDelta(double contourDelta) {
		this.contourDelta = contourDelta;
		try {
			broker.propagateChange(this);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler beim Erneuern ("+getKey()+").",e);
		}
	}//setContourDelta    

    /**
	 * Diese Methoden werden von Außen aufgerufen z.B. p.x, p.y usw.
	 * 
	 * Anfang --------------------------------------------------------
	 */

	public Object operator_mapFunction(Object[] values) {
		return mapFunc;
	} //operator_mapFunction

	public Object operator_set_mapFunction(Object[] values) {
		mapFunc.setFunction(((UserFunction) values[0]).getFunction());
		try {
			broker.propagateChange(mapFunc);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler beim Erneuern durch das Setzten der Kartenfunktion ("+getKey()+").",e);
		}
		return null;
	} //operator_mapFunction

	public Class returnType_mapFunction(MethodContext mc) {
		return UserFunction.class;
	} //returnType_mapFuncion

	//-------------------------------------------------------

	public Object operator_bandVector(Object[] values) {
		return bandVector;
	} //operator_bandVector

	public Object operator_set_bandVector(Object[] values) {
		bandVector.setFunction(((UserFunction) values[0]).getFunction());
		try {
			broker.propagateChange(bandVector);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler beim Erneuern durch das Setzen der Höhenlinien ("+getKey()+").",e);
		}
		return null;
	} //operator_bandVector

	public Class returnType_bandVector(MethodContext mc) {
		return VectorType.class;
	} //returnType_bandVector	

	/**
	 * Ende ----------------------------------------------------------
	 */
    
} //class Map
