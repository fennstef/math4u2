package math4u2.controller.relation;

/**
 * Diese Exception wird geworfen, wenn eine ungültige Beziehung erzeugt wird
 * oder eine Beziehung falsch eingetragen wird.
 * 
 * @author Fenn Stefan
 */

public class IllegalRelationConnectionException extends RuntimeException {

	public IllegalRelationConnectionException() {
		super();
	} //Konstruktor 1

	public IllegalRelationConnectionException(String message) {
		super(message);
	} //Konstruktor 2

} //class BrokerException
