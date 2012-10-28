package math4u2.view.gui.wizard.step;

import java.util.Iterator;

import math4u2.view.gui.wizard.title.HasTitle;
import math4u2.view.gui.wizard.types.WizParamType;

/**
 * Wizard-Step speziell für eine Farbkarte
 * 
 * @author Fenn Stefan
 */
public class MapWizardStep extends StandardWizardStep {

	public MapWizardStep(String fullname, HasTitle previousTitle, String name, String description) {
		super(fullname, previousTitle, name, description, WizParamType.MAP);
	}//Konstruktor
	
	protected String _refreshResult(boolean withHtml) {
		StringBuffer buffer = new StringBuffer(name + "(");
		
		Iterator iter = params.iterator();
		ParamEntry param = (ParamEntry) iter.next();
		
		// Funktion
		buffer.append(markIt(param.comp.getText(),withHtml, param.comp));
		buffer.append(", vektor({");
		
		// z-Min
		param = (ParamEntry) iter.next();
		buffer.append(markIt(param.comp.getText(),withHtml, param.comp));
		buffer.append(", ");
		
		// z-Max
		param = (ParamEntry) iter.next();
		String max = markIt(param.comp.getText(),withHtml, param.comp);
		
		// Höhenlinien
		param = (ParamEntry) iter.next();
		String text = param.comp.getText();
		text = text.substring(1,text.length()-1);
		buffer.append(markIt(text,withHtml, param.comp));
		if(text.length()!=0)
			buffer.append(" ,");
		
		buffer.append(max+"}))");		
		return buffer.toString();
	}//refreshResult	
}//class MapWizardStep
