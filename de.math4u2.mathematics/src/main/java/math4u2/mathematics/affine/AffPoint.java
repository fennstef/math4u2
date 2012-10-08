package math4u2.mathematics.affine;

import java.awt.Color;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
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
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeFunct;
import math4u2.mathematics.termnodes.TermNodeVector;
import math4u2.mathematics.termnodes.Variable;
import math4u2.mathematics.types.ScalarType;
import math4u2.mathematics.types.VectorType;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.AppendShapeInterface;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.HasGraph;
import math4u2.view.graph.drawarea.DrawAreaInterface;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.gui.listview.ListViewItemInterface;
import math4u2.view.gui.listview.ViewFactoryInterface;

public class AffPoint extends AbstractAffineObject 
                      implements  AppendShapeInterface {
	public static final String CIRCLE_CROSS = "CIRCLE_CROSS";

	public static final String CIRCLE_DOT = "CIRCLE_DOT";

	public static final String ARROW_H = "ARROW_H";

	public static final String ARROW_V = "ARROW_V";

	protected String style;

	private UserFunction xf, yf;

	/**
	 * Verknüpt den Punkt p mit seinen Beziehungspartnern (parts)
	 * 
	 * @param p
	 *            AffPunkt, der verknüpft werden soll
	 * @param parts
	 *            Liste der Objekte, die Beziehungspartner sind
	 * @param regList
	 *            Liste der Objekte, die beim scheitern gelöscht werden (anonyme
	 *            Objekte)
	 * @param broker
	 */
	public static void register(MathObject p, List partsWithPath, List regList,
			Broker broker) throws BrokerException {
		//Überprüfung, ob alles in Ordnung ist
		if (!(p instanceof AffPoint))
			throw new IllegalArgumentException(
					"Das Objekt muß ein Punkt sein. Ist aber " + p.getClass());
		if (partsWithPath.size() != 2)
			throw new IllegalArgumentException(
					"Die Teile-Liste muß genau die Länge 2 haben.");
		if (broker == null)
			throw new NullPointerException(
					"Der Broker ist null. Dies ist hier nicht erlaubt.");
		//Beziehungen erzeugen
		RelationInterface[] ra = new RelationInterface[2];
		UserFunction part0 = (UserFunction) (partsWithPath.get(0));
		UserFunction part1 = (UserFunction) (partsWithPath.get(1));
		if (regList.contains(part0))
			ra[0] = RelationFactory.getPart_Of_Relation();
		else
			ra[0] = RelationFactory.getFunction_SubFunction_Relation();
		if (regList.contains(part1))
			ra[1] = RelationFactory.getPart_Of_Relation();
		else
			ra[1] = RelationFactory.getFunction_SubFunction_Relation();
		List parts = new LinkedList();
		parts.add(part0);
		parts.add(part1);
		//Beziehungen benennen
		ra[0].setShortName(Relation.FIRST, "x");
		ra[1].setShortName(Relation.FIRST, "y");
		//_setCreationPath(ra[0], part0.getTermString());
		//_setCreationPath(ra[1], part1.getTermString());
		ra[0].setCreationPath(part0.getTermString().split("."));
		ra[1].setCreationPath(part1.getTermString().split("."));

		List relations = Arrays.asList(ra);
		//Definieren
		broker.defineRelations(p, parts, relations, Broker.FIRST_OBJECT);
	} //register

	public AffPoint(MathObject x, MathObject y, Broker broker, ViewFactoryInterface viewFactory)
			throws BrokerException {
		super("punkt", null, Color.BLACK, HasGraph.SOLID_STROKE, true, broker, viewFactory);
		name = null;
		xf = (UserFunction) x;
		yf = (UserFunction) y;
		setStyle(CIRCLE_CROSS);
	} //Konstruktor

	public void setFreeze(boolean freeze) {
		super.setFreeze(freeze);
		xf.setFreeze(freeze);
		yf.setFreeze(freeze);
	}//setFreeze

	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {
		if (xf == oldObject)
			xf = (UserFunction) newObject;
		if (yf == oldObject)
			yf = (UserFunction) newObject;
		xf.swapLinks(oldObject, newObject);
		yf.swapLinks(oldObject, newObject);
	} //swapLinks

	public void setName(String newName) {
		if (name != null)
			throw new RuntimeException("AffPoint mit Name " + name
					+ " kann nicht in " + newName + " umbenannt werden");
		name = newName;
	} //setName

	public UserFunction getX() {
		return xf;
	} //getX

	public UserFunction getY() {
		return yf;
	} //getY

	public GraphInterface getGraph(DrawAreaInterface da, UserFunction f) {
		return viewFactory.getGraphFactory().createPointGraph(da, f, broker);
	} //getGraph

	public double evalX() {
		try {
			return getX().evalScalar();
		} catch (MathException e) {
			ExceptionManager.doError("Fehler beim Holen der X-Koordinate ("+getKey()+").",e);
		} //catch
		return 0;
	} //evalX

	public double evalY() {
		try {
			return getY().evalScalar();
		} catch (MathException e) {
			ExceptionManager.doError("Fehler beim Holen der y-Koordinate ("+getKey()+").",e);
		} //catch
		return 0;
	} //evalY

	public boolean testDelete(java.util.Set aggregateSet) {
		return true;
	} //testDelete

	public String bodyToString() {
		return type+"(" + getRepresentation(getX()) + ","
				+ getRepresentation(getY()) + ")";
	} //bodyToString

	public ListViewItemInterface getCompleteView(UserFunction f, ListViewInterface alv,
			Broker broker) {
		return viewFactory.createCompletePointViewItem(f,alv, broker);
	} //getCompleteView

	public String getStyle() {
		return style;
	}//getStyle

	public void setStyle(String style) {
		style = style.toUpperCase();
		if (style.equals(CIRCLE_CROSS) || style.equals(CIRCLE_DOT)
				|| style.equals(ARROW_H) || style.equals(ARROW_V)) {
			this.style = style;
		} else {
			throw new IllegalArgumentException(
					"Der Darstellungstil ist ungültig (" + style
							+ ").\nGültig sind " + CIRCLE_CROSS + ", " + CIRCLE_DOT
							+ ", " + ARROW_H + " und " + ARROW_V + ".");
		}//else
	}//setStyle

	/**
	 * Diese Methoden werden von Außen aufgerufen z.B. p.x, p.y usw.
	 * 
	 * Anfang --------------------------------------------------------
	 */

	public Object operator_x(Object[] values) {
		return getX();
	} //operator_x

	public Object operator_set_x(Object[] values) {
		xf.setFunction(((UserFunction) values[0]).getFunction());
		try {
			broker.propagateChange(xf);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler bei der Methode 'x' ("+getKey()+").",e);
		}
		return null;
	} //operator_x

	public Class returnType_x(MethodContext mc) {
		return ScalarType.class;
	} //returnType_x

	//-------------------------------------------------------

	public Object operator_y(Object[] values) {
		return getY();
	} //operator_y

	public Class returnType_y(MethodContext mc) {
		return ScalarType.class;
	} //returnType_y

	public Object operator_set_y(Object[] values) {
		yf.setFunction(((UserFunction) values[0]).getFunction());
		try {
			broker.propagateChange(yf);
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler bei der Methode 'y' ("+getKey()+").",e);
		}
		return null;
	} //operator_set_y

	//--------------------------------------------------------

	public Object operator_r(Object[] values) throws Exception {
		return new UserFunction(new TermNodeVector(new TermNode[] {
				new TermNodeFunct(getX(), new TermNode[] {}, broker),
				new TermNodeFunct(getY(), new TermNode[] {}, broker) }),
				new Variable[] {}, broker, viewFactory);
	} //operator_r

	public Class returnType_r(MethodContext mc) {
		return VectorType.class;
	} //returnType_r
	
	public void appendShape( GeneralPath gp, DrawAreaInterface da, UserFunction own)
	    throws MathException  {
		gp.append( new Line2D.Double(da.xCoordToPix(getX().evalScalar()),
									 da.yCoordToPix(getY().evalScalar()),
				                     da.xCoordToPix(getX().evalScalar()),
				                     da.yCoordToPix(getY().evalScalar())), true) ;
	}

	public void applyProperties(HasGraph oldObject) {
		super.applyProperties(oldObject);
		setStyle(((AffPoint) oldObject).getStyle());
	}

	/**
	 * Ende ----------------------------------------------------------
	 */

} //class Point
