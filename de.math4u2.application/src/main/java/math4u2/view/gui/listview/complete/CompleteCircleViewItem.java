package math4u2.view.gui.listview.complete;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import math4u2.controller.Broker;
import math4u2.controller.relation.ObjectNotInRelationException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.dragAndDrop.DNDHandler;
import math4u2.view.dragAndDrop.target.DropTargetOnCompleteView;
import math4u2.view.graph.HasGraph;
import math4u2.view.gui.component.ColorAreaButton;
import math4u2.view.gui.component.ColorBorderButton;
import math4u2.view.gui.component.DefinitionField;
import math4u2.view.gui.component.LineStyleCombo;
import math4u2.view.gui.component.VisibleButton;
import math4u2.view.gui.listview.AbstractListViewItem;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.layout.TableLayout;

/**
 * Darsteller eines Punktes in dem CompleteViewContainer
 * 
 * @author Fenn Stefan
 * @see CompleteViewContainer
 */
public class CompleteCircleViewItem extends AbstractListViewItem {
	private VisibleButton visibleBox;

	private ColorBorderButton colorButton;

	private ColorAreaButton areaColorButton;

	private LineStyleCombo lineStyleCombo;

	private JLabel fnLabel;

	private DefinitionField center;

	private DefinitionField radius;

	public CompleteCircleViewItem(UserFunction model, ListViewInterface parent,
			Broker broker) {
		super(model, parent, broker);
	} //CompleteCircleViewItem(MathObje...

	protected void onceInit() {
		//Visible Box
		visibleBox = new VisibleButton((HasGraph) model, broker);
		addListener(visibleBox);
		//colorButton
		colorButton = new ColorBorderButton((HasGraph) model, broker);
		addListener(colorButton);
		//areaColorButton
		areaColorButton = new ColorAreaButton((HasGraph) model, broker);
		addListener(areaColorButton);
		//Linestyle
		lineStyleCombo = new LineStyleCombo((HasGraph) model, broker);
		//function Label
		fnLabel = new JLabel();
		addListener(fnLabel);

		try {
			String name = model.getDefinitionHeader();
			center = new DefinitionField(name+Broker.SEPERATOR+"mitte",model.getRelationContainer()
					.getObjectByName("mitte"), broker);
			radius = new DefinitionField(name+Broker.SEPERATOR+"radius",model.getRelationContainer()
					.getObjectByName("radius"), broker);
		} catch (ObjectNotInRelationException e) {
			ExceptionManager.doError("Fehler beim Initialisieren der Kreis-Sicht",e);
		}

		//Layout
		double border = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;
		double size[][] = {
		/* Zeilen */// 
				{ border, P, border, P, border, P, F, border },
				/* Spalten */// 
				{ border, P, border, P, border, P, border, P, border } //
		};

		setLayout(new TableLayout(size));
		
		JPanel gap = new JPanel();
		gap.setPreferredSize(new Dimension(10, 10));
		gap.setOpaque(false);

		JPanel panel = new JPanel(new FlowLayout((int) border, (int) border,
				FlowLayout.LEFT));
		panel.setOpaque(false);
		panel.add(visibleBox);
		panel.add(colorButton);
		panel.add(lineStyleCombo);
		panel.add(gap);
		panel.add(areaColorButton);
		panel.add(areaColorButton.getAlphaSlider());

		JPanel panel2 = new JPanel(new FlowLayout((int) border + 2,
				(int) border, FlowLayout.LEFT));
		panel2.setOpaque(false);
		panel2.add(fnLabel);

		add(panel, "0, 1, 7, 1");
		add(panel2, "0, 3, 7, 3");
		add(center.getLabel(), "1, 5, L, C");
		add(new JLabel(":="), "3, 5, L, C");
		add(center.getInput(), "5, 5, 6, 5");
		add(radius.getLabel(), "1, 7, L, C");
		add(new JLabel(":="), "3, 7, L, C");
		add(radius.getInput(), "5, 7, 6, 7");

		super.onceInit();
	} //onceInit()

	public void reInit() {
		fnLabel.setText(model.toString());
		colorButton.setMathModel(model);
		colorButton.refresh();
		areaColorButton.setMathModel(model);
		areaColorButton.refresh();
		visibleBox.setMathModel(model);
		visibleBox.refresh();
		lineStyleCombo.setMathModel(model);
		lineStyleCombo.refresh();
		
		try {
			center.setMathModel(model.getRelationContainer().getObjectByName(
					"mitte"));
			radius.setMathModel(model.getRelationContainer().getObjectByName(
					"radius"));
		} catch (ObjectNotInRelationException e) {
			ExceptionManager.doError("Fehler beim Erneuern der Kreis-Sicht",e);
		}//catch
		center.refresh();
		radius.refresh();
	} //reInit()

	protected void addListener(JComponent c) {
		super.addListener(c);
		new DNDHandler(c, "CompleteView.$" + model.getKey(), broker);
		new DropTargetOnCompleteView(this, (CompleteViewBox) parent);
	} //addListener(JComponent c)

} //class CompleteCircleViewItem ex...
