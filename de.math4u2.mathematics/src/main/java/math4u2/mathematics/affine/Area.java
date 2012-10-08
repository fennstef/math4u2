package math4u2.mathematics.affine;

import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.relation.RelationFactory;
import math4u2.controller.relation.RelationInterface;
import math4u2.mathematics.functions.UserFunction;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.HasGraph;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.gui.listview.ListViewItemInterface;
import math4u2.view.gui.listview.ViewFactoryInterface;

public class Area extends AbstractArea {
	/** Teile der Fläche */
	private MathObject[] parts;

	public Area(String name, MathObject[] parts, Color color, int lineStyle,
			boolean isVisible, boolean fillArea, Broker broker,
			ViewFactoryInterface viewFactory) {
		super(null, name, color, fillArea, lineStyle, isVisible, broker,
				viewFactory);
		this.parts = parts;
	} // Konstruktor

	public Area(MathObject[] parts, boolean fillArea, Broker broker,
			ViewFactoryInterface viewFactory) {
		this(null, parts, Color.black, HasGraph.SOLID_STROKE, true, fillArea,
				broker, viewFactory);
	} // Konstruktor2

	/**
	 * Verknüpt die Fläche f mit seinen Beziehungspartnern (parts)
	 * 
	 * @param f
	 *            Kreis, der verknüpft werden soll.
	 * @param parts
	 *            Liste der Objekte, die Beziehungspartner sind
	 * @param regList
	 *            Liste der Objekte, die beim scheitern gelöscht werden (anonyme
	 *            Objekte)
	 * @param broker
	 */
	public static void register(MathObject f, List partsWithPath, List regList,
			Broker broker) throws BrokerException {
		// Überprüfung, ob alles in Ordnung ist
		if (!(f instanceof Area))
			throw new IllegalArgumentException(
					"Das Objekt muß eine Fläche sein. Ist aber " + f.getClass());
		if (broker == null)
			throw new NullPointerException(
					"Der Broker ist null. Dies ist hier nicht erlaubt.");
		// Beziehungen erzeugen
		List parts = new LinkedList();
		RelationInterface[] ra = new RelationInterface[partsWithPath.size()];
		for (int i = 0; i < ra.length; i++) {
			UserFunction part = (UserFunction) partsWithPath.get(i);
			if (regList.contains(part))
				ra[i] = RelationFactory.getPart_Of_Relation();
			else
				ra[i] = RelationFactory.getFunction_SubFunction_Relation();

			parts.add(part);
			_setCreationPath(ra[i], part);
		}// for

		List relations = Arrays.asList(ra);

		// Definieren lassen
		broker.defineRelations(f, parts, relations, Broker.FIRST_OBJECT);
	} // register

	public MathObject[] getParts() {
		return parts;
	}// getParts

	public void setFreeze(boolean freeze) {
		super.setFreeze(freeze);
		for (int i = 0; i < parts.length; i++) {
			if (parts[i] instanceof HasGraph)
				((HasGraph) parts[i]).setFreeze(freeze);
		}// for
	}// setFreeze

	public String bodyToString() {
		return getTypeString();
	} // bodyToString

	public String getTypeString() {
		if (isFillArea())
			return _bodyToString("flaeche");
		else
			return _bodyToString("kurvenzug");
	}// getTypeString

	protected String _bodyToString(String type) {
		String tmp = getRepresentation(parts[0]);
		String result = type + "({" + tmp;
		for (int i = 1; i < parts.length; i++) {
			result += "," + getRepresentation(parts[i]);
		}
		return result + "})";
	} // toString

	public GraphInterface getGraph(DrawAreaInterface da, UserFunction f) {
		return viewFactory.getGraphFactory().createAreaGraph(da, f);
	} // getGraph

	/**
	 * @see math4u2.controller.MathObject#swapLinks(math4u2.controller.MathObject,
	 *      math4u2.controller.MathObject)
	 */
	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {
		for (int i = 0; i < parts.length; i++) {
			if (parts[i] == oldObject) {
				parts[i] = (MathObject) newObject;
			} else {
				parts[i].swapLinks(oldObject, newObject);
			}// else
		}// for i
	} // swapLinks

	public void setName(String name) {
		this.name = name;
	} // setName

	public ListViewItemInterface getCompleteView(UserFunction f,
			ListViewInterface alv, Broker broker) {
		return viewFactory.createCompleteAreaViewItem(f, alv, broker);
	} // getCompleteView

} // class Area
