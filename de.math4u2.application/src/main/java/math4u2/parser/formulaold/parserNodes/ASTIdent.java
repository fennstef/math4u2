/* Generated By:JJTree: Do not edit this line. ASTIdent.java */

package math4u2.parser.formulaold.parserNodes;

import java.awt.Component;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.relation.RelationFactory;
import math4u2.controller.relation.RelationInterface;
import math4u2.exercises.Step;
import math4u2.mathematics.functions.UserFunction;
import math4u2.parser.formulaold.fsParser;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.Formatierer;
import math4u2.view.formula.AtomicBox;
import math4u2.view.formula.InteractionBox;
import math4u2.view.formula.OrdBox;
import math4u2.view.formula.SliderBox;
import math4u2.view.gui.component.MathObjectWrapper;
import math4u2.view.gui.component.ParameterComponent;
/**
 * Klasse f�r Variablen.
 * 
 * @author Christoph Beckmann
 *  
 */

public class ASTIdent extends SimpleNode {
    private AtomicBox varBox;

    private Map modifiers;

    private Broker broker;

    public ASTIdent(int id) {
        super(id);
    }

    public ASTIdent(fsParser p, int id) {
        super(p, id);
    }

    public void setName(String symbol) {
        super.symbol = symbol;
    }

    public void setModifier(Map modifiers) {
        this.modifiers = modifiers;
    }

    /**
     * Liefert den Component zur�ck der die Variable enth�lt
     * 
     * @return
     */
    public Component getVarComponent() {
        return varBox;
    }

    public void setVarComponent(AtomicBox box) {
        varBox = box;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public Broker getBroker() {
        return broker;
    }

    /*
     * (non-Javadoc)
     * 
     * @see math4u2.view.parser.SimpleNode#bakeComponents(math4u2.view.parser.Node)
     */
    public void bakeComponents(AtomicBox ab) {
        if (modifiers == null) {
            varBox = new OrdBox(ab.getRenderContext(), symbol);
            ab.add(varBox);
            return;
        }
        if (((String) modifiers.get("style")).equals("text")) {
            boolean oneStepActive = true;
            if(modifiers.containsKey("oneStepActive") && modifiers.get("oneStepActive").equals("false")) {
                oneStepActive = false;
            }
            try {
                UserFunction f = (UserFunction) broker.getObject(symbol);
                varBox = new InteractionBox(ab.getRenderContext(), f, broker, oneStepActive);

                String widthString = (String) modifiers.get("width");
                if (widthString != null) {
                    try {
                        int width = Integer.parseInt(widthString);
                        ((InteractionBox) varBox).setColumnsVal(width);
                    } catch (NumberFormatException e1) {
                        ExceptionManager
                                .doError("Textfeldbreite muss eine Zahl sein.");
                        e1.printStackTrace();
                    }
                }
                RelationInterface ri = RelationFactory
                        .getView_Function_Relation();
                List list = new LinkedList();
                list.add(f);
                broker.defineRelations(varBox, list, ri);
            } catch (BrokerException e) {
                ExceptionManager.doError(e);
            }
        } else if (((String) modifiers.get("style")).equals("slider")) {
            try {
                UserFunction f = (UserFunction) broker.getObject(symbol);
                double max = Double.parseDouble((String) modifiers.get("max"));
                double min = Double.parseDouble((String) modifiers.get("min"));
                Formatierer format = new Formatierer();
                format.setDecimalSpecialPattern((String) modifiers.get("pattern")) ;
                ParameterComponent ps = new ParameterComponent(f, broker, format);
                MathObjectWrapper wrapper = new MathObjectWrapper(ps);

            	//Beziehungen erzeugen
        		RelationInterface[] ra = new RelationInterface[1];
        		ra[0] = RelationFactory.getView_Function_Relation();
        		List parts = new LinkedList();
        		parts.add(wrapper);
        		List relations = Arrays.asList(ra);

        		try {
        			//Definieren lassen
        			broker.defineRelations(f, parts, relations, Broker.SECOND_OBJECT);
        		} catch (BrokerException e) {
        			ExceptionManager.doError(e);
        		}		
        		Step.addParameterBoxesToDeleteAfterUse(wrapper);                
                
                varBox = new SliderBox(ab.getRenderContext(), min, max, ps);

            } catch (NumberFormatException e) {
                ExceptionManager.doError("Maximum o. Minimum f�r Slider ist keine g�ltige Zahl",e);
            } catch (BrokerException e) {
                ExceptionManager.doError("Fehler beim holen des Objekts "+symbol,e);
            }
        } else {
            varBox = new OrdBox(ab.getRenderContext(), symbol);
        }
        ab.add(varBox);

    }
}