package math4u2.mathematics.affine;

import java.util.LinkedList;
import java.util.List;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.reference.PathReference;
import math4u2.controller.relation.ObjectNotInRelationException;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.Variable;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.gui.listview.ListViewItemInterface;
import math4u2.view.gui.listview.ViewFactoryInterface;

/**
 * @author Fenn Stefan
 * 
 *         Die aktuelle Mauskoordinate wird hier verwaltet.
 */
public class MousePosition extends AffPoint {
	public static final String NAME = "mouse";

	private static MousePosition instance;
	private static Broker tmpBroker;

	private UserFunction mouseFunction;

	public static void setBroker(Broker broker) {
		MousePosition.tmpBroker = broker;
	}// setBroker

	public static MousePosition getInstance(ViewFactoryInterface viewFactory) {
		if (instance == null) {
			try {
				instance = new MousePosition(viewFactory);
				List parts = new LinkedList();
				parts.add(instance.getX());
				parts.add(instance.getY());
				AffPoint.register(instance, parts, new LinkedList(), tmpBroker);
			} catch (BrokerException e) {
				ExceptionManager
						.doError(
								"Fehler beim Erzeugen der Mauskoordinaten-Darstellung.",
								e);
			}
		}// if
		return instance;
	}// getInstance

	private MousePosition(ViewFactoryInterface viewFactory) throws BrokerException {
		super(new UserFunction(NAME + ".x", 0, tmpBroker, viewFactory), new UserFunction(
				NAME + ".y", 0, tmpBroker, viewFactory), tmpBroker, viewFactory);
		try {
			TermNode term = new PathReference(this, new LinkedList(), broker);
			mouseFunction = new UserFunction(NAME, term, new Variable[0],
					broker, viewFactory);
			broker.publishObject(mouseFunction, NAME);
		} catch (ObjectNotInRelationException e) {
			ExceptionManager.doError(
					"Fehler beim Erzeugen der Mauskoordinaten-Darstellung.", e);
		} catch (BrokerException e) {
			ExceptionManager.doError(
					"Fehler beim Erzeugen der Mauskoordinaten-Darstellung.", e);
		}
	}// Konstruktor

	public ListViewItemInterface getCompleteView(UserFunction f,
			ListViewInterface alv, Broker broker) {
		return viewFactory.createCompleteMouseViewItem(f, alv, broker);
	}// getCompleteView

	public void setMousePosition(double x, double y) {
		try {
			getX().setValue(x);
			getY().setValue(y);
			broker.propagateChange(mouseFunction);
		} catch (MathException e) {
			ExceptionManager.doError(
					"Fehler beim Setzen der Mauskoordinaten-Darstellung.", e);
		} catch (BrokerException e) {
			ExceptionManager.doError(
					"Fehler beim Setzen der Mauskoordinaten-Darstellung.", e);
		}
	}// setMousePosition

	public UserFunction getMouseFunction() {
		return mouseFunction;
	}// getMouseFunction
}// class MousePosition
