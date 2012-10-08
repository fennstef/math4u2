package math4u2.controller.relation;

/**
 * Diese Exception wird geworfen, wenn ein Beziehung nicht gel�scht werden kann.
 * 
 * @author Fenn Stefan
 */
public class DeleteRelationException extends RelationException {

	public DeleteRelationException() {
		super();
	} //Konstruktor 1

	public DeleteRelationException(String message) {
		super(message);
	} //Konstruktor 2

} //class BrokerException
