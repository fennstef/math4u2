package math4u2.view.gui.listview.complete;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
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
 * Darsteller einer Fläche in dem CompleteViewContainer
 * 
 * @author Fenn Stefan
 * @see CompleteViewContainer
 */
public class CompleteAngleViewItem extends AbstractListViewItem {
	private VisibleButton visibleBox;

	private ColorBorderButton colorButton;

	private ColorAreaButton areaColorButton;

	private LineStyleCombo lineStyleCombo;

	private JLabel fnLabel;
	
	private DefinitionField apex;	
	
	private DefinitionField vector1;
	
	private DefinitionField vector2;
	
	private DefinitionField distance;


	public CompleteAngleViewItem(UserFunction model, ListViewInterface parent,
			Broker broker) {
		super(model, parent, broker);
	} //Konstruktor

	protected void onceInit() {
		//Visible Box
		visibleBox = new VisibleButton((HasGraph) model, broker);
		addListener(visibleBox);
		//color
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
		    String head = model.getDefinitionHeader();
			apex = new DefinitionField(head+Broker.SEPERATOR+"scheitelpunkt",getMathObject().getRelationContainer()
					.getObjectByName("scheitelpunkt"), broker);
			vector1 = new DefinitionField(head+Broker.SEPERATOR+"vektor1",getMathObject().getRelationContainer()
					.getObjectByName("vektor1"), broker);
			vector2 = new DefinitionField(head+Broker.SEPERATOR+"vektor2",getMathObject().getRelationContainer()
					.getObjectByName("vektor2"), broker);	
			distance = new DefinitionField(head+Broker.SEPERATOR+"radius",getMathObject().getRelationContainer()
					.getObjectByName("radius"), broker);		
		} catch (ObjectNotInRelationException e) {
			ExceptionManager.doError("Fehler beim Initialisieren der Winkel-Sicht",e);
		}		

		//Layout
		double border = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;
		double size[][] = {
		/* Zeilen */// 
				{ border, P, border, P, border, P, F, border },
				/* Spalten */// 
				{ border, P, border, P, border, P, border, P, border, P, border, P, border } };

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
		add(new JLabel(":="), "3, 5, L, C");
		add(apex.getLabel(), "1, 5, L, C");
		add(new JLabel(":="), "3, 5, L, C");
		add(apex.getInput(), "5, 5, 6, 5");
		add(vector1.getLabel(), "1, 7, L, C");
		add(new JLabel(":="), "3, 7, L, C");
		add(vector1.getInput(), "5, 7, 6, 5");
		add(vector2.getLabel(), "1, 9, L, C");
		add(new JLabel(":="), "3, 9, L, C");
		add(vector2.getInput(), "5, 9, 6, 5");		
		add(distance.getLabel(), "1, 11, L, C");
		add(new JLabel(":="), "3, 11, L, C");
		add(distance.getInput(), "5, 11, 6, 5");			
		
		super.onceInit();
	} //onceInit

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
			apex.setMathModel(model.getRelationContainer().getObjectByName(
					"scheitelpunkt"));
			vector1.setMathModel(model.getRelationContainer().getObjectByName(
			"vektor1"));
			vector2.setMathModel(model.getRelationContainer().getObjectByName(
			"vektor2"));
			distance.setMathModel(model.getRelationContainer().getObjectByName(
			"radius"));				
		} catch (ObjectNotInRelationException e) {
			ExceptionManager.doError("Fehler beim Erneuern der Winkel-Sicht",e);
		}//catch
		apex.refresh();
		vector1.refresh();
		vector2.refresh();
		distance.refresh();
	} //init

	private MathObject getMathObject() {
		try {
			return broker.getObject((String) model.getKey());
		} catch (BrokerException e) {
			ExceptionManager.doError("Fehler beim Initialisieren der Winkel-Sicht",e);
			throw new RuntimeException(e);
		} //catch (BrokerException e)
	} //MathObject getMathObject()

	protected void addListener(JComponent c) {
		super.addListener(c);
		new DNDHandler(c, "CompleteView.$" + model.getKey(), broker);
		new DropTargetOnCompleteView(this, (CompleteViewBox) parent);
	} //addListener

} //class ListBoxItem
