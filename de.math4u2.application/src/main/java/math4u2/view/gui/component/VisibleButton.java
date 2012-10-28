package math4u2.view.gui.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JCheckBox;

import math4u2.application.resource.Images;
import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.relation.ObjectNotInRelationException;
import math4u2.controller.relation.RelationFactory;
import math4u2.controller.relation.RelationInterface;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.GraphInterface;
import math4u2.view.graph.HasGraph;

/**
 * Ist zum Setzen und Anzeigen der Sichtbarkeit aller Graphen da.
 * 
 * @author Fenn Stefan
 *  
 */
public class VisibleButton extends JCheckBox implements MathComponentView {

	private Broker broker;

	private HasGraph hasGraph;

	public VisibleButton(HasGraph hasGraph, Broker broker) {
		this.hasGraph = hasGraph;
		this.broker = broker;
		setSelected(true);
		setOpaque(false);
		setBorder(null);
		setSelectedIcon(Images.EYE_ENABLED);
		setIcon(Images.EYE_DISABLED);
		addActionListener(new VisibleAction());
	} //Konstruktor

	/**
	 * Erneuert die Sicht. Es wird bei der Funktion überprüft, ob
	 * ein Graph zur Darstellung existiert.
	 * 
	 * @param model
	 */
	public void refresh() {
		boolean graphExists = false;
		Iterator iter = hasGraph.getRelationContainer()
				.getSpecificRelationsIterator(
						RelationFactory.getView_Function_Relation().getName());
		while (iter.hasNext()) {
			RelationInterface ri = (RelationInterface) iter.next();
			try {
				if(ri.getPartner((MathObject) hasGraph) instanceof GraphInterface){
					graphExists = true;
					break;
				}//if
			} catch (ObjectNotInRelationException e) {
				ExceptionManager.doError("Fehler beim Erneuern der Sicht",e);
			}
		}//while
		
		setEnabled(graphExists);
		setSelected(hasGraph.isVisible());
	} //refresh

	public void setMathModel(MathObject mo) {
		hasGraph = (HasGraph) mo;
	}//setHasGraph

	public void deactivate() {
		setEnabled(false);
	}//deactivate

	class VisibleAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			hasGraph.setVisible(isSelected());
			try {
				broker.propagateChange(hasGraph);
			} catch (BrokerException e1) {
				ExceptionManager.doError(e1);
			}//catch
		} //actionPerformed
	} //class VisibleAction

} //class VisibleButton
