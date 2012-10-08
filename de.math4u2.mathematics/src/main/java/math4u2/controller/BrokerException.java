package math4u2.controller;

/**
 * @author Fenn Stefan
 */

public class BrokerException extends Exception {

	public BrokerException() {
		super();
	} //Konstruktor 1

	public BrokerException(String message) {
		super(message);
	} //Konstruktor 2

	public BrokerException(Throwable t) {
		super(t);
	}//Konstruktor3

	public BrokerException(String message, Throwable t) {
		super(message, t);
	}//Konstruktor4

} //class BrokerException
