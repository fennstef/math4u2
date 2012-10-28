package math4u2.parser.formula.parserNodes;

import math4u2.parser.formula.fsParser;
import math4u2.view.formula.AtomicBox;
import math4u2.view.formula.BracketedBox;
import math4u2.view.formula.EmptyBox;
import math4u2.view.formula.FormulaRenderContext;
import math4u2.view.formula.OrdBox;

public class ASTEvalStep extends SimpleNode {

    public ASTEvalStep(int i) {
        super(i);
    }

    public ASTEvalStep(fsParser p, int i) {
        super(p, i);
    }

    public void bakeComponents(AtomicBox ab) {
        FormulaRenderContext frc= ab.getRenderContext();
        BracketedBox bb = new BracketedBox(frc);
        for (int i = 0; i < jjtGetNumChildren()-1; i++) {
            jjtGetChild(i).bakeComponents(bb);
            bb.add(new OrdBox(frc, ","));
            bb.add(new EmptyBox(frc, 3f, 1f));
        }
        jjtGetChild(jjtGetNumChildren()-1).bakeComponents(bb);
        ab.add(bb);
    }

    public String toString() {
        // TODO Auto-generated method stub
        return null;
    }

}
