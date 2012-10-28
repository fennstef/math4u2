/*
 * Created on 20.02.2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package math4u2.parser.formula.parserNodes;

import java.util.Map;

import math4u2.controller.Broker;
import math4u2.parser.formula.fsParser;
import math4u2.view.formula.AtomicBox;
import math4u2.view.formula.DecorationBox;
import math4u2.view.formula.IndexedBox;
import math4u2.view.formula.OrdBox;
import math4u2.view.layout.IndexLayout;

/**
 * @author Toni Zacherl
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ASTSpecialIdent extends SimpleNode {

	private Map modifierMap;
	
	private Broker broker;

	/**
	 * @param i
	 */
	public ASTSpecialIdent(int i) {
		super(i);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param p
	 * @param i
	 */
	public ASTSpecialIdent(fsParser p, int i) {
		super(p, i);
		// TODO Auto-generated constructor stub
	}

	public void setName(String symbol) {
		super.symbol = symbol;
	}

	public void setModifierMap(Map modifierMap) {
		this.modifierMap = modifierMap;
	}
	
	public void setBroker(Broker broker) {
        this.broker = broker;
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see math4u2.parser.formula.Node#bakeComponents(math4u2.view.formula.AtomicBox)
	 */
	public void bakeComponents(AtomicBox ab) {
		String ident = jjtGetChild(0).toString();
		String special = jjtGetChild(1).toString();
		String name = ident + symbol + special;
		
		// wenn der Bezeichner als Wert dargestellt wird, wird hier künstlich
		// ein ASTIdent erzeugt, der dann die entsprechenden Formelelmente erzeugt.
		if (modifierMap.get(name) != null) {
			ASTIdent identNode = new ASTIdent(0);
			identNode.setName(name);
			identNode.setModifier((Map)modifierMap.get(name));
			identNode.setBroker(broker);
			identNode.bakeComponents(ab);
			return;
		}
		AtomicBox specialIdent = null;
		specialIdent = new IndexedBox(ab.getRenderContext());
		specialIdent.add(new OrdBox(ab.getRenderContext(), ident),
				IndexLayout.CENTER);
		if(symbol.equals("~")){
			if (special.equalsIgnoreCase("tilde")
					|| special.equalsIgnoreCase("quer")
					|| special.equalsIgnoreCase("dach")) {
				specialIdent.add(new DecorationBox(ab.getRenderContext(), "$"
						+ special), IndexLayout.TOP);
			} else if (special.equalsIgnoreCase("stern")) {
				specialIdent.add(new OrdBox(ab.getRenderContext(), "$" + special),
						IndexLayout.RIGHT_TOP);
			}else {
				specialIdent.add(new OrdBox(ab.getRenderContext(), special),
						IndexLayout.RIGHT_TOP);
			}
		}else if(symbol.equals("_")||symbol.equals(".")){
			specialIdent.add(new OrdBox(ab.getRenderContext(), special),
					IndexLayout.RIGHT_BOTTOM);
		}
		ab.add(specialIdent);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
        String ident = jjtGetChild(0).toString();
        String special = jjtGetChild(1).toString();
        return ident + symbol + special;
	}

}