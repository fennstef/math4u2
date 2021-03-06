package math4u2.view.gui.listview.complete;

import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.relation.ObjectNotInRelationException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.view.dragAndDrop.DNDHandler;
import math4u2.view.dragAndDrop.target.DropTargetOnCompleteView;
import math4u2.view.graph.HasGraph;
import math4u2.view.gui.component.ColorButton;
import math4u2.view.gui.component.DefinitionField;
import math4u2.view.gui.component.LineStyleCombo;
import math4u2.view.gui.component.VisibleButton;
import math4u2.view.gui.listview.AbstractListViewItem;
import math4u2.view.gui.listview.ListViewInterface;
import math4u2.view.layout.TableLayout;

/**
 * Darsteller eines Punktes in dem CompleteViewContainer
 * 
 * @author Fenn Stefan
 * @see CompleteViewContainer
 */
public class CompleteArrowViewItem extends AbstractListViewItem {
    private VisibleButton visibleBox;

    private ColorButton colorButton;

    private LineStyleCombo lineStyleCombo;

    private JLabel fnLabel;

    private DefinitionField begin;

    private DefinitionField richtung;

    public CompleteArrowViewItem(UserFunction model, ListViewInterface parent,
            Broker broker) {
        super(model, parent, broker);
    } //Konstruktor

    protected void onceInit() {
        //Visible Box
        visibleBox = new VisibleButton((HasGraph) model, broker);
        addListener(visibleBox);
        //color
        colorButton = new ColorButton((HasGraph) model, broker);
        addListener(colorButton);
        //Linestyle
        lineStyleCombo = new LineStyleCombo((HasGraph) model, broker);
        //function Label
        fnLabel = new JLabel();
        addListener(fnLabel);

        try {
            String head = model.getDefinitionHeader();
            begin = new DefinitionField(head + Broker.SEPERATOR + "start",
                    getMathObject().getRelationContainer().getObjectByName(
                            "start"), broker);
            richtung = new DefinitionField(
                    head + Broker.SEPERATOR + "richtung",
                    getMathObject().getRelationContainer().getObjectByName(
                            "richtung"), broker);
        } catch (ObjectNotInRelationException e) {
        	ExceptionManager.doError("Fehler beim Initialisieren der Pfeil-Sicht",e);
        }

        //Layout
        double border = 5, P = TableLayout.PREFERRED, F = TableLayout.FILL;
        double size[][] = {
        /* Zeilen */// 
                { border, P, border, P, border, P, F, border },
                /* Spalten */// 
                { border, P, border, P, border, P, border, P, border } };

        setLayout(new TableLayout(size));

        JPanel panel = new JPanel(new FlowLayout((int) border, (int) border,
                FlowLayout.LEFT));
        panel.setOpaque(false);
        panel.add(visibleBox);
        panel.add(colorButton);
        panel.add(lineStyleCombo);

        JPanel panel2 = new JPanel(new FlowLayout((int) border + 2,
                (int) border, FlowLayout.LEFT));
        panel2.setOpaque(false);
        panel2.add(fnLabel);

        add(panel, "0, 1, 7, 1");
        add(panel2, "0, 3, 7, 3");
        add(begin.getLabel(), "1, 5, L, C");
        add(new JLabel(":="), "3, 5, L, C");
        add(begin.getInput(), "5, 5, 6, 5");
        add(richtung.getLabel(), "1, 7, L, C");
        add(new JLabel(":="), "3, 7, L, C");
        add(richtung.getInput(), "5, 7, 6, 7");

        super.onceInit();
    } //onceInit

    public void reInit() {
        fnLabel.setText(model.toString());
        colorButton.setMathModel(model);
        colorButton.refresh();
        visibleBox.setMathModel(model);
        visibleBox.refresh();
        lineStyleCombo.setMathModel(model);
        lineStyleCombo.refresh();
        try {
            begin.setMathModel(model.getRelationContainer().getObjectByName(
                    "start"));
            richtung.setMathModel(model.getRelationContainer().getObjectByName(
                    "richtung"));
        } catch (ObjectNotInRelationException e) {
        	ExceptionManager.doError("Fehler beim Erneuern der Pfeil-Sicht",e);
        }//catch
        begin.refresh();
        richtung.refresh();
    } //init

    private MathObject getMathObject() {
        try {
            return broker.getObject((String) model.getKey());
        } catch (BrokerException e) {
        	ExceptionManager.doError("Fehler beim Initialisieren der Pfeil-Sicht",e);
            throw new RuntimeException(e);
        } //catch (BrokerException e)
    } //MathObject getMathObject()

    protected void addListener(JComponent c) {
        super.addListener(c);
        new DNDHandler(c, "CompleteView.$" + model.getKey(), broker);
        new DropTargetOnCompleteView(this, (CompleteViewBox) parent);
    } //addListener

} //class ListBoxItem
