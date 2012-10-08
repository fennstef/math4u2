package math4u2.controller.relation;

/**
 * @author Fenn Stefan
 */
public class RelationException extends Exception {

	public RelationException() {
		super();
	} //Konstruktor 1

	public RelationException(String message) {
		super(message);
	} //Konstruktor 2

	public RelationException(Throwable t) {
		super(t);
	}//Konstruktor3

	public RelationException(String message, Throwable t) {
		super(message, t);
	}//Konstruktor4

} //class BrokerException
