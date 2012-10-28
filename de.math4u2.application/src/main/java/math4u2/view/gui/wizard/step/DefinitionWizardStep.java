package math4u2.view.gui.wizard.step;

import javax.swing.JCheckBox;

import org.pietschy.wizard.WizardModel;

import math4u2.view.gui.Math4U2Win;
import math4u2.view.gui.listview.ListBox;
import math4u2.view.gui.wizard.components.CanModifyText;
import math4u2.view.gui.wizard.components.WizTextField;
import math4u2.view.gui.wizard.title.SimpleHasTitle;
import math4u2.view.gui.wizard.types.WizParamType;

/**
 * Wizard-Step speziell für eine Farbkarte
 * 
 * @author Fenn Stefan
 */
public class DefinitionWizardStep extends StandardWizardStep {
	private boolean firstTime = true;

	public DefinitionWizardStep() {
		super("Definition erzeugen", SimpleHasTitle.EMPTY_TITLE, "Erzeugen Sie hier ein Objekt oder eine Funktion", WizParamType.DEFINITION);
	}// Konstruktor

	public void init(WizardModel model) {
		super.init(model);
		JCheckBox shouldClear = (JCheckBox) ((ParamEntry) params.get(2)).comp;
		shouldClear.setSelected(true);
	}//init
	
	public void prepare() {
		JCheckBox shouldClear = (JCheckBox) ((ParamEntry) params.get(2)).comp;

		if (shouldClear.isSelected()) {
			((ParamEntry) params.get(0)).comp.setText("");
			((ParamEntry) params.get(1)).comp.setText("");
		}// if

		if (firstTime) {
			JCheckBox shouldExecute = (JCheckBox) ((ParamEntry) params.get(3)).comp;
			shouldExecute.setSelected(true);
		}// if

		WizTextField defField = (WizTextField) ((ParamEntry) params.get(0)).comp;
		setFirstTimeFocus(defField);
		defField.selectAll();
		firstTime = false;
	}// prepare

	protected String _refreshResult(boolean withHtml) {
		CanModifyText head = ((ParamEntry) params.get(0)).comp;
		CanModifyText body = ((ParamEntry) params.get(1)).comp;
		boolean empty = head.getText().length() == 0
				&& body.getText().length() == 0;
		return markIt(head.getText().trim(), withHtml, head)
				+ (empty ? "" : " := ")
				+ markIt(body.getText(), withHtml, body);
	}// _refreshResult

	
	protected void refreshResult() {
		resultLabel.setText(_refreshResult(true));
		output = _refreshResult(false);
		refreshErrorHighlights();
	}//refreshResult

	public void endAction(String inputStr) {
		JCheckBox shouldExcute = (JCheckBox) ((ParamEntry) params.get(3)).comp;
		ListBox listbox = Math4U2Win.getInstance().getListBox();
		listbox.getInputField().setText(inputStr);
		if (shouldExcute.isSelected()) {			
			listbox.manageInput(inputStr);
			listbox.getInputField().setText("");
		}// if
	}// endAction
	
	protected void addParam(String title, String description, String value, WizParamType type) {
		ParamEntry pe;
		if(type==WizParamType.DEFINITION_BODY){
			pe = new ParamEntry(title, description, value, type, null){
				public String getTitle(){
					return "Definition "+((ParamEntry) params.get(0)).comp.getText();
				}//getTitle
			};
		}else{
			pe = new ParamEntry(title, description, value, type, null);
		}
		params.add(pe);
	}//addParam

}// class DefinitionWizardStep
