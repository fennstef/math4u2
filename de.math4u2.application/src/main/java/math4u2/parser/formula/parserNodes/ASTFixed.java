/*
 * Created on 08.10.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package math4u2.parser.formula.parserNodes;

import math4u2.parser.formula.fsParser;
import math4u2.view.formula.AtomicBox;

/**
 * @author Christoph Beckmann
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ASTFixed extends SimpleNode {

    /**
     * @param p
     * @param i
     */
    public ASTFixed(fsParser p, int i) {
        super(p, i);
    }
    
    public ASTFixed(int i) {
        super(i);
    }

    /* (non-Javadoc)
     * @see math4u2.parser.formula.Node#bakeComponents(math4u2.view.formula.AtomicBox)
     */
    public void bakeComponents(AtomicBox ab) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        // TODO Auto-generated method stub
        return null;
    }

}
