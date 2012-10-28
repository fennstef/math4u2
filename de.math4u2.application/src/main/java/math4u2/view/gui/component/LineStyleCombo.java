package math4u2.view.gui.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import math4u2.application.resource.Colors;
import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.graph.AbstractGraph;
import math4u2.view.graph.HasGraph;

/**
 * Auswahl des Strichstils
 * 
 * @author Fenn Stefan
 */
public class LineStyleCombo extends JComboBox implements MathComponentView {

	private final int WIDTH = 50;

	private final int HEIGHT = 16;

	private HasGraph hasGraph;

	private Broker broker;

	public LineStyleCombo(HasGraph hasGraph, Broker broker) {
		super(new Object[] { new Integer(HasGraph.SOLID_STROKE),
				new Integer(HasGraph.DASH_STROKE),
				new Integer(HasGraph.DOT_STROKE),
				new Integer(HasGraph.DOT_DASH_STROKE) });
		this.broker = broker;
		this.hasGraph = hasGraph;
		setRenderer(new StrokeRenderer());
		addItemListener(new ComboItemListener());
	} //Konstruktor

	public void refresh() {
		int style = hasGraph.getLineStyle();
		for (int i = 0; i < getItemCount(); i++) {
			Integer integer = (Integer) getItemAt(i);
			if (integer.intValue() != style)
				continue;
			setSelectedIndex(i);
			break;
		} //for i
		//		if(getSelectedIndex()!=2)
		//			setSelectedIndex(2);
	} //refresh

	public void setMathModel(MathObject mo) {
		hasGraph = (HasGraph) mo;
	} //setHasGraph

	public int getSelectedLineStyle() {
		return ((Integer) getSelectedItem()).intValue();
	} //getSelectedLineStyle
	
	public void deactivate() {
		setEnabled(false);
	}//deactivate

	private final class ComboItemListener implements ItemListener {
		public void itemStateChanged(ItemEvent arg0) {
			//wenn der richtige Linienstil gewählt wurde, wird nichts gemacht
			if (getSelectedLineStyle() == LineStyleCombo.this.hasGraph
					.getLineStyle())
				return;
			LineStyleCombo.this.hasGraph.setLineStyle(getSelectedLineStyle());
			try {
				LineStyleCombo.this.broker
						.propagateChange(LineStyleCombo.this.hasGraph);
			} catch (BrokerException e) {
				ExceptionManager.doError("Fehler beim Erneuern des Objekts "+LineStyleCombo.this.hasGraph.getIdentifier(),e);
			}//catch
		}//itemStateChanged
	}//class ComboItemListener

	class StrokeRenderer implements ListCellRenderer {
		HashMap cache = new HashMap();
		
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			Object obj = cache.get(value);
			
			LinePainter lp = null;
			
			if(obj!=null){
				lp = (LinePainter) obj;				
			}else{
				//neues Objekt erzeugen
				int style = ((Integer) value).intValue();
				lp = new LinePainter(style, isSelected);
				lp.setPreferredSize(new Dimension(WIDTH, HEIGHT));
				lp.doLayout();
				cache.put(value,lp);
			}//else
			
			lp.setSelected(isSelected);			
			return lp;
		} //getListCellRendererComponent
	} //class ImageRenderer

	class LinePainter extends JComponent {
		int style;

		public LinePainter(int style, boolean isSelected) {
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
			Graphics2D g = (Graphics2D) gr;
			g.setColor(Color.BLACK);
			Stroke oldStroke = g.getStroke();
			g.setStroke(AbstractGraph.getStroke(2, style));
			g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
			g.setStroke(oldStroke);
		} //paintComponents
	}//class LinePainter

} //class LineStyleCombo
