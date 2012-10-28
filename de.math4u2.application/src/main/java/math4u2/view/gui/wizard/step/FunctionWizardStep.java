package math4u2.view.gui.wizard.step;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JLabel;

import math4u2.view.gui.wizard.components.CanModifyText;
import math4u2.view.gui.wizard.components.WizComboBox;
import math4u2.view.gui.wizard.title.HasTitle;
import math4u2.view.gui.wizard.types.WizParamType;
import math4u2.view.gui.wizard.types.WizParamTypeNewFunc;
import math4u2.view.layout.TableLayout;
import math4u2.view.layout.TableLayoutConstraints;

/**
 * Wizard-Step speziell für Funktionen
 * 
 * @author Fenn Stefan
 */
public class FunctionWizardStep extends StandardWizardStep {

	public FunctionWizardStep(String fullname, HasTitle previousTitle) {
		super(fullname, previousTitle, "", WizParamType.NEW_FUNC);
	}//Konstruktor
	
	public void prepare() {
		super.prepare();
		ParamEntry  funcParam = (ParamEntry)params.get(0);
		final WizParamTypeNewFunc wptnf = (WizParamTypeNewFunc) funcParam.type;
		final WizComboBox comboBox = (WizComboBox) funcParam.comp;
		comboBox.addChangeListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				updateComponents(comboBox, wptnf);
			}//actionPerformed
		});
		updateComponents(comboBox, wptnf);
	}//prepare
	
	/**
	 * Ersetzt die TextBoxen, wenn eine neue Funktion ausgewählt wird.
	 */
	private void updateComponents(WizComboBox comboBox, WizParamTypeNewFunc wptnf){
		int pos = comboBox.getSelectedIndex();
		WizParamTypeNewFunc.Entry entry = wptnf.getFunctionEntry(pos);
		WizParamType[] argumentTypes = entry.argumentTypes;
		for (int i = 0; i < argumentTypes.length; i++) {
			ParamEntry argParam = (ParamEntry) params.get(i+1);
			JComponent oldComp = (JComponent) argParam.comp;
			argParam.type = argumentTypes[i];
			CanModifyText newComp = FunctionWizardStep.this.createComponent(argParam);
			argParam.comp = newComp;
			
			argParam.description = entry.argumentTexts[i];
			argParam.title = entry.argumentStrings[i];
			JLabel labelOld = argParam.titleComp;
			JLabel labelNew = getTitle(argParam);
			argParam.titleComp = labelNew;
			
			
			changeComponents(oldComp, (JComponent)newComp);
			changeComponents(labelOld, labelNew);
		}//for i
		
		//Restliche Komponenten unsichtbar machen
		for(int i=argumentTypes.length+1; i<params.size(); i++){
			ParamEntry param = (ParamEntry) params.get(i);
			((JComponent)param.comp).setVisible(false);
			((JLabel)param.titleComp).setVisible(false);
		}//for i
		
		//Funktion Tooltipbeschreibung ändern
		ParamEntry funcParam = (ParamEntry) params.get(0);
		funcParam.description = entry.functionText;
		JLabel labelNew = getTitle(funcParam);
		changeComponents(funcParam.titleComp, labelNew);
		funcParam.titleComp = labelNew;
	}//updateComponents
	
	private static void changeComponents(JComponent oldComp, JComponent newComp){
		Container parent = oldComp.getParent();	
		TableLayout layout = (TableLayout)parent.getLayout(); 
		TableLayoutConstraints constraints = layout.getConstraints(oldComp);
		parent.remove(oldComp);
		parent.add(newComp, constraints);
	}//changeComponents

	protected String _refreshResult(boolean withHtml) {
		Iterator iter = params.iterator();
		ParamEntry param = (ParamEntry) iter.next();
		StringBuffer buffer = new StringBuffer();
		
		if(iter.hasNext() && !((JComponent)((ParamEntry)params.get(1)).comp).isVisible()){
			//Konstante
			return markIt(param.comp.getText(), withHtml, param.comp);
		}//if
		
		buffer = new StringBuffer(markIt(param.comp.getText() + "(", withHtml, param.comp));

		boolean firstTime = true;
		while(iter.hasNext()){
			param = (ParamEntry) iter.next();
			if( !((JComponent)param.comp).isVisible())
				break;
			
			String text = ((CanModifyText) param.comp).getText();
			
			if(firstTime){
				firstTime = false;
				if(text==null || text.length()==0)
					buffer.append(" ");
			}else{
				buffer.append(", ");
			}
			
			buffer.append(markIt(text,withHtml, param.comp));
		}//while
		
		
		param = (ParamEntry) params.iterator().next();
		buffer.append(markIt(")",withHtml,param.comp));
		return buffer.toString();
	}//refreshResult	
}//class FunctionWizardStep
