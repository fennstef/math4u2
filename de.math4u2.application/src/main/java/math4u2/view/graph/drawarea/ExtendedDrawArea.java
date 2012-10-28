package math4u2.view.graph.drawarea;

import java.awt.Color;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import math4u2.application.ShowMeGraph;
import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.reference.AbstractPathStep;
import math4u2.controller.reference.CreatesPath;
import math4u2.controller.reference.MethodContext;
import math4u2.controller.reference.PathStep;
import math4u2.controller.relation.ObjectNotInRelationException;
import math4u2.controller.relation.Relation;
import math4u2.controller.relation.RelationContainer;
import math4u2.controller.relation.RelationFactory;
import math4u2.controller.relation.RelationInterface;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.types.ScalarType;
import math4u2.util.color.ColorUtil;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.NotFoundException;
import math4u2.view.Formatierer;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.SimpleGraphInterface;
import math4u2.view.gui.listview.FunctionScalarDoubleHolder;
import math4u2.view.gui.listview.ViewFactorySingleton;

public class ExtendedDrawArea extends DrawArea implements MathObject,
		CreatesPath {
	protected Broker broker;
	protected RelationContainer relationContainer;
	// Nullstellige Funktionen, liefern die aktuellen Grenzen des
	// Koordinatensystems
	protected UserFunction xMinUsrFunct, xMaxUsrFunct, yMinUsrFunct, yMaxUsrFunct,
			xDistUsrFunct, yDistUsrFunct;

	public ExtendedDrawArea(String name, Broker broker) {
		super(name);
		this.relationContainer = new RelationContainer(this);
		this.broker = broker;
		mouseDisplayListener = new MouseDisplayListener(broker, this);
        addMouseMotionListener(mouseDisplayListener);
	}

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
			PathStep ps = AbstractPathStep.createStep(this, mc.getMethodName(),
					m, mc.getArgs());

			// falls keine weiteren Methoden folgen, aufhören
			// if(methods.isEmpty()) return ps;

			CreatesPath nextObj = (CreatesPath) ps.getObjectFromMethod(this);

			// erfolgt, wenn eine Set-Methode null zurück gibt
			if (nextObj == null)
				return ps;

			PathStep nextStep = nextObj.createPathStep(methods);
			ps.setNextStep(nextStep);

			return ps;
		} catch (Exception e) {
			throw new RuntimeException(
					"Fehler beim Erzeugen des Methodenpfads in " + getName(), e);
		}
	}

	public Class getReturnType(PathStep nextStep) {
		MethodContext mc = nextStep.getMethodContext();
		return getReturnType(mc);
	}

	public Class getReturnType(MethodContext mc) {
		try {
			Method m = this.getClass().getMethod(
					"returnType_" + mc.getMethodName(),
					new Class[] { MethodContext.class });
			return (Class) m.invoke(this, new Object[] { mc });
		} catch (Exception e) {
			ExceptionManager
					.doError("Fehler beim Ermitteln des Rückgabetyps in "
							+ getName(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * @see math4u2.controller.MathObject#getRelationContainer()
	 */
	public RelationContainer getRelationContainer() {
		return relationContainer;
	}

	public void prepareDelete() {
	}

	public void renew(MathObject source) {
		if (makeNoRenew)
			return;
		graphHasChanged();
	}

	/**
	 * @see math4u2.controller.MathObject#swapLinks(math4u2.controller.MathObject,
	 *      math4u2.controller.MathObject)
	 */
	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {
		if (!(newObject instanceof UserFunction))
			return;
		UserFunction newObjectF = (UserFunction) newObject;
		if (oldObject == xMinUsrFunct)
			xMinUsrFunct = newObjectF;
		else if (oldObject == xMaxUsrFunct)
			xMaxUsrFunct = newObjectF;
		else if (oldObject == yMinUsrFunct)
			yMinUsrFunct = newObjectF;
		else if (oldObject == yMaxUsrFunct)
			yMaxUsrFunct = newObjectF;
		else if (oldObject == xDistUsrFunct)
			xDistUsrFunct = newObjectF;
		else if (oldObject == yDistUsrFunct)
			yDistUsrFunct = newObjectF;
	}

	public boolean testDelete() {
		return true;
	}

	public boolean testSubstitution(MathObject a, Set oldAggregateSet) {
		return true;
	}

	@Override
	public String getKey() {
		return name;
	}
	
	public Object getIdentifier(){
		return name;
	}

	public void unregisterParameter() {
		try {
			if (broker.getObject(getKey()) != null)
				broker.deleteObjectByKey(getKey());
		} catch (BrokerException e) {
			ExceptionManager.doError(
					"Fehler beim Löschen der Parameter der Zeichenfläche", e);
		}
		removeAllGraphs();
	}
	
	   /**
     * Verknüpt die DrawArea mit xMaxFunct,yMaxFunct,xMinFunct und yMinFunct
     * 
     * @param p
     *            AffPunkt, der verknüpft werden soll
     * @param broker
     */
    public void register() throws BrokerException {
        if (broker == null)
            throw new NullPointerException(
                    "Der Broker ist null. Dies ist hier nicht erlaubt.");

        removeAllGraphs();
        
        //Funktionen erzeugen
        xMinUsrFunct = new UserFunction(null, -5, broker, ViewFactorySingleton.getInstance());
        xMaxUsrFunct = new UserFunction(null, 5, broker, ViewFactorySingleton.getInstance());
        yMinUsrFunct = new UserFunction(null, -5, broker,ViewFactorySingleton.getInstance());
        yMaxUsrFunct = new UserFunction(null, 5, broker, ViewFactorySingleton.getInstance());
        xDistUsrFunct = new UserFunction(null, getGridPointDist(getXMin(), getXMax(), xFak), broker, ViewFactorySingleton.getInstance());
        yDistUsrFunct = new UserFunction(null, getGridPointDist(getYMin(), getYMax(), yFak), broker, ViewFactorySingleton.getInstance());
        
        xMinFunct = new FunctionScalarDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return xMinUsrFunct;
			}		
		};
		xMaxFunct = new FunctionScalarDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return xMaxUsrFunct;
			}
		};
		yMinFunct = new FunctionScalarDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return yMinUsrFunct;
			}
		};
		yMaxFunct = new FunctionScalarDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return yMaxUsrFunct;
			}
		};
		xDistFunct = new FunctionScalarDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return xDistUsrFunct;
			}
		};
		yDistFunct = new FunctionScalarDoubleHolder() {
			@Override
			public Function getFunction() throws Exception {
				return yDistUsrFunct;
			}
		};
        
        //Beziehungen erzeugen
        RelationInterface[] ra = new RelationInterface[6];
        for (int i = 0; i < ra.length; i++) {
            ra[i] = RelationFactory.getPart_Of_Relation();
        } //for i

        //Teile speichern
        List parts = new LinkedList();
        parts.add(xMinUsrFunct);
        parts.add(xMaxUsrFunct);
        parts.add(yMinUsrFunct);
        parts.add(yMaxUsrFunct);
        parts.add(xDistUsrFunct);
        parts.add(yDistUsrFunct);

        //Beziehungen benennen
        ra[0].setShortName(Relation.FIRST, "xMin");
        ra[1].setShortName(Relation.FIRST, "xMax");
        ra[2].setShortName(Relation.FIRST, "yMin");
        ra[3].setShortName(Relation.FIRST, "yMax");
        ra[4].setShortName(Relation.FIRST, "xDist");
        ra[5].setShortName(Relation.FIRST, "yDist");

        List relations = Arrays.asList(ra);
        //Definieren
        broker.defineRelations(this, parts, relations, Broker.FIRST_OBJECT);

        try {
            broker.publishObject(this, name);
        } catch (ObjectNotInRelationException e) {
            ExceptionManager.doError("Fehler beim Erzeugen der Zeichenfläche",e);
        } catch (BrokerException e) {
            ExceptionManager.doError("Fehler beim Erzeugen der Zeichenfläche",e);
        } 
    } 
    
    public void addGraph(String functionName) {
        GraphInterface graph;
        try {
            UserFunction func = (UserFunction) broker.getObject(functionName);
            
            if(func==null){
            	ExceptionManager.doError("Objekt mit Schlüssel "+functionName+" nicht gefunden");
            	return;
            }//if
            
            for (Iterator iter = graphs.iterator(); iter.hasNext();) {
				GraphInterface gi = (GraphInterface) iter.next();
				if(gi.getModel()==func){
					/*
					System.err.println("Es existiert schon ein Graph für "+func.getKey()
							+" auf der Zeichenfläche "+getName()+".");
					*/
					return;
				}//if
			}//for iter
            
            graph = func.getGraph(this,func);

            List list = new LinkedList();
            list.add(func);

            broker.defineRelations(graph, list, RelationFactory
                    .getView_Function_Relation());
            // graph->mo
            //if(broker.getObject(getKey())==null) this.register(broker);
            list = new LinkedList();
            list.add(graph);
            broker.defineRelations(this, list, RelationFactory
                    .getCanvas_View_Relation());
            // da->graph
            addGraph(graph);

            //Erneuern der Sichtbarkeitsbuttons
            broker.propagateChange(func);
        } catch (BrokerException e) {
            ExceptionManager.doError("Fehler beim Hinzufügen des Graphen der Funktion "+functionName ,e);
        } //catch
    } //addGraph

	/**
	 * Diese Methoden werden von Außen aufgerufen z.B. p.x, p.y usw.
	 * 
	 * Anfang --------------------------------------------------------
	 */

	public Object operator_xMin(Object[] values) {
		return xMinUsrFunct;
	} // operator_xMin

	public Object operator_set_xMin(Object[] values) {
		xMinUsrFunct.setFunction(((UserFunction) values[0]).getFunction());
		try {
			broker.propagateChange(xMinUsrFunct);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler beim Setzen des Parameters xMin",
					e);
		}
		return null;
	} // operator_set_xMin

	public Class returnType_xMin(MethodContext mc) {
		return ScalarType.class;
	} // returnType_xMin

	// -------------------------------------------------------

	public Object operator_yMin(Object[] values) {
		return yMinUsrFunct;
	} // operator_yMin

	public Object operator_set_yMin(Object[] values) {
		yMinUsrFunct.setFunction(((UserFunction) values[0]).getFunction());
		try {
			broker.propagateChange(yMinUsrFunct);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler beim Setzen des Parameters yMin",
					e);
		}
		return null;
	} // operator_set_yMin

	public Class returnType_yMin(MethodContext mc) {
		return ScalarType.class;
	} // returnType_yMin

	// --------------------------------------------------------

	public Object operator_xMax(Object[] values) {
		return xMaxUsrFunct;
	} // operator_xMax

	public Object operator_xDist(Object[] values) {
		return xDistUsrFunct;
	} // operator_xDist

	public Object operator_set_xMax(Object[] values) {
		xMaxUsrFunct.setFunction(((UserFunction) values[0]).getFunction());
		try {
			broker.propagateChange(xMaxUsrFunct);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler beim Setzen des Parameters xMax",
					e);
		}
		return null;
	} // operator_set_xMax

	public Class returnType_xMax(MethodContext mc) {
		return ScalarType.class;
	} // returnType_xmax

	public Class returnType_xDist(MethodContext mc) {
		return ScalarType.class;
	} // returnType_xDist

	// -------------------------------------------------------

	public Object operator_yMax(Object[] values) {
		return yMaxUsrFunct;
	} // operator_ymax

	public Object operator_yDist(Object[] values) {
		return yDistUsrFunct;
	} // operator_yDist

	public Object operator_set_yMax(Object[] values) {
		yMaxUsrFunct.setFunction(((UserFunction) values[0]).getFunction());
		try {
			broker.propagateChange(yMaxUsrFunct);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler beim Setzen des Parameters yMax",
					e);
		}
		return null;
	} // operator_set_yMax

	public Class returnType_yMax(MethodContext mc) {
		return ScalarType.class;
	} // returnType_ymax

	public Class returnType_yDist(MethodContext mc) {
		return ScalarType.class;
	} // returnType_yDist

	/**
	 * Ende ----------------------------------------------------------
	 */
	
	  /**
     * Registierung eines Graphen mit der Zeichenfläche und dem Objekt
     * 
     * @param mo
     *            Objekt
     * @param gr
     *            Graph
     * @param da
     *            Zeichenfläche
     * @param broker
     */
    public static void register(MathObject mo, GraphInterface gr,
            DrawAreaInterface da, Broker broker) {
        try {
            RelationInterface ri = RelationFactory.getCanvas_View_Relation();
            List list = new LinkedList();
            list.add(da);
            broker.defineRelations(gr, list, ri, Broker.SECOND_OBJECT);
            ri = RelationFactory.getView_Function_Relation();
            list = new LinkedList();
            list.add(mo);
            broker.defineRelations(gr, list, ri);
            da.addGraph(gr);
        } catch (BrokerException e) {
            ExceptionManager.doError("Fehler beim Hinzufügen des neuen Graphen "+gr.getKey()+" zur Zeichenfläche "+da.getName(),e);
        } //catch
    } //register
    
    public static Object getDummyObject(){
    	return DrawAreasManager.getAllDrawAreas().iterator().next();
    }//getDummyObject
    
	public void zoom1To1(HashMap params) {
		String active = null;
		try {
			active = ShowMeGraph.getFullKeyAndNormalize("a".length(), "active", ShowMeGraph.REQUIRED, params);
		} catch (NotFoundException e) {
			ExceptionManager.doError("Attrubzt 'active' wurde nicht gefunden.",e);
		}//catch
			activate1To1Zoom("true".equalsIgnoreCase(active));
	} 
	
    public void addGraph(HashMap params) {
        try {
            String fn = ShowMeGraph.getFullKeyAndNormalize("n".length(), "name",
                    ShowMeGraph.REQUIRED, params);
            String[] fns = fn.split(",");

            setMakeNoRenew(true);
            for (int i = 0; i < fns.length; i++) {
                addGraph(fns[i].trim());
            }
            setMakeNoRenew(false);
            graphHasChanged();
        } catch (NotFoundException e) {
            ExceptionManager.doError("Das Attribut 'name' wurde nicht gefunden.",e);
        }
    } 

    public void coordinateSystem(HashMap params) {
		double xMin, xMax, yMin, yMax;
		try {
			xMin = Formatierer.parse2Number(
					ShowMeGraph.getFullKeyAndNormalize("xmi".length(), "xmin", ShowMeGraph.REQUIRED, params))
					.doubleValue();
			xMax = Formatierer.parse2Number(
					ShowMeGraph.getFullKeyAndNormalize("xma".length(), "xmax", ShowMeGraph.REQUIRED, params))
					.doubleValue();
			yMin = Formatierer.parse2Number(
					ShowMeGraph.getFullKeyAndNormalize("ymi".length(), "ymin", ShowMeGraph.REQUIRED, params))
					.doubleValue();
			yMax = Formatierer.parse2Number(
					ShowMeGraph.getFullKeyAndNormalize("yma".length(), "ymax", ShowMeGraph.REQUIRED, params))
					.doubleValue();
			coordinateSystem(xMin, xMax,yMin, yMax);
		} catch (NotFoundException e) {
			ExceptionManager.doError("Fehler es werden die Attribute xmin, xmax, ymin und ymax erwartet.",e);
		}
	}
    
    public void deleteGraph(HashMap params) {
        try {
            String fn = ShowMeGraph.getFullKeyAndNormalize("n".length(), "name",
                    ShowMeGraph.REQUIRED, params);
            String[] fns = fn.split(",");

            for (int i = 0; i < fns.length; i++) {
                deleteGraph(fns[i].trim());
            }
        } catch (NotFoundException e) {
            ExceptionManager.doError("Das Attribut 'name' wird erwartet.",e);
        }
    } 
    
    public void set(HashMap params){
    	try {
			String gridColor = ShowMeGraph.getFullKeyAndNormalize("g".length(),
					"gridcolor", ShowMeGraph.OPTIONAL, params);
			String axisColor = ShowMeGraph.getFullKeyAndNormalize("a".length(),
					"axiscolor", ShowMeGraph.OPTIONAL, params);
			String backgroundColor = ShowMeGraph.getFullKeyAndNormalize("b".length(),
					"backgroundcolor", ShowMeGraph.OPTIONAL, params);
			String mesh = ShowMeGraph.getFullKeyAndNormalize("m".length(),
					"mesh", ShowMeGraph.OPTIONAL, params);
			if (gridColor != null) {
				Color c = ColorUtil.parseColor(gridColor);
				setGridColor(c);
			}//if	
			if (axisColor != null) {
				Color c = ColorUtil.parseColor(axisColor);
				setAxisColor(c);
			}//if	
			if (backgroundColor != null) {
				Color c = ColorUtil.parseColor(backgroundColor);
				setBackground(c);
			}//if	
			if (mesh != null){
				double val = 1.0;
				int s = Integer.parseInt(mesh);
				if(s<=0) throw new IllegalArgumentException("mesh="+mesh+" ist ungültig. Es werden nur Werte 1-5 angenommen.");
				if(s==1) val=.25;
				if(s==2) val=0.5;
				if(s==3) val=1.0;
				if(s==4) val=2.0;
				if(s==5) val=2.5;
				if(s>=6) throw new IllegalArgumentException("mesh="+mesh+" ist ungültig. Es werden nur Werte 1-5 angenommen.");
				setGridMesh(val, val);					
			}//if			
		} catch (NotFoundException e) {
			ExceptionManager.doError("Attribut nicht gefunden.",e);
		} catch (IllegalArgumentException e) {
			ExceptionManager.doError("Fehler bei 'set'.",e);			
		}//catch 
		graphHasChanged();
    }
    
    public void setTitle(HashMap params) {
        try {
            String title = ShowMeGraph.getFullKeyAndNormalize("t".length(), "title",
                    ShowMeGraph.REQUIRED, params);
            if(title.startsWith("\"") && title.endsWith("\"")){
                setTitle(title.substring(1,title.length()-1));    
            }else{
                ExceptionManager.doError("Titel kann nur in Hochkommas (\") gesetzt werden. (title="+title+")");
            }            
        } catch (NotFoundException e) {
            ExceptionManager.doError("Das Attribut 'title' wurde nicht gefunden.",e);
        }//catch
    }
    
    void fireChangeEvent() {
    	super.fireChangeEvent();
		graphHasChanged();
	}
}
