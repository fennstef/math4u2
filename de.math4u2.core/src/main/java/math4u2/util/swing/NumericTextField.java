package math4u2.util.swing;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Insets;

import javax.swing.JTextField;
import javax.swing.text.Document;

/**
 * Klasse für numerische Textfelder.
 * Zur Zeit wird insbesondere die Berechnung der Spaltenbreite überschrieben.
 * 
 * @author Christoph Beckmann
 *
 */

public class NumericTextField extends JTextField {

    public NumericTextField() {
        super();
        // TODO Auto-generated constructor stub
    }

    public NumericTextField(int columns) {
        super(columns);
        // TODO Auto-generated constructor stub
    }

    public NumericTextField(String text) {
        super(text);
        // TODO Auto-generated constructor stub
    }

    public NumericTextField(String text, int columns) {
        super(text, columns);
        // TODO Auto-generated constructor stub
    }

    public NumericTextField(Document doc, String text, int columns) {
        super(doc, text, columns);
        // TODO Auto-generated constructor stub
    }

    
    public Dimension getPreferredSize() {
        FontMetrics metrics = getFontMetrics(getFont());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i <= getColumns(); i++) {
            sb.append('0');
        }
        int stringWidth=metrics.stringWidth(sb.toString());
        Dimension size = super.getPreferredSize();
        if (stringWidth != 0) {
            Insets insets = getInsets();
            size.width = stringWidth +
                insets.left + insets.right;
        }
        return size;
    }

}
