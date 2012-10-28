package math4u2.view.gui.component;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import math4u2.application.resource.Colors;
import math4u2.controller.Broker;
import math4u2.controller.MathObject;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.termnodes.TermNode;
import math4u2.mathematics.termnodes.TermNodeNum;
import math4u2.parser.parser;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.NotImplementedException;
import math4u2.util.exception.parser.ParseException;
import math4u2.view.formula.FormulaElement;
import math4u2.view.formula.OrdBox;
import math4u2.view.graph.HasGraph;

/**
 * Hier können für graphische Objekte z.B. Kreis, Punkt usw. und Funktionen
 * Definitionen eingegeben werden.
 * 
 * @author Fenn Stefan
 */
public class DefinitionField implements MathComponentView {

	protected MathObject mo;

	protected String head;

	protected Broker broker;

	protected MathTextField definitionInput;

	protected JLabel bodyLabel;

	public DefinitionField(String head, MathObject mo, Broker broker) {
		this.broker = broker;
		this.mo = mo;
		this.head = head;
		init();
	} //Konstruktor

	public void init() {
		//BodyLabel z.B. f(x)
		bodyLabel = new JLabel(head);

		//DefinitionInput z.B. sin(x)+cos(x)
		definitionInput = new MathTextField();
		definitionInput.setBackground(Colors.TEXT_FIELD);
		Border b1 = BorderFactory.createLineBorder(Color.BLACK);
		Border b2 = BorderFactory.createEmptyBorder(2,5,2,5);
		Border b3 = BorderFactory.createCompoundBorder(b1,b2);
		definitionInput.setBorder(b3);
		InputActionListener ial = new InputActionListener();
		definitionInput.addActionListener(ial);
		definitionInput.addFocusListener(ial);
		definitionInput.setDropTarget(null);

		refresh();
	} //init

	public void refresh() {
		if (mo == null)
			return;
		
		if (mo instanceof UserFunction){
			UserFunction func = (UserFunction) mo;
			if(func.isEncapsulated()){
				definitionInput.setText(func.getTermString());
				return;
			}
			TermNode term = func.getFunction();
			if(!(term instanceof TermNodeNum)){
				definitionInput.setText(func.getTermString());
			}else{
				//gibt die Zahl vollständig aus
				try {
					if(func.getArity()!=0){
						definitionInput.setText(func.getTermString());
					}else{
						definitionInput.setText(func.evalScalar()+"");
					}
					//definitionInput.setCaretPosition(0);
				} catch (MathException e) {
					ExceptionManager.doError("Fehler beim Evaluieren der Funktion "+func.getName(),e);
				}//catch
			}
		}else{
			throw new NotImplementedException("Das Objekt ist " + mo.getClass());
		}//else
	} //refresh

	public JLabel getLabel() {
		return bodyLabel;
	}//getLabel

	public JTextField getInput() {
		return definitionInput;
	}//getInput

	/**
	 * setMathModel
	 */
	public void setMathModel(MathObject mo) {
		this.mo = mo;
		definitionInput.setEnabled(!(mo instanceof HasGraph && ((HasGraph)mo).isFreeze()));
	} //setMathModel
	
	public void deactivate() {
		definitionInput.deactivate();
	}//deactivate

	class InputActionListener implements ActionListener, FocusListener {
		public void actionPerformed(ActionEvent e) {
			String definition = head + ":=" + definitionInput.getText();
			try {
				parser.NEWParseDefinition(definition, true, broker);
			} catch (ParseException ex) {
				ExceptionManager.doError("Fehler bei der Definition "+definition,ex);
			}
		} //actionPerformed

		public void focusGained(FocusEvent e) {
		}

		public void focusLost(FocusEvent e) {
			actionPerformed(null);
		}
	}//class InputActionListener

	class MathTextField extends JTextField implements MathComponentView, FormulaElement {

		public void refresh() {
			DefinitionField.this.refresh();
		}//refresh

		public void setMathModel(MathObject mo) {
			DefinitionField.this.setMathModel(mo);
			setEnabled(!(mo instanceof HasGraph && ((HasGraph)mo).isFreeze()));
		}//setMathModel

		/* (non-Javadoc)
		 * @see math4u2.view.formula.FormulaElement#getBaseline()
		 */
		public float getBaseline() {
			// TODO: 5/12-tel mit RenderContext.getAxisHeight() ersetzen
			return (float)super.getHeight()/2f + 5f*getDisplayHeight()/16f;
		}

		/* (non-Javadoc)
		 * @see math4u2.view.formula.FormulaElement#getSpacingClass()
		 */
		public Class getSpacingClass() {
			return OrdBox.class;
		}

		/* (non-Javadoc)
		 * @see math4u2.view.formula.FormulaElement#getDisplayHeight()
		 */
		public float getDisplayHeight() {
			return 16;
		}

		public float getXHeight() {
			return 0f;
		}

		public void deactivate() {
			definitionInput.setEnabled(false);
			definitionInput.setFocusable(false);
		}//deactivate

		public float getRealAscend() {
			return getBaseline();
		}

		public float getRealDescend() {
			return super.getHeight()-getBaseline();
		}

	}//class MathTextField

} //class DefinitonField
