package math4u2.parser.formulaold.parserNodes;

import math4u2.view.formula.OrdBox;
import math4u2.parser.formulaold.*;
import math4u2.view.formula.AtomicBox;
/* Generated By:JJTree: Do not edit this line. ASTPraefix.java */

public class ASTPraefix extends SimpleNode {
    public ASTPraefix(int id) {
        super(id);
    }

    public ASTPraefix(fsParser p, int id) {
        super(p, id);
    }

    public void setName(String symbol) {
        super.symbol = symbol;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math4u2.view.parser.SimpleNode#bakeComponents(math4u2.view.parser.Node)
     */
    public void bakeComponents(AtomicBox ab) {
        ab.add(new OrdBox(ab.getRenderContext(), symbol));

    }
}