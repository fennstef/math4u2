package math4u2.view.gui.listview.complete;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.relation.ObjectNotInRelationException;
import math4u2.mathematics.affine.GradMap;
import math4u2.mathematics.affine.Map;
import math4u2.mathematics.functions.UserFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.dragAndDrop.DNDHandler;
import math4u2.view.dragAndDrop.target.DropTargetOnCompleteView;
import math4u2.view.graph.HasGraph;
import math4u2.view.gui.component.ColorMapCombo;
import math4u2.view.gui.component.DefinitionField;
import math4u2.view.gui.component.VisibleButton;
import math4u2.view.gui.listview.AbstractListViewItem;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.layout.TableLayout;

/**
 * Darsteller einer Farbkarte in dem CompleteViewContainer
 * 
 * @author Fenn Stefan
 * @see CompleteViewContainer
 */
public class CompleteMapViewItem extends AbstractListViewItem {
	private VisibleButton visibleBox;
	private JLabel fnLabel;
	private DefinitionField mapFunction;
	private DefinitionField bandVector;
	private DefinitionField gradation;
	private ColorMapCombo colorMapCombo;
	private JSlider slider;

	public CompleteMapViewItem(UserFunction model, ListViewInterface alv,
			Broker broker) {
		super(model, alv, broker);
	} //Konstruktor

	protected void onceInit() {
		//Visible Box
		visibleBox = new VisibleButton((HasGraph) model, broker);
		addListener(visibleBox);
		//function Label
		fnLabel = new JLabel();
		addListener(fnLabel);
		//ColorMapCombo
		colorMapCombo = new ColorMapCombo((HasGraph) model, broker);

		try {
		    String name = model.getDefinitionHeader();
		    String[] var = ((Map)getModel()).getMapFunc().getVariableNames();
			mapFunction = new DefinitionField(name+Broker.SEPERATOR+"mapFunction("+var[0]+", "+var[1]+")",getModel().getRelationContainer()
					.getObjectByName("mapFunction"), broker);			
			bandVector = new DefinitionField(name+Broker.SEPERATOR+"bandVector",getModel().getRelationContainer()
					.getObjectByName("bandVector"), broker);			
			if(getModel() instanceof GradMap){
				var = ((GradMap)getModel()).getGradFunc().getVariableNames();
				gradation = new DefinitionField(name+Broker.SEPERATOR+"gradation("+var[0]+")",getModel().getRelationContainer()
					.getObjectByName("gradation"), broker);
			}//if
		} catch (ObjectNotInRelationException e) {
			ExceptionManager.doError("Fehler beim Initialisieren der Farbkarten-Sicht",e);
		}

		//Layout
		double border = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;
		double size[][] = {
		/* Zeilen */// 
				{ border, P, border, P, border, P, F, border },
				/* Spalten */// 
				{ border, P, border, P, border, P, border, P, border, P, border } };

		setLayout(new TableLayout(size));

		JPanel panel = new JPanel(new FlowLayout((int) border, (int) border,
				FlowLayout.LEFT));
		panel.setOpaque(false);
		panel.add(visibleBox);
		panel.add(colorMapCombo);
		panel.add(getAlphaSlider());

		JPanel panel2 = new JPanel(new FlowLayout((int) border + 2,
				(int) border, FlowLayout.LEFT));
		panel2.setOpaque(false);
		panel2.add(fnLabel);

		add(panel, "0, 1, 7, 1");
		add(panel2, "0, 3, 7, 3");
		add(mapFunction.getLabel(), "1, 5, L, C");
		add(new JLabel(":="), "3, 5, L, C");
		add(mapFunction.getInput(), "5, 5, 6, 5");
		add(bandVector.getLabel(), "1, 7, L, C");
		add(new JLabel(":="), "3, 7, L, C");
		add(bandVector.getInput(), "5, 7, 6, 7");
		if(gradation!=null){
			add(gradation.getLabel(), "1, 9, L, C");
			add(new JLabel(":="), "3, 9, L, C");
			add(gradation.getInput(), "5, 9, 6, 7");
		}
		super.onceInit();
	} //onceInit

	public void reInit() {
		fnLabel.setText(model.toString());
		visibleBox.setMathModel(model);
		visibleBox.refresh();
		try {
			mapFunction.setMathModel(model.getRelationContainer().getObjectByName("mapFunction"));
			bandVector.setMathModel(model.getRelationContainer().getObjectByName("bandVector"));
			if(((Map)getModel()) instanceof GradMap){
				gradation.setMathModel(model.getRelationContainer().getObjectByName("gradation"));
				gradation.refresh();
			}//if
		} catch (ObjectNotInRelationException e) {
			e.printStackTrace();
		}
		mapFunction.refresh();
		bandVector.refresh();
				
		colorMapCombo.setMathModel(model);
		colorMapCombo.refresh();
		
		slider.setValue(model.getColor().getAlpha());
	} //init

	protected void addListener(JComponent c) {
		super.addListener(c);
		new DNDHandler(c, "CompleteView.$" + model.getKey(), broker);
		new DropTargetOnCompleteView(this, (CompleteViewBox) parent);
	} //addListener
	
	public JSlider getAlphaSlider() {
		slider = new JSlider(0, 255, 255 / 2);
		slider.setValue(model.getColor().getAlpha());
		Dimension dim =new Dimension(130, (int)slider.getPreferredSize()
				.getHeight());
		slider.setPreferredSize(dim);
		slider.addMouseListener(new MouseAdapter(){
			public void mouseReleased(MouseEvent arg0) {
				Color c = model.getColor();
				applyColor(c);
				try {
					broker.propagateChange(model);
				} catch (BrokerException e) {
					ExceptionManager.doError("Fehler beim Erneuern der Farbkarten-Sicht",e);
				}//catch				
			}});
		slider.setOpaque(false);
		return slider;
	}//getAlphaSlider

	public void applyColor(Color c) {
		Color c2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), slider
				.getValue());
		model.setColor(c2);
	}//applyColor	
	
} //class ListBoxItem
