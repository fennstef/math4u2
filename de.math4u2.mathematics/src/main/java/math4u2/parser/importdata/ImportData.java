package math4u2.parser.importdata;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Fenn Stefan
 *
 * Klassen mit dieser Schnittstelle können Daten als Math4u2-String importieren.
 */
public interface ImportData {
	public String importFromStream(InputStream is) throws FileNotFoundException, IOException;	
}
