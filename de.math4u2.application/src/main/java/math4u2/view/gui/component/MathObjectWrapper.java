package math4u2.view.gui.component;

import java.util.Set;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.relation.RelationContainer;
import math4u2.util.exception.NotImplementedException;
import math4u2.view.formula.interactive.StepInteractive;

/**
 * <code>MathComponentView</code> ist kein <code>MathObject</code>, da der
 * Renew-Aufruf von der <code>ListBox</code> verwaltet wird, da bei Änderung
 * einer Funktion z.B. die <code>MathComponentView</code> neu erstellt werden
 * muß.
 * 
 * Es sollen aber auch im Themenblatt <code>MathComponentView</code> vorhanden
 * sein, diese müssen mit dem <code>MathObjectWrapper</code> gekapselt sein.
 * 
 * @see math4u2.controller.MathObject
 * @see math4u2.view.gui.componente.MathComponentView
 * 
 * @author Fenn Stefan
 */

public class MathObjectWrapper implements MathObject, StepInteractive{

	private MathComponentView mcv;
	private RelationContainer relationContainer;
	
	public MathObjectWrapper(MathComponentView mcv){
		this.mcv = mcv;
		relationContainer = new RelationContainer(this);
	}//Konstruktor

	public Object getIdentifier() {		
		return "MathObjectWrapper"+hashCode();
	}//getKey

	public boolean testSubstitution(MathObject oldObject, Set oldAggregateSet) {
		return true;
	}//testSubstitution

	public boolean testDelete() {
		return true;
	}//testDelete

	public void renew(MathObject source) {
		mcv.setMathModel(source);
		mcv.refresh();
	}//renew

	public RelationContainer getRelationContainer() {
		return relationContainer;
	}

	public void setName(String name) {
		if(!name.equals(getIdentifier()))
			throw new NotImplementedException();
	}//setName

	public void swapLinks(MathObject oldObject, MathObject newObject) throws Exception {
	}//swapLinks

	public void prepareDelete() {		
	}//prepareDelete

	
	public void deactivate(Broker broker) throws BrokerException {
		broker.deleteObject(this);
		mcv.deactivate();
	}

}//class MathObjectWrapper