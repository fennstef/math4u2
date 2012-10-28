/*
 * Created on 11.11.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package math4u2.view.formula;

import math4u2.controller.Broker;
import math4u2.controller.MathObject;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.Formatierer;

/**
 * @author Toni Zacherl
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MatrixInteractionBox extends InteractionBox {
    
    private int row;
    
    private int col;
    
    /**
     * @param frc
     * @param func
     * @param broker
     */
    public MatrixInteractionBox(FormulaRenderContext frc, UserFunction func,
            Broker broker, int row, int col,boolean toDeleteAfterUse) {
        super(frc, func, broker,toDeleteAfterUse);
        this.row=row;
        this.col=col;
        setEditable(false);
        renew(null);
    }

//    /**
//     * @param frc
//     * @param func
//     * @param broker
//     * @param relFontSize
//     * @param fontName
//     * @param fontStyle
//     * @param stdColumns
//     */
//    public MatrixInteractionBox(FormulaRenderContext frc, UserFunction func,
//            Broker broker, int relFontSize, String fontName, int fontStyle,
//            int stdColumns, int row, int col) {
//        super(frc, func, broker, relFontSize, fontName, fontStyle, stdColumns);
//        this.row=row;
//        this.col=col;
//        setEditable(false);
//    }
    
    public void renew(MathObject mo) {
		try {
			MatrixDoubleResult mdr=(MatrixDoubleResult)(getFunction().eval());
            double d =mdr.valueArray[row][col];
			setValue(d);
			
            String fullValue= Formatierer.fullValue2String(d);
            textField.setToolTipText(f.getName()+"["+row+"]"+"["+col+"] = "+fullValue);
            
		} catch (MathException e) {
			ExceptionManager.doError("Fehler beim Evaluieren der Funktion "+getFunction().getName(),e);
		} //catch
	} //renew
    
    public void setFunction(double d) {
        //TODO: Methode implementieren
//        if (f == null)
//            return;
//        try {
//            f.setValue(d);
//            broker.propagateChange(f);
//        } catch (BrokerException e) {
//            ExceptionManager.doError("Die Funktion " + f.getName()
//                    + " konnte nicht erneuert werden.", e);
//        } catch (MathException e) {
//            ExceptionManager.doError("Fehler beim Setzen der Funktion "
//                    + f.getName() + " auf den Wert " + d, e);
//        }// catch
    }

}
