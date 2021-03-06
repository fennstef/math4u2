/* Generated By:JJTree: Do not edit this line. ASTExpo.java */

package math4u2.parser.formula.parserNodes;

import math4u2.view.formula.*;
import math4u2.view.layout.IndexLayout;
import math4u2.parser.formula.*;

/**
 * Klasse f�r exponentielle Ausdr�cke
 * 
 * @author Christoph Beckmann
 *  
 */

public class ASTExpo extends SimpleNode {
    public ASTExpo(int id) {
        super(id);
    }

    public ASTExpo(fsParser p, int id) {
        super(p, id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math4u2.view.parser.SimpleNode#bakeComponents(math4u2.view.parser.Node)
     */
    public void bakeComponents(AtomicBox ab) {
        IndexedBox ib = new IndexedBox(ab.getRenderContext());
        ContainerBox cb0 = new ContainerBox(ab.getRenderContext());
        jjtGetChild(0).bakeComponents(cb0);
        ib.add(cb0, IndexLayout.CENTER);
        ContainerBox cb1 = new ContainerBox(ab.getRenderContext());
        Node n1 = jjtGetChild(1);
        if (n1 instanceof ASTBrackets) {
            if(!((ASTBrackets)n1).getSymbol().equals("|")){
                n1 = n1.jjtGetChild(0);
            }
        }
        n1.bakeComponents(cb1);
        ib.add(cb1, IndexLayout.RIGHT_TOP);
        ab.add(ib);
    }

    /* (non-Javadoc)
     * @see math4u2.parser.formula.parserNodes.SimpleNode#toString()
     */
    public String toString() {
        return "exp("+jjtGetChild(0)+","+jjtGetChild(1)+")";
    }
}