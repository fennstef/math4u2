package math4u2.view.gui.wizard;

import java.util.HashMap;

import org.pietschy.wizard.models.DynamicModel;

/**
 * Erweitert das <code>DynamicModel</code> um eine Hashmap.
 * 
 * @author Fenn Stefan
 */
public class Math4u2WizardModel extends DynamicModel {

	private HashMap map = new HashMap();
	
	public void put(String key, Object value){
		map.put(key,value);
	}//put
	
	public Object getObj(String key){
		return map.get(key);
	}//getObj
	
	public String get(String key){
		return (String) map.get(key);
	}
}//class Math4u2WizardModel
