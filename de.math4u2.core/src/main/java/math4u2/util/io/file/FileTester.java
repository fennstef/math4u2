package math4u2.util.io.file;

import java.io.File;

/**
 * Dieses Interface beschreibt eine Klasse, die nach einen bestimmten Kriterium
 * einer Datei getestet wird.
 * 
 * @author Fenn Stefan
 */
interface FileTester {

	public boolean test(File file);

}//interface FileTester
