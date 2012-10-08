package math4u2.util.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import math4u2.util.exception.ExceptionManager;
import math4u2.util.io.StreamCopy;

/**
 * Tool-Klasse fuer HTTP-Verbindungen und URL-Parsen.
 * 
 * @author Michael Lichtenstern
 */
public class HTTP {
	/**
	 * Liefert den Inhalt fuer eine passwortgeschützte URL. <br>
	 * Die Zugangsdaten werden Base64 verschlüsselt geschickt.
	 * 
	 * @param serverURL
	 *            URL
	 * @param name
	 *            Benutzername
	 * @param password
	 *            Passwort
	 * @return
	 */

	public static String getSecureHTTPContent(String serverURL, String name,
			String password) {
		String temp = "";
		try {
			URL url = new URL(serverURL);
			String userPassword = name + ":" + password;
			String encoding = new sun.misc.BASE64Encoder().encode(userPassword
					.getBytes());
			URLConnection uc = url.openConnection();
			uc.setRequestProperty("Authorization", "Basic " + encoding);
			InputStream is = uc.getInputStream();
			temp = StreamCopy.streamRead(is);
			is.close();
		} catch (MalformedURLException e) {
			ExceptionManager
					.doError("Die passwortgeschützte URL ist nicht erreichbar!");
		} catch (IOException e) {
			ExceptionManager
					.doError("Bitte überprüfen Sie nochmals Name und Passwort!");
		}
		return temp;
	}

	/**
	 * Liefert den Inhalt fuer eine URL.
	 * 
	 * @param serverURL
	 *            Server-URL
	 * @return Inhalt der angeforderten Adresse
	 */
	public static String getHTTPContent(String serverURL) {
		String temp = "";
		try {
			URL url = new URL(serverURL);
			InputStream is = url.openStream();
			temp = StreamCopy.streamRead(is);
			is.close();
		} catch (MalformedURLException e) {
			ExceptionManager.doError(serverURL+" ist keine gültige URL", e);
		} catch (IOException e) {
			ExceptionManager.doError("Die angebene URL ist nicht erreichbar ("+serverURL+")", e);
		}
		return temp;
	}

	/**
	 * Parst aus einer URL einen Namen welcher im Dateisystem verwendet werden
	 * kann. <br>
	 * <br>
	 * Beispiel: URL: http://www.math4u2.de <br>
	 * Name: math4u2_de
	 * 
	 * @param url
	 *            URL
	 * @return Name
	 */
	public static String parseNameFromURL(String url) {
		url = url.replaceAll(":\\/\\/", "");
		return url.replaceAll(".","_");
	}
	
	/**
	 * Parst die URL.<br>
	 * <br>
	 * Beispiel: http://www.math4u2.de/asdf/bsdf.html<br>
	 * url: http://www.math4u2.de/
	 * 
	 * @param url
	 * @return
	 */
	public static String parseURL (String url) {
	    int pos = url.indexOf("://");
	    if(pos==-1)
	    	throw new IllegalArgumentException("Ungültige URL: "+url);
	    pos = url.indexOf("/",pos+3);
	    return url.substring(0,pos+1);
	}

	/**
	 * Enfernt das Protokoll von einer URL <br>
	 * <br>
	 * Beispiel: URL: http://www.math4u2.de <br>
	 * Name: www.math4u2.de
	 * 
	 * @param url
	 *            URL
	 * @return Name
	 */
	public static String removeProtocolFromURL(String url) {
		int pos = url.indexOf("://");
		if(pos==-1)
			return url;
		else
			return url.substring(pos+"://".length());
	}
}