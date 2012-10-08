package math4u2.application.resource;

import javax.swing.ImageIcon;

import math4u2.util.exception.ExceptionManager;

/**
 * Sammlung aller benötigten Bilddaten
 * 
 * @author Fenn Stefan
 */
public class Images {
	/** Aktuelles Math4u2-Logo als Icon */
	public static final ImageIcon LOGO_ICON = load("math4u2/images/icon.gif");

	/** Fehler Icon */
	public static final ImageIcon ERROR_ICON = load("math4u2/images/error.gif");

	/** Pfeil nach unten */
	public static final ImageIcon DOWN_ARROW = load("math4u2/images/down.gif");

	/** Pfeil nach unten disabled */
	public static final ImageIcon DOWN_DISABLED = load("math4u2/images/down_disabled.gif");

	/** Pfeil nach oben */
	public static final ImageIcon UP_ARROW = load("math4u2/images/up.gif");

	/** Pfeil nach oben disabled */
	public static final ImageIcon UP_DISABLED = load("math4u2/images/up_disabled.gif");
	
	/** Leere Liste */
	public static final ImageIcon EMPTY_LIST = load("math4u2/images/empty.gif");
	
	/** Leere Liste disabled */
	public static final ImageIcon EMPTY_LIST_DISABLED = load("math4u2/images/empty_disabled.gif");

	/** Zum Anfang der Lektion */
	public static final ImageIcon BEGIN = load("math4u2/images/begin.gif");

	/** Zur nächsten Lektion */
	public static final ImageIcon NEXT = load("math4u2/images/next.gif");

	/** Auge (Sichtbarkeit) eingeschalten */
	public static final ImageIcon EYE_ENABLED = load("math4u2/images/eye.gif");

	/** Auge (Sichtbarkeit) ausgeschalten */
	public static final ImageIcon EYE_DISABLED = load("math4u2/images/eyedis.gif");

	/** Aktuelles Math4u2-Logo */
	public static final ImageIcon LOGO = load("math4u2/images/logo.gif");

	/** Gro0ßes Math4u2-Logo */
	public static final ImageIcon LOGO_BIG = load("math4u2/images/Math4uLogoBig.gif");
	
	/** Bild für Hilfe */
	public static final ImageIcon HELP = load("math4u2/images/help.gif");

	/** Papierkorb Icon */
	public static final ImageIcon TRASH = load("math4u2/images/trash.gif");

	/** Preload Bild */
	public static final ImageIcon PRELOADER = load("math4u2/images/initialLogo.gif");

	/** Themenübersicht anzeigen */
	public static final ImageIcon TREE = load("math4u2/images/tree.gif");

	/** Folder */
	public static final ImageIcon FOLDER = load("math4u2/images/folder.gif");
	
	/** Nachladen */
	public static final ImageIcon REFRESH = load("math4u2/images/refresh.gif");

	/** Internet-Update */
	public static final ImageIcon EARTH = load("math4u2/images/earth.gif");
	
	/** Grenze nach Links */
	public static final ImageIcon LIMIT_LEFT = load("math4u2/images/LimitLeft.gif");
	
	/** Grenze nach Rechts */
	public static final ImageIcon LIMIT_RIGHT = load("math4u2/images/LimitRight.gif");
	
	/** Slider-Modus */
	public static final ImageIcon SLIDER_MODE = load("math4u2/images/SliderMode.png");	
	
	/** Text-Modus */
	public static final ImageIcon TEXT_MODE = load("math4u2/images/TextMode.png");	

	/** Cursor */
	public static final ImageIcon CURSOR = load("math4u2/images/cursor.gif");	
	
	/** Lokale Datei */
	public static final ImageIcon LOCAL_FILE = load("math4u2/images/localFile.gif");
	
	/** Remote Datei */
	public static final ImageIcon REMOTE_FILE = load("math4u2/images/remoteFile.gif");
	
	/** Temporäre Datei */
	public static final ImageIcon TEMP_FILE = load("math4u2/images/tempFile.gif");
	
	/** Druck Icon */
	public static final ImageIcon PRINT_ICON = load("math4u2/images/printIcons/PrintIcon.gif");
	
	/** Collapse Icon */
	public static final ImageIcon COLLAPSED_ICON = load("math4u2/images/collapsed.gif");
	
	/** Expand Icon */
	public static final ImageIcon EXPAND_ICON = load("math4u2/images/expand.gif");
	
	/** Transparentes 1x1 Pixel Icon */
	public static final ImageIcon TRANSPARENT_ICON = load("math4u2/images/transparent.gif");
	
	/** Hinzufügen eines Objekts Icon */
	public static final ImageIcon ADD_BUTTON_ICON = load("math4u2/images/plusButton.gif");
	
	/** Matrix nach oben */
	public static final ImageIcon MATRIX_ARROW_UP = load("math4u2/images/arrows/arrowUp.gif");
	
	/** Matrix nach unten */
	public static final ImageIcon MATRIX_ARROW_DOWN = load("math4u2/images/arrows/arrowDown.gif");
	
	/** Matrix nach links */
	public static final ImageIcon MATRIX_ARROW_LEFT = load("math4u2/images/arrows/arrowLeft.gif");
	
	/** Matrix nach rechts */
	public static final ImageIcon MATRIX_ARROW_RIGHT = load("math4u2/images/arrows/arrowRight.gif");	
	
	public static ImageIcon load(String name) {
		try {
			return new ImageIcon(ClassLoader.getSystemResource(name));
		} catch (Throwable t) {
			ExceptionManager.doError("Das Bild '" + name
					+ "' wurde nicht gefunden");
			throw new NullPointerException();
		}//catch
	}//load
} //class Images
