/*
 * Created on 23.09.2004
 *
 */
package math4u2.view.formula;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import math4u2.controller.Broker;
import math4u2.controller.BrokerException;
import math4u2.controller.MathObject;
import math4u2.controller.relation.ObjectNotInRelationException;
import math4u2.controller.relation.RelationContainer;
import math4u2.exercises.Step;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.NotFoundException;
import math4u2.util.swing.NumericTextField;
import math4u2.util.swing.font.DisplayFont;
import math4u2.view.Formatierer;
import math4u2.view.formula.interactive.StepInteractive;

/**
 * Klasse für interaktive/editierbare Elemente innerhalb einer Formel.
 * 
 * @author Christoph Beckmann, Erich Seifert
 * 
 */
public class InteractionBox extends AtomicBox implements StepInteractive {

    public static final int STANDARDCOLUMNS = 5;

    protected UserFunction f;

    protected Broker broker;

    protected NumericTextField textField;

    private DisplayFont font;

    private String fontName;

    private int fontStyle;

    private int stdColumns;

    private static final float EINENG_FAKTOR = 5f;

    private RelationContainer relationContainer;

    private static int count = 0;

    private int num = count++;

    private JTextFieldLimit jtfl;

//    private boolean toDeleteAfterUse = true;

    private DecimalFormat decimalFormat;

    private Color validColor;

    private Color invalidColor = new Color(240, 120, 120);
    
    private boolean formatSet=false;

    /**
     * Konstruktor, der ein neues <code>InteractionBox</code> -Objekt erzeugt
     * und initialisiert.
     * 
     * @param frc
     *            Darstellungsumgebung
     * @param func
     *            Funktion, die durch dieses Interaktionsfeld repräsentiert wird
     * @param broker
     *            Verwaltung für Funktionen
     */
    public InteractionBox(FormulaRenderContext frc, UserFunction func,
            Broker broker, boolean toDeleteAfterUse) {
        this(frc, func, broker, frc.getDefaultFontSize(), frc
                .getDefaultFontName(), Font.PLAIN, STANDARDCOLUMNS,
                toDeleteAfterUse);
    }

    /**
     * Konstruktor, der ein neues <code>InteractionBox</code> -Objekt erzeugt
     * und initialisiert.
     * 
     * @param frc
     *            Darstellungsumgebung
     * @param func
     *            Funktion, die durch dieses Interaktionsfeld repräsentiert wird
     * @param broker
     *            Verwaltung für Funktionen
     * @param relFontSize
     *            relative Größe
     * @param fontName
     *            Name des Zeichensatzes
     * @param fontStyle
     *            Optionen für die Zeichnsatzdarstellung
     * @param stdColumns
     */
    public InteractionBox(FormulaRenderContext frc, UserFunction func,
            Broker broker, int relFontSize, String fontName, int fontStyle,
            int stdColumns, boolean toDeleteAfterUse) {
        super(frc);
        this.broker = broker;
//        this.toDeleteAfterUse=toDeleteAfterUse;
        this.relationContainer = new RelationContainer(this);
        f = func;
        setBorder(BorderFactory.createLineBorder(Color.black, 2));
        setLayout(new FlowLayout());
//        setBackground(Color.white);
        setOpaque(false);
        this.fontName = fontName;
        this.fontStyle = fontStyle;
        validColor = this.getBackground();
        jtfl = new JTextFieldLimit(5);

        textField = new NumericTextField();

        textField.setHorizontalAlignment(JTextField.CENTER);
        /* Schrift wird ermittelt und gesetzt */

        font = new DisplayFont(fontName, fontStyle, Math
                .round(getDisplayHeight()));
        font.initMetrics(getRenderContext().getDefaultFontRenderContext());
        textField.setFont((Font) font);
        textField.setBorder(null);
        textField.setOpaque(false);
        textField.setToolTipText("Zahl für " + f.getName() + " eingeben.");
        setColumnsVal(stdColumns);
        textField.setDocument(jtfl);
        
        add(textField);
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(!textField.isEditable())
            		return;            	
                try {
                    setFunction(updateText());
                    textField.selectAll();
                } catch (NumberFormatException ex) {
                    ExceptionManager
                            .doError(
                                    "Eingabefehler: Zahl konnte nicht ausgewertet werden.",
                                    ex);
                } catch (NotFoundException ex) {
                    ExceptionManager.doError(
                            "Objekt konnte nicht gefunden werden.", ex);
                }// catch
            } // actionPerformed
        });
        textField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
            	if(!textField.isEditable())
            		return;
                try {
                    setFunction(updateText());
                    textField.selectAll();
                } catch (NumberFormatException ex) {
                    ExceptionManager
                            .doError(
                                    "Eingabefehler: Zahl konnte nicht ausgewertet werden.",
                                    ex);
                } catch (NotFoundException ex) {
                    ExceptionManager.doError(
                            "Objekt konnte nicht gefunden werden.", ex);
                }// catch
            } // focusLost
        });
        try {
            List list = new LinkedList();
            list.add(this);
            broker.publishObject(this, (String) getKey());
        } catch (BrokerException e) {
            e.printStackTrace();
        } catch (ObjectNotInRelationException e) {
            e.printStackTrace();
        }
        if (toDeleteAfterUse) {
            Step.addParameterBoxesToDeleteAfterUse(this);
        }
        decimalFormat = new DecimalFormat();
        decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        renew(null);
        doLayout();
    }

    public void setFunction(UserFunction func) {
        f = func;
    }

    /** weist dem Parameter einen neuen Wert zu */
    public void setFunction(double d) {
        if (f == null)
            return;
        try {
            f.setValue(d);
            broker.propagateChange(f);
        } catch (BrokerException e) {
            ExceptionManager.doError("Die Funktion " + f.getName()
                    + " konnte nicht erneuert werden.", e);
        } catch (MathException e) {
            ExceptionManager.doError("Fehler beim Setzen der Funktion "
                    + f.getName() + " auf den Wert " + d, e);
        }// catch
    } // setFunction

    public UserFunction getFunction() {
        return f;
    }

    public double updateText() throws NotFoundException {
        if (textField.getText().trim().length() == 0) {
            throw new NotFoundException("Name muss definiert sein");
        } // if
        Number n = Formatierer.parse2Number(textField.getText().trim());
        if (n == null) {
            throw new NumberFormatException("Nummer wird erwartet. Z.B. 1.5");
        } // if
        return n.doubleValue();
    } // updateText

    public void setEditable(boolean editable) {
        textField.setEditable(editable);
        if (editable) {
            setBorder(BorderFactory.createLineBorder(Color.black, 2));
        } else {
            setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        }
    }

    public void setFormat(String pattern) {
        decimalFormat.applyPattern(pattern);
        formatSet=true;
        // damit die Zahl von Anfang an richtig dargestellt wird muss renew
        // aufgerufen werden
        renew(null);
    }

    public void doLayout() {
        super.doLayout();
        textField.doLayout();
        // Label wird so verschoben, dass er eng am Rand anliegt
        // EINENG_FAKTOR
        Dimension d = textField.getPreferredSize();
        Dimension dim = new Dimension(d.width, (int) (d.height - EINENG_FAKTOR
                * font.getLeading()));
        setPreferredSize(dim);
        setMaximumSize(dim);
        textField.setLocation(0, -(int) (EINENG_FAKTOR * font.getLeading()));
        // Basislinie wird berechnet
        setBaseline(font.getAscent() + font.getLeading() - EINENG_FAKTOR
                * font.getLeading());
    } // doLayout

    /**
     * Der Border soll zum Schluß gezeichnet werden
     */
    public void paint(Graphics g) {
        paintComponent(g);
        paintChildren(g);
        paintBorder(g);
    }// paint

    public int getColumnsVal() {
        if (stdColumns <= -1)
            return 0;
        else
            return stdColumns;
    } // getColumnsVal

    public void setColumnsVal(int stdColumns) {
        this.stdColumns = stdColumns;
        textField.setColumns(getColumnsVal());
        jtfl.setLimit(getColumnsVal());
        revalidate();
    }

    /**
     * Überschriebene Methode.
     * 
     * @see math4u2.view.formula.AtomicBox#rebuild()
     */
    protected void rebuild() {
        super.rebuild();

        font = new DisplayFont(fontName, fontStyle, Math
                .round(getDisplayHeight()));
        font.initMetrics(getRenderContext().getDefaultFontRenderContext());
    }

    /**
     * Überschriebene Methode.
     * 
     * @see math4u2.view.formula.FormulaElement#getSpacingClass()
     */
    public Class getSpacingClass() {
        return getClass();
    }

    /*
     * (non-Javadoc)
     * 
     * @see math4u2.view.textgraphics.interactive.StepInteractive#deactivate(math4u2.controller.Broker)
     */
    public void deactivate(Broker broker) throws BrokerException {
        textField.setEditable(false);
        textField.setDisabledTextColor(Color.black);
        textField.setEnabled(false);
        broker.deleteObjectByKey(getKey());
        setFunction(null);
    } // deactivate

    // ------------------------------------------------------------
    // von MathObject implementierte Methoden

    public Object getKey() {
        return "InteractionBox" + num;
    } // getKey

    /*
     * (non-Javadoc)
     * 
     * @see math4u2.controller.MathObject#getRelationContainer()
     */
    public RelationContainer getRelationContainer() {
        return relationContainer;
    } // relationContainer

    public void renew(MathObject mo) {
		try {
			setValue(f.evalScalar());
		} catch (MathException e) {
			ExceptionManager.doError("Fehler beim Evaluieren der Funktion "
					+ f.getName(), e);
		} // catch
	} // renew

	public void setValue(double d) {
		String s = "";

		if (formatSet) {
			s = decimalFormat.format(d);
		} else {
			s = Formatierer.value2FormularString(d,
					getColumnsVal());
		}
		textField.setText(s);

		if (s.length() <= getColumnsVal()) {
			this.setBackground(validColor);
		} else {
			this.setBackground(invalidColor);
		}
	}//setValue

    public void swapLinks(MathObject oldObject, MathObject newObject)
            throws Exception {
        if (oldObject == f) {
            f = (UserFunction) newObject;
        } else {
            f.swapLinks(oldObject, newObject);
        }
    } // swapLinks

    // --------------------------
    // Ende von MathObject implementierte Methoden

    public class JTextFieldLimit extends PlainDocument {
        private int limit;

        JTextFieldLimit(int limit) {
            super();
            this.limit = limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public void insertString(int offset, String str, AttributeSet attr)
                throws BadLocationException {
            if (str == null)
                return;

            if ((getLength() + str.length()) <= limit) {
                super.insertString(offset, str, attr);
            }
        }
    }

}