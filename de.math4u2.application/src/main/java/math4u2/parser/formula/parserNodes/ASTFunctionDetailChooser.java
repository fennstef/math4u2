/*
 * Created on 13.09.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package math4u2.parser.formula.parserNodes;

import java.util.Map;

import math4u2.controller.Broker;
import math4u2.controller.MathObject;
import math4u2.parser.formula.Node;
import math4u2.parser.formula.ParseException;
import math4u2.parser.formula.fsParser;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.formula.AtomicBox;

/**
 * @author Christoph Beckmann
 */
public class ASTFunctionDetailChooser extends SimpleNode {

    private Broker broker;

    private Map modifierMap;

    /**
     * @param i
     */
    public ASTFunctionDetailChooser(int i) {
        super(i);
    }

    /**
     * @param p
     * @param i
     */
    public ASTFunctionDetailChooser(fsParser p, int i) {
        super(p, i);
    }

    public void setModifierMap(Map modifierMap) {
        this.modifierMap = modifierMap;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public void setName(String symbol) {
        super.symbol = symbol;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math4u2.parser.formula.Node#bakeComponents(math4u2.view.formula.AtomicBox)
     */
    public void bakeComponents(AtomicBox ab) {
        Node n;
        String functionString = jjtGetChild(0).toString();
        MathObject mo = broker.getMathObject(functionString);
        String valueStr = mo.toString().trim();
        int posAss = valueStr.indexOf(":=");
        if (posAss != -1) {
            try {
                if (symbol.equals("$fd")) {
                    n = fsParser.parseFormula(valueStr, modifierMap, broker);
                    n.bakeComponents(ab);
                } else if (symbol.equals("$fh")) {
                    n = fsParser.parseFormula(valueStr.substring(0, posAss),
                            modifierMap, broker);
                    n.bakeComponents(ab);
                } else if (symbol.equals("$fb")) {
                    n = fsParser.parseFormula(valueStr.substring(posAss + 2),
                            modifierMap, broker);
                    n.bakeComponents(ab);
                }
                
            } catch (ParseException e) {
                ExceptionManager.doError(new ParseException(
                        "Fehler in Formel: " + valueStr + e.getMessage()));
            }
        }
    }

    /* (non-Javadoc)
     * @see math4u2.parser.formula.parserNodes.SimpleNode#toString()
     */
    public String toString() {
        return symbol+"("+jjtGetChild(0).toString()+")";
    }

}