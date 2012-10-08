package math4u2.util.formulaUtils;

import java.io.*;
import java.util.*;

/**
 * Klasse zum Einlesen von Dateien mit Wertpaaren. In den Dateien sind auch
 * Kommentare am Zeilenende oder ganze Kommentarzeilen erlaubt. Die Zeichenfolge
 * für Kommentare sowie die Zeichenfolge zum Trennen der Wertpaare können selbst
 * festeglegt werden.
 * 
 * @author Erich Seifert
 * @version $Revision: 1.6 $
 */
public class PropertyReader {
    /** Map zum Speichern der Wertpaare */
    private final Map props;

    /** Zeichenfolge, die Kommentare kennzeichnet */
    private String commentChar;

    /** Zeichenfolge, die Trennzeichen kennzeichnet */
    private String delimiterChar;

    /**
     * Konstruktor, der ein neues <code>PropReader</code> -Objekt erstellt und
     * initialisiert es.
     */
    public PropertyReader() {
        props = new HashMap();
        commentChar = "//";
        delimiterChar = "\t";
    }

    /**
     * Liest Wertpaare aus der Datei mit dem übergebenen Dateinamen ein. Dabei
     * werden Kommentarzeilen oder Kommentare am Zeilenende ignoriert.
     * 
     * @param filename
     *            Pfad der Datei, die einglesen werden soll
     * @throws IOException
     *             Wenn ein Lesefehler auftritt oder die Datei nicht gelesen
     *             werden kann
     */
    public void read(String filename) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(
                ClassLoader.getSystemResourceAsStream(filename), "UTF-8"));
        props.clear();        
        String line;
        while ((line = in.readLine()) != null) {
            line = unescape(line);
            int commentIndex = line.indexOf(commentChar);
            if (commentIndex > 0)
                line = line.substring(0, commentIndex);
            line = line.replaceAll("\t+$", "");

            String pair[] = line.split(delimiterChar);
            if (pair.length >= 2) {
                props.put(pair[0], pair[1]);
            }

        }
        in.close();
        dumpProperties(System.out);
    }//read

    /**
     * Liefert den zugeörigen Wert zu einem vorgegebenen Schlüsselwert.
     * 
     * @param key
     *            Schlüsselwert
     * @return zugeöriger Wert
     */
    public String getProperty(String key) {
        return (String) props.get(key);
    }

    /**
     * Gibt alle eingelesenen Properties in den übergebenen Stream aus.
     * 
     * @param o
     *            Stream, in den geschrieben werden soll.
     */
    private void dumpProperties(OutputStream o) {
        PrintWriter out = new PrintWriter(new BufferedOutputStream(o));

        Iterator i = props.keySet().iterator();
        while (i.hasNext()) {
            String key = (String) i.next();
            String value = (String) props.get(key);
            out.println(key+"\t"+value);
        }
    }

    /**
     * Ersetzt alle, durch Umschaltzeichen angegebenen, Unicode-Zeichen durch
     * deren Unicode.
     * 
     * @param str
     *            Zeichenfolge, die umgewandelt werden soll.
     * @return Umgewandelte Zeichenfolge
     */
    private static String unescape(String str) {
        int pos = 0;
        while ((pos = str.indexOf("\\", pos)) != -1) {
            ++pos;
            if ((pos < str.length()) && (str.charAt(pos) == 'u')) {
                ++pos;
                if ((pos + 4 < str.length())) {
                    String code = str.substring(pos, pos + 4);
                    char uni = (char) Integer.parseInt(code, 16);
                    str = str.replaceAll("\\\\u" + code, "" + uni);
                }
            }
        }
        return str;
    }

    /**
     * Gibt die Zeichenfolge zurück, die einen Kommentar kennzeichnet.
     * 
     * @return Zeichenfolge für Kommentare
     */
    public String getCommentChar() {
        return commentChar;
    }

    /**
     * Setzt die Zeichenfolge, die einen Kommentar kennzeichnen soll.
     * 
     * @param commentChar
     */
    public void setCommentChar(String commentChar) {
        this.commentChar = commentChar;
    }

    /**
     * Gibt die Zeichenfolge zurück, die Trennzeichen kennzeichnet.
     * 
     * @return Zeichenfolge für Trennzeichen
     */
    public String getDelimiterChar() {
        return delimiterChar;
    }

    /**
     * Setzt die Zeichenfolge, die Trennzeichen kennzeichnen soll.
     * 
     * @param delimiterChar
     */
    public void setDelimiterChar(String delimiterChar) {
        this.delimiterChar = delimiterChar;
    }

}