package math4u2.controller;

import java.util.*;

/**
 * Wenn der Broker ein Objekt veröffentlich oder löscht, wird ein BrokerEvent
 * erzeugt.
 * 
 * @author Fenn Stefan
 */
public class BrokerEvent extends EventObject {
	protected String key;

	public BrokerEvent(Object source, String key) {
		super(source);
		this.key = key;
	}//Konstruktor

	/**
	 * Gibt den Schlüssel des Objekts zurück
	 */
	public String getKey() {
		return key;
	}//getName

}//class BrokerEvent
