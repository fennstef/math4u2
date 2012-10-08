package math4u2.controller.relation;

/**
 * Diese Exception wird geworfen, wenn das Objekt nicht in der Beziehung
 * vorkommt.
 * 
 * @author Fenn Stefan
 */
public class ObjectNotInRelationException extends RelationException {

	public ObjectNotInRelationException() {
		super();
	} //Konstruktor 1

	public ObjectNotInRelationException(String message) {
		super(message);
	} //Konstruktor 2

	public ObjectNotInRelationException(String message, Throwable t) {
		super(message, t);
	} //Konstruktor 2

} //class BrokerException
