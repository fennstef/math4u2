/*
 * Created on 12.09.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package math4u2.parser.formula.parserNodes;

import math4u2.parser.formula.fsParser;
import math4u2.view.formula.*;
import math4u2.view.layout.IndexLayout;

/**
 * @author Christoph Beckmann
 *  
 */
public class ASTLimes extends SimpleNode {

    /**
     * @param p
     * @param i
     */
    public ASTLimes(fsParser p, int i) {
        super(p, i);
    }

    public ASTLimes(int i) {
        super(i);
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
    	FormulaRenderContext frc = ab.getRenderContext();

    	// Limes mit "lim" vor Term
        AtomicBox limes = new IndexedBox(frc);
        limes.add(new OpBox(frc, "lim"), IndexLayout.CENTER);

        AtomicBox limitDef = new ContainerBox(frc);
        jjtGetChild(0).bakeComponents(limitDef);
        limitDef.add(new BinBox(frc, "$rarr"));

        //      Näherung des Grenzwertes von rechts, links oder ohne
        AtomicBox limit = new ContainerBox(frc);
        jjtGetChild(2).bakeComponents(limit);
        if (!symbol.equals("lim")) {
            IndexedBox plusMinus = new IndexedBox(frc);
            plusMinus.add(limit, IndexLayout.CENTER);
            plusMinus.add(
                    new OrdBox(frc, symbol.substring(3)),
                    IndexLayout.RIGHT_TOP);
            limit = plusMinus;
        }
        limitDef.add(limit);
        limes.add(limitDef, IndexLayout.BOTTOM);
        ab.add(limes);
        ContainerBox term = new ContainerBox(frc);
        jjtGetChild(1).bakeComponents(term);
        ab.add(term);

        //lim unter Pfeil
        /*
         * IndexedBox arrowBox = new IndexedBox(rc);
         * arrowBox.add(new OrdBox(rc,"$rarr"),
         * IndexLayout.CENTER); arrowBox.add(limes,IndexLayout.BOTTOM);
         */
    }

    /*
     * (non-Javadoc)
     * 
     * @see math4u2.parser.formula.parserNodes.SimpleNode#toString()
     */
    public String toString() {
        return symbol + "(vars(" + jjtGetChild(0).toString() + ","
                + jjtGetChild(1).toString() + "," + jjtGetChild(2).toString();
    }

}