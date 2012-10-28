package math4u2.view.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import math4u2.controller.Broker;
import math4u2.controller.MathObject;
import math4u2.exercises.EParser;
import math4u2.exercises.ui.Math4u2TextPane;
import math4u2.exercises.ui.StyledText;
import math4u2.mathematics.affine.TextBubble;
import math4u2.mathematics.functions.MathException;
import math4u2.mathematics.functions.UserFunction;
import math4u2.util.exception.ExceptionManager;
import math4u2.util.exception.parser.ParseException;
import math4u2.view.graph.drawarea.DrawAreaInterface;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * TextBubble-Graph
 */
public class TextBubbleGraph extends AbstractGraph {
	/** Referenz zu TextBubble */
	private UserFunction textBubble;
	
	/** gecachte Evaluierung der Textblase */
	private TextBubble cacheObject;

	/** Referenz zum Parser für XML-Lektionen */
	private static EParser parser;

	/** Textfläche */
	private Math4u2TextPane textPane = new Math4u2TextPane();

	/** Geparster Inhalt der Textfläche */
	private StyledText styledText = new StyledText();

	/** Textinhalt des vorherigen Zustands (aus Caching-Gründen)*/
	private String oldTextContent = "";

	/**
	 * Rahmenbreite der Sprechblasen, Pfeile und PostIt
	 */
	private int thickness = 5;

	/** Abstand vom Rand bis zum Beginn des Sprechblasenstiels */
	private int indent = 10;

	/** Abstand der Sprechblase in X-Richtung vom Zeigepunkt */
	private int distX = 20;

	/** Abstand der Sprechblase in Y-Richtung vom Zeigepunkt */
	private int distY = 30;
	
	/** Maximale Breite des Sprechblasenstiels */
	private int bubbleWidth = 20;
	
	public TextBubbleGraph(DrawAreaInterface da, UserFunction textBubble,
			Broker broker) {
		super(da);
		this.textBubble = textBubble;

		if(parser==null){
			parser = new EParser(broker);
		}
		
		updateTextPane();
		da.add(textPane);
	} //Konstruktor

	public TextBubble getTextBubble(){
		if(cacheObject==null)
			cacheObject=evalTextBubble();
		return cacheObject;
	}//getTextBubble
	
	public TextBubble evalTextBubble(){
		try {
			return (TextBubble) textBubble.eval();
		} catch (MathException e) {
			ExceptionManager.doError("Fehler beim Zeichnen des Textblasen-Graphen "+textBubble,e);
			return null;
		}//catch
	}//evalTextBubble
	
	/**
	 * Parst den Text und schreibt den neu formartieren Text in die
	 * TextPane.
	 */
	private void updateTextPane() {
		if (oldTextContent.equals(getStringText()))
			return;
		Document doc = textPane.getDocument();
		textPane.setVisible(true);
		try {
			//Alten Text löschen
			doc.remove(0, doc.getLength());

			//Neuen Text parsen
			
			org.w3c.dom.Document docDOM = analyseDOM(getStringText());

			styledText = parser.parseText(docDOM.getLastChild(), false);
			styledText.insertText(doc);
			textPane.setCaretPosition(0);
		} catch (BadLocationException e) {
			ExceptionManager.doError("Fehler beim Zeichnen des Textblasen-Graphen "+textBubble,e);
		} catch (ParseException e) {
			ExceptionManager.doError("Fehler beim Zeichnen des Textblasen-Graphen "+textBubble,e);
		}
		oldTextContent = getStringText();
		//		textPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}//updateTextPane

	
	/**
	 * Gibt den Text ohne '"' am Anfang und Ende zurück
	 */
	public String getStringText() {
		String text = getTextBubble().getText();
		text = text.substring(1, text.length() - 1);
		text = text.replaceAll("\\\\\"","\"");
		return text;
	}//getStringText
	
	
	public void paintGraph(Graphics gr) {
		textPane.setVisible(isVisible());
		if (isVisible()) {
			Graphics2D g = (Graphics2D) gr;
			Color ca = g.getColor();
			g.setStroke(getStroke());
			g.setColor(getColor());
			int desX;
			int desY;
			try {
				desX = da.xCoordToPix((getTextBubble().getX()));
				desY = da.yCoordToPix((getTextBubble().getY()));
			} catch (MathException e) {
				ExceptionManager.doError("Fehler beim Zeichnen des Textblasen-Graphen "+textBubble,e);
				return;
			}

			textPane.doLayout();
			Dimension dim = textPane.getPreferredSize();

			//weitere Berechnungen für Bubble
			int butX = desX + distX + thickness;
			int butY = desY + distY + thickness;

			int topX = butX + dim.width;
			int topY = butY + dim.height;

			//weitere Berechnungen für Arrow
			int height = dim.height + 2 * thickness;
			int width = dim.width + thickness;

			int pfY = height * 3 / 2;
			int pfX = Math.min(width * 1 / 2, distX + height / 3) + thickness;
			width = pfX + width;

			//Positionierung des Textes. Hängt von der Orientierung ab.
			int locX = butX;
			int locY = butY;

			GeneralPath gp = null;
			if (getTextBubble().getOrientation().equals("B_SE")) {
				//Sprechblase Südost
				gp = getBubble(desX, desY, butX, butY, topX, topY);
			} else if (getTextBubble().getOrientation().equals("B_NE")) {
				//Sprechblase Nordost
				gp = getBubble(desX, desY, butX, butY, topX, topY);
				gp
						.transform(AffineTransform.getTranslateInstance(-desX,
								-desY));
				gp.transform(AffineTransform.getScaleInstance(1, -1));
				gp.transform(AffineTransform.getTranslateInstance(desX, desY));
				locY = desY - distY - thickness - dim.height;
			} else if (getTextBubble().getOrientation().equals("B_NW")) {
				//Sprechblase Nordwest
				gp = getBubble(desX, desY, butX, butY, topX, topY);
				gp
						.transform(AffineTransform.getTranslateInstance(-desX,
								-desY));
				gp.transform(AffineTransform.getScaleInstance(-1, -1));
				gp.transform(AffineTransform.getTranslateInstance(desX, desY));
				locY = desY - distY - thickness - dim.height;
				locX = desX - distX - thickness - dim.width;
			} else if (getTextBubble().getOrientation().equals("B_SW")) {
				//Sprechblase Südwest
				gp = getBubble(desX, desY, butX, butY, topX, topY);
				gp
						.transform(AffineTransform.getTranslateInstance(-desX,
								-desY));
				gp.transform(AffineTransform.getScaleInstance(-1, 1));
				gp.transform(AffineTransform.getTranslateInstance(desX, desY));
				locX = desX - distX - thickness - dim.width;
			} else if (getTextBubble().getOrientation().equals("A_W")) {
				//Pfeil Westen
				gp = getArrow(desX, desY, pfX, pfY, height, width);
				locX = desX - dim.width - pfX;
				locY = desY - dim.height / 2;
			} else if (getTextBubble().getOrientation().equals("A_E")) {
				//Pfeil Osten
				gp = getArrow(desX, desY, pfX, pfY, height, width);
				gp
						.transform(AffineTransform.getTranslateInstance(-desX,
								-desY));
				gp.transform(AffineTransform.getScaleInstance(-1, 1));
				gp.transform(AffineTransform.getTranslateInstance(desX, desY));
				locX = desX + pfX;
				locY = desY - dim.height / 2;
			} else if (getTextBubble().getOrientation().equals("A_N")) {
				//Pfeil Norden
				width = dim.height + thickness;
				height = dim.width + 2 * thickness;
				pfY = height * 3 / 2;
				pfX = Math.min(width * 1 / 2, distX + height / 3) + thickness;
				width = pfX + width;
				gp = getArrow(desX, desY, pfX, pfY, height, width);
				gp
						.transform(AffineTransform.getTranslateInstance(-desX,
								-desY));
				gp.transform(AffineTransform.getRotateInstance(Math.PI / 2));
				gp.transform(AffineTransform.getTranslateInstance(desX, desY));
				locX = desX - dim.width / 2;
				locY = desY - width + thickness;
			} else if (getTextBubble().getOrientation().equals("A_S")) {
				//Pfeil Süden
				width = dim.height + thickness;
				height = dim.width + 2 * thickness;
				pfY = height * 3 / 2;
				pfX = Math.min(width * 1 / 2, distX + height / 3) + thickness;
				width = pfX + width;
				gp = getArrow(desX, desY, pfX, pfY, height, width);
				gp
						.transform(AffineTransform.getTranslateInstance(-desX,
								-desY));
				gp.transform(AffineTransform.getRotateInstance(-Math.PI / 2));
				gp.transform(AffineTransform.getTranslateInstance(desX, desY));
				locX = desX - dim.width / 2;
				locY = desY + pfX;
			} else if (getTextBubble().getOrientation().equals("POSTIT")) {
				//Notizzettel PostIt
				paintPostIt(g, height, width, dim, desX, desY, locX, locY);
				g.setColor(ca);
				return;
			} else if (getTextBubble().getOrientation().equals("C_S")){
				//Textblase
				butX=desX-dim.width/2;
				topX=desX+dim.width/2;
				gp = getBubbleHorizontal(desX, desY, butX, butY, topX, topY);
				locX = desX - dim.width/2;
				int radius=4;
				g.fillArc(desX-radius,desY-radius,radius*2+1,radius*2+1,0,360);
			} else if (getTextBubble().getOrientation().equals("C_N")){
				//Textblase
				butX=desX-dim.width/2;
				topX=desX+dim.width/2;
				gp = getBubbleHorizontal(desX, desY, butX, butY, topX, topY);
				gp.transform(AffineTransform.getTranslateInstance(-desX,-desY));
				gp.transform(AffineTransform.getScaleInstance(1, -1));
				gp.transform(AffineTransform.getTranslateInstance(desX, desY));
								
				locX = desX - dim.width/2;
				locY = desY - distY-dim.height-thickness;
				
				int radius=4;
				g.fillArc(desX-radius,desY-radius,radius*2+1,radius*2+1,0,360);
			} else if(getTextBubble().getOrientation().equals("C_W")){
				width = dim.height + thickness;
				height = dim.width + thickness;
				butX=desX-dim.height/2;
				topX=desX+dim.height/2;
				butY=desY + distY+thickness;
				topY = butY + dim.width;
						
				gp = getBubbleHorizontal(desX, desY, butX, butY, topX, topY);
				gp.transform(AffineTransform.getTranslateInstance(-desX,-desY));
				gp.transform(AffineTransform.getRotateInstance(Math.PI / 2));
				gp.transform(AffineTransform.getTranslateInstance(desX, desY));	
				locX = desX - dim.width - distY - thickness;
				locY = desY - dim.height/2;
				
				int radius=4;
				g.fillArc(desX-radius,desY-radius,radius*2+1,radius*2+1,0,360);
			} else if(getTextBubble().getOrientation().equals("C_E")){
				width = dim.height + thickness;
				height = dim.width + thickness;
				butX=desX-dim.height/2;
				topX=desX+dim.height/2;
				butY=desY + distY+thickness;
				topY = butY + dim.width;
						
				gp = getBubbleHorizontal(desX, desY, butX, butY, topX, topY);
				gp.transform(AffineTransform.getTranslateInstance(-desX,-desY));
				gp.transform(AffineTransform.getRotateInstance(-Math.PI / 2));
				gp.transform(AffineTransform.getTranslateInstance(desX, desY));	
				locX = desX + distY + thickness;
				locY = desY - dim.height/2;
				
				int radius=4;
				g.fillArc(desX-radius,desY-radius,radius*2+1,radius*2+1,0,360);
			} else if (getTextBubble().getOrientation().equals("K_E")) {
				width = dim.width/2;
				height = dim.height/2;
				
				//g.drawRect(desX-distX,desY,15,15);
				int translX = -desX + width +thickness + distX;
				gp = new GeneralPath();
				gp.transform(AffineTransform.getTranslateInstance(translX, -desY));
				
				gp.moveTo(-width,-height-thickness);
				gp.quadTo(-width-thickness, -height-thickness, -width-thickness,-height); 
				gp.lineTo(-width-thickness, height);
				gp.quadTo(-width-thickness,height+thickness, -width, height+thickness);
				gp.lineTo(width,height+thickness);
				gp.quadTo(width+thickness, height+thickness, width+thickness, height);
				gp.lineTo(width+thickness,-height);
				gp.quadTo(width+thickness,-height-thickness, width, -height-thickness);
				gp.closePath();												
				gp.transform(AffineTransform.getTranslateInstance(-translX, desY));
				gp.transform(AffineTransform.getTranslateInstance(-desX, -desY));
				gp.moveTo(-distX/2,distY/2);
				gp.lineTo(0,0);
				gp.lineTo(-distX/2,-distY/2);
				gp.transform(AffineTransform.getRotateInstance(Math.PI));
				gp.closePath();
				gp.transform(AffineTransform.getTranslateInstance(desX, desY));
				
				locX = desX + distX + thickness; 
				locY = desY - dim.height/2;
			} else if (getTextBubble().getOrientation().equals("K_W")) {
				width = dim.width/2;
				height = dim.height/2;
				
				int translX = -desX + width +thickness + distX;
				gp = new GeneralPath();
				gp.transform(AffineTransform.getTranslateInstance(translX, -desY));
				
				gp.moveTo(-width,-height-thickness);
				gp.quadTo(-width-thickness, -height-thickness, -width-thickness,-height); 
				gp.lineTo(-width-thickness, height);
				gp.quadTo(-width-thickness,height+thickness, -width, height+thickness);
				gp.lineTo(width,height+thickness);
				gp.quadTo(width+thickness, height+thickness, width+thickness, height);
				gp.lineTo(width+thickness,-height);
				gp.quadTo(width+thickness,-height-thickness, width, -height-thickness);
				gp.closePath();				
				gp.transform(AffineTransform.getTranslateInstance(-translX, desY));
				gp.transform(AffineTransform.getTranslateInstance(-desX, -desY));
				gp.moveTo(-distX/2,distY/2);
				gp.lineTo(0,0);
				gp.lineTo(-distX/2,-distY/2);
				gp.closePath();
				gp.transform(AffineTransform.getTranslateInstance(desX, desY));
				
				locX = desX - dim.width - distX - thickness; 
				locY = desY - dim.height/2;
			} else if (getTextBubble().getOrientation().equals("K_N")) {
				width = dim.width/2;
				height = dim.height/2;
				
				int translY = -desY-height-thickness-distX;
				gp = new GeneralPath();
				gp.transform(AffineTransform.getTranslateInstance(-desX, translY));
				
				gp.moveTo(-width,-height-thickness);
				gp.quadTo(-width-thickness, -height-thickness, -width-thickness,-height); 
				gp.lineTo(-width-thickness, height);
				gp.quadTo(-width-thickness,height+thickness, -width, height+thickness);
				gp.lineTo(width,height+thickness);
				gp.quadTo(width+thickness, height+thickness, width+thickness, height);
				gp.lineTo(width+thickness,-height);
				gp.quadTo(width+thickness,-height-thickness, width, -height-thickness);
				gp.closePath();				
				gp.transform(AffineTransform.getTranslateInstance(desX, -translY));
				gp.transform(AffineTransform.getTranslateInstance(-desX, -desY));
				gp.moveTo(-distY/2,distX/2);
				gp.lineTo(0,0);
				gp.lineTo(distY/2,distX/2);
				gp.transform(AffineTransform.getRotateInstance(Math.PI));
				gp.closePath();
				gp.transform(AffineTransform.getTranslateInstance(desX, desY));
				
				locX = desX - width; 
				locY = desY - dim.height - distX - thickness;
			} else if (getTextBubble().getOrientation().equals("K_S")) {
				width = dim.width/2;
				height = dim.height/2;
				
				int translY = -desY-height-thickness-distX;
				gp = new GeneralPath();
				gp.transform(AffineTransform.getTranslateInstance(-desX, translY));
				
				gp.moveTo(-width,-height-thickness);
				gp.quadTo(-width-thickness, -height-thickness, -width-thickness,-height); 
				gp.lineTo(-width-thickness, height);
				gp.quadTo(-width-thickness,height+thickness, -width, height+thickness);
				gp.lineTo(width,height+thickness);
				gp.quadTo(width+thickness, height+thickness, width+thickness, height);
				gp.lineTo(width+thickness,-height);
				gp.quadTo(width+thickness,-height-thickness, width, -height-thickness);
				gp.closePath();				
				gp.transform(AffineTransform.getTranslateInstance(desX, -translY));
				gp.transform(AffineTransform.getTranslateInstance(-desX, -desY));
				gp.moveTo(-distY/2,distX/2);
				gp.lineTo(0,0);
				gp.lineTo(distY/2,distX/2);
				gp.closePath();
				gp.transform(AffineTransform.getTranslateInstance(desX, desY));
				
				locX = desX - width; 
				locY = desY + distX + thickness;			
			} else {
				ExceptionManager.doError("Orientierung nicht bekannt: "
						+ getTextBubble().getOrientation());
				return;
			}//else

			g.setColor(getTextBubble().getFillColor());
			g.fill(gp);
			g.setColor(getTextBubble().getBorderColor());
			g.draw(gp);

			textPane.setLocation(locX, locY);
			textPane.setSize(dim);

			g.setColor(ca);
		} //if visible
	} //paintGraph

	/**
	 * Zeichnet ein PostIt auf die Zeichenfläche 
	 */
	private void paintPostIt(Graphics2D g, int height, int width, Dimension dim, int desX,
			int desY, int locX, int locY) {
		height = dim.height + 2 * thickness;
		width = dim.width + 2 * thickness;

		GeneralPath gp = new GeneralPath();
		gp.transform(AffineTransform.getTranslateInstance(-desX, -desY));
		gp.moveTo(0, 0);
		gp.lineTo(width, 0);
		gp.lineTo(width, height - indent);
		gp.lineTo(width - indent * 2, height);
		gp.lineTo(0, height);
		gp.closePath();
		gp.transform(AffineTransform.getTranslateInstance(desX, desY));

		GeneralPath knickFill = new GeneralPath();
		knickFill.transform(AffineTransform.getTranslateInstance(-desX, -desY));
		knickFill.moveTo(width - indent * 2, height);
		knickFill.lineTo(width - indent * 2, height - indent);
		knickFill.quadTo(width - indent, height - indent * 2 / 3, width, height
				- indent);
		knickFill.lineTo(width, height - indent);
		knickFill.closePath();
		knickFill.transform(AffineTransform.getTranslateInstance(desX, desY));

		locX = desX + thickness;
		locY = desY + thickness;

		g.setColor(getTextBubble().getFillColor());
		g.fill(gp);
		Color c = getTextBubble().getFillColor().darker().darker();
		g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), getTextBubble()
				.getFillColor().getAlpha()));
		g.fill(knickFill);
		g.setColor(getTextBubble().getBorderColor());
		gp.append(knickFill, false);
		g.draw(gp);

		textPane.setLocation(locX, locY);
		textPane.setSize(dim);
	}//paintPostIt

	
	/**
	 * Erzeugt einen Pfeil nach rechts
	 */
	private GeneralPath getArrow(int desX, int desY, int pfX, int pfY,
			int height, int width) {
		GeneralPath gp = new GeneralPath();
		gp.transform(AffineTransform.getTranslateInstance(-desX, -desY));
		gp.moveTo(0, 0);
		gp.lineTo(-pfX, -pfY / 2);
		gp.lineTo(-pfX, -height / 2);
		gp.lineTo(-width, -height / 2);
		gp.lineTo(-width, height / 2);
		gp.lineTo(-pfX, height / 2);
		gp.lineTo(-pfX, pfY / 2);
		gp.transform(AffineTransform.getTranslateInstance(desX, desY));
		gp.closePath();
		return gp;
	}//getArrow

	
	/**
	 * Erzeugt eine Sprechblase mit Pfeil nach links oben
	 */
	private GeneralPath getBubble(int desX, int desY, int butX, int butY,
			int topX, int topY) {
		GeneralPath gp = new GeneralPath();
		gp.moveTo(desX, desY);
		gp.lineTo(butX + indent, butY - thickness);
		gp.lineTo(butX, butY - thickness);
		gp.quadTo(butX - thickness, butY - thickness, butX - thickness, butY);
		gp.lineTo(butX - thickness, topY);
		gp.quadTo(butX - thickness, topY + thickness, butX, topY + thickness);
		gp.lineTo(topX, topY + thickness);
		gp.quadTo(topX + thickness, topY + thickness, topX + thickness, topY);
		gp.lineTo(topX + thickness, butY);
		gp.quadTo(topX + thickness, butY - thickness, topX, butY - thickness);
		gp.lineTo(Math.min(butX + indent + bubbleWidth, topX - 5), butY - thickness);
		gp.closePath();
		return gp;
	}//getBubble
	
	/**
	 * Erzeugt eine Sprechblase mit Pfeil nach unten
	 */
	private GeneralPath getBubbleHorizontal(int desX, int desY, int butX, int butY,
			int topX, int topY) {
		GeneralPath gp = new GeneralPath();
		gp.moveTo(desX, desY-indent);
		gp.lineTo(desX, butY - thickness);
		gp.lineTo(butX, butY - thickness);
		gp.quadTo(butX - thickness, butY - thickness, butX - thickness, butY);
		gp.lineTo(butX - thickness, topY);
		gp.quadTo(butX - thickness, topY + thickness, butX, topY + thickness);
		gp.lineTo(topX, topY + thickness);
		gp.quadTo(topX + thickness, topY + thickness, topX + thickness, topY);
		gp.lineTo(topX + thickness, butY);
		gp.quadTo(topX + thickness, butY - thickness, topX, butY - thickness);
		gp.lineTo(desX, butY - thickness);
		gp.closePath();
		return gp;
	}//getBubble	

	
	public HasGraph getModel() {
		return textBubble;
	}//getModel

	
	public void renew(MathObject source) {
		super.renew(source);
		updateTextPane();
	}//renew

	
	/**
	 * @see math4u2.controller.MathObject#swapLinks(math4u2.controller.MathObject,
	 *      math4u2.controller.MathObject)
	 */
	public void swapLinks(MathObject oldObject, MathObject newObject)
			throws Exception {
		if (textBubble == oldObject)
			textBubble = (UserFunction) newObject;
		else
			textBubble.swapLinks(oldObject, newObject);
		
		cacheObject = evalTextBubble();
	} //swapLinks
	
	
	public void prepareDelete() {
		da.remove(textPane);
		super.prepareDelete();
	}//prepareDelete
	

	/**
	 * Die TextPane wird aus einem XML-String generiert. Diesr String 
	 * muß zuerst hier geparst werden.
	 */
	private org.w3c.dom.Document analyseDOM(String message) {
		try {
			String header = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<!DOCTYPE math4u2 PUBLIC \"-//FH Augsburg//DTD Math4u2//DE\" \"http://www.math4u2.de/xml/dtd/math4u2DTD.dtd\">";
			StringReader sr = new StringReader(header + "<description>" + message + "</description>");
			return parser.getDocumentBuilder().parse(new InputSource(sr));
		} catch (SAXParseException e) {
			ExceptionManager.doError("Zeile: " + e.getLineNumber()
					+ " Spalte: " + e.getColumnNumber() + " in der Datei:"
					+ e.getSystemId(), e);
		} catch (SAXException e) {
			ExceptionManager.doError(e);
		} catch (IOException e) {
			ExceptionManager.doError(e);
		}
		return null;
	}//analyseDOM
} //class TextBubbleGraph
