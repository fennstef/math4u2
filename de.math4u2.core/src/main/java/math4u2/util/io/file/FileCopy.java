package math4u2.util.io.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import math4u2.util.exception.ExceptionManager;

public class FileCopy implements Visitor {
	// copying files from here.
	private File sourceDir;

	// copying files to here.
	private File destDir;

	// test files
	private FileTester tester;

	public FileCopy(File sourceDir, File destDir) {
		this.sourceDir = sourceDir;
		this.destDir = destDir;
	} //Konstruktor

	public FileCopy(File sourceDir, File destDir, FileTester tester) {
		this(sourceDir, destDir);
		this.tester = tester;
	} //Konstruktor 2

	public void visit(Object arg) {
		File sourceFile = (File) arg;
		String relativeSourceFile = sourceFile.toString().substring(
				sourceDir.toString().length() + 1);
		try {
			if (tester == null || tester.test(sourceFile))
				copyFile(new File(sourceDir, relativeSourceFile), new File(
						destDir, relativeSourceFile));
		} catch (IOException e) {
			ExceptionManager.doError("Fehler beim Kopieren der Datei "+sourceFile.getAbsolutePath(),e);
		}//catch
	} //visit

	public static void copyFile(File src, File dest) throws IOException {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			if (dest.isDirectory()) {
				dest.mkdirs();
			} else {
				new File(dest.getParent()).mkdirs();
			} //else
			in = new FileInputStream(src);
			out = new FileOutputStream(dest);

			ByteArrayOutputStream outBuf = new ByteArrayOutputStream((int) src
					.length());

			byte[] buffer = new byte[4096];
			int count = in.read(buffer);
			while (count > 0) {
				outBuf.write(buffer, 0, count);
				count = in.read(buffer);
			} //while
			out.write(outBuf.toByteArray());
		} //try
		finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				ExceptionManager.doError(e);
			}//catch
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				ExceptionManager.doError(e);
			}//catch
		} //finally
	} //copyFile

} //class FileCopy
