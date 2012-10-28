package math4u2.view.gui.wizard.step;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import math4u2.view.gui.wizard.Math4u2WizardModel;
import math4u2.view.gui.wizard.components.WizRadioButtonGroup;
import math4u2.view.gui.wizard.title.HasTitle;
import math4u2.view.gui.wizard.types.WizParamType;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;

/**
 * Entscheidungswizard, ob Matrix/Vektor/Dualvektor implizit,
 * explizit oder über eine Datei geladen werden sollen.
 *
 */
public class ListInstantiateChoiceStep extends StandardWizardStep {

	private WizRadioButtonGroup comp;
	private NextStepListener nextStepListener = new NextStepListener();
	private ChangedSelectionListener changedSelectionListener = new ChangedSelectionListener();
	
	public ListInstantiateChoiceStep(String fullname, HasTitle previousTitle,
			String name, String description) {
		super(fullname, previousTitle, name, description, WizParamType.ANY_INPUT);
		setIcon((ImageIcon)ChooseObjectWizardStep.getType(fullname).image);
	}//Konstruktor

	public void init(WizardModel model) {
		this.model = (Math4u2WizardModel) model;		
		setComplete(true);
	}//init
	
	public void prepare(){
		removeAll();

		String [] types = {"explizit", "implizit"};
		String [] descriptions = {
				"Explizite Definition z.B. <funktion(x)> liste({x, x^2, x^3})",
				"Implizite Definition z.B. <funktion(x)> liste(i, 3, x^i)"			
		};
		char[] mnenomics = {'e','i'};
		
		WizRadioButtonGroup.Entry[] list = new WizRadioButtonGroup.Entry[types.length];		
		for(int i=0; i<types.length; i++){
			list[i] = new WizRadioButtonGroup.Entry(descriptions[i],types[i],mnenomics[i]);
		}//for i
		comp = new WizRadioButtonGroup(list);
		comp.addChangeListener(changedSelectionListener);
		comp.addFinishListener(nextStepListener);
		((ParamEntry)params.get(0)).comp = comp;
		
		add(comp);	
		refreshResult();
		
		setFirstTimeFocus(comp);
	}//prepare

	protected void refreshResult() {
		String text = comp.getText();
		if(text!=null)
			model.put("model", "Liste "+comp.getText());
		refreshErrorHighlights();
	}//refreshResult
	
	public void applyState() throws InvalidStateException {		
	}//applyState
	
	class NextStepListener extends MouseAdapter implements MouseListener ,KeyListener{
		public void mouseClicked(MouseEvent event){
			if(event.getClickCount()==2)
				model.nextStep();
		}//mouseClicked

		public void keyTyped(KeyEvent e) {
			if(e.getKeyChar()=='\n' || e.getKeyChar()==' ')
				model.nextStep();
		}//keyReleased

		public void keyReleased(KeyEvent arg0) {}
		public void keyPressed(KeyEvent arg0) {}		
	}//class NextStepListener
	
 	class ChangedSelectionListener implements ActionListener, ChangeListener{
		public void actionPerformed(ActionEvent e) {
			refreshResult();
		}//actionPerformed

		public void stateChanged(ChangeEvent e) {
			refreshResult();
		}//stateChanged
	}//class ChangeListener

}//class MatrixInstantiateChoiceStep
