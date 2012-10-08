package math4u2.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Fenn Stefan
 *  
 */
public class StreamCopy {

	public static void streamCopy(InputStream in, OutputStream out)
			throws IOException {
		//Kopieren der Daten
		int numRead = 0;
		byte[] buf = new byte[4096];
		while ((numRead = in.read(buf)) >= 0) {
			out.write(buf, 0, numRead);
		}//while
		out.flush();
	}//streamCopy

	public static String streamRead(InputStream in) throws IOException {
		StringBuffer sb = new StringBuffer();
		//Kopieren der Daten
		int numRead = 0;
		byte[] buf = new byte[4096];
		while ((numRead = in.read(buf)) >= 0) {
			sb.append(new String(buf, 0, numRead));
		}//while
		return sb.toString();
	}//streamCopy
}