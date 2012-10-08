package math4u2.util.string;

/**
 * @author Fenn Stefan
 *
 * Tools die bei der Stringverarbeitung hilfreich sind.
 */
public class StringUtil {
	
	private static StringUtil instance;
	
	private StringBuffer sb = new StringBuffer();
	
	private StringUtil(){
	}//privater Konstruktor
	
	private static StringUtil getInstance(){
		if(instance==null)
			instance = new StringUtil();
		return instance;
	}//getInstance
	
	public static String implode(String[] strings, String fillPattern){
		StringBuffer sb = getInstance().sb;
		sb.delete(0,sb.length());
		for(int i=0; i<strings.length; i++){
			if(i!=0) sb.append(fillPattern);
			sb.append(strings[i]);
		}//for i
		return sb.toString();
	}//implode
	
    /** 
     * Helper method to convert the first letter of a string to uppercase.
     * And prefix the string with some next string.
     */
     public static String firstLetterToUpperCase(String s) {
         switch (s.length()) {
         case 0:
             return s;

         case 1:
             return Character.toUpperCase(s.charAt(0))+ "";

         default:
             return Character.toUpperCase(s.charAt(0)) + s.substring(1);
         }
     }//firstLetterToUpperCase

}//class StringUtil
