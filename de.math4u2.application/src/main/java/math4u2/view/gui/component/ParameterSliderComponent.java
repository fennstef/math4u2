package math4u2.view.gui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.Border;

import math4u2.application.resource.Images;
import math4u2.view.gui.Math4U2Win;

/**
 * @author Fenn Stefan
 * 
 * Slider mit Komponenten für Min und Max
 */
public class ParameterSliderComponent extends JSlider {
    /**
     * Referenz zum ParameterSlider, hier werden die Ereignisse
     * weiterverarbeitet
     */
    private ParameterComponent parameterSlider;

    private JLabel minLabel;

    private JLabel maxLabel;

    private JTextField valueTextFieldMin;

    private JTextField valueTextFieldMax;

    public ParameterSliderComponent(int min, int max, int value,
            ParameterComponent parameterSlider) {
        super(min, max, value);
        this.parameterSlider = parameterSlider;
        initValueFrame();
    }//Konstruktor

    public void initValueFrame() {
        valueTextFieldMin = new JTextField("", 10);
        valueTextFieldMax = new JTextField("", 10);
        MinMaxListener minListener = new MinMaxListener(MinMaxListener.MIN,
                valueTextFieldMin);
        MinMaxListener maxListener = new MinMaxListener(MinMaxListener.MAX,
                valueTextFieldMax);

        minLabel = new JLabel(Images.LIMIT_LEFT);
        minLabel.setToolTipText("Minimum einstellen");
        minLabel.addMouseListener(minListener);
        add(minLabel);
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                Dimension dim = minLabel.getPreferredSize();
                minLabel.setBounds(5, getHeight() - dim.height,
                        dim.width, dim.height);
            }//componentResized
        });

        maxLabel = new JLabel(Images.LIMIT_RIGHT);
        maxLabel.addMouseListener(maxListener);
        maxLabel.setToolTipText("Maximum einstellen");
        add(maxLabel);
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                Dimension dim = maxLabel.getPreferredSize();
                maxLabel.setBounds(getPreferredSize().width - dim.width
                        - 6, getHeight() - dim.height, dim.width,
                        dim.height);
            }//componentResized
        });

        
        Border b1 = BorderFactory.createLineBorder(Color.BLACK);
        Border b2 = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        Border b3 = BorderFactory.createCompoundBorder(b1, b2);
        valueTextFieldMin.setBorder(b3);
        valueTextFieldMin.setVisible(false);
        valueTextFieldMin.addActionListener(minListener);
        valueTextFieldMin.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                valueTextFieldMin.setVisible(false);
            }//focusLost
        });

        
        valueTextFieldMax.setBorder(b3);
        valueTextFieldMax.setVisible(false);
        valueTextFieldMax.addActionListener(maxListener);
        valueTextFieldMax.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                valueTextFieldMax.setVisible(false);
            }//focusLost
        });

        Math4U2Win.getInstance().getLayeredPane().add(valueTextFieldMin,
                JLayeredPane.MODAL_LAYER);
        Math4U2Win.getInstance().getLayeredPane().add(valueTextFieldMax,
                JLayeredPane.MODAL_LAYER);

    }//initValueFrame

    class MinMaxListener extends MouseAdapter implements ActionListener {
        public static final int MIN = 0;

        public static final int MAX = 1;

        JTextField valueField;

        /** Entweder für Minimum oder Maximum zuständig */
        private int competency;

        public MinMaxListener(int competency, JTextField valueField) {
            this.competency = competency;
            this.valueField = valueField;
        }//Konstruktor

        public void mouseClicked(MouseEvent e) {
            if (valueField.isVisible()) {
                valueField.setVisible(false);
            } else {
                if (competency == MIN) {
                    valueField.setText(parameterSlider.getMinString());
                } else {
                    valueField.setText(parameterSlider.getMaxString());
                }

                Point absP = ((JComponent)e.getSource()).getLocationOnScreen();
                Point frameP = Math4U2Win.getInstance().getLocation();
                Dimension dim = valueField.getPreferredSize();
                valueField.setLocation(
                		e.getX() + absP.x - frameP.x - dim.width / 2, 
						absP.y - frameP.y - dim.height/2);
                valueField.setSize(dim);
                valueField.setVisible(true);
                valueField.requestFocus();
            }//else isVisible
        }//mouseClicked

        public void actionPerformed(ActionEvent e) {
            if (competency == MIN) {
                parameterSlider.setMinValue(valueField.getText());
            } else {
                parameterSlider.setMaxValue(valueField.getText());
            }
            valueField.setVisible(false);
        }//actionPerformed
    }//class MinMaxMouseListener

}//class ParameterSliderComponent
