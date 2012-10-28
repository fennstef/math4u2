package math4u2.exercises;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import math4u2.util.io.file.xml.filestatus.FileStatusInterface;

/**
 * Klasse für alle Übungsthemen
 * 
 * @version 0.1
 * @author Erich Seifert
 */
public class EIssue implements Comparable {
    /** Alle Themen-Elemente */
	private List steps = new LinkedList(); 

	/** Alle Alle Übungsfragen/-elemente */
	private final List exercises = new LinkedList();

	private String title, version, author, summary;

	private FileStatusInterface status = null;

	/** Liste der Histories */
	private LinkedList history = new LinkedList();

	/**
	 * Konstruktor, der ein neues EIssue-Objekt erzeugt.
	 * 
	 * @param steps
	 *            Liste mit verschiedenen Steps
	 */
	public EIssue(List steps, List exercises) {
		if (exercises != null) {
			this.exercises.addAll(exercises);
			Iterator i = exercises.iterator();
			while (i.hasNext()) {
				((Exercise) i.next()).setParent(this);
			}//while
		}// if exercises!=null

		this.steps = steps;
		Iterator i = steps.iterator();
		while (i.hasNext()) {
			((Step) i.next()).setParentIssue(this);
		}//while
		//		((Step)steps.get(0)).createView(); // Ansicht erzeugen
	}//Konstruktor

	/**
	 * Dieser Konstruktor ist als Platzhalter für die eigentliche
	 * Initialisierung gedacht, wenn der Parser das Dokument (filename) wirklich
	 * parst.
	 */
	public EIssue(FileStatusInterface status) {
		this.status = status;
	}//EIssue

	public FileStatusInterface getStatus() {
		return status;
	}

	public List getSteps() {
		return steps;
	}//getSteps

	public Step getStep(int i) {
		return (Step) steps.get(i);
	}//getStep
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String t) {
		title = t;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String a) {
		author = a;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String ver) {
		version = ver;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String s) {
		summary = s;
	}

	public String getFilename() {
		return status.getFilename();
	}

	public void setFilename(String fn) {
		status.setFilename(fn);
	}

	public LinkedList getHistory() {
		return history;
	}

	/**
	 * Fügt dem Thema eine neue Übung hinzu.
	 * 
	 * @param e
	 *            Exercise-Objekt das hinzugefügt werden soll
	 */
	public void addExercise(Exercise e) {
		exercises.add(e);
		e.setParent(this);
	}

	/**
	 * Gibt einen Iterator zum Durchlaufen der gespeicherten Elemente zurück.
	 * 
	 * @return Iterator zum Ansprechen der Elemente
	 */
	public Iterator getExerciseIterator() {
		return exercises.iterator();
	}//getExerciseIterator

	/**
	 * Gibt eine bestimmte Aufgabe zurück
	 * 
	 * @param n
	 *            Stelle der Übung
	 */
	public Exercise getExercise(int n) {
		return (Exercise) exercises.get(n);
	}//getExercise

	/**
	 * Erzeugt die Darstellung und speichert diese intern.
	 */
	protected void createView() {
	}//creatView

	public int compareTo(Object o) {
		if (!(o instanceof EIssue))
			return -1;
		else
			return title.compareTo(((EIssue) o).getTitle());
	}//compareTo

	/**
	 * @param interface1
	 */
	public void setStatus(FileStatusInterface interface1) {
		status = interface1;
	}

}//class EIssue
