package math4u2.util.io.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;

import math4u2.util.exception.ExceptionManager;

public class FileUtils {
	public static void traverseDir(File f, FileFilter filter, Visitor visitor) {
		if (f.isDirectory()) {
			File files[] = f.listFiles(filter);
			for (int i = 0; i < files.length; i++) {
				traverseDir(files[i], filter, visitor);
			} // for i
		} else {
			visitor.visit(f);
		} // else
	} // traverseDir

	/**
	 * Berechnet wie viele "../" an der Datei begin angefügt werden müßen, bis
	 * man bei end steht
	 * 
	 * @param begin
	 *            Datei, von der aus zurück gegangen werden soll
	 * @param end
	 *            Pfad, an den man zum Schluß ankommen soll
	 * @return String, der zurück führt z.B. "../../../"
	 */
	public static String getBackStepsToFile(File begin, File end)
			throws FileNotFoundException {
		if (!end.exists())
			throw new FileNotFoundException("Endpfad nicht gefunden: " + end);
		if (!end.isDirectory())
			throw new IllegalArgumentException("Endpfad ist kein Verzeichnis: "
					+ end);
		String str = "";
		begin = begin.getParentFile();
		while (!begin.equals(end)) {
			begin = begin.getParentFile();
			str += "../";
		} // while not same
		return str;
	} // getBackStepsToFile

	public static String urlDecode(String str) {
		String encoding = "ISO-8859-1";
		try {
			return java.net.URLDecoder.decode(str, encoding);
		} catch (UnsupportedEncodingException e) {
			ExceptionManager.doError("Das Encoding " + encoding
					+ " wird nicht unterstützt", e);
		}// catch
		return null;
	} // urlDecode

	/**
	 * Legt eine eine temporäre Datei an, die ordnungsgemäßem Herunterfahren der
	 * Applikation wieder gelöscht wird.
	 * 
	 * @param file
	 *            die anzulegende Datei
	 * @param content
	 *            Inhalt der Datei
	 */
	public static void writeTempFile(File file, String content) {
		FileWriter fw;
		BufferedWriter bw;

		try {
			file.getParentFile().mkdirs();
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			fw.close();
		} catch (IOException e) {
			ExceptionManager.doError("Fehler beim Schreiben der Temporärdatei "
					+ file.getAbsolutePath(), e);
		}
		file.deleteOnExit();
	}

	/**
	 * Legt eine neue Datei an.
	 * 
	 * @param pathname
	 *            Pfad der Datei
	 * @return <code>true</code> wenn Datei erzeugt wurde, anderfalls
	 *         <code>false</code>
	 */
	public static boolean createFile(String pathname) {
		File newServerProperties = new File(pathname);
		boolean result = false;
		try {
			result = newServerProperties.createNewFile();
		} catch (IOException e) {
			ExceptionManager.doError("FileUtils.createFile IOException!", e);
		}
		return result;
	}

	/**
	 * Konvertiert eine URL zu einen URI-Pfad
	 * 
	 * @param url
	 *            URL der Datei
	 * @return URI-Pfad der Datei
	 */
	public static String urlToUriPath(URL url) {
		return urlToFile(url).toURI().getPath();
	}

	/**
	 * Konvertiert eine URL to einer Datei.
	 * 
	 * @param url
	 *            URL der Datei
	 * @return File-Objekt
	 */
	public static File urlToFile(URL url) {
		File file = null;
		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			file = new File(url.getPath());
		}
		return file;
	}

} // FileUtils
