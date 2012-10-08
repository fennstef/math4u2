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
import math4u2.mathematics.collection.Sequence;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.HasGraph;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.gui.listview.ListViewItemInterface;
import math4u2.view.gui.listview.ViewFactoryInterface;

public class DiscreteSequence extends AbstractArea {

	private UserFunction xSequence; // Folge mit skalaren Werten, x-Koordinaten
	private UserFunction ySequence; // Folge mit skalaren Werten, y-Koordinaten
	private UserFunction maxIndexFunc; // skalare Funktion, Index des letzten
										// darzustellenden Paares
	private UserFunction rFunc; // skalare Funktion, Radius der Markierungen in
								// Pixel
	private String layout;

	public DiscreteSequence(String name, MathObject xSequence,
			MathObject ySequence, MathObject maxIndexFunc, MathObject rFunc,
			String layoutStr, Color color, int lineStyle, boolean isVisible,
			Broker broker, ViewFactoryInterface viewFactory) {
		super("punktFolge", name, color, true, lineStyle, true, broker,
				viewFactory);
		this.xSequence = (UserFunction) xSequence;
		this.ySequence = (UserFunction) ySequence;
		this.maxIndexFunc = (UserFunction) maxIndexFunc;
		this.rFunc = (UserFunction) rFunc;
		setLayout(layoutStr);
	}

	public DiscreteSequence(MathObject xSequence, MathObject ySequence,
			MathObject maxIndexFunc, MathObject rFunc, String layoutStr,
			Broker broker, ViewFactoryInterface viewFactory) {
		this(null, xSequence, ySequence, maxIndexFunc, rFunc, layoutStr,
				Color.black, HasGraph.SOLID_STROKE, true, broker, viewFactory);
	}

	public static void register(MathObject b, List partsWithPath, List regList,
			Broker broker) throws BrokerException {
		// Überprüfung, ob alles in Ordnung ist
		if (!(b instanceof DiscreteSequence))
			throw new IllegalArgumentException(
					"Das Objekt muß ein diskretes Folgen-Diagramm sein. Ist aber "
							+ b.getClass());
		if (partsWithPath.size() != 4)
			throw new IllegalArgumentException(
					"Die Teile-Liste muß genau die Länge 4 haben.");
		if (broker == null)
			throw new NullPointerException(
					"Der Broker ist null. Dies ist hier nicht erlaubt.");
		// Beziehungen erzeugen
		RelationInterface[] ra = new RelationInterface[4];
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
		ra[0].setShortName(Relation.FIRST, "x-werte-folge");
		ra[1].setShortName(Relation.FIRST, "y-werte-folge");
		ra[2].setShortName(Relation.FIRST, "maxIndex");
		ra[3].setShortName(Relation.FIRST, "radius");
		List relations = Arrays.asList(ra);

		// Definieren lassen
		broker.defineRelations(b, parts, relations, Broker.FIRST_OBJECT);
	} // register

	public ListViewItemInterface getCompleteView(UserFunction f,
			ListViewInterface alv, Broker broker) {
		return viewFactory.createCompleteDiscreteSequenceViewItem(f, alv,
				broker);
	}

	public void setLayout(String layout) {
		layout = layout.toUpperCase();
		if (layout.equals("P") || layout.equals("S") || layout.equals("PS")
				|| layout.equals("SP")) {
			this.layout = layout;
		} else {
			throw new IllegalArgumentException("Das Layout ist ungültig ("
					+ layout + ")");
		}
	}// setLayout

	public String getLayout() {
		return layout;
	}

	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {

		if (xSequence == oldObject) {
			xSequence = (UserFunction) newObject;
		} else {
			xSequence.swapLinks(oldObject, newObject);
		}
		if (ySequence == oldObject) {
			ySequence = (UserFunction) newObject;
		} else {
			ySequence.swapLinks(oldObject, newObject);
		}
		if (maxIndexFunc == oldObject) {
			maxIndexFunc = (UserFunction) newObject;
		} else {
			maxIndexFunc.swapLinks(oldObject, newObject);
		}
		if (rFunc == oldObject) {
			rFunc = (UserFunction) newObject;
		} else {
			rFunc.swapLinks(oldObject, newObject);
		}
	} // swapLinks

	public GraphInterface getGraph(DrawAreaInterface da, UserFunction f) {
		return viewFactory.getGraphFactory().createDiscreteSequenceGraph(da, f);
	}

	public double getRadius() throws MathException {
		if (rFunc == null)
			return 0;
		return ((ScalarDoubleResult) rFunc.eval()).value;
	} // getRadius

	public double[] getXValues() throws MathException {
		double mI = ((ScalarDoubleResult) maxIndexFunc.eval()).value;
		return ((Sequence) xSequence.eval()).getScalarElementArray((int) Math
				.floor(mI));
	}

	public double[] getYValues() throws MathException {
		double mI = ((ScalarDoubleResult) maxIndexFunc.eval()).value;
		return ((Sequence) ySequence.eval()).getScalarElementArray((int) Math
				.floor(mI));
	}

	public String bodyToString() {
		String seq1Str = getRepresentation(xSequence);
		String seq2Str = getRepresentation(ySequence);
		String maxIndexStr = getRepresentation(maxIndexFunc);
		String radiusStr = getRepresentation(rFunc);
		return type + "(" + seq1Str + "," + seq2Str + "," + maxIndexStr + ","
				+ radiusStr + ", " + layout + ")";
	}
}
