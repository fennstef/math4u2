package math4u2.view.gui.wizard.types;

import math4u2.view.gui.wizard.Math4u2WizardModel;
import math4u2.view.gui.wizard.components.CanModifyText;
import math4u2.view.gui.wizard.components.WizMatrix;
import math4u2.view.gui.wizard.step.StandardWizardStep.ParamEntry;

public class WizParamTypeMatrix extends WizParamType {

	public WizParamTypeMatrix() {
		super("Matrix");		
	}//Konstruktor
	
	public CanModifyText getComponent(String previousTitle, ParamEntry param,
			Math4u2WizardModel model) {
		return new WizMatrix();
	}// getComponent	
}//class WizParamTypeNewFunc
