package math4u2.application;

import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.relation.ObjectNotInRelationException;
import math4u2.mathematics.affine.HasCompleteView;
import math4u2.mathematics.affine.Map;
import math4u2.mathematics.collection.MathList;
import math4u2.mathematics.functions.Function;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.util.color.ColorUtil;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.NotFoundException;
import math4u2.util.exception.parser.ParseException;
import math4u2.view.Formatierer;
import math4u2.view.graph.HasGraph;
import math4u2.view.graph.MapGraph;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.graph.drawarea.DrawAreasManager;
import math4u2.view.graph.drawarea.ExtendedDrawArea;
import math4u2.view.gui.Math4U2Win;
import math4u2.view.gui.component.ListCheckBox;
import math4u2.view.gui.component.ParameterComponent;
import math4u2.view.gui.listview.ListBox;
import math4u2.view.gui.listview.ViewFactorySingleton;
import math4u2.view.gui.listview.complete.CompleteFunctionViewItem;
import math4u2.view.gui.listview.complete.CompleteViewBox;

/**
 * Repräsentiert die Schnittstelle zu den XML-Aufrufen.
 * 
 * @author Fenn Stefan
 */
public class ShowMeGraph {

	public final static int REQUIRED = 0, OPTIONAL = 1;

	private Broker broker = Math4U2Win.getInstance().getBroker();

	private ParameterChanger paramChanger = new ParameterChanger();

	private ListBox listBox;

	private CompleteViewBox completeViewBox;
	
	private boolean defaultsHide = false;
	
	static{
		Function.setViewFactory(ViewFactorySingleton.getInstance());
	}

	public ListBox getListBox() {
		if (listBox == null) {
			try {
				listBox = (ListBox) broker.getObject("ListBox");
				return listBox;
			} catch (BrokerException e) {
				ExceptionManager.doError("Das Objekt ListBox wurde nicht gefunden",e);
				return null;
			}//catch
		} else
			return listBox;
	} //getListBox

	public CompleteViewBox getCompleteViewBox() {
		if (completeViewBox == null) {
			try {
				completeViewBox = (CompleteViewBox) broker
						.getObject("CompleteView");
				return completeViewBox;
			} catch (BrokerException e) {
				ExceptionManager.doError("Das Objekt CompleteView wurde nicht gefunden",e);
				return null;
			}//catch
		} else
			return completeViewBox;
	} //getListBox

	/**
	 * Findet in params einen Key, der aus einen Teil von fullKey besteht und
	 * mindestens die Länge shortestKeyLen besitzt. Der Key wird dann entfernt und
	 * als fullKey eingetragen.
	 * 
	 * @throws NotFoundException
	 */
	public static String getFullKeyAndNormalize(int shortestKeyLen, String fullKey,
			int type, HashMap params) throws NotFoundException{
		List keys = new LinkedList();
		List values = new LinkedList();
		List deleteKeyList = new LinkedList();
		for (Iterator iter = params.keySet().iterator(); iter.hasNext();) {
			String origKey = ((String) iter.next()); 
			String key = origKey.toLowerCase();
			if (key.length() < shortestKeyLen)
				continue;
			if (key.length() > fullKey.length())
				continue;
			if (fullKey.startsWith(key.substring(0,
					shortestKeyLen))) {
				keys.add(key);
				values.add(params.get(origKey));
				if(!fullKey.equals(origKey))
					deleteKeyList.add(origKey);
			}//if
		}//for iter
		
		params.keySet().removeAll(deleteKeyList);
		
		if (values.size() == 0) {
			if (type == REQUIRED)
				throw new NotFoundException("Schlüsselwort '" + fullKey
						+ "' nicht gefunden");
			else
				return null;
		} else if (values.size() == 1) {
			return ((String) values.get(0)).trim();
		} else
			throw new NotFoundException("Nicht eindeutige Angabe: " + keys
					+ " für Schlüsselwort '" + fullKey + "'");
	}//getFullKeyName

	public ParameterComponent getParameterSlider(String name) {
		try {
			Object o = getCompleteViewBox().getRelationContainer()
					.getObjectByName("$" + name);
			if (!(o instanceof CompleteFunctionViewItem)) {
				return null;
			}//if

			ParameterComponent ps = ((CompleteFunctionViewItem) o)
					.getParameterSlider();
			if (ps == null) {
				return null;
			}//if
			return ps;
		} catch (ObjectNotInRelationException e) {
			ExceptionManager.doError("Das Objekt "+(getCompleteViewBox().getKey()+Broker.SEPERATOR+"$"+name)+" wurde nicht gefunden",e);
			return null;
		}//catch
	}//getParameterSlider

	/***************************************************************************
	 * Methoden, die der Parser aus dem XML aufruft *ANFANG*
	 */

	public void deleteAll(HashMap params) {
		deleteAll();
	}//deleteAll

	public void deleteAll() {
		List trash = getComputeDeleteList();
		
		try {
			broker.deleteObjects(trash);
		} catch (BrokerException e) {
			e.printStackTrace();
			ExceptionManager.doError("Die Objekte "+trash+" konnten nicht gelöscht werden",e);
		}//catch
		
		getListBox().clearInputField();		
	}//deleteAll

	private List getComputeDeleteList() {
		Set names = getListBox().getRelationContainer()
				.getAllNamedRelationNames();
		
		//Alle benannten Objekte in ListBox löschen
		List trash = new LinkedList();
		for (Iterator iter = names.iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			if(element.contains(".")) trash.add(element.substring(1));
			else trash.add(getListBox().getName() + Broker.SEPERATOR + element);
		} //for iter

		//Alle CompleteView Elemente löschen
		names = getCompleteViewBox().getRelationContainer()
				.getAllNamedRelationNames();
		for (Iterator iter = names.iterator(); iter.hasNext();) {
			String element = (String) iter.next();			
			trash.add(getCompleteViewBox().getName() + Broker.SEPERATOR
					+ element);
		} //for iter
		
//		if(trash.size()!=0)
//			System.out.println("Löschliste: "+trash);
		return trash;
	}
	
	public void reInit(HashMap map) {
		if(getListBox()==null) return;
		List trash = getComputeDeleteList();
		
		List dais = DrawAreasManager.getAllDrawAreas();
		for (Iterator iter = dais.iterator(); iter.hasNext();) {
			ExtendedDrawArea dai = (ExtendedDrawArea) iter.next();
            try {
            	Object obj = broker.getObject(dai.getKey());
            	if(obj!=null) trash.add(dai.getKey());
			} catch (BrokerException e1) {
			}        
        }//for
		
		try {
			broker.deleteObjects(trash);
		} catch (BrokerException e) {
			ExceptionManager.doError("Die Objekte "+trash+" konnten nicht gelöscht werden",e);
		}//catch
		DrawAreasManager.unregisterAllDrawAreas();
		DrawAreasManager.get(broker,"da");
		getListBox().clearInputField();
	}//reInit	

	//------------------------------------------------------------
	
	public void defaults(HashMap params) {
		String allwaysHideStr = null;
		try {
			allwaysHideStr = getFullKeyAndNormalize("h".length(),
					"hide", OPTIONAL, params);
			if (allwaysHideStr != null) {
				defaultsHide = "true".equalsIgnoreCase(allwaysHideStr);
				listBox.clearHideList();
			}//if
		} catch (NotFoundException e) {
			ExceptionManager.doError("Attribut wurde nicht gefunden",e);
		}//catch
	}//defaults

	public void modifyObject(HashMap params) {
		String name = null, lineStyle = null, visible = null, color = null, select = null, 
		       min = null, max = null, target = null, fillColor = null, borderColor = null, 
			   hide = null, freeze = null, style = null, colorMap = null, contourDelta=null,
			   showNames = null;
		Set usedSet = new HashSet();
		usedSet.add("name");
		try {
			name = getFullKeyAndNormalize("n".length(), "name", REQUIRED, params);
			lineStyle = getFullKeyAndNormalize("l".length(), "linestyle", OPTIONAL,
					params);
			style = getFullKeyAndNormalize("st".length(), "style", OPTIONAL,
					params);			
			color = getFullKeyAndNormalize("c".length(), "color", OPTIONAL, params);
			fillColor = getFullKeyAndNormalize("fi".length(), "fillcolor", OPTIONAL,
					params);
			borderColor = getFullKeyAndNormalize("b".length(), "bordercolor", OPTIONAL,
					params);
			select = getFullKeyAndNormalize("sel".length(), "select", OPTIONAL, params);
			min = getFullKeyAndNormalize("min".length(), "min", OPTIONAL, params);
			max = getFullKeyAndNormalize("max".length(), "max", OPTIONAL, params);
			target = getFullKeyAndNormalize("t".length(), "target", OPTIONAL, params);
			visible = getFullKeyAndNormalize("v".length(), "visible", OPTIONAL, params);
			hide = getFullKeyAndNormalize("hide".length(), "hide", OPTIONAL, params);
			freeze = getFullKeyAndNormalize("fr".length(), "freeze", OPTIONAL, params);
			colorMap = getFullKeyAndNormalize("map".length(), "mapstyle", OPTIONAL, params);
			contourDelta = getFullKeyAndNormalize("contour".length(), "contourdelta", OPTIONAL, params);
			showNames = getFullKeyAndNormalize("sh".length(), "shownames", OPTIONAL, params);
			
			if (lineStyle != null){
				setLineStyle(name, lineStyle);
				usedSet.add("linestyle");
			}//if
			
			if (style !=null){
				setPointStyle(name,style);
				usedSet.add("style");
			}//if
			
			if (color != null){
				setColorOfObject(name, ColorUtil.parseColor(color));
				usedSet.add("color");
			}//if
			
			if (fillColor != null){
				setFillColorOfObject(name, ColorUtil.parseColor(fillColor));
				usedSet.add("fillcolor");
			}//if
			
			if (borderColor != null){
				setBorderColorOfObject(name, ColorUtil.parseColor(borderColor));
				usedSet.add("bordercolor");
			}//if
			
			if (select != null){
				selectObject(name, "true".equalsIgnoreCase(select));
				usedSet.add("select");
			}//if
			
			if (min != null){
				setSliderMin(name, Formatierer.parse2Number(min).floatValue());
				usedSet.add("min");
			}//if
			
			if (max != null){
				setSliderMax(name, Formatierer.parse2Number(max).floatValue());
				usedSet.add("max");
			}//if
			
			if (target != null) {
				String[] das = target.split(",");
				for (int i = 0; i < das.length; i++) {
					DrawAreaInterface dai = DrawAreasManager
							.get(broker, das[i].trim());
					((ExtendedDrawArea)dai).addGraph(name);
				}//for i
				usedSet.add("target");
			}//if
			
			if (visible != null){
				setObjectVisible(name, visible.equalsIgnoreCase("true"));
				usedSet.add("visible");
			}//if
			
			if (hide != null){
				hideObject(name, hide.equalsIgnoreCase("true"));
				usedSet.add("hide");
			}else{
				if(!listBox.isHideObject(name))
					hideObject(name, defaultsHide);
			}//else
			
			if (freeze != null){
				freezeObject(name, freeze.equalsIgnoreCase("true"));
				usedSet.add("freeze");
			}//if
			
			if(colorMap!=null){
				int i = Integer.parseInt(colorMap);
				if(i>0 && i<MapGraph.gradientMaps.length){
					try {
						HasGraph hg = (HasGraph) broker.getObject(name);
						hg.setLineStyle(i);
					} catch (BrokerException e) {
						ExceptionManager.doError("Das Object " + name
								+ " wurde nicht gefunden.", e);
					}//catch
				}//if
				usedSet.add("colormap");
			}//if
			
			if (contourDelta != null){
				try {
					Object obj = ((UserFunction)broker.getObject(name)).eval();
					if(!(obj instanceof Map)){
						ExceptionManager.doError("Objekt "+name+" ist keine Farkarte");
						return;
					}//if
					Map map = (Map) obj;
					map.setContourDelta(Formatierer.parse2Number(contourDelta).doubleValue());
				} catch (MathException e) {
					ExceptionManager.doError("Fehler beim Holen der Farbkarte "+name+".",e);
				} catch (BrokerException e) {
					ExceptionManager.doError("Fehler beim Holen der Farbkarte "+name+".",e);
				}
				usedSet.add("contourdelta");
			}//if
			
			if(showNames!=null){
				try {
					Object obj = ((UserFunction)broker.getObject(name)).eval();
					if(!(obj instanceof MathList)){
						ExceptionManager.doError("Objekt "+name+" ist keine Liste");
						return;
					}//if
					MathList ml = (MathList) obj;
					ml.setShowNames("true".equals(showNames));
					broker.propagateChange(ml);
				} catch (MathException e) {
					ExceptionManager.doError("Fehler beim Holen der Liste "+name+".",e);
				} catch (BrokerException e) {
					ExceptionManager.doError("Fehler beim Holen der Liste "+name+".",e);
				} //catch				
			}//if
			
			Set tmpSet = new HashSet(params.keySet());
			tmpSet.removeAll(usedSet);
			if(tmpSet.size()!=0){
				ExceptionManager.doError("Es wurden nicht alle Parameter verarbeitet.("+tmpSet+")");
				throw new IllegalArgumentException();
			}//if
		} catch (NotFoundException e) {
			ExceptionManager.doError("Attribut wurde nicht gefunden",e);
			throw new IllegalArgumentException();
		}//catch
	}//modifyObject

	/**
	 * Läßt die Funktion vom Benutzer nicht mehr verändern.
	 */
	private void freezeObject(String name, boolean b) {
		try {
			MathObject mo = broker.getObject(name);
			if(!(mo instanceof HasGraph)){
				throw new BrokerException("Es exisitiert die Funktion '"+name+"' nicht");
			}//if
			HasGraph hg = (HasGraph) mo;
			hg.setFreeze(b);
			broker.propagateChange(hg);
		} catch (BrokerException e) {
			ExceptionManager.doError("Es exisitiert die Funktion '"+name+"' nicht",e);
		}//catch
	}//freezeObject

	public void hideObject(String name, boolean hide) {
		getListBox().hideObject(name, hide);
	}//hideObject

	public void setLineStyle(String objectName, String style) {
		try {
			HasGraph hg = (HasGraph) broker.getObject(objectName);
			if (hg == null)
				throw new NullPointerException("Das Object " + objectName
						+ " wurde nicht gefunden.");
			style = style.toLowerCase();
			if (style.equalsIgnoreCase("solid"))
				hg.setLineStyle(HasGraph.SOLID_STROKE);
			else if (style.equalsIgnoreCase("dash"))
				hg.setLineStyle(HasGraph.DASH_STROKE);
			else if (style.equalsIgnoreCase("dot"))
				hg.setLineStyle(HasGraph.DOT_STROKE);
			else if (style.equalsIgnoreCase("dot-dash"))
				hg.setLineStyle(HasGraph.DOT_DASH_STROKE);
			else{
				//Entweder Nummer...
				try{
					int lineStyle = Integer.parseInt(style);
					hg.setLineStyle(lineStyle);
					return;
				}catch(NumberFormatException nfe){					
				}
				
				//...oder Fehler
				ExceptionManager
						.doError(new ParseException(
								"Strichstil '"
										+ style
										+ "' unbekannt. Strichstile: 'solid', 'dash', 'dot', 'dot-dash'"));
			}
		} catch (BrokerException e) {
			ExceptionManager.doError("Das Objekt "+objectName+" wurde nicht gefunden.",e);
		}//catch
	} //setLineStyle
	
	public void setPointStyle(String objectName, String style) {
		try {
			UserFunction pointFunc = (UserFunction) broker.getObject(objectName);
			pointFunc.setStyle(style);
		} catch (BrokerException e) {
			ExceptionManager.doError("Das Objekt "+objectName+" wurde nicht gefunden.",e);
		}//catch
	} //setLineStyle	

	public void setObjectVisible(String name, boolean b) {
		Object obj = null;
		try {
			obj = broker.getObject(name);
			((HasGraph) obj).setVisible(b);
		} catch (BrokerException e) {
			ExceptionManager.doError("Das Objekt "+name+" wurde nicht gefunden.",e);
			return;
		}//catch		
		try{
			broker.propagateChange((MathObject) obj);
		}catch(BrokerException e){
			ExceptionManager.doError("Das Objekt "+name+" konnte nicht erneuert werden.",e);
		}
	} //setObjectVisible

	public void setColorOfObject(HashMap params) {
		try {
			String name = getFullKeyAndNormalize("n".length(), "name", REQUIRED, params);
			String color = getFullKeyAndNormalize("c".length(), "color", REQUIRED,
					params);
			setColorOfObject(name, ColorUtil.parseColor(color));
		} catch (NotFoundException e) {
			ExceptionManager.doError("Attribut wurde nicht gefunden.",e);
		}//catch
	}//setColorOfObject

	public void setColorOfObject(String name, Color c) {
		MathObject mo = broker.getMathObject(name);
		if (mo instanceof HasGraph) {
			((HasGraph) mo).setColor(c);
			try{
				broker.propagateChange(mo);
			}catch(BrokerException e){
				ExceptionManager.doError("Das Objekt "+name+" konnte nicht erneuert werden.",e);
			}
		}else{
			ExceptionManager.doError("Das Objekt "+name+" besitzt keine Farbe.");
		}
	} //setColorOfObject

	public void setFillColorOfObject(String name, Color c) {
		MathObject mo = broker.getMathObject(name);
		if (mo instanceof UserFunction) {
			((UserFunction) mo).setFillColor(c);
			try{
				broker.propagateChange(mo);
			}catch(BrokerException e){
				ExceptionManager.doError("Das Objekt "+name+" konnte nicht erneuert werden.",e);
			}
		} else{
			ExceptionManager.doError("Das Objekt "+name+" besitzt keine Füllfarbe.");
		}
	} //setColorOfObject

	public void setBorderColorOfObject(String name, Color c) {
		MathObject mo = broker.getMathObject(name);
		if (mo instanceof UserFunction) {
			((UserFunction) mo).setBorderColor(c);
			try {
				broker.propagateChange(mo);
			} catch (BrokerException e) {
				ExceptionManager.doError("Das Objekt "+name+" konnte nicht erneuert werden.",e);
			}
		}else{
			ExceptionManager.doError("Das Objekt "+name+" besitzt keine Randfarbe.");
		}
	} //setColorOfObject

	public void selectObject(String name, boolean b) {
		MathObject mo = null;
		try {
			mo = (MathObject) broker.getObject(name);
		} catch (BrokerException e) {
			ExceptionManager.doError("Das Objekt "+name+" konnte nicht gefunden werden.",e);
			return;
		}//catch
		
		String key = getCompleteViewBox().getKey() + Broker.SEPERATOR + "$"
				+ name;
		if (mo == null)
			throw new IllegalArgumentException("Objekt mit Schlüssel " + name
					+ " nicht vorhanden.");
		if (!(mo instanceof HasCompleteView))
			throw new IllegalArgumentException("Das Objekt hat keine Sicht.");
		if (b) {
			try {
				if (getCompleteViewBox().getRelationContainer()
						.getObjectByName(name) != null)
					return;
			} catch (ObjectNotInRelationException e) {
				ExceptionManager.doError("Object " + key
						+ " wurde nicht vollständig gefunden", e);
			}
			CompleteViewBox.register(mo, getCompleteViewBox(), broker);
		} else {
			MathObject obj = null;
			obj = broker.getMathObject(key);
			try {
				if (obj != null)
					broker.deleteObjectByKey(key);
			} catch (BrokerException e) {
				ExceptionManager.doError("Fehler beim Löschen des Objekts "+key,e);
			}//catch
		}//else
	} //selectObject

	public void selectObject(String name) {
		selectObject(name, true);
	} //selectObject

	public void setSlider(String name, float min, float max) {
		setSliderMin(name, min);
		setSliderMax(name, max);
	} //setSliderMin

	public void setSliderMin(String name, float min) {
		selectObject(name, true);
		ParameterComponent ps = getParameterSlider(name);
		ps.setMinValue(Formatierer.value2String(min));
	} //setSliderMin

	public void setSliderMax(String name, float max) {
		selectObject(name, true);
		ParameterComponent ps = getParameterSlider(name);
		ps.setMaxValue(Formatierer.value2String(max));
	} //setSliderMin

	//------------------------------------------------------------

	public void activate1To1Zoom(boolean b) {
		DrawAreasManager.get(broker, "da").activate1To1Zoom(b);
	} //activate1To1Zoom

	public void activate1To1Zoom(String da, boolean b) {
		DrawAreasManager.get(broker, da).activate1To1Zoom(b);
	} //activate1To1Zoom

	//------------------------------------------------------------

	public void newObject(HashMap params) {
		HashMap temp = new HashMap(params);
		String definition = null;
		try {
			definition = getFullKeyAndNormalize("d".length(), "definition", REQUIRED,
					temp);
		} catch (NotFoundException e) {
			ExceptionManager.doError("newObject: Definition konnte nicht gefunden werden.",e);
		}//catch
		definition = definition.replaceAll("\n","");
		String fnStr = getListBox().manageInput(definition);
		if(fnStr!=null){
		    temp.put("name", fnStr);
		    modifyObject(temp);
		}//if
	} //newObject

	public void newObject(String name) {
		getListBox().manageInput(name);
	} //newObject

	public void newObject(String name, Color c) {
		String s = getListBox().manageInput(name);
		setColorOfObject(s, c);
	} //newObject

	public void newObject(String name, boolean visible) {
		String s = getListBox().manageInput(name);
		setObjectVisible(s, visible);
	} //newObject

	public void newObject(String name, Color c, boolean visible) {
		newObject(name, visible, c);
	} //newObject

	public void newObject(String name, boolean visible, Color c) {
		String s = getListBox().manageInput(name);
		setObjectVisible(s, visible);
		setColorOfObject(s, c);
	} //newObject

	//------------------------------------------------------------

	public void deleteObject(HashMap params) {
		String name = null;
		try {
			name = getFullKeyAndNormalize("n".length(), "name", REQUIRED, params);
		} catch (NotFoundException e) {
			ExceptionManager.doError("Attribut wurde nicht gefunden", e);
		}//catch

		String[] fns = name.split(",");

		List deleteList = new LinkedList();
		for (int i = 0; i < fns.length; i++) {
			deleteList.add(fns[i].trim());
		}//for i
		
		try {
			broker.deleteObjects(deleteList);
		} catch (BrokerException e) {
			ExceptionManager.doError("Die Objekte "+deleteList+" konnten nicht gelöscht werden",e);
		}
	}//deleteObject

	public void deleteObject(String name) {
		List list = null;
		try {
			String[] parts = name.split(",");
			list = Arrays.asList(parts);
			broker.deleteObjects(list);
		} catch (Exception e) {
			ExceptionManager.doError("Die Objekte "+list+" konnten nicht gelöscht werden",e);
		}//catch
	} //deleteObject

	//------------------------------------------------------------

	public void coordinateSystem(float xmin, float xmax, float ymin, float ymax) {
		DrawAreasManager.get(broker, "da").coordinateSystem(xmin, xmax, ymin,
				ymax);
	} //coordinateSystem

	public void coordinateSystem(double xmin, double xmax, double ymin,
			double ymax) {
		DrawAreasManager.get(broker, "da").coordinateSystem(xmin, xmax, ymin,
				ymax);
	} //coordinateSystem

	//------------------------------------------------------------

	public void expand(HashMap params) {
		try {
			String left = getFullKeyAndNormalize("l".length(), "left", OPTIONAL, params);
			String right = getFullKeyAndNormalize("r".length(), "right", OPTIONAL,
					params);
			if (left != null)
				expandLeft(getExpandState(left));
			if (right != null)
				expandRight(getExpandState(right));
		} catch (NotFoundException e) {
			ExceptionManager.doError("Attribut nicht gefunden.",e);
		} catch (IllegalArgumentException e) {
			ExceptionManager.doError("Fehler bei 'expand'.",e);			
		}//catch
	}//expand
	

	private ListCheckBox.State getExpandState(String stateStr){
		if("true".equalsIgnoreCase(stateStr) || "all".equalsIgnoreCase(stateStr))
			return ListCheckBox.SELECTED;			
		else if("false".equalsIgnoreCase(stateStr) || "none".equalsIgnoreCase(stateStr))
			return ListCheckBox.NOT_SELECTED;
		else if("one".equalsIgnoreCase(stateStr))
			return ListCheckBox.DONT_CARE;
		else
			throw new IllegalArgumentException("Listenzustand '"+stateStr+"' nicht erkannt");
	}

	public void expandLeft(ListCheckBox.State state) {
		((ListCheckBox)getListBox().getExpandControl()).setState(state);
		getListBox().setExpandList();
	} //expandLeft

	public void expandRight(ListCheckBox.State state) {
		((ListCheckBox)getCompleteViewBox().getExpandControl()).setState(state);
		getListBox().setExpandList();
	} //expandRight

	public void expandLists(boolean bool) {
		expandLeft(getExpandState(Boolean.toString(bool)));
		expandRight(getExpandState(Boolean.toString(bool)));
		getListBox().setExpandList();
	}//expandLists

	//------------------------------------------------------------

	public synchronized float getParameter(String name) {
		paramChanger.setName(name);
		return (float) paramChanger.getValue();
	} //getParameter

	public synchronized void setParameter(String name, float value) {
			paramChanger.setName(name);
			paramChanger.setValue(value);			
			paramChanger.run();
	} //setParameter

	/***************************************************************************
	 * 
	 * Methoden, die der Parser aus dem XML aufruft *ENDE*
	 */

	/**
	 * stellt ein Objekt zur Verfügung, mit dem ein Parameter gändert werden
	 * kann
	 */
	class ParameterChanger{
		/** Wert der gesetzt werden soll */
		private double value;

		/** Funktion, deren Parameter geändert werden soll */
		private UserFunction f;

		public void run() {
			try {
				f.setValue(value);
				broker.propagateChange(f);
			} catch (Exception e) {
				ExceptionManager.doError(e);
			}//catch
		} //run

		public void setName(String name) {
			// -- Fenn Stefan
			// Diese Abkürzung geht nicht, da beim neuen
			// abspielen einer Definition, sich das Objekt ändert.
//			 if (name.equals(this.name))
//			     return;

			try {
				f = (UserFunction) broker.getObject(name);
				if(f==null)
				    ExceptionManager.doError("Der Parameter '"+name+"' existiert nicht.");
			} catch (BrokerException e) {
				ExceptionManager.doError(e);
			}//catch
		}//setName

		public void setValue(double value) {
			this.value = value;
		}//setValue
		
		public double getValue(){
			try {
				return f.evalScalar();
			} catch (MathException e) {
				ExceptionManager.doError(e);
				return Double.NaN;
			}
		}

	} //ParameterChanger

}//class ShowMeGraph
