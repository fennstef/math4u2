package math4u2.parser.formula.parserNodes;

import math4u2.view.formula.*;
import math4u2.view.layout.IndexLayout;
import math4u2.parser.formula.*;

/* Generated By:JJTree: Do not edit this line. ASTSpecialFunction.java */

/**
 * Klasse f�r sum, prod und int
 * 
 * @author Christoph Beckmann
 */

public class ASTSpecialFunction extends SimpleNode {
    private int numBodyChild=3;
    
    public ASTSpecialFunction(int id) {
        super(id);
    }

    public ASTSpecialFunction(fsParser p, int id) {
        super(p, id);
    }

    public void setName(String symbol) {
        super.symbol = symbol;
    }

    /**
     * Baut nur die Components die f�r den Head der Funktion ben�tigt werden
     * 
     * @param ab
     */
    public void bakeHead(AtomicBox ab) {
    	FormulaRenderContext frc = ab.getRenderContext();
        BigOpBox op = new BigOpBox(frc, symbol);
        if (symbol.equals("sum") || symbol.equals("prod")) {
            IndexedBox ib = new IndexedBox(frc);
            ib.add(op, IndexLayout.CENTER);
            ContainerBox cbDown = new ContainerBox(frc);
            ContainerBox cbUp = new ContainerBox(frc);
            jjtGetChild(0).bakeComponents(cbDown);
            cbDown.add(new RelBox(frc, "="));
            jjtGetChild(1).bakeComponents(cbDown);
            jjtGetChild(2).bakeComponents(cbUp);
            ib.add(cbUp, IndexLayout.TOP);
            ib.add(cbDown, IndexLayout.BOTTOM);
            ab.add(ib);
        } else if (symbol.equals("int")) {
            if(jjtGetNumChildren()==2){
                numBodyChild=1;
                ab.add(op);
            }else{
                IndexedBox ib = new IndexedBox(frc);
                ib.add(op, IndexLayout.CENTER);
                ContainerBox cbDown = new ContainerBox(frc);
                ContainerBox cbUp = new ContainerBox(frc);
                jjtGetChild(1).bakeComponents(cbDown);
                jjtGetChild(2).bakeComponents(cbUp);
                ib.add(cbUp, IndexLayout.TOP);
                ib.add(cbDown, IndexLayout.BOTTOM);
                ab.add(ib);
            }
        }
    }

    /**
     * Baut nur die Components die f�r den Body der Funktion ben�tigt werden
     * 
     * @param ab
     */
    public void bakeBody(AtomicBox ab) {
        jjtGetChild(numBodyChild).bakeComponents(ab);
        if (symbol.equals("int")) {
            //Schlussterm
            ab.add(new OrdBox(ab.getRenderContext(), "d"));
            jjtGetChild(0).bakeComponents(ab);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see math4u2.view.parser.SimpleNode#bakeComponents(math4u2.view.parser.Node)
     */
    public void bakeComponents(AtomicBox ab) {
        bakeHead(ab);
        bakeBody(ab);
    }

    /* (non-Javadoc)
     * @see math4u2.parser.formula.parserNodes.SimpleNode#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(symbol+"(");
        for (int i = 0; i < children.length; i++) {
            sb.append(jjtGetChild(i).toString());
            if(i<children.length-1)
                sb.append(",");
        }
        sb.append(")");
        return sb.toString();
    }

}