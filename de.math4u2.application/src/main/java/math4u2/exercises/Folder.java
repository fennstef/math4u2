// Copyright (c) 2002 Fachhochschule Augsburg

package math4u2.exercises;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Klasse für Ordnerstrukturen im Ansichtsbaum
 *  
 */
public class Folder implements Comparable {
	private List folders = new LinkedList(); // Alle Ordner

	private List issues = new LinkedList(); // Alle Issues

	private String name; //Name des Ordners

	/**
	 * Konstruktor, der ein neues Ordner-Objekt erzeugt.
	 * 
	 * @param nameStr
	 *            Name des Ordners
	 * @param folders
	 *            alle Unterorderner
	 * @param issues
	 *            alle Unterthemen
	 */
	public Folder(String nameStr, List folders, List issues) {
		name = nameStr;
		java.util.Collections.sort(folders);
		java.util.Collections.sort(issues);
		this.issues = issues;
		this.folders = folders;
		sortAll();
	}//Konstruktor

	/**
	 * Konstruktor, der ein neues Ordner-Objekt erzeugt.
	 * 
	 * @param nameStr
	 *            Name des Ordners
	 */
	public Folder(String nameStr) {
		name = nameStr;
	}//Konstruktor

	public List getFolders() {
		return folders;
	}

	public List getIssues() {
		return issues;
	}

	public String getName() {
		return name;
	}

	/**
	 * Gibt einen Iterator zum Durchlaufen der gespeicherten Elemente zurück.
	 * 
	 * @return Iterator zum Ansprechen der Elemente
	 */
	public Iterator getFolderIterator() {
		return folders.iterator();
	}//getFolderIterator

	/**
	 * Gibt einen Iterator zum Durchlaufen der gespeicherten Elemente zurück.
	 * 
	 * @return Iterator zum Ansprechen der Elemente
	 */
	public Iterator getIssuesIterator() {
		return issues.iterator();
	}//getIssuesIterator

	/**
	 * Fügt dem Ordner eine neues Thema hinzu.
	 * 
	 * @param issue
	 *            EIssue-Objekt das hinzugefügt werden soll
	 */
	public void addIssue(EIssue issue) {
		issues.add(issue);
		java.util.Collections.sort(issues);
	}//addIssue

	public boolean hasElement(EIssue issue) {
		List l = this.getIssues();
		Iterator i = l.iterator();
		while (i.hasNext()) {
			EIssue ie = (EIssue) i.next();
			if (ie.getTitle().equalsIgnoreCase(issue.getTitle())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Fügt dem Ordner einen neuen Ordner hinzu.
	 * 
	 * @param folder
	 *            Folder-Objekt das hinzugefügt werden soll
	 */
	public void addFolder(Folder folder) {
		folders.add(folder);
		sortAll();
	}//addFolder

	/** sortiert alle untergeordneten issues und folders */
	public void sortAll() {
		java.util.Collections.sort(issues);
		java.util.Collections.sort(folders);

		Iterator iterator = folders.iterator();
		while (iterator.hasNext()) {
			Folder f = (Folder) iterator.next();
			f.sortAll();
		}//while
	}//sortAll

	public String toString() {
		return getName();
	}//toString

	public int compareTo(Object o) {
		return getName().compareTo(((Folder) o).getName());
	}//compareTo
}//class Folder
