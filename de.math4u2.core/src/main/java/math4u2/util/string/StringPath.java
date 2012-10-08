package math4u2.util.string;

import java.util.*;

/**
 * Manager von Pfad-Strings wie z.B. a.b.c
 * 
 * @author Fenn Stefan
 */
public class StringPath {

	private String[] path;

	/**
	 * Pfadangabe in der Art wie "a.b.c"
	 */
	public StringPath(String path, String delimiter) {
		if (path == null)
			return;
		StringTokenizer st = new StringTokenizer(path, delimiter);
		List tokens = new ArrayList();
		while (st.hasMoreElements()) {
			tokens.add(st.nextElement());
		}//while
		this.path = (String[]) tokens.toArray(new String[tokens.size()]);
	}//Konstruktor 1

	/**
	 * Delimiter mit "./\"
	 */
	public StringPath(String path) {
		this(path, "./\\");
	}//Konstruktor 2

	/**
	 * Initialisierung mit schon geparsten Pfad
	 */
	public StringPath(String[] path) {
		this.path = path;
	}//Konstruktor 3

	/**
	 * Gibt einen Pfad als Array aus z.B. wird "a.b.c.def" als [a,b,c,de] Array
	 * zurück gegeben
	 */
	public String[] getPath() {
		return path;
	}//getPath

	/**
	 * Gibt den letzten Teil des Pfads zurück
	 */
	public String lastPart() {
		return path[path.length - 1];
	}//lastPart

	public boolean isEmpty() {
		return path.length == 0;
	}//isEmpty

	/**
	 * Gibt den Pfad aus
	 * 
	 * @param goBackIndex
	 *            Anzahl der Pfade, die zurück gegangen werden soll z.B.
	 *            s.begin.x mit goBackIndex=1 => s.begin
	 * @return
	 */
	public String toString(int goBackIndex) {
		String s = "";
		if (path == null)
			return s;
		for (int i = 0; i < path.length - goBackIndex; i++) {
			s += path[i];
			if (i < path.length - goBackIndex - 1)
				s += ".";
		}//for
		return s;
	}//toString

	public String toString() {
		return toString(0);
	}//toString

	public boolean equals(Object o) {
		if (!(o instanceof StringPath))
			return false;
		return ((StringPath) o).toString().equals(toString());
	}//equals

}//class StringPath
