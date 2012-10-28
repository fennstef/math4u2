// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Klasse die ein Math4U2-Dokument repräsentiert.
 *  
 */
public class Math4U2Document {
	private Map metaTags = new HashMap();

	private List issues = new LinkedList();

	private List folders = new LinkedList();

	public String getMetaTag(String name) {
		return (String) metaTags.get(name);
	}//getMetaTag

	public List getIssues() {
		return issues;
	}//getIssues

	public List getFolders() {
		return folders;
	}//getFolders

	public void setMetaTags(Map metaTags) {
		this.metaTags = metaTags;
	}//setMetaTags

	public void setIssues(List issues) {
		this.issues = issues;
		if (this.issues != null)
			java.util.Collections.sort(this.issues);
	}//setIssues

	public void setFolders(List folders) {
		this.folders = folders;
		sortAll();
	}//setFolders

	public void sortAll() {
		if (folders != null) {
			Collections.sort(folders);

			Iterator iterator = folders.iterator();
			while (iterator.hasNext()) {
				Folder f = (Folder) iterator.next();
				f.sortAll();
			}//while
		}//if
	}//sortAll

}//class Math4u2Document
