/* Generated By:JJTree: Do not edit this line. ASTBrackets.java */

package math4u2.parser.formula.parserNodes;

import math4u2.view.formula.*;
import math4u2.parser.formula.*;

/**
 * Klasse f�r runde Klammern.
 * 
 * @author Christoph Beckmann
 *  
 */

public class ASTBrackets extends SimpleNode {
    public ASTBrackets(int id) {
        super(id);
    }

    public ASTBrackets(fsParser p, int id) {
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
        byte type=BracketedBox.TYPE_PARENTHESE;
        if(symbol.equals("|")){
            type=BracketedBox.TYPE_ABS;
        }else if(symbol.equals("[")){
            type=BracketedBox.TYPE_BRACKET;
        }else if(symbol.equals("{")){
            type=BracketedBox.TYPE_BRACE;
        }
    	BracketedBox bb = new BracketedBox(ab.getRenderContext(),type);
        jjtGetChild(0).bakeComponents(bb);
        ab.add(bb);

    }

    /* (non-Javadoc)
     * @see math4u2.parser.formula.parserNodes.SimpleNode#toString()
     */
    public String toString() {
        if(symbol.equals("|")){
            return symbol+jjtGetChild(0).toString()+symbol;
        }else if(symbol.equals("[")){
            return "(["+jjtGetChild(0).toString()+"]";
        }else if(symbol.equals("{")){
            return "{"+jjtGetChild(0).toString()+"}";
        }else{
            return "("+jjtGetChild(0).toString()+")";
        }
        
    }

}