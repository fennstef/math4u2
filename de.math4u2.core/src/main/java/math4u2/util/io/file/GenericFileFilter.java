package math4u2.util.io.file;

import java.io.*;

/**
 * A file filter that recognize *.*, *.ext (any file with the given extendsion),
 * and name.* (any file with the given name)
 */
public class GenericFileFilter extends javax.swing.filechooser.FileFilter
		implements FileFilter {
	private String pattern;

	private String description = "No Description";

	private boolean includeDirs;

	public GenericFileFilter(String pattern) {
		this(pattern, true);
	}

	public GenericFileFilter(String pattern, String des) {
		this(pattern, true);
		description = des;
	}

	public GenericFileFilter(String pattern, String des, boolean includeDirs) {
		this(pattern, true);
		description = des;
		this.includeDirs = includeDirs;
	}

	public GenericFileFilter(String pattern, boolean includeDirs) {
		this.pattern = pattern;
		this.includeDirs = includeDirs;
	}

	public boolean accept(File pathname) {
		if (pathname.isDirectory())
			return includeDirs;

		String name = pathname.getName();
		if (pattern.equals("*.*")) {
			return true;
		} else if (pattern.startsWith("*.")) {
			String patternExt = pattern.substring(pattern.indexOf(".") + 1);
			return name.endsWith("." + patternExt);
		} else if (pattern.endsWith(".*")) {
			String patternName = pattern.substring(0, pattern.indexOf("."));
			return name.startsWith(patternName + ".");
		} else {
			return false;
		}
	}

	public String getDescription() {
		return description;
	}
}//GenericFileFilter
