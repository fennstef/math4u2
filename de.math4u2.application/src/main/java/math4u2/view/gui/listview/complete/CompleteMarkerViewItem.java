package math4u2.view.gui.listview.complete;

import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import math4u2.controller.Broker;
import math4u2.controller.relation.ObjectNotInRelationException;
import math4u2.mathematics.affine.Marker;
import math4u2.mathematics.functions.UserFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.dragAndDrop.DNDHandler;
import math4u2.view.dragAndDrop.target.DropTargetOnCompleteView;
import math4u2.view.graph.HasGraph;
import math4u2.view.gui.component.ColorButton;
import math4u2.view.gui.component.DefinitionField;
import math4u2.view.gui.component.MarkerStyleCombo;
import math4u2.view.gui.component.VisibleButton;
import math4u2.view.gui.listview.AbstractListViewItem;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.layout.TableLayout;

/**
 * Darsteller eines Markers in dem CompleteViewContainer
 * 
 * @author Fenn Stefan
 * @see CompleteViewContainer
 */
public class CompleteMarkerViewItem extends AbstractListViewItem {
	private VisibleButton visibleBox;

	private ColorButton colorButton;

	private JLabel fnLabel;

	private DefinitionField px;

	private DefinitionField py;
	
	private MarkerStyleCombo markerStyleCombo;

	public CompleteMarkerViewItem(UserFunction model, ListViewInterface parent,
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
		//function Label
		fnLabel = new JLabel();
		addListener(fnLabel);

		try {
			String name = model.getDefinitionHeader();
			px = new DefinitionField(name+Broker.SEPERATOR+"x",model.getRelationContainer()
					.getObjectByName("x"), broker);
			py = new DefinitionField(name+Broker.SEPERATOR+"y",model.getRelationContainer()
					.getObjectByName("y"), broker);
		} catch (ObjectNotInRelationException e) {
			ExceptionManager.doError("Fehler beim Initialisieren der Marker-Sicht",e);
		}
		
		//markerStyleCombo
		markerStyleCombo = new MarkerStyleCombo((Marker) getModel(), broker);

		//Layout
		double border = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;
		double size[][] = {
		/* Zeilen */// 
				{ border, P, border, P, border, P, F, border },
				/* Spalten */// 
				{ border, P, border, P, border, P, border, P, border } };

		setLayout(new TableLayout(size));

		JPanel panel = new JPanel(new FlowLayout((int) border, (int) border,
				FlowLayout.LEFT));
		panel.setOpaque(false);
		panel.add(visibleBox);
		panel.add(colorButton);
		panel.add(markerStyleCombo);

		JPanel panel2 = new JPanel(new FlowLayout((int) border + 2,
				(int) border, FlowLayout.LEFT));
		panel2.setOpaque(false);
		panel2.add(fnLabel);

		add(panel, "0, 1, 7, 1");
		add(panel2, "0, 3, 7, 3");
		add(px.getLabel(), "1, 5, L, C");
		add(new JLabel(":="), "3, 5, L, C");
		add(px.getInput(), "5, 5, 6, 5");
		add(py.getLabel(), "1, 7, L, C");
		add(new JLabel(":="), "3, 7, L, C");
		add(py.getInput(), "5, 7, 6, 7");

		super.onceInit();
	} //onceInit

	public void reInit() {
		fnLabel.setText(model.toString());
		colorButton.setMathModel(model);
		colorButton.refresh();
		visibleBox.setMathModel(model);
		visibleBox.refresh();
		try {
			px.setMathModel(model.getRelationContainer().getObjectByName("x"));
			py.setMathModel(model.getRelationContainer().getObjectByName("y"));
		} catch (ObjectNotInRelationException e) {
			e.printStackTrace();
		}
		px.refresh();
		py.refresh();
		markerStyleCombo.setMathModel(getModel());
		markerStyleCombo.refresh();
	} //init

	protected void addListener(JComponent c) {
		super.addListener(c);
		new DNDHandler(c, "CompleteView.$" + model.getKey(), broker);
		new DropTargetOnCompleteView(this, (CompleteViewBox) parent);
	} //addListener

} //class ListBoxItem
