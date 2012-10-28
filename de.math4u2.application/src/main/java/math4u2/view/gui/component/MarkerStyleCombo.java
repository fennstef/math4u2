package math4u2.view.gui.component;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import math4u2.application.resource.Colors;
import math4u2.application.resource.Images;
import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.mathematics.affine.Marker;
import math4u2.util.exception.ExceptionManager;

/**
 * Auswahl der Marker-Style
 * 
 * @author Fenn Stefan
 */
public class MarkerStyleCombo extends JComboBox implements MathComponentView {

    private static final HashMap imageCache = new HashMap();

    private static final String[] markerStyles = { Marker.CIRCLE,
            Marker.CIRCLE_CROSS, Marker.CROSS1, Marker.CROSS2, Marker.SQUARE };

    private Marker marker;

    private Broker broker;
    
    static {
		for (int i = 0; i < markerStyles.length; i++) {
			String style = markerStyles[i];
			JLabel tmp = new JLabel(Images.load("math4u2/images/markerstyle/"+style+".gif"));
			imageCache.put(style,tmp);
		}//for i
	}//statischer Konstruktor

    public MarkerStyleCombo(Marker marker, Broker broker) {
        super(markerStyles);
        this.broker = broker;
        this.marker = marker;
        setRenderer(new MarkerStyleRenderer());
        addItemListener(new ComboItemListener());
    } //Konstruktor

    public void refresh() {
        String style = marker.getStyle();
        for (int i = 0; i < getItemCount(); i++) {
            String tmp = (String) getItemAt(i);
            if (!tmp.equals(style))
                continue;
            setSelectedIndex(i);
            break;
        } //for i
    } //refresh

    public void setMathModel(MathObject mo) {
        marker = (Marker) mo;
    } //setHasGraph

    public String getSelectedStyle() {
        return (String) getSelectedItem();
    } //getSelectedLineStyle

    public void deactivate() {
        setEnabled(false);
    }//deactivate

    private final class ComboItemListener implements ItemListener {
        public void itemStateChanged(ItemEvent arg0) {
            //wenn der richtige Linienstil gewählt wurde, wird nichts gemacht
            if (getSelectedStyle().equals(
                    MarkerStyleCombo.this.marker.getStyle()))
                return;
            MarkerStyleCombo.this.marker.setStyle(getSelectedStyle());
            try {
                MarkerStyleCombo.this.broker
                        .propagateChange(MarkerStyleCombo.this.marker);
            } catch (BrokerException e) {
            	ExceptionManager.doError("Fehler beim Erneuern des Objekts "+MarkerStyleCombo.this.marker.getKey(),e);
            }//catch
        }//itemStateChanged
    }//class ComboItemListener
    
	class MarkerStyleRenderer implements ListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			String  orientation = (String) value;
			JLabel label = (JLabel)imageCache.get(orientation);
			if(isSelected)
				label.setBorder(BorderFactory.createLineBorder(Colors.COMBO_BOX_SELECTED_LINE));
			else
				label.setBorder(null);
			return label;	
		} //getListCellRendererComponent
	} //class TextStyleRenderer    
} //class LineStyleCombo
