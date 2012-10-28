// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises.ui;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import math4u2.controller.Broker;
import math4u2.exercises.EParser;
import math4u2.exercises.scripting.EActionContainer;
import math4u2.exercises.scripting.Scriptable;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.parser.ParseException;
import math4u2.view.graph.drawarea.DrawAreasManager;
import math4u2.view.gui.Math4U2Win;
import math4u2.view.layout.PercentLayout;

import org.w3c.dom.Node;

/**
 * Abstrakte Basisklasse für alle darstellbaren Dokumente. Dies können sowohl
 * Themen als auch Übungen, etc. sein. Sie alle besitzen einen Titel, einen
 * beschreibenden Haupttext und eine Zusammenfassung.
 * 
 * @author Erich Seifert
 * @version 0.1
 */
public abstract class EDocument implements Viewable, Scriptable, Layoutable {
    /** Titel */
    protected StyledText title;

    /** Beschreibung */
    protected StyledText description;

    protected Node descriptionNode;

    /** Zusammenfassung */
    protected StyledText summary;

    protected EParser parser;

    protected EActionContainer actions;

    /**
     * Text, der in der Kopfzeile dargestellt wird (Titel und Beschreibung).
     */
    protected Math4u2TextPane head = new Math4u2TextPane(); 

    /**
     * Text, der in der Fußzeile dargestellt wird (Zusammenfassung).
     */
    protected Math4u2TextPane foot = new Math4u2TextPane();

    /** Layout für die DrawAreas */
    protected PercentLayout layout;

    /** Namen aller DrawAreas */
    protected String[] drawAreaNames;

    /** Referenz auf den Broker */
    protected Broker broker;

    /**
     * Cache der Swing-Darstellung.
     */
    protected JComponent view;

    /**
     * Konstruktor, der ein neues EDocument-Objekt erzeugt.
     * 
     * @param title
     *            Ein Titel, der als Überschrift dargestellt wird
     * @param descriptionNode
     *            Knoten einer Beschreibung des Themas
     * @param summary
     *            Eine Zusammenfassung
     * @param parser
     *            Parser der den Knoten parst
     */
    public EDocument(StyledText title, Node descriptionNode,
            StyledText summary, PercentLayout layout, String[] drawAreaNames,
            Broker broker, EParser parser) {
        this.title = title;
        this.descriptionNode = descriptionNode;
        this.summary = summary;
        this.layout = layout;
        this.drawAreaNames = drawAreaNames;
        this.broker = broker;
        this.parser = parser;

        head = new Math4u2TextPane();
        foot = new Math4u2TextPane();

        buildHeader(head, true); // Haupttext in Math4u2TextPane einfügen
        buildFooter(foot, true); // Fußtext in Math4u2TextPane einfügen

        head.setEditable(false); // Kopftext ist nicht vom Benutzer bearbeitbar
        head.setOpaque(false); // Kopftext ist durchsichtig (nicht opak)
        foot.setEditable(false); // Fußext ist nicht vom Benutzer bearbeitbar
        foot.setOpaque(false); // Fußtext ist durchsichtig (nicht opak)
    }

    /**
     * Gibt den Titel des Übungsthemas zurück.
     * 
     * @return Den aktuellen Titeltext
     */
    public StyledText getTitle() {
        return title;
    }

    /**
     * Setzt den Titel des Dokuments.
     * 
     * @param title
     *            Neuer Titeltext der verwendet werden soll
     */
    public void setTitle(StyledText title) {
        this.title = title;
        buildHeader(head, true);
    }

    /**
     * Gibt die Beschreibung des Dokuments zurück.
     * 
     * @return Den aktuellen Beschreibungstext
     */
    public StyledText getDescription() {
        return description;
    }

    /**
     * Setzt die Beschreibung des Dokuments.
     * 
     * @param description
     *            Neue Beschreibung die verwendet werden soll
     */
    public void setDescription(StyledText description) {
        this.description = description;
        buildHeader(head, true);
    }

    /**
     * Gibt die Zusammenfassung des Dokuments zurück.
     * 
     * @return Den aktuellen Zusammenfassungstext
     */
    public StyledText getSummary() {
        return summary;
    }

    /**
     * Setzt die Zusammenfassung des Dokuments.
     * 
     * @param summary
     *            Neuer Zusammenfassungtext der verwendet werden soll
     */
    public void setSummary(StyledText summary) {
        this.summary = summary;
        buildFooter(foot, true);
    }

    /**
     * Erzeugt die Darstellung des Haupttextes neu.
     */
    protected void buildHeader(Math4u2TextPane textPane, boolean withDelete) {
        if ((title != null) || (description != null)) {

            textPane.setVisible(true);
            // Titel und Beschreibung in den Kopftext einfügen
            Document doc = textPane.getDocument();

            try {
                if (withDelete)
                    doc.remove(0, doc.getLength()); // Alten Text löschen
                if (title != null) {
                    title.insertText(doc, "heading");
                    doc.insertString(doc.getLength(), "\n\n",
                            ESkin.styleContext.getStyle("regular"));
                }//if title!=null
                if (description != null) {
                    description.insertText(doc);
                } //if description!=null
                textPane.setCaretPosition(0);
            } catch (BadLocationException ble) {
                ExceptionManager.doError("Couldn't insert initial text.",ble);
                // Exception!
            }
        } else {
            textPane.setVisible(false);
        }
    } //buildHeader

    /**
     * Erzeugt die Darstellung des Fußtextes neu.
     */
    protected void buildFooter(Math4u2TextPane textPane, boolean withDelete) {
        if (summary != null) {
            textPane.setVisible(true);
            // Die Zusammenfassung in den Fußtext einfügen
            Document doc = textPane.getDocument();
            try {
                if (withDelete)
                    doc.remove(0, doc.getLength()); // Alten Text löschen
                summary.insertText(doc);
                textPane.setCaretPosition(0);
            } catch (BadLocationException ble) {
                ExceptionManager.doError("Couldn't insert initial text.",ble);
                // Exception!
            }
        } else {
            textPane.setVisible(false);
        }
    }//buildFooter

    /**
     * Gibt eine Swing-Komponente zur Darstellung zurück.
     * 
     * @return Eine neue Swing-Komponente zur späteren Darstellung
     */
    public JComponent getView() {
        if (descriptionNode != null) {
            StyledText tmp = null;
            try {
                tmp = parser.parseText(descriptionNode);
            } catch (ParseException e) {
                ExceptionManager.doError("Fehler beim Parsen der aktuellen Sicht",e, descriptionNode);
            }//catch
            setDescription(tmp);
        }//if
        createView();
        return view;
    } //getView

    /** Layoutet alle Zeichenflächen */
    public void buildLayout() {
        //Falls gerade in der Hilfe navigiert wird
        if (DrawAreasManager.isLocked())
            return;

        if (layout == null)
            return;
        JPanel container = Math4U2Win.getInstance().getDrawAreaContainer();
        container.setLayout(layout);
        container.removeAll();
        for (int i = 0; i < drawAreaNames.length; i++) {
            JPanel da = (JPanel) DrawAreasManager.get(broker, drawAreaNames[i]);
            da.setVisible(true);
            container.add(da);
            da.doLayout();
        } //for
        container.revalidate();
    } //buildLayout

    /**
     * Führt eventuelle Animationen aus..
     */
    public void animate() {
        if (getActions() != null) {
            getActions().startActions();
        }
    }

    /**
     * Legt die Aktionen fest, die beim Aufruf ausgeführt werden sollen.
     * 
     * @param actions
     *            Der <code>EActionContainer</code> mit den Aktionen.
     */
    public void setActions(EActionContainer actions) {
        this.actions = actions;
    }

    /**
     * Gibt die Aktionen zurück, die beim Aufruf ausgeführt werden sollen.
     * 
     * @return Der <code>EActionContainer</code> mit den Aktionen.
     */
    public EActionContainer getActions() {
        return actions;
    }

    /**
     * Erzeugt die Darstellung und speichert diese intern.
     */
    abstract protected void createView();
} //class EDocument
