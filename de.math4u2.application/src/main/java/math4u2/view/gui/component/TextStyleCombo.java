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
import math4u2.mathematics.affine.TextBubble;
import math4u2.util.exception.ExceptionManager;

/**
 * Auswahl der Textumrandung bei text(...)
 * 
 * @see math4u2.mathematics.affine.TextBubbleGraph
 * @author Fenn Stefan
 */
public class TextStyleCombo extends JComboBox implements MathComponentView {

	private static final HashMap imageCache = new HashMap();
	
	protected static final String[] textStyles = { "B_NE", "B_SE", "B_SW",
			"B_NW", "A_N", "A_E", "A_S", "A_W", "POSTIT", "C_S", "C_N", "C_E",
			"C_W", "K_E", "K_W", "K_N", "K_S" };
	
	private TextBubble textBubble;

	private Broker broker;
	
	private String orientation;
	
	static {
		for (int i = 0; i < textStyles.length; i++) {
			String style = textStyles[i];
			JLabel tmp = new JLabel(Images.load("math4u2/images/textstyle/"+style+".gif"));
			imageCache.put(style,tmp);
		}//for i
	}//statischer Konstruktor
	
	protected TextStyleCombo(Object[] oa){
		super(oa);
	}//Konstruktor

	public TextStyleCombo(TextBubble textBubble, Broker broker) {
		super(textStyles);
		this.broker = broker;
		this.textBubble = textBubble;
		setRenderer(new TextStyleRenderer());
		addItemListener(new ComboItemListener());
	} //Konstruktor

	public void refresh() {
		orientation = textBubble.getOrientation();
		for (int i = 0; i < getItemCount(); i++) {
			String orient = (String) getItemAt(i);
			if (!orient.equals(orientation))
				continue;
			setSelectedIndex(i);
			break;
		} //for i
	} //refresh

	public void setMathModel(MathObject mo) {
		textBubble = (TextBubble) mo;
	} //setHasGraph

	public String getSelectedOrientation() {
		return (String) getSelectedItem();
	} //getSelectedLineStyle
	
	public void deactivate() {
		setEnabled(false);
	}//deactivate

	private final class ComboItemListener implements ItemListener {
		public void itemStateChanged(ItemEvent arg0) {
			//wenn der richtige Linienstil gewählt wurde, wird nichts gemacht
			if (getSelectedOrientation().equals(TextStyleCombo.this.textBubble.getOrientation()))
				return;
			TextStyleCombo.this.textBubble.setOrientation(getSelectedOrientation());
			try {
				TextStyleCombo.this.broker
						.propagateChange(TextStyleCombo.this.textBubble);
			} catch (BrokerException e) {
				ExceptionManager.doError("Fehler beim Erneuern des Objekts "+TextStyleCombo.this.textBubble.getKey(),e);
			}//catch
		}//itemStateChanged
	}//class ComboItemListener

	public static class TextStyleRenderer implements ListCellRenderer {
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
