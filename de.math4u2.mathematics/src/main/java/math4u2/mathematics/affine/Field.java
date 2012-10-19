package math4u2.mathematics.affine;

import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.relation.Relation;
import math4u2.controller.relation.RelationFactory;
import math4u2.controller.relation.RelationInterface;
import math4u2.mathematics.functions.UserFunction;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.HasGraph;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.gui.listview.ListViewItemInterface;
import math4u2.view.gui.listview.ViewFactoryInterface;

public class Field extends AbstractAffineObject {
	/** vektorwertige Funktion fieldFunc(x,y), liefert das Feld */
	private UserFunction fieldFunc;
	/** Referenzpunkt für Darstellungen */
	private UserFunction refPoint;
	/**
	 * skalare nulstellige Funktion, liefert Abstand der Darstellungen in
	 * x-Richtung
	 */
	private UserFunction xDistFunc;
	/**
	 * skalare nulstellige Funktion, liefert Abstand der Darstellungen in
	 * x-Richtung
	 */
	private UserFunction yDistFunc;
	/**
	 * skalare nulstellige Funktion, liefert den Cutoff für die darzustellenden
	 * Vektoren
	 */
	private UserFunction cutoffFunc;
	private String layout;

	public Field(String name, MathObject fieldFunc, MathObject refPoint,
			MathObject xDistFunc, MathObject yDistFunc, MathObject cutoffFunc,
			String layoutStr, boolean isVisible, Broker broker,
			ViewFactoryInterface viewFactory) {
		super("feld", null, Color.black, HasGraph.SOLID_STROKE, isVisible,
				broker, viewFactory);
		this.fieldFunc = (UserFunction) fieldFunc;
		this.refPoint = (UserFunction) refPoint;
		this.xDistFunc = (UserFunction) xDistFunc;
		this.yDistFunc = (UserFunction) yDistFunc;
		this.cutoffFunc = (UserFunction) cutoffFunc;
		setLayout(layoutStr);

	} // Konstruktor

	/*
	 * Konstruktor für ein anonymes Feld
	 */

	public Field(MathObject fieldFunc, MathObject refPoint,
			MathObject xDistFunc, MathObject yDistFunc, MathObject cutoffFunc,
			String layoutStr, boolean isVisible, Broker broker,
			ViewFactoryInterface viewFactory) {
		this("", fieldFunc, refPoint, xDistFunc, yDistFunc, cutoffFunc,
				layoutStr, isVisible, broker, viewFactory);
	} // Konstruktor

	public UserFunction getFieldFunc() {
		return fieldFunc;
	}

	public UserFunction getXDistFunc() {
		return xDistFunc;
	}

	public UserFunction getYDistFunc() {
		return yDistFunc;
	}

	public UserFunction getCutoffFunc() {
		return cutoffFunc;
	}

	public UserFunction getRefPoint() {
		return refPoint;
	}

	public void setLayout(String layout) {
		layout = layout.toUpperCase();
		if (layout.equals("V") || layout.equals("NV") || layout.equals("T")
				|| layout.equals("NT") || layout.equals("VP")
				|| layout.equals("NVP") || layout.equals("TP")
				|| layout.equals("NTP")) {
			this.layout = layout;
		} else {
			throw new IllegalArgumentException("Das Layout ist ungültig ("
					+ layout + ")");
		}
	}// setLayout

	public String getLayout() {
		return layout;
	}

	/**
	 * Verknüpt das Feld b mit ihren Beziehungspartnern (parts)
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
		// Überprüfung, ob alles in Ordnung ist
		if (!(b instanceof Field))
			throw new IllegalArgumentException(
					"Das Objekt muß eie Feld sein. Ist aber " + b.getClass());
		if (partsWithPath.size() != 5)
			throw new IllegalArgumentException(
					"Die Teile-Liste muß genau die Länge 5 haben.");
		if (broker == null)
			throw new NullPointerException(
					"Der Broker ist null. Dies ist hier nicht erlaubt.");
		// Beziehungen erzeugen
		RelationInterface[] ra = new RelationInterface[5];
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
		// Beziehungen bennenen
		ra[0].setShortName(Relation.FIRST, "feld-Funktion");
		ra[1].setShortName(Relation.FIRST, "referenzpunkt");
		ra[2].setShortName(Relation.FIRST, "x-distanz");
		ra[3].setShortName(Relation.FIRST, "y-distanz");
		ra[4].setShortName(Relation.FIRST, "cuttoff");
		List relations = Arrays.asList(ra);

		// Definieren lassen
		broker.defineRelations(b, parts, relations, Broker.FIRST_OBJECT);
	} // register

	public void setFreeze(boolean freeze) {
		super.setFreeze(freeze);
	}// setFreeze

	public String bodyToString() {
		String fieldFuncStr = fieldFunc.getName();
		String refStr = getRepresentation(refPoint);
		String xDistStr = getRepresentation(xDistFunc);
		String yDistStr = getRepresentation(yDistFunc);
		String cutoffStr = getRepresentation(cutoffFunc);
		return type + "(" + fieldFuncStr + "," + refStr + "," + xDistStr + ","
				+ yDistStr + "," + cutoffStr + "," + layout + ")";
	} // bodyToString

	public GraphInterface getGraph(DrawAreaInterface da, UserFunction f) {
		return viewFactory.getGraphFactory().createFieldVectorGraph(da, f);
	} // getGraph

	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {

		if (fieldFunc == oldObject) {
			fieldFunc = (UserFunction) newObject;
		} else {
			fieldFunc.swapLinks(oldObject, newObject);
		}
		if (xDistFunc == oldObject) {
			xDistFunc = (UserFunction) newObject;
		} else {
			xDistFunc.swapLinks(oldObject, newObject);
		}
		if (yDistFunc == oldObject) {
			yDistFunc = (UserFunction) newObject;
		} else {
			yDistFunc.swapLinks(oldObject, newObject);
		}
		if (cutoffFunc == oldObject) {
			cutoffFunc = (UserFunction) newObject;
		} else {
			cutoffFunc.swapLinks(oldObject, newObject);
		}
		if (refPoint == oldObject) {
			refPoint = (UserFunction) newObject;
		} else {
			refPoint.swapLinks(oldObject, newObject);
		}
	} // swapLinks

	public ListViewItemInterface getCompleteView(UserFunction f,
			ListViewInterface alv, Broker broker) {
		return viewFactory.createCompleteDefaultViewItem(f, alv, broker);
	}// getCompleteView

} // class Field
