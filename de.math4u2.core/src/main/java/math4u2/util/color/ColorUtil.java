package math4u2.util.color;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Hilfsklasse zum parsen von Farben
 * 
 * @author Fenn Stefan
 */
public class ColorUtil {
	
	public static final Map colorMap = new HashMap();
	
	static{
		colorMap.put("black", new Color(0,0,0));
		colorMap.put("darkgray",new Color(70,70,70));
		colorMap.put("gray", new Color(100,100,100));
		colorMap.put("lightgray", new Color(130,130,130));
		colorMap.put("white", new Color(255,255,255));
		colorMap.put("darkred", new Color(166,36,36));
		colorMap.put("red", new Color(236,0,0));
		colorMap.put("orange", new Color(232,167,1));
		colorMap.put("lightorange", new Color(244,200,108));
		colorMap.put("yellow", new Color(211,213,2));
		colorMap.put("lightgreen", new Color(104,185,94));
		colorMap.put("green", new Color(57,126,42));		
		colorMap.put("cyan", new Color(54,175,210));
		colorMap.put("blue", new Color(58,54,210));
		colorMap.put("magenta", new Color(177,2,175));
		colorMap.put("pink", new Color(255,192,203));

		
		//Textfarben
		colorMap.put("fillgray", new Color(100,100,100,100));
		colorMap.put("fillwhite", new Color(255,255,255,190));
		colorMap.put("fillred", new Color(238,81,81,190));
		colorMap.put("fillorange", new Color(232,167,1,190));
		colorMap.put("fillyellow", new Color(255,255,102,190));
		colorMap.put("fillgreen", new Color(200,250,50,170));
		colorMap.put("fillcyan", new Color(54,175,210,150));
		colorMap.put("fillblue", new Color(138,139,255,150));
		colorMap.put("fillmagenta", new Color(179,68,218,130));
	}//statischer Konstruktor

    public static Color parseColor(String paramValue) {
        //Gibt es in java.awt.Color eine vordefinierte Farbe z.B "red","blue"
        // ...
        paramValue = paramValue.trim();
        
        Object obj = colorMap.get(paramValue.toLowerCase());
        if(obj!=null)
        	return (Color) obj;
        
        //Ist es eine Hex-Bezeichnung für die Farbe z.B. 0xff3366ff
        //													a r g b
        if (paramValue.substring(0, 2).equals("0x")
                || paramValue.substring(0, 1).equals("#")) {
            if (paramValue.substring(0, 1).equals("#"))
                paramValue = paramValue.toUpperCase().substring(1,
                        paramValue.length());
            else
                paramValue = paramValue.toUpperCase().substring(2,
                        paramValue.length());
            int r = Integer.parseInt(paramValue.substring(0, 2), 16);
            int g = Integer.parseInt(paramValue.substring(2, 4), 16);
            int b = Integer.parseInt(paramValue.substring(4, 6), 16);
            if (paramValue.length() == 8) {
                //mit Alpha
                int a = Integer.parseInt(paramValue.substring(6, 8), 16);
                return new Color(r, g, b, a);
            } //else mit Alpha
            else {
                return new Color(r, g, b);
            } //else ohne Alpha
        } else if (paramValue.indexOf('.') != -1) {
            //Muß eine RGB Auszeichnung sein z.B. 123.82.0.100
            //									   r .g .b. a
            String parts[] = paramValue.split("\\.");
            int[] c= new int[]{0,0,0,255};
            for (int i = 0; i < Math.min(parts.length,4); i++) {
                c[i] = Integer.parseInt(parts[i]);
            }//for
            return new Color(c[0],c[1],c[2],c[3]);
        } else {
            throw new RuntimeException("Die Farbe " + paramValue
                    + " konnte nicht geparst werden");
        } //else
    } //parseColor
}
