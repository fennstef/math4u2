package math4u2.util.exception;

/**
 * Wenn etwas nicht gefunden wurde, kann diese Exception benutzt werden
 * @author Fenn Stefan
 */
public class NotFoundException extends Exception {

	public NotFoundException() {
		super();
	}

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundException(Throwable cause) {
		super(cause);
	}

}//class NotFoundException
