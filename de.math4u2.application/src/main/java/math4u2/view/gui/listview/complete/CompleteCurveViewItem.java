package math4u2.view.gui.listview.complete;

import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import math4u2.controller.Broker;
import math4u2.controller.relation.ObjectNotInRelationException;
import math4u2.mathematics.affine.Curve;
import math4u2.mathematics.functions.UserFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.dragAndDrop.DNDHandler;
import math4u2.view.dragAndDrop.target.DropTargetOnCompleteView;
import math4u2.view.graph.HasGraph;
import math4u2.view.gui.component.ColorButton;
import math4u2.view.gui.component.DefinitionField;
import math4u2.view.gui.component.LineStyleCombo;
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
public class CompleteCurveViewItem extends AbstractListViewItem {
	private VisibleButton visibleBox;

	private ColorButton colorButton;

	private DefinitionField definitionFieldX;

	private DefinitionField definitionFieldY;

	private DefinitionField definitionFieldMin;

	private DefinitionField definitionFieldMax;

	private LineStyleCombo lineStyleCombo;

	private JLabel fnLabel;

	public CompleteCurveViewItem(UserFunction model, ListViewInterface parent,
			Broker broker) {
		super(model, parent, broker);
	} //Konstruktor

	protected void onceInit() {
		//Visible Box
		visibleBox = new VisibleButton((HasGraph) model, broker);
		addListener(visibleBox);
		//color
		colorButton = new ColorButton((HasGraph) model, broker);
		addListener(colorButton);
		//Linestyle
		lineStyleCombo = new LineStyleCombo((HasGraph) model, broker);

		//function Label
		fnLabel = new JLabel();
		addListener(fnLabel);

		try {
			String name = model.getDefinitionHeader();
			String var = ((Curve)getModel()).getXFunction().getVariableNames()[0];
			//Definition Field X
			definitionFieldX = new DefinitionField(name + Broker.SEPERATOR
					+ "x("+var+")", model.getRelationContainer().getObjectByName("x"),
					broker);
			//Definition Field Y
			definitionFieldY = new DefinitionField(name + Broker.SEPERATOR
					+ "y("+var+")", model.getRelationContainer().getObjectByName("y"),
					broker);
			//Definition Field Min
			definitionFieldMin = new DefinitionField(name + Broker.SEPERATOR
					+ "min", model.getRelationContainer()
					.getObjectByName("min"), broker);
			//Definition Field Max
			definitionFieldMax = new DefinitionField(name + Broker.SEPERATOR
					+ "max", model.getRelationContainer()
					.getObjectByName("max"), broker);
		} catch (ObjectNotInRelationException e) {
			ExceptionManager.doError("Fehler beim Initialisieren der Kurven-Sicht",e);
		}//catch

		//Layout
		double border = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;
		double size[][] = {
				{ border, P, border / 2, P, border, P, F, border },
				{ border, P, border, P, border, P, border, P, border, P,
						border, P, border } };

		setLayout(new TableLayout(size));

		JPanel panel = new JPanel(new FlowLayout((int) border, (int) border,
				FlowLayout.LEFT));
		panel.setOpaque(false);
		panel.add(visibleBox);
		panel.add(colorButton);
		panel.add(lineStyleCombo);

		JPanel panel2 = new JPanel(new FlowLayout((int) border + 2,
				(int) border, FlowLayout.LEFT));
		panel2.setOpaque(false);
		panel2.add(fnLabel);

		add(panel, "0, 1, 7, 1");
		add(panel2, "0, 3, 7, 3");
		add(definitionFieldMin.getLabel(), "1, 5, L, C");
		add(new JLabel(":="), "3, 5, L, C");
		add(definitionFieldMin.getInput(), "5, 5, 6, 5");
		add(definitionFieldMax.getLabel(), "1, 7, L, C");
		add(new JLabel(":="), "3, 7, L, C");
		add(definitionFieldMax.getInput(), "5, 7, 6, 7");

		add(definitionFieldX.getLabel(), "1, 9, L, C");
		add(new JLabel(":="), "3, 9, L, C");
		add(definitionFieldX.getInput(), "5, 9, 6, 9");
		add(definitionFieldY.getLabel(), "1, 11, L, C");
		add(new JLabel(":="), "3, 11, L, C");
		add(definitionFieldY.getInput(), "5, 11, 6, 11");

		//		add(definitionFieldMin, "1, 7, 6, 7");
		//		add(definitionFieldMax, "1, 9, 6, 9");
		super.onceInit();
	}//onceInit

	public void reInit() {
		fnLabel.setText(model.toString());

		try {
			definitionFieldX.setMathModel(model.getRelationContainer()
					.getObjectByName("x"));
			definitionFieldY.setMathModel(model.getRelationContainer()
					.getObjectByName("y"));
			definitionFieldMin.setMathModel(model.getRelationContainer()
					.getObjectByName("min"));
			definitionFieldMax.setMathModel(model.getRelationContainer()
					.getObjectByName("max"));
		} catch (ObjectNotInRelationException e) {
			ExceptionManager.doError("Fehler beim Erneuern der Kurven-Sicht",e);
		}//catch

		definitionFieldX.refresh();
		definitionFieldY.refresh();
		definitionFieldMin.refresh();
		definitionFieldMax.refresh();
		colorButton.setMathModel(model);
		colorButton.refresh();
		visibleBox.setMathModel(model);
		visibleBox.refresh();
		lineStyleCombo.setMathModel(model);
		lineStyleCombo.refresh();
	}//init

	protected void addListener(JComponent c) {
		super.addListener(c);
		new DNDHandler(c, "CompleteView.$" + model.getKey(), broker);
		new DropTargetOnCompleteView(this, (CompleteViewBox) parent);
	}//addListener

} //class ListBoxItem
