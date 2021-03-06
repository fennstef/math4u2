/* Generated By:JJTree: Do not edit this line. ASTIdent.java */

package math4u2.parser.formula.parserNodes;

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
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.mathematics.results.DoubleResult;
import math4u2.mathematics.results.MatrixDoubleResult;
import math4u2.mathematics.results.ScalarDoubleResult;
import math4u2.parser.formula.Node;
import math4u2.parser.formula.fsParser;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.parser.script.ParseException;
import math4u2.view.Formatierer;
import math4u2.view.formula.AtomicBox;
import math4u2.view.formula.BinBox;
import math4u2.view.formula.ContainerBox;
import math4u2.view.formula.FormulaRenderContext;
import math4u2.view.formula.GridBox;
import math4u2.view.formula.InteractionBox;
import math4u2.view.formula.MatrixBox;
import math4u2.view.formula.MatrixInteractionBox;
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
        FormulaRenderContext frc = ab.getRenderContext();
        if (modifiers == null || modifiers.get("style") == null) {
            varBox = new OrdBox(frc, symbol);
            ab.add(varBox);
            return;
        }

        String style = ((String) modifiers.get("style")).trim();
        String pattern = (String) modifiers.get("pattern");
        UserFunction f;
        try {
            f = (UserFunction) broker.getObject(symbol);
        } catch (BrokerException e2) {
            e2.printStackTrace();
            varBox = new OrdBox(frc, symbol);
            ab.add(varBox);
            ExceptionManager.doError("Fehler beim Nachschlagen von " + symbol);
            return;
        }

        if (f == null) {
            varBox = new OrdBox(frc, symbol);
            ab.add(varBox);
            ExceptionManager.doError(symbol + " ist nicht definiert");
            return;
        }

        /*
         * --------------------------------------------------------- Erzeugen
         * einer InteractionBox +++++++++++++++++++++++++++
         * ---------------------------------------------------------
         */
        if (style.equals("dtext") || style.equals("text")
                || style.equals("deval") || style.equals("eval")) {

            // vorbereiten der Broker-Beziehugen
            RelationInterface ri = RelationFactory.getView_Function_Relation();
            List list = new LinkedList();
            list.add(f);

            // vorbereiten der Schrittanzahl
            boolean oneStepActive = true;
            if (modifiers.containsKey("oneStepActive")) {

                String bool = ((String) modifiers.get("oneStepActive")).trim();
                oneStepActive = Boolean.valueOf(bool).booleanValue();
            }

            String binSymbol = ":=";
            // vorbereiten der Breite der InteractionBox
            String widthString = (String) modifiers.get("width");
            int width = InteractionBox.STANDARDCOLUMNS;
            if (widthString != null) {
                widthString = widthString.trim();
                try {
                    width = Integer.parseInt(widthString);
                } catch (NumberFormatException e1) {
                    ExceptionManager
                            .doError("Textfeldbreite muss eine Zahl sein.");
                    e1.printStackTrace();
                }
            }
            DoubleResult dr;
            try {
                dr = (DoubleResult) f.eval();
            } catch (MathException e1) {
                ExceptionManager.doError("Funktion " + symbol
                        + " konnte nicht evaluiert werden.");
                e1.printStackTrace();
                varBox = new OrdBox(frc, symbol);
                ab.add(varBox);
                return;
            }
            if (dr instanceof MatrixDoubleResult) {
                MatrixDoubleResult mr = (MatrixDoubleResult) dr;
                double[][] valueArray = mr.valueArray;
                varBox = new MatrixBox(frc);

                GridBox gb = new GridBox(frc, mr.colDim, mr.rowDim);
                varBox.add(gb);
                for (int i = 0; i < valueArray.length; i++) {
                    for (int j = 0; j < valueArray[0].length; j++) {

                        MatrixInteractionBox mib = new MatrixInteractionBox(ab
                                .getRenderContext(), f, broker, i, j,
                                oneStepActive);
                        mib.setEditable(style.indexOf("text") != -1);
                        mib.setColumnsVal(width);
                        if (pattern != null) {
                            mib.setFormat(pattern);
                        }
                        gb.add(mib);
                        try {
                            // Definieren der Beziehung von jedem einzelnen
                            // Matrixelement
                            broker.defineRelations(mib, list, ri);
                        } catch (BrokerException e) {
                            ExceptionManager
                                    .doError("Variable "
                                            + symbol
                                            + "konnte nicht beim Broker registriert werden.");
                            e.printStackTrace();
                        }

                    }
                }
            } else if (dr instanceof ScalarDoubleResult) {

                InteractionBox ib = new InteractionBox(frc, f, broker,
                        oneStepActive);

                // Pr�fung ob Textfeld nur evaluiert werden soll
                ib.setEditable(style.indexOf("eval") == -1);

                // setze Breite des Textfelds
                ib.setColumnsVal(width);
                varBox = ib;

                // Setzen des Patterns
                if (pattern != null) {
                    ib.setFormat(pattern);
                }
                ib.renew(null);
                // Pr�fung ob Wert als Definition angezeigt werden soll
                
                try {
                    // Definieren der Beziehung des elements
                    broker.defineRelations(ib, list, ri);
                } catch (BrokerException e) {
                    ExceptionManager.doError("Variable " + symbol
                            + "konnte nicht beim Broker registriert werden.");
                    e.printStackTrace();
                }
            } else {
                varBox = new OrdBox(frc, symbol);
                ExceptionManager.doError("Variable " + symbol
                        + " ist vom falschen Typ");
            }
            if (style.startsWith("d")) {
                ContainerBox temp = new ContainerBox(frc);
                try {
                    Node node = fsParser.parseFormula(symbol, null, null);
                    node.bakeComponents(temp);
                } catch (math4u2.parser.formula.ParseException e) {
                    temp.add(new OrdBox(frc, symbol));
                    e.printStackTrace();
                    ExceptionManager
                            .doError("Fehler beim Erzeugen der Formel: "
                                    + symbol);
                }

                if (style.equals("deval")) {
                    binSymbol = "=";
                }
                temp.add(new BinBox(frc, binSymbol));
                temp.add(varBox);
                varBox=temp;
            }
        }
        /*
         * --------------------------------------------------------- Erzeugen
         * des Sliders ++++++++++++++++++++++++++++++++++++
         * ---------------------------------------------------------
         */
        else if (style.equals("slider") || style.equals("dslider")) {
            if (modifiers.get("max") == null || modifiers.get("min") == null) {
                ExceptionManager.doError(new ParseException(
                        "kein Minimum oder Maximum f�r Slider des Parameters \""
                                + symbol + "\" definiert"));
                return;
            }
            double max = Double.parseDouble((String) modifiers.get("max"));
            double min = Double.parseDouble((String) modifiers.get("min"));
            Formatierer format = new Formatierer();
            format.setDecimalSpecialPattern((String) modifiers.get("pattern")) ;
            ParameterComponent ps = new ParameterComponent(f, broker, format);
            String widthStr = (String) modifiers.get("width");
            if(widthStr!=null){
            	int width = Integer.parseInt(widthStr);
            	if(width>0)
            		ps.getEvalParameter().getInput().setColumns(width);            	
            }//if widthStr!=null

            MathObjectWrapper wrapper = new MathObjectWrapper(ps);

            // Beziehungen erzeugen
            RelationInterface[] ra = new RelationInterface[1];
            ra[0] = RelationFactory.getView_Function_Relation();
            List parts = new LinkedList();
            parts.add(wrapper);
            List relations = Arrays.asList(ra);

            try {
                // Definieren lassen
                broker.defineRelations(f, parts, relations,
                        Broker.SECOND_OBJECT);
            } catch (BrokerException e) {
                ExceptionManager.doError(e);
            }
            Step.addParameterBoxesToDeleteAfterUse(wrapper);
            if (style.equals("slider")) {
                varBox = new SliderBox(frc, min, max, ps);
            } else if (style.equals("dslider")) {
                varBox = new ContainerBox(frc);
                varBox.add(new OrdBox(frc, symbol));
                varBox.add(new BinBox(frc, ":="));
                varBox.add(new SliderBox(frc, min, max, ps));
            }
        } else {
            varBox = new OrdBox(frc, symbol);
            ExceptionManager.doError("Unbekannter Parameter: " + style
                    + ". Wird ignoriert.");
        }
        ab.add(varBox);
    }

    /*
     * (non-Javadoc)
     * 
     * @see math4u2.parser.formula.parserNodes.SimpleNode#toString()
     */
    public String toString() {
        return symbol;
    }
}