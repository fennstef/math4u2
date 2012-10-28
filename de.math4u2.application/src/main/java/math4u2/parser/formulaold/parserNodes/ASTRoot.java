/*
 * Created on 21.09.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package math4u2.parser.formulaold.parserNodes;

import math4u2.parser.formulaold.Node;
import math4u2.parser.formulaold.fsParser;
import math4u2.view.formula.*;
import math4u2.view.formula.AtomicBox;
import math4u2.view.formula.FormulaRenderContext;
/**
 * @author Christoph Beckmann
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ASTRoot extends SimpleNode {
    
    public ASTRoot(int id) {
        super(id);
    }

    public ASTRoot(fsParser p, int id) {
        super(p, id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math4u2.view.parser.SimpleNode#bakeComponents(math4u2.view.parser.Node)
     */
    public void bakeComponents(AtomicBox ab) {
        FormulaRenderContext frc = ab.getRenderContext();

		ContainerBox cb = new ContainerBox(frc);
        jjtGetChild(0).bakeComponents(cb);

        RootBox rb = new RootBox(frc);

        ContainerBox radicand = new ContainerBox(frc);
        jjtGetChild(0).bakeComponents(radicand);
        rb.add(radicand, RootBox.RADICAND);
        
		// Index einfügen falls sich nicht ohnihin um eine
		// Quadratwurzel handelt
		Node indexNode = jjtGetChild(1);
		if (!(indexNode instanceof ASTNumber) || !((ASTNumber)indexNode).getSymbol().equals("2")) {
	        ContainerBox index = new ContainerBox(frc);
	        jjtGetChild(1).bakeComponents(index);
	        rb.add(index, RootBox.INDEX);
		}

        ab.add(rb);
    }

}
