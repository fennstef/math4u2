package math4u2.exercises;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import math4u2.util.exception.ExceptionManager;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/** SimpleTagParser parst Tags in einer XML-Datei */
public class SimpleTagParser {

	private String fileName;
	
	private Reader reader;
	
	private KXmlParser parser = new KXmlParser();
	
	private StringBuffer text = new StringBuffer();

	public SimpleTagParser(String fileName) {
		this.fileName = fileName;
		restart();
	} //Konstruktor
	
	private void setEntities(){
		try {
			parser.defineEntityReplacementText("not", "\u00AC");
			parser.defineEntityReplacementText("ne", "\u2260");
			parser.defineEntityReplacementText("equals", "\u003D");
			parser.defineEntityReplacementText("NotEqual", "\u2260");
			parser.defineEntityReplacementText("equiv", "\2261");
			parser.defineEntityReplacementText("le", "\u2264");
			parser.defineEntityReplacementText("ge", "\u2265");
			parser.defineEntityReplacementText("sub", "\u2282");
			parser.defineEntityReplacementText("sup", "\2283");
			parser.defineEntityReplacementText("nsub", "\2284");
			parser.defineEntityReplacementText("sube", "\2286");
			parser.defineEntityReplacementText("supe", "\u2287");
			parser.defineEntityReplacementText("oplus", "\u2295");
			parser.defineEntityReplacementText("otimes", "\u2297");
			parser.defineEntityReplacementText("perp", "\u22A5");
			parser.defineEntityReplacementText("forall", "\2200");
			parser.defineEntityReplacementText("part", "\u2202");
			parser.defineEntityReplacementText("exist", "\u2203");
			parser.defineEntityReplacementText("nabla", "\u2207");
			parser.defineEntityReplacementText("isin", "\u2208");
			parser.defineEntityReplacementText("Element", "\u2208");
			parser.defineEntityReplacementText("notin", "\u2209");
			parser.defineEntityReplacementText("ni", "\u220B");
			parser.defineEntityReplacementText("prop", "\u221D");
			parser.defineEntityReplacementText("infin", "\u221E");
			parser.defineEntityReplacementText("ang", "\u2220");
			parser.defineEntityReplacementText("and", "\u2227");
			parser.defineEntityReplacementText("or", "\u2228");
			parser.defineEntityReplacementText("cap", "\u2229");
			parser.defineEntityReplacementText("cup", "\u222A");
			parser.defineEntityReplacementText("sim", "\u223C");
			parser.defineEntityReplacementText("cong", "\u2245");
			parser.defineEntityReplacementText("asymp", "\u224D");
			parser.defineEntityReplacementText("minus", "\u2212");
			parser.defineEntityReplacementText("plus", "\u002B");
			parser.defineEntityReplacementText("plusmn", "\u00B1");
			parser.defineEntityReplacementText("larr", "\u2190");
			parser.defineEntityReplacementText("rarr", "\2192");
			parser.defineEntityReplacementText("uarr", "\u2191");
			parser.defineEntityReplacementText("darr", "\u2193");
			parser.defineEntityReplacementText("harr", "\u2194");
			parser.defineEntityReplacementText("lArr", "\u21D0");
			parser.defineEntityReplacementText("rArr", "\u21D2");
			parser.defineEntityReplacementText("uArr", "\u21D1");
			parser.defineEntityReplacementText("dArr", "\u21D3");
			parser.defineEntityReplacementText("hArr", "\u21D4");
			parser.defineEntityReplacementText("fnof", "\u0192");
			parser.defineEntityReplacementText("alpha", "\u03B1");
			parser.defineEntityReplacementText("beta", "\u03B2");
			parser.defineEntityReplacementText("chi", "\u03C7");
			parser.defineEntityReplacementText("delta", "\u03B4");
			parser.defineEntityReplacementText("Delta", "\u0394");
			parser.defineEntityReplacementText("epsi", "\u03B5");
			parser.defineEntityReplacementText("epsiv", "\u025B");
			parser.defineEntityReplacementText("eta", "\u03B7");
			parser.defineEntityReplacementText("gamma", "\u03B3");
			parser.defineEntityReplacementText("Gamma", "\u0393");
			parser.defineEntityReplacementText("gammad", "\u03DC");
			parser.defineEntityReplacementText("Gammad", "\u03DC");
			parser.defineEntityReplacementText("iota", "\u03B9");
			parser.defineEntityReplacementText("kappa", "\u03BA");
			parser.defineEntityReplacementText("kappav", "\u03F0");
			parser.defineEntityReplacementText("lambda", "\u03BB");
			parser.defineEntityReplacementText("Lambda", "\u039B");
			parser.defineEntityReplacementText("mu", "\u03BC");
			parser.defineEntityReplacementText("nu", "\u03BD");
			parser.defineEntityReplacementText("omega", "\u03C9");
			parser.defineEntityReplacementText("Omega", "\u03A9");
			parser.defineEntityReplacementText("phi", "\u03C6");
			parser.defineEntityReplacementText("Phi", "\u03A6");
			parser.defineEntityReplacementText("phiv", "\u03D5");
			parser.defineEntityReplacementText("pi", "\u03C0");
			parser.defineEntityReplacementText("Pi", "\u03A0");
			parser.defineEntityReplacementText("piv", "\u03D6");
			parser.defineEntityReplacementText("psi", "\u03C8");
			parser.defineEntityReplacementText("Psi", "\u03A8");
			parser.defineEntityReplacementText("rho", "\u03C1");
			parser.defineEntityReplacementText("rhov", "\u03F1");
			parser.defineEntityReplacementText("sigma", "\u03C3");
			parser.defineEntityReplacementText("Sigma", "\u03A3");
			parser.defineEntityReplacementText("sigmav", "\u03C2");
			parser.defineEntityReplacementText("tau", "\u03C4");
			parser.defineEntityReplacementText("theta", "\u03B8");
			parser.defineEntityReplacementText("Theta", "\u0398");
			parser.defineEntityReplacementText("thetav", "\u03D1");
			parser.defineEntityReplacementText("upsi", "\u03C5");
			parser.defineEntityReplacementText("Upsi", "\u03D2");
			parser.defineEntityReplacementText("xi", "\u03BE");
			parser.defineEntityReplacementText("Xi", "\u039E");
			parser.defineEntityReplacementText("zeta", "\u03B6");
			parser.defineEntityReplacementText("Upsilon", "\u03D2");
			parser.defineEntityReplacementText("upsilon", "\u03C5");
		} catch (XmlPullParserException e) {
			ExceptionManager.doError(e);
		}
	}//setEntities

	/** läßt den Parser wieder neu anfangen */
	public void restart() {
		closeReader();
		reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));
		} catch (IOException e) {
			ExceptionManager.doError("Fehler beim Reinitialisieren des Parsers",e);
		}//catch
		
	    try {
			parser.setInput(reader);
			setEntities();
		} catch (XmlPullParserException e1) {
			ExceptionManager.doError("Fehler beim Parsen der Datei "+fileName+" aufgetreten");
		}
	} //init

	/**
	 * Parst die Datei nach bestimmten Tags
	 * 
	 * @param tags
	 *            2-Dimensionaler Array mit folgenden Aufbau: <br>
	 *            1.Dimension: Tags, die irgendwo vorkommen 2.Dimension: Tags,
	 *            die nacheinander kommen müssen Beispiel [ [hallo],[welt] ]
	 *            Findet überall das Tag <hallo> und <welt>Beispiel [
	 *            [hallo,welt] ] Findet die Tag Reihenfolge <hallo>... <welt>
	 * 
	 * return Map mit gefundenen Tags
	 * @throws XmlPullParserException
	 * @throws NoMath4u2DocumentException 
	 */
	public Map parseTagText(String[][] tags) throws XmlPullParserException, NoMath4u2DocumentException {
		Map map = new HashMap();
		int currentTag = 0;
		boolean firstTime = true;
		try {
			while (parser.next() != XmlPullParser.END_DOCUMENT) {
				
				if(parser.getEventType() != XmlPullParser.START_TAG)
					continue;
				
				if(firstTime){
					firstTime=false;
					if(!"math4u2".equals(parser.getName()))
						throw new NoMath4u2DocumentException();
				}
				
				boolean nextTag = false;
				
				int i = 0;
				for (; i < tags[currentTag].length; i++) {

					if(parser.getEventType() != XmlPullParser.START_TAG)
						continue;
				
					String name = parser.getName();
					
//					if("step".equals(name)){
//						closeReader();
//						return map;
//					}//if
					
					if (!tags[currentTag][i].equals(name)){
						continue;						
					}//if
					
					text.delete(0,text.length());
					
					while(parser.next() != XmlPullParser.END_TAG){
						if(parser.getEventType()==XmlPullParser.TEXT){
							text.append(parser.getText());
						}//if
					}//while

					String t = text.toString();
					
					if(!map.containsKey(tags[currentTag][i]))
						map.put(tags[currentTag][i], t);
					//st.nextToken();
					if (i == 0)
						nextTag = true;
				} //for
				if (nextTag){
					currentTag++;
					if(currentTag==tags.length)
						currentTag--;
				}if (currentTag == tags.length && i == tags[currentTag].length-1){
					closeReader();
					return map;
				}
			} //while
		} catch (IOException e) {
			ExceptionManager.doError(e);
		}//catch
		closeReader();
		return map;
	} //parseTagText

	/**
	 * Parst nach einer Tagreihenfolge
	 * @throws XmlPullParserException
	 */
	public List parseForTagList(String[] tags2) throws XmlPullParserException {
		List list = new LinkedList();
		String[][] tags = new String[][] { tags2 };
		int currentTag = 0;
		int i = 0;
		try {
			while (parser.next() != XmlPullParser.END_DOCUMENT) {

				if(parser.getEventType() != XmlPullParser.START_TAG)
					continue;
				
				String name = parser.getName();
				
				if("step".equals(name)){
//					closeReader();
//					return list;
					continue;
				}//if
				
				if (!tags[currentTag][i].equals(name)){
					continue;
				}else if(i!=tags2.length-1){
					i++;
					continue;
				}
				

				text.delete(0,text.length());
				
				while(parser.next() != XmlPullParser.END_TAG){
					if(parser.getEventType()==XmlPullParser.TEXT){
						text.append(parser.getText());
					}//if
				}//while

				String t = text.toString();
				
				if (i++ == tags2.length - 1) {
					list.add(t);
					i = 0;
				} //if
			} //while
		} catch (IOException e) {
			ExceptionManager.doError(e);
		}//catch
		closeReader();
		return list;
	} //parseForTagList
	
	private void closeReader(){
		try {
			if(reader != null)
				reader.close();
		} catch (IOException e) {
			ExceptionManager.doError("Die Datei "+fileName+" kann nicht geschlossen werden",e);
		}//catch		
	}//closeReader

} //class SimpleTagParser
