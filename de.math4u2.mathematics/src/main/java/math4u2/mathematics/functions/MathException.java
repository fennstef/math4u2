package math4u2.mathematics.functions;

/**
 * Exception wird geworfen bei Division durch 0, wenn Variablen nicht definiert
 * sind, etc.
 */
public class MathException extends Exception {

	/**
	 *  
	 */
	public MathException() {
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MathException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public MathException(Throwable cause) {
		super(cause);
	}

	/**
	 * Konstruktor
	 * 
	 * @param desc
	 *            Erlaeuterungen
	 */
	public MathException(String desc) {
		super(desc);
	}
}