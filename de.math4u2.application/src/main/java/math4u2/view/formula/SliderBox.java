/*
 * Created on 22.02.2005
 *
 */
package math4u2.view.formula;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import math4u2.view.gui.component.DefinitionField;
import math4u2.view.gui.component.ParameterComponent;

/**
 * @author Christoph Beckmann
 *
 * Klasse für Slider in Lektionen 
 * 
 */
public class SliderBox extends AtomicBox {
    
    private JSlider sliderParameter;
    
    private DefinitionField evalParameter;
    
    private JTextField wert;
    
    public SliderBox(FormulaRenderContext frc, double min, double max,ParameterComponent pac){
        super(frc);
        setOpaque(false);
        setBorder(BorderFactory.createLineBorder(Color.black,1));
        setLayout(new FlowLayout());

        this.evalParameter=pac.getEvalParameter();
        
        this.sliderParameter= pac.getParameterSlider();
        pac.setMaxValue(String.valueOf(max));
        pac.setMinValue(String.valueOf(min));
        JLabel jlMin= new JLabel(String.valueOf(min));
        JLabel jlMax= new JLabel(String.valueOf(max));
        jlMin.setFont(FormulaRenderContext.getFont("Dialog", Font.PLAIN, 10));
        jlMax.setFont(FormulaRenderContext.getFont("Dialog", Font.PLAIN, 10));

        wert= evalParameter.getInput();
        wert.setVisible(true);
        wert.setOpaque(false);
        wert.setEditable(false);

        add(sliderParameter);
        add(wert);
        
        setBaseline(sliderParameter.getHeight()/2);
        doLayout();
    }
    
    
    /**
     * Überschriebene Methode.
     * @see math4u2.view.formula.FormulaElement#getSpacingClass()
     */
    public Class getSpacingClass() {
        return getClass();
    }

	/**
	 * Überschriebene Methode, um das Element vertikal zu zentrieren.
	 * @see math4u2.view.formula.FormulaElement#getBaseline()
	 */
	public float getBaseline() {
		return (float)super.getHeight()/2f + getRenderContext().getAxisHeight()*getDisplayHeight();
	}
    
    public Dimension getMaximumSize(){
        return getPreferredSize();
    }

}
