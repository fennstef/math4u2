package math4u2.view.gui.wizard.step;

import java.util.Iterator;

import math4u2.view.gui.wizard.title.HasTitle;
import math4u2.view.gui.wizard.types.WizParamType;


/**
 * Wizardstep für Listen
 * 
 * @author Fenn Stefan
 */
public class ListWizardStep extends StandardWizardStep {

	public ListWizardStep(String fullname, HasTitle previousTitle, String name) {
		super(fullname, previousTitle, name, WizParamType.ANONYM_LIST);
	}//Konstruktor
	
	protected String _refreshResult(boolean withHtml) {
		StringBuffer text = new StringBuffer();
		
		Iterator iter = params.iterator();
		
		ParamEntry param = (ParamEntry) iter.next();
		text.append(markIt(param.comp.getText(),withHtml, param.comp));
		
		text.append(" ");
		
		params.remove(0);
		
		text.append(super._refreshResult(withHtml));
		
		params.add(0,param);
		
		return text.toString();
	}//_refreshResult
}//class StandardWizardStep
