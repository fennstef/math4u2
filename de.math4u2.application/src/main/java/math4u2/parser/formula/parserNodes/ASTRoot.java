/*
 * Created on 21.09.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package math4u2.parser.formula.parserNodes;

import math4u2.parser.formula.Node;
import math4u2.parser.formula.fsParser;
import math4u2.view.formula.*;

/**
 * @author Christoph Beckmann
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ASTRoot extends SimpleNode {

    public ASTRoot(int id) {
        super(id);
    }

    public ASTRoot(fsParser p, int id) {
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
        FormulaRenderContext frc = ab.getRenderContext();
        RootBox rb = new RootBox(frc);

        ContainerBox radicand = new ContainerBox(frc);
        jjtGetChild(0).bakeComponents(radicand);
        rb.add(radicand, RootBox.RADICAND);

        // Index einfügen falls sich nicht ohnehin um eine
        // Quadratwurzel handelt
        if (symbol.equals("root")) {
            ContainerBox index = new ContainerBox(frc);
            jjtGetChild(1).bakeComponents(index);
            rb.add(index, RootBox.INDEX);
        }

        ab.add(rb);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math4u2.parser.formula.parserNodes.SimpleNode#toString()
     */
    public String toString() {
        String s = symbol + "(" + jjtGetChild(0).toString();
        if (jjtGetNumChildren() > 1) {
            s += "," + jjtGetChild(1).toString();
        }
        s+=")";
        return s;
    }

}