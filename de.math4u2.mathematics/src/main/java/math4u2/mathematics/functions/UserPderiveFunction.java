package math4u2.mathematics.functions;

import java.lang.reflect.Method;

import math4u2.controller.Broker;
import math4u2.controller.MathObject;
import math4u2.mathematics.affine.HasCompleteView;
import math4u2.mathematics.collection.HasTypeString;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.Variable;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.HasGraph;
import math4u2.view.gui.listview.ViewFactoryInterface;

/**
 * Repraesentiert eine (selbstdefinierte) partielle Ableitung.
 */

public class UserPderiveFunction extends UserFunction implements HasCompleteView, HasTypeString, HasGraph {
    

    /*
     * Variablen, nach denen abgeleitet wird.
     */
    Variable[] deriveVars = null;

    /*
     * Funktionsterm, der abgeleitet werden soll
     */
    TermNode toBeDerivedTerm = null;



    /***************************************************************************
     * Konstruktor für eine partielle Ableitung
     */
    public UserPderiveFunction(String name, TermNode toBeDerivedTerm,
            Variable[] variables, Variable[] deriveVars, Broker broker, ViewFactoryInterface viewFactory)
            throws Exception {

        super(name, toBeDerivedTerm, variables, broker, viewFactory);
        
        this.deriveVars = deriveVars;
        this.toBeDerivedTerm = toBeDerivedTerm;

        makePartialDerive();
    }

    

    /***************************************************************************
     * Konstruktor für eine partielle Ableitung ohne Namen
     */
    public UserPderiveFunction(TermNode toBeDerivedTerm, Variable[] variables,
            Variable[] deriveVars, Broker broker, ViewFactoryInterface viewFactory) throws Exception {
        this("", toBeDerivedTerm, variables, deriveVars, broker, viewFactory);
        name = null;
    }

    

    /***************************************************************************
     * Berechnet ausgehend von toBeDerivedTerm und den in deriveVars gegebenen
     * Variablen die partielle Ableitung in functionTerm .
     */
    private void makePartialDerive() throws MathException {
        try {
            functionTerm = toBeDerivedTerm.getClone(variables, variables);

            for (int i = 0; i < deriveVars.length; i++) {
                functionTerm = functionTerm.derive(deriveVars[i]);
                functionTerm = functionTerm.simplify();
                functionTerm = functionTerm.evalNum();
            }
        } catch (Exception e) {
            throw new MathException(e);
        }
    }

 
 

    /**
     * Ersetzt im aktuellen Objekt jede referenz auf oldObject durch eine
     * Referenz auf newObject.
     * 
     * @param oldObject
     *            zunaechst referenziertes Objekt
     * @param newObject
     *            statt oldObjekt zu referenzierendes Objekt
     * @throws MathException
     */
    public void swapLinks(MathObject oldObject, MathObject newObject)
            throws MathException {

           toBeDerivedTerm.swapLinks(oldObject, newObject);
           makePartialDerive();
           maxValidOrder = -1; // erzwingt, dass Ableitungen neu gerechnet werden

    }

    
    public void renew(MathObject source) {
        isValid = false;
            try {
                /*
                 * Der Funktionsterm muss in folgenden Situationen neu berechnet
                 * werden: - Die Ursache der Veraenderung (source) ist eine ein-
                 * oder mehrstellige benutzerdefinierte Funktion, weil sich dies
                 * im Term der Ableitung auswirkt.
                 */
            	maxValidOrder = -1;
            	if ((source instanceof UserFunction)
                        && ((UserFunction) source).getArity() != 0) {
                    makePartialDerive();
                    broker.propagateChange(this);
                }
            } catch (Exception E) {
            }
    }

    

    public String buildTermString(String[] argStrings, String name) {
        
            String ret = "pderive(vars(";
            if (variables.length > 0) {
                ret = ret + variables[0].getTermString();
                for (int i = 1; i < variables.length; i++) {
                    ret = ret + "," + variables[i].getTermString();
                }
            }

            ret = ret + ")," + toBeDerivedTerm.getTermString() + ","
                    + deriveVars[0].getTermString();

            for (int i = 1; i < deriveVars.length; i++) {
                ret = ret + "," + deriveVars[i].getTermString();
            }

            ret = ret + ")(";

            for (int i = 0; i < argStrings.length; i++) {
                ret += argStrings[i];
                if (argStrings.length - 1 != i)
                    ret += ", ";
            }
            return ret + ")";
    } //buildTermString

   
    
    

   
    
    /***************************************************************************
     * Setzen des Namens
     */    
    
    
    /***************************************************************************
     * Transferiert in die Hülle von oldObject den Funktionsterm, die Variablen
     * und alle anderen Bestandteile der aktuellen Funktion.
     * 
     * @param oldObject
     *            Funktion, deren Funktionalität ersetzt werden soll
     * @return oldObject, mit neuem Inhalt versehen.
     */
    public MathObject constructSubstitution(MathObject oldObject) {
        ((UserFunction) oldObject).setFunction(this.getFunction());
        ((UserFunction) oldObject).setVariables(this.getVariables());
        ((UserPderiveFunction) oldObject).deriveVars = this.deriveVars;
        ((UserPderiveFunction) oldObject).toBeDerivedTerm = this.toBeDerivedTerm;
        try {
            broker.propagateChange(oldObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        isValid = false;
        return oldObject;
    }

    public Class getVariableType(int pos) throws MathException {
        if (0 <= pos && pos < variables.length)
            return variables[pos].getResultType();
        else
            throw new MathException("Funktion " + name
                    + " hat keine Variable an der Stelle " + pos);
    }

    
    
    public String getTypeString(){        
        if(isEncapsulated()){
            try {
                Object obj = eval();
                if(obj instanceof HasTypeString){
                    return ((HasTypeString)obj).getTypeString();
                }
            } catch (MathException e) {
                ExceptionManager.doError("Fehler bei der Erstellung der Zeichendarstellung ("+name+").",e);
            }
        }
        
        int arity = getArity();
        String typeString = "";
        String[] vars = getVariableNames();

        for (int i = 0; i < arity; i++) {
            try {
                Class type = getVariableType(i);
                Method m = type.getMethod("getTypeString", new Class[0]);
                typeString += (String) m.invoke(null, (Object[])null);
            } catch (Exception e) {
                ExceptionManager.doError("Fehler bei der Erstellung der Zeichendarstellung ("+name+").",e);
            }//catch

            if (i != 0)
                typeString += ",";
            typeString += vars[i];
        }//for i
        if (getArity() != 0)
            typeString = "(" + typeString + ")";        
        return "funktion"+typeString;
    }//getTypeString

    


    /***************************************************************************
     * Erzeugt Definitions-String wie "f(x,x) := sin(x)+y^2" oder
     * "derive(sin(x)*y,y,2)" .
     * 
     * @return Definitions-String
     */

    public String toString() {    	
            return "";
    }

    /***************************************************************************
     * Erzeugt eine Kopie der aktuellen Funktion. Die Variablen der Kopie haben
     * die gleichen Namen wie die Variablen der aktuellen Funktion. Die Kopie
     * erhält den gleichen Namen wie die aktuelle Funktion.
     * 
     * @return Kopie
     * @throws Exception
     */

    public UserFunction cloneFunction() throws Exception {
        // ScalarVariable klonen
        Variable[] newVars = new Variable[variables.length];
        Variable[] newDeriveVars = new Variable[deriveVars.length];
        
        for (int i = 0; i < variables.length; i++) {
            newVars[i] = variables[i].getClone();
            
            for (int id = 0; id < newDeriveVars.length; id++) {
                if (deriveVars[id].equals(variables[i])) {
                    newDeriveVars[id] = newVars[i];
                    break;
                }
            }
            
        }
            // Term der abzuleitenden Funktion klonen
            TermNode newtoBeDerivedTerm = toBeDerivedTerm.getClone(variables,
                    newVars);         
            
            return new UserPderiveFunction(this.getName(),
                    newtoBeDerivedTerm, newVars, newDeriveVars, broker, viewFactory);
    } // cloneFunction

    /***************************************************************************
     * Vereinfacht die Funktion durch Vereinfachung des Funktionsterms.
     * 
     * @throws Exception
     */

    public void simplify() throws Exception {
        functionTerm = functionTerm.simplify();
    } // simplify

} //UserPderiveFunction
