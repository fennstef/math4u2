package math4u2.view.gui.listview.complete;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import math4u2.controller.Broker;
import math4u2.controller.MathObject;
import math4u2.mathematics.functions.UserFunction;
import math4u2.view.dragAndDrop.DNDHandler;
import math4u2.view.dragAndDrop.target.DropTargetOnCompleteView;
import math4u2.view.gui.component.ColorButton;
import math4u2.view.gui.component.DefinitionField;
import math4u2.view.gui.component.LineStyleCombo;
import math4u2.view.gui.component.MathComponentView;
import math4u2.view.gui.component.ParameterComponent;
import math4u2.view.gui.component.VisibleButton;
import math4u2.view.gui.listview.AbstractListViewItem;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.layout.TableLayout;

/**
 * Darsteller einer Funkion in dem CompleteViewContainer
 * 
 * @author Fenn Stefan
 * @see CompleteViewContainer
 */
public class CompleteFunctionViewItem extends AbstractListViewItem {

	private JComponent c;

	private String oldType;

	private ParameterComponent parameterSlider;

	public CompleteFunctionViewItem(MathObject model, ListViewInterface parent,
			Broker broker) {
		super((UserFunction)model, parent, broker);
		refreshRecursive(this);
	} //Konstruktor

	protected void onceInit() {
		super.onceInit();
	} //onceInit

	public void reInit() {
		c = getCurrentCompleteView(((UserFunction) model));

		//Falls es sich noch um die alte Komponente handelt
		//wird nichts gemacht
		if (c == this && oldType != null) {
			return;
		}//if

		if (getComponentCount() != 0)
			removeAll();

		Component[] ca = c.getComponents();
		setLayout(c.getLayout());
		for (int i = 0; i < ca.length; i++) {
		    if(!(ca[i] instanceof JTextField))
		        addListener((JComponent) ca[i]);
			if (c.getLayout() instanceof TableLayout)
				add(ca[i], ((TableLayout) c.getLayout()).getConstraints(ca[i]));
			else
				add(ca[i]);
		} //for i
		setView();
	} //init

	public void setView() {
		parent.setExpandList();
	} //setView

	public void renew(MathObject source) {
		reInit();
		refreshRecursive(this);
	}//renew

	public void refreshRecursive(Container c) {
		Component[] ca = c.getComponents();
		for (int i = 0; i < ca.length; i++) {
			if (ca[i] instanceof MathComponentView) {
				((MathComponentView) ca[i]).setMathModel(model);
				((MathComponentView) ca[i]).refresh();
			} else if (ca[i] instanceof Container) {
				refreshRecursive((Container) ca[i]);
			}//else if
		} //for i

	}//refreshRecursive

	public ParameterComponent getParameterSlider() {
		return parameterSlider;
	}//getParameterSlider

	public void setParameterSlider(ParameterComponent parameterSlider) {
		this.parameterSlider = parameterSlider;
	}//setParameterSlider

	public String getOldType() {
		return oldType;
	}//getOldType

	public void setOldType(String oldType) {
		this.oldType = oldType;
	}//setOldType

	/**
	 * Es muß immer nachgeschaut werden, ob die aktuelle View noch paßt, da ein
	 * Parameter z.B. zuvor nur per Schieberegler funktioniert, später aber eine
	 * Funktion ist z.B. a:=sin(3)
	 * 
	 * @param f
	 *            Funktion, die dargestellt werden soll
	 * @return Entweder neue oder alte Komponente
	 */
	public JComponent getCurrentCompleteView(UserFunction f) {
		JPanel panel = new JPanel();
		if (f.isScalarParameter()) {
			if ("constant".equals(getOldType()))
				return this;
			else
				setOldType("constant");
			ParameterComponent parameterSlider = new ParameterComponent(f, broker,null);
			setParameterSlider(parameterSlider);
			//Layout
			double border = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;
			double size[][] = { { border, F, border }, { border, F, border } };
			panel.setLayout(new TableLayout(size));
			panel.add(parameterSlider, "1, 1, F, F");
			return panel;
		} else if (f.hasCurrentObjectGraph()) {
			parameterSlider = null;
			if ("drawable".equals(getOldType()))
				return this;
			else
				setOldType("drawable");
			VisibleButton visibleBox = new VisibleButton(f, broker);
			ColorButton colorButton = new ColorButton(f, broker);
			LineStyleCombo lineStyleCombo = new LineStyleCombo(f, broker);
			DefinitionField definitionField = new DefinitionField(f.getDefinitionHeader(),f, broker);
			
			//Layout
			double border = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;
			double size[][] = {
					{ border, P, border / 2, P, border, P, F, border },
					{ border, P, border, P, border } };

			panel.setLayout(new TableLayout(size));

			JPanel panel1 = new JPanel(new FlowLayout((int) border,
					(int) border, FlowLayout.LEFT));
			panel1.setOpaque(false);
			panel1.add(visibleBox);
			panel1.add(colorButton);
			panel1.add(lineStyleCombo);

			panel.add(panel1, "0, 1, 6, 0");
			panel.add(definitionField.getLabel(), "1, 3, L, C");
			panel.add(new JLabel(":="), "3, 3, L, C");
			panel.add(definitionField.getInput(), "5, 3, 6, 3");

			return panel;
		} else {
			parameterSlider = null;
			if ("multiOrder".equals(getOldType()))
				return this;
			else
				setOldType("multiOrder");
			//Layout
			double border = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;
			double size[][] = { { border, P, border, P, border, F, border },
					{ border, P, border } };
			panel.setLayout(new TableLayout(size));

			DefinitionField df = new DefinitionField(f.getDefinitionHeader(),f, broker);
			panel.add(df.getLabel(), "1, 1, L, C");
			panel.add(new JLabel(":="), "3, 1, L, C");
			panel.add(df.getInput(), "5, 1, 5, 1");
			return panel;
		}//else
	}//getCurrentCompleteView
	
	protected void addListener(JComponent c) {
		super.addListener(c);
		new DNDHandler(c, "CompleteView.$" + model.getKey(), broker);
		new DropTargetOnCompleteView(this, (CompleteViewBox) parent);
	}//addListener	

} //class ListBoxItem
