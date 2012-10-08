package math4u2.controller.reference;

/**
 * Wenn der jeweilige Pfad nicht aufgebaut werden konnte,
 * wird eine CreatePathException geworfen.
 * 
 * @author Fenn Stefan
 */
public class CreatePathException extends Exception {

	public CreatePathException() {
		super();
	}

	public CreatePathException(String message) {
		super(message);
	}

	public CreatePathException(Throwable cause) {
		super(cause);
	}

	public CreatePathException(String message, Throwable cause) {
		super(message, cause);
	}

}
