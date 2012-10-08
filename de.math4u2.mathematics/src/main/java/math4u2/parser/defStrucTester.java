/*
 * Created on 19.09.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package math4u2.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dr. Weiss
 *
 */
public class defStrucTester {
	
	public static boolean hasDefStruc(String str ){
		Pattern def = Pattern.compile( "[^:=]+:=(\\s)*(vektor|dualvektor|matrix)(\\s)*\\(\\s*finput((:[^\\)]+)|(\\s*))\\)" );
		Matcher mat = def.matcher(str);
		return mat.matches();
	}

}
