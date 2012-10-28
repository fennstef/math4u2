package math4u2.view.gui.component;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import math4u2.application.resource.Images;
import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.NotImplementedException;
import math4u2.view.Formatierer;
import math4u2.view.formula.FormulaRenderContext;
import math4u2.view.layout.TableLayout;

/**
 * Diese Komponente stellt einen Parameter als Slider oder als Definition dar.
 * 
 * @author Fenn Stefan
 */
public class ParameterComponent extends JPanel implements MathComponentView {
    /** Referenz zum Broker */
    private Broker broker;

    /** Referenz zum Parameter */
    private UserFunction parameter;

    /** Slider */
    private ParameterSliderComponent sliderParameter;

    /** Labeltable für Slider */
    private Hashtable labelTable;

    /** Null Position */
    private JLabel labelZero = new JLabel("0");

    /** aktueller Wert des Parameters */
    private DefinitionField wertParameter;
    
    /** evaluierter Wert des Parameters */
    private DefinitionField evalParameter;
    
    /** Schaltelement für Slider / Definition - Sicht */
    private JCheckBox viewModeCheckBox;

    /** Grenzen des Sliders */
    private double pMin = -5, pMax = 5;

    /** Aktuelle Eingabe des Minimums */
    private String paramMinStr;

    /** Aktuelle Eingabe des Maximums */
    private String paramMaxStr;
    
    /** Soll die Veränderung des Sliders ignoriert werden? */
    private boolean ignoreSliderChange = true;
    
    /** Formatierer für String-Repräsentation von Zahlen */
    private Formatierer formatierer;

    public ParameterComponent(UserFunction parameter, Broker broker, Formatierer formatierer) {
        this.broker = broker;
        this.parameter = parameter;
        this.formatierer = formatierer;
        init();
    } //Konstruktor

    private UserFunction getParameter() {
        return parameter;
    }//getFunction

    /**
     * Initialisierung aller Komponenten
     *
     */    
    private void init() {
        labelZero = new JLabel("0");
        labelZero.setFont(FormulaRenderContext.getFont("Dialog", Font.PLAIN, 10));
        // "="
        JLabel paramEq = new JLabel(parameter.getName() + " := ");
        // wertParamter
        String head = parameter.getDefinitionHeader();
        wertParameter = new DefinitionField(head,parameter, broker);
        wertParameter.getInput().setColumns(6);
        wertParameter.getInput().setVisible(false);
        //evalParameter
        final Formatierer f2 = formatierer;
        evalParameter = new DefinitionField(head, parameter, broker){       	
        	public void refresh() {        		
        		if (mo == null)
        			return;
        		if (mo instanceof UserFunction){
       				//gibt die Zahl vollständig aus
					try {
						double value = ((UserFunction)mo).evalScalar();
						if(f2!=null){
							int cols = this.getInput().getColumns();
					        if (cols <= 0)
					            cols = 500;
					        else if(cols <=3){
					        	cols = 3;
					        }else{
					        	cols = this.getInput().getColumns();
					        }
							definitionInput.setText(f2.value2StringSpecial(value,cols));
						}else{
							definitionInput.setText(Formatierer.value2FloatString(value));
						}
	   					definitionInput.setCaretPosition(0);						
					} catch (MathException e) {
						ExceptionManager.doError("Fehler beim Evaluieren der Funktion "+mo.getIdentifier(),e);
					}
        		}else{
        			throw new NotImplementedException("Das Objekt ist " + mo.getClass());
        		}//else
        	} //refresh        	
        };
        evalParameter.getInput().setColumns(10);
        //viewModeCheckBox
        viewModeCheckBox = new JCheckBox();
        viewModeCheckBox.setBorder(null);
        viewModeCheckBox.setSelectedIcon(Images.SLIDER_MODE);
        viewModeCheckBox.setIcon(Images.TEXT_MODE);
        viewModeCheckBox.setPreferredSize(new Dimension(12, 30));  
        viewModeCheckBox.setOpaque(false);
        viewModeCheckBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
			    showSliderDisplay(!viewModeCheckBox.isSelected());				
			}
		});
        
        //Slider
        sliderParameter = new ParameterSliderComponent(0, 100, 50, this);     
        int maxi = (int) sliderParameter.getPreferredSize().width;
        sliderParameter.setMaximum(maxi);
        setSliderParameterTable();
        sliderParameter.setPaintLabels(true);
        sliderParameter.addChangeListener(new SliderListener());
        sliderParameter.setPreferredSize(new Dimension(150, sliderParameter
                .getPreferredSize().height));
        sliderParameter.setOpaque(false);

		//Layout
		double border = 5, gap = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;
		double size[][] = {
				/* Zeilen */// 
				{ border, P, 0, P, 0, P, gap*2, F, border },
				/* Spalten */// 
				{ 5, P, 5 } };

		setLayout(new TableLayout(size));

		add(paramEq, 					"1, 1, L, C");
		add(wertParameter.getInput(), 	"3, 1, F, C");		
		add(sliderParameter,			"3, 1, L, C");
		add(viewModeCheckBox,			"5, 1, L, C");
		add(evalParameter.getInput(),	"7, 1, L, C");
        
        setOpaque(false);        
        showSliderDisplay(parameter.isNumber());
    } //init
    
    public void showSliderDisplay(boolean b){
        viewModeCheckBox.setSelected(!b);
        sliderParameter.setVisible(b);
		wertParameter.getInput().setVisible(!b);
    }//setSliderDisplay
 
    /**
     * MouseListener werden auch an den Slider angefügt
     */
    public void addMouseListener(MouseListener ml) {
        super.addMouseListener(ml);
        sliderParameter.addMouseListener(ml);
    } //addMouseListener

    /** 
     * Neueingabe im Wert-Parameter-Feld 
     */
    private void wertParameterAction() {        
        double d;
		try {
			d = parameter.evalScalar();
			if (d < pMin) {
	            pMin = d;
	            paramMinStr = Formatierer.value2FloatString(pMin);
	        }
	        if (d > pMax) {
	            pMax = d;
	            paramMaxStr = Formatierer.value2FloatString(pMax);
	        }
	        setSliderParameterTable(); //Positionierung der Null
	        	  
	        ignoreSliderChange=true;
	        sliderParameter
	                .setValue(value2Position(d, sliderParameter, pMin, pMax));			
		} catch (MathException e) {
			ExceptionManager.doError("Fehler beim Evaluieren des Parameters "+parameter.getName(),e);
		}
    } //wertParameterAction
    
    public String getMinString(){
        return Formatierer.value2FloatString(pMin);
    }//getMinString
    
    public String getMaxString(){
        return Formatierer.value2FloatString(pMax);
    }//getMaxString    
    
    public void setMinValue(String value){
        paramMinStr = value;
        paramMinAction();
    }//setMinValue
    
    public void setMaxValue(String value){
        paramMaxStr = value;
        paramMaxAction();
    }//setMinValue    

    /** 
     * Setzt gegebenen Falls die Null beim Slider 
     */
    private void setSliderParameterTable() {
        int maxi = sliderParameter.getSize().width;
        if (maxi == 0)
            maxi = sliderParameter.getPreferredSize().width;
        sliderParameter.setMaximum(maxi);
        int i = value2Position(0, sliderParameter, pMin, pMax);
        
        //Distanz vom Rand aus, ab der die Null angezeigt wird
        int dist = 13;
        if ((i > dist) && (i+dist < maxi)) {
            labelTable = new Hashtable();
            labelTable.put(new Integer(i), labelZero);
            sliderParameter.setLabelTable(labelTable);
        } //if
        else
            sliderParameter.setLabelTable(null);
    } //setSliderParamterTabel

    /** 
     * Wenn der Slider sich bewegt, wird diese Methode aufgerufen 
     */
    private void manageSliderParameter() {    	
        try {
        	double value = position2Value(sliderParameter, pMin, pMax);
            //neuen Wert setzten
            if (!getParameter().isNotFixed())
                return;
            if(getParameter().evalScalar()==value)
            	return;
            getParameter().setValue(value);
            broker.propagateChange(getParameter());
        } catch (BrokerException e) {
            ExceptionManager.doError("Fehler beim Erneuern des Parameters "+getParameter().getName(),e);
        } catch (MathException e) {
        	ExceptionManager.doError("Fehler beim Setzen des Parameters "+getParameter().getName(),e);
        } catch (Throwable t) {
            ExceptionManager.doError("Unvorhergesehener Fehler",t);
        }
    } //manageSliderParameter

    /** 
     * Setzt den Slider neu und ändert unter Umständen die Grenzen 
     */
    private void setParameterSlider(double value) {
        sliderParameter.setValue(value2Position(value,sliderParameter,pMin,pMax));
    } //setParameterSlider

    /**
     * Setzt das neue Minimum und ändert eventl. den aktuellen Wert.
     */
    public void paramMinAction() {
        Number n;
        try {
            n = Formatierer.parse2Number(paramMinStr.trim());
        } catch (NumberFormatException e) {
            ExceptionManager.doError("Nummer wird erwartet. Zum Beispiel 1.5");
            return;
        }//catch

        double d = n.doubleValue();
        if (d >= pMax) {
            ExceptionManager
                    .doError("Das Minimum darf nicht größer als das Maximum sein");
            paramMinStr = Formatierer.value2FloatString(pMin);
            return;
        }//if
        pMin = d;
        
        setSliderParameterTable();
        
        try {
            double val = parameter.evalScalar();
            
            if(val<pMin && parameter.isNotFixed()){
                parameter.setValue(pMin);
                broker.propagateChange(parameter);
            }else{        
                ignoreSliderChange = true;
                setParameterSlider(val);
            }//else
        } catch (MathException e) {
            ExceptionManager.doError("Fehler beim Evaluieren des Parameters "+parameter.getName(),e);
        } catch (BrokerException e) {
        	ExceptionManager.doError("Fehler beim Erneuern des Parameters "+parameter.getName(),e);
        }//catch
    } //paraMinAction

    /**
     * Setzt das neue Maximum und ändert eventl. den aktuellen Wert
     */
    public void paramMaxAction() {
        Number n;
        try {
            n = Formatierer.parse2Number(paramMaxStr.trim());
        } catch (NumberFormatException e) {
            ExceptionManager.doError("Nummer wird erwartet. Zum Beispiel 1.5");
            return;
        }//catch

        double d = n.doubleValue();
        if (d <= pMin) {
            ExceptionManager
                    .doError("Das Maximum darf nicht kleiner als das Minimum sein. "
                            + d);
            paramMinStr = Formatierer.value2FloatString(pMin);
            return;
        } //if
        pMax = d;
        
        setSliderParameterTable();
        
        try {
            double val = parameter.evalScalar();
            
            if(val>pMax && parameter.isNotFixed()){
                parameter.setValue(pMax);
                broker.propagateChange(parameter);
            }else{        
                ignoreSliderChange = true;
                setParameterSlider(val);
            }
        } catch (MathException e) {
        	ExceptionManager.doError("Fehler beim Setzen des Parameters "+parameter.getName(),e);
        } catch (BrokerException e) {
        	ExceptionManager.doError("Fehler beim Evaluieren des Parameters "+parameter.getName(),e);
        }//catch
    } //paraMaxAction

    /** 
     * Umrechnung von beliebigen Wert zu einer Slider-Position 
     */
    private int value2Position(double d, JSlider slider, double min, double max) {
        return (int) Math.round((slider.getMaximum() - slider.getMinimum())
                / (max - min) * (d - min) + slider.getMinimum());
    } //value2Position

    /** 
     * Umrechnung von einer Slider-Position zu einen Wert 
     */
    private double position2Value(JSlider slider, double min, double max) {
        int v = slider.getValue();
        return (max - min) / (slider.getMaximum() - slider.getMinimum())
                * (v - slider.getMinimum()) + min;
    } //position2Value

    /**
     * Erneuern der gesamten Ansicht
     */
    public void refresh() {
        wertParameter.refresh();
        evalParameter.refresh();
        wertParameterAction();
    }//refresh

    /**
     * Ein neues MathModel ist nun zuständig
     */
    public void setMathModel(MathObject mo) {
        parameter = (UserFunction) mo;
        wertParameter.setMathModel(mo);
        evalParameter.setMathModel(mo);
        sliderParameter.setEnabled(parameter.isNotFixed());
    }//setMathModel
    
    public JSlider getParameterSlider(){
        return sliderParameter;
    }//getParameterSlider
    
    public DefinitionField getWertParameter(){
        return wertParameter;
    }//getWertParameter
    
    public DefinitionField getEvalParameter(){
        return evalParameter;
    }//getEvalParameter    
    
	public void deactivate() {
		sliderParameter.setEnabled(false);
		wertParameter.deactivate();
		evalParameter.deactivate();
	}//deactivate
    
    /**
     * Verarbeitet die Veränderungen am Slider, falls das
     * Flag <code>ignoreSliderChange</code> nicht gesetzt
     * wurde.
     * 
     * Falls also das Flag gesetzt wird und der Slider mit
     * <code>setValue</code> positioniert, so hat dies keine
     * Auswirkungen auf die Variable.
     */
    class SliderListener implements ChangeListener{
        public void stateChanged(ChangeEvent e) {
            try {
                if (ignoreSliderChange) {
                    ignoreSliderChange = false;
                    return;
                }//if
                
                manageSliderParameter();
            } catch (Throwable t) {
                ExceptionManager.doError(t);
            }//catch
        } //stateChanged
    }//class SliederListener

} //class ParameterSlider
