package math4u2.parser.formula.parserNodes;

import java.util.Map;

import math4u2.controller.Broker;
import math4u2.parser.formula.Node;
import math4u2.parser.formula.fsParser;
import math4u2.view.formula.AtomicBox;
import math4u2.view.formula.ContainerBox;
import math4u2.view.formula.FormulaRenderContext;
import math4u2.view.formula.IndexedBox;
import math4u2.view.formula.OrdBox;
import math4u2.view.layout.IndexLayout;

/**
 * Klasse für Ausdrücke zusammengesetzt aus einer Variable und IndexStep bzw.
 * EvalStep.
 * 
 * @author Christoph Beckmann
 * 
 */

public class ASTMethodPath extends SimpleNode {

    private Map modifierMap;

    private Broker broker;

    private boolean isFunction = false;

    public ASTMethodPath(int i) {
        super(i);
        // TODO Auto-generated constructor stub
    }

    public ASTMethodPath(fsParser p, int i) {
        super(p, i);
        // TODO Auto-generated constructor stub
    }

    public void setModifierMap(Map modifierMap) {
        this.modifierMap = modifierMap;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public void bakeComponents(AtomicBox ab) {
        String s = this.toString();
        if (modifierMap.containsKey(s)) {
            ASTIdent interaction = new ASTIdent(0);// Zahl ist egal
            interaction.setName(s);
            interaction.setBroker(broker);
            interaction.setModifier((Map) modifierMap.get(s));
            interaction.bakeComponents(ab);
            return;
        }
        FormulaRenderContext frc = ab.getRenderContext();
        ContainerBox methodPath = new ContainerBox(frc);
        jjtGetChild(0).bakeComponents(methodPath);
        for (int i = 1; i < jjtGetNumChildren(); i++) {
            if (jjtGetChild(i) instanceof ASTEvalStep) {
                // EvalStep
                jjtGetChild(i).bakeComponents(methodPath);
            } else {
                // IndexStep
                IndexedBox ib = new IndexedBox(frc);
                ib.add(methodPath, IndexLayout.CENTER);
                ContainerBox indeces = new ContainerBox(frc);
                // Mehrere Indeces müssen per Komma separiert werden
                while (i < jjtGetNumChildren()
                        && jjtGetChild(i) instanceof ASTIndeces) {
                    jjtGetChild(i).bakeComponents(indeces);
                    if (i + 1 < jjtGetNumChildren()
                            && jjtGetChild(i + 1) instanceof ASTIndeces) {
                        indeces.add(new OrdBox(ab.getRenderContext(), ","));
                    }
                    i++;

                }
                ib.add(indeces, IndexLayout.RIGHT_BOTTOM);
                methodPath = new ContainerBox(frc);
                methodPath.add(ib);
                i--;
            }
        }
        ab.add(methodPath);

    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < children.length; i++) {
            sb.append(children[i].toString());
        }
        return sb.toString();
    }

    /**
     * Gibt an ob der Term eine Funktion wie f(x) ist.
     * 
     * @return
     */
    public boolean isFunction() {
        return isFunction;
    }

    public void setFunction(boolean isFunction) {
        this.isFunction = isFunction;
    }

    /**
     * Löschen des letzen Kindes
     * 
     */
    public void removeLastChild() {
        Node[] newChildren= new Node[jjtGetNumChildren()-1];
        System.arraycopy(children,0,newChildren,0,newChildren.length);
        children=newChildren;
    }

}
