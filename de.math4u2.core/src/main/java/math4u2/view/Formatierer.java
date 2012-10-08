package math4u2.view;
import java.awt.Color;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParsePosition;

import javax.swing.border.Border;

public class Formatierer {
	//Properties
	private boolean memberBrackets = false;

	private boolean editable = true;

	private int relFontSize = 0;

	private Border border = null;

	private static DecimalFormatSymbols dfs = new DecimalFormatSymbols(
			java.util.Locale.US);

	private static DecimalFormat decimalFormat = new DecimalFormat("#.##",
			dfs);

	private static DecimalFormat decimalFormatExp = new DecimalFormat(
			"0E0", dfs);

	private Color backColor = null;

	private String showStyle = "formal";
	
	private DecimalFormat decimalFormatSpecial;

	/**
	 * Stil, wie das Objekt dargestellt werden soll.
	 * 
	 * @param s
	 */
	public void setShowStyle(String s) {
		showStyle = s;
	}//setShowStyle

	public String getShowStyle() {
		return showStyle;
	}//getShowStyle

	/**
	 * Setzen der Hintergrundfarbe
	 * 
	 * @param c
	 *            Farbe
	 */
	public void setBackColor(Color c) {
		backColor = c;
	}//setBackColor

	/**
	 * Holen der Hintergrundfarbe
	 * 
	 * @return Farbe
	 */
	public Color getBackColor() {
		return backColor;
	}//getBackColor

	/**
	 * Flag, ob Benutzerklammern gesetzt werden sollen
	 * 
	 * @param b
	 */
	public void setMemberBrackets(boolean b) {
		memberBrackets = b;
	}//setMemeberBrackets

	public boolean getMemberBrackets() {
		return memberBrackets;
	}//getMemeberBrackets

	/**
	 * Wie soll der Rahmen aussehen?
	 * 
	 * @param b
	 */
	public void setBorder(Border b) {
		border = b;
	}//setBorder

	public Border getBorder() {
		return border;
	}//getBorder

	/**
	 * Soll das Objekt editierbar sein?
	 * 
	 * @param b
	 */
	public void setEditable(boolean b) {
		editable = b;
	}//setEditable

	public boolean isEditable() {
		return editable;
	}//isEditable

	/**
	 * Holen der relativen Fontgröße
	 * 
	 * @return Fontgröße
	 */
	public int getRelFontSize() {
		return relFontSize;
	}//getRelFontSize

	/**
	 * Setzen der relativen Fontgröße
	 * 
	 * @param r
	 *            Fontgröße
	 */
	public void setRelFontSize(int r) {
		relFontSize = r;
	}//setRelFontSize

	/**
	 * Parst einen String nach dem aktuellen Format.
	 * 
	 * @param s
	 *            zu parsender String
	 * @return Nummer
	 */
	public static Number parse2Number(String s) {
		return decimalFormat.parse(s, new ParsePosition(0));
	} //parseDouble

	/**
	 * Erzeugt einen String aus ein Zahl mit dem aktuellen Format
	 * 
	 * @param d
	 *            Zahl die dargestellt werden soll
	 * @return Stringrepräsentation
	 */
	public static String value2String(double d) {
		/*
		 * Wenn die 0 nicht extra behandelt wird, kommt es beim Drucken
		 * zu Problemen (bis zum Absturz des Betriebsystems!) 
		 */
		if(d==0) return "0";
		
		double ad = Math.abs(d);
		if ( ((ad > 0.01) && ((ad < 9999)) || (d == 0.0)))
			return decimalFormat.format(d);
		else
			return decimalFormatExp.format(d);
	} //value2String
	
	/**
	 * Erzeugt einen String aus ein Zahl mit dem aktuellen Format
	 * Kann überschrieben werden - für verschiedene Zwecke;
	 * @param d
	 *            Zahl die dargestellt werden soll
	 * @return Stringrepräsentation
	 */
	public String value2StringSpecial(double d, int width) {
		if(decimalFormatSpecial==null){
			return value2FormularString(d,width);
		}else{
			return decimalFormatSpecial.format(d);
		}
	} //value2String	
	
	
	/**
	 * Erzeugt einen String aus ein Zahl mit dem aktuellen Format
	 * 
	 * @param d
	 *            Zahl die dargestellt werden soll
	 * @param width
	 * 			  Anzahl der Stellen, die (wenn möglich) nicht überschritten werden sollen.
	 * @return Stringrepräsentation
	 */
	public static String value2FormularString(double d, int width) {
		/*
		 * Wenn die 0 nicht extra behandelt wird, kommt es beim Drucken
		 * zu Problemen (bis zum Absturz des Betriebsystems!) 
		 */
		String s = Double.toString(d);
		if(s.length()<=width) return s;
		
		double ad = Math.abs(d);
		
		//Anzahl der Ziffern berechnen
		int digits = width;
		if(d!=ad){
			//Minus-Zeichen abzeihen, da d negativ ist
			digits--;
		}//if
		
		if ( ((ad > Math.pow(10,-digits+3)) && ((ad < Math.pow(10,digits)-1)) || (d == 0.0))){
			double rnd = Math.round(ad);
			
			int posDigits = (new DecimalFormat("0", dfs).format(rnd)).length();
			if(rnd!=0 && posDigits>=digits-1){
				//Falls positive Ziffern
				String format ="#";
				for(int i=0; i<digits; i++)
					format +='#';
				DecimalFormat decimalFormat2 = new DecimalFormat(format, dfs);
				return decimalFormat2.format(d);
			}//if
			
			String format = "#.";
			String result=null;
			for(int i=0; i<digits-1; i++){
				format +='#';
				DecimalFormat decimalFormat2 = new DecimalFormat(format, dfs);
				String tmp = decimalFormat2.format(d);
				
				if(result==null)
					result = tmp;
				
				if(tmp.length()>width)
					return result;
				result = tmp;
			}//for i
				
			return result;
		}else{
			String format = "0.";
			String result=null;
			for(int i=0; i<digits-1; i++){
				format +='0';
				
				DecimalFormat decimalFormatExp2 = new DecimalFormat(
						format+"E0", dfs);	
				String tmp = decimalFormatExp2.format(d);
				
				if(result==null)
					result = tmp;
				
				if(tmp.length()>width-1)
					return result.replaceFirst("E"," E");
				result = tmp;
			}//for i
			return result.replaceFirst("E"," E");
		}//else
	} //value2FormularString	
	
	/**
	 * Erzeugt einen String aus ein Zahl mit der vollständigen Genauigkeit
	 * 
	 * @param d
	 *            Zahl die dargestellt werden soll
	 * @return Stringrepräsentation
	 */
	public static String fullValue2String(double d) {
	    return d+"";
	} //fullValue2String	
	
	/**
	 * Erzeugt einen String aus ein Zahl mit der Genauigkeit von float
	 * 
	 * @param d
	 *            Zahl die dargestellt werden soll
	 * @return Stringrepräsentation
	 */
	public static String value2FloatString(double d) {
	    return ((float)d)+"";
	} //value2FloatString		
	
	public void setDecimalSpecialPattern(String pattern) {
		if(pattern!=null){
			decimalFormatSpecial = new DecimalFormat(pattern, dfs);
		}
	}//getDecimalSpecialPattern

} //Formatierer
