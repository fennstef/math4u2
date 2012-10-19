package math4u2.mathematics.affine;

import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.reference.MethodContext;
import math4u2.controller.reference.PathReference;
import math4u2.controller.relation.Relation;
import math4u2.controller.relation.RelationFactory;
import math4u2.controller.relation.RelationInterface;
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

/**
 * @author Stefan
 * 
 *         Sprechblase für einen Text in der Zeichenfläche
 */
public class TextBubble extends AbstractArea {

	private UserFunction start;

	private String text;

	private String orientation;

	public TextBubble(MathObject start, String text, String orientationStr,
			Broker broker, ViewFactoryInterface viewFactory) {
		super("text", null, Color.BLACK, true, HasGraph.SOLID_STROKE, true,
				broker, viewFactory);
		setFillColor(new Color(253, 255, 154));
		this.start = (UserFunction) start;
		this.text = text;
		setOrientation(orientationStr);
	} // Konstruktor 2

	/**
	 * Verknüpt den Textblase tb mit seinen Beziehungspartnern (parts)
	 * 
	 * @param tb
	 *            Textblase, die verknüpft werden soll.
	 * @param parts
	 *            Liste der Objekte, die Beziehungspartner sind
	 * @param regList
	 *            Liste der Objekte, die beim scheitern gelöscht werden (anonyme
	 *            Objekte)
	 * @param broker
	 */
	public static void register(MathObject tb, List partsWithPath,
			List regList, Broker broker) throws BrokerException {
		// Überprüfung, ob alles in Ordnung ist
		if (!(tb instanceof TextBubble))
			throw new IllegalArgumentException(
					"Das Objekt muß eine Textblase sein. Ist aber "
							+ tb.getClass());
		if (partsWithPath.size() != 1)
			throw new IllegalArgumentException(
					"Die Teile-Liste muß genau die Länge 1 haben.");
		if (broker == null)
			throw new NullPointerException(
					"Der Broker ist null. Dies ist hier nicht erlaubt.");
		// Beziehungen erzeugen
		RelationInterface[] ra = new RelationInterface[1];
		UserFunction part0 = (UserFunction) partsWithPath.get(0);
		if (regList.contains(part0))
			ra[0] = RelationFactory.getPart_Of_Relation();
		else
			ra[0] = RelationFactory.getFunction_SubFunction_Relation();
		List parts = new LinkedList();
		parts.add(part0);
		// Beziehungen bennenen
		ra[0].setShortName(Relation.FIRST, "position");
		_setCreationPath(ra[0], part0);
		List relations = Arrays.asList(ra);

		// Definieren lassen
		broker.defineRelations(tb, parts, relations, Broker.FIRST_OBJECT);
	} // register

	private AffPoint _getStart() throws MathException {
		AffPoint ap = (AffPoint) ((UserFunction) start).eval();
		if (ap == null)
			throw new MathException("Der Referenzpunkt wurde nicht gefunden.");
		return ap;
	} // getCenter

	public String bodyToString() {
		String startStr = null;
		startStr = getRepresentation(start);
		return type + "(" + startStr + "," + text + "," + orientation + ")";
	} // bodyToString

	public GraphInterface getGraph(DrawAreaInterface da, UserFunction f) {
		return viewFactory.getGraphFactory().createBubbleGraph(da,f,broker);
	} // getGraph

	public double getX() throws MathException {
		return _getStart().evalX();
	} // getX

	public double getY() throws MathException {
		return _getStart().evalY();
	} // getY

	public String getText() {
		return text;
	}// getText

	public String getOrientation() {
		return orientation;
	}// getOrientation

	public void setOrientation(String orientation) {
		orientation = orientation.toUpperCase();
		if (orientation.equals("B_NE") || orientation.equals("B_NW")
				|| orientation.equals("B_SE") || orientation.equals("B_SW")
				|| orientation.equals("A_N") || orientation.equals("A_S")
				|| orientation.equals("A_W") || orientation.equals("A_E")
				|| orientation.equals("C_S") || orientation.equals("C_N")
				|| orientation.equals("C_E") || orientation.equals("C_W")
				|| orientation.equals("K_E") || orientation.equals("K_W")
				|| orientation.equals("K_N") || orientation.equals("K_S")
				|| orientation.equals("POSTIT")) {
			this.orientation = orientation;
		} else {
			throw new IllegalArgumentException(
					"Die Orientierung ist ungültig (" + orientation + ")");
		}
	}// setOrientation

	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {
		if (start == oldObject) {
			start = (UserFunction) newObject;
		} else {
			start.swapLinks(oldObject, newObject);
		}// else
	} // swapLinks

	public void setFreeze(boolean freeze) {
		super.setFreeze(freeze);
		start.setFreeze(freeze);
	}// setFreeze

	public ListViewItemInterface getCompleteView(UserFunction f,
			ListViewInterface alv, Broker broker) {
		return viewFactory.createCompleteTextBubbleViewItem(f, alv, broker);
	} // getCompleteView

	/**
	 * Diese Methoden werden von Außen aufgerufen z.B. p.x, p.y usw.
	 * 
	 * Anfang --------------------------------------------------------
	 */

	public Object operator_position(Object[] values) {
		try {
			return start.eval();
		} catch (MathException e) {
			ExceptionManager.doError(
					"Fehler beim Holen des Parameters 'start' in der Textblase "
							+ getIdentifier(), e);
			throw new RuntimeException();
		}
	} // operator_position

	public Object operator_set_position(Object[] values) {
		Object obj = values[0];
		TermNode tm;
		if (obj instanceof AffPoint) {
			tm = new PathReference((AffPoint) obj, new LinkedList(), broker);
		} else {
			tm = ((UserFunction) values[0]).getFunction();
		}

		start.setFunction(tm);
		try {
			broker.propagateChange(start);
		} catch (BrokerException e) {
			ExceptionManager.doError(
					"Fehler beim Erneuern des Parameters 'start' in der Textblase "
							+ getIdentifier(), e);
		}
		return null;
	} // operator_set_position

	public Class returnType_position(MethodContext mc) {
		return AffPoint.class;
	}// returnType_position

	/**
	 * Ende ----------------------------------------------------------
	 */

}