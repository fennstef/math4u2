package math4u2.mathematics.affine;

import java.awt.Color;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;
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
import math4u2.view.graph.AppendShapeInterface;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.HasGraph;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.gui.listview.ListViewItemInterface;
import math4u2.view.gui.listview.ViewFactoryInterface;

public class Bezier extends AbstractAffineObject implements
		AppendShapeInterface {
	private UserFunction startPoint, startDirPoint, endDirPoint, endPoint;

	public Bezier(MathObject startPoint, MathObject startDirPoint,
			MathObject endDirPoint, MathObject endPoint, Color color,
			int lineStyle, boolean isVisible, Broker broker,
			ViewFactoryInterface viewFactory) {
		super("bezier", null, color, lineStyle, isVisible, broker, viewFactory);
		this.startPoint = (UserFunction) startPoint;
		this.startDirPoint = (UserFunction) startDirPoint;
		this.endDirPoint = (UserFunction) endDirPoint;
		this.endPoint = (UserFunction) endPoint;
	} // Broker broker)

	public Bezier(MathObject Start, MathObject startDirPoint,
			MathObject endDirPoint, MathObject End, Broker broker,
			ViewFactoryInterface viewFactory) {
		this(Start, startDirPoint, endDirPoint, End, Color.black,
				HasGraph.SOLID_STROKE, true, broker, viewFactory);
	} // Stretch(AffineProxy Start, A...

	/**
	 * Verknüpt die Bezier-Kurve b mit seinen Beziehungspartnern (parts)
	 * 
	 * @param b
	 *            Kurve b, die verknüpft werden soll
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
		if (!(b instanceof Bezier))
			throw new IllegalArgumentException(
					"Das Objekt muß eine Bezier-Kurve sein. Ist aber "
							+ b.getClass());
		if (partsWithPath.size() != 4)
			throw new IllegalArgumentException(
					"Die Teile-Liste muß genau die Länge 4 haben.");
		if (broker == null)
			throw new NullPointerException(
					"Der Broker ist null. Dies ist hier nicht erlaubt.");
		// Beziehungen erzeugen
		RelationInterface[] ra = new RelationInterface[4];
		UserFunction part0 = (UserFunction) partsWithPath.get(0);
		UserFunction part1 = (UserFunction) partsWithPath.get(1);
		UserFunction part2 = (UserFunction) partsWithPath.get(2);
		UserFunction part3 = (UserFunction) partsWithPath.get(3);

		if (regList.contains(part0))
			ra[0] = RelationFactory.getPart_Of_Relation();
		else
			ra[0] = RelationFactory.getFunction_SubFunction_Relation();

		if (regList.contains(part1))
			ra[1] = RelationFactory.getPart_Of_Relation();
		else
			ra[1] = RelationFactory.getFunction_SubFunction_Relation();

		if (regList.contains(part2))
			ra[2] = RelationFactory.getPart_Of_Relation();
		else
			ra[2] = RelationFactory.getFunction_SubFunction_Relation();
		if (regList.contains(part3))
			ra[3] = RelationFactory.getPart_Of_Relation();
		else
			ra[3] = RelationFactory.getFunction_SubFunction_Relation();

		List parts = new LinkedList();
		parts.add(part0);
		parts.add(part1);
		parts.add(part2);
		parts.add(part3);
		// Beziehungen bennenen
		ra[0].setShortName(Relation.FIRST, "p1");
		ra[1].setShortName(Relation.FIRST, "p2");
		ra[2].setShortName(Relation.FIRST, "p3");
		ra[3].setShortName(Relation.FIRST, "p4");
		_setCreationPath(ra[0], part0);
		_setCreationPath(ra[1], part1);
		_setCreationPath(ra[2], part2);
		_setCreationPath(ra[3], part3);
		List relations = Arrays.asList(ra);
		// Definieren
		broker.defineRelations(b, parts, relations, Broker.FIRST_OBJECT);
	} // register(MathObject s, List par...

	public GraphInterface getGraph(DrawAreaInterface da, UserFunction f) {
		return viewFactory.getGraphFactory().createBezierGraph(da, f);
	} 

	public void setName(String newName) {
		if (name == null)
			name = newName;
		else
			throw new RuntimeException("Bezierkurve mit Name " + name
					+ " kann nicht in " + newName + " umbenannt werden");
	} // setName(String newName)

	public String bodyToString() {
		String startStr = "";
		String startDirStr = "";
		String endDirStr = "";
		String endStr = "";
		startStr = getRepresentation(startPoint);
		startDirStr = getRepresentation(startDirPoint);
		endDirStr = getRepresentation(endDirPoint);
		endStr = getRepresentation(endPoint);
		return type + "(" + startStr + "," + startDirStr + "," + endDirStr
				+ "," + endStr + ")";
	} // String bodyToString()

	public AffPoint getStartPoint() throws MathException {
		AffPoint ap = (AffPoint) ((UserFunction) startPoint).eval();
		if (ap == null)
			throw new MathException("Der Startpunkt wurde nicht gefunden.");
		return ap;
	}

	public AffPoint getStartDirPoint() throws MathException {
		AffPoint ap = (AffPoint) ((UserFunction) startDirPoint).eval();
		if (ap == null)
			throw new MathException(
					"Der Start-Richtungs-Punkt wurde nicht gefunden.");
		return ap;
	}

	public AffPoint getEndDirPoint() throws MathException {
		AffPoint ap = (AffPoint) ((UserFunction) endDirPoint).eval();
		if (ap == null)
			throw new MathException(
					"Der End-Richtungs-Punkt wurde nicht gefunden.");
		return ap;
	}

	public AffPoint getEndPoint() throws MathException {
		AffPoint ap = (AffPoint) ((UserFunction) endPoint).eval();
		if (ap == null)
			throw new MathException("Der Endpunkt wurde nicht gefunden.");
		return ap;
	}

	public void appendShape(GeneralPath gp, DrawAreaInterface da,
			UserFunction own) throws MathException {
		int xs = da.xCoordToPix(getStartPoint().evalX());
		int ys = da.yCoordToPix(getStartPoint().evalY());
		int xsdir = da.xCoordToPix(getStartDirPoint().evalX());
		int ysdir = da.yCoordToPix(getStartDirPoint().evalY());
		int xedir = da.xCoordToPix(getEndDirPoint().evalX());
		int yedir = da.yCoordToPix(getEndDirPoint().evalY());
		int xe = da.xCoordToPix(getEndPoint().evalX());
		int ye = da.yCoordToPix(getEndPoint().evalY());
		gp.append(new CubicCurve2D.Double(xs, ys, xsdir, ysdir, xedir, yedir,
				xe, ye), true);
	}// appendShape

	public void setFreeze(boolean freeze) {
		super.setFreeze(freeze);
		startPoint.setFreeze(freeze);
		startDirPoint.setFreeze(freeze);
		endDirPoint.setFreeze(freeze);
		endPoint.setFreeze(freeze);
	}// setFreeze

	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {
		if (oldObject == startPoint)
			startPoint = (UserFunction) newObject;
		else
			startPoint.swapLinks(oldObject, newObject);

		if (oldObject == startDirPoint)
			startDirPoint = (UserFunction) newObject;
		else
			startDirPoint.swapLinks(oldObject, newObject);

		if (oldObject == endDirPoint)
			endDirPoint = (UserFunction) newObject;
		else
			endDirPoint.swapLinks(oldObject, newObject);

		if (oldObject == endPoint)
			endPoint = (UserFunction) newObject;
		else
			endPoint.swapLinks(oldObject, newObject);
	} // swapLinks(MathObject oldObject,...

	public ListViewItemInterface getCompleteView(UserFunction f,
			ListViewInterface alv, Broker broker) {
		return viewFactory.createCompleteBezierViewItem(f, alv, broker);
	} // AbstractListViewItem getComplet...

	/**
	 * Diese Methoden werden von Außen aufgerufen z.B. p.x, p.y usw.
	 * 
	 * Anfang --------------------------------------------------------
	 */

	public Object operator_p1(Object[] values) {
		try {
			return getStartPoint();
		} catch (MathException e) {
			e.printStackTrace();
			return null;
		}
	} // operator_begin

	public Object operator_set_p1(Object[] values) {
		Object obj = values[0];
		TermNode tm;
		if (obj instanceof AffPoint) {
			tm = new PathReference((AffPoint) obj, new LinkedList(), broker);
		} else {
			tm = ((UserFunction) values[0]).getFunction();
		}

		startPoint.setFunction(tm);
		try {
			broker.propagateChange(startPoint);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler bei der Methode 'p1' (" + getKey()
					+ ").", e);
		}
		return null;
	} // operator_set_begin

	public Class returnType_p1(MethodContext mc) {
		return AffPoint.class;
	} // returnType_begin

	// ---------------------------------------------------

	public Object operator_p2(Object[] values) {
		try {
			return getStartDirPoint();
		} catch (MathException e) {
			e.printStackTrace();
			return null;
		}
	} // operator_begindir

	public Object operator_set_p2(Object[] values) {
		Object obj = values[0];
		TermNode tm;
		if (obj instanceof AffPoint) {
			tm = new PathReference((AffPoint) obj, new LinkedList(), broker);
		} else {
			tm = ((UserFunction) values[0]).getFunction();
		}

		startDirPoint.setFunction(tm);
		try {
			broker.propagateChange(startDirPoint);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler bei der Methode 'p2' (" + getKey()
					+ ").", e);
		}
		return null;
	} // operator_set_begin

	public Class returnType_p2(MethodContext mc) {
		return AffPoint.class;
	} // returnType_begindir

	// ---------------------------------------------------

	public Object operator_p3(Object[] values) {
		try {
			return getEndDirPoint();
		} catch (MathException e) {
			e.printStackTrace();
			return null;
		}
	} // operator_enddir

	public Object operator_set_p3(Object[] values) {
		Object obj = values[0];
		TermNode tm;
		if (obj instanceof AffPoint) {
			tm = new PathReference((AffPoint) obj, new LinkedList(), broker);
		} else {
			tm = ((UserFunction) values[0]).getFunction();
		}

		endDirPoint.setFunction(tm);
		try {
			broker.propagateChange(endPoint);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler bei der Methode 'p3' (" + getKey()
					+ ").", e);
		}
		return null;
	} // operator_enddir

	public Class returnType_p3(MethodContext mc) {
		return AffPoint.class;
	} // returnType_enddir

	// ---------------------------------------------------

	public Object operator_p4(Object[] values) {
		try {
			return getEndPoint();
		} catch (MathException e) {
			e.printStackTrace();
			return null;
		}
	} // operator_ende

	public Object operator_set_p4(Object[] values) {
		Object obj = values[0];
		TermNode tm;
		if (obj instanceof AffPoint) {
			tm = new PathReference((AffPoint) obj, new LinkedList(), broker);
		} else {
			tm = ((UserFunction) values[0]).getFunction();
		}

		endPoint.setFunction(tm);
		try {
			broker.propagateChange(endPoint);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler bei der Methode 'p4' (" + getKey()
					+ ").", e);
		}
		return null;
	} // operator_ende

	public Class returnType_p4(MethodContext mc) {
		return AffPoint.class;
	} // returnType_p4

	/**
	 * Ende ----------------------------------------------------------
	 */

} // class Stretch extends Abstra...
