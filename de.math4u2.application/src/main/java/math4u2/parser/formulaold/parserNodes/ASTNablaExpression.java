package math4u2.parser.formulaold.parserNodes;

import math4u2.parser.formulaold.*;
import math4u2.view.formula.AtomicBox;
/* Generated By:JJTree: Do not edit this line. ASTNablaExpression.java */

public class ASTNablaExpression extends SimpleNode {
    public ASTNablaExpression(int id) {
        super(id);
    }

    public ASTNablaExpression(fsParser p, int id) {
        super(p, id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math4u2.view.parser.SimpleNode#bakeComponents(math4u2.view.parser.Node)
     */
    public void bakeComponents(AtomicBox ab) {
        for (int i = 0; i < children.length; i++) {
            jjtGetChild(i).bakeComponents(ab);
        }
    }

}