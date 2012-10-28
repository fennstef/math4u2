package math4u2.mathematics.affine;

import java.awt.Color;
import java.lang.reflect.Method;
import java.util.List;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.reference.AbstractPathStep;
import math4u2.controller.reference.CreatesPath;
import math4u2.controller.reference.MethodContext;
import math4u2.controller.reference.PathReference;
import math4u2.controller.reference.PathStep;
import math4u2.controller.relation.RelationContainer;
import math4u2.controller.relation.RelationInterface;
import math4u2.mathematics.collection.HasTypeString;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.HasGraph;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.gui.listview.ListViewItemInterface;
import math4u2.view.gui.listview.ViewFactoryInterface;

public abstract class AbstractAffineObject implements MathObject, HasGraph,
        HasCompleteView, CreatesPath, HasTypeString{

    /** Referenz zum Broker */
    protected Broker broker;
    
    /** Referenz zur ViewFactory */
    protected ViewFactoryInterface viewFactory;

    /** Referenz zum Container der Beziehungen */
    private RelationContainer relationContainer;

    /** Name des grafischen Objekts */
    protected String name;

    /** Farbe des grafischen Objekts */
    private Color color;

    /** Linienstil des grafischen Objekts */
    private int lineStyle;

    /** Sichtbarkeit des grafischen Objekts */
    private boolean visible;

    /** Ist das Objekt modifizierbar? */
	private boolean freeze;
	
	/** Typ des Objekts z.B. punkt */
	protected String type;

    /**
     * Hilfsmethod um den CreationPath zu setzten
     * 
     * @param ri
     *            Beziehung, die den CreationPath bekommen soll
     * @param mop
     *            Unterobjekt, welches den CreationPath besitzt
     */
    public static void _setCreationPath(RelationInterface ri, UserFunction f) {
        ri.setCreationPath(f.getTermString().split("."));
    }//_setCreationPath

	abstract public ListViewItemInterface getCompleteView(UserFunction f,ListViewInterface alv,
            Broker broker);

    abstract public void swapLinks(MathObject oldObject, MathObject newObject)
            throws Exception;

    public AbstractAffineObject(String type, String name, Color color, int lineStyle,
            boolean visible, Broker broker, ViewFactoryInterface viewFactory) {
        this.type = type;
        this.relationContainer = new RelationContainer(this);
        this.broker = broker;
        if (broker == null)
            throw new NullPointerException("Broker nicht vorhanden");
        this.name = name;
        this.color = color;
        this.lineStyle = lineStyle;
        this.visible = visible;
        if (viewFactory == null)
            throw new NullPointerException("ViewFactory nicht vorhanden");
        this.viewFactory = viewFactory;
    } //Konstruktor

    public void setColor(Color c) {
        color = c;
        try {
            if (broker != null)
                broker.propagateChange(this);
        } catch (BrokerException e) {
            ExceptionManager.doError("Fehler beim Erneuern durch das Ändern der Farbe ("+getKey()+").",e);
        } //catch
    } //setColor

    public Color getColor() {
        return color;
    }

    public void setLineStyle(int lineStyle) {
        this.lineStyle = lineStyle;
        try {
            if (broker != null)
                broker.propagateChange(this);
        } catch (BrokerException e) {
            ExceptionManager.doError("Fehler beim Erneuern durch das Ändern der Linienstils ("+getKey()+").",e);
        } //catch
    } //setLineStyle

    public int getLineStyle() {
        return lineStyle;
    }//getLineStyle

    public boolean isVisible() {
        return visible;
    }//isVisible

    public void setVisible(boolean visible) {
        this.visible = visible;
    }//setVisible
    
	public boolean isFreeze() {
		return freeze;
	}//isFreeze
	
	public void setFreeze(boolean freeze) {
		this.freeze = freeze;
	}//setFreeze
	
    public Object getIdentifier() {
        return name;
    }
    
    public String getKey(){
    	return name;
    }

    public String getName() {
        return name;
    }//getName
    
    public String getTypeString() {
        return type;
    }//getTypeString
    
    public abstract GraphInterface getGraph(DrawAreaInterface da, UserFunction f);

    public boolean hasCurrentObjectGraph() {
        return true;
    }//hasCurrentObjectGraph

    public boolean testSubstitution(MathObject oldObject,
            java.util.Set oldAggregateSet) {
        if (oldObject.getClass().toString().equals(this.getClass().toString()))
            return true;
        return false;
    } //testSubstitution

    public boolean testDelete() {
        return true;
    } //testDelete

    public void prepareDelete() {
    }

    protected void propagateChange() {
        try {
            broker.propagateChange(this);
        } catch (BrokerException e) {
            ExceptionManager.doError("Fehler beim Erneuern ("+getKey()+").",e);
        } //catch
    } //propagateChange

    public abstract String bodyToString();

    public String toString() {
        return name + ":=" + bodyToString();
    } //toString

    public void renew(MathObject source) {
    }//renew

    public RelationContainer getRelationContainer() {
        return relationContainer;
    } //getRelationContainer

    public void setName(String name) {
        this.name = name;
    } //setName

    public String getRepresentation(MathObject mo) {
        String rep = null;
        if (mo instanceof UserFunction) {
            TermNode tm = ((UserFunction) mo).getFunction();
            rep = ((UserFunction) mo).getTermString(this);
            if (rep.equals("null") && (tm instanceof PathReference)) {
                try {
                    rep = ((UserFunction) ((PathReference) tm).eval())
                            .getTermString();
                } catch (MathException e) {
                    ExceptionManager.doError("Fehler beim Holen des gekapselten Objekts ("+getKey()+").",e);
                }//catch
            }//if
            return rep;
        }
        if (mo.getRelationContainer().getCreationPath() != null)
            rep = mo.getRelationContainer().getCreationPath().toString();
        if (rep != null)
            return rep;

        if (mo instanceof AbstractAffineObject)
            return ((AbstractAffineObject) mo).bodyToString();
        else
            return (String) mo.toString();
    }//getRepresentation

    public PathStep createPathStep(List methods) {
        if (methods.isEmpty())
            return null;

        MethodContext mc = (MethodContext) methods.get(0);
        methods.remove(0);
        try {
            Method m = this.getClass().getMethod(
                    "operator_" + mc.getMethodName(),
                    new Class[] { Object[].class });
            
            // es ist noch nicht der nächste Schritt eingtragen worden
            PathStep ps = AbstractPathStep.createStep(this, mc.getMethodName(), m, mc.getArgs()); 

            //falls keine weiteren Methoden folgen, aufhören
            //if(methods.isEmpty()) return ps;

            CreatesPath nextObj = (CreatesPath) ps.getObjectFromMethod(this);

            //erfolgt, wenn eine Set-Methode null zurück gibt
            if (nextObj == null)
                return ps;

            PathStep nextStep = nextObj.createPathStep(methods);
            ps.setNextStep(nextStep);

            return ps;
        } catch (Exception e) {
        	throw new RuntimeException("Fehler beim Aufbau des Methodenpfads ("+getKey()+").",e);
        }//catch
    }//createPathStep

    public Class getReturnType(PathStep nextStep) {
        if(nextStep==null)
            return this.getClass();
        MethodContext mc = nextStep.getMethodContext();
        return getReturnType(mc);
    } //getReturnType

    public Class getReturnType(MethodContext mc) {
        try {
            Method m = this.getClass().getMethod(
                    "returnType_" + mc.getMethodName(),
                    new Class[] { MethodContext.class });
            return (Class) m.invoke(this, new Object[] { mc });
        } catch (Exception e) {
            ExceptionManager.doError("Fehler beim Bestimmen des Rückgabetyps ("+getKey()+").",e);
            throw new RuntimeException(e);
        }//catch
    }//getReturnType

	public void applyProperties(HasGraph oldObject) {
      setColor(oldObject.getColor());
      setLineStyle(oldObject.getLineStyle());
      setVisible(oldObject.isVisible());
	}

} //class AbstractAffineObject
