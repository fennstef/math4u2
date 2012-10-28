package math4u2.view.gui.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import math4u2.application.resource.Colors;
import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.HasGraph;
import math4u2.view.graph.MapGraph;

/**
 * Auswahl des Farbkarte. Dabei wird der Index der Farbkarte als
 * Index des Strichstils gespeichert.
 * 
 * @author Fenn Stefan
 */
public class ColorMapCombo extends JComboBox implements MathComponentView {

	private final int WIDTH = 150;

	private final int HEIGHT = 16;

	private HasGraph hasGraph;

	private Broker broker;

	public ColorMapCombo(HasGraph hasGraph, Broker broker) {
		super();
		this.broker = broker;
		this.hasGraph = hasGraph;
		init();
	} //Konstruktor
	
	private void init(){
		for(int i=0; i<MapGraph.gradientMaps.length; i++){
			((DefaultComboBoxModel)getModel()).addElement(new Integer(i));	
		}//for i
		setRenderer(new ColorMapRenderer());
		addItemListener(new ComboItemListener());
	}//init

	public void refresh() {
		int style = hasGraph.getLineStyle();
		setSelectedIndex(style);
	} //refresh

	public void setMathModel(MathObject mo) {
		hasGraph = (HasGraph) mo;
	} //setHasGraph

	public int getSelectedColorMap() {
		return ((Integer) getSelectedItem()).intValue();
	} //getSelectedLineStyle
	
	public void deactivate() {
		setEnabled(false);
	}//deactivate

	private final class ComboItemListener implements ItemListener {
		public void itemStateChanged(ItemEvent arg0) {
			//wenn der richtige Linienstil gewählt wurde, wird nichts gemacht
			if (getSelectedColorMap() == ColorMapCombo.this.hasGraph
					.getLineStyle())
				return;
			ColorMapCombo.this.hasGraph.setLineStyle(getSelectedColorMap());
			try {
				ColorMapCombo.this.broker
						.propagateChange(ColorMapCombo.this.hasGraph);
			} catch (BrokerException e) {
				ExceptionManager.doError("Fehler beim Erneuern des Objekts "+ColorMapCombo.this.hasGraph.getIdentifier(),e);
			}//catch
		}//itemStateChanged
	}//class ComboItemListener

	class ColorMapRenderer implements ListCellRenderer {
		HashMap cache = new HashMap();
		
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Object obj = cache.get(value);
			
			ColorMapPainter lp = null;
			
			if(obj!=null){
				lp = (ColorMapPainter) obj;				
			}else{
				//neues Objekt erzeugen
				lp = new ColorMapPainter(((Integer)value).intValue(), isSelected);
				lp.setPreferredSize(new Dimension(WIDTH, HEIGHT));
				lp.doLayout();
				cache.put(value,lp);
			}//else
			
			lp.setSelected(isSelected);			
			return lp;
		} //getListCellRendererComponent
	} //class ImageRenderer

	class ColorMapPainter extends JComponent {
		int style;

		public ColorMapPainter(int style, boolean isSelected) {
			this.style = style;
			setSelected(isSelected);
		}

		public void setSelected(boolean isSelected) {
			if(isSelected)
				setBorder(BorderFactory.createLineBorder(Colors.COMBO_BOX_SELECTED_LINE));
			else
				setBorder(null);
		}//setSelected

		public void paintComponent(Graphics gr) {
			int[][] map = MapGraph.gradientMaps;
			Graphics2D g = (Graphics2D) gr;
			for(int i=0; i<getWidth(); i++){
				int pos = i*(MapGraph.GRADIENT_WIDTH/getWidth());
				int color = map[style][pos];
				g.setColor(new Color(color));
				g.drawLine(i,0,i,getHeight());
			}
		} //paintComponents
	}//class LinePainter

} //class LineStyleCombo
