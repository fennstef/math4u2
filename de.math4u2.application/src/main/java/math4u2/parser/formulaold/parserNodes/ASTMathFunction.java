/* Generated By:JJTree: Do not edit this line. ASTMathFunction.java */

package math4u2.parser.formulaold.parserNodes;

import math4u2.view.formula.*;
import math4u2.view.layout.IndexLayout;
import math4u2.parser.formulaold.*;
import math4u2.view.formula.AtomicBox;
/**
 * Klasse f�r die Funktionen sin, cos, tan, log
 * 
 * @author Christoph Beckmann
 *  
 */

public class ASTMathFunction extends SimpleNode {
    public ASTMathFunction(int id) {
        super(id);
    }

    public ASTMathFunction(fsParser p, int id) {
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

        if (symbol.equals("log")) {
            IndexedBox ib = new IndexedBox(ab.getRenderContext());
            ib.add(new OpBox(ab.getRenderContext(), symbol), IndexLayout.CENTER);
            ContainerBox cb = new ContainerBox(ab.getRenderContext());
            jjtGetChild(0).bakeComponents(cb);
            ib.add(cb, IndexLayout.RIGHT_BOTTOM);
            ab.add(ib);
            jjtGetChild(1).bakeComponents(ab);
        } else {
            ab.add(new OpBox(ab.getRenderContext(), symbol));
            BracketedBox bb = new BracketedBox(ab.getRenderContext());
            jjtGetChild(0).bakeComponents(bb);
            ab.add(bb);
        }
    }
}