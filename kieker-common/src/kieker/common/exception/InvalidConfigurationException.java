package kieker.common.exception;

/**
 * This exception should be thrown if an illegal configuration (parameter) is detected by a configurable component.
 * 
 * @author Holger Knoche
 * @since 1.13
 *
 */
public class InvalidConfigurationException extends RuntimeException {

	private static final long serialVersionUID = -7683665726823960846L;

	/**
	 * Creates a new exception with the given message.
	 * @param message The message to associate with this exception
	 */
	public InvalidConfigurationException(final String message) {
		super(message);
	}
	
	/**
	 * Creates a new exception with the given message and cause.
	 * @param message The message to associate with this exception
	 * @param cause The cause for this exception
	 */
	public InvalidConfigurationException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
}
