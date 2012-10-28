package math4u2.parser.formula.parserNodes;

import math4u2.view.formula.*;
import math4u2.parser.formula.*;

/* Generated By:JJTree: Do not edit this line. ASTNabla.java */

public class ASTSpecialSymbol extends SimpleNode {
    public ASTSpecialSymbol(int id) {
        super(id);
    }

    public ASTSpecialSymbol(fsParser p, int id) {
        super(p, id);
    }
    
    public void setName(String symbol){
        super.symbol=symbol;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math4u2.view.parser.SimpleNode#bakeComponents(math4u2.view.parser.Node)
     */
    public void bakeComponents(AtomicBox ab) {
        ab.add(new OrdBox(ab.getRenderContext(), symbol));

    }

    /* (non-Javadoc)
     * @see math4u2.parser.formula.parserNodes.SimpleNode#toString()
     */
    public String toString() {
        return symbol;
    }

}