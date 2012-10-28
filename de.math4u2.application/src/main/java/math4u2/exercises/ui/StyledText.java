// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises.ui;

import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import math4u2.view.formula.AtomicBox;

/**
 * Klasse für formatierten Text
 * 
 * @author Erich Seifert
 * @version 0.1
 */
public class StyledText {
	private StringBuffer text = new StringBuffer();

	private final LinkedList styleRuns = new LinkedList();

	private final LinkedList components = new LinkedList();

	private final class StyleRun {
		public final String style;

		public int off, len, indent = 0;

		public StyleRun(String styleName, int offset, int length, int ind) {
			style = styleName;
			off = offset;
			len = length;
			indent = ind;
		}
	}

	/**
	 * Fügt den aktuellen formatierten Text in ein Dokument ein. Die dafür
	 * verwendeten Formatierungen können mit Hilfe des Parameters
	 * <code>style</code> überschrieben werden.
	 * 
	 * @param doc
	 *            Das Dokument in das der Text eingefügt werden soll
	 * @param style
	 *            Der Textstil der zum Einfügen verwendet werden soll
	 */
	public void insertText(Document doc, String style)
			throws BadLocationException {
		StyledDocument sDoc = (StyledDocument) doc;
		Iterator iter = styleRuns.iterator();
		Iterator c = components.iterator();

		while (iter.hasNext()) {
			StyleRun sr = (StyleRun) iter.next();

			if (style == null) {
				if (sr.style.compareTo("formula") == 0) {
					if (c.hasNext()) {
						AtomicBox comp = (AtomicBox) c.next();
						SimpleAttributeSet attrs = new SimpleAttributeSet();
						//						if(comp.getFormatierer().getParametersToDeleteAfterUse()!=null)
						// throw new NullPointerException();

						StyleConstants.setComponent(attrs, comp);
						doc.insertString(doc.getLength(), " ", attrs);
					}//if c.hasNext()
				}//if "formula"
				else {
					Style styleC = ESkin.styleContext.getStyle(sr.style);
					doc.insertString(doc.getLength(), text.substring(sr.off,
							sr.off + sr.len), styleC);
					if (sr.indent != 0)
						sDoc.setParagraphAttributes(sDoc.getLength() - sr.len,
								sr.len, ESkin.styleContext.getStyle("indent"),
								false);
					else
						sDoc.setParagraphAttributes(sDoc.getLength() - sr.len,
								sr.len,
								ESkin.styleContext.getStyle("noindent"), false);
				}//else - not "formula"
			} else {
				sDoc.insertString(doc.getLength(), text.substring(sr.off,
						sr.off + sr.len), ESkin.styleContext.getStyle(style));
			}//else
		}
	}//insertText

	/**
	 * Fügt den aktuellen formatierten Text in ein Dokument ein.
	 * 
	 * @param doc
	 *            Das Dokument in das der Text eingefügt werden soll
	 */
	public void insertText(Document doc) throws BadLocationException {
		insertText(doc, null);
	}

	/**
	 * Fügt ein neues, einheitlich formtiertes Textfragment hinzu.
	 * 
	 * @param text
	 *            Der anhzuhängende Text
	 * @param style
	 *            Der Textstil in dem der Text später eingefügt werden soll
	 */
	public void add(String str, String style, int indent) {
		StyleRun lastStyle = null;
		if (!styleRuns.isEmpty())
			lastStyle = (StyleRun) (styleRuns.getLast());

		if ((lastStyle != null) && (lastStyle.style.compareTo(style) == 0))
			lastStyle.len += str.length();
		else
			styleRuns.add(new StyleRun(style, text.length(), str.length(),
					indent));

		text.append(str);
	}//add

	public void add(String str, String style) {
		add(str, style, 0);
	}

	/**
	 * Fügt ein neues Textfragment im Standard-Textstil hinzu.
	 * 
	 * @param text
	 *            Der anzuhängende Text
	 */
	public void add(String str) {
		add(str, StyleContext.DEFAULT_STYLE);
	}

	/**
	 * Fügt eine neue <code>AtomicBox</code> -Komponente in den Text ein.
	 * 
	 * @param c
	 *            Die anzuhängende <code>AtomicBox</code> -Komponente
	 */
	public void add(AtomicBox c) {
		// FIXME: Warum braucht's das? ->FormulaLayout
		c.doLayout();
		c.setSize(c.getPreferredSize());

		components.add(c);
		styleRuns.add(new StyleRun("formula", text.length(), 0, 0));
	}
	
	/**
	 * Hängt einen einen bereits vorhandenen <code>StyledText</code> an den
	 * bisherigen an.
	 * 
	 * @param text
	 *            Der anzuhängende Text
	 */
	public void add(StyledText s, int indent) {
		Iterator iter = s.styleRuns.iterator();
		int textLen = text.length();
		while (iter.hasNext()) {
			StyleRun sr = (StyleRun) iter.next();
			styleRuns.add(new StyleRun(sr.style, sr.off + textLen, sr.len,
					sr.indent + indent));
		}
		text.append(s.text);
		components.addAll(s.components);
	}//add

	/**
	 * Hängt einen einen bereits vorhandenen <code>StyledText</code> an den
	 * bisherigen an.
	 * 
	 * @param text
	 *            Der anzuhängende Text
	 */
	public void add(StyledText s) {
		add(s, 0);
	}

	/**
	 * Wandelt den gespeicherten Text in einen Java-String um.
	 */
	public String toString() {
		return text.toString();
	}

	/**
	 * Gibt an, ob der Text leer ist, also keine Textfragente gespeichert sind.
	 * 
	 * @return Einen boolschen Wert. Wenn das Objekt leer ist ist er
	 *         <code>true</code> und wenn das Objekt Text beinhaltet ist er
	 *         <code>false</code>.
	 */
	public final boolean isEmpty() {
		return (text.length() == 0) && (components.isEmpty());
	}//isEmpty

	/**
	 * Gibt eine Liste aller Komponenten zurück
	 */
	public LinkedList getComponents() {
		return components;
	}//getComponents

}//StyledText
