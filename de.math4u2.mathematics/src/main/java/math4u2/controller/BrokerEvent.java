package math4u2.controller;

import java.util.*;

/**
 * Wenn der Broker ein Objekt ver�ffentlich oder l�scht, wird ein BrokerEvent
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
	 * Gibt den Schl�ssel des Objekts zur�ck
	 */
	public String getKey() {
		return key;
	}//getName

}//class BrokerEvent
